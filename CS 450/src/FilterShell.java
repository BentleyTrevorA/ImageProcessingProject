import jigl.image.*;
import jigl.image.io.*;
import jigl.image.utils.*;

public class FilterShell {
    public static void main(String args[])
    {
        try {
            String inFileName = args[0];
            String outFileName = args[1];

            // Read in the file
            ImageInputStream infile = new ImageInputStream(inFileName);
            GrayImage im = (GrayImage)infile.read();

            // Do the forward FFT
            ComplexImage spectrum = jigl.image.utils.FFT.forward(im);
            spectrum.divide(im.X()*im.Y(),0);

            // do the filtering here by multiplying the values in "spectrum"
            // by the desired filter

            // Invert the FFT
            ComplexImage outc = jigl.image.utils.FFT.reverse(spectrum);

            // Get the real part and scale to grey
            RealGrayImage outr = outc.real();
            outr.byteSize();
            GrayImage outg = ImageConverter.toGray(outr);

            // Write the output file
            ImageOutputStream outfile = new ImageOutputStream(outFileName);
            outfile.write(outg);
            outfile.close();
        }

        catch (Exception e) {
            System.out.print(e);
        }
    }
}

