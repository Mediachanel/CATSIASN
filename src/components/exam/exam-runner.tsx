"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

type Question = { position: number; prompt: string; category: string; topic: string; options: Array<{ label: string; text: string }>; selectedOption: string | null };
type Overview = { position: number; selectedOption: string | null };

export function ExamRunner({ sessionId, deadlineAt, eventTitle, initialQuestion, overview }: { sessionId: string; deadlineAt: string; eventTitle: string; initialQuestion: Question; overview: Overview[] }) {
  const router = useRouter();
  const [question, setQuestion] = useState(initialQuestion);
  const [answers, setAnswers] = useState(overview);
  const [saving, setSaving] = useState<"saved" | "saving" | "error">("saved");
  const [seconds, setSeconds] = useState(() => Math.max(0, Math.floor((new Date(deadlineAt).getTime() - Date.now()) / 1000)));
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const timer = window.setInterval(() => setSeconds(Math.max(0, Math.floor((new Date(deadlineAt).getTime() - Date.now()) / 1000))), 1000);
    return () => window.clearInterval(timer);
  }, [deadlineAt]);

  const clock = `${String(Math.floor(seconds / 3600)).padStart(2, "0")}:${String(Math.floor((seconds % 3600) / 60)).padStart(2, "0")}:${String(seconds % 60).padStart(2, "0")}`;
  async function open(position: number) { const response = await fetch(`/api/v1/exam/sessions/${sessionId}/questions/${position}`, { cache: "no-store" }); if (response.ok) setQuestion(await response.json()); }
  async function choose(selectedOption: string) {
    setQuestion(current => ({ ...current, selectedOption }));
    setAnswers(current => current.map(answer => answer.position === question.position ? { ...answer, selectedOption } : answer));
    setSaving("saving");
    const response = await fetch(`/api/v1/exam/sessions/${sessionId}/answers`, { method: "PUT", headers: { "Content-Type": "application/json" }, body: JSON.stringify({ position: question.position, selectedOption }) });
    setSaving(response.ok ? "saved" : "error");
  }
  async function submit() {
    if (!window.confirm("Akhiri simulasi? Jawaban yang sudah tersimpan akan dinilai.")) return;
    setSubmitting(true);
    const response = await fetch(`/api/v1/exam/sessions/${sessionId}/submit`, { method: "POST" });
    if (response.ok) router.push(`/results/${sessionId}`);
    else { setSubmitting(false); setSaving("error"); }
  }

  return <main className="exam-shell"><header className="exam-header"><div className="exam-brand"><span className="exam-mark">N</span><span><b>CAT NUSANTARA</b><small>{eventTitle}</small></span></div><div className="exam-identity"><span>Status jawaban: {saving === "saving" ? "menyimpan" : saving === "saved" ? "tersimpan" : "gagal disimpan"}</span><small>SESI UJIAN AKTIF</small></div><div className="exam-clock"><small>SISA WAKTU</small><strong>{clock}</strong></div></header><div className="exam-progress"><span className="exam-progress-label">SIMULASI SKD</span><span>Soal {question.position} dari {overview.length}</span><span>{answers.filter(answer => answer.selectedOption).length} terjawab</span><div className="exam-progress-track"><i style={{ width: `${(question.position / overview.length) * 100}%` }} /></div></div><section className="exam-workspace"><article className="exam-question-panel"><div className="exam-question-meta"><span className="question-number">SOAL {String(question.position).padStart(3, "0")}</span><span className="question-category">{question.category} · {question.topic}</span></div><h1>{question.prompt}</h1><p className="exam-instruction">Pilih satu jawaban yang paling tepat. Kunci dan pembahasan tidak ditampilkan selama ujian.</p><div className="exam-options">{question.options.map(option => <label className="exam-option" key={option.label}><input type="radio" name="answer" checked={question.selectedOption === option.label} onChange={() => choose(option.label)} /><span className="option-label">{option.label}</span><span>{option.text}</span></label>)}</div><div className="exam-actions"><button className="exam-secondary" type="button" disabled={question.position === 1} onClick={() => open(question.position - 1)}>← Sebelumnya</button><button className="exam-skip" type="button" disabled={question.position === overview.length} onClick={() => open(question.position + 1)}>Lewati soal</button><button className="exam-primary" type="button" disabled={question.position === overview.length} onClick={() => open(question.position + 1)}>Lanjutkan <span>→</span></button></div></article><aside className="exam-sidebar"><div className="exam-sidebar-head"><h2>Navigasi soal</h2><span>{question.position} / {overview.length}</span></div><div className="exam-legend"><span><i className="legend-current" />Aktif</span><span><i className="legend-answered" />Terjawab</span><span><i className="legend-empty" />Belum</span></div><div className="exam-grid">{answers.map(answer => <button key={answer.position} className={`exam-number ${answer.position === question.position ? "is-current" : answer.selectedOption ? "is-answered" : ""}`} type="button" onClick={() => open(answer.position)}>{String(answer.position).padStart(2, "0")}</button>)}</div><div className="exam-sidebar-footer"><p>Komposisi soal</p><div><span>TWK <b>30</b></span><span>TIU <b>35</b></span><span>TKP <b>45</b></span></div><button className="exam-finish" type="button" onClick={submit} disabled={submitting}>{submitting ? "Menilai…" : "Akhiri simulasi"}</button></div></aside></section></main>;
}
