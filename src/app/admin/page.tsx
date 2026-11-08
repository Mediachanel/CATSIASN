import Link from "next/link";
import { redirect } from "next/navigation";
import { auth } from "../../auth";
import { db } from "../../lib/db/client";
import { logout } from "../(auth)/actions";

export default async function AdminDashboardPage() {
  const session = await auth();
  if (!session?.user?.id) redirect("/login");

  const user = await db.user.findUnique({
    where: { id: session.user.id },
    select: { id: true, name: true, email: true, role: true },
  });

  if (!user || user.role !== "ADMIN") {
    redirect("/dashboard");
  }

  // Aggregate Metrics
  const [
    totalQuestions,
    twkCount,
    tiuCount,
    tkpCount,
    totalParticipants,
    totalSessions,
    totalCompleted,
    events,
    recentSessions,
    sampleQuestions,
  ] = await Promise.all([
    db.question.count(),
    db.question.count({ where: { category: "TWK" } }),
    db.question.count({ where: { category: "TIU" } }),
    db.question.count({ where: { category: "TKP" } }),
    db.user.count({ where: { role: "PARTICIPANT" } }),
    db.examSession.count(),
    db.examSession.count({ where: { status: "SUBMITTED" } }),
    db.tryoutEvent.findMany({
      include: { _count: { select: { sessions: true } } },
      orderBy: { createdAt: "desc" },
    }),
    db.examSession.findMany({
      take: 8,
      orderBy: { startedAt: "desc" },
      include: {
        user: { select: { name: true, email: true } },
        event: { select: { title: true } },
        result: true,
      },
    }),
    db.question.findMany({
      take: 6,
      orderBy: { createdAt: "desc" },
      include: {
        versions: {
          take: 1,
          orderBy: { version: "desc" },
        },
      },
    }),
  ]);

  return (
    <main style={{ minHeight: "100vh", background: "var(--slate-100)" }}>
      {/* ==================== ADMIN NAVBAR ==================== */}
      <header
        style={{
          background: "#0f172a",
          borderBottom: "1px solid rgba(255,255,255,0.1)",
          height: "72px",
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          padding: "0 5%",
          color: "#fff",
        }}
      >
        <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
          <img
            src="/cpns_launcher_icon_1794011079614.jpg"
            alt="Logo"
            style={{ width: "40px", height: "40px", borderRadius: "10px" }}
          />
          <div>
            <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
              <h2 style={{ fontSize: "17px", fontWeight: 800, letterSpacing: "-0.01em" }}>
                CAT <span style={{ color: "var(--gold-light)" }}>CPNS PRO</span>
              </h2>
              <span
                style={{
                  background: "var(--crimson-red)",
                  color: "#fff",
                  fontSize: "10px",
                  fontWeight: 800,
                  padding: "2px 6px",
                  borderRadius: "4px",
                  letterSpacing: "0.06em",
                }}
              >
                ADMIN PANEL
              </span>
            </div>
            <p style={{ fontSize: "11px", color: "#94a3b8" }}>Pusat Pengelola Soal & Simulasi SKD</p>
          </div>
        </div>

        <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
          <Link
            href="/dashboard?view=participant"
            style={{
              background: "rgba(255,255,255,0.1)",
              border: "1px solid rgba(255,255,255,0.2)",
              color: "#f8fafc",
              padding: "8px 14px",
              borderRadius: "8px",
              fontSize: "13px",
              fontWeight: 600,
              display: "inline-flex",
              alignItems: "center",
              gap: "6px",
            }}
          >
            👤 Mode Simulasi Peserta
          </Link>
          <Link
            href="/tryouts"
            style={{
              background: "var(--gold-primary)",
              color: "#fff",
              padding: "8px 14px",
              borderRadius: "8px",
              fontSize: "13px",
              fontWeight: 700,
            }}
          >
            + Coba Tryout
          </Link>
          <form action={logout}>
            <button
              type="submit"
              style={{
                background: "transparent",
                border: "1px solid rgba(255,255,255,0.2)",
                color: "#cbd5e1",
                padding: "8px 14px",
                borderRadius: "8px",
                fontSize: "13px",
                cursor: "pointer",
              }}
            >
              Keluar
            </button>
          </form>
        </div>
      </header>

      {/* ==================== HERO ADMIN ==================== */}
      <section
        style={{
          background: "linear-gradient(135deg, #020617 0%, #0f172a 50%, #1e3a8a 100%)",
          color: "#fff",
          padding: "40px 5%",
        }}
      >
        <div style={{ maxWidth: "1280px", margin: "0 auto" }}>
          <div style={{ display: "flex", alignItems: "center", gap: "10px", marginBottom: "8px" }}>
            <span
              style={{
                background: "#059669",
                color: "#fff",
                padding: "3px 10px",
                borderRadius: "6px",
                fontSize: "11px",
                fontWeight: 800,
              }}
            >
              SUPER ADMINISTRATOR
            </span>
            <span style={{ fontSize: "13px", color: "#cbd5e1" }}>{user.email}</span>
          </div>

          <h1 style={{ fontSize: "32px", fontWeight: 800, letterSpacing: "-0.02em" }}>
            Pusat Kendali Admin, {user.name}
          </h1>
          <p style={{ color: "#94a3b8", fontSize: "14px", maxWidth: "640px", marginTop: "4px" }}>
            Kelola bank soal 9.000 butir, konfigurasi event tryout nasional, pantau hasil ujian peserta, dan pastikan kesiapan materi SKD sesuai standar BKN.
          </p>

          {/* 4 Stat Cards */}
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
              gap: "16px",
              marginTop: "28px",
            }}
          >
            <div
              style={{
                background: "rgba(255,255,255,0.06)",
                border: "1px solid rgba(255,255,255,0.12)",
                borderRadius: "14px",
                padding: "20px",
              }}
            >
              <div style={{ fontSize: "12px", color: "#93c5fd", fontWeight: 600 }}>Total Bank Soal (Aktif)</div>
              <div style={{ fontSize: "32px", fontWeight: 800, color: "#fff", marginTop: "4px" }}>
                {totalQuestions.toLocaleString("id-ID")}
                <span style={{ fontSize: "14px", fontWeight: 600, color: "var(--gold-light)", marginLeft: "6px" }}>
                  Butir
                </span>
              </div>
              <div style={{ fontSize: "11px", color: "#cbd5e1", marginTop: "4px" }}>
                Status DRAFT (9.000 siap verifikasi)
              </div>
            </div>

            <div
              style={{
                background: "rgba(255,255,255,0.06)",
                border: "1px solid rgba(255,255,255,0.12)",
                borderRadius: "14px",
                padding: "20px",
              }}
            >
              <div style={{ fontSize: "12px", color: "#93c5fd", fontWeight: 600 }}>Peserta Terdaftar</div>
              <div style={{ fontSize: "32px", fontWeight: 800, color: "#fff", marginTop: "4px" }}>
                {totalParticipants.toLocaleString("id-ID")}
                <span style={{ fontSize: "14px", fontWeight: 600, color: "#86efac", marginLeft: "6px" }}>
                  Akun
                </span>
              </div>
              <div style={{ fontSize: "11px", color: "#cbd5e1", marginTop: "4px" }}>
                Peserta aktif siap simulasi
              </div>
            </div>

            <div
              style={{
                background: "rgba(255,255,255,0.06)",
                border: "1px solid rgba(255,255,255,0.12)",
                borderRadius: "14px",
                padding: "20px",
              }}
            >
              <div style={{ fontSize: "12px", color: "#93c5fd", fontWeight: 600 }}>Sesi Ujian Berjalan & Selesai</div>
              <div style={{ fontSize: "32px", fontWeight: 800, color: "#fff", marginTop: "4px" }}>
                {totalSessions.toLocaleString("id-ID")}
                <span style={{ fontSize: "14px", fontWeight: 600, color: "#fef08a", marginLeft: "6px" }}>
                  Sesi
                </span>
              </div>
              <div style={{ fontSize: "11px", color: "#cbd5e1", marginTop: "4px" }}>
                {totalCompleted} sesi telah dinilai tuntas
              </div>
            </div>

            <div
              style={{
                background: "rgba(255,255,255,0.06)",
                border: "1px solid rgba(255,255,255,0.12)",
                borderRadius: "14px",
                padding: "20px",
              }}
            >
              <div style={{ fontSize: "12px", color: "#93c5fd", fontWeight: 600 }}>Paket Tryout Nasional</div>
              <div style={{ fontSize: "32px", fontWeight: 800, color: "var(--gold-light)", marginTop: "4px" }}>
                {events.length}
                <span style={{ fontSize: "14px", fontWeight: 600, color: "#cbd5e1", marginLeft: "6px" }}>
                  Event
                </span>
              </div>
              <div style={{ fontSize: "11px", color: "#cbd5e1", marginTop: "4px" }}>
                {events.filter((e) => e.status === "PUBLISHED").length} Published (Aktif)
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ==================== MAIN CONTENT BODY ==================== */}
      <div style={{ maxWidth: "1280px", margin: "32px auto", padding: "0 20px" }}>
        {/* ROW 1: Komposisi Bank Soal & Paket Tryout */}
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(360px, 1fr))", gap: "20px", marginBottom: "28px" }}>
          {/* Komposisi Bank Soal Card */}
          <div
            style={{
              background: "#fff",
              borderRadius: "16px",
              padding: "24px",
              border: "1px solid var(--slate-200)",
              boxShadow: "0 4px 16px rgba(15,23,42,0.04)",
            }}
          >
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "16px" }}>
              <div>
                <h3 style={{ fontSize: "18px", fontWeight: 800, color: "var(--slate-900)" }}>
                  Distribusi Komposisi Bank Soal
                </h3>
                <p style={{ fontSize: "12px", color: "var(--slate-600)" }}>
                  Total 9.000 butir soal terdistribusi dalam 3 sub-tes SKD resmi
                </p>
              </div>
              <span
                style={{
                  background: "var(--gold-soft)",
                  color: "var(--gold-primary)",
                  padding: "4px 8px",
                  borderRadius: "6px",
                  fontSize: "11px",
                  fontWeight: 700,
                }}
              >
                Draft 100%
              </span>
            </div>

            <div style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
              {/* TWK */}
              <div
                style={{
                  background: "var(--navy-soft)",
                  borderRadius: "10px",
                  padding: "14px",
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <div>
                  <b style={{ color: "var(--navy-primary)", fontSize: "14px" }}>TWK (Tes Wawasan Kebangsaan)</b>
                  <div style={{ fontSize: "11px", color: "var(--slate-600)", marginTop: "2px" }}>
                    Pancasila, UUD 1945, NKRI, Bhinneka Tunggal Ika, Bela Negara
                  </div>
                </div>
                <div style={{ textAlign: "right" }}>
                  <div style={{ fontSize: "18px", fontWeight: 800, color: "var(--navy-primary)" }}>
                    {twkCount.toLocaleString("id-ID")}
                  </div>
                  <div style={{ fontSize: "10px", color: "var(--slate-600)" }}>
                    {Math.round((twkCount / (totalQuestions || 1)) * 100)}% dari total
                  </div>
                </div>
              </div>

              {/* TIU */}
              <div
                style={{
                  background: "var(--emerald-soft)",
                  borderRadius: "10px",
                  padding: "14px",
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <div>
                  <b style={{ color: "var(--emerald-success)", fontSize: "14px" }}>TIU (Tes Inteligensi Umum)</b>
                  <div style={{ fontSize: "11px", color: "var(--slate-600)", marginTop: "2px" }}>
                    Silogisme, Deret Angka, Numerik Berhitung, Penalaran Analitis
                  </div>
                </div>
                <div style={{ textAlign: "right" }}>
                  <div style={{ fontSize: "18px", fontWeight: 800, color: "var(--emerald-success)" }}>
                    {tiuCount.toLocaleString("id-ID")}
                  </div>
                  <div style={{ fontSize: "10px", color: "var(--slate-600)" }}>
                    {Math.round((tiuCount / (totalQuestions || 1)) * 100)}% dari total
                  </div>
                </div>
              </div>

              {/* TKP */}
              <div
                style={{
                  background: "var(--gold-soft)",
                  borderRadius: "10px",
                  padding: "14px",
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <div>
                  <b style={{ color: "var(--gold-primary)", fontSize: "14px" }}>TKP (Tes Karakteristik Pribadi)</b>
                  <div style={{ fontSize: "11px", color: "var(--slate-600)", marginTop: "2px" }}>
                    Pelayanan Publik, Jejaring Kerja, Sosial Budaya, Anti-Radikalisme
                  </div>
                </div>
                <div style={{ textAlign: "right" }}>
                  <div style={{ fontSize: "18px", fontWeight: 800, color: "var(--gold-primary)" }}>
                    {tkpCount.toLocaleString("id-ID")}
                  </div>
                  <div style={{ fontSize: "10px", color: "var(--slate-600)" }}>
                    {Math.round((tkpCount / (totalQuestions || 1)) * 100)}% dari total
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Paket Tryout Table Card */}
          <div
            style={{
              background: "#fff",
              borderRadius: "16px",
              padding: "24px",
              border: "1px solid var(--slate-200)",
              boxShadow: "0 4px 16px rgba(15,23,42,0.04)",
            }}
          >
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "16px" }}>
              <div>
                <h3 style={{ fontSize: "18px", fontWeight: 800, color: "var(--slate-900)" }}>
                  Daftar Paket Tryout Nasional
                </h3>
                <p style={{ fontSize: "12px", color: "var(--slate-600)" }}>
                  Event tryout yang tersedia untuk diikuti oleh peserta
                </p>
              </div>
              <Link
                href="/tryouts"
                style={{
                  fontSize: "12px",
                  fontWeight: 700,
                  color: "var(--navy-primary)",
                }}
              >
                Lihat di Katalog →
              </Link>
            </div>

            <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
              {events.map((event) => (
                <div
                  key={event.id}
                  style={{
                    border: "1px solid var(--slate-200)",
                    borderRadius: "10px",
                    padding: "14px",
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                  }}
                >
                  <div>
                    <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
                      <span style={{ fontWeight: 700, fontSize: "14px", color: "var(--slate-900)" }}>
                        {event.title}
                      </span>
                      <span
                        style={{
                          fontSize: "10px",
                          fontWeight: 700,
                          padding: "2px 6px",
                          borderRadius: "4px",
                          background: event.status === "PUBLISHED" ? "var(--emerald-soft)" : "var(--slate-100)",
                          color: event.status === "PUBLISHED" ? "var(--emerald-success)" : "var(--slate-700)",
                        }}
                      >
                        {event.status}
                      </span>
                    </div>
                    <div style={{ fontSize: "11px", color: "var(--slate-600)", marginTop: "4px" }}>
                      Durasi: {event.durationMinutes} Menit • Komposisi: {event.twkCount} TWK, {event.tiuCount} TIU, {event.tkpCount} TKP
                    </div>
                  </div>

                  <div style={{ textAlign: "right" }}>
                    <div style={{ fontSize: "15px", fontWeight: 800, color: "var(--navy-primary)" }}>
                      {event._count.sessions} Peserta
                    </div>
                    <span style={{ fontSize: "10px", color: "var(--slate-600)" }}>telah mengikuti</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* ROW 2: Aktivitas Ujian Peserta Realtime */}
        <div
          style={{
            background: "#fff",
            borderRadius: "16px",
            padding: "24px",
            border: "1px solid var(--slate-200)",
            boxShadow: "0 4px 16px rgba(15,23,42,0.04)",
            marginBottom: "28px",
          }}
        >
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "18px" }}>
            <div>
              <h3 style={{ fontSize: "18px", fontWeight: 800, color: "var(--slate-900)" }}>
                Log Aktivitas & Evaluasi Skor Peserta Terbaru
              </h3>
              <p style={{ fontSize: "12px", color: "var(--slate-600)" }}>
                Rekap waktu pengerjaan dan pencapaian ambang batas nilai kelulusan SKD (TWK≥65, TIU≥80, TKP≥166)
              </p>
            </div>
          </div>

          <div style={{ overflowX: "auto" }}>
            <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "13px" }}>
              <thead>
                <tr style={{ background: "var(--slate-50)", borderBottom: "1px solid var(--slate-200)" }}>
                  <th style={{ textAlign: "left", padding: "12px 14px", fontWeight: 700, color: "var(--slate-700)" }}>
                    Nama Peserta
                  </th>
                  <th style={{ textAlign: "left", padding: "12px 14px", fontWeight: 700, color: "var(--slate-700)" }}>
                    Paket Simulasi
                  </th>
                  <th style={{ textAlign: "center", padding: "12px 14px", fontWeight: 700, color: "var(--slate-700)" }}>
                    Status
                  </th>
                  <th style={{ textAlign: "center", padding: "12px 14px", fontWeight: 700, color: "var(--slate-700)" }}>
                    TWK / TIU / TKP
                  </th>
                  <th style={{ textAlign: "right", padding: "12px 14px", fontWeight: 700, color: "var(--slate-700)" }}>
                    Total Skor
                  </th>
                  <th style={{ textAlign: "center", padding: "12px 14px", fontWeight: 700, color: "var(--slate-700)" }}>
                    Hasil BKN
                  </th>
                </tr>
              </thead>
              <tbody>
                {recentSessions.length ? (
                  recentSessions.map((s) => {
                    const res = s.result;
                    const isPassed = res
                      ? res.twkScore >= 65 && res.tiuScore >= 80 && res.tkpScore >= 166
                      : false;

                    return (
                      <tr key={s.id} style={{ borderBottom: "1px solid var(--slate-100)" }}>
                        <td style={{ padding: "12px 14px" }}>
                          <b>{s.user.name}</b>
                          <div style={{ fontSize: "11px", color: "var(--slate-600)" }}>{s.user.email}</div>
                        </td>
                        <td style={{ padding: "12px 14px", color: "var(--slate-700)" }}>{s.event.title}</td>
                        <td style={{ padding: "12px 14px", textAlign: "center" }}>
                          <span
                            style={{
                              fontSize: "11px",
                              fontWeight: 700,
                              padding: "2px 8px",
                              borderRadius: "4px",
                              background: s.status === "SUBMITTED" ? "var(--slate-100)" : "var(--gold-soft)",
                              color: s.status === "SUBMITTED" ? "var(--slate-700)" : "var(--gold-primary)",
                            }}
                          >
                            {s.status === "SUBMITTED" ? "Selesai" : "Sedang Ujian"}
                          </span>
                        </td>
                        <td style={{ padding: "12px 14px", textAlign: "center" }}>
                          {res ? (
                            <span style={{ fontSize: "12px", fontFamily: "monospace" }}>
                              {res.twkScore} / {res.tiuScore} / {res.tkpScore}
                            </span>
                          ) : (
                            <span style={{ color: "var(--slate-600)" }}>-</span>
                          )}
                        </td>
                        <td style={{ padding: "12px 14px", textAlign: "right", fontWeight: 800, color: "var(--navy-primary)" }}>
                          {res ? res.totalScore : "-"}
                        </td>
                        <td style={{ padding: "12px 14px", textAlign: "center" }}>
                          {res ? (
                            <span
                              style={{
                                fontSize: "11px",
                                fontWeight: 700,
                                padding: "3px 8px",
                                borderRadius: "4px",
                                background: isPassed ? "var(--emerald-soft)" : "var(--crimson-soft)",
                                color: isPassed ? "var(--emerald-success)" : "var(--crimson-red)",
                              }}
                            >
                              {isPassed ? "LULUS" : "BELUM LULUS"}
                            </span>
                          ) : (
                            <span style={{ color: "var(--slate-600)" }}>-</span>
                          )}
                        </td>
                      </tr>
                    );
                  })
                ) : (
                  <tr>
                    <td colSpan={6} style={{ textAlign: "center", padding: "24px", color: "var(--slate-600)" }}>
                      Belum ada sesi ujian peserta.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* ROW 3: Cuplikan Sampel Bank Soal */}
        <div
          style={{
            background: "#fff",
            borderRadius: "16px",
            padding: "24px",
            border: "1px solid var(--slate-200)",
            boxShadow: "0 4px 16px rgba(15,23,42,0.04)",
          }}
        >
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "16px" }}>
            <div>
              <h3 style={{ fontSize: "18px", fontWeight: 800, color: "var(--slate-900)" }}>
                Sampel Butir Soal Terkini (Bank Soal)
              </h3>
              <p style={{ fontSize: "12px", color: "var(--slate-600)" }}>
                Pratinjau soal aktif yang siap disajikan ke dalam ujian simulasi CAT
              </p>
            </div>
            <span style={{ fontSize: "12px", color: "var(--slate-600)" }}>
              Menampilkan 6 dari {totalQuestions.toLocaleString("id-ID")} soal
            </span>
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(340px, 1fr))", gap: "14px" }}>
            {sampleQuestions.map((q) => {
              const version = q.versions[0];
              return (
                <div
                  key={q.id}
                  style={{
                    background: "var(--slate-50)",
                    border: "1px solid var(--slate-200)",
                    borderRadius: "10px",
                    padding: "16px",
                  }}
                >
                  <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "8px" }}>
                    <div style={{ display: "flex", gap: "6px" }}>
                      <span
                        style={{
                          fontSize: "10px",
                          fontWeight: 800,
                          padding: "2px 6px",
                          borderRadius: "4px",
                          background:
                            q.category === "TWK"
                              ? "var(--navy-soft)"
                              : q.category === "TIU"
                              ? "var(--emerald-soft)"
                              : "var(--gold-soft)",
                          color:
                            q.category === "TWK"
                              ? "var(--navy-primary)"
                              : q.category === "TIU"
                              ? "var(--emerald-success)"
                              : "var(--gold-primary)",
                        }}
                      >
                        {q.category}
                      </span>
                      <span style={{ fontSize: "11px", fontWeight: 600, color: "var(--slate-600)" }}>
                        {q.topic}
                      </span>
                    </div>
                    <span style={{ fontSize: "10px", color: "var(--slate-600)", fontFamily: "monospace" }}>
                      {q.code}
                    </span>
                  </div>

                  <p
                    style={{
                      fontSize: "13px",
                      color: "var(--slate-800)",
                      lineHeight: "1.5",
                      overflow: "hidden",
                      textOverflow: "ellipsis",
                      display: "-webkit-box",
                      WebkitLineClamp: 3,
                      WebkitBoxOrient: "vertical",
                    }}
                  >
                    {version ? version.prompt : "Tidak ada versi soal."}
                  </p>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </main>
  );
}
