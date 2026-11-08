"use client";

import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useState } from "react";
import { register } from "../actions";

const ERROR_MESSAGES: Record<string, string> = {
  invalid: "Data yang dimasukkan tidak valid. Periksa kembali format nama, email, dan kata sandi (minimal 12 karakter).",
  unavailable: "Email ini sudah terdaftar atau layanan sedang tidak tersedia. Gunakan email lain atau coba lagi nanti.",
  default: "Terjadi kesalahan. Silakan coba beberapa saat lagi.",
};

export default function RegisterPage() {
  const searchParams = useSearchParams();
  const errorKey = searchParams.get("error") ?? "";
  const errorMsg = errorKey ? (ERROR_MESSAGES[errorKey] ?? ERROR_MESSAGES.default) : null;

  const [loading, setLoading] = useState(false);
  const [pwStrength, setPwStrength] = useState<"" | "weak" | "medium" | "strong">("");

  function checkStrength(val: string) {
    if (!val) { setPwStrength(""); return; }
    if (val.length < 12) { setPwStrength("weak"); return; }
    const hasLower = /[a-z]/.test(val);
    const hasUpper = /[A-Z]/.test(val);
    const hasNum = /[0-9]/.test(val);
    const hasSpecial = /[^a-zA-Z0-9]/.test(val);
    const score = [hasLower, hasUpper, hasNum, hasSpecial].filter(Boolean).length;
    setPwStrength(score >= 3 ? "strong" : "medium");
  }

  const strengthLabel = { weak: "Lemah (min. 12 karakter)", medium: "Cukup", strong: "Kuat ✓" };
  const strengthColor = { weak: "var(--crimson-red)", medium: "var(--gold-primary)", strong: "var(--emerald-success)" };

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
          <h1>Daftar Akun Peserta</h1>
          <p>Mulai persiapan simulasi CAT SKD dengan standar resmi BKN</p>
        </div>

        {/* Error banner */}
        {errorMsg && (
          <div className="auth-banner auth-banner-error">
            ⚠️ {errorMsg}
          </div>
        )}

        <form action={register} onSubmit={() => setLoading(true)}>
          <div className="form-group">
            <label htmlFor="name">Nama Lengkap</label>
            <input
              id="name"
              name="name"
              autoComplete="name"
              required
              minLength={2}
              maxLength={100}
              placeholder="Contoh: Rian Pratama, S.Kom"
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Alamat Email</label>
            <input
              id="email"
              name="email"
              type="email"
              autoComplete="email"
              required
              maxLength={254}
              placeholder="nama@email.com"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">
              Kata Sandi{" "}
              {pwStrength && (
                <span style={{ fontWeight: 700, color: strengthColor[pwStrength] }}>
                  — {strengthLabel[pwStrength]}
                </span>
              )}
            </label>
            <input
              id="password"
              name="password"
              type="password"
              autoComplete="new-password"
              required
              minLength={12}
              maxLength={128}
              placeholder="Minimal 12 karakter (contoh: Rahasia12345!)"
              onChange={(e) => checkStrength(e.target.value)}
            />
            {pwStrength && (
              <div style={{ marginTop: "6px", height: "4px", borderRadius: "999px", background: "var(--slate-200)", overflow: "hidden" }}>
                <div
                  style={{
                    height: "100%",
                    borderRadius: "999px",
                    background: strengthColor[pwStrength],
                    width: pwStrength === "weak" ? "33%" : pwStrength === "medium" ? "66%" : "100%",
                    transition: "width 0.3s ease",
                  }}
                />
              </div>
            )}
            <span style={{ fontSize: "11px", color: "var(--slate-600)", marginTop: "6px", display: "block" }}>
              💡 Gunakan kombinasi huruf besar/kecil, angka, dan simbol.
            </span>
          </div>

          <button
            className="btn-submit-auth"
            type="submit"
            disabled={loading}
            style={{ background: "var(--gold-primary)" }}
          >
            {loading ? "Mendaftarkan akun…" : "Buat Akun Peserta Baru →"}
          </button>
        </form>

        <div className="auth-footer-note">
          Sudah memiliki akun? <Link href="/login">Masuk ke akun</Link>
        </div>
      </section>
    </main>
  );
}
