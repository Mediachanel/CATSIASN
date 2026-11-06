import { chromium } from "@playwright/test";
import { mkdir } from "node:fs/promises";

await mkdir("artifacts/visual-audit", { recursive: true });
const browser = await chromium.launch({ headless: true, executablePath: "C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe" });
const page = await browser.newPage({ viewport: { width: 1366, height: 768 }, deviceScaleFactor: 1 });
const baseUrl = process.env.VISUAL_AUDIT_BASE_URL || "http://127.0.0.1:3000";
for (const route of ["login", "exam"]) {
  const response = await page.goto(`${baseUrl}/${route}`, { waitUntil: "networkidle" });
  console.log(JSON.stringify({ route, status: response?.status(), url: page.url(), title: await page.title() }));
  await page.screenshot({ path: `artifacts/visual-audit/${route}-1366.png`, fullPage: true });
}
await browser.close();
