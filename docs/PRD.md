# PRD ? CAT CPNS Membership

## Tujuan dan persona
Mendukung peserta CPNS mempersiapkan SKD melalui simulasi, evaluasi, dan keanggotaan; admin mengelola soal, event, pembayaran, kualitas, risiko dan insiden. Aplikasi independen, bukan produk resmi BKN.

## Cakupan MVP
Registrasi dan autentikasi, bank soal terverifikasi dan review, ujian berwaktu, penilaian server, hasil, entitlement dan pembayaran, serta operasi admin. Default blueprint 30 TWK, 35 TIU, 45 TKP, 100 menit; passing grade terpisah dan dapat dikonfigurasi. Non-scope: klaim kelulusan resmi dan soal fiktif sebagai bank terverifikasi.

## User stories dan acceptance
- Peserta memulai event yang diizinkan: kuota terpotong secara atomik sekali, urutan disimpan dan soal cukup.
- Peserta menjawab dan melanjutkan: jawaban tersimpan, refresh mempertahankan sesi, deadline server menentukan batas.
- Peserta submit: hasil dihitung di server dan submit berulang tidak menggandakan hasil.
- Admin menerbitkan soal: review disetujui, versi published immutable dan sesi memiliki snapshot.
- Pembeli membayar: entitlement aktif hanya setelah verifikasi gateway; webhook duplikat aman.
- Operator memantau risiko dan insiden dengan audit.

## Sasaran dan batas
3.000 soal asli tervalidasi dan 1.000 peserta serentak adalah target, bukan kondisi terpenuhi. p95 autosave <= 1 detik dan soal <= 2 detik memerlukan pengukuran pada beban yang disepakati.
