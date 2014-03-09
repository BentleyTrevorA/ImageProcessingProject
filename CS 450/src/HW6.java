import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.List;

public class HW6 {
    private final int SIMPLE_SIN2D = 1;
    private final int SIMPLE_COS2D = 2;
    private final int SIMPLE_BOTH2D = 3;
    private final int GAUSSIAN = 4;
    private final int RECTANGULAR = 5;
    private final int DATA_FILE = 6;
    private final int TRANSFER = 7;
    private int s;
    private int M; // X -----> U
    private int N; // Y -----> V
    private int type;
    private List<Double> data = null;

    /* **************************************
     *          GUI FUNCTIONS
     * **************************************/

    public void doSaveImgB() {
        BufferedImage img = CS450.getImageB();

        CS450.saveImage(img);
    }

    // f[x,y] = sin(2 pi s x / M) - Each row is the same
    public void doSimpleSine2D() {
        type = SIMPLE_SIN2D;
        s = 25;
        N = 256;
        M = 256;

        Fourier2D ft2D = new Fourier2D(M, N);

        Complex2[][] sine = new Complex2[M][N];
        for(int y = 0; y < N; y++) {
            for(int x = 0; x < M; x++) {
                sine[x][y] = new Complex2(functionF(x, y), 0);
            }
        }

        Complex2[][] ftData = ft2D.fft(sine);

        // TODO: This doesn't quite work right
//        Complex[][] ftData = new Complex[M][N];

//        Complex[] input = new Complex[M];
//        for(int y = 0; y < N; y++) {
//            for(int x = 0; x < M; x++) {
//                input[x] = new Complex(functionF(x, y), 0);
//            }
//            Complex[] output = FFT.fft(FFT.fft(input));
//
//            for(int i = 0; i < output.length; i++) {
//                ftData[i][y] = output[i].times(1.0 / (M * N));
//            }
//        }

        plotFtAsImage(ftData, M, N);
    }

    public void doPlotSimpleSine2D() {
        type = SIMPLE_SIN2D;
        s = 25;
        N = 256;
        M = 256;

        plot2DFunction(M, N);
    }

    public void doGreyScale() {

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


                // Set red, green, and blue parts of image
                out.setSample(x, y, 0, value);
                out.setSample(x, y, 1, value);
                out.setSample(x, y, 2, value);
            }
        }

        CS450.setImageB(outputImage);
    }

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

    private void plotFtAsImage(Complex2[][] data, int width, int height) {
        BufferedImage magnitudeImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        WritableRaster out = magnitudeImg.getRaster();

        double max = 0;

        for (int v = 0; v < height; v++) {
            for (int u = 0; u < width; u++) {
                Complex2 value = data[u][v];
                double magnitude = value.Betrag();

                if(magnitude > max) {
                    max = magnitude;
                    System.out.println(u + ", " + v);
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

                double magnitude = (value.Betrag() / max) * 255;

                System.out.println(magnitude);
                // TODO: Scale values
                out.setSample(shiftedU, shiftedV, 0, magnitude);
                out.setSample(shiftedU, shiftedV, 1, magnitude);
                out.setSample(shiftedU, shiftedV, 2, magnitude);
            }
        }

        CS450.setImageB(magnitudeImg);
    }

    private double scaleValue(double input) {
        if(type == SIMPLE_SIN2D) {
            // SINE = -1 to 1
            // SCALE to 0 to 255
            // (SINE + 1) * 128
            return (input + 1) * 127.5;
        }
        else
            return 0;
    }

    private MyComplex[][] dFT2D() {
        MyComplex[][] result = new MyComplex[M][N];
        for(int u = 0; u < M; u++) {
            for(int v = 0; v < N; v++) {

                double twoPiUOverM = 2 * Math.PI * u / M;
                double twoPiVOverN = 2 * Math.PI * v / N;

                MyComplex outerComplex = new MyComplex(0,0);

                for(int y = 0; y < N; y++) {

                    MyComplex complex1 = new MyComplex(0,0);

                    //SUM x from 0 to M-1 of ---- f(x,y) * e^(-i * 2 * pi * u * x / M)
                    for(int x = 0; x < M; x++)
                    {
                        double f_XY = functionF(x, y);
                        double insidePiece = x * twoPiUOverM;
                        complex1.plus(new MyComplex(
                                        f_XY * Math.cos(insidePiece),
                                        -1 * f_XY * Math.sin(insidePiece)));
                    }

                    // e^(-i * 2 * pi * v * y / M)
                    double insidePiece = y * twoPiVOverN;
                    MyComplex complex2 = new MyComplex(Math.cos(insidePiece), -1  * Math.sin(insidePiece));

                    // f(x,y) * e^(-i * 2 * pi * u * x / M) * e^(-i * 2 * pi * v * y / M)
                    complex1.times(complex2);

                    // SUM y from 0 to N-1
                    outerComplex.plus(complex1);
                }

                result[u][v] = new MyComplex(outerComplex.re() / (M * N), outerComplex.im() / (M * N));
            }
        }

        return result;
    }


    // Inverse Discrete Fourier Transform
    private MyComplex[][] iDFT2D() {
        MyComplex[][] result = new MyComplex[M][N];
        for(int u=0; u<M; u++) {
            for(int v=0; v<N; v++) {

                MyComplex outerComplex = new MyComplex(0,0);

                for(int y=0; y<N; y++) {

                    MyComplex complex1 = new MyComplex(0,0);
                    for(int x=0; x<M; x++)
                    {
                        double f_XY = functionF(x, 0);
                        //SUM x from 0 to M-1 of ---- f(x,y) * e^(i * 2 * pi * u * x / M)
                        complex1.plus(new MyComplex(
                                        f_XY * Math.cos(2 * Math.PI * u * x / M),
                                        f_XY * Math.sin(2 * Math.PI * u * x / M)));
                    }

                    // e^(i * 2 * pi * v * y / M)
                    MyComplex complex2 = new MyComplex(
                            Math.cos(2 * Math.PI * v * y / N),
                            Math.sin(2 * Math.PI * v * y / N));

                    // f(x,y) * e^(i * 2 * pi * u * x / M) * e^(i * 2 * pi * v * y / M)
                    complex1.times(complex2);

                    // SUM y from 0 to N-1
                    outerComplex.plus(complex1);
                }

                result[u][v] = outerComplex;
            }
        }

        return result;
    }

    // Functions in the time domain
    private double functionF(int x, int y) {
        if (type == SIMPLE_SIN2D) { return Math.sin(2 * Math.PI * s * x / M); }
        if (type == SIMPLE_COS2D) { return Math.cos(2 * Math.PI * s * x / M); }
        if (type == SIMPLE_BOTH2D) { return Math.sin(2 * Math.PI * s * x / M) + Math.cos(2 * Math.PI * s * x / M); }
        if (type == GAUSSIAN || type == RECTANGULAR || type == DATA_FILE || type == TRANSFER) { return data.get(x); }
        else { return 0; }
    }
}
