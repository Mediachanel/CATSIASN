import Link from "next/link";
import Image from "next/image";

const plans = [
  { name: "Trial", price: "Gratis", detail: "5 soal TWK, 5 soal TIU, dan 5 soal TKP", duration: null, badge: "COBA GRATIS" },
  { name: "Standar", price: "Rp45.000", detail: "3 kali tes", duration: "Berlaku 90 hari setelah aktivasi", badge: "HARGA PROMO" },
  { name: "Premium", price: "Rp100.000", detail: "Tes tanpa batas", duration: "Berlaku 90 hari setelah aktivasi", badge: "AKSES PREMIUM" },
];

export default function PackagesPage() {
  return (
    <main style={{ minHeight: "100vh", background: "#f1f5f9" }}>
      <header className="main-nav">
        <Link className="brand-badge-group" href="/">
          <Image src="/cpns_launcher_icon_1794011079614.jpg" alt="Logo CAT CPNS Pro" width={48} height={48} className="brand-logo-img" />
          <div className="brand-text-block"><h2>CAT <span>CPNS PRO</span></h2><p>Latihan CAT CPNS</p></div>
        </Link>
        <div className="nav-actions"><Link href="/dashboard" className="btn-ghost">Dashboard</Link></div>
      </header>
      <section style={{ maxWidth: 1120, margin: "0 auto", padding: "56px 20px" }}>
        <div style={{ textAlign: "center", marginBottom: 36 }}>
          <h1 style={{ color: "#102c50", fontSize: "clamp(30px, 5vw, 44px)", margin: "0 0 12px" }}>Pilih paket latihan Anda</h1>
          <p style={{ color: "#475569" }}>Mulai dengan trial, atau pilih paket untuk latihan lebih banyak.</p>
        </div>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))", gap: 20 }}>
          {plans.map((plan) => (
            <article key={plan.name} style={{ background: "white", border: plan.name === "Standar" ? "2px solid #c8912d" : "1px solid #e2e8f0", borderRadius: 16, padding: 28, boxShadow: "0 8px 28px rgba(15,23,42,.06)" }}>
              <span style={{ color: "#9a6515", fontSize: 12, fontWeight: 800, letterSpacing: ".08em" }}>{plan.badge}</span>
              <h2 style={{ color: "#102c50", margin: "14px 0 8px" }}>{plan.name}</h2>
              <p style={{ color: "#102c50", fontSize: 30, fontWeight: 800, margin: "0 0 20px" }}>{plan.price}</p>
              <p style={{ color: "#475569", minHeight: 50, lineHeight: 1.6 }}>{plan.detail}</p>
              {plan.duration && <p style={{ color: "#475569", fontSize: 14 }}>{plan.duration}</p>}
              <p style={{ fontSize: 13, color: "#64748b", marginTop: 24 }}>Pendaftaran paket belum tersedia.</p>
            </article>
          ))}
        </div>
        <p style={{ color: "#64748b", textAlign: "center", fontSize: 14, marginTop: 24 }}>Pembayaran otomatis melalui Midtrans sedang disiapkan.</p>
      </section>
    </main>
  );
}
