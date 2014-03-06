import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class HW4
{
    int width, height, kernelSize, kernelCenter;
    float kernelSum;
    float[][] kernel;
    BufferedImage inputImg, outputImg;
    File file;
    WritableRaster in, out;
    String whiteBox = "img\\2D_White_Box.pgm";
    String blocks = "img\\blocks.pgm";
    String img = blocks;
    private static boolean debug = true;

    public void doUniformAveraging() throws IOException {
        setupImages();

        uniformAverage();

        CS450.setImageA(inputImg);
        CS450.setImageB(outputImg);
    }

    public void doSobelX() throws IOException {
        if(debug)
            System.out.println("Sobel X");

        setupImages();

        rightSobel(false);
        leftSobel(true);

        CS450.setImageA(inputImg);
        CS450.setImageB(outputImg);
    }

    public void doSobelY() throws IOException {
        if(debug)
            System.out.println("Sobel Y");

        setupImages();

        topSobel(false);
        bottomSobel(true);

        CS450.setImageA(inputImg);
        CS450.setImageB(outputImg);
    }

    public void doLaplacian() throws IOException {
        if(debug)
            System.out.println("Laplacian ");

        setupImages();

        laplacian(false);

        CS450.setImageA(inputImg);
        CS450.setImageB(outputImg);
    }

    public void doGradientMagnitude() throws IOException {
        setupImages();

        rightSobel(false);
        leftSobel(true);
        topSobel(true);
        bottomSobel(true);

        CS450.setImageA(inputImg);
        CS450.setImageB(outputImg);
    }

    public void doBlurThenGradientMagnitude() throws IOException{
        setupImages();

        uniformAverage();
        setFirstImageToNewImage();
        leftSobel(true);
        rightSobel(true);
        topSobel(true);
        bottomSobel(true);

        CS450.setImageA(inputImg);
        CS450.setImageB(outputImg);
    }

    public void doSharpen() throws IOException {
        setupImages();

        sharpen();

        CS450.setImageA(inputImg);
        CS450.setImageB(outputImg);
    }

    private void uniformAverage() {
        String value = CS450.prompt("Size of Kernel", new String[]{"3", "5", "7", "9"});
        if(debug)
            System.out.println("Uniform Averaging " + value + " x " + value);

        if (value == null)
            return;

        kernelSum = 0;
        kernelSize = Integer.parseInt(value);
        kernel = new float[kernelSize][kernelSize];
        for (int x = 0; x < kernelSize; x++) {
            for (int y = 0; y < kernelSize; y++) {
                kernel[x][y] = 1;
                kernelSum++;
            }
        }
        kernelCenter = (kernelSize - 1) / 2;

        applyKernel();
    }

    private void rightSobel(boolean addToKernel) {
        // RIGHT EDGE
        kernelSize = 3;
        kernel = new float[kernelSize][kernelSize];
        kernel[0][0] = 1;
        kernel[0][1] = 2;
        kernel[0][2] = 1;
        kernel[2][0] = -1;
        kernel[2][1] = -2;
        kernel[2][2] = -1;
        kernelSum = 1;
        kernelCenter = (kernelSize - 1) / 2;

        if(addToKernel)
            applyKernelAndAddToImage();
        else
            applyKernel();
    }

    private void leftSobel(boolean addToKernel) {
        // LEFT EDGE
        kernelSize = 3;
        kernel = new float[kernelSize][kernelSize];
        kernel[0][0] = -1;
        kernel[0][1] = -2;
        kernel[0][2] = -1;
        kernel[2][0] = 1;
        kernel[2][1] = 2;
        kernel[2][2] = 1;
        kernelSum = 1;
        kernelCenter = (kernelSize - 1) / 2;

        if(addToKernel)
            applyKernelAndAddToImage();
        else
            applyKernel();
    }

    private void topSobel(boolean addToKernel) {
        // Top Edge
        kernelSize = 3;
        kernel = new float[kernelSize][kernelSize];
        kernel[0][0] = 1;
        kernel[1][0] = 2;
        kernel[2][0] = 1;
        kernel[0][2] = -1;
        kernel[1][2] = -2;
        kernel[2][2] = -1;
        kernelSum = 1;
        kernelCenter = (kernelSize - 1) / 2;

        if(addToKernel)
            applyKernelAndAddToImage();
        else
            applyKernel();
    }

    private void bottomSobel(boolean addToKernel) {
        // Bottom Edge
        kernelSize = 3;
        kernel = new float[kernelSize][kernelSize];
        kernel[0][0] = -1;
        kernel[1][0] = -2;
        kernel[2][0] = -1;
        kernel[0][2] = 1;
        kernel[1][2] = 2;
        kernel[2][2] = 1;
        kernelSum = 1;
        kernelCenter = (kernelSize - 1) / 2;

        if(addToKernel)
            applyKernelAndAddToImage();
        else
            applyKernel();
    }

    private void laplacian(boolean addToKernel) {
        kernelSize = 3;
        kernel = new float[kernelSize][kernelSize];
        kernel[0][1] = -1;
        kernel[1][0] = -1;
        kernel[1][1] = 4;
        kernel[1][2] = -1;
        kernel[2][1] = -1;
        kernelSum = 1;
        kernelCenter = (kernelSize - 1) / 2;

        if(addToKernel)
            applyKernelAndAddToImage();
        else
            applyKernel();
    }

    private void sharpen() {
        int a = 3;

        kernelSize = 3;
        kernel = new float[kernelSize][kernelSize];
        kernel[0][1] = -1;
        kernel[1][0] = -1;
        kernel[1][1] = a + 4;
        kernel[1][2] = -1;
        kernel[2][1] = -1;
        kernelSum = a;
        kernelCenter = (kernelSize - 1) / 2;

        applyKernel();
    }

    // Read in specified image, and create a new blank image for writing to
    private void setupImages() throws IOException {
        String value = CS450.prompt("Image", new String[]{"White Box", "Blocks"});

        if(value !=null && value.equals("White Box"))
            file = new File(whiteBox);
        else
            file = new File(blocks);


        inputImg = ImageIO.read(file);
        in = inputImg.getRaster();

        width = inputImg.getWidth();
        height = inputImg.getHeight();

        outputImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        out = outputImg.getRaster();
    }

    // Set the inputImg to be the outputImg and reset the OutputImg
    private void setFirstImageToNewImage() {
        inputImg = outputImg;
        in = inputImg.getRaster();

        outputImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        out = outputImg.getRaster();
    }

    private void applyKernel() {
        debugPrintKernel();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float pixelValue = applyKernelToNeighborhood(x, y) / kernelSum;

                if(pixelValue > 255)
                    pixelValue = 255;
                if(pixelValue < 0)
                    pixelValue = 0;

                out.setSample(x, y, 0, pixelValue);
                out.setSample(x, y, 1, pixelValue);
                out.setSample(x, y, 2, pixelValue);
            }
        }
    }

    private void applyKernelAndAddToImage() {
        debugPrintKernel();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float pixelValue = applyKernelToNeighborhood(x, y) / kernelSum;

                if(pixelValue > 255)
                    pixelValue = 255;
                if(pixelValue < 0)
                    pixelValue = 0;

                pixelValue = out.getSample(x, y, 0) + pixelValue;

                out.setSample(x, y, 0, pixelValue);
                out.setSample(x, y, 1, pixelValue);
                out.setSample(x, y, 2, pixelValue);
            }
        }
    }

    private float applyKernelToNeighborhood(int x, int y) {
        float sum = 0;
        for (int xPos = 0; xPos < kernelSize; xPos++) {
            for (int yPos = 0; yPos < kernelSize; yPos++) {
                Point index = calculateIndex(xPos, yPos, x, y);
                sum += kernel[xPos][yPos] * in.getSample(index.x, index.y, 0);
            }
        }

        return sum;
    }

    private void debugPrintKernel() {
        if (!debug)
            return;

        String output = "KERNEL:\n";
        for (int x = 0; x < kernelSize; x++) {
            for (int y = 0; y < kernelSize; y++) {
                output += "[" + (int)kernel[x][y] + "]";
            }
            output += "\n";
        }
        System.out.println(output);
    }

    // Calculate the index of the image to reference - if the index extends past the end of the
    // image, wrap around to other side of image
    private Point calculateIndex(int xPos, int yPos, int x, int y) {
        int xOffset = xPos - kernelCenter;
        int yOffset = yPos - kernelCenter;

        int xIndex = x + xOffset;
        int yIndex = y + yOffset;

        // Wrap around image when index is out of bounds
        if (xIndex < 0) {
            xIndex = (width - 1) + xIndex;
        }
        else if (xIndex > (width - 1)) {
            xIndex = xIndex - (width - 1);
        }

        // Wrap around image when index is out of bounds
        if (yIndex < 0) {
            yIndex = (height - 1) + yIndex;
        }
        else if (yIndex > (height - 1)) {
            yIndex = yIndex - (height - 1);
        }

        return new Point(xIndex, yIndex);
    }
}
