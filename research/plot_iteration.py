"""Export standalone research figures from a completed, saved analysis.

Use the pinned optional plotting environment in research/plot-requirements.txt.
Figures are artifacts for review/slides; the explorer remains a numeric table.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import platform

import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy as np

from research.import_evidence import ROOT, canonical, digest
from research.scientific_summary import ISSUES

LABELS = {'none': 'None', 'overview': 'Overview', 'requirements': 'Requirements', 'boundaries': 'Trust boundaries'}
CHECK_LABELS = ['Negative score', 'Negative time', 'Null name', 'Blank name', 'Excessive name', 'Retained entries',
                'Malformed store', 'Oversized line', 'Deserialization hook', 'Large record set']


def render(identifier, qualified=False):
    directory = ROOT / 'research/iterations' / identifier
    source = directory / ('qualified-analysis.json' if qualified else 'analysis.json'); raw = source.read_bytes(); data = json.loads(raw)
    target = directory / ('figures-qualified' if qualified else 'figures'); target.mkdir(exist_ok=True)
    plt.rcParams.update({'font.family': 'DejaVu Sans', 'font.size': 10, 'axes.spines.top': False, 'axes.spines.right': False,
                         'svg.hashsalt': identifier, 'pdf.fonttype': 42, 'ps.fonttype': 42})
    parents = list(dict.fromkeys(c['parent'] for c in data['comparisons']))
    by_condition = {c['condition']: c for c in data['conditions']}
    sizes = sorted({c['n'] for c in data['conditions']})
    n_label = str(sizes[0]) if len(sizes) == 1 else '/'.join(map(str, sizes))
    budget = json.loads((directory / 'plan.json').read_text())['maxSubmissions']
    def parent_label(parent):
        row = by_condition[parent + '__none']
        return row['method'] + ' · ' + row['paperContext']
    generated = []
    def save(fig, name):
        for extension in ('pdf', 'svg', 'png'):
            path = target / f'{name}.{extension}'
            metadata = {'CreationDate': None, 'ModDate': None} if extension == 'pdf' else {'Date': None} if extension == 'svg' else None
            fig.savefig(path, dpi=200, metadata=metadata, facecolor='white')
            generated.append(path)
        plt.close(fig)

    fig, axes = plt.subplots(len(parents), 2, figsize=(11.5, 3 * len(parents) + 1.2), squeeze=False)
    for index, parent in enumerate(parents):
        rows = [by_condition[parent + '__' + strategy] for strategy in LABELS]
        labels = [LABELS[c['securityStrategy']] for c in rows]
        for side in (0, 1):
            ax = axes[index, side]; ax.set_yticks(range(len(rows)), labels); ax.invert_yaxis(); ax.grid(axis='x', color='.90', linewidth=.7); ax.set_axisbelow(True)
        quality, security = axes[index]
        quality.set_title(parent_label(parent) + ' · functionality', loc='left', fontsize=11)
        security.set_title('Issue-check counts', loc='left', fontsize=11)
        for y, row in enumerate(rows):
            rate = 100 * row['withinBudgetFullRate']; lo, hi = row['withinBudgetFullWilson95']
            quality.errorbar(rate, y, xerr=[[rate - 100 * lo], [100 * hi - rate]], fmt='o', color='#333333', capsize=3, markersize=5)
            quality.scatter(100 * row['firstFullRate'], y + .15, marker='x', color='#999999', s=25)
            lo, hi = row['failureCountIdentificationBoundsPerTrajectory']
            security.plot([lo, hi], [y, y], color='#71559b', linewidth=3)
            security.scatter([lo], [y], color='#71559b', s=26)
            if hi > lo: security.scatter([hi], [y], facecolor='white', edgecolor='#71559b', s=26)
            security.text(10.25, y, f"{row['issues']['failed']}/{row['issues']['evaluated']}", va='center', ha='left', fontsize=9)
        quality.set_xlim(-3, 105); quality.set_xticks([0, 25, 50, 75, 100]); quality.set_xlabel('Full functional success (%)')
        security.set_xlim(-.2, 11.7); security.set_xticks(range(0, 11, 2)); security.set_xlabel('Failed issue checks per trajectory; unresolved bounds')
    fig.subplots_adjust(left=.16, right=.97, top=.93, bottom=.23, wspace=.48, hspace=.65)
    fig.text(.02, .055, 'Functionality: filled dot = within budget; cross = first submission; bars = marginal Wilson 95% intervals.\n'
             'Security: filled dot = observed failures; open dot = all unresolved checks failing. This range is not a confidence interval.\n'
             f"{identifier}{' (qualified)' if qualified else ''}; N={n_label} trajectories per combination; at most {budget} submissions each. Fixed task and acquired contexts.", fontsize=9, va='bottom')
    save(fig, 'quality-and-security')

    columns = min(2, len(parents)); rows = (len(parents) + columns - 1) // columns
    fig, axes = plt.subplots(rows, columns, figsize=(12, 4.8 * rows + 2), squeeze=False)
    cmap = plt.get_cmap('RdBu_r').copy(); cmap.set_bad('#e8e8e8')
    for column, parent in enumerate(parents):
        comparisons = [next(c for c in data['comparisons'] if c['parent'] == parent and c['securityStrategy'] == strategy) for strategy in list(LABELS)[1:]]
        values = np.array([[np.nan if c['tests'][row]['failureRateDelta'] is None else 100 * c['tests'][row]['failureRateDelta'] for c in comparisons] for row in range(len(ISSUES))])
        ax = axes.flat[column]; shown = ax.imshow(values, cmap=cmap, vmin=-100, vmax=100, aspect='auto')
        ax.set_title(parent_label(parent), fontsize=11)
        ax.set_xticks(range(3), ['Overview', 'Requirements', 'Boundaries'])
        ax.set_yticks(range(len(ISSUES)), CHECK_LABELS if column % columns == 0 else [])
        for row in range(len(ISSUES)):
            for x in range(3):
                value = values[row, x]
                ax.text(x, row, '?' if np.isnan(value) else f'{value:+.0f}' if value != 0 else '0', ha='center', va='center', color='white' if not np.isnan(value) and abs(value) >= 70 else '#222222', fontsize=10)
        ax.tick_params(length=0)
    for ax in list(axes.flat)[len(parents):]: ax.set_visible(False)
    fig.subplots_adjust(left=.19, right=.96, top=.94, bottom=.26 if rows == 1 else .18, wspace=.1, hspace=.28)
    color_axis = fig.add_axes([.28, .16 if rows == 1 else .12, .5, .02]); fig.colorbar(shown, cax=color_axis, orientation='horizontal', ticks=[-100, -50, 0, 50, 100]).set_label('Failure-rate difference from fresh control (percentage points)')
    fig.text(.04, .025, 'Negative = fewer failed checks. ? = at least one unresolved observation in either arm; no directional estimate.\n'
             f'Each check is measured across N={n_label} trajectories per arm. Checks within an artifact are correlated; no significance claim.\n'
             'These contracts and finite fixtures do not enumerate all vulnerabilities.', fontsize=9, va='bottom')
    save(fig, 'per-check-security-effects')
    provenance = {'iteration': identifier, 'analysisSha256': digest(raw), 'rendererSha256': digest(Path(__file__).read_bytes()),
        'requirementsSha256': digest((ROOT / 'research/plot-requirements.txt').read_bytes()), 'python': platform.python_version(),
        'matplotlib': matplotlib.__version__, 'numpy': np.__version__, 'outputs': {p.name: digest(p.read_bytes()) for p in generated}}
    (target / 'provenance.json').write_bytes(canonical(provenance))
    print(f'Exported {len(generated)} figures with data, renderer and dependency fingerprints')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__); parser.add_argument('--iteration', required=True)
    parser.add_argument('--qualified', action='store_true'); args = parser.parse_args()
    render(args.iteration, args.qualified)
