# CAT Nusantara

Platform latihan CAT CPNS independen. Alur peserta yang tersedia mencakup akun, pemilihan simulasi, sesi SKD 110 soal, penyimpanan jawaban, timer, dan hasil skor. Ini bukan aplikasi resmi atau afiliasi BKN; event internal memakai bank soal DRAFT sampai lolos validasi ahli. Lihat [status implementasi](docs/IMPLEMENTATION_STATUS.md).

## Setup lokal

Prasyarat: Node.js 24, npm, PostgreSQL, dan Redis yang berjalan secara lokal. Salin `.env.example` menjadi `.env`, ganti password lokal, buat `AUTH_SECRET` acak dengan perintah pada `.env.example`, lalu pastikan `DATABASE_URL` dan `REDIS_URL` mengarah ke layanan lokal Anda. Contoh ini hanya untuk pengembangan.

```powershell
npm ci
npm run db:generate
npm run db:migrate
npm run db:seed
npm run dev -- --hostname 127.0.0.1 --port 3100
```

Buka http://localhost:3100. Seed menambahkan event internal SKD 110 soal bila belum ada. Jangan menjalankan migrasi terhadap database produksi.

Pemeriksaan: `npm run lint`, `npm run typecheck`, `npm test`, `npm run build`, `npm run test:integration` (memerlukan database yang sudah dimigrasi dan di-seed), serta `npm run test:e2e` (juga memerlukan Chromium Playwright dan database/Redis aktif). Registrasi/login serta sesi ujian peserta tersedia untuk pengujian lokal; reset password, MFA, RBAC admin, bank soal tervalidasi, pembayaran, dan fitur operasional belum tersedia. Jangan gunakan pada produksi.

Lihat [PRD](docs/PRD.md), [arsitektur](docs/ARCHITECTURE.md), [ERD](docs/ERD.md), dan [UI/UX](docs/UI_UX.md).

## Import bank soal draft

Tiga file `bank_soal_3000_DRAFT.json`, `bank_soal_3000_BATCH02_DRAFT.json`, dan `bank_soal_3000_BATCH03_DRAFT.json` di folder proyek dapat diimpor setelah migrasi dengan `npm run db:import-question-bank`. Perintah ini memvalidasi dan menyimpan seluruh 9.000 baris sebagai DRAFT internal. Jalankan `npm run test:integration` untuk memeriksa jumlah batch, soal, versi, opsi, distribusi kategori, dan bobot TKP.

## Tes autentikasi browser

Setelah database dan Redis siap, jalankan `npx playwright install chromium` sekali, lalu `npm run test:e2e`. Pengujian membuat akun unik dan menghapusnya setelah selesai; gunakan hanya database pengembangan. Pada Windows pengujian juga dapat menggunakan Edge yang sudah terpasang dengan mengatur `PLAYWRIGHT_BROWSER_PATH` ke path executable browser.

## Menjalankan app dan worker dalam Compose

Setel `DATABASE_URL_DOCKER` di `.env` agar memakai host `postgres` dan kredensial yang sama dengan layanan PostgreSQL; karakter khusus dalam kredensial URL harus di-encode. Setelah Docker aktif, jalankan `docker compose up -d --build --wait app worker`, kemudian `docker compose exec app npm run db:migrate` dan `docker compose exec app npm run db:seed`. Aplikasi tersedia pada http://localhost:3000. Pengujian worker menggunakan `npm run test:worker` pada host dengan Redis lokal yang sama. Jangan menjalankan pemeriksaan ini pada sistem produksi.

Endpoint `/api/v1/health/live` memeriksa proses HTTP; `/api/v1/health/ready` memeriksa koneksi PostgreSQL dan Redis, bukan kelengkapan migrasi atau kesehatan worker. Batasi endpoint ini pada jaringan internal saat deployment. Worker belum menjalankan job bisnis.

Untuk menjalankan tes browser terhadap app Compose yang sedang aktif, tetapkan `E2E_COMPOSE=1` sebelum `npm run test:e2e`. Pengujian menulis dan menghapus satu akun uji pada database yang ditunjuk `DATABASE_URL`; gunakan database pengembangan yang sama dengan `DATABASE_URL_DOCKER`.
