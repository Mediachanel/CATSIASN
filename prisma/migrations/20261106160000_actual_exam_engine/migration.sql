CREATE TYPE "EventStatus" AS ENUM ('DRAFT', 'PUBLISHED', 'ARCHIVED');
CREATE TYPE "ExamSessionStatus" AS ENUM ('IN_PROGRESS', 'SUBMITTED', 'EXPIRED');

CREATE TABLE "tryout_events" (
    "id" TEXT NOT NULL,
    "code" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "description" TEXT,
    "status" "EventStatus" NOT NULL DEFAULT 'DRAFT',
    "isInternalDemo" BOOLEAN NOT NULL DEFAULT false,
    "durationMinutes" INTEGER NOT NULL DEFAULT 100,
    "twkCount" INTEGER NOT NULL DEFAULT 30,
    "tiuCount" INTEGER NOT NULL DEFAULT 35,
    "tkpCount" INTEGER NOT NULL DEFAULT 45,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "tryout_events_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "exam_sessions" (
    "id" TEXT NOT NULL,
    "userId" TEXT NOT NULL,
    "eventId" TEXT NOT NULL,
    "status" "ExamSessionStatus" NOT NULL DEFAULT 'IN_PROGRESS',
    "startedAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "deadlineAt" TIMESTAMP(3) NOT NULL,
    "submittedAt" TIMESTAMP(3),
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "exam_sessions_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "session_questions" (
    "id" TEXT NOT NULL,
    "sessionId" TEXT NOT NULL,
    "questionVersionId" TEXT NOT NULL,
    "position" INTEGER NOT NULL,
    "optionOrder" JSONB NOT NULL,
    "correctOption" TEXT,
    "optionWeights" JSONB,
    CONSTRAINT "session_questions_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "exam_answers" (
    "id" TEXT NOT NULL,
    "sessionQuestionId" TEXT NOT NULL,
    "selectedOption" TEXT NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,
    CONSTRAINT "exam_answers_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "exam_results" (
    "id" TEXT NOT NULL,
    "sessionId" TEXT NOT NULL,
    "twkScore" INTEGER NOT NULL,
    "tiuScore" INTEGER NOT NULL,
    "tkpScore" INTEGER NOT NULL,
    "totalScore" INTEGER NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "exam_results_pkey" PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX "tryout_events_code_key" ON "tryout_events"("code");
CREATE INDEX "exam_sessions_userId_status_idx" ON "exam_sessions"("userId", "status");
CREATE INDEX "exam_sessions_eventId_status_idx" ON "exam_sessions"("eventId", "status");
CREATE INDEX "session_questions_sessionId_idx" ON "session_questions"("sessionId");
CREATE UNIQUE INDEX "session_questions_sessionId_position_key" ON "session_questions"("sessionId", "position");
CREATE UNIQUE INDEX "exam_answers_sessionQuestionId_key" ON "exam_answers"("sessionQuestionId");
CREATE UNIQUE INDEX "exam_results_sessionId_key" ON "exam_results"("sessionId");

ALTER TABLE "exam_sessions" ADD CONSTRAINT "exam_sessions_userId_fkey" FOREIGN KEY ("userId") REFERENCES "users"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "exam_sessions" ADD CONSTRAINT "exam_sessions_eventId_fkey" FOREIGN KEY ("eventId") REFERENCES "tryout_events"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "session_questions" ADD CONSTRAINT "session_questions_sessionId_fkey" FOREIGN KEY ("sessionId") REFERENCES "exam_sessions"("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "session_questions" ADD CONSTRAINT "session_questions_questionVersionId_fkey" FOREIGN KEY ("questionVersionId") REFERENCES "question_versions"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "exam_answers" ADD CONSTRAINT "exam_answers_sessionQuestionId_fkey" FOREIGN KEY ("sessionQuestionId") REFERENCES "session_questions"("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "exam_results" ADD CONSTRAINT "exam_results_sessionId_fkey" FOREIGN KEY ("sessionId") REFERENCES "exam_sessions"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
