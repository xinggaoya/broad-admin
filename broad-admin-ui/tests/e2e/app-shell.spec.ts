import { expect, test, type Page } from '@playwright/test'

async function bootstrapAppSession(page: Page) {
  await page.addInitScript(() => {
    localStorage.setItem(
      'user-info',
      JSON.stringify({
        userId: 1,
        userName: 'playwright',
        roleId: 1,
        avatar: '',
        token: 'playwright-token',
        nickName: 'Playwright'
      })
    )
  })

  await page.route('**/sysMenu/getRouters', (route) => {
    route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({ code: 200, data: [] })
    })
  })

  await page.route('**/sysAdmin/checkLogin', (route) => {
    route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({ code: 200 })
    })
  })
}

test.describe('App Shell', () => {
  test('移动端抽屉遮罩与可视化快照', async ({ page }) => {
    await bootstrapAppSession(page)
    await page.setViewportSize({ width: 414, height: 896 })
    await page.goto('/', { waitUntil: 'networkidle' })

    const shellRoot = page.getByTestId('app-shell-root')
    await expect(shellRoot).toHaveAttribute('data-device', 'mobile')

    const sidebar = page.getByTestId('app-shell-sidebar')
    await expect(sidebar).toBeHidden()

    await page.getByTestId('shell-toggle-sidebar').click()

    const overlay = page.getByTestId('app-shell-overlay')
    await expect(overlay).toBeVisible()
    await expect(sidebar).toBeVisible()

    await expect(page.getByTestId('app-shell')).toHaveScreenshot('mobile-shell-open.png', {
      maxDiffPixelRatio: 0.03
    })
  })

  test('侧边栏皮肤切换与截图回归', async ({ page }) => {
    await bootstrapAppSession(page)
    await page.setViewportSize({ width: 1440, height: 900 })
    await page.goto('/', { waitUntil: 'networkidle' })

    const shellRoot = page.getByTestId('app-shell-root')
    await expect(shellRoot).toHaveAttribute('data-device', 'pc')

    await page.getByTestId('action-open-settings').click()
    const darkCard = page.getByTestId('side-theme-card-dark')
    await expect(darkCard).toBeVisible()
    await darkCard.click()

    await expect(shellRoot).toHaveAttribute('data-side-theme', 'dark')
    await expect(page.getByTestId('app-shell-sidebar')).toHaveScreenshot('sidebar-dark.png', {
      maxDiffPixelRatio: 0.05
    })

    await page.getByTestId('side-theme-card-white').click()
    await expect(shellRoot).toHaveAttribute('data-side-theme', 'white')
  })
})
