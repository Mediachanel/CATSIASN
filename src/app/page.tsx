import Link from "next/link";

export default function Home() {
  return (
    <main>
      {/* ==================== TOP NAVIGATION ==================== */}
      <nav className="main-nav">
        <Link className="brand-badge-group" href="/">
          <img
            src="/cpns_launcher_icon_1794011079614.jpg"
            alt="Logo CAT CPNS Pro"
            className="brand-logo-img"
          />
          <div className="brand-text-block">
            <h2>
              CAT <span>CPNS PRO</span>
            </h2>
            <p>Simulasi Resmi Standar BKN</p>
          </div>
        </Link>

        <div className="nav-links">
          <Link href="/paket" className="nav-link">
            Paket Berlangganan
          </Link>
          <Link href="/tryouts" className="nav-link">
            Paket Tryout
          </Link>
          <Link href="/exam" className="nav-link">
            Simulasi CAT
          </Link>
          <a href="#passing-grade" className="nav-link">
            Passing Grade
          </a>
          <a href="#materi" className="nav-link">
            Materi SKD
          </a>
        </div>

        <div className="nav-actions">
          <Link href="/login" className="btn-ghost">
            Masuk
          </Link>
          <Link href="/register" className="btn-primary-nav">
            Daftar Gratis
          </Link>
        </div>
      </nav>

      <div className="home-container">
        {/* ==================== HERO BANNER ==================== */}
        <section className="hero-banner-wrapper">
          <img
            src="/img_cpns_hero.jpg"
            alt="Simulasi CAT CPNS BKN"
            className="hero-banner-img"
          />
          <div className="hero-overlay">
            <span className="hero-pill-badge">
              ★ STANDAR RESMI BKN 2026 / 2027
            </span>
            <h1>Simulasi Sistem CAT CPNS</h1>
            <p>
              Ujian acak berwaktu real-time, live passing grade SKD, grafik radar kompetensi, dan evaluasi pembahasan 110 soal HOTS sesuai KepmenPAN-RB.
            </p>
          </div>
        </section>

        {/* ==================== CTA & PASSING GRADE ROW ==================== */}
        <section className="cta-grid">
          {/* LEFT: Start Exam CTA */}
          <div className="exam-cta-card">
            <div>
              <div className="cta-header-top">
                <div>
                  <span className="cta-tag">FITUR UJIAN RESMI TERBARU</span>
                  <h3>Simulasi CAT SKD Acak</h3>
                  <p>
                    Komposisi resmi 110 soal berbeda setiap sesi ujian (30 TWK, 35 TIU, 45 TKP) dengan batas waktu 100 menit.
                  </p>
                </div>
                <div className="cta-icon-badge">⚡</div>
              </div>
            </div>

            <div className="cta-button-row">
              <Link href="/tryouts" className="btn-start-exam">
                Mulai Ujian Acak Baru →
              </Link>
              <Link href="/tryouts" className="btn-outline-exam">
                Pilih Paket
              </Link>
            </div>
          </div>

          {/* RIGHT: Passing Grade Reference */}
          <div className="passing-grade-card" id="passing-grade">
            <div className="card-title-head">
              <h3>Ambang Batas Resmi BKN</h3>
              <span className="cta-tag" style={{ background: "var(--navy-primary)" }}>
                SKD 550 POIN
              </span>
            </div>

            <div className="subtest-pills-list">
              <div className="subtest-row">
                <div className="subtest-info">
                  <h4>TWK (Wawasan Kebangsaan)</h4>
                  <span>30 Soal • Skor Maksimal 150</span>
                </div>
                <div className="subtest-score-badge">
                  <div className="score-pg">65</div>
                  <span className="score-max">Min Passing Grade</span>
                </div>
              </div>

              <div className="subtest-row">
                <div className="subtest-info">
                  <h4>TIU (Inteligensia Umum)</h4>
                  <span>35 Soal • Skor Maksimal 175</span>
                </div>
                <div className="subtest-score-badge">
                  <div className="score-pg">80</div>
                  <span className="score-max">Min Passing Grade</span>
                </div>
              </div>

              <div className="subtest-row">
                <div className="subtest-info">
                  <h4>TKP (Karakteristik Pribadi)</h4>
                  <span>45 Soal • Skor Maksimal 225</span>
                </div>
                <div className="subtest-score-badge">
                  <div className="score-pg">166</div>
                  <span className="score-max">Min Passing Grade</span>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* ==================== SUBTEST DRILLS ==================== */}
        <section id="materi">
          <div className="section-title-wrap">
            <h2>Drill Mandiri per Subtes</h2>
            <p>Perdalam kelemahan topik spesifik untuk melampaui passing grade</p>
          </div>

          <div className="features-grid-3">
            <div className="feature-box-card">
              <div className="feature-badge-icon badge-twk">🏛️</div>
              <h4>Drill Khusus TWK</h4>
              <p>
                Kuasai 45 butir pengamalan Pancasila, pemahaman pasal UUD 1945, wewenang lembaga negara, dan nilai bela negara non-militer.
              </p>
              <Link href="/tryouts" className="feature-action-link">
                Mulai Latihan TWK →
              </Link>
            </div>

            <div className="feature-box-card">
              <div className="feature-badge-icon badge-tiu">📐</div>
              <h4>Drill Khusus TIU</h4>
              <p>
                Latih kecepatan penarikan kesimpulan silogisme, trik deret angka bertingkat, dan rumus cepat soal cerita perbandingan & waktu kerja.
              </p>
              <Link href="/tryouts" className="feature-action-link">
                Mulai Latihan TIU →
              </Link>
            </div>

            <div className="feature-box-card">
              <div className="feature-badge-icon badge-tkp">🌟</div>
              <h4>Drill Khusus TKP</h4>
              <p>
                Pahami pola jawaban 5 poin berorientasi nilai ASN BerAKHLAK: integritas, pelayanan publik tanpa diskriminasi, dan anti-radikalisme.
              </p>
              <Link href="/tryouts" className="feature-action-link">
                Mulai Latihan TKP →
              </Link>
            </div>
          </div>
        </section>

        {/* ==================== WHY CHOOSE US ==================== */}
        <section>
          <div className="section-title-wrap">
            <h2>Keunggulan Simulasi CAT CPNS Pro</h2>
            <p>Pengalaman ujian paling mendekati sistem Computer Assisted Test BKN</p>
          </div>

          <div className="features-grid-3">
            <div className="feature-box-card">
              <div className="feature-badge-icon" style={{ background: "#fef3c7", color: "#d97706" }}>
                ⏱️
              </div>
              <h4>Timer & Auto-Submit Akurat</h4>
              <p>
                Countdown 100 menit terikat dengan deadline di server. Sesi yang terputus dapat dilanjutkan tanpa kehilangan jawaban sebelumnya.
              </p>
            </div>

            <div className="feature-box-card">
              <div className="feature-badge-icon" style={{ background: "#dbeafe", color: "#2563eb" }}>
                🔒
              </div>
              <h4>Penilaian Aman di Server</h4>
              <p>
                Kunci dan bobot jawaban tidak dikirim ke browser selama ujian berlangsung untuk menjamin integritas simulasi setara ujian asli.
              </p>
            </div>

            <div className="feature-box-card">
              <div className="feature-badge-icon" style={{ background: "#dcfce7", color: "#059669" }}>
                📊
              </div>
              <h4>Analisis Radar & Rekomendasi</h4>
              <p>
                Setelah ujian selesai, dapatkan laporan kelemahan subtes, akurasi per topik, serta pembahasan lengkap per butir soal.
              </p>
            </div>
          </div>
        </section>
      </div>

      {/* ==================== FOOTER ==================== */}
      <footer style={{ background: "var(--navy-dark)", color: "#94a3b8", padding: "40px 5%", borderTop: "1px solid #1e293b", fontSize: "13px" }}>
        <div style={{ maxWidth: "1240px", margin: "0 auto", display: "flex", justifyContent: "space-between", alignItems: "center", flexWrap: "wrap", gap: "20px" }}>
          <div>
            <strong style={{ color: "#fff", fontSize: "16px", display: "block", marginBottom: "4px" }}>
              CAT CPNS PRO
            </strong>
            <p>Platform persiapan simulasi SKD independen standar BKN dan KemenPAN-RB.</p>
          </div>
          <div>
            <p>© 2026 CAT CPNS Pro. Seluruh hak cipta dilindungi.</p>
          </div>
        </div>
      </footer>
    </main>
  );
}
