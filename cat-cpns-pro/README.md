# CAT CPNS Android

Aplikasi latihan CAT Android dengan data hasil dan penanda soal yang disimpan di perangkat. Proyek ini masih berupa prototipe dan belum siap dipublikasikan sebagai layanan ujian resmi.

## Menjalankan proyek

Buka direktori ini di Android Studio dengan JDK 17 atau lebih baru dan Android SDK yang sesuai. Sinkronkan Gradle, lalu jalankan konfigurasi `app` pada emulator atau perangkat. Dari PowerShell, gunakan `./gradlew.bat :app:assembleDebug`. Gradle dan dependensi harus dapat diunduh. Debug menggunakan keystore standar Android yang dibuat otomatis.

## Persiapan rilis

Untuk membuat Android App Bundle, siapkan upload keystore pribadi dan atur environment variable `KEYSTORE_PATH`, `STORE_PASSWORD`, dan `KEY_PASSWORD`. Alias pada konfigurasi saat ini adalah `upload`. Jalankan `./gradlew.bat :app:bundleRelease`. Simpan keystore dan kata sandinya di tempat aman; jangan memasukkannya ke repositori. Pastikan application ID sesuai akun penerbit sebelum rilis pertama. Unggah AAB melalui Google Play Console setelah pengujian perangkat dan pemenuhan persyaratan toko.

## Pekerjaan sebelum publikasi

- Login dan hak admin masih simulasi; kredensial admin tertanam dan semua pengguna dapat beralih peran. Perlu autentikasi dan otorisasi server jika dipakai sungguhan.
- Papan peringkat berisi data simulasi. Perubahan bank soal admin hanya tersimpan di memori dan hilang saat aplikasi ditutup.
- Hasil dan penanda soal disimpan lokal, bukan disinkronkan antarperangkat. Hasil baru menyimpan salinan soal untuk pembahasan; hasil lama tidak memiliki salinan ini sehingga pembahasan paket acak lama mungkin tidak sesuai.
- Tinjau akurasi soal, pengulangan soal, nilai ambang, klaim afiliasi resmi, privasi, dan seluruh alur ujian sebelum menawarkan aplikasi kepada publik.
- Belum ada build atau pengujian rilis yang terverifikasi pada lingkungan ini.
