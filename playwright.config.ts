import { defineConfig } from "@playwright/test";
const compose = process.env.E2E_COMPOSE === "1";
export default defineConfig({
  testDir: "./tests/e2e",
  workers: 1,
  use: { baseURL: compose ? "http://127.0.0.1:3000" : "http://127.0.0.1:3100", browserName: "chromium", launchOptions: process.env.PLAYWRIGHT_BROWSER_PATH ? { executablePath: process.env.PLAYWRIGHT_BROWSER_PATH } : {} },
  webServer: compose ? undefined : { command: "npm run dev -- --hostname 127.0.0.1 --port 3100", url: "http://127.0.0.1:3100", timeout: 120000, reuseExistingServer: false },
});
