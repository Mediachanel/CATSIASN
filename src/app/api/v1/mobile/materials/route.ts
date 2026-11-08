export async function GET() {
  const materials = [
    {
      id: "mat_twk_pancasila",
      category: "TWK",
      title: "Pancasila & Butir-Butir Pengamalan",
      summary: "Memahami 45 butir pengamalan Pancasila, dimensi ideologi terbuka (realitas, idealisme, fleksibilitas), dan implementasi dalam kehidupan berbangsa.",
      points: [
        "Sila 1: Toleransi beragama, kebebasan beribadah, tidak memaksakan agama.",
        "Sila 2: Hak asasi manusia, kesetaraan derajat, empati sosial dan kemanusiaan.",
        "Sila 3: Cinta tanah air, persatuan bangsa, penempatan kepentingan negara di atas golongan.",
        "Sila 4: Musyawarah mufakat, demokrasi Pancasila, tidak memaksakan kehendak.",
        "Sila 5: Keadilan distribusi publik, anti korupsi/nepotisme, menghormati hak orang lain."
      ]
    },
    {
      id: "mat_twk_uud45",
      category: "TWK",
      title: "UUD 1945 & Sistem Ketatanegaraan",
      summary: "Struktur ketatanegaraan pasca amandemen (1999-2002), kewenangan lembaga tinggi negara (MPR, DPR, DPD, Presiden, MA, MK, BPK).",
      points: [
        "MPR berwenang mengubah dan menetapkan UUD, melantik Presiden/Wapres.",
        "DPR memegang kekuasaan membentuk Undang-Undang (fungsi legislasi, anggaran, pengawasan).",
        "MK berwenang menguji UU terhadap UUD, memutus sengketa kewenangan lembaga negara, memutus pembubaran parpol, memutus PHPU."
      ]
    },
    {
      id: "mat_tiu_silogisme",
      category: "TIU",
      title: "Penalaran Logis (Silogisme) Cepat",
      summary: "Aturan inferensi premis mayor & minor, hukum silogisme kategorik, dan cara cepat eliminasi opsi jebakan.",
      points: [
        "Semua A adalah B + Semua B adalah C => Semua A adalah C.",
        "Beberapa A adalah B + Semua B adalah C => Beberapa A adalah C.",
        "Jika ada premis partikular (sebagian/beberapa), kesimpulan WAJIB partikular.",
        "Jika ada premis negatif (tidak/bukan), kesimpulan WAJIB negatif."
      ]
    },
    {
      id: "mat_tiu_berhitung",
      category: "TIU",
      title: "Berhitung Cepat & Deret Angka",
      summary: "Strategi hitung aproksimasi, pola deret angka lompat satu/dua/tiga, dan operasi pecahan/desimal.",
      points: [
        "Gunakan pembulatan angka (aproksimasi) jika pilihan jawaban berjarak cukup jauh.",
        "Identifikasi pola Fibonacci, deret kuadrat bertingkat, dan deret selang-seling.",
        "Hafalkan konversi pecahan umum: 1/8 = 12.5%, 1/6 = 16.67%, 3/8 = 37.5%, 5/8 = 62.5%."
      ]
    },
    {
      id: "mat_tkp_pelayanan",
      category: "TKP",
      title: "Pelayanan Publik & Profesionalisme",
      summary: "Tips mendapatkan poin maksimal 5 pada soal integritas, orientasi pelayanan publik, dan kerja tim.",
      points: [
        "Pilih tindakan solutif langsung yang tuntas dan tidak melempar tanggung jawab.",
        "Utamakan kepuasan masyarakat/klien tanpa melanggar SOP dan regulasi yang berlaku.",
        "Tunjukkan sikap adaptif terhadap teknologi baru dan keterbukaan terhadap kritik yang membangun."
      ]
    }
  ];

  return Response.json({
    success: true,
    data: materials
  });
}
