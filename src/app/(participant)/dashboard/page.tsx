import Link from "next/link";
import { redirect } from "next/navigation";
import { auth } from "../../../auth";
import { db } from "../../../lib/db/client";
import { logout } from "../../(auth)/actions";

export default async function Dashboard({
  searchParams,
}: {
  searchParams?: Promise<{ view?: string }>;
}) {
  const session = await auth();
  if (!session?.user?.id) redirect("/login");

  const sp = searchParams ? await searchParams : {};

  const user = await db.user.findUnique({
    where: { id: session.user.id },
    select: {
      name: true,
      email: true,
      role: true,
      examSessions: {
        orderBy: { startedAt: "desc" },
        take: 5,
        include: { event: true, result: true },
      },
    },
  });
  if (!user) redirect("/login");

  // Jika user berstatus ADMIN dan tidak secara eksplisit meminta tampilan peserta, bawa ke /admin
  if (user.role === "ADMIN" && sp.view !== "participant") {
    redirect("/admin");
  }

  const completed = user.examSessions.filter((s) => s.result);
  const highest = completed.length
    ? Math.max(...completed.map((s) => s.result?.totalScore ?? 0))
    : 0;

  return (
    <main style={{ minHeight: "100vh", background: "var(--slate-100)" }}>
      {/* Admin view banner jika admin sedang melihat tampilan peserta */}
      {user.role === "ADMIN" && (
        <div
          style={{
            background: "#0f172a",
            color: "#f8fafc",
            padding: "8px 5%",
            fontSize: "12px",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <span>🛡️ Anda sedang melihat <b>Mode Peserta</b> sebagai Administrator.</span>
          <Link
            href="/admin"
            style={{
              background: "var(--gold-primary)",
              color: "#fff",
              padding: "4px 10px",
              borderRadius: "4px",
              fontWeight: 700,
              fontSize: "11px",
            }}
          >
            Kembali ke Admin Panel →
          </Link>
        </div>
      )}

      {/* ==================== NAVBAR ==================== */}
      <header className="main-nav">
        <Link className="brand-badge-group" href="/">
          <img
            src="/cpns_launcher_icon_1794011079614.jpg"
            alt="Logo"
            className="brand-logo-img"
          />
          <div className="brand-text-block">
            <h2>
              CAT <span>CPNS PRO</span>
            </h2>
            <p>Dashboard Peserta</p>
          </div>
        </Link>
        <div className="nav-actions">
          <Link href="/paket" className="btn-ghost">Paket Berlangganan</Link>
          {user.role === "ADMIN" && (
            <Link
              href="/admin"
              style={{
                background: "var(--crimson-red)",
                color: "#fff",
                padding: "8px 14px",
                borderRadius: "8px",
                fontSize: "12px",
                fontWeight: 700,
                textDecoration: "none",
              }}
            >
              🛡️ Panel Admin
            </Link>
          )}
          <Link href="/tryouts" className="btn-primary-nav">
            + Mulai Tryout
          </Link>
          <form action={logout}>
            <button type="submit" className="btn-ghost" style={{ cursor: "pointer", border: 0 }}>
              Keluar
            </button>
          </form>
        </div>
      </header>

      {/* ==================== HERO SECTION ==================== */}
      <section style={{ background: "linear-gradient(135deg, var(--navy-dark) 0%, var(--navy-primary) 100%)", color: "#fff", padding: "44px 5%" }}>
        <div style={{ maxWidth: "1240px", margin: "0 auto" }}>
          <span style={{ background: "var(--gold-primary)", color: "#fff", padding: "4px 10px", borderRadius: "6px", fontSize: "11px", fontWeight: 800, letterSpacing: "0.08em" }}>
            PESERTA RESMI SKD
          </span>
          <h1 style={{ fontSize: "36px", fontWeight: 800, margin: "12px 0 6px", letterSpacing: "-0.03em" }}>
            Selamat Datang, {user.name}
          </h1>
          <p style={{ color: "#cbd5e1", fontSize: "15px", maxWidth: "600px" }}>
            Pantau progres kesiapan SKD, skor tertinggi, dan riwayat simulasi CAT secara berkala.
          </p>

          <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))", gap: "16px", marginTop: "28px" }}>
            <div style={{ background: "rgba(255,255,255,0.1)", borderRadius: "12px", padding: "18px", border: "1px solid rgba(255,255,255,0.15)" }}>
              <span style={{ fontSize: "12px", color: "#93c5fd" }}>Total Ujian Diikuti</span>
              <div style={{ fontSize: "28px", fontWeight: 800, color: "#fff", marginTop: "4px" }}>
                {user.examSessions.length}
              </div>
            </div>

            <div style={{ background: "rgba(255,255,255,0.1)", borderRadius: "12px", padding: "18px", border: "1px solid rgba(255,255,255,0.15)" }}>
              <span style={{ fontSize: "12px", color: "#93c5fd" }}>Skor Tertinggi</span>
              <div style={{ fontSize: "28px", fontWeight: 800, color: "var(--gold-light)", marginTop: "4px" }}>
                {highest > 0 ? highest : "-"} <small style={{ fontSize: "14px", color: "#cbd5e1" }}>/ 550</small>
              </div>
            </div>

            <div style={{ background: "rgba(255,255,255,0.1)", borderRadius: "12px", padding: "18px", border: "1px solid rgba(255,255,255,0.15)" }}>
              <span style={{ fontSize: "12px", color: "#93c5fd" }}>Status Akun</span>
              <div style={{ fontSize: "20px", fontWeight: 700, color: "#86efac", marginTop: "6px" }}>
                ✓ Aktif (Terverifikasi)
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ==================== RIWAYAT SIMULASI ==================== */}
      <div style={{ maxWidth: "1240px", margin: "36px auto", padding: "0 20px" }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "baseline", marginBottom: "18px" }}>
          <h2 style={{ fontSize: "22px", fontWeight: 800, color: "var(--slate-900)" }}>
            Riwayat Simulasi Terbaru
          </h2>
          <Link href="/tryouts" style={{ color: "var(--navy-primary)", fontWeight: 700, fontSize: "14px" }}>
            Lihat Semua Tryout →
          </Link>
        </div>

        {user.examSessions.length ? (
          <div style={{ display: "grid", gap: "14px" }}>
            {user.examSessions.map((exam) => {
              const res = exam.result;
              const isPassed = res
                ? res.twkScore >= 65 && res.tiuScore >= 80 && res.tkpScore >= 166
                : false;

              return (
                <article
                  key={exam.id}
                  style={{
                    background: "#fff",
                    borderRadius: "14px",
                    padding: "20px 24px",
                    border: "1px solid var(--slate-200)",
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    flexWrap: "wrap",
                    gap: "16px",
                  }}
                >
                  <div>
                    <h3 style={{ fontSize: "17px", fontWeight: 700, color: "var(--slate-900)" }}>
                      {exam.event.title}
                    </h3>
                    <div style={{ display: "flex", gap: "10px", marginTop: "6px", alignItems: "center" }}>
                      <span
                        style={{
                          fontSize: "12px",
                          fontWeight: 700,
                          padding: "2px 8px",
                          borderRadius: "4px",
                          background: exam.status === "IN_PROGRESS" ? "var(--gold-soft)" : "var(--slate-100)",
                          color: exam.status === "IN_PROGRESS" ? "var(--gold-primary)" : "var(--slate-700)",
                        }}
                      >
                        {exam.status === "IN_PROGRESS" ? "⏳ Sedang Berjalan" : "✓ Selesai"}
                      </span>
                      {res && (
                        <span
                          style={{
                            fontSize: "12px",
                            fontWeight: 700,
                            padding: "2px 8px",
                            borderRadius: "4px",
                            background: isPassed ? "var(--emerald-soft)" : "var(--crimson-soft)",
                            color: isPassed ? "var(--emerald-success)" : "var(--crimson-red)",
                          }}
                        >
                          {isPassed ? "LULUS PASSING GRADE" : "BELUM LULUS"}
                        </span>
                      )}
                    </div>
                  </div>

                  <div>
                    {res ? (
                      <div style={{ textAlign: "right" }}>
                        <div style={{ fontSize: "24px", fontWeight: 800, color: "var(--navy-primary)" }}>
                          {res.totalScore}{" "}
                          <span style={{ fontSize: "14px", fontWeight: 600, color: "var(--slate-600)" }}>
                            / 550
                          </span>
                        </div>
                        <div style={{ fontSize: "12px", color: "var(--slate-600)", marginTop: "2px" }}>
                          TWK: {res.twkScore} • TIU: {res.tiuScore} • TKP: {res.tkpScore}
                        </div>
                      </div>
                    ) : (
                      <Link
                        href={`/exam/${exam.id}`}
                        style={{
                          background: "var(--navy-primary)",
                          color: "#fff",
                          padding: "10px 18px",
                          borderRadius: "8px",
                          fontWeight: 700,
                          fontSize: "13px",
                          display: "inline-block",
                        }}
                      >
                        Lanjutkan Ujian →
                      </Link>
                    )}
                  </div>
                </article>
              );
            })}
          </div>
        ) : (
          <div
            style={{
              padding: "48px",
              background: "#fff",
              borderRadius: "16px",
              textAlign: "center",
              border: "1px dashed var(--slate-300)",
            }}
          >
            <div style={{ fontSize: "40px", marginBottom: "12px" }}>📝</div>
            <h3 style={{ fontSize: "18px", fontWeight: 700, color: "var(--slate-900)" }}>
              Belum Ada Riwayat Ujian
            </h3>
            <p style={{ color: "var(--slate-600)", fontSize: "14px", marginTop: "4px", marginBottom: "20px" }}>
              Mulai simulasi CAT pertama Anda untuk mengukur skor dan peluang kelulusan SKD.
            </p>
            <Link
              href="/tryouts"
              style={{
                background: "var(--gold-primary)",
                color: "#fff",
                padding: "12px 24px",
                borderRadius: "10px",
                fontWeight: 700,
                fontSize: "14px",
                display: "inline-block",
              }}
            >
              Mulai Ujian Sekarang →
            </Link>
          </div>
        )}
      </div>
    </main>
  );
}
