package factory;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

import utils.Library;

public class PlaywrightFactory {

    private static final InheritableThreadLocal<Playwright> playwrightPool =
            new InheritableThreadLocal<>();
    private static final InheritableThreadLocal<Browser> browserPool =
            new InheritableThreadLocal<>();
    private static final InheritableThreadLocal<BrowserContext> contextPool =
            new InheritableThreadLocal<>();
    private static final InheritableThreadLocal<Page> pagePool =
            new InheritableThreadLocal<>();

    private PlaywrightFactory() {
    }

    public static Page getPage() {
        if (pagePool.get() == null) {
            createPage();
        }

        return pagePool.get();
    }

    public static boolean hasPage() {
        return pagePool.get() != null;
    }

    private static void createPage() {
        Playwright playwright = Playwright.create();
        playwrightPool.set(playwright);

        Browser browser = createBrowser(playwright);
        browserPool.set(browser);

        BrowserContext context = createContext(browser);
        contextPool.set(context);

        if (getBooleanProperty("playwrightTrace", false)) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true));
        }

        pagePool.set(context.newPage());
    }

    private static Browser createBrowser(Playwright playwright) {
        String browserName = getProperty("playwrightBrowserType", "chrome")
                .toLowerCase()
                .trim();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(getBooleanProperty("playwrightHeadless", false))
                .setSlowMo(getDoubleProperty("playwrightSlowMo", 0));

        switch (browserName) {
            case "chrome":
                return playwright.chromium().launch(launchOptions.setChannel("chrome"));
            case "edge":
                return playwright.chromium().launch(launchOptions.setChannel("msedge"));
            case "chromium":
                return playwright.chromium().launch(launchOptions);
            case "firefox":
                return playwright.firefox().launch(launchOptions);
            case "webkit":
                return playwright.webkit().launch(launchOptions);
            default:
                throw new RuntimeException("Unsupported Playwright browser type: " + browserName);
        }
    }

    private static BrowserContext createContext(Browser browser) {
        int viewportWidth = getIntegerProperty("playwrightViewportWidth", 1920);
        int viewportHeight = getIntegerProperty("playwrightViewportHeight", 1080);
        int timeout = getIntegerProperty("playwrightTimeout", 30000);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setAcceptDownloads(getBooleanProperty("playwrightAcceptDownloads", true))
                .setIgnoreHTTPSErrors(true)
                .setViewportSize(viewportWidth, viewportHeight);

        BrowserContext context = browser.newContext(contextOptions);
        context.setDefaultTimeout(timeout);
        context.setDefaultNavigationTimeout(timeout);

        return context;
    }

    public static void closePlaywright() {
        try {
            if (contextPool.get() != null && getBooleanProperty("playwrightTrace", false)) {
                contextPool.get().tracing().stop(new Tracing.StopOptions()
                        .setPath(Paths.get("target/playwright-traces/trace.zip")));
            }
        } catch (Exception e) {
            System.err.println("Unable to stop Playwright trace: " + e.getMessage());
        }

        try {
            if (contextPool.get() != null) {
                contextPool.get().close();
            }
        } catch (Exception e) {
            System.err.println("Unable to close Playwright context: " + e.getMessage());
        }

        try {
            if (browserPool.get() != null) {
                browserPool.get().close();
            }
        } catch (Exception e) {
            System.err.println("Unable to close Playwright browser: " + e.getMessage());
        }

        try {
            if (playwrightPool.get() != null) {
                playwrightPool.get().close();
            }
        } catch (Exception e) {
            System.err.println("Unable to close Playwright: " + e.getMessage());
        } finally {
            pagePool.remove();
            contextPool.remove();
            browserPool.remove();
            playwrightPool.remove();
        }
    }

    private static String getProperty(String key, String defaultValue) {
        String value = Library.getLibrary().getProperty(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = Library.getLibrary().getProperty(key);
        return value == null || value.isBlank()
                ? defaultValue
                : Boolean.parseBoolean(value);
    }

    private static int getIntegerProperty(String key, int defaultValue) {
        String value = Library.getLibrary().getProperty(key);
        return value == null || value.isBlank()
                ? defaultValue
                : Integer.parseInt(value.trim());
    }

    private static double getDoubleProperty(String key, double defaultValue) {
        String value = Library.getLibrary().getProperty(key);
        return value == null || value.isBlank()
                ? defaultValue
                : Double.parseDouble(value.trim());
    }
}
