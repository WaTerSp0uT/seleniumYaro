from pathlib import Path

from docx import Document
from docx.enum.section import WD_SECTION
from docx.enum.table import WD_CELL_VERTICAL_ALIGNMENT, WD_TABLE_ALIGNMENT
from docx.enum.text import WD_ALIGN_PARAGRAPH, WD_BREAK, WD_LINE_SPACING
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.shared import Inches, Pt, RGBColor


ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "docs" / "Playwright_Java_Cucumber_Framework_Onboarding_Guide.docx"
ARCH = ROOT / "docs" / "images" / "playwright-framework-architecture-v3-generic.png"

NAVY = "0B2545"
BLUE = "1F4D78"
TEAL = "087F8C"
ORANGE = "D95F02"
LIGHT_BLUE = "E8EEF5"
LIGHT_TEAL = "E7F3F3"
LIGHT_ORANGE = "FFF1E8"
LIGHT_GRAY = "F2F4F7"
MID_GRAY = "697386"
WHITE = "FFFFFF"
BLACK = "202124"


def set_font(run, name="Calibri", size=11, bold=None, italic=None, color=BLACK):
    run.font.name = name
    run._element.get_or_add_rPr().rFonts.set(qn("w:ascii"), name)
    run._element.get_or_add_rPr().rFonts.set(qn("w:hAnsi"), name)
    run.font.size = Pt(size)
    if bold is not None:
        run.bold = bold
    if italic is not None:
        run.italic = italic
    if color:
        run.font.color.rgb = RGBColor.from_string(color)


def shade_paragraph(paragraph, fill, border=None):
    ppr = paragraph._p.get_or_add_pPr()
    shd = ppr.find(qn("w:shd"))
    if shd is None:
        shd = OxmlElement("w:shd")
        ppr.append(shd)
    shd.set(qn("w:fill"), fill)
    if border:
        pbdr = OxmlElement("w:pBdr")
        bottom = OxmlElement("w:bottom")
        bottom.set(qn("w:val"), "single")
        bottom.set(qn("w:sz"), "8")
        bottom.set(qn("w:color"), border)
        pbdr.append(bottom)
        ppr.append(pbdr)


def set_cell_shading(cell, fill):
    tcpr = cell._tc.get_or_add_tcPr()
    shd = tcpr.find(qn("w:shd"))
    if shd is None:
        shd = OxmlElement("w:shd")
        tcpr.append(shd)
    shd.set(qn("w:fill"), fill)


def set_cell_margins(cell, top=100, start=120, bottom=100, end=120):
    tc = cell._tc
    tcpr = tc.get_or_add_tcPr()
    tcmar = tcpr.first_child_found_in("w:tcMar")
    if tcmar is None:
        tcmar = OxmlElement("w:tcMar")
        tcpr.append(tcmar)
    for m, value in (("top", top), ("start", start), ("bottom", bottom), ("end", end)):
        node = tcmar.find(qn(f"w:{m}"))
        if node is None:
            node = OxmlElement(f"w:{m}")
            tcmar.append(node)
        node.set(qn("w:w"), str(value))
        node.set(qn("w:type"), "dxa")


def set_repeat_table_header(row):
    trpr = row._tr.get_or_add_trPr()
    tbl_header = OxmlElement("w:tblHeader")
    tbl_header.set(qn("w:val"), "true")
    trpr.append(tbl_header)


def set_table_widths(table, widths):
    table.autofit = False
    for row in table.rows:
        trpr = row._tr.get_or_add_trPr()
        cant_split = OxmlElement("w:cantSplit")
        trpr.append(cant_split)
        for idx, width in enumerate(widths):
            row.cells[idx].width = Inches(width)
            tcpr = row.cells[idx]._tc.get_or_add_tcPr()
            tcw = tcpr.find(qn("w:tcW"))
            if tcw is None:
                tcw = OxmlElement("w:tcW")
                tcpr.append(tcw)
            tcw.set(qn("w:w"), str(int(width * 1440)))
            tcw.set(qn("w:type"), "dxa")
    tblpr = table._tbl.tblPr
    tblw = tblpr.find(qn("w:tblW"))
    if tblw is None:
        tblw = OxmlElement("w:tblW")
        tblpr.append(tblw)
    tblw.set(qn("w:w"), str(int(sum(widths) * 1440)))
    tblw.set(qn("w:type"), "dxa")
    tblind = tblpr.find(qn("w:tblInd"))
    if tblind is None:
        tblind = OxmlElement("w:tblInd")
        tblpr.append(tblind)
    tblind.set(qn("w:w"), "120")
    tblind.set(qn("w:type"), "dxa")


def keep_with_next(paragraph):
    paragraph.paragraph_format.keep_with_next = True


def add_page_number(paragraph):
    paragraph.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    run = paragraph.add_run("Page ")
    set_font(run, size=9, color=MID_GRAY)
    fld = OxmlElement("w:fldSimple")
    fld.set(qn("w:instr"), "PAGE")
    paragraph._p.append(fld)


def add_heading(doc, text, level=1):
    p = doc.add_paragraph(text, style=f"Heading {level}")
    keep_with_next(p)
    return p


def add_body(doc, text, bold_lead=None):
    p = doc.add_paragraph()
    if bold_lead and text.startswith(bold_lead):
        r = p.add_run(bold_lead)
        set_font(r, bold=True, color=NAVY)
        r = p.add_run(text[len(bold_lead):])
        set_font(r)
    else:
        set_font(p.add_run(text))
    return p


def add_bullet(doc, text, level=0):
    style = "List Bullet" if level == 0 else "List Bullet 2"
    p = doc.add_paragraph(style=style)
    set_font(p.add_run(text))
    return p


def add_number(doc, text):
    p = doc.add_paragraph(style="List Number")
    set_font(p.add_run(text))
    return p


def add_callout(doc, label, text, kind="note"):
    colors = {"note": (LIGHT_BLUE, BLUE), "tip": (LIGHT_TEAL, TEAL), "warning": (LIGHT_ORANGE, ORANGE)}
    fill, accent = colors[kind]
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Inches(0.12)
    p.paragraph_format.right_indent = Inches(0.12)
    p.paragraph_format.space_before = Pt(6)
    p.paragraph_format.space_after = Pt(8)
    shade_paragraph(p, fill, accent)
    r = p.add_run(f"{label}: ")
    set_font(r, bold=True, color=accent)
    set_font(p.add_run(text))
    return p


def add_code(doc, code, caption=None):
    if caption:
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(5)
        p.paragraph_format.space_after = Pt(3)
        set_font(p.add_run(caption), size=9, bold=True, color=MID_GRAY)
        keep_with_next(p)
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Inches(0.12)
    p.paragraph_format.right_indent = Inches(0.12)
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(8)
    p.paragraph_format.line_spacing = 1.0
    p.paragraph_format.keep_together = True
    shade_paragraph(p, "F6F8FA")
    for i, line in enumerate(code.rstrip().splitlines()):
        if i:
            p.add_run().add_break()
        set_font(p.add_run(line or " "), name="Courier New", size=8.5, color="24292F")
    return p


def add_component_table(doc, rows):
    table = doc.add_table(rows=1, cols=3)
    table.style = "Table Grid"
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    headers = ["Component", "Primary responsibility", "When you change it"]
    for i, text in enumerate(headers):
        cell = table.rows[0].cells[i]
        set_cell_shading(cell, NAVY)
        set_cell_margins(cell)
        cell.vertical_alignment = WD_CELL_VERTICAL_ALIGNMENT.CENTER
        p = cell.paragraphs[0]
        set_font(p.add_run(text), size=9.5, bold=True, color=WHITE)
    set_repeat_table_header(table.rows[0])
    for component, responsibility, when in rows:
        cells = table.add_row().cells
        for i, text in enumerate((component, responsibility, when)):
            set_cell_margins(cells[i], top=90, bottom=90)
            cells[i].vertical_alignment = WD_CELL_VERTICAL_ALIGNMENT.CENTER
            if len(table.rows) % 2 == 0:
                set_cell_shading(cells[i], "F8FAFC")
            p = cells[i].paragraphs[0]
            set_font(p.add_run(text), size=9.2, bold=(i == 0), color=NAVY if i == 0 else BLACK)
    set_table_widths(table, [1.55, 2.65, 2.30])
    doc.add_paragraph().paragraph_format.space_after = Pt(1)
    return table


def configure_styles(doc):
    sec = doc.sections[0]
    sec.top_margin = Inches(0.75)
    sec.bottom_margin = Inches(0.75)
    sec.left_margin = Inches(0.85)
    sec.right_margin = Inches(0.85)
    sec.header_distance = Inches(0.35)
    sec.footer_distance = Inches(0.35)

    normal = doc.styles["Normal"]
    normal.font.name = "Calibri"
    normal._element.rPr.rFonts.set(qn("w:ascii"), "Calibri")
    normal._element.rPr.rFonts.set(qn("w:hAnsi"), "Calibri")
    normal.font.size = Pt(11)
    normal.font.color.rgb = RGBColor.from_string(BLACK)
    normal.paragraph_format.space_after = Pt(6)
    normal.paragraph_format.line_spacing = 1.15

    specs = {
        "Heading 1": (16, BLUE, 18, 8),
        "Heading 2": (13, BLUE, 14, 7),
        "Heading 3": (12, NAVY, 10, 5),
    }
    for name, (size, color, before, after) in specs.items():
        st = doc.styles[name]
        st.font.name = "Calibri"
        st._element.rPr.rFonts.set(qn("w:ascii"), "Calibri")
        st._element.rPr.rFonts.set(qn("w:hAnsi"), "Calibri")
        st.font.size = Pt(size)
        st.font.bold = True
        st.font.color.rgb = RGBColor.from_string(color)
        st.paragraph_format.space_before = Pt(before)
        st.paragraph_format.space_after = Pt(after)
        st.paragraph_format.keep_with_next = True

    for name in ("List Bullet", "List Bullet 2", "List Number"):
        st = doc.styles[name]
        st.font.name = "Calibri"
        st._element.rPr.rFonts.set(qn("w:ascii"), "Calibri")
        st._element.rPr.rFonts.set(qn("w:hAnsi"), "Calibri")
        st.font.size = Pt(11)
        st.paragraph_format.space_after = Pt(4)
        st.paragraph_format.line_spacing = 1.15


def add_running_furniture(doc):
    sec = doc.sections[0]
    hp = sec.header.paragraphs[0]
    hp.alignment = WD_ALIGN_PARAGRAPH.LEFT
    hp.paragraph_format.space_after = Pt(0)
    set_font(hp.add_run("PLAYWRIGHT AUTOMATION FRAMEWORK  |  ONBOARDING GUIDE"), size=8.5, bold=True, color=MID_GRAY)
    fp = sec.footer.paragraphs[0]
    add_page_number(fp)


def add_cover(doc):
    doc.add_paragraph().paragraph_format.space_after = Pt(62)
    kicker = doc.add_paragraph()
    kicker.alignment = WD_ALIGN_PARAGRAPH.CENTER
    set_font(kicker.add_run("TECHNICAL ONBOARDING GUIDE"), size=10, bold=True, color=ORANGE)
    kicker.paragraph_format.space_after = Pt(16)
    title = doc.add_paragraph()
    title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    title.paragraph_format.space_after = Pt(8)
    set_font(title.add_run("Playwright Java Cucumber\nAutomation Framework"), size=28, bold=True, color=NAVY)
    subtitle = doc.add_paragraph()
    subtitle.alignment = WD_ALIGN_PARAGRAPH.CENTER
    subtitle.paragraph_format.space_after = Pt(26)
    set_font(subtitle.add_run("How the framework works, how to run it, and how to add new tests"), size=14, color=TEAL)

    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.space_before = Pt(210)
    p.paragraph_format.space_after = Pt(4)
    set_font(p.add_run("Framework stack"), size=9, bold=True, color=MID_GRAY)
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    set_font(p.add_run("Java 17  •  Maven  •  Cucumber  •  JUnit 4  •  Microsoft Playwright  •  Extent Reports  •  GitLab CI/CD"), size=10, color=NAVY)
    doc.add_page_break()


def build():
    doc = Document()
    configure_styles(doc)
    add_running_furniture(doc)
    add_cover(doc)

    add_heading(doc, "How to use this guide", 1)
    add_body(doc, "This guide is written for a teammate who has just cloned the repository and needs to understand the architecture, execute tests, diagnose failures, and safely add new scenarios. Read Sections 1–4 for orientation, then use Sections 5–8 as an operator reference.")
    add_callout(doc, "Fast path", "To run the existing suite locally: create a root-level .env file with the required Application Under Test credentials, confirm the browser setting in Configs/config.properties, and run mvn test.", "tip")

    add_heading(doc, "Contents", 2)
    contents = [
        "1. Framework purpose and design", "2. Architecture and execution workflow", "3. Project structure and component responsibilities",
        "4. Local setup and execution", "5. GitLab CI/CD execution", "6. Adding a new test case", "7. Reports, screenshots, tracing, and reruns",
        "8. Troubleshooting and team conventions", "9. New contributor checklist"
    ]
    for item in contents:
        add_bullet(doc, item)

    add_heading(doc, "1. Framework purpose and design", 1)
    add_body(doc, "The framework automates browser-based acceptance tests with Playwright for Java. Cucumber expresses expected behavior in business-readable Gherkin, JUnit 4 starts the Cucumber runner, Maven manages dependencies and execution, and GitLab CI/CD provides repeatable pipeline execution.")
    add_body(doc, "The central design principle is separation of responsibilities. Feature files describe what the user expects; step definitions translate those statements into Java; page objects contain locators and reusable browser actions; the Playwright factory owns browser lifecycle; hooks manage scenario setup, evidence, and cleanup; utilities provide shared services.")
    add_bullet(doc, "Readable tests: Given/When/Then scenarios describe behavior without UI implementation detail.")
    add_bullet(doc, "Maintainable UI automation: locators and page actions are centralized in page objects.")
    add_bullet(doc, "Parallel-safe browser sessions: Playwright objects are stored in inheritable thread-local pools.")
    add_bullet(doc, "Environment portability: settings and credentials can be supplied locally or by CI.")
    add_bullet(doc, "Actionable evidence: HTML/PDF reports, JSON, screenshots, traces, and rerun files are produced.")

    doc.add_page_break()
    add_heading(doc, "2. Architecture and execution workflow", 1)
    if ARCH.exists():
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.paragraph_format.space_after = Pt(4)
        p.add_run().add_picture(str(ARCH), width=Inches(6.75))
        cap = doc.add_paragraph()
        cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
        set_font(cap.add_run("Figure 1. Framework components, execution flow, and GitLab pipeline integration"), size=9, italic=True, color=MID_GRAY)
    add_heading(doc, "The workflow, from command to report", 2)
    steps = [
        "Execution begins locally with mvn test or remotely when .gitlab-ci.yml starts a GitLab job.",
        "Maven reads pom.xml, resolves dependencies, compiles test code, and invokes the JUnit/Cucumber runner.",
        "PlaywrightTestRunner selects feature files and scenarios. Its @CucumberOptions define tags, feature location, glue packages, and reporting plugins.",
        "Cucumber reads a .feature file and matches each Given/When/Then sentence to a Java method in stepDefinitions.",
        "Step-definition methods call page objects and shared utilities. Secret keys are resolved before sensitive values are passed to page objects.",
        "A page object uses Playwright locators and actions. PlaywrightFactory supplies the thread-local Page, BrowserContext, Browser, and Playwright instances.",
        "Playwright drives the application under test. Hooks can attach screenshots after steps and always close browser resources after the scenario.",
        "Cucumber and Extent plugins write reports. GitLab publishes selected report folders and failure files as job artifacts."
    ]
    for s in steps:
        add_number(doc, s)

    add_callout(doc, "Important", "A feature file does not call a page object directly. Cucumber first maps its step text to a step-definition method; that method coordinates the page object and utilities.", "note")

    add_heading(doc, "3. Project structure and component responsibilities", 1)
    add_code(doc, """src/test/java/
  factory/             PlaywrightFactory.java
  hooks/               PlaywrightHooks.java
  pages/application/   ApplicationPage.java
  runner/              PlaywrightTestRunner.java
                       PlaywrightFailedTestRunner.java
  stepDefinitions/     CommonPlaywrightSteps.java
                       ApplicationPlaywrightSteps.java
  utils/               Library.java, SecretConfigReader.java,
                       SoftAssert.java, Constants.java,
                       VisualComparisonUtil.java
src/test/resources/
  features/            application.feature
  extent.properties    Extent output configuration
Configs/               config.properties
.gitlab-ci.yml         GitLab pipeline
pom.xml                Maven dependencies and build
settings.xml           Maven proxy settings for GitLab""", "Repository layout")

    add_component_table(doc, [
        ("Feature files", "Business-readable scenarios and tags in Gherkin.", "Adding or changing expected behavior."),
        ("Step definitions", "Map Gherkin phrases to Java orchestration.", "A step is new or needs different coordination."),
        ("Page objects", "Own locators and reusable UI actions.", "A page, locator, or UI workflow changes."),
        ("PlaywrightFactory", "Creates and closes Playwright, Browser, BrowserContext, and Page per thread.", "Browser/session behavior must change."),
        ("Hooks", "Scenario lifecycle, screenshots, logging, cleanup.", "Evidence or lifecycle behavior changes."),
        ("Utilities", "Configuration, secrets, assertions, constants, visual comparison.", "Shared cross-test capability is needed."),
        ("Runners", "Select features/tags and register report plugins.", "Suite scope or report plugins change."),
        ("config.properties", "Default browser and runtime behavior.", "Local/default execution settings change."),
        (".gitlab-ci.yml", "Build/test jobs, container, variables, secrets, artifacts.", "Pipeline execution or publishing changes."),
    ])

    doc.add_page_break()
    add_heading(doc, "4. Local setup and execution", 1)
    add_heading(doc, "Prerequisites", 2)
    add_bullet(doc, "Java 17 (the compiler source and target are set to 17 in pom.xml).")
    add_bullet(doc, "Apache Maven available from the terminal or IDE.")
    add_bullet(doc, "A supported browser: chrome, edge, chromium, firefox, or webkit.")
    add_bullet(doc, "Repository access and any organization-required proxy/VPN access.")

    add_heading(doc, "First-time local setup", 2)
    for s in [
        "Clone the repository and open the project root—the folder containing pom.xml.",
        "Import the Maven project in IntelliJ or your preferred Java IDE and allow dependencies to resolve.",
        "Create a file named .env in the project root. Do not commit this file.",
        "Add the required credential keys shown below.",
        "Review Configs/config.properties and select an available browser.",
        "Run the suite with mvn test."
    ]:
        add_number(doc, s)
    add_code(doc, """APP_VALID_USERNAME=replace-with-local-username
APP_VALID_PASSWORD=replace-with-local-password
APP_INVALID_USERNAME=invalid-user
APP_INVALID_PASSWORD=invalid-password""", "Example .env structure — adapt the keys for your Application Under Test")
    add_callout(doc, "Security", "SecretConfigReader checks .env, JVM system properties, and environment variables. Never place real passwords in a feature file, page object, config.properties, README, or Git commit.", "warning")

    add_heading(doc, "Configuration defaults", 2)
    add_code(doc, """playwrightBrowserType=firefox
playwrightHeadless=false
playwrightSlowMo=0
playwrightTimeout=30000
playwrightViewportWidth=1920
playwrightViewportHeight=1080
playwrightAcceptDownloads=true
playwrightTrace=false
playwrightScreenshotOnStep=failed""", "Configs/config.properties")
    add_body(doc, "Library.getProperty first checks a Maven/JVM -D system property and then falls back to config.properties. This allows a command to override a default without modifying the repository.")

    add_heading(doc, "Common local commands", 2)
    add_code(doc, """# Run all scenarios selected by the runner
mvn test

# Run only positive application scenarios
mvn test -Dcucumber.filter.tags="@APPLICATION_PLAYWRIGHT and @Positive"

# Run only negative application scenarios
mvn test -Dcucumber.filter.tags="@APPLICATION_PLAYWRIGHT and @Negative"

# Run headless with Edge
mvn test -DplaywrightHeadless=true -DplaywrightBrowserType=edge

# Enable Playwright tracing
mvn test -DplaywrightTrace=true

# Rerun only failed scenarios
mvn test -Dtest=PlaywrightFailedTestRunner""", "Local execution examples")
    add_callout(doc, "Debugging tip", "Use headed mode, a nonzero playwrightSlowMo value, screenshots=always, or playwrightTrace=true while diagnosing a failure. Restore efficient defaults before committing shared configuration.", "tip")

    add_heading(doc, "What happens when you run mvn test", 2)
    add_body(doc, "Maven Surefire includes PlaywrightTestRunner and allows parallel method execution with a configured thread count of five. The runner applies its configured application tag, loads features from src/test/resources/features, and loads glue from stepDefinitions and hooks.")
    add_code(doc, """@CucumberOptions(
    tags = "@APPLICATION_PLAYWRIGHT",
    features = "src/test/resources/features",
    glue = { "stepDefinitions", "hooks" },
    plugin = {
        "rerun:target/playwright-failed.txt",
        "html:target/playwright-builtInReport",
        "json:target/PlaywrightCucumber.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    }
)""", "Simplified PlaywrightTestRunner configuration")

    doc.add_page_break()
    add_heading(doc, "5. GitLab CI/CD execution", 1)
    add_body(doc, "The GitLab pipeline uses .gitlab-ci.yml as its entry point. It runs in the Microsoft Playwright Java v1.61.0 Noble container, so the CI environment is consistent and includes the Playwright runtime expected by the project.")
    add_heading(doc, "Pipeline behavior", 2)
    add_bullet(doc, "Workflow rules prevent a normal push to main from starting this pipeline, allow scheduled pipelines, and allow manual or other-branch execution.")
    add_bullet(doc, "The build stage runs Maven test compilation with tests skipped and publishes target/ for one day.")
    add_bullet(doc, "The test stage runs the selected Cucumber tags in headless Chromium by default.")
    add_bullet(doc, "GitLab CI/CD variables supply credentials and configurable execution options.")
    add_bullet(doc, "Reports, screenshots, JSON, and failed-test locations are uploaded as job artifacts even when tests fail.")
    add_code(doc, """stages:
  - build
  - test

variables:
  PLAYWRIGHT_BROWSER_TYPE: "chromium"
  PLAYWRIGHT_HEADLESS: "true"
  SCREENSHOTS_OPTION: "failed"
  CUCUMBER_TAGS: "@APPLICATION_PLAYWRIGHT"

build:
  image: mcr.microsoft.com/playwright/java:v1.61.0-noble
  script:
    - mvn $MAVEN_CLI_OPTS clean test-compile -DskipTests

test:
  image: mcr.microsoft.com/playwright/java:v1.61.0-noble
  script:
    - mvn $MAVEN_CLI_OPTS clean test ...""", "Simplified .gitlab-ci.yml structure")

    add_heading(doc, "Required GitLab CI/CD variables", 2)
    add_component_table(doc, [
        ("APP_VALID_USERNAME", "Valid Application Under Test username.", "Store as a masked/protected CI variable."),
        ("APP_VALID_PASSWORD", "Valid Application Under Test password.", "Store as a masked/protected CI variable."),
        ("APP_INVALID_USERNAME", "Intentional invalid username for negative testing.", "CI variable; mask if organizational policy requires."),
        ("APP_INVALID_PASSWORD", "Intentional invalid password for negative testing.", "CI variable; mask if organizational policy requires."),
        ("CUCUMBER_TAGS", "Controls which tagged scenarios execute.", "Override per pipeline/schedule when needed."),
        ("PLAYWRIGHT_BROWSER_TYPE", "Selects the CI browser.", "Default: chromium."),
        ("PLAYWRIGHT_HEADLESS", "Controls headless execution.", "Keep true for typical CI runs."),
        ("SCREENSHOTS_OPTION", "failed, always, or none.", "Use always only when extra evidence is needed."),
    ])
    add_callout(doc, "Pipeline setup", "In GitLab, add secrets under Settings → CI/CD → Variables. Use the project’s branch and environment protection rules. Do not write actual values into .gitlab-ci.yml.", "warning")

    add_heading(doc, "How to start and review a pipeline", 2)
    for s in [
        "Push a working branch, start a manual pipeline, or use a configured schedule. A normal push to main is intentionally blocked by the workflow rule.",
        "Confirm the build job completes. Compilation failures should be fixed before interpreting test results.",
        "Open the test job and review its Maven/Cucumber output.",
        "Download or browse job artifacts for Extent, Cucumber HTML/JSON, screenshots, and target/playwright-failed.txt.",
        "If a scenario failed, reproduce locally with the same tags and runtime overrides before changing the test."
    ]:
        add_number(doc, s)

    add_heading(doc, "Local versus GitLab execution", 2)
    table = doc.add_table(rows=1, cols=3)
    table.style = "Table Grid"
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    for i, t in enumerate(("Area", "Local", "GitLab CI/CD")):
        set_cell_shading(table.rows[0].cells[i], NAVY)
        set_cell_margins(table.rows[0].cells[i])
        set_font(table.rows[0].cells[i].paragraphs[0].add_run(t), size=9.5, bold=True, color=WHITE)
    set_repeat_table_header(table.rows[0])
    comparisons = [
        ("Trigger", "Developer runs mvn test.", "Pipeline workflow, schedule, or manual trigger."),
        ("Runtime", "Local Java/Maven and installed browser.", "Playwright Java container image."),
        ("Secrets", "Root .env, -D properties, or shell environment.", "GitLab CI/CD variables passed as -D properties."),
        ("Defaults", "Configs/config.properties plus command overrides.", "Pipeline variables override browser, headless, screenshots, and tags."),
        ("Evidence", "Files remain in local target/ and test-output/.", "Selected files are published as job artifacts for one day."),
        ("Typical mode", "Headed for debugging or headless for parity.", "Headless Chromium."),
    ]
    for row in comparisons:
        cells = table.add_row().cells
        for i, value in enumerate(row):
            set_cell_margins(cells[i])
            if len(table.rows) % 2 == 0:
                set_cell_shading(cells[i], "F8FAFC")
            set_font(cells[i].paragraphs[0].add_run(value), size=9.2, bold=(i == 0), color=NAVY if i == 0 else BLACK)
    set_table_widths(table, [1.25, 2.65, 2.60])

    doc.add_page_break()
    add_heading(doc, "6. Adding a new test case", 1)
    add_body(doc, "Use the following sequence for a new application behavior. Keep each layer focused: scenario language in the feature, orchestration in steps, and browser details in the page object.")

    add_heading(doc, "Step 1 — Decide the scope and tags", 2)
    add_body(doc, "Choose tags that identify the Application Under Test and test category. A scenario without the application tag selected by the runner will not execute unless you update the runner or command-line filter strategy.")
    add_code(doc, """@APPLICATION_PLAYWRIGHT @Positive @Smoke
Scenario: User signs in with valid credentials""", "Tag example")

    add_heading(doc, "Step 2 — Create or extend a feature file", 2)
    add_body(doc, "Create a descriptive .feature file under src/test/resources/features. Use business language, one behavior per scenario, and avoid locator or implementation terminology.")
    add_code(doc, """Feature: Application authentication
  As an application user
  I want to sign in successfully
  So that I can access the Application Under Test

  @APPLICATION_PLAYWRIGHT @Positive @Smoke
  Scenario: User signs in with valid credentials
    Given I navigate to the Application Under Test login page
    When I sign in with username key "APP_VALID_USERNAME"
      and password key "APP_VALID_PASSWORD"
    Then I should see the application home page""", "Generic example feature file")
    add_callout(doc, "Reuse first", "Before creating a new step phrase, search the existing stepDefinitions package. Reusing the same wording prevents duplicate or ambiguous Cucumber mappings.", "tip")

    add_heading(doc, "Step 3 — Add page-object behavior", 2)
    add_body(doc, "If the test needs a new locator or reusable UI action, add it to the appropriate page object. Keep locators private and expose behavior-oriented methods.")
    add_code(doc, """private final Locator usernameField;
private final Locator passwordField;
private final Locator signInButton;

public void signIn(String username, String password) {
    usernameField.fill(username);
    passwordField.fill(password);
    signInButton.click();
}

public boolean isHomePageDisplayed() {
    return isVisible(homePageHeader);
}""", "Illustrative page-object addition")

    add_heading(doc, "Step 4 — Add the step definition", 2)
    add_body(doc, "Map the new Gherkin sentence to a concise Java method. Obtain the Playwright Page from PlaywrightFactory and delegate detailed browser interactions to the page object.")
    add_code(doc, """@When("I sign in with username key {string} and password key {string}")
public void iSignInWithCredentialKeys(String usernameKey, String passwordKey) {
    applicationPage.signIn(
        SecretConfigReader.getSecret(usernameKey),
        SecretConfigReader.getSecret(passwordKey));
}

@Then("I should see the application home page")
public void iShouldSeeTheApplicationHomePage() {
    Assert.assertTrue(
        "Expected the Application Under Test home page.",
        applicationPage.isHomePageDisplayed());
}""", "Illustrative step-definition addition")

    add_heading(doc, "Step 5 — Add data and secrets correctly", 2)
    add_bullet(doc, "Public, non-sensitive test data may be expressed in examples, constants, or test-data resources.")
    add_bullet(doc, "Credentials and tokens must be referenced by key and resolved through SecretConfigReader.")
    add_bullet(doc, "Add a developer-only value to .env and the pipeline value to GitLab CI/CD Variables.")
    add_bullet(doc, "Do not log resolved secret values. Review any debug output before committing.")

    add_heading(doc, "Step 6 — Execute the smallest useful scope", 2)
    add_code(doc, """# Run the new smoke scenario locally
mvn test -Dcucumber.filter.tags="@APPLICATION_PLAYWRIGHT and @Smoke"

# Run in CI using the equivalent pipeline variable
CUCUMBER_TAGS=@APPLICATION_PLAYWRIGHT and @Smoke""", "Focused execution")

    add_heading(doc, "Step 7 — Review evidence and commit", 2)
    for s in [
        "Confirm the scenario passes locally in the intended browser and mode.",
        "Review the Cucumber/Extent report and any screenshot attachments.",
        "Run a related negative or regression tag if the change affects shared page behavior.",
        "Confirm no secret, generated report, screenshot, or trace file is accidentally staged.",
        "Push the branch and validate the GitLab build and test jobs."
    ]:
        add_number(doc, s)

    add_heading(doc, "Layering rules for new tests", 2)
    add_component_table(doc, [
        ("Feature", "Business intent, examples, tags, expected outcomes.", "No CSS/XPath selectors or Java implementation detail."),
        ("Steps", "Translate phrases, obtain data, coordinate page objects, assert outcomes.", "Avoid long locator/action sequences."),
        ("Page object", "Locators, waits, page actions, page-state queries.", "Avoid Cucumber annotations and scenario wording."),
        ("Utility", "Cross-cutting reusable capability.", "Do not turn application-specific behavior into a generic utility."),
        ("Hook", "Global scenario lifecycle behavior.", "Do not hide ordinary test actions in hooks."),
    ])

    add_heading(doc, "7. Reports, screenshots, tracing, and reruns", 1)
    add_component_table(doc, [
        ("Cucumber HTML", "target/playwright-builtInReport", "Human-readable built-in execution report."),
        ("Cucumber JSON", "target/PlaywrightCucumber.json", "Machine-readable results and downstream integrations."),
        ("Extent Spark", "test-output/SparkReport/", "Interactive visual HTML report."),
        ("Extent PDF", "test-output/PdfReport/ExtentPdf.pdf", "Portable report copy."),
        ("Screenshots", "Scenario attachments / test-output-screenshots/", "Evidence for failed or all steps, based on configuration."),
        ("Trace", "target/playwright-traces/trace.zip", "Detailed Playwright debugging when trace is enabled."),
        ("Rerun file", "target/playwright-failed.txt", "Failed scenario locations used by PlaywrightFailedTestRunner."),
    ])
    add_heading(doc, "Screenshot lifecycle", 2)
    add_body(doc, "PlaywrightHooks reads playwrightScreenshotOnStep during @Before. After each step, it captures a full-page PNG when the mode is always, or only when the scenario has failed and the mode is failed. The screenshot is attached to the Cucumber Scenario. The @After hook then closes Playwright resources.")
    add_code(doc, """@AfterStep
public void afterStep(Scenario scenario) {
    if ("always".equals(screenshotMode)
            || ("failed".equals(screenshotMode) && scenario.isFailed())) {
        takeScreenshot(scenario);
    }
}

@After
public void tearDown() {
    PlaywrightFactory.closePlaywright();
}""", "Simplified hook behavior")
    add_heading(doc, "Failed-test rerun", 2)
    add_body(doc, "The main runner writes failed scenario locations to target/playwright-failed.txt. PlaywrightFailedTestRunner reads that file and generates separate rerun HTML and JSON outputs. Rerunning is useful after a transient environment issue, but a repeated failure should be diagnosed—not repeatedly retried until green.")
    add_code(doc, "mvn test -Dtest=PlaywrightFailedTestRunner", "Rerun command")

    add_heading(doc, "8. Troubleshooting and team conventions", 1)
    add_heading(doc, "Common issues", 2)
    issues = [
        ("No scenarios run", "Check the runner tag, command-line tag expression, feature path, and scenario tags. The scenario must match the application tag selected by the runner."),
        ("Undefined step", "Confirm the annotation text exactly matches the Gherkin phrase and the class is under a configured glue package."),
        ("Ambiguous step", "Search for duplicate annotation expressions and consolidate reusable wording."),
        ("Browser does not launch", "Verify the selected browser/channel exists locally. Try chrome or edge on managed machines, or use the CI container for Chromium."),
        ("Secret key not found", "Confirm exact key spelling in .env, -D arguments, shell variables, or GitLab CI/CD Variables."),
        ("Test passes locally, fails in CI", "Compare browser type, headless mode, viewport, tags, proxy/network access, data, and secret scope."),
        ("Timeout", "Check page state and locator quality before increasing playwrightTimeout. Prefer resilient Playwright locators and explicit state checks."),
        ("Missing screenshot", "Verify playwrightScreenshotOnStep/SCREENSHOTS_OPTION and confirm PlaywrightFactory created a page before failure."),
        ("Missing GitLab report", "Confirm the output path matches the artifact path and artifacts use when: always."),
    ]
    table = doc.add_table(rows=1, cols=2)
    table.style = "Table Grid"
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    for i, h in enumerate(("Symptom", "What to check")):
        set_cell_shading(table.rows[0].cells[i], NAVY)
        set_cell_margins(table.rows[0].cells[i])
        set_font(table.rows[0].cells[i].paragraphs[0].add_run(h), size=9.5, bold=True, color=WHITE)
    set_repeat_table_header(table.rows[0])
    for issue, check in issues:
        cells = table.add_row().cells
        for i, value in enumerate((issue, check)):
            set_cell_margins(cells[i])
            if len(table.rows) % 2 == 0:
                set_cell_shading(cells[i], "F8FAFC")
            set_font(cells[i].paragraphs[0].add_run(value), size=9.2, bold=(i == 0), color=NAVY if i == 0 else BLACK)
    set_table_widths(table, [1.65, 4.85])

    add_heading(doc, "Team conventions", 2)
    add_bullet(doc, "Use behavior-oriented names: describe what the user does or observes, not the selector implementation.")
    add_bullet(doc, "Reuse existing steps where the business meaning is the same.")
    add_bullet(doc, "Keep page objects cohesive; create a new page/component object when responsibilities separate naturally.")
    add_bullet(doc, "Prefer Playwright auto-waiting and stable locators over arbitrary sleeps.")
    add_bullet(doc, "Keep tests independent so parallel execution does not create shared-state failures.")
    add_bullet(doc, "Never commit secrets or print resolved credentials in logs.")
    add_bullet(doc, "Treat report and artifact paths as contracts shared by Maven, runners, Extent, and GitLab.")

    add_callout(doc, "Security review", "SecretConfigReader currently includes debug output that prints the resolved value. Remove or mask any secret-value logging before using real credentials in shared logs or CI.", "warning")

    add_heading(doc, "9. New contributor checklist", 1)
    checks = [
        "Java 17 and Maven are available.",
        "The Maven project imports and compiles.",
        "A local .env file exists and is excluded from version control.",
        "The configured browser is available.",
        "mvn test runs the expected application-tagged scenarios.",
        "The contributor can find the Cucumber and Extent reports.",
        "The contributor understands feature → steps → page object → Playwright flow.",
        "A focused tag expression can run one scenario category.",
        "New secrets are added to both the local environment and GitLab CI/CD Variables.",
        "The GitLab pipeline publishes evidence and does not expose secrets.",
    ]
    for c in checks:
        add_bullet(doc, "☐ " + c)

    props = doc.core_properties
    props.title = "Playwright Java Cucumber Automation Framework — Onboarding Guide"
    props.subject = "Framework architecture, local execution, GitLab CI/CD, and adding tests"
    props.author = "Automation Team"
    props.keywords = "Playwright, Java, Cucumber, Maven, GitLab CI/CD, test automation"

    OUT.parent.mkdir(parents=True, exist_ok=True)
    doc.save(OUT)
    print(OUT)


if __name__ == "__main__":
    build()
