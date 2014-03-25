import jigl.image.ComplexImage;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.utils.ImageConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class HW7 {

    public static void main(String[] args) {
//        doButterWorthLowPassFilter1D();
        do2DSpacialAveraging();
//        do2DSpacialAveraging2();
    }

    private static void doButterWorthLowPassFilter1D() {
        Fourier1D fourier1D = new Fourier1D(128);
        List<Double> data = OperationsManager.readDatFile("1D_Noise.dat");

        Complex2[] input = new Complex2[128];
        for (int i = 0; i < data.size(); i++) {
            input[i] = new Complex2(data.get(i), 0);
        }
        for (int i = data.size(); i < 128; i++) {
            input[i] = new Complex2(0, 0);
        }

        Complex2[] fftOutput = fourier1D.fft(input);

        Complex2[] newOutput = new Complex2[fftOutput.length];
        double cutoff = 16;
        for (int u = 0; u < 128; u++) {
            // Butterworth H(u) = 1 / 1 + (u^2 / Ucutoff^2) ^ n
            double result = 1 / (1 + Math.pow(((u * u) / (cutoff * cutoff)), 96));
            newOutput[u] = fftOutput[u].mult(result);
        }

        Complex2[] finalOutput = fourier1D.ifft(newOutput);

        String fileData = "";
        for (int i = 0; i < data.size(); i++) {
            fileData += finalOutput[i].real + "\n";
        }

        OperationsManager.writeFile("csv/1D_Filtering" + (int)cutoff +".dat", fileData);
    }

    private static void do2DSpacialAveraging() {
        try {
//            String inFileName = "img/2D_White_Box3.pgm";
            String fileName = "2D_White_Box3";
            String inFileName = "img/" + fileName + ".pgm";
            String outFileName = "img/output/" + fileName + "-Filtered.pgm";

            // Read in the file
            ImageInputStream infile = new ImageInputStream(inFileName);
            GrayImage im = (GrayImage) infile.read();

            // Do the forward FFT
            ComplexImage spectrum = jigl.image.utils.FFT.forward(im);
            spectrum.divide(im.X() * im.Y(), 0);

            // do the filtering here by multiplying the values in "spectrum" by the desired filter
            doButterWorth2D(spectrum);
//            doGaussianLowPass2D(spectrum);

            // Invert the FFT
            ComplexImage outComplex = jigl.image.utils.FFT.reverse(spectrum);

            // Get the real part and scale to grey
            RealGrayImage outRealPart = outComplex.real();
            outRealPart.byteSize();
            GrayImage outGreyScaleImage = ImageConverter.toGray(outRealPart);

            // Write the output file
            ImageOutputStream outfile = new ImageOutputStream(outFileName);
            outfile.write(outGreyScaleImage);
            outfile.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    // 1 / (1 + [D(u,v)/D0]^2n)
    // D(u,v) = sqrt(u^2 + v^2)
    private static void doButterWorth2D(ComplexImage frequencySpectrumImage)
    {
        double frequencyCutoff = 10; // D0 = cutoff
        double filterOrder = 2;      // n = filter order

        for (int u = 0; u < frequencySpectrumImage.X(); u++) {
            for (int v = 0; v < frequencySpectrumImage.Y(); v++) {
                // IMPORTANT - need to check frequencies from -Niquist to Niquist or else it does weird crap
                int U = u;
                int V = v;
                if(u > frequencySpectrumImage.X() / 2) {
                    U -= frequencySpectrumImage.X();
                }
                if(v > frequencySpectrumImage.Y() / 2) {
                    V -= frequencySpectrumImage.Y();
                }

                double D = Math.sqrt((U * U) + (V * V));
                double numDouble = 1 / (1 + Math.pow((D / frequencyCutoff), 2 * filterOrder));
                float value = (float)numDouble;
                frequencySpectrumImage.multiply(u, v, value, 1);

                // IDEAL FILTER
//                if(D >= frequencyCutoff)
//                {
//                    frequencySpectrumImage.setReal(u, v, 0);
//                    frequencySpectrumImage.setImag(u, v, 0);
//                }
            }
        }
    }

    // http://www.originlab.com/www/helponline/origin/en/UserGuide/Algorithm_(2D_FFT_Filters).html
    private static void doGaussianLowPass2D(ComplexImage frequencySpectrumImage)
    {
        double frequencyCutoff = 10; // D0 = cutoff

        for (int u = 0; u < frequencySpectrumImage.X(); u++) {
            for (int v = 0; v < frequencySpectrumImage.Y(); v++) {
                double rSquared = u * u + v * v; // r = sqrt(u^2 + v^2)
                float Hu = (float)Math.exp(-1 * rSquared / (2 * frequencyCutoff * frequencyCutoff));
                frequencySpectrumImage.multiply(u, v, Hu, 1);
            }
        }
    }

    /* *********************************************
     *               With Complex2[][]
     ********************************************* */
    private static void do2DSpacialAveraging2()
    {
        plot2DFunction(getFilteredImage("img/2D_White_Box3.pgm"), 128, 128);
    }

    private static Complex2[][] getFilteredImage(String filePath) {
        try {
            File file = new File(filePath);
            BufferedImage input =  ImageIO.read(file);
            if (input == null) {
                return null;
            }

            WritableRaster in = input.getRaster();

            int M = input.getWidth();
            int N = input.getHeight();

            Fourier2D ft2D = new Fourier2D(M, N);

            Complex2[][] img = new Complex2[M][N];
            for(int y = 0; y < N; y++) {
                for(int x = 0; x < M; x++) {
                    img[x][y] = new Complex2(in.getSample(x, y, 0), 0);
                }
            }

            Complex2[][] fft = ft2D.fft(img);
            return ft2D.ifft(doButterWorth2D(fft));
//            return ft2D.ifft(doGaussianLowPass2D2(fft));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // Used for plotting simple sin functions
    private static void plot2DFunction(Complex2[][] imageData, int width, int height) {
        BufferedImage functionPlot = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster out = functionPlot.getRaster();

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double f_XY = imageData[x][y].real;

                // Set red, green, and blue parts of image
                out.setSample(x, y, 0, f_XY);
                out.setSample(x, y, 1, f_XY);
                out.setSample(x, y, 2, f_XY);
            }
        }

        CS450.saveImage(functionPlot);
    }

    // 1 / (1 + [D(u,v)/D0]^2n)
    // D(u,v) = sqrt(u^2 + v^2)
    private static Complex2[][] doButterWorth2D(Complex2[][] frequencySpectrumImage)
    {
        double frequencyCutoff = 10; // D0 = cutoff
        double filterOrder = 1; // n = filter order

        for (int u = 0; u < 128; u++) {
            for (int v = 0; v < 128; v++) {
                int U = u;
                int V = v;
                if(u >= 64) {
                    U -= 128;
                }
                if(v >= 64) {
                    V -= 128;
                }
                double D = Math.sqrt(U * U + V * V);
                double numDouble = 1 + Math.pow((D / frequencyCutoff), 2 * filterOrder);
                numDouble = 1 / numDouble;

                frequencySpectrumImage[u][v].mult(numDouble);
            }
        }

        return frequencySpectrumImage;
    }

    // http://www.originlab.com/www/helponline/origin/en/UserGuide/Algorithm_(2D_FFT_Filters).html
    private static Complex2[][] doGaussianLowPass2D2(Complex2[][] frequencySpectrumImage)
    {
        double frequencyCutoff = 10; // D0 = cutoff

        for (int u = 0; u < 128; u++) {
            for (int v = 0; v < 128; v++) {
                double rSquared = u * u + v * v; // r = sqrt(u^2 + v^2)
                double Hu = Math.exp(-1 * rSquared / (2 * frequencyCutoff * frequencyCutoff));
                frequencySpectrumImage[u][v].mult(Hu);
            }
        }

        return frequencySpectrumImage;
    }
}
