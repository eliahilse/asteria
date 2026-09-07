"""Descriptive acquisition measurements; identifier changes do not prove semantic claim loss."""
from __future__ import annotations


def diagnose(record: dict) -> dict:
    turns = record.get('turns', [])
    candidates = [t['candidateOutput'] for t in turns if 'candidateOutput' in t]
    first = candidates[0]['items'] if candidates else []
    final = (record.get('output') or {}).get('items', [])
    first_ids, final_ids = {i['id'] for i in first}, {i['id'] for i in final}
    return {'id': record['id'], 'strategy': record['strategy'], 'turns': len(turns),
            'sourceFiles': record['sourceFiles'], 'sourceLines': record['sourceLines'],
            'inspectedFiles': record.get('inspectedFiles', 0), 'inspectedLines': record.get('inspectedLines', 0),
            'toolErrorTurns': sum('error' in t.get('toolResult', {}) for t in turns),
            'citationFeedbackTurns': sum(bool(t.get('toolResult', {}).get('citationErrors')) for t in turns),
            'candidateOutputs': len(candidates), 'firstCandidateItems': len(first) if candidates else None,
            'finalItems': len(final) if record.get('output') else None,
            'firstCandidateIdsAbsentFromFinal': sorted(first_ids - final_ids),
            'newFinalIds': sorted(final_ids - first_ids),
            'interpretation': 'Counts describe this acquisition only. Changed or missing IDs are not automatically semantic claim loss; inspect original candidates and final statements.'}
