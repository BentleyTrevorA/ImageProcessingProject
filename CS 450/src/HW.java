import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class HW
{
    public static void main(String[] args) {
        HW hw = new HW();
        CS450.run(hw);

        for (String name : ImageIO.getReaderFormatNames()) {
            System.out.println(name);
        }
    }

    public void doOpen() {
        BufferedImage img = CS450.openImage();

        if (img != null) {
            CS450.setImageA(img);
        }
    }

    public void doSave() {
        BufferedImage img = CS450.getImageB();

        CS450.saveImage(img);
    }
    /*
    public void doGreyScale() {
        int[] values = new int[256];

        BufferedImage inputImage = CS450.getImageA();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        WritableRaster in = inputImage.getRaster();
        WritableRaster out = outputImage.getRaster();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = in.getSample(x, y, 0);
                int g = in.getSample(x, y, 1);
                int b = in.getSample(x, y, 2);

                // Grey = 0.299 Red + 0.587 Green + 0.114 Blue
                double value = .299 * r + .587 * g + .114 * b;

                int v = (int)value;

                values[v]++;

                // Set red, green, and blue parts of image
                out.setSample(x, y, 0, value);
                out.setSample(x, y, 1, value);
                out.setSample(x, y, 2, value);
            }
        }

        // Print array values for histogram
        printArray(values);
        CS450.setImageB(outputImage);
    }*/

    public void doEqualization() {
        int[] histogramValues = new int[256];
        int[] equalizedHistogram = new int[256];
        float[] scaledHistogram = new float[256];

        BufferedImage inputImage = CS450.getImageA();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        float area = width * height;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // P(r) = H(r) / Area
        WritableRaster in = inputImage.getRaster();
        WritableRaster out = outputImage.getRaster();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                histogramValues[in.getSample(x, y, 0)]++;
            }
        }

        for(int i=0; i< 256; i++) {
            int total = 0;
            for(int j=0; j<i; j++) {
                total += histogramValues[j];
            }
            scaledHistogram[i] = total / area;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = in.getSample(x, y, 0);

                int value = (int)(scaledHistogram[r] * 256);
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

    /*
    public void doThreshold() {
        String threshold = CS450.prompt("threshold (0 - 255)", "128");
        if (threshold == null) return;
        int t = Integer.parseInt(threshold);

        BufferedImage inputImage = CS450.getImageA();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        WritableRaster in = inputImage.getRaster();
        WritableRaster out = outputImage.getRaster();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int val = in.getSample(x, y, 0);

                if (val < t) {
                    out.setSample(x, y, 0, 0); // black
                }
                else {
                    out.setSample(x, y, 0, 255); // white
                }
            }
        }

        CS450.setImageB(outputImage);
    }*/

    /*
    public void doColor_Filter() {
        String[] choices = {"RED", "GREEN", "BLUE"};
        String colorChannel = CS450.prompt("color", choices, "GREEN");
        if (colorChannel == null) return;

        BufferedImage inputImage = CS450.getImageA();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        WritableRaster in = inputImage.getRaster();
        WritableRaster out = outputImage.getRaster();

        int channel = 0; // defaults to RED filter

        if (colorChannel.equals("GREEN")) {
            channel = 1;
        }
        else if (colorChannel.equals("BLUE")) {
            channel = 2;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int val = in.getSample(x, y, channel);

                out.setSample(x, y, channel, val);
            }
        }

        CS450.setImageB(outputImage);
    }*/
}