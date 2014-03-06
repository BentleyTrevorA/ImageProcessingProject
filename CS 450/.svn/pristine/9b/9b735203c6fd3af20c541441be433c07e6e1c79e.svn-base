import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class HWBase
{
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
}