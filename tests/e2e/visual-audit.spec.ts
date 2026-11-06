import { test, expect } from "@playwright/test";

test.use({ viewport: { width: 1366, height: 768 } });

test("login and exam preview fit the audit viewport", async ({ page }) => {
  await page.goto("/login");
  await expect(page.getByRole("heading", { name: "Masuk ke akunmu." })).toBeVisible();
  await expect(page.getByRole("button", { name: /Masuk/ })).toBeVisible();
  await page.goto("/exam");
  await expect(page.getByRole("heading", { name: "Navigasi soal" })).toBeVisible();
  await expect(page.getByText("01:39:42")).toBeVisible();
  await expect(page.getByRole("button", { name: "Simpan & lanjutkan" })).toBeVisible();
  await expect(page.locator("body")).toHaveCSS("overflow-x", "visible");
});
