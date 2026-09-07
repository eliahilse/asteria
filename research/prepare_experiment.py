"""Freeze exact prompts and the schedule before submitting any Luna experiment."""
from __future__ import annotations
import argparse
import csv
import json
import random
import re
from pathlib import Path
from research.import_evidence import ROOT, canonical, digest, MEETING
from research.extract_context import render


def prepare(output: Path, bridge_repetitions: int = 5, ablation_repetitions: int = 10) -> dict:
    if bridge_repetitions < 1 or ablation_repetitions < 1: raise ValueError('Repetitions must be positive')
    output.mkdir(parents=True,exist_ok=True)
    contexts_path=ROOT/'research/results/security-context-v1.json';contexts=json.loads(contexts_path.read_text())
    check=dict(contexts);fingerprint=check.pop('fingerprint')
    if digest(canonical(check))!=fingerprint:raise ValueError('Context fingerprint mismatch')
    for path,sha in contexts['inputHashes'].items():
        if digest((ROOT/path).read_bytes())!=sha:raise ValueError(f'Context extractor input changed: {path}; regenerate contexts')
    with (ROOT/'vamos-artifact/Pipeline/Prompts.csv').open(encoding='utf-8-sig') as source:
        rows=list(csv.DictReader(source))
    conditions=[];prompts={}
    def condition(id,stage,strategy,base,types,prompt,facts,repetitions,source):
        sha=digest(prompt.encode());prompts[sha]=prompt
        conditions.append({'id':id,'stage':stage,'strategy':strategy,'baseContext':base,'contextTypes':types,'factIds':facts,'factCount':len(facts),
            'promptSha256':sha,'promptCharacters':len(prompt),'promptBytes':len(prompt.encode()),'promptTokens':None,
            'repetitions':repetitions,'promptFile':f'prompts/{sha}.txt','source':source})
    for strategy,stem in [('Generation','generation_s'),('Reuse','reuse_sfb')]:
        baseline=(ROOT/MEETING/'prompts'/f'{stem}.txt').read_text()
        for secured in (False,True):
            name=stem+('_security' if secured else '')
            path=ROOT/MEETING/'prompts'/f'{name}.txt'
            condition('bridge_'+name,'bridge',strategy,'S' if strategy=='Generation' else 'S+F+B',['legacy_mixed'] if secured else [],path.read_text(),
                      [f'legacy-{i}' for i in range(1,6)] if secured else [],bridge_repetitions,str(path.relative_to(ROOT)))
        row=next(r for r in rows if r['Task']=='Highscore' and r['Method']==strategy and not any(r[k].strip() for k in ('Structural','Functional','Behavioral')))
        task=row['Prompt'].replace('\\n','\n')
        attachments=re.findall(r'\n\n--- BEGIN ATTACHED (?:TARGET SOURCE|REUSE SOURCE): .*?--- END ATTACHED (?:TARGET SOURCE|REUSE SOURCE): [^\n]+ ---',baseline,re.S)
        if len(attachments)!=len([ref for key in ('Reuse Files','Source Code') for ref in row[key].split(';') if ref.strip()]):raise ValueError(f'Unexpected source attachment count for {strategy}: {len(attachments)}')
        base=task+''.join(attachments)
        suffix='\n\n--- OUTPUT FORMAT ---\nOutput only complete Java source files. Wrap each file in a fenced block: ```java filename=ClassName.java. Preserve complete modified classes.\n'
        variants=[('none',[]),('c1',['C1']),('c2',['C2']),('c3',['C3']),('c4',['C4']),('all',['C1','C2','C3','C4'])]
        for label,types in variants:
            facts=[f['id'] for f in contexts['facts'] if f['scope']=='feature' and f['type'] in types]
            # Same threat-model disclosure in all ablation arms, including zero-fact control.
            intervention=render(contexts,types,'feature')
            prompt=base+'\n\n'+intervention+suffix
            condition(f'{strategy.lower()}_security_{label}','ablation',strategy,'none',types,prompt,facts,ablation_repetitions,'VaMoS no-context task + archived source attachments + frozen extraction')
    randomizer=random.Random(20260907);schedule=[]
    for stage in ('bridge','ablation'):
        group=[c for c in conditions if c['stage']==stage]
        for repetition in range(1,max(c['repetitions'] for c in group)+1):
            block=[{'runId':f"luna_medium__{c['id']}__r{repetition}",'condition':c['id'],'stage':stage,'repetition':repetition,'status':'planned'} for c in group if repetition<=c['repetitions']]
            randomizer.shuffle(block);schedule.extend(block)
    manifest={'schemaVersion':1,'id':'luna-highscore-v1','status':'planned','model':'gpt-5.6-luna','reasoning':'medium','temperature':None,'maxOutputTokens':65536,
        'contextFingerprint':contexts['fingerprint'],'sourceHashes':{str(p.relative_to(ROOT)):digest(p.read_bytes()) for p in [Path(__file__),contexts_path,ROOT/'vamos-artifact/Pipeline/Prompts.csv']},
        'scope':'feature','injection':'once before generation','scheduleSeed':20260907,'conditions':conditions,'schedule':schedule,
        'analysis':{'primary':'Full functional success and all declared security properties pass / all attempts. Missing checks cannot establish success.',
                    'secondary':['Main compilation / all attempts','Per-property pass, fail, unknown and unevaluated counts','Source-reviewed findings with reviewed denominator','Context facts, prompt tokens, output tokens and observed cost'],
                    'sampleCaveat':'Exploratory sizes, not a statistical power calculation. No claim of generalization from one feature.',
                    'controls':'No prior model results or exact adversarial fixture data are provided in the ablation prompts.',
                    'confounds':'The ablation fixes the legacy conflicting output suffix for every arm. Bridge prompts are exact historical payloads. Do not attribute bridge-to-ablation differences solely to context.',
                    'timing':'Randomized block schedule reduces temporal confounding. Schedule seed is not a model sampling seed.'}}
    manifest['fingerprint']=digest(canonical(manifest))
    path=output/'manifest.json'
    if path.exists() and path.read_bytes()!=canonical(manifest):raise FileExistsError('Frozen manifest differs; choose a new output directory.')
    (output/'prompts').mkdir(exist_ok=True)
    for sha,text in prompts.items():(output/'prompts'/f'{sha}.txt').write_text(text)
    path.write_bytes(canonical(manifest));return manifest


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',type=Path,default=ROOT/'research/experiments/luna-highscore-v1');args=parser.parse_args()
    report=prepare(args.output);print(f"{len(report['conditions'])} conditions; {len(report['schedule'])} planned attempts; {report['fingerprint']}")
