import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class HW10 extends HWBase {
    /*  Using compact:
        File:         blocks.pgm
        Size:         262.2 kB
        Compact Size: 188.6 kB
     */

    /*

    Reduces the entropy of an image:

    Each pixel is predicted as the weighted average of the adjacent 4 pixels already encountered in raster order
    Three above, one to the left
    Guess pixel A based on preceding pixels

    x x x
    x a

    Calculate and encode the residual
    */
    public void doPredictiveCoding() {
        System.out.println("FORWARD");
        BufferedImage inputImage = CS450.getImageA();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        WritableRaster in = inputImage.getRaster();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster out = outputImage.getRaster();

        int left, topLeft, top, topRight;
        int residual, predictedValue;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = in.getSample(x, y, 0);

                // Top Left Pixel
                if (y == 0 && x == 0) {
                    predictedValue = 0;
                }
                // Top Row
                else if (y == 0 && x > 0) {
                    left = in.getSample(x - 1, y, 0);
                    predictedValue = left;
                }
                // Left Column
                else if (y > 0 && x == 0) {
                    top = in.getSample(x, y - 1, 0);
                    topRight = in.getSample(x + 1, y - 1, 0);

                    predictedValue = (top + topRight) / 2;
                }
                // Right Column
                else if (y > 0 && x == width - 1) {
                    left = in.getSample(x - 1, y, 0);
                    topLeft = in.getSample(x - 1, y - 1, 0);
                    top = in.getSample(x, y - 1, 0);

                    predictedValue = (left + topLeft + top) / 3;
                }
                // Center Section
                else {
                    left = in.getSample(x - 1, y, 0);
                    topLeft = in.getSample(x - 1, y - 1, 0);
                    top = in.getSample(x, y - 1, 0);
                    topRight = in.getSample(x + 1, y - 1, 0);

                    predictedValue = (left + topLeft + top + topRight) / 4;
                }

                residual = adjustValueToBeInRange(value - predictedValue);
                out.setSample(x, y, 0, residual);
                out.setSample(x, y, 1, residual);
                out.setSample(x, y, 2, residual);

                if(y == 0)
                    System.out.println(value + ", " + predictedValue + ", " + residual);

            }
        }

        CS450.setImageB(outputImage);
    }

    public void doClearImgA() {
        BufferedImage outputImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        CS450.setImageA(outputImage);
    }

    public void doReversePredictiveEncoding() {
        System.out.println("REVERSE");
        BufferedImage inputImage = CS450.getImageB();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        WritableRaster in = inputImage.getRaster();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster out = outputImage.getRaster();

        int left, topLeft, top, topRight;
        int originalValue, predictedValue;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int residual = in.getSample(x, y, 0);

                // Top Left Pixel
                if (y == 0 && x == 0) {
                    predictedValue = 0;
                }
                // Top Row
                else if (y == 0 && x > 0) {
                    left = out.getSample(x - 1, y, 0);
                    predictedValue = left;
                }
                // Left Column
                else if (y > 0 && x == 0) {
                    top = out.getSample(x, y - 1, 0);
                    topRight = out.getSample(x + 1, y - 1, 0);

                    predictedValue = (top + topRight) / 2;
                }
                // Right Column
                else if (y > 0 && x == width - 1) {
                    left = out.getSample(x - 1, y, 0);
                    topLeft = out.getSample(x - 1, y - 1, 0);
                    top = out.getSample(x, y - 1, 0);

                    predictedValue = (left + topLeft + top) / 3;
                }
                // Center Section
                else {
                    left = out.getSample(x - 1, y, 0);
                    topLeft = out.getSample(x - 1, y - 1, 0);
                    top = out.getSample(x, y - 1, 0);
                    topRight = out.getSample(x + 1, y - 1, 0);

                    predictedValue = (left + topLeft + top + topRight) / 4;
                }

                originalValue = inverseAdjustValue(residual + predictedValue);
                out.setSample(x, y, 0, originalValue);
                out.setSample(x, y, 1, originalValue);
                out.setSample(x, y, 2, originalValue);

                if(y == 0)
                    System.out.println(originalValue + ", " + predictedValue + ", " + residual);
            }
        }

        CS450.setImageA(outputImage);
    }

    public void doCopyAtoB() {
        BufferedImage inputImage = CS450.getImageA();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        WritableRaster in = inputImage.getRaster();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster out = outputImage.getRaster();;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = in.getSample(x, y, 0);

                out.setSample(x, y, 0, value);
                out.setSample(x, y, 1, value);
                out.setSample(x, y, 2, value);
            }
        }

        CS450.setImageB(outputImage);
    }

    public void doHistogramImgA() {
        BufferedImage inputImage = CS450.getImageA();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        WritableRaster in = inputImage.getRaster();

        int[] histogram = new int[256];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = in.getSample(x, y, 0);
                if(value >= 256 / 2) {
                    histogram[value - (256 / 2)]++;
                }
                else
                    histogram[value + 256 / 2]++;
            }
        }

        Plotter plotter = new Plotter("Histogram");
        plotter.makeChart(histogram);
        plotter.pack();
        RefineryUtilities.centerFrameOnScreen(plotter);
        plotter.setVisible(true);
        plotter.setSize(new Dimension(640, 480));
    }

    private int adjustValueToBeInRange(int value) {
        if (value < 0)
            return value + 256;
        else
            return value;
    }

    private int inverseAdjustValue(int value) {
        if (value > 256)
            return value - 256;
        else
            return value;
    }
}
