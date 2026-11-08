# Audit awal bank soal — 8 November 2026

## Cakupan

Audit ini membaca tiga file `bank_soal_3000*_DRAFT.json` di workspace dan memeriksa alur impor serta tampilan aplikasi. Database PostgreSQL belum dapat diaudit: koneksi aplikasi pada port 55432 tidak tersedia dan kredensial konfigurasi aplikasi ditolak oleh PostgreSQL lokal pada port 5432. Karena itu, jumlah dan isi database saat ini tidak dapat dipastikan dari hasil ini.

## Hasil audit file sumber

- Total 9.000 baris: 2.250 TWK, 3.150 TIU, dan 3.600 TKP. Seluruhnya berstatus DRAFT dan bertipe TEXT.
- Semua baris memiliki teks soal, lima opsi tidak kosong, dan teks pembahasan tidak kosong. Keberadaan teks bukan bukti kebenaran atau kualitas pembahasan.
- Setelah normalisasi spasi dan huruf besar-kecil, ada 6.648 teks soal unik dan 2.352 baris yang mengulang teks soal lain. Hasil ini tidak mencakup variasi soal yang hanya mengubah kalimat pengantar.
- Jumlah pembahasan unik adalah 2.467. Secara kategori ada 30 pembahasan unik untuk 2.250 soal TWK dan 38 untuk 3.600 soal TKP; banyak soal merupakan variasi dari materi yang sama dan pembahasannya perlu ditinjau agar sesuai dengan masing-masing opsi.
- Sebagai contoh, TWK-3001, TWK-3002 dan TWK-3003 menanyakan letak rumusan Pancasila dalam UUD 1945 dengan pengantar berbeda tetapi substansi dan pembahasan sama.
- Ada 285 soal bertopik Geometri dalam file. Contoh yang diperiksa adalah hitungan keliling persegi panjang dalam teks; topik geometri saja tidak membuktikan bahwa setiap soal membutuhkan gambar.
- Semua 9.000 baris memiliki `media_status=NOT_REQUIRED`; kolom gambar soal, semua opsi, dan pembahasan kosong. Tidak ditemukan kata kunci gambar/diagram/grafik/bangun ruang/kubus/balok/jaring-jaring pada teks soal dalam pemeriksaan sederhana. Ini bukan pengganti penelaahan substantif seluruh soal.

## Aplikasi dan impor

- Tabel `question_versions` memiliki kolom `explanation`, dan import menyimpan teks pembahasan dari file.
- Halaman hasil saat ini hanya menyatakan bahwa pembahasan akan tersedia setelah event diverifikasi; ia belum menampilkan teks pembahasan per soal.
- Skema pertanyaan dan opsi belum memiliki kolom media; importer tidak menyimpan kolom gambar pada file. Endpoint dan tampilan ujian juga belum menampilkan gambar soal atau pilihan gambar.
- Semua baris diimpor sebagai DRAFT/isDemo dan dokumen proyek menyebut bank soal belum ditinjau ahli. Jangan menganggapnya bank soal resmi atau tervalidasi.

## Tindak lanjut

1. Hubungkan ke database pengembangan yang benar dengan akun yang memiliki hak baca, lalu ulangi penghitungan pada data yang benar-benar digunakan aplikasi.
2. Tinjau duplikasi substantif, kunci jawaban, bobot TKP dan kecocokan pembahasan; jangan hanya menghapus soal berdasarkan teks yang mirip.
3. Tambahkan jenis soal visual yang benar-benar memerlukan gambar, bersama aset soal/opsi/pembahasan yang dapat diakses, teks alternatif, dan uji tampilan seluler. Setelahnya tambahkan dukungan media pada skema, impor, API, dan tampilan.
4. Tampilkan pembahasan dan jawaban hanya setelah sesi berakhir, dengan tetap menjaga kunci jawaban tidak dikirim saat ujian berlangsung.
