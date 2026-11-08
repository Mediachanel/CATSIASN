package com.example.data.seed

import com.example.data.model.ExamCategory
import com.example.data.model.Question
import com.example.data.model.QuestionOption

object TkpQuestionBank {

    val questions: List<Question> = listOf(
        Question(
            id = "tkp_pelayanan_publik_1",
            packageId = "pool",
            number = 1,
            category = ExamCategory.TKP,
            subCategory = "Pelayanan Publik",
            questionText = "Saat Anda sedang melayani antrean warga yang cukup padat di kantor kecamatan, sistem komputer server pusat mendadak mengalami gangguan teknis (offline) dan diperkirakan baru normal 45 menit lagi. Sebagian warga mulai tampak gelisah dan mengeluh. Tindakan Anda adalah...",
            options = listOf(
                QuestionOption("A", "Menutup loket dan meminta seluruh warga untuk pulang serta kembali esok hari", 1),
                QuestionOption("B", "Menyampaikan permohonan maaf dengan ramah atas kendala server, mencatat data pemohon secara manual terlebih dahulu, dan menyiapkan ruang tunggu yang nyaman", 5),
                QuestionOption("C", "Mendiamkan situasi tersebut karena gangguan server berada di luar tanggung jawab petugas loket", 2),
                QuestionOption("D", "Memandu warga mengisi formulir berkas manual sembari menunggu teknisi merestart server", 4),
                QuestionOption("E", "Menjelaskan bahwa server sedang rusak dan meminta warga menunggu dengan tenang tanpa kepastian", 3)
            ),
            correctKey = null,
            explanation = "Pelayanan publik menuntut sikap responsif, empati, dan kemampuan mitigasi proaktif. Opsi B (poin 5) menenangkan warga secara humanis sekaligus tetap mencatat dokumen secara manual agar warga tidak menunggu sia-sia.",
            fastTips = "Poin 5 Pelayanan Publik: Ramah, solutif, ada tindakan mitigasi konkret (manual backup), tidak lepas tangan."
        ),
        Question(
            id = "tkp_pelayanan_publik_2",
            packageId = "pool",
            number = 2,
            category = ExamCategory.TKP,
            subCategory = "Pelayanan Publik",
            questionText = "Seorang pemohon izin usaha menyampaikan kritik pedas dan bernada emosional melalui saluran pengaduan resmi karena menganggap proses verifikasi berkasnya terlalu lambat dibandingkan standar operasional (SOP). Sikap Anda adalah...",
            options = listOf(
                QuestionOption("A", "Mengabaikan pengaduan tersebut karena dianggap tidak sopan dan mencemarkan nama baik instansi", 1),
                QuestionOption("B", "Menjelaskan sopan mengenai tahapan verifikasi dan segera mempercepat koordinasi dengan tim verifikator lapangan", 4),
                QuestionOption("C", "Menanggapi secara dingin dan menyuruh pemohon membaca kembali brosur alur perizinan", 2),
                QuestionOption("D", "Merespon dengan bahasa yang santun, memohon maaf atas ketidaknyamanan, memeriksa kendala status berkas di sistem, serta memberikan estimasi waktu penyelesaian yang jelas", 5),
                QuestionOption("E", "Menyerahkan keluhan tersebut kepada pimpinan tanpa melakukan pengecekan awal", 3)
            ),
            correctKey = null,
            explanation = "Aspek Pelayanan Publik menguji kesabaran dan profesionalitas menghadapi komplain masyarakat. Opsi D (poin 5) tidak terpancing emosi, memberikan penjelasan transparan, dan menyelesaikan akar masalah.",
            fastTips = "Poin 5: Jangan defensif, dengarkan dengan empati, periksa sistem, berikan kepastian waktu."
        ),
        Question(
            id = "tkp_jejaring_kerja_1",
            packageId = "pool",
            number = 3,
            category = ExamCategory.TKP,
            subCategory = "Jejaring Kerja",
            questionText = "Instansi Anda bekerja sama dengan instansi swasta dan lembaga swadaya masyarakat untuk menyelenggarakan program pengentasan kemiskinan. Dalam rapat perdana, terjadi perbedaan pandangan yang tajam mengenai alokasi anggaran dan metode survei lapangan. Peran yang Anda ambil adalah...",
            options = listOf(
                QuestionOption("A", "Memilih diam dan membiarkan kedua pihak berdebat sampai lelah", 1),
                QuestionOption("B", "Memihak kepada instansi swasta karena mereka menyediakan sebagian besar dana bantuan", 2),
                QuestionOption("C", "Menjadi fasilitator netral, merangkum poin-poin kesepakatan bersama, dan mengajak mencari titik temu yang selaras dengan tujuan program utama", 5),
                QuestionOption("D", "Mengusulkan pembentukan tim kecil perumus bersama yang beranggotakan perwakilan dari masing-masing pihak untuk menyusun kompromi teknis", 4),
                QuestionOption("E", "Menyarankan penundaan rapat sampai suasana emosi mereda beberapa hari kemudian", 3)
            ),
            correctKey = null,
            explanation = "Jejaring kerja menuntut kemampuan diplomasi, sinergi multi-pihak (stakeholder engagement), dan mengarahkan perbedaan menjadi solusi kolaboratif.",
            fastTips = "Poin 5 Jejaring Kerja: Jadilah jembatan komunikasi pemersatu (mediator) yang fokus pada tujuan bersama."
        ),
        Question(
            id = "tkp_sosial_budaya_1",
            packageId = "pool",
            number = 4,
            category = ExamCategory.TKP,
            subCategory = "Sosial Budaya",
            questionText = "Anda dimutasi ke kantor cabang di daerah terpencil yang mayoritas warganya memegang teguh tradisi adat istiadat setempat yang sangat berbeda dengan adat asal Anda. Beberapa kebiasaan lokal menuntut waktu musyawarah adat yang panjang sebelum program dinas dapat dimulai. Sikap Anda...",
            options = listOf(
                QuestionOption("A", "Menghormati tradisi adat setempat, bersilaturahmi dengan tokoh masyarakat adat untuk berdialog, dan menyelaraskan tahapan program dinas secara bijak", 5),
                QuestionOption("B", "Memaksakan program dinas harus berjalan cepat tanpa perlu meminta pertimbangan tetua adat setempat", 1),
                QuestionOption("C", "Mempelajari aturan adat melalui rekan kerja lokal agar tidak terjadi kesalahpahaman saat sosialisasi program", 4),
                QuestionOption("D", "Mengajukan mutasi kembali ke kota asal secepat mungkin karena tidak cocok dengan budaya lokal", 2),
                QuestionOption("E", "Mengikuti kegiatan adat bila diajak saja sambil tetap menjaga jarak sosial tertentu", 3)
            ),
            correctKey = null,
            explanation = "Aspek Sosial Budaya menuntut kemampuan adaptasi tinggi, penghormatan terhadap kearifan lokal (local wisdom), dan mampu menyinergikan tugas negara dengan norma masyarakat.",
            fastTips = "Poin 5 Sosial Budaya: Menghormati adat lokal, aktif membaur secara santun, dan menyelaraskan program tanpa melanggar aturan."
        ),
        Question(
            id = "tkp_tik_1",
            packageId = "pool",
            number = 5,
            category = ExamCategory.TKP,
            subCategory = "Teknologi Informasi & Komunikasi (TIK)",
            questionText = "Kementerian Anda meluncurkan platform analitik data berbasis kecerdasan buatan (AI) untuk mempercepat perumusan kebijakan publik. Kebanyakan staf di unit kerja Anda belum pernah menggunakan tools tersebut dan merasa ragu. Tindakan yang Anda lakukan adalah...",
            options = listOf(
                QuestionOption("A", "Menolak menggunakan platform tersebut karena khawatir menghasilkan data yang keliru", 1),
                QuestionOption("B", "Mempelajari fitur dan dokumentasi platform AI tersebut secara antusias, mempraktikkannya pada tugas kerja, serta berbagi tips penggunaan kepada rekan tim", 5),
                QuestionOption("C", "Menunggu instruksi resmi berupa surat perintah tugas tertulis baru bersedia mencoba", 3),
                QuestionOption("D", "Mempelajari mandiri penggunaan platform tersebut dan menggunakannya untuk menunjang kecepatan laporan kerja pribadi", 4),
                QuestionOption("E", "Menyerahkan penggunaan platform AI tersebut kepada staf honorer yang masih muda saja", 2)
            ),
            correctKey = null,
            explanation = "Aspek TIK menguji kemauan belajar teknologi mutakhir (digital literacy) dan inisiatif menjadi katalisator inovasi di tempat kerja.",
            fastTips = "Poin 5 TIK: Proaktif belajar teknologi baru, adaptif, dan menularkan ilmu kepada tim."
        ),
        Question(
            id = "tkp_profesionalisme_1",
            packageId = "pool",
            number = 6,
            category = ExamCategory.TKP,
            subCategory = "Profesionalisme",
            questionText = "Anda sedang menyelesaikan laporan audit penting yang harus diserahkan kepada pimpinan esok pagi pukul 08.00. Pada pukul 16.30 (jam pulang kantor), rekan-rekan Anda mengajak pergi merayakan ulang tahun salah seorang sahabat kantor. Keputusan Anda adalah...",
            options = listOf(
                QuestionOption("A", "Langsung ikut pergi bersenang-senang dan mengerjakan laporan audit esok pagi terburu-buru", 1),
                QuestionOption("B", "Menyampaikan ucapan selamat dan meminta maaf secara baik-baik belum bisa bergabung, lalu tetap fokus menyelesaikan laporan audit lembur hingga tuntas dan akurat", 5),
                QuestionOption("C", "Menyelesaikan sebagian laporan sampai magrib, lalu menyusul ke tempat perayaan sebentar", 4),
                QuestionOption("D", "Meminta rekan staf lain yang tidak ikut pesta untuk melanjutkan pengerjaan audit Anda", 2),
                QuestionOption("E", "Membawa pulang berkas audit dan mencoba mengerjakannya di rumah larut malam", 3)
            ),
            correctKey = null,
            explanation = "Profesionalisme mengedepankan tanggung jawab penuh terhadap output kerja dinas di atas kesenangan pergaulan pribadi.",
            fastTips = "Poin 5 Profesionalisme: Prioritaskan deadline tugas negara dengan dedikasi tinggi tanpa menyakiti relasi sosial."
        ),
        Question(
            id = "tkp_anti_radikalisme_1",
            packageId = "pool",
            number = 7,
            category = ExamCategory.TKP,
            subCategory = "Anti-Radikalisme",
            questionText = "Seorang rekan kantor yang baru saja mengikuti kajian kelompok tertentu mulai menunjukkan perubahan perilaku drastis: menolak menghormat bendera merah putih saat apel upacara dan terang-terangan menyebut sistem demokrasi pancasila sebagai sistem yang sesat. Langkah yang paling tepat Anda lakukan adalah...",
            options = listOf(
                QuestionOption("A", "Mendiamkan saja karena hal tersebut urusan keyakinan pribadi masing-masing", 2),
                QuestionOption("B", "Mengingatkan yang bersangkutan mengenai statusnya sebagai ASN yang disumpah setia pada Pancasila dan undang-undang", 4),
                QuestionOption("C", "Mengajak berdiskusi secara empat mata mengenai komitmen sumpah janji ASN terhadap Pancasila dan NKRI, serta jika tidak ada perubahan segera melaporkan kepada atasan dan unit pembinaan kepegawaian", 5),
                QuestionOption("D", "Membuat postingan viral di media sosial untuk mempermalukan rekan tersebut", 1),
                QuestionOption("E", "Melaporkan langsung ke aparat kepolisian tanpa berkoordinasi dengan pimpinan kantor", 3)
            ),
            correctKey = null,
            explanation = "Anti-radikalisme menuntut ketegasan menjaga ideologi Pancasila. Opsi C (poin 5) melakukan pendekatan persuasif dialogis terlebih dahulu, lalu menindaklanjuti secara prosedural ke pihak berwenang kepegawaian.",
            fastTips = "Poin 5 Anti-Radikalisme: Tegas, setia pada Pancasila, persuasif empat mata, dan laporkan lewat jalur resmi kepegawaian."
        ),
        Question(
            id = "tkp_anti_radikalisme_2",
            packageId = "pool",
            number = 8,
            category = ExamCategory.TKP,
            subCategory = "Anti-Radikalisme",
            questionText = "Anda mendapati selebaran gelap terselip di majalah dinding kantor yang berisi seruan untuk mendirikan negara berbasis ideologi ekstrem serta ajakan membangkang terhadap hukum konstitusi negara. Sikap Anda...",
            options = listOf(
                QuestionOption("A", "Membiarkannya terpasang karena bukan Anda yang menempelkannya", 1),
                QuestionOption("B", "Segera mencopot selebaran tersebut, mengamankan barang bukti, dan melaporkannya kepada bagian keamanan kantor serta pimpinan", 5),
                QuestionOption("C", "Melaporkan kepada petugas keamanan kantor untuk segera memeriksa dan melepas selebaran tersebut", 4),
                QuestionOption("D", "Membaca dan menyimpannya di meja kerja Anda sendiri tanpa memberitahu siapapun", 2),
                QuestionOption("E", "Menutup selebaran tersebut dengan pengumuman dinas lain agar tidak terbaca", 3)
            ),
            correctKey = null,
            explanation = "Tanggap terhadap penyebaran konten intoleran/radikal dengan segera mengamankan bukti dan melapor secara teratur demi ketertiban institusi.",
            fastTips = "Poin 5: Cepat tanggap, cegah penyebaran propaganda, lapor ke pihak berwenang."
        )
    )
}
