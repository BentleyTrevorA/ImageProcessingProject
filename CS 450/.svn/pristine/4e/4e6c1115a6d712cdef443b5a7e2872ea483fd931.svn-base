import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedMap;

public class HW3
{
    int width, height, kernelSize, kernelCenter;
    float[][] kernel;
    BufferedImage inputImg, outputImg;
    File file;
    WritableRaster in, out;

    public void doAveraging() throws Exception {
        String prompt = CS450.prompt("How many images to average?", new String[]{"2", "5", "10", "20", "40"});

        if (prompt == null)
            return;

        int howMany = Integer.parseInt(prompt);

        setupImages("img\\Frames\\Cat0.pgm");

        float sums[][] = new float[width][height];

        for (int i = 0; i < howMany; i++) {
            file = new File("img\\Frames\\Cat" + i + ".pgm");
            inputImg = ImageIO.read(file);

            if (inputImg == null) {
                throw new Exception("unable to read image");
            }

            in = inputImg.getRaster();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    sums[x][y] += in.getSample(x, y, 0);
                }
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float value = sums[x][y] / howMany;
                out.setSample(x, y, 0, value);
                out.setSample(x, y, 1, value);
                out.setSample(x, y, 2, value);
            }
        }

        CS450.setImageA(outputImg);
        CS450.setImageB(outputImg);
    }

    public void doUniformAveraging() throws IOException {
        String value = CS450.prompt("Size of Kernel", new String[]{"3", "5", "7", "9"});

        if (value == null)
            return;

        kernelSize = Integer.parseInt(value);
        kernel = new float[kernelSize][kernelSize];
        kernelCenter = (kernelSize - 1) / 2;

        setupImages("img\\NoisyGull.pgm");

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float pixelValue = uniformAverage(x, y);
                out.setSample(x, y, 0, pixelValue);
                out.setSample(x, y, 1, pixelValue);
                out.setSample(x, y, 2, pixelValue);
            }
        }

        CS450.setImageA(inputImg);
        CS450.setImageB(outputImg);
    }

    private float uniformAverage(int x, int y) {
        float sum = 0;
        for(int xPos = 0; xPos < kernelSize; xPos++) {
            for(int yPos = 0; yPos < kernelSize; yPos++) {
                Point index = calculateIndex(xPos, yPos, x, y);
                sum += in.getSample(index.x, index.y, 0);
            }
        }

        return (sum / (kernelSize * kernelSize));
    }

    public void doMedianFilter() throws IOException {
        String value = CS450.prompt("Size of Kernel", new String[]{"3", "5", "7"});

        if (value == null)
            return;

        kernelSize = Integer.parseInt(value);
        kernel = new float[kernelSize][kernelSize];
        kernelCenter = (kernelSize - 1) / 2;

        setupImages("img\\NoisyGull.pgm");

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixelValue = medianFilter(x, y);
                out.setSample(x, y, 0, pixelValue);
                out.setSample(x, y, 1, pixelValue);
                out.setSample(x, y, 2, pixelValue);
            }
        }

        CS450.setImageA(inputImg);
        CS450.setImageB(outputImg);
    }

    private int medianFilter(int x, int y) {
        ArrayList<Integer> medianList = new ArrayList<Integer>();
        for(int xPos = 0; xPos < kernelSize; xPos++) {
            for(int yPos = 0; yPos < kernelSize; yPos++) {
                Point index = calculateIndex(xPos, yPos, x, y);
                medianList.add(in.getSample(index.x, index.y, 0));
            }
        }

        Collections.sort(medianList);

        int centerIndex = (medianList.size() - 1) / 2;
        return medianList.get(centerIndex);
    }

    // Read in specified image, and create a new blank image for writing to
    private void setupImages(String imgPath) throws IOException {
        file = new File(imgPath);
        inputImg = ImageIO.read(file);
        in = inputImg.getRaster();

        width = inputImg.getWidth();
        height = inputImg.getHeight();

        outputImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        out = outputImg.getRaster();
    }

    // Calculate the index of the image to reference - if the index extends past the end of the
    // image, wrap around to other side of image
    private Point calculateIndex(int xPos, int yPos, int x, int y) {
        int xOffset = xPos - kernelCenter;
        int yOffset = yPos - kernelCenter;

        int xIndex = x + xOffset;
        int yIndex = y + yOffset;

        // Wrap around image when index is out of bounds
        if(xIndex < 0) {
            xIndex = (width - 1) + xIndex;
        }
        else if(xIndex > (width - 1)) {
            xIndex = xIndex - (width - 1);
        }

        // Wrap around image when index is out of bounds
        if(yIndex < 0) {
            yIndex = (height - 1) + yIndex;
        }
        else if(yIndex > (height - 1)) {
            yIndex = yIndex - (height - 1);
        }

        return new Point(xIndex, yIndex);
    }
}
