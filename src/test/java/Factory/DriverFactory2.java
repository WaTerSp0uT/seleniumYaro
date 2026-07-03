package Factory;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.time.Duration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.File;

// import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.safari.SafariDriver;

import utils.CommonUtils;
import utils.Library;

public class DriverFactory2 {

    /*
     * Thread-Safe Driver: Creating a private constructor, so we are closing access
     * to the object of this class from outside the class
     */
    private DriverFactory2() {
    }

    /*
     * We make WebDriver private, because we want to close access from outside the
     * class. We make it static because we will use it in a static method.
     */

    // private static WebDriver driver; // value is null by default

    // Thread-safe WebDriver instance
    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    // Returns the WebDriver instance for the current thread
    public static WebDriver getDriver() {
        if (driverPool.get() == null) {
            driverPool.set(createDriver());
        }

        return driverPool.get();
    }
    /*
     * Create a re-usable utility method which will return same driver instance when
     * we call it based on our configuration.properties file
     */
    private static WebDriver createDriver() {

        WebDriver driver = null;

        // Load properties from configuration file
        String browser = Library.getLibrary().getProperty("browserType").toLowerCase();
        String execution = Library.getLibrary().getProperty("executionType").toLowerCase();
        boolean isHeadless = Boolean.parseBoolean(
                Library.getLibrary().getProperty("headless").toLowerCase());

        try {

            // Global JVM proxy system properties (applies to all browsers)

            // or if above does not work use this AS WELL!
            // Proxy proxy = new Proxy();

            if (execution.equals("remote")) {


                // Isolate the Grid connection so it bypasses Zscaler completely

                // Grid URL from config
                String targetMachine = Library.getLibrary().getProperty("targetMachine");
                String remoteURL = Library.getLibrary().getProperty(targetMachine + ".url");
                URL gridUrl = new URL(remoteURL);

                ClientConfig clientConfig = ClientConfig.defaultConfig()
                        .baseUrl(gridUrl)
                        .proxy(java.net.Proxy.NO_PROXY); // Forces direct communication with Grid

                switch (browser) {

                    case "firefox":

                        FirefoxOptions firefoxOptions = new FirefoxOptions();

                        // firefoxOptions.setProxy(proxy);
                        // firefoxOptions.addArguments("--no-sandbox");
                        // firefoxOptions.addArguments("--disable-dev-shm-usage");
                        // firefoxOptions.addArguments("--ignore-ssl-errors");
                        // firefoxOptions.addArguments("--ignore-certificate-errors");

                        if (isHeadless) {
                            firefoxOptions.addArguments("--headless");
                        }

                        firefoxOptions.setCapability("acceptInsecureCerts", true);
                        firefoxOptions.setCapability("browserName", "firefox");
                        firefoxOptions.setCapability("platformName", "ANY"); // or specific OS if needed

                        driver = new RemoteWebDriver(gridUrl, firefoxOptions);
                        driver.manage().window().setSize(new Dimension(1920, 1080));

                        break;

                    case "edge":

                        EdgeOptions edgeOptions = new EdgeOptions();

                        // edgeOptions.setProxy(proxy);
                        //edgeOptions.addArguments("--no-sandbox");
                        //edgeOptions.addArguments("--remote-allow-origins=*");
                        //edgeOptions.addArguments("--disable-dev-shm-usage");
                        //edgeOptions.addArguments("--ignore-ssl-errors");
                        edgeOptions.addArguments("--disable-dev-shm-usage");
                        edgeOptions.addArguments("--ignore-ssl-errors");
                        edgeOptions.addArguments("--ignore-certificate-errors");

                        if (isHeadless) {
                            edgeOptions.addArguments("--headless=new");
                            edgeOptions.addArguments("--window-size=1920,1080");
                        }

                        edgeOptions.setCapability("acceptInsecureCerts", true);
                        edgeOptions.setCapability("browserName", "MicrosoftEdge");
                        edgeOptions.setCapability("platformName", "ANY"); // or specific OS if needed

                        driver = new RemoteWebDriver(gridUrl, edgeOptions);
                        driver.manage().window().setSize(new Dimension(1920, 1080));

                        break;

                    case "chrome":

                        ChromeOptions chromeOptions = new ChromeOptions();

                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--remote-allow-origins=*");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--ignore-ssl-errors");
                        chromeOptions.addArguments("--ignore-certificate-errors");

                        if (isHeadless) {
                            chromeOptions.addArguments("--headless=new");
                            chromeOptions.addArguments("--window-size=1920,1080");
                        }

                        chromeOptions.setCapability("acceptInsecureCerts", true);
                        chromeOptions.setCapability("browserName", "chrome");
                        chromeOptions.setCapability("platformName", "ANY"); // or specific OS if needed

                        driver = new RemoteWebDriver(gridUrl, chromeOptions);

                        break;

                    default:
                        throw new RuntimeException(
                                "Unsupported browser for remote execution: " + browser);
                }

            } else if (execution.equals("local")) {

                // Local execution setup
                switch (browser) {

                    case "firefox":

                        // WebDriverManager.firefoxdriver().setup();

                        FirefoxProfile fProfile = new FirefoxProfile();
                        fProfile.setPreference("print.always_print_silent", true);
                        fProfile.setPreference("print.show_print_progress", false);

                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        firefoxOptions.setProfile(fProfile);

                        // firefoxOptions.setProxy(proxy);
                        // firefoxOptions.addArguments("--no-sandbox");
                        // firefoxOptions.addArguments("--disable-dev-shm-usage");
                        // firefoxOptions.addArguments("--ignore-ssl-errors");
                        // firefoxOptions.addArguments("--ignore-certificate-errors");
                        // firefoxOptions.setCapability("acceptInsecureCerts", true);

                        if (isHeadless) {
                            firefoxOptions.addArguments("--headless");
                        }

                        driver = new FirefoxDriver(firefoxOptions);
                        driver.manage().window().setSize(new Dimension(1920, 1080));

                        break;
                    case "edge":
                        WebDriverManager.edgedriver().setup();

                        EdgeOptions edgeOptions = new EdgeOptions();

                        // edgeOptions.setProxy(proxy);
                        edgeOptions.addArguments("--no-sandbox");
                        edgeOptions.addArguments("--remote-allow-origins=*");
                        edgeOptions.addArguments("--disable-dev-shm-usage");
                        edgeOptions.addArguments("--ignore-ssl-errors");
                        edgeOptions.addArguments("--ignore-certificate-errors");
                        edgeOptions.setCapability("acceptInsecureCerts", true);

                        if (isHeadless) {
                            edgeOptions.addArguments("--headless=new");
                            edgeOptions.addArguments("--window-size=1920,1080");
                        }

                        driver = new EdgeDriver(edgeOptions);
                        driver.manage().window().setSize(new Dimension(1920, 1080));
                        break;

                    case "chrome":
                        WebDriverManager.chromedriver().setup();

                        ChromeOptions chromeOptions = new ChromeOptions();

                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--remote-allow-origins=*");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--ignore-ssl-errors");
                        chromeOptions.addArguments("--ignore-certificate-errors");
                        chromeOptions.setCapability("acceptInsecureCerts", true);

                        if (isHeadless) {
                            chromeOptions.addArguments("--headless=new");
                            chromeOptions.addArguments("--window-size=1920,1080");
                        }

                        driver = new ChromeDriver();
                        break;

                    default:
                        throw new RuntimeException("Unsupported browser for local execution: " + browser);
                }

            } else {
                throw new RuntimeException("Invalid execution type specified: " + execution);
            }

            // Common post-init setup
            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("WebDriver creation failed: " + e.getMessage());
        }

        return driver;
    }

    // Clean up WebDriver after test execution
    public static void closeDriver() {
        try {
            if (driverPool.get() != null) {
                driverPool.get().quit(); // End the browser session
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driverPool.remove(); // Prevent memory leaks
        }
    }

    // Clean up WebDriver after test execution
    public static void closeCurrentTab() {
        if (driverPool.get() != null) {
            driverPool.get().close(); // Close current Tab
        }
    }

}