/**
 * MATLAB:
 * http://matlabgeeks.com/tips-tutorials/how-to-do-a-2-d-fourier-transform-in-matlab/
 */

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class HW6 {
    private final int SIMPLE_SIN2D_IN_X = 1;
    private final int SIMPLE_SIN2D_IN_Y = 2;
    private final int SIMPLE_PLUS2D = 3;
    private final int SIMPLE_MULT2D = 4;
    private final int FROM_IMG = 5;
    private int s, s2;
    private int M; // X -----> U
    private int N; // Y -----> V
    private int type;
    private BufferedImage input;
    private WritableRaster in;
    private Fourier2D ft2D;

    /* **************************************
     *          GUI FUNCTIONS
     * **************************************/

    public void doSaveImgB() {
        BufferedImage img = CS450.getImageB();

        CS450.saveImage(img);
    }

    // f[x,y] = sin(2 pi s x / M) - Each row is the same
    public void doFuncFourier() {
        setTypeAndS();
        N = 256;
        M = 256;

        ft2D = new Fourier2D(M, N);

        Complex2[][] sine = new Complex2[M][N];
        for(int y = 0; y < N; y++) {
            for(int x = 0; x < M; x++) {
                sine[x][y] = new Complex2(functionF(x, y), 0);
            }
        }

        Complex2[][] ftData = ft2D.fft(sine);

        plotFtMagnitude(ftData, M, N);
    }

    public void doMakeImage() {
        setTypeAndS();
        N = 256;
        M = 256;

        plot2DFunction(M, N);
    }

    // Used in scaling values of simple sin functions
    private double scaleValue(double input) {
        if(type == SIMPLE_SIN2D_IN_X || type == SIMPLE_SIN2D_IN_Y || type == SIMPLE_MULT2D) {
            // SINE = -1 to 1
            // SCALE to 0 to 255
            // (SINE + 1) * 128
            return (input + 1) * (255.0 / 2.0);
        }
        if(type == SIMPLE_PLUS2D)
        {
            return (input + 2) * (255.0 / 4.0);
        }
        else
            return 0;
    }

    private void setTypeAndS() {
        String input = CS450.prompt("Pick Direction of Sine Function:", new String[]{"X","Y", "X + Y", "X * Y"});
        if(input.equals("X"))
            type = SIMPLE_SIN2D_IN_X;
        if(input.equals("Y"))
            type = SIMPLE_SIN2D_IN_Y;
        if(input.equals("X + Y"))
            type = SIMPLE_PLUS2D;
        if(input.equals("X * Y"))
            type = SIMPLE_MULT2D;

        s = Integer.parseInt(CS450.prompt("Enter a value for S"));
        if(type == SIMPLE_PLUS2D || type == SIMPLE_MULT2D)
            s2 = Integer.parseInt(CS450.prompt("Enter a value for S2"));
    }

    // Used for plotting simple sin functions
    private void plot2DFunction(int width, int height) {
        BufferedImage functionPlot = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster out = functionPlot.getRaster();

        for(int x = 0; x < M; x++) {
            for(int y = 0; y < N; y++) {

                double f_XY = scaleValue(functionF(x, y));

                // Set red, green, and blue parts of image
                out.setSample(x, y, 0, f_XY);
                out.setSample(x, y, 1, f_XY);
                out.setSample(x, y, 2, f_XY);
            }
        }

        CS450.setImageB(functionPlot);
    }

    public void doImgFourier() {

        plotFtMagnitude(getImageFourier(), M, N);
    }

    public void doImgFourierSwap() {
        Complex2[][] ftData_1 = getImageFourier(); // Ball
        Complex2[][] ftData_2 = getImageFourier(); // Gull

        Complex2[][] ftData_1WithPhase2 = imgWithOtherMagnitude(ftData_1, ftData_2); // Ball with Gull Phase
        Complex2[][] ftData_2WithPhase1 = imgWithOtherMagnitude(ftData_2, ftData_1); // Gull with Ball Phase

        Complex2[][] ifft1 = ft2D.ifft(ftData_1WithPhase2); // Ball with Gull Phase
        Complex2[][] ifft2 = ft2D.ifft(ftData_2WithPhase1); // Gull with Ball Phase

        BufferedImage outputA = new BufferedImage(M, N, BufferedImage.TYPE_INT_RGB);
        WritableRaster outA = outputA.getRaster();

        BufferedImage outputB = new BufferedImage(M, N, BufferedImage.TYPE_INT_RGB);
        WritableRaster outB = outputB.getRaster();

        for(int x=0; x<M; x++) {
            for(int y=0; y<N; y++) {
                double valueA = ifft1[x][y].real;
                double valueB = ifft2[x][y].real;

                outA.setSample(x, y, 0, valueA);
                outA.setSample(x, y, 1, valueA);
                outA.setSample(x, y, 2, valueA);

                outB.setSample(x, y, 0, valueB);
                outB.setSample(x, y, 1, valueB);
                outB.setSample(x, y, 2, valueB);
            }
        }

        CS450.setImageA(outputA); // Ball with Gull Phase
        CS450.setImageB(outputB); // Gull with Ball Phase
    }

    public void doImageFourierBackAndForth()
    {
        Complex2[][] ftData_1 = getImageFourier();
        Complex2[][] ifft1 = ft2D.ifft(ftData_1);

        BufferedImage outputB = new BufferedImage(M, N, BufferedImage.TYPE_INT_RGB);
        WritableRaster outB = outputB.getRaster();

        for(int x=0; x<M; x++) {
            for(int y=0; y<N; y++) {
                double valueB = ifft1[x][y].real;

                outB.setSample(x, y, 0, valueB);
                outB.setSample(x, y, 1, valueB);
                outB.setSample(x, y, 2, valueB);
            }
        }

        CS450.setImageB(outputB);
    }

    private Complex2[][] getImageFourier() {
        type = FROM_IMG;

        input = CS450.openImage();
        if (input == null) {
            return null;
        }

        CS450.setImageA(input);
        in = input.getRaster();

        M = input.getWidth();
        N = input.getHeight();

        ft2D = new Fourier2D(M, N);

        Complex2[][] img = new Complex2[M][N];
        for(int y = 0; y < N; y++) {
            for(int x = 0; x < M; x++) {
                img[x][y] = new Complex2(functionF(x, y), 0);
            }
        }

        return ft2D.fft(img);
    }

    // Return img_1 having paired it's Magnitude with img_2's phase
    private Complex2[][] imgWithOtherMagnitude(Complex2[][] img_1, Complex2[][] img_2) {
        Complex2[][] result = new Complex2[M][N];

        for(int x = 0; x < M; x++) {
            for (int y = 0; y < N; y++) {
                double img_1Magnitude = img_1[x][y].Betrag();
                double img_2Phase = img_2[x][y].Phase();

                double newReal = img_1Magnitude * Math.cos(img_2Phase);
                double newImag = img_1Magnitude * Math.sin(img_2Phase);

                result[x][y] = new Complex2(newReal, newImag);
            }
        }

        return result;
    }

    // Used for plotting Fourier Transform Data as an image
    private void plotFtMagnitude(Complex2[][] data, int width, int height) {
        BufferedImage magnitudeImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        WritableRaster out = magnitudeImg.getRaster();

        double maxMagnitude = 0;

        for (int v = 0; v < height; v++) {
            for (int u = 0; u < width; u++) {
                Complex2 value = data[u][v];
                double magnitude = value.Betrag();

                if(magnitude > maxMagnitude) {
                    maxMagnitude = magnitude;
                }
            }
        }

        for (int v = 0; v < height; v++) {
            for (int u = 0; u < width; u++) {
                int shiftedU = u;
                int shiftedV = v;

                // Get points centered around the middle of the image
                if(shiftedU >= (M / 2))
                    shiftedU -= M;
                shiftedU += (M / 2);

                if(shiftedV >= (N / 2))
                    shiftedV -= N;
                shiftedV += (N / 2);

                Complex2 value = data[u][v];

                // Scale values
                double magnitude = (value.Betrag() / maxMagnitude) * 255;

                out.setSample(shiftedU, shiftedV, 0, magnitude);
                out.setSample(shiftedU, shiftedV, 1, magnitude);
                out.setSample(shiftedU, shiftedV, 2, magnitude);
            }
        }

        CS450.setImageB(magnitudeImg);
    }

    // Functions in the time domain
    private double functionF(int x, int y) {
        if (type == SIMPLE_SIN2D_IN_X) { return Math.sin(2 * Math.PI * s * x / M); }
        if (type == SIMPLE_SIN2D_IN_Y) { return Math.sin(2 * Math.PI * s * y / M); }
        if (type == SIMPLE_PLUS2D) { return Math.sin(2 * Math.PI * s * x / M) + Math.sin(2 * Math.PI * s2 * y / M); }
        if (type == SIMPLE_MULT2D) { return Math.sin(2 * Math.PI * s * x / M) * Math.sin(2 * Math.PI * s2 * y / M); }
        if (type == FROM_IMG) { return in.getSample(x, y, 0); }
        else { return 0; }
    }
}
