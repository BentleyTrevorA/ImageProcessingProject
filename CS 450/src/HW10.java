import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class HW10 extends HWBase{
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
        BufferedImage inputImage = CS450.getImageA();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        WritableRaster in = inputImage.getRaster();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster out = outputImage.getRaster();

        int left, topLeft, top, topRight;
        int predictiveValue;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = in.getSample(x, y, 0);

                if(y == 0 && x == 0) {
                    predictiveValue = value;
                }
                else if(y == 0 && x > 0) {
                    left = in.getSample(x-1, y, 0);
                    predictiveValue = adjustValueToBeInRange(left - value);
                }
                else if(y > 0 && x == 0){
                    top = in.getSample(x, y-1, 0);
                    topRight = in.getSample(x + 1, y - 1, 0);
                    int avg = (top + topRight) / 2;
                    predictiveValue = adjustValueToBeInRange(avg - value);
                }
                else if(y > 0 && x == width -1) {
                    left = in.getSample(x-1, y, 0);
                    topLeft = in.getSample(x - 1, y - 1, 0);
                    top = in.getSample(x, y - 1, 0);

                    int avg = (left + topLeft + top) / 3;
                    predictiveValue = adjustValueToBeInRange(avg - value);
                }
                else {
                    left = in.getSample(x-1, y, 0);
                    topLeft = in.getSample(x - 1, y - 1, 0);
                    top = in.getSample(x, y - 1, 0);
                    topRight = in.getSample(x + 1, y - 1, 0);

                    int avg = (left + topLeft + top + topRight) / 4;
                    predictiveValue = adjustValueToBeInRange(avg - value);
                }

                out.setSample(x, y, 0, predictiveValue);
                out.setSample(x, y, 1, predictiveValue);
                out.setSample(x, y, 2, predictiveValue);

            }
        }

        CS450.setImageB(outputImage);
    }

    public void doClearImgA() {
        BufferedImage outputImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        CS450.setImageA(outputImage);
    }

    public void doReversePredictiveEncoding() {
        BufferedImage inputImage = CS450.getImageB();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        WritableRaster in = inputImage.getRaster();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster out = outputImage.getRaster();

        int left, topLeft, top, topRight;
        int predictiveValue;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = in.getSample(x, y, 0);

                if(y == 0 && x == 0) {
                    predictiveValue = value;
                }
                else if(y == 0 && x > 0) {
                    left = out.getSample(x-1, y, 0);
                    predictiveValue = inverseAdjustValue(value + left);
                }
                else if(y > 0 && x == 0) {
                    top = out.getSample(x, y-1, 0);
                    topRight = out.getSample(x + 1, y - 1, 0);
                    int avg = (top + topRight) / 2;
                    predictiveValue = inverseAdjustValue(value + avg);
                }
                else if(y > 0 && x == width -1) {
                    left = out.getSample(x-1, y, 0);
                    topLeft = out.getSample(x - 1, y - 1, 0);
                    top = out.getSample(x, y - 1, 0);

                    int avg = (left + topLeft + top) / 3;
                    predictiveValue = inverseAdjustValue(value + avg);
                }
                else {
                    left = out.getSample(x-1, y, 0);
                    topLeft = out.getSample(x - 1, y - 1, 0);
                    top = out.getSample(x, y - 1, 0);
                    topRight = out.getSample(x + 1, y - 1, 0);

                    int avg = (left + topLeft + top + topRight) / 4;
                    predictiveValue = inverseAdjustValue(value + avg);
                }

                out.setSample(x, y, 0, predictiveValue);
                out.setSample(x, y, 1, predictiveValue);
                out.setSample(x, y, 2, predictiveValue);
            }
        }

        CS450.setImageA(outputImage);
    }

    private int adjustValueToBeInRange(int value) {
        if(value < 0)
            return value + 256;
        else
            return value;
    }

    private int inverseAdjustValue(int value) {
        if(value > 256)
            return value - 256;
        else
            return value;
    }
}
