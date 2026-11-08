"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Image from "next/image";

type Question = {
  position: number;
  prompt: string;
  imagePath: string | null;
  imageAlt: string | null;
  category: string;
  topic: string;
  options: Array<{ label: string; text: string }>;
  selectedOption: string | null;
};

type Overview = { position: number; selectedOption: string | null };

export function ExamRunner({
  sessionId,
  deadlineAt,
  eventTitle,
  initialQuestion,
  overview: initialOverview,
}: {
  sessionId: string;
  deadlineAt: string;
  eventTitle: string;
  initialQuestion: Question;
  overview: Overview[];
}) {
  const router = useRouter();
  const [question, setQuestion] = useState(initialQuestion);
  const [answers, setAnswers] = useState(initialOverview);
  const [saving, setSaving] = useState<"saved" | "saving" | "error">("saved");
  const [seconds, setSeconds] = useState(() =>
    Math.max(0, Math.floor((new Date(deadlineAt).getTime() - Date.now()) / 1000))
  );
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const timer = window.setInterval(
      () => setSeconds(Math.max(0, Math.floor((new Date(deadlineAt).getTime() - Date.now()) / 1000))),
      1000
    );
    return () => window.clearInterval(timer);
  }, [deadlineAt]);

  const isLowTime = seconds < 300; // last 5 minutes
  const hh = String(Math.floor(seconds / 3600)).padStart(2, "0");
  const mm = String(Math.floor((seconds % 3600) / 60)).padStart(2, "0");
  const ss = String(seconds % 60).padStart(2, "0");
  const clock = `${hh}:${mm}:${ss}`;

  async function open(position: number) {
    const res = await fetch(`/api/v1/exam/sessions/${sessionId}/questions/${position}`, {
      cache: "no-store",
    });
    if (res.ok) setQuestion(await res.json());
  }

  async function choose(selectedOption: string) {
    setQuestion((cur) => ({ ...cur, selectedOption }));
    setAnswers((cur) =>
      cur.map((a) => (a.position === question.position ? { ...a, selectedOption } : a))
    );
    setSaving("saving");
    const res = await fetch(`/api/v1/exam/sessions/${sessionId}/answers`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ position: question.position, selectedOption }),
    });
    setSaving(res.ok ? "saved" : "error");
  }

  async function submit() {
    if (!window.confirm("Akhiri simulasi? Jawaban yang sudah tersimpan akan dinilai.")) return;
    setSubmitting(true);
    const res = await fetch(`/api/v1/exam/sessions/${sessionId}/submit`, { method: "POST" });
    if (res.ok) router.push(`/results/${sessionId}`);
    else {
      setSubmitting(false);
      setSaving("error");
    }
  }

  const answeredCount = answers.filter((a) => a.selectedOption).length;
  const progressPct = (question.position / answers.length) * 100;

  const savingLabel =
    saving === "saving" ? "⏳ Menyimpan…" : saving === "saved" ? "✓ Tersimpan" : "⚠ Gagal simpan";

  return (
    <main className="exam-shell">
      {/* ── HEADER ── */}
      <header className="exam-header">
        <div className="exam-brand">
          <span className="exam-mark">N</span>
          <span>
            <b>CAT NUSANTARA</b>
            <small>{eventTitle}</small>
          </span>
        </div>

        <div className="exam-identity">
          <span>{savingLabel}</span>
          <small>SESI UJIAN AKTIF</small>
        </div>

        <div
          className="exam-clock"
          style={isLowTime ? { borderColor: "#fca5a5", background: "rgba(220,38,38,0.2)" } : {}}
        >
          <small>SISA WAKTU</small>
          <strong style={isLowTime ? { color: "#fca5a5" } : {}}>{clock}</strong>
        </div>
      </header>

      {/* ── PROGRESS BAR ── */}
      <div className="exam-progress">
        <span className="exam-progress-label">SIMULASI SKD</span>
        <span>Soal {question.position} dari {answers.length}</span>
        <span>{answeredCount} terjawab</span>
        <div className="exam-progress-track">
          <i style={{ width: `${progressPct}%` }} />
        </div>
      </div>

      {/* ── WORKSPACE ── */}
      <section className="exam-workspace">
        {/* ── QUESTION PANEL ── */}
        <article className="exam-question-panel">
          <div className="exam-question-meta">
            <span className="question-number">SOAL {String(question.position).padStart(3, "0")}</span>
            <span className="question-category">
              {question.category} · {question.topic}
            </span>
          </div>

          <h1>{question.prompt}</h1>
          {question.imagePath && question.imageAlt && (
            <Image src={question.imagePath} alt={question.imageAlt} width={500} height={310} unoptimized style={{ width: "100%", maxWidth: 500, height: "auto", display: "block", margin: "20px auto" }} />
          )}
          <p className="exam-instruction">
            Pilih satu jawaban yang paling tepat. Kunci dan pembahasan tidak ditampilkan selama ujian.
          </p>

          <div className="exam-options">
            {question.options.map((opt) => (
              <label className="exam-option" key={opt.label}>
                <input
                  type="radio"
                  name="answer"
                  checked={question.selectedOption === opt.label}
                  onChange={() => choose(opt.label)}
                />
                <span className="option-label">{opt.label}</span>
                <span>{opt.text}</span>
              </label>
            ))}
          </div>

          <div className="exam-actions">
            <button
              className="exam-secondary"
              type="button"
              disabled={question.position === 1}
              onClick={() => open(question.position - 1)}
            >
              ← Sebelumnya
            </button>
            <button
              className="exam-skip"
              type="button"
              disabled={question.position === answers.length}
              onClick={() => open(question.position + 1)}
            >
              Lewati
            </button>
            <button
              className="exam-primary"
              type="button"
              disabled={question.position === answers.length}
              onClick={() => open(question.position + 1)}
            >
              Lanjutkan →
            </button>
          </div>
        </article>

        {/* ── SIDEBAR ── */}
        <aside className="exam-sidebar">
          <div className="exam-sidebar-head">
            <h2>Navigasi soal</h2>
            <span>{question.position} / {answers.length}</span>
          </div>

          <div className="exam-legend">
            <span><i className="legend-current" />Aktif</span>
            <span><i className="legend-answered" />Terjawab</span>
            <span><i className="legend-empty" />Belum</span>
          </div>

          <div className="exam-grid">
            {answers.map((a) => (
              <button
                key={a.position}
                className={`exam-number${
                  a.position === question.position
                    ? " is-current"
                    : a.selectedOption
                    ? " is-answered"
                    : ""
                }`}
                type="button"
                onClick={() => open(a.position)}
              >
                {String(a.position).padStart(2, "0")}
              </button>
            ))}
          </div>

          <div className="exam-sidebar-footer">
            <p>Komposisi soal</p>
            <div>
              <span>TWK <b>30</b></span>
              <span>TIU <b>35</b></span>
              <span>TKP <b>45</b></span>
            </div>
            <button
              className="exam-finish"
              type="button"
              onClick={submit}
              disabled={submitting}
            >
              {submitting ? "Menilai…" : "Akhiri simulasi"}
            </button>
          </div>
        </aside>
      </section>
    </main>
  );
}
