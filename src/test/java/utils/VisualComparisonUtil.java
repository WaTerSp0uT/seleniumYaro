package utils;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;

public class VisualComparisonUtil {

    private VisualComparisonUtil() {}

    /**
     * Saves "actual" into target folder, compares to expected.
     * On mismatch saves diff into target folder and throws AssertionError.
     */
    public static void compareAndFailOnMismatch(
            BufferedImage expected,
            BufferedImage actual,
            Path actualOutputPath,
            Path diffOutputPath,
            int threshold
    ) {
        try {
            Files.createDirectories(actualOutputPath.getParent());
            ImageIO.write(actual, "PNG", actualOutputPath.toFile());

            ImageComparison comparison = new ImageComparison(expected, actual);
            comparison.setThreshold(threshold);

            ImageComparisonResult result = comparison.compareImages();

            System.out.println("Visual state:" + result.getImageComparisonState());

            if (result.getImageComparisonState() != ImageComparisonState.MATCH) {
                Files.createDirectories(diffOutputPath.getParent());
                ImageIO.write(result.getResult(), "PNG", diffOutputPath.toFile());

                System.out.println("Visual state:" + result.getImageComparisonState());
                System.out.println("Diff rectangles:" + result.getRectangles().size());
                System.out.println("Diff percent:" + result.getDifferencePercent());

                throw new AssertionError(
                        "Visual mismatch!\nActual: " + actualOutputPath +
                                "\nDiff: " + diffOutputPath
                );
            }

        } catch (IOException e) {
            throw new RuntimeException("Visual comparison failed", e);
        }
    }
}