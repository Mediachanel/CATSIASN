ALTER TABLE "question_versions" ADD COLUMN "imagePath" TEXT, ADD COLUMN "imageAlt" TEXT;
ALTER TABLE "question_versions" ADD CONSTRAINT "question_versions_image_pair" CHECK (("imagePath" IS NULL AND "imageAlt" IS NULL) OR ("imagePath" IS NOT NULL AND "imageAlt" IS NOT NULL AND length(trim("imageAlt")) > 0));
