import Link from "next/link";

const options = [
  ["A", "Membantu peserta memahami langkah yang harus dilakukan."],
  ["B", "Menyelesaikan proses sesuai prosedur yang berlaku."],
  ["C", "Menunda keputusan sampai mendapat arahan tambahan."],
  ["D", "Mengabaikan pertanyaan agar waktu tidak terbuang."],
  ["E", "Mencatat kebutuhan peserta untuk ditindaklanjuti."],
];

export default function ExamPreview() {
  return <main className="exam-shell">
    <header className="exam-header">
      <Link href="/" className="exam-brand"><span className="exam-mark">N</span><span><b>CAT NUSANTARA</b><small>SIMULASI SKD</small></span></Link>
      <div className="exam-identity"><span>Peserta demo</span><small>TRYOUT SKD · PREVIEW</small></div>
      <div className="exam-clock"><small>SISA WAKTU</small><strong>01:39:42</strong></div>
    </header>
    <div className="exam-progress"><span className="exam-progress-label">SIMULASI SKD</span><span>Soal 12 dari 110</span><span>11 terjawab</span><div className="exam-progress-track"><i /></div></div>
    <section className="exam-workspace">
      <article className="exam-question-panel">
        <div className="exam-question-meta"><span className="question-number">SOAL 012</span><span className="question-category">TWK · KEWARGANEGARAAN</span></div>
        <h1>Dalam pelayanan publik, sikap yang paling sesuai dengan prinsip akuntabilitas adalah ...</h1>
        <p className="exam-instruction">Pilih satu jawaban yang paling tepat.</p>
        <div className="exam-options">{options.map(([label, text]) => <label className="exam-option" key={label}><input type="radio" name="answer" /><span className="option-label">{label}</span><span>{text}</span></label>)}</div>
        <div className="exam-actions"><button className="exam-secondary" type="button">← Sebelumnya</button><button className="exam-skip" type="button">Lewati soal</button><button className="exam-primary" type="button">Simpan & lanjutkan <span>→</span></button></div>
      </article>
      <aside className="exam-sidebar" aria-label="Navigasi soal">
        <div className="exam-sidebar-head"><h2>Navigasi soal</h2><span>12 / 110</span></div>
        <div className="exam-legend"><span><i className="legend-current" />Aktif</span><span><i className="legend-answered" />Terjawab</span><span><i className="legend-empty" />Belum</span></div>
        <div className="exam-grid">{Array.from({ length: 30 }, (_, i) => <button key={i} className={`exam-number ${i === 11 ? "is-current" : i < 11 ? "is-answered" : ""}`} type="button">{String(i + 1).padStart(2, "0")}</button>)}</div>
        <div className="exam-sidebar-footer"><p>Komposisi soal</p><div><span>TWK <b>30</b></span><span>TIU <b>35</b></span><span>TKP <b>45</b></span></div><button className="exam-finish" type="button">Akhiri simulasi</button></div>
      </aside>
    </section>
  </main>;
}
