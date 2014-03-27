import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class HW8 {
    /* **************************************
     *          GUI FUNCTIONS
     * **************************************/

    public void doSave_Img_B() {
        BufferedImage img = CS450.getImageB();

        CS450.saveImage(img);
    }

    public void doLoad() {
        BufferedImage img = CS450.openImage();
        CS450.setImageA(img);
    }

    // Part 1
    public void doMagnify_Image() {
        BufferedImage img = CS450.openImage();
        WritableRaster imgRaster = img.getRaster();
        CS450.setImageA(img);
        int width = img.getWidth();
        int height = img.getHeight();

        int factor = Integer.parseInt(CS450.prompt("Enter the multiplication factor you'd like to use"));
        float factorFloat = (float)factor;
        int newWidth = img.getWidth() * factor;
        int newHeight = img.getHeight() * factor;

        BufferedImage newImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        WritableRaster newImgRaster = newImg.getRaster();

        for(int x = 0; x < newWidth; x++) {
            for(int y = 0; y < newHeight; y++) {

                float xPos = x / factorFloat;
                float yPos = y / factorFloat;

                int x0 = (int)(xPos - (xPos % 1));
                int x1 = x0 + 1;

                if(x1 >= width) {
                    x1 = width - 1;
                }

                int y0 = (int)(yPos - (yPos % 1));
                int y1 = y0 + 1;

                if(y1 >= height) {
                    y1 = height - 1;
                }

                float x0Weight = (x1 - xPos);
                float x1Weight = (xPos - x0);

                float lowerInterpolation  = x0Weight * imgRaster.getSample(x0, y0, 0) + x1Weight * imgRaster.getSample(x1, y0, 0);
                float higherInterpolation = x0Weight * imgRaster.getSample(x0, y1, 0) + x1Weight * imgRaster.getSample(x1, y1, 0);

                float y0Weight = (y1 - yPos);
                float y1Weight = (yPos - y0);

                float interpolatedValue = y0Weight * lowerInterpolation + y1Weight * higherInterpolation;

                newImgRaster.setSample(x, y, 0, interpolatedValue);
                newImgRaster.setSample(x, y, 1, interpolatedValue);
                newImgRaster.setSample(x, y, 2, interpolatedValue);
            }
        }

        CS450.setImageB(newImg);
    }

    // Part 2
    public void doShrink_Image() {
        BufferedImage img = CS450.openImage();
        WritableRaster imgRaster = img.getRaster();
        CS450.setImageA(img);
        int width = img.getWidth();
        int height = img.getHeight();

        int factor = Integer.parseInt(CS450.prompt("Enter the shrinking factor you'd like to use"));
        float factorFloat = (float)factor;
        int newWidth = img.getWidth() / factor;
        int newHeight = img.getHeight() / factor;

        BufferedImage newImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        WritableRaster newImgRaster = newImg.getRaster();

        for(int x = 0; x < newWidth; x++) {
            for(int y = 0; y < newHeight; y++) {

                float xPos = x * factorFloat;
                float yPos = y * factorFloat;

                float interpolatedValue = 0;

                float xDivisionValue = factorFloat;
                float yDivisionValue = factorFloat;
                if(x == 0) {
                    xDivisionValue /= factorFloat;
                }
                if(y == 0) {
                    yDivisionValue /= factorFloat;
                }

                // Average (interpolate equally) all the pixels that this new pixel now covers
                float divisionValue = xDivisionValue * yDivisionValue;
                for(int xP = 0; xP < xDivisionValue; xP++) {
                    for(int yP = 0; yP < yDivisionValue; yP++) {
                        interpolatedValue += imgRaster.getSample((int)xPos - xP, (int)yPos - yP, 0) / divisionValue;
                    }
                }

                newImgRaster.setSample(x, y, 0, interpolatedValue);
                newImgRaster.setSample(x, y, 1, interpolatedValue);
                newImgRaster.setSample(x, y, 2, interpolatedValue);
            }
        }

        CS450.setImageB(newImg);
    }
}
