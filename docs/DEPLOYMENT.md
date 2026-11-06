# Deployment lokal

Lihat README untuk konfigurasi `.env`, build, migrasi, dan seed. App, worker, PostgreSQL, serta Redis disediakan Compose; PostgreSQL, Redis, dan app hanya diterbitkan ke localhost. `DATABASE_URL` untuk proses host dan `DATABASE_URL_DOCKER` untuk app container harus mengarah ke database yang sama. Migrasi dan seed dijalankan eksplisit setelah layanan tersedia; readiness belum memverifikasi versi skema. Worker baru mendukung job health-check.

MinIO adalah profil opsional yang masih terblokir karena image registry tidak tersedia. Contoh `.env` berisi kredensial dummy dan harus diganti; `.env` tidak masuk Git. Untuk staging dan produksi, HTTPS, secret manager, database dan storage terkelola, backup, restore drill, migrasi terkontrol, keamanan, monitoring dan job bisnis masih perlu dirancang dan diuji. Jangan men-deploy baseline ini untuk pengguna.

Compose lokal menetapkan `AUTH_TRUST_HOST=true` dan hanya menerbitkan app pada loopback. Untuk produksi, gunakan proxy tepercaya yang memvalidasi dan mengganti header host/forwarded, dan uji konfigurasi Auth.js; jangan langsung mengekspos pengaturan lokal ini.
