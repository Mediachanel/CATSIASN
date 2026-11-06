# Status implementasi

Pembaruan 2026-11-06. Ini checkpoint parsial, bukan MVP atau penyelesaian Phase 1.

- Phase 0: PRD, arsitektur, ERD dan UI/UX awal tersedia; dokumen lengkap masih perlu dilanjutkan.
- Phase 1: halaman publik, skema dan client Prisma awal, dua migrasi, seed dua paket contoh, PostgreSQL dan Redis lokal. Tabel awal mencakup user, session, reset token, paket, subscription, usage, pertanyaan/versi/opsi, dan audit. Model tersebut belum berarti fitur aplikasi sudah tersedia.
- Registrasi/login/logout peserta menggunakan Auth.js Credentials, Argon2id, JWT dan rate limit per email pada Redis tersedia untuk pengujian lokal. MFA, RBAC admin, dan MinIO belum berfungsi. App dan worker tersedia di Compose; worker baru menerima job pemeriksaan. Image MinIO tidak dapat diunduh dari registry; profil storage belum teruji. Auth.js tersedia pada /api/v1/auth; tidak ada API admin.
- Engine ujian peserta tersedia untuk event yang diterbitkan: sistem memilih komposisi soal per kategori, membuat snapshot urutan soal dan opsi per sesi, menyimpan jawaban, menghitung timer dari deadline server, menilai di server, dan menyimpan hasil TWK/TIU/TKP/total. Peserta dapat melanjutkan sesi yang belum habis. Kunci, bobot, dan pembahasan tidak dikirim ke browser selama sesi.
- Halaman depan, daftar tryout, dashboard, ruang ujian, dan hasil telah dirombak dari preview menjadi alur peserta yang tersambung ke database. Event `DEMO-SKD-110` sengaja diberi label internal karena memakai soal DRAFT.
- Bank soal terverifikasi, pembayaran, keanggotaan operasional, admin, reset password, MFA, dan klaim production readiness belum tersedia.
- Target 3.000 soal terverifikasi belum tersedia. Kapasitas 1.000 peserta belum diuji.
- Tiga batch draft bank soal sudah diimpor ke database: 9.000 soal, 9.000 versi, dan 45.000 opsi. Seluruhnya berstatus DRAFT dan `isDemo=true`; belum terverifikasi, belum dipublikasikan, dan bukan soal resmi BKN.
- Audit visual tahap kedua pada resolusi 1366×768 lulus untuk landing dan login. Ruang ujian kini memakai layout kerja dua kolom dengan timer, progres, opsi, dan navigasi soal yang hidup; pengujian browser alur penuh masih menunggu PostgreSQL/Redis lokal yang dikonfigurasi aplikasi aktif.

## Bukti checkpoint database

Docker Desktop diaktifkan; PostgreSQL dan Redis sehat pada Compose. PostgreSQL host menggunakan port 55432 karena 5432 sudah ditempati. Prisma validate dan dua migrasi berhasil; migrate status up to date. Seed diulang dan integration check memverifikasi dua paket tersedia serta check constraint menolak harga negatif. Migrasi kedua sempat gagal karena BOM UTF-8; status ditandai rolled back dan migrasi yang diperbaiki berhasil diterapkan. Tidak ada pengujian integrasi auth, browser, atau beban.

Lint, typecheck, empat tes scoring, dan build baseline diperiksa kembali; lihat hasil akhir pada commit dan output pengujian. Audit terbaru melaporkan 11 kerentanan: 3 high pada dependensi Prisma/deepmerge-ts dan 8 moderate pada dependensi ESLint/minimatch; `npm audit --omit=dev` juga melaporkan 3 high karena relasi dependensi Prisma. Penyelesaian yang ditawarkan npm memerlukan upgrade major; belum diperbaiki dan merupakan blocker produksi. MinIO upstream tidak dapat diunduh. Tidak ada secret yang dicommit.

## Checkpoint autentikasi

Auth.js v5 saat ini berlabel beta. Pendaftaran divalidasi Zod, password disimpan sebagai Argon2id, login dibatasi per alamat email dan dashboard memeriksa sesi di server. Pengujian Playwright pada Edge lokal lulus untuk redirect tanpa sesi, registrasi, hash, password salah, login dan logout; akun uji dihapus setelah tes. Auth.js menggunakan JWT, bukan tabel auth_sessions; pencabutan sesi terpusat belum tersedia. Limit saat ini per email saja, belum perlindungan berbasis IP/global, dan dapat digunakan untuk mengunci akun; perlindungan abuse dan audit perlu diperkuat. Reset password, verifikasi email, MFA dan RBAC admin belum tersedia. Layanan masih belum siap produksi.

## Checkpoint Compose dan worker

App, PostgreSQL, Redis, dan worker berhasil dijalankan pada Docker Compose. Migrasi di container sudah terkini. Endpoint liveness dan readiness mengembalikan status ok saat diuji; readiness hanya menguji koneksi PostgreSQL dan Redis, bukan versi skema atau worker. Job health-check BullMQ berhasil diproses oleh worker container. Playwright terhadap app container lulus registrasi, password salah, login, dashboard, dan logout. Build awal gagal karena kedua service bersaing memberi nama image yang sama; konfigurasi diperbaiki agar image hanya dibangun sekali. Pengujian browser awal menemukan `UntrustedHost`; Compose lokal dikonfigurasi untuk mempercayai host dan pengujian ulang lulus. Pengaturan host harus ditinjau saat memakai reverse proxy produksi.

Sembilan tes unit lulus, termasuk respons readiness ketika dependency gagal. Audit produksi masih melaporkan 3 high pada rantai Prisma; belum diperbaiki. MinIO belum tersedia; worker belum menjalankan job bisnis, belum ada metrics lengkap, backup, atau uji beban.

## Checkpoint import bank soal

`npm run db:import-question-bank` berhasil memvalidasi dan mengimpor `bank_soal_3000_DRAFT.json`, `bank_soal_3000_BATCH02_DRAFT.json`, dan `bank_soal_3000_BATCH03_DRAFT.json`. Masing-masing berisi 3.000 baris: TWK 750, TIU 1.050, TKP 1.200. Kode soal di ketiga file unik. TKP disimpan tanpa kunci tunggal dan dengan lima bobot opsi; kolom media belum menjadi asset storage. Import ulang berhasil tanpa menambah jumlah record, sehingga checkpoint ini idempotent. Karena source label menyatakan generated/internal dan catatan review masih ada, target 3.000 soal terverifikasi belum boleh dianggap tercapai.
