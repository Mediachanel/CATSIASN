# Status implementasi

Pembaruan 2026-11-06. Ini checkpoint parsial, bukan MVP atau penyelesaian Phase 1.

- Phase 0: PRD, arsitektur, ERD dan UI/UX awal tersedia; dokumen lengkap masih perlu dilanjutkan.
- Phase 1: halaman publik, skema dan client Prisma awal, dua migrasi, seed dua paket contoh, PostgreSQL dan Redis lokal. Tabel awal mencakup user, session, reset token, paket, subscription, usage, pertanyaan/versi/opsi, dan audit. Model tersebut belum berarti fitur aplikasi sudah tersedia.
- Registrasi/login/logout peserta menggunakan Auth.js Credentials, Argon2id, JWT dan rate limit per email pada Redis tersedia untuk pengujian lokal. MFA, RBAC admin, app/worker pada Compose, dan MinIO belum berfungsi. Image MinIO tidak dapat diunduh dari registry; profil storage belum teruji. Auth.js tersedia pada /api/v1/auth; tidak ada API admin.
- Phase 2?8: belum diimplementasikan. Tidak ada ujian, bank soal terverifikasi, pembayaran, keanggotaan operasional, admin, atau klaim production readiness.
- Target 3.000 soal terverifikasi belum tersedia. Kapasitas 1.000 peserta belum diuji.

## Bukti checkpoint database

Docker Desktop diaktifkan; PostgreSQL dan Redis sehat pada Compose. PostgreSQL host menggunakan port 55432 karena 5432 sudah ditempati. Prisma validate dan dua migrasi berhasil; migrate status up to date. Seed diulang dan integration check memverifikasi dua paket tersedia serta check constraint menolak harga negatif. Migrasi kedua sempat gagal karena BOM UTF-8; status ditandai rolled back dan migrasi yang diperbaiki berhasil diterapkan. Tidak ada pengujian integrasi auth, browser, atau beban.

Lint, typecheck, empat tes scoring, dan build baseline diperiksa kembali; lihat hasil akhir pada commit dan output pengujian. Audit terbaru melaporkan 11 kerentanan: 3 high pada dependensi Prisma/deepmerge-ts dan 8 moderate pada dependensi ESLint/minimatch; `npm audit --omit=dev` juga melaporkan 3 high karena relasi dependensi Prisma. Penyelesaian yang ditawarkan npm memerlukan upgrade major; belum diperbaiki dan merupakan blocker produksi. MinIO upstream tidak dapat diunduh. Tidak ada secret yang dicommit.

## Checkpoint autentikasi

Auth.js v5 saat ini berlabel beta. Pendaftaran divalidasi Zod, password disimpan sebagai Argon2id, login dibatasi per alamat email dan dashboard memeriksa sesi di server. Pengujian Playwright pada Edge lokal lulus untuk redirect tanpa sesi, registrasi, hash, password salah, login dan logout; akun uji dihapus setelah tes. Auth.js menggunakan JWT, bukan tabel auth_sessions; pencabutan sesi terpusat belum tersedia. Limit saat ini per email saja, belum perlindungan berbasis IP/global, dan dapat digunakan untuk mengunci akun; perlindungan abuse dan audit perlu diperkuat. Reset password, verifikasi email, MFA dan RBAC admin belum tersedia. Layanan masih belum siap produksi.
