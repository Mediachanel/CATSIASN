# Keamanan: status awal

Pendaftaran memvalidasi input dan menyimpan password dengan Argon2id. Auth.js mengelola cookie dan sesi JWT; endpoint dashboard memeriksa sesi di server. Next.js Server Actions dan Auth.js menyediakan pemeriksaan origin/CSRF pada alur masing-masing; konfigurasi reverse proxy dan HTTPS harus diuji saat deployment. Rate limit Redis bersifat per email dan fail closed jika Redis tidak tersedia.

Kontrol yang belum tersedia: MFA admin, RBAC admin dan audit tindakan, verifikasi email, pemulihan password, pencabutan sesi terpusat, rate limit IP/global dan perlindungan penyalahgunaan registrasi, kebijakan privasi/retensi, pengelolaan media aman, header dan HTTPS produksi, serta pengujian keamanan menyeluruh. Tidak ada akses admin aktif. Jangan deploy aplikasi ini untuk pengguna nyata. AUTH_SECRET harus acak, privat dan unik per environment. Risiko dependensi tercatat di IMPLEMENTATION_STATUS.md.
