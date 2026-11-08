from pathlib import Path
import collections, hashlib, json, re

ROOT = Path(__file__).resolve().parent.parent
FILES = ['bank_soal_3000_DRAFT.json', 'bank_soal_3000_BATCH02_DRAFT.json', 'bank_soal_3000_BATCH03_DRAFT.json']
OUT = ROOT / 'data' / 'bank_soal_revisi'

def normalized(value):
    return re.sub(r'\s+', ' ', str(value).strip()).casefold()

def fingerprint(row):
    # Template group plus the same five answer texts signals potential variants,
    # not proof that questions are identical. Do not use this for automatic deletion.
    options = sorted(normalized(row['option_' + c]) for c in 'abcde')
    if row['category'] in ('TWK', 'TKP') and row.get('origin_template'):
        parts = [row['category'], row['origin_template'], *options]
    else:
        parts = [row['category'], normalized(row['question_text']), *options]
    return hashlib.sha256('\x1f'.join(parts).encode()).hexdigest()[:16]

def main():
    OUT.mkdir(parents=True, exist_ok=True)
    datasets = [(name, json.loads((ROOT / name).read_text(encoding='utf-8-sig'))) for name in FILES]
    allrows = [row for _, rows in datasets for row in rows]
    groups = collections.Counter(fingerprint(row) for row in allrows)
    mismatch = 0
    for name, rows in datasets:
        for row in rows:
            group = fingerprint(row)
            row['audit_group'] = group
            row['audit_group_size'] = groups[group]
            row['audit_status'] = 'PERLU_TELAAH_AHLI'
            row['review_status'] = 'DRAFT'
            if row['category'] == 'TKP':
                old = dict(re.findall(r'\b([A-E])\s*=\s*([1-5])\s*poin', row.get('explanation', ''), flags=re.I))
                mismatch += bool(old) and any(int(old.get(c.upper(), -1)) != int(row['score_' + c]) for c in 'abcde')
                mapping = '; '.join(f'{c.upper()} = {int(row["score_" + c])} poin' for c in 'abcde')
                row['explanation'] = ('Bobot latihan untuk opsi pada urutan di atas: ' + mapping + '. '
                    'Skor tertinggi menunjukkan respons yang dinilai paling sesuai dalam skenario ini. '
                    'Penilaian ini bersifat draf dan memerlukan telaah ahli TKP beserta alasan tiap opsi.')
                row['review_note'] = str(row.get('review_note') or '') + '; PEMBAHASAN_DISESUAIKAN_DENGAN_BOBOT_OPSI; BOBOT_WAJIB_TELAAH_AHLI'
        target = OUT / name.replace('_DRAFT.json', '_REVISI_DRAFT.json')
        target.write_text(json.dumps(rows, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    assert sum(len(rows) for _, rows in datasets) == 9000
    for row in allrows:
        assert row['question_text'].strip() and row['explanation'].strip()
        assert all(str(row['option_' + c]).strip() for c in 'abcde')
        scores = [int(row['score_' + c]) for c in 'abcde']
        if row['category'] == 'TKP': assert sorted(scores) == [1, 2, 3, 4, 5]
        else:
            assert row['correct_answer'] in 'ABCDE'
            assert scores == [5 if c.upper() == row['correct_answer'] else 0 for c in 'abcde']
    print(json.dumps({'jumlah':len(allrows), 'ketidakcocokan_pembahasan_tkp_diperbaiki':mismatch,
        'kelompok_potensi_variasi':sum(n > 1 for n in groups.values()),
        'baris_dalam_kelompok_potensi_variasi':sum(n for n in groups.values() if n > 1),
        'output':str(OUT)}, ensure_ascii=False, indent=2))

if __name__ == '__main__': main()
