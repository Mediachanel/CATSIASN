import Link from "next/link";
import { redirect } from "next/navigation";
import { auth } from "../../../auth";
import { db } from "../../../lib/db/client";
import { startTryout } from "./actions";

export default async function TryoutsPage() {
  const session = await auth();
  if (!session?.user?.id) redirect("/login");

  const [user, events] = await Promise.all([
    db.user.findUnique({ where: { id: session.user.id }, select: { role: true } }),
    db.tryoutEvent.findMany({
      where: { status: "PUBLISHED" },
      orderBy: { createdAt: "desc" },
    }),
  ]);

  return (
    <main style={{ minHeight: "100vh", background: "var(--slate-100)" }}>
      {/* ==================== HEADER ==================== */}
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
            <p>Katalog Tryout SKD Resmi</p>
          </div>
        </Link>
        <div className="nav-actions">
          {user?.role === "ADMIN" && (
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
          <Link href="/dashboard" className="btn-ghost">
            Dashboard
          </Link>
        </div>
      </header>

      {/* ==================== HERO HEADER ==================== */}
      <section style={{ background: "linear-gradient(135deg, var(--navy-dark) 0%, var(--navy-primary) 100%)", color: "#fff", padding: "48px 5%" }}>
        <div style={{ maxWidth: "1240px", margin: "0 auto" }}>
          <span style={{ background: "var(--gold-primary)", color: "#fff", padding: "4px 12px", borderRadius: "6px", fontSize: "11px", fontWeight: 800, letterSpacing: "0.08em" }}>
            SIMULASI CAT BKN RESMI
          </span>
          <h1 style={{ fontSize: "36px", fontWeight: 800, margin: "14px 0 8px", letterSpacing: "-0.03em" }}>
            Pilih Paket Tryout SKD
          </h1>
          <p style={{ color: "#cbd5e1", fontSize: "15px", maxWidth: "680px" }}>
            Setiap paket dilengkapi timer 100 menit, snapshot acak 110 soal HOTS, evaluasi passing grade BKN (TWK 65, TIU 80, TKP 166), dan pembahasan lengkap.
          </p>
        </div>
      </section>

      {/* ==================== EVENT CARDS ==================== */}
      <div style={{ maxWidth: "1240px", margin: "32px auto", padding: "0 20px" }}>
        <div style={{ display: "grid", gap: "20px" }}>
          {events.map((event) => (
            <article
              key={event.id}
              style={{
                background: "#fff",
                borderRadius: "16px",
                padding: "28px",
                border: "1px solid var(--slate-200)",
                boxShadow: "0 4px 16px rgba(15, 23, 42, 0.05)",
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                flexWrap: "wrap",
                gap: "24px",
              }}
            >
              <div style={{ flex: 1, minWidth: "280px" }}>
                <span
                  style={{
                    fontSize: "11px",
                    fontWeight: 800,
                    color: event.isInternalDemo ? "var(--gold-primary)" : "var(--emerald-success)",
                    letterSpacing: "0.08em",
                    textTransform: "uppercase",
                  }}
                >
                  {event.isInternalDemo ? "★ Simulasi Internal Resmi" : "★ Tryout Nasional"}
                </span>
                <h2 style={{ fontSize: "22px", fontWeight: 800, margin: "6px 0 8px", color: "var(--slate-900)" }}>
                  {event.title}
                </h2>
                <p style={{ color: "var(--slate-600)", fontSize: "14px", lineHeight: "1.6", maxWidth: "600px" }}>
                  {event.description}
                </p>
                <div style={{ display: "flex", gap: "10px", marginTop: "14px", flexWrap: "wrap" }}>
                  <span style={{ background: "var(--navy-soft)", color: "var(--navy-primary)", padding: "4px 10px", borderRadius: "6px", fontSize: "12px", fontWeight: 700 }}>
                    🏛️ TWK: {event.twkCount} Soal
                  </span>
                  <span style={{ background: "var(--emerald-soft)", color: "var(--emerald-success)", padding: "4px 10px", borderRadius: "6px", fontSize: "12px", fontWeight: 700 }}>
                    📐 TIU: {event.tiuCount} Soal
                  </span>
                  <span style={{ background: "var(--gold-soft)", color: "var(--gold-primary)", padding: "4px 10px", borderRadius: "6px", fontSize: "12px", fontWeight: 700 }}>
                    🌟 TKP: {event.tkpCount} Soal
                  </span>
                </div>
              </div>

              <div style={{ textAlign: "right", minWidth: "180px" }}>
                <div style={{ fontSize: "28px", fontWeight: 800, color: "var(--navy-primary)" }}>
                  {event.durationMinutes}{" "}
                  <span style={{ fontSize: "14px", fontWeight: 600, color: "var(--slate-600)" }}>
                    Menit
                  </span>
                </div>
                <div style={{ fontSize: "12px", color: "var(--slate-600)", marginBottom: "14px" }}>
                  Total {event.twkCount + event.tiuCount + event.tkpCount} Butir Soal
                </div>
                <form action={startTryout}>
                  <input type="hidden" name="eventCode" value={event.code} />
                  <button
                    type="submit"
                    style={{
                      background: "var(--gold-primary)",
                      color: "#fff",
                      border: 0,
                      borderRadius: "10px",
                      padding: "12px 24px",
                      fontWeight: 700,
                      fontSize: "14px",
                      cursor: "pointer",
                      boxShadow: "0 4px 12px rgba(217, 119, 6, 0.3)",
                    }}
                  >
                    Mulai Ujian Sekarang →
                  </button>
                </form>
              </div>
            </article>
          ))}
        </div>
      </div>
    </main>
  );
}
