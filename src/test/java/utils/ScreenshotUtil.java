package utils;

import org.openqa.selenium.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.*;

public class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    public static byte[] captureBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static BufferedImage captureImage(WebDriver driver) {
        try {
            byte[] bytes = captureBytes(driver);
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert screenshot to BufferedImage", e);
        }
    }

    public static void saveBytesToFile(byte[] bytes, Path outputPath) {
        try {
            Files.createDirectories(outputPath.getParent());
            Files.write(outputPath, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed saving screenshot: " + outputPath, e);
        }
    }

    public static String safeName(String raw) {
        if (raw == null || raw.isBlank()) return "screenshot";
        return raw.replaceAll("[^a-zA-Z0-9._/-]+", "_");
    }
}