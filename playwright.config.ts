import { defineConfig } from "@playwright/test";
export default defineConfig({
  testDir: "./tests/e2e",
  workers: 1,
  use: { baseURL: "http://127.0.0.1:3100", browserName: "chromium", launchOptions: process.env.PLAYWRIGHT_BROWSER_PATH ? { executablePath: process.env.PLAYWRIGHT_BROWSER_PATH } : {} },
  webServer: { command: "npm run dev -- --hostname 127.0.0.1 --port 3100", url: "http://127.0.0.1:3100", timeout: 120000, reuseExistingServer: false },
});
