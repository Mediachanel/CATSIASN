import { readFile } from "node:fs/promises";
import { basename } from "node:path";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();
const files = process.argv.slice(2);
const expected = [
  ["bank_soal_3000_DRAFT.json", "BATCH01"],
  ["bank_soal_3000_BATCH02_DRAFT.json", "BATCH02"],
  ["bank_soal_3000_BATCH03_DRAFT.json", "BATCH03"],
];
const selected = files.length ? files.map(file => [file, basename(file).match(/BATCH0[1-3]/)?.[0] ?? "CUSTOM"]) : expected;
if (selected.length !== 3) throw new Error("Provide exactly the three bank files or no arguments.");
const labels = new Set();
const allowed = new Set(["TWK", "TIU", "TKP"]);
const optionLabels = ["A", "B", "C", "D", "E"];
function parseRow(row, file) {
  if (!row || !allowed.has(row.category) || typeof row.code !== "string" || !row.question_text || !row.explanation) throw new Error(`Invalid row in ${file}: ${row?.code ?? "unknown"}`);
  const weights = optionLabels.map(label => Number(row[`score_${label.toLowerCase()}`]));
  if (weights.some(value => !Number.isInteger(value) || value < 0 || value > 5)) throw new Error(`Invalid score in ${file}: ${row.code}`);
  if (row.category !== "TKP" && !optionLabels.includes(String(row.correct_answer).toUpperCase())) throw new Error(`Invalid answer key in ${file}: ${row.code}`);
  if (row.category === "TKP" && (new Set(weights).size !== 5 || weights.some(value => value < 1))) throw new Error(`Invalid TKP weights in ${file}: ${row.code}`);
  return { ...row, weights };
}
async function main() {
  const allCodes = new Set();
  for (const [file, batchCode] of selected) {
    const rows = JSON.parse(await readFile(file, "utf8"));
    if (!Array.isArray(rows) || rows.length !== 3000) throw new Error(`${file} must contain exactly 3000 rows.`);
    if (labels.has(batchCode)) throw new Error(`Duplicate batch ${batchCode}`);
    labels.add(batchCode);
    const parsed = rows.map(row => parseRow(row, file));
    for (const row of parsed) { if (allCodes.has(row.code)) throw new Error(`Duplicate question code ${row.code}`); allCodes.add(row.code); }
    const counts = parsed.reduce((result, row) => ({ ...result, [row.category]: (result[row.category] ?? 0) + 1 }), {});
    if (counts.TWK !== 750 || counts.TIU !== 1050 || counts.TKP !== 1200) throw new Error(`${file} has an unexpected category distribution.`);
    await prisma.$transaction(async tx => {
      const batch = await tx.questionImportBatch.upsert({ where: { code: batchCode }, create: { code: batchCode, filename: basename(file), sourceLabel: "DRAFT INTERNAL - generated/imported; needs expert review", rowCount: rows.length }, update: { filename: basename(file), rowCount: rows.length } });
      for (let offset = 0; offset < parsed.length; offset += 100) {
        for (const row of parsed.slice(offset, offset + 100)) {
          const question = await tx.question.upsert({ where: { code: row.code }, create: { code: row.code, category: row.category, topic: row.topic, difficulty: row.difficulty, sourceReference: row.source_reference, reviewNote: row.review_note }, update: { topic: row.topic, difficulty: row.difficulty, sourceReference: row.source_reference, reviewNote: row.review_note } });
          const version = await tx.questionVersion.upsert({ where: { questionId_version: { questionId: question.id, version: 1 } }, create: { questionId: question.id, version: 1, status: "DRAFT", prompt: row.question_text, explanation: row.explanation, isDemo: true, importBatchId: batch.id }, update: { prompt: row.question_text, explanation: row.explanation, isDemo: true, importBatchId: batch.id, status: "DRAFT" } });
          await tx.questionOption.deleteMany({ where: { questionVersionId: version.id } });
          await tx.questionOption.createMany({ data: optionLabels.map((label, index) => ({ questionVersionId: version.id, label, text: row[`option_${label.toLowerCase()}`], isCorrect: row.category === "TKP" ? null : String(row.correct_answer).toUpperCase() === label, weight: row.category === "TKP" ? row.weights[index] : null })) });
        }
      }
    }, { timeout: 120000 });
    console.log(JSON.stringify({ batch: batchCode, file: basename(file), imported: rows.length, counts }));
  }
  console.log(JSON.stringify({ totalImported: allCodes.size, status: "DRAFT", warning: "Rows are internal draft data and are not verified or published." }));
}
main().catch(error => { console.error(error); process.exitCode = 1; }).finally(() => prisma.$disconnect());
