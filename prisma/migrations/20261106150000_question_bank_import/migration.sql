CREATE TABLE "question_import_batches" (
    "id" TEXT NOT NULL,
    "code" TEXT NOT NULL,
    "filename" TEXT NOT NULL,
    "sourceLabel" TEXT NOT NULL,
    "rowCount" INTEGER NOT NULL,
    "importedAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "question_import_batches_pkey" PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX "question_import_batches_code_key" ON "question_import_batches"("code");

ALTER TABLE "questions" ADD COLUMN "code" TEXT;
ALTER TABLE "questions" ADD COLUMN "topic" TEXT;
ALTER TABLE "questions" ADD COLUMN "difficulty" TEXT;
ALTER TABLE "questions" ADD COLUMN "sourceReference" TEXT;
ALTER TABLE "questions" ADD COLUMN "reviewNote" TEXT;

UPDATE "questions" SET "code" = 'LEGACY-' || "id", "topic" = 'Uncategorized', "difficulty" = 'UNKNOWN' WHERE "code" IS NULL;
ALTER TABLE "questions" ALTER COLUMN "code" SET NOT NULL;
ALTER TABLE "questions" ALTER COLUMN "topic" SET NOT NULL;
ALTER TABLE "questions" ALTER COLUMN "difficulty" SET NOT NULL;
CREATE UNIQUE INDEX "questions_code_key" ON "questions"("code");

ALTER TABLE "question_versions" ADD COLUMN "importBatchId" TEXT;
ALTER TABLE "question_versions" ADD CONSTRAINT "question_versions_importBatchId_fkey" FOREIGN KEY ("importBatchId") REFERENCES "question_import_batches"("id") ON DELETE SET NULL ON UPDATE CASCADE;
CREATE INDEX "question_versions_importBatchId_idx" ON "question_versions"("importBatchId");
