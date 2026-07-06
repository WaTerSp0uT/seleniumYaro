# Playwright Automation Framework

This branch contains a Playwright Java automation framework.
It uses Java, Maven, Cucumber, JUnit 4, Extent reporting, Page Object Model, hooks, reusable utilities, and secret-based credential handling.

The framework is built manually. It does not use Playwright AI features, MCP tooling, code generation, or Playwright CLI scaffolding.

## Framework Structure

```text
src/test/java/
  factory/
    PlaywrightFactory.java
  hooks/
    PlaywrightHooks.java
  pages/
    OHRM/
      OrangeHRMLoginPage.java
  runner/
    PlaywrightTestRunner.java
  stepDefinitions/
    CommonPlaywrightSteps.java
    OrangeHRMPlaywrightSteps.java
  utils/
    Library.java
    SecretConfigReader.java
    SoftAssert.java

src/test/resources/features/
  orangeHRM.feature
```

## Main Components

`PlaywrightFactory` creates and manages Playwright, Browser, BrowserContext, and Page objects using thread-local storage.

`PlaywrightHooks` handles scenario lifecycle, screenshot attachment, thread logging, and browser cleanup.

`OrangeHRMLoginPage` is the Playwright Page Object for the OrangeHRM login page and dashboard validation.

`OrangeHRMPlaywrightSteps` maps Cucumber steps to the OrangeHRM Playwright page object.

`CommonPlaywrightSteps` contains shared Playwright steps such as navigation.

`PlaywrightTestRunner` runs only the Playwright feature files and uses only Playwright glue packages.

## Configuration

The Playwright framework reads from:

```text
Configs/config.properties
```

Playwright-specific keys:

```properties
playwrightBrowserType=chrome
playwrightHeadless=false
playwrightSlowMo=0
playwrightTimeout=30000
playwrightViewportWidth=1920
playwrightViewportHeight=1080
playwrightAcceptDownloads=true
playwrightTrace=false
playwrightScreenshotOnStep=failed
```

Supported `playwrightBrowserType` values:

```text
chrome
edge
chromium
firefox
webkit
```

For government or locked-down machines, prefer `chrome` or `edge` because those use installed enterprise browsers through Playwright browser channels. Avoid relying on generated setup or CLI-installed browser binaries unless your organization explicitly approves that process.

You can override config values from Maven:

```bash
mvn test -DplaywrightBrowserType=edge -DplaywrightHeadless=true
```

## Credentials

Credentials are resolved through `SecretConfigReader`.

It checks:

```text
1. local .env file
2. JVM system properties, such as -DKEY=value
3. operating system or CI pipeline environment variables
```

Required OrangeHRM keys:

```env
OHRM_VALID_USERNAME=Admin
OHRM_VALID_PASSWORD=admin123
OHRM_INVALID_USERNAME=username
OHRM_INVALID_PASSWORD=password
```

For local execution, create a `.env` file in the project root.

For pipeline execution, define those same keys as pipeline secrets or environment variables.

## Feature File

The migrated Playwright OrangeHRM feature is here:

```text
src/test/resources/features/orangeHRM.feature
```

Current scenarios:

```text
@OHRM_PLAYWRIGHT @Negative
Login with invalid credentials

@OHRM_PLAYWRIGHT @Positive
Login with valid credentials
```

## How To Execute

Run all Playwright scenarios:

```bash
mvn test
```

Run only the positive scenario:

```bash
mvn test -Dcucumber.filter.tags="@OHRM_PLAYWRIGHT and @Positive"
```

Run only the negative scenario:

```bash
mvn test -Dcucumber.filter.tags="@OHRM_PLAYWRIGHT and @Negative"
```

Run headless:

```bash
mvn test -DplaywrightHeadless=true
```

Run with Microsoft Edge:

```bash
mvn test -DplaywrightBrowserType=edge
```

## Rerun Failed Tests

The main runner writes failed scenario locations to:

```text
target/playwright-failed.txt
```

This is configured in `PlaywrightTestRunner`:

```java
"rerun:target/playwright-failed.txt"
```

To rerun only failed scenarios, use:

```bash
mvn test -Dtest=PlaywrightFailedTestRunner
```

The failed-test runner reads from:

```text
target/playwright-failed.txt
```

Rerun report outputs are written to:

```text
target/playwright-builtInReport_rerun
target/PlaywrightCucumber_rerun.json
```

## Reports

The framework creates multiple report outputs after execution.

### Cucumber Reports

The Playwright runner writes Cucumber report outputs to:

```text
target/playwright-builtInReport
target/PlaywrightCucumber.json
target/playwright-failed.txt
```

Open the Cucumber built-in HTML report:

```bash
open target/playwright-builtInReport/index.html
```

If the browser does not open the file directly, open the folder instead:

```bash
open target/playwright-builtInReport
```

### Extent Reports

Extent reporting is configured by:

```text
src/test/resources/extent.properties
src/test/resources/extent-config.xml
```

The main visual Extent HTML report is generated at:

```text
test-output/SparkReport/Index.html
```

Open it on macOS:

```bash
open test-output/SparkReport/Index.html
```

The Extent PDF report is generated at:

```text
test-output/PdfReport/ExtentPdf.pdf
```

Open it on macOS:

```bash
open test-output/PdfReport/ExtentPdf.pdf
```

In IntelliJ, you can also right-click the HTML report file and select `Open in Browser`.

## Screenshot Behavior

Screenshot behavior is controlled by:

```properties
playwrightScreenshotOnStep=failed
```

Supported values:

```text
failed
always
none
```

Screenshots are attached to the Cucumber scenario and are available to compatible report outputs.
