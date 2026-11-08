import Link from "next/link";
import { notFound, redirect } from "next/navigation";
import { auth } from "../../../../auth";
import { db } from "../../../../lib/db/client";

export default async function ResultPage({
  params,
}: {
  params: Promise<{ sessionId: string }>;
}) {
  const user = await auth();
  if (!user?.user?.id) redirect("/login");

  const { sessionId } = await params;
  const session = await db.examSession.findFirst({
    where: { id: sessionId, userId: user.user.id },
    include: { event: true, result: true, questions: { include: { answer: true } } },
  });

  if (!session?.result) notFound();

  const { result, event, questions } = session;
  const answeredCount = questions.filter((q) => q.answer).length;
  const totalQuestions = questions.length;

  // Passing grade thresholds (official BKN 2024 reference)
  const pgTwk = 65;
  const pgTiu = 80;
  const pgTkp = 166;

  const twkPct = Math.round((result.twkScore / 150) * 100);
  const tiuPct = Math.round((result.tiuScore / 175) * 100);
  const tkpPct = Math.round((result.tkpScore / 225) * 100);

  const isLulus =
    result.twkScore >= pgTwk &&
    result.tiuScore >= pgTiu &&
    result.tkpScore >= pgTkp;

  return (
    <div className="result-shell">
      {/* ── TOPBAR ── */}
      <header className="result-topbar">
        <div className="result-topbar-brand">
          <span>N</span>
          CAT <b style={{ color: "#f59e0b", marginLeft: 4 }}>NUSANTARA</b>
        </div>
        <nav style={{ display: "flex", gap: 24 }}>
          <Link href="/dashboard">Dashboard</Link>
          <Link href="/tryouts">Tryout lain</Link>
        </nav>
      </header>

      {/* ── BODY ── */}
      <div className="result-body">
        <p className="result-event-label">Hasil Simulasi</p>
        <h1 className="result-event-title">{event.title}</h1>

        {/* Draft warning */}
        <div className="draft-warning-banner">
          <span>⚠️</span>
          <span>
            Soal dalam event ini masih berstatus <b>DRAFT</b> dan belum diverifikasi ahli.
            Gunakan hasil ini hanya sebagai latihan mandiri, bukan tolok ukur resmi.
          </span>
        </div>

        {/* Total score card */}
        <div className="result-total-card">
          <div className="result-total-left">
            <p>Skor Total</p>
            <h2>
              {result.totalScore}
              <small>/ 550</small>
            </h2>
            <span>
              {answeredCount} dari {totalQuestions} soal dijawab
            </span>
          </div>
          <div className="result-total-right">
            <div className={`result-status-badge ${isLulus ? "status-lulus" : "status-belum"}`}>
              {isLulus ? "✓ LULUS PASSING GRADE" : "✗ BELUM LULUS"}
            </div>
            <p
              style={{
                marginTop: 10,
                fontSize: 11,
                color: "rgba(255,255,255,0.55)",
                fontWeight: 600,
              }}
            >
              PG: TWK≥{pgTwk} · TIU≥{pgTiu} · TKP≥{pgTkp}
            </p>
          </div>
        </div>

        {/* Score cards per subtest */}
        <div className="result-score-grid">
          {/* TWK */}
          <div className="score-card score-twk">
            <div className="score-card-label">TWK</div>
            <div className="score-card-value">{result.twkScore}</div>
            <div className="score-card-max">maks. 150 · PG {pgTwk}</div>
            <div className="score-card-bar">
              <i style={{ width: `${twkPct}%` }} />
            </div>
            <p
              style={{
                marginTop: 8,
                fontSize: 11,
                fontWeight: 700,
                color: result.twkScore >= pgTwk ? "#059669" : "#dc2626",
              }}
            >
              {result.twkScore >= pgTwk ? "✓ Memenuhi PG" : `✗ Kurang ${pgTwk - result.twkScore} poin`}
            </p>
          </div>

          {/* TIU */}
          <div className="score-card score-tiu">
            <div className="score-card-label">TIU</div>
            <div className="score-card-value">{result.tiuScore}</div>
            <div className="score-card-max">maks. 175 · PG {pgTiu}</div>
            <div className="score-card-bar">
              <i style={{ width: `${tiuPct}%` }} />
            </div>
            <p
              style={{
                marginTop: 8,
                fontSize: 11,
                fontWeight: 700,
                color: result.tiuScore >= pgTiu ? "#059669" : "#dc2626",
              }}
            >
              {result.tiuScore >= pgTiu ? "✓ Memenuhi PG" : `✗ Kurang ${pgTiu - result.tiuScore} poin`}
            </p>
          </div>

          {/* TKP */}
          <div className="score-card score-tkp">
            <div className="score-card-label">TKP</div>
            <div className="score-card-value">{result.tkpScore}</div>
            <div className="score-card-max">maks. 225 · PG {pgTkp}</div>
            <div className="score-card-bar">
              <i style={{ width: `${tkpPct}%` }} />
            </div>
            <p
              style={{
                marginTop: 8,
                fontSize: 11,
                fontWeight: 700,
                color: result.tkpScore >= pgTkp ? "#059669" : "#dc2626",
              }}
            >
              {result.tkpScore >= pgTkp ? "✓ Memenuhi PG" : `✗ Kurang ${pgTkp - result.tkpScore} poin`}
            </p>
          </div>
        </div>

        {/* Info box */}
        <div className="result-info-box">
          <h3>📋 Tentang hasil ini</h3>
          <p>
            Hasil latihan telah tersimpan di akun Anda dan dapat dilihat di dashboard kapan saja.
            Pembahasan soal lengkap tersedia setelah event diverifikasi oleh tim ahli. Terus
            berlatih untuk meningkatkan skor Anda!
          </p>
        </div>

        {/* Action buttons */}
        <div className="result-actions-row">
          <Link href="/tryouts" className="btn-result-primary">
            🔁 Ikuti tryout lain
          </Link>
          <Link href="/dashboard" className="btn-result-outline">
            📊 Lihat riwayat di dashboard
          </Link>
        </div>
      </div>
    </div>
  );
}
