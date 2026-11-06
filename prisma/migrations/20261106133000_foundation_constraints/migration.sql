ALTER TABLE "membership_plans" ADD CONSTRAINT "membership_plans_valid_values" CHECK ("priceIdr" >= 0 AND "durationDays" > 0 AND "quota" >= 0);
ALTER TABLE "subscriptions" ADD CONSTRAINT "subscriptions_valid_values" CHECK ("quota" >= 0 AND "endsAt" > "startsAt");
ALTER TABLE "question_versions" ADD CONSTRAINT "question_versions_positive_version" CHECK ("version" > 0);
ALTER TABLE "question_options" ADD CONSTRAINT "question_options_valid_weight" CHECK ("weight" IS NULL OR "weight" BETWEEN 1 AND 5);
