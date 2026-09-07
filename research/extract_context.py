"""Extract versioned security context from Java syntax and preserved audit records.

C1: observed syntax. C2: local syntactic flow candidates. C3: historical audit
leads, with original validation status. C4: explicit, versioned study policy.
No LLM inference, dependency vulnerability lookup or resolved whole-program taint.
"""
from __future__ import annotations
import argparse
import base64
import collections
import json
import re
import subprocess
import tempfile
import zipfile
from pathlib import Path

from research.import_evidence import ROOT, canonical, digest
from research.evaluate_security import find_jdk

AST_SOURCE = ROOT / "research/context/AstFacts.java"
POLICY = ROOT / "research/context/policy.json"
CATEGORIES = [
    ("network", r"\b(?:URL|URLConnection|HttpURLConnection|Socket|ServerSocket)\b|\.openConnection\(|https?://", ["CWE-319"]),
    ("persistence", r"\b(?:FileInputStream|FileOutputStream|FileReader|FileWriter|DataInputStream|DataOutputStream|BufferedReader|Scanner)\b|\bFiles\.(?:read|write|new)", ["CWE-20", "CWE-400"]),
    ("serialization", r"\bObjectInputStream\b|\.readObject\(", ["CWE-502"]),
    ("cryptography", r"\b(?:MessageDigest|Cipher|Mac|SecretKey|KeyGenerator|SecureRandom)\b", ["CWE-328"]),
    ("code_loading", r"\bURLClassLoader\b|\.(?:loadClass|defineClass)\(|\bClass\.forName\(", ["CWE-470", "CWE-829"]),
    ("native_loading", r"\bSystem\.load(?:Library)?\(", ["CWE-114"]),
]


def extract(root: Path = ROOT) -> dict:
    jdk = find_jdk()
    manifest: dict[str, dict] = {}
    with tempfile.TemporaryDirectory(prefix="asteria-context-") as directory:
        temp = Path(directory); sources = temp / "sources"; classes = temp / "classes"; sources.mkdir(); classes.mkdir()
        def source(key: str, raw: bytes, origin: str, member: str | None = None):
            try: text = raw.decode("utf-8"); encoding="utf-8"
            except UnicodeDecodeError: text=raw.decode("latin-1"); encoding="latin-1"
            path=sources/key;path.parent.mkdir(parents=True,exist_ok=True);path.write_text(text,encoding="utf-8")
            manifest[key]={"path": origin, "member": member, "sha256": digest(raw), "parserEncoding": encoding, "lines": len(text.splitlines())}
        mario=Path("apogames/Java/ApoMario/ApoMario.jar")
        with zipfile.ZipFile(root/mario) as archive:
            for name in sorted(archive.namelist()):
                if name.endswith('.java'):
                    if Path(name).is_absolute() or '..' in Path(name).parts: raise ValueError(f"Unsafe archive entry {name}")
                    source(f"ApoMario/{name}",archive.read(name),str(mario),name)
        for path in sorted((root/'apogames/Java/ApoIcarus').rglob('*.java')):
            source(f"ApoIcarus/{path.relative_to(root/'apogames/Java/ApoIcarus')}",path.read_bytes(),str(path.relative_to(root)))
        subprocess.run([str(jdk/'javac'),'-d',str(classes),str(AST_SOURCE)],check=True,capture_output=True,text=True)
        result=subprocess.run([str(jdk/'java'),'-cp',str(classes),'research.context.AstFacts',str(sources)],check=True,capture_output=True,text=True,timeout=120)
        events=[]
        for line in result.stdout.splitlines():
            kind,file,method,number,text,origins=[base64.b64decode(x).decode('utf-8') for x in line.split('\t')]
            events.append({"kind":kind,"file":file,"method":method,"line":int(number),"text":text,"origins":origins})
    facts=[]
    def add(type: str, category: str, text: str, cwes: list[str], source: dict, status: str, scope: str, **extra):
        fact={"type":type,"category":category,"text":text,"cwes":cwes,"source":source,"status":status,"scope":scope,**extra}
        fact['id']=f"{type.lower()}-{digest(canonical(fact))[:16]}";facts.append(fact)
    for event in events:
        if event['kind'] in ('parsed','parse_error'):continue
        location={**manifest[event['file']],"unit":event['file'],"line":event['line'],"method":event['method'],"snippet":event['text']}
        scope='feature' if 'highscore' in event['file'].lower() else 'repository'
        if event['kind']=='flow_candidate':
            add('C2','input_flow',f"Local flow candidate in {event['method']}: {event['origins']} → {event['text']}. Receiver types, reachability and guard effectiveness are unresolved.",['CWE-20','CWE-400'],location,'syntactic_flow_candidate',scope,origins=event['origins']);continue
        for category,pattern,cwes in CATEGORIES:
            if re.search(pattern,event['text']):
                add('C1',category,f"Observed {event['kind']} in {event['method']}: {event['text']}. Presence alone does not establish a vulnerability or reachable behavior.",cwes,location,'observed_syntax',scope)
        if event['kind']=='import' and not re.match(r'^(java|javax|apoMario|apoJump|org\.apogames|test)\.',event['text']):
            add('C1','dependency',f"Imported namespace: {event['text']}. Dependency identity/version and vulnerability status are unresolved.",[],location,'observed_import',scope)
    audit_path=Path('audit/findings/reconciled.json')
    for finding in json.loads((root/audit_path).read_text()):
        if finding['variant'] not in ('ApoMario','ApoIcarus'):continue
        matched=next((key for key in manifest if key.startswith(finding['variant']+'/') and Path(key).name==finding['file']),None)
        if matched is None:continue
        record={"path":str(audit_path),"sha256":digest((root/audit_path).read_bytes()),"record":f"{finding['variant']}:{finding['file']}:{finding['category']}","reportedCode":manifest[matched],"reportedLine":finding.get('line')}
        add('C3',finding['category'],f"Historical audit lead: {finding['category']} in {finding['variant']}/{finding['file']}; {finding['cwe']}. Original status {finding['status']}, reported reachable={finding['reachable']}. Source matching is automatic; the reported vulnerability and server behavior are not revalidated here.",re.findall(r'CWE-\d+',finding['cwe']),record,'historical_'+finding['status'].lower(),'feature' if 'highscore' in matched.lower() else 'repository')
    policy=json.loads(POLICY.read_text())
    for requirement in policy['requirements']:
        add('C4','requirement',requirement['text'],requirement['cwes'],{"path":str(POLICY.relative_to(ROOT)),"sha256":digest(POLICY.read_bytes()),"rule":requirement['id']},'declared_policy','feature')
    facts=sorted({f['id']:f for f in facts}.values(),key=lambda f:(f['type'],f['scope'],f['id']))
    report={"schemaVersion":1,"extractor":"java-ast-context-v1","policy":policy,"facts":facts,
        "sourceManifest":manifest,"inputHashes":{str(p.relative_to(ROOT)):digest(p.read_bytes()) for p in [Path(__file__),AST_SOURCE,POLICY,root/audit_path,root/mario]},
        "coverage":{"sourceFiles":len(manifest),"parsedFiles":sum(e['kind']=='parsed' for e in events),"parseErrors":[e for e in events if e['kind']=='parse_error'],"factsByType":dict(collections.Counter(f['type'] for f in facts))},
        "limitations":["Syntax is parsed with javac; receiver types and calls are not semantically resolved.","C2 tracks direct method-local assignments and arguments in source order. It does not establish interprocedural, alias-aware, path-sensitive or whole-program taint.","C3 imports audit leads with original status. Cross-model agreement is not executable validation.","C4 is a declared policy. The requirement to distrust editable files is not automatically inferred from Java syntax.","Feature scope selects Highscore-named source files. It is a deterministic filename projection, not a resolved dependency slice.","CWE tags identify review categories, not confirmed weaknesses; an MD5 API may serve a non-security purpose.","No external vulnerability database, dependency version resolution or runtime tracing was performed."]}
    report['fingerprint']=digest(canonical(report));return report


def render(report: dict, types: list[str], scope: str = 'feature') -> str:
    selected=[f for f in report['facts'] if f['type'] in types and (scope=='repository' or f['scope']=='feature')]
    lines=['--- BEGIN SECURITY CONTEXT ---', 'Threat model: '+report['policy']['threat_model']]
    for f in selected:
        origin=f['source'];locator=origin['path']+('!'+origin['member'] if origin.get('member') else '')
        if origin.get('line'):locator+=':'+str(origin['line'])
        lines.append(f"[{f['id']} | {f['type']} | {f['status']}] {f['text']} (source: {locator})")
    lines.append('--- END SECURITY CONTEXT ---');return '\n'.join(lines)


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',type=Path,default=ROOT/'research/results/security-context-v1.json');args=parser.parse_args()
    report=extract();args.output.parent.mkdir(parents=True,exist_ok=True);args.output.write_bytes(canonical(report));print(json.dumps(report['coverage'],indent=2));print(report['fingerprint'])
