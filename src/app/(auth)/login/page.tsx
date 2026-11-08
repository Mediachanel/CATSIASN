"use client";

import Link from "next/link";
import { useState } from "react";
import { useSearchParams } from "next/navigation";
import { login } from "../actions";

const ERROR_MESSAGES: Record<string, string> = {
  credentials: "Email atau kata sandi salah. Periksa kembali dan coba lagi.",
  default: "Terjadi kesalahan. Silakan coba beberapa saat lagi.",
};

export default function LoginPage() {
  const searchParams = useSearchParams();
  const errorKey = searchParams.get("error") ?? "";
  const justRegistered = searchParams.get("registered") === "1";

  const [role, setRole] = useState<"USER" | "ADMIN">("USER");
  const [email, setEmail] = useState("peserta@gmail.com");
  const [password, setPassword] = useState("user12345678");
  const [loading, setLoading] = useState(false);

  const errorMsg = errorKey ? (ERROR_MESSAGES[errorKey] ?? ERROR_MESSAGES.default) : null;

  const handleSelectRole = (selectedRole: "USER" | "ADMIN") => {
    setRole(selectedRole);
    if (selectedRole === "ADMIN") {
      setEmail("admin@cpns.go.id");
      setPassword("admin12345678");
    } else {
      setEmail("peserta@gmail.com");
      setPassword("user12345678");
    }
  };

  return (
    <main className="auth-shell">
      <section className="auth-card">
        {/* Logo + Heading */}
        <div className="auth-header-center">
          <Link href="/" style={{ display: "inline-flex", alignItems: "center", gap: "10px", marginBottom: "12px" }}>
            <img
              src="/cpns_launcher_icon_1794011079614.jpg"
              alt="Logo"
              style={{ width: "38px", height: "38px", borderRadius: "8px" }}
            />
            <span style={{ fontWeight: 800, fontSize: "16px", color: "var(--navy-primary)" }}>
              CAT <span style={{ color: "var(--gold-primary)" }}>CPNS PRO</span>
            </span>
          </Link>
          <h1>Masuk ke Akun</h1>
          <p>Pilih peran dan masuk untuk melanjutkan simulasi SKD</p>
        </div>

        {/* Success banner (after register) */}
        {justRegistered && (
          <div className="auth-banner auth-banner-success">
            ✅ Akun berhasil dibuat! Masuk menggunakan email dan kata sandi yang baru saja Anda daftarkan.
          </div>
        )}

        {/* Error banner */}
        {errorMsg && (
          <div className="auth-banner auth-banner-error">
            ⚠️ {errorMsg}
          </div>
        )}

        {/* Dual-role Tabs */}
        <div className="demo-tabs-pill">
          <button
            type="button"
            className={`tab-btn ${role === "USER" ? "active" : ""}`}
            onClick={() => handleSelectRole("USER")}
          >
            👤 Peserta CPNS
          </button>
          <button
            type="button"
            className={`tab-btn ${role === "ADMIN" ? "active" : ""}`}
            onClick={() => handleSelectRole("ADMIN")}
          >
            🛡️ Pengelola (Admin)
          </button>
        </div>

        <form action={login} onSubmit={() => setLoading(true)}>
          <div className="form-group">
            <label htmlFor="email">Alamat Email</label>
            <input
              id="email"
              name="email"
              type="email"
              autoComplete="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="nama@email.com"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Kata Sandi</label>
            <input
              id="password"
              name="password"
              type="password"
              autoComplete="current-password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••••••"
            />
          </div>

          <button className="btn-submit-auth" type="submit" disabled={loading}>
            {loading
              ? "Memverifikasi…"
              : role === "ADMIN"
              ? "Masuk Sebagai Administrator"
              : "Masuk ke Simulasi CAT →"}
          </button>
        </form>

        {/* Quick Demo Fill */}
        <button
          type="button"
          className="btn-quick-demo"
          disabled={loading}
          onClick={() => {
            setEmail(role === "ADMIN" ? "admin@cpns.go.id" : "peserta@gmail.com");
            setPassword(role === "ADMIN" ? "admin12345678" : "user12345678");
          }}
        >
          ⚡ Isi Otomatis Kredensial Demo ({role === "ADMIN" ? "Admin" : "Peserta"})
        </button>

        <div className="auth-footer-note">
          Belum memiliki akun? <Link href="/register">Daftar sekarang</Link>
        </div>
      </section>
    </main>
  );
}
