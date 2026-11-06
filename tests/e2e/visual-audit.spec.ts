import { test, expect } from "@playwright/test";

test.use({ viewport: { width: 1366, height: 768 } });

test("landing and login fit the audit viewport", async ({ page }) => {
  await page.goto("/");
  await expect(page.getByRole("heading", { name: "Latihan CAT yang terasa seperti ujian sesungguhnya." })).toBeVisible();
  await expect(page.getByRole("link", { name: "Mulai latihan gratis" })).toBeVisible();
  await page.goto("/login");
  await expect(page.getByRole("heading", { name: "Masuk ke akunmu." })).toBeVisible();
  await expect(page.getByRole("button", { name: /Masuk/ })).toBeVisible();
  await expect(page.locator("body")).toHaveCSS("overflow-x", "visible");
});
