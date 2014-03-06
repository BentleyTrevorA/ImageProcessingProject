import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class HW2 extends HWBase
{
    public void doEqualization() {
        int[] histogramValues = new int[256];
        int[] equalizedHistogram = new int[256];
        float[] transformationValues = new float[256];

        BufferedImage inputImage = CS450.getImageA();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        float area = width * height;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // P(r) = H(r) / Area
        WritableRaster in = inputImage.getRaster();
        WritableRaster out = outputImage.getRaster();

        // Get original histogram H(R)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                histogramValues[in.getSample(x, y, 0)]++;
            }
        }

        // Calculate the corresponding transformation values using summation (these values will be used to map old histogram
        // to the equalized one
        // S(R) = (SUM[H(0) -> H(R)])/number of pixels
        for(int i=0; i< 256; i++) {
            int total = 0;
            for(int j=0; j<i; j++) {
                total += histogramValues[j];
            }
            transformationValues[i] = total / area;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = in.getSample(x, y, 0);

                // Scale the scaledHistogram by 256 to map value to the 0-255 range
                int value = (int)(transformationValues[r] * 256);
                // Calculate equalized histogram
                equalizedHistogram[value]++;

                // Set red, green, and blue parts of image
                out.setSample(x, y, 0, value);
                out.setSample(x, y, 1, value);
                out.setSample(x, y, 2, value);
            }
        }

        // Print array values for histograms
        printArray(histogramValues);

        System.out.println("EQUALIZED");
        printArray(equalizedHistogram);

        CS450.setImageB(outputImage);
    }

    private void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}