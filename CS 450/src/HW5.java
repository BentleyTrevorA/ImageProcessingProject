import java.util.*;

public class HW5
{
    private final int SIMPLE_SIN = 1;
    private final int SIMPLE_COS = 2;
    private final int SIMPLE_BOTH = 3;
    private final int GAUSSIAN = 4;
    private final int RECTANGULAR = 5;
    private final int DATA_FILE = 6;
    private final int TRANSFER = 7;
    private int s;
    private int M;
    private int type;
    private List<Double> data = null;

    class FrequencyResult implements Comparable<FrequencyResult>{
        int frequency;
        double value;

        public FrequencyResult(int frequency, double value) {
            this.frequency = frequency;
            this.value = (double)Math.round(value * 1000000) / 1000000;
        }

        public int compareTo(FrequencyResult a) {
            double diff = (a.value - this.value);

            if(diff > 0)
                return 1;
            if(diff < 0)
                return -1;
            else
                return this.frequency - a.frequency;
        }
    }

    /* *****************************************t
    *               DISCRETE FOURIER
    *  ****************************************/
    public void doSimpleSinGraph() {
        s = 8;
        M = 128;
        type = SIMPLE_SIN;

        String output = "";
        for (int x = 0; x < M; x++) {
            output += x + "," + functionF(x) + "\n";
        }

        OperationsManager.writeFile("SimpleSin.csv", output);

        plot(discreteFourierTransform());
    }

    public void doSimpleCosGraph() {
        s = 8;
        M = 128;
        type = SIMPLE_COS;

        String output = "";
        for (int x = 0; x < M; x++) {
            output += x + "," + functionF(x) + "\n";
        }

        OperationsManager.writeFile("SimpleCos.csv", output);

        plot(discreteFourierTransform());
    }

    public void doSimpleBothGraph() {
        s = 8;
        M = 128;
        type = SIMPLE_BOTH;

        String output = "";
        for (int x = 0; x < M; x++) {
            output += x + "," + functionF(x) + "\n";
        }

        OperationsManager.writeFile("SimpleBoth.csv", output);

        plot(discreteFourierTransform());
    }

    private void plot(FourierTransformResult fourier) {
        // Real Part
        String output = "U, real, imaginary, magnitude, phase\n";
        for (int u = 0; u < M; u++) {
            double real = fourier.getReal(u);
            double imaginary = fourier.getImaginary(u);
            double magnitude = fourier.getMagnitude(u);
            double phase = fourier.getPhase(u);
            output += u + "," + real + "," + imaginary + "," + magnitude + "," + phase + "\n";
        }
        writeFourierCSV(output);
    }

    private void writeFourierCSV(String contents) {
        String fileName = "";

        if (type == SIMPLE_SIN) {
            fileName += "SimpleSin";
        }
        if (type == SIMPLE_COS) {
            fileName += "SimpleCos";
        }
        if (type == SIMPLE_BOTH) {
            fileName += "SimpleBoth";
        }
        if (type == GAUSSIAN) {
            fileName += "Gaussian";
        }
        if (type == RECTANGULAR) {
            fileName += "Rectangular";
        }
        if (type == DATA_FILE) {
            fileName += "FrequencyAnalysis";
        }
        if (type == TRANSFER) {
            fileName += "TransferFunction";
        }

        fileName += ".csv";

        OperationsManager.writeFile(fileName, contents);
    }

    private FourierTransformResult discreteFourierTransform() {
        FourierTransformResult result = new FourierTransformResult();

        for (int u = 0; u < M; u++) {
            double real = 0;
            double imaginary = 0;

            for (int x = 0; x < M; x++) {
                double fX = functionF(x);
                real += fX * Math.cos(2 * Math.PI * u * x / M);
                imaginary -= fX * Math.sin(2 * Math.PI * u * x / M);
            }

            real /= M;
            imaginary /= M;

            result.addReal(real);
            result.addImaginary(imaginary);
        }

        return result;
    }

    private FourierTransformResult discreteFourierTransformOfDistribution() {
        FourierTransformResult result = new FourierTransformResult();

        for (int u = 0 - (M/2); u < (M / 2); u++) {
            double real = 0;
            double imaginary = 0;

            for (int x = 0; x < M; x++) {
                double fX = functionF(x);
                real += fX * Math.cos(2 * Math.PI * u * x / M);
                imaginary -= fX * Math.sin(2 * Math.PI * u * x / M);
            }

            real /= M;
            imaginary /= M;

            result.addReal(real);
            result.addImaginary(imaginary);
        }

        return result;
    }

    private double functionF(int t) {
        if (type == SIMPLE_SIN) {
            return Math.sin(2 * Math.PI * s * t / M);
        }
        if (type == SIMPLE_COS) {
            return Math.cos(2 * Math.PI * s * t / M);
        }
        if (type == SIMPLE_BOTH) {
            return Math.sin(2 * Math.PI * s * t / M) + Math.cos(2 * Math.PI * s * t / M);
        }
        if (type == GAUSSIAN || type == RECTANGULAR || type == DATA_FILE || type == TRANSFER) {
            return data.get(t);
        }
        else
            return 0;
    }

    /* *****************************************
    *               RECT FOURIER
    *  ****************************************/
    public void doRectFourier() {
        type = RECTANGULAR;
        data = OperationsManager.readDatFile("1D_Rect128.dat");
        s = 1;
        M = data.size();

        // Sinc function way
//        double a = 2;
//        String output = "U, FourierTransform, Magnitude, Power Spectrum\n";
//        for (int i = 0; i < data.size(); i++) {
//            if (data.get(i) != 0) {
//                double sincData;
//                double omega = i - (data.size() / 2);
//                if (omega == 0)
//                    sincData = 1;
//                else {
//                    sincData = Math.sin(a * omega);
//                    sincData /= (a * omega);
//                }
//                output += omega + "," + sincData + "," + Math.abs(sincData) + "," + (sincData * sincData) + "\n";
//            }
//        }
//
//        OperationsManager.writeFile("RectFourier.csv", output);

        plot(discreteFourierTransformOfDistribution());
    }

    /* *****************************************
    *               GAUSSIAN FOURIER
    *  ****************************************/
    public void doGaussianFourier() {
        type = GAUSSIAN;
        data = OperationsManager.readDatFile("1D_Gaussian.dat");
        s = 1;
        M = data.size();

        double fishie = 2; // TODO: How to determine what fishie is?

        String output = "U, Gaussian Value, Fourier Value\n";

        // Attempt at a different way
//        for (int i = 0; i < data.size(); i++) {
//            double omega = i - (data.size() / 2);
//            double result = (2 * fishie) / (Math.pow(fishie, 2) + Math.pow((2 * Math.PI * omega), 2));
//            output += i + "," + data.get(i) + "," + result + "\n";
//        }

//        OperationsManager.writeFile("GaussianFourier.csv", output);

        plot(discreteFourierTransformOfDistribution());
    }

    public void doFrequencyAnalysis() {
        type = DATA_FILE;
        data = OperationsManager.readDatFile("1D_Signal.dat");
        s = 1;
        M = data.size();

        // Assume correct input
        int n = Integer.parseInt(CS450.prompt("Enter a value for n (max value = " + (M/2) + ")"));

        FourierTransformResult fourier = discreteFourierTransform();

        List<FrequencyResult> magnitudes = new ArrayList<FrequencyResult>();

        String output = "U, real, imaginary, magnitude, phase\n";
        for (int u = 0; u < M; u++) {
            double real = fourier.getReal(u);
            double imaginary = fourier.getImaginary(u);

            double magnitude = fourier.getMagnitude(u);
            double phase = fourier.getPhase(u);
            output += u + "," + real + "," + imaginary + "," + magnitude + "," + phase + "\n";

            magnitudes.add(new FrequencyResult(u, magnitude));
        }

        Collections.sort(magnitudes);

        int numPrinted = 0;
        double lastValue = 0;
        for(int i = 1; numPrinted < n && i < magnitudes.size(); i++)
        {
            FrequencyResult result = magnitudes.get(i);
            if(lastValue != result.value)
            {
                System.out.println("FREQUENCY: " + result.frequency);
                System.out.println("VALUE: " + result.value);
                numPrinted++;
            }
            lastValue = result.value;
        }
    }

    // http://en.wikipedia.org/wiki/Transfer_function
    // Refer to img found from slides
    public void doTransferFunction() {
        // g(t) = f(t) * h(t) <==> F(u)H(u) = G(u)
        // F(u)H(u) = G(u)
        // H(u) = G(u)/F(u)

        type = TRANSFER;
        data = OperationsManager.readDatFile("1D_Rect128.dat");
        List<Double> outputData = OperationsManager.readDatFile("1D_Output128.dat");
        List<Double> transferFunction =  new ArrayList<Double>();
        s = 1;
        M = data.size();

        // TODO: Fourier transform of g(t) (output)
        // TODO: Fouriert transform of f(t) (input)
        // TODO: H(u) = G(u) (real + imaginary) / F(u) (real: it has no imaginary part)
        FourierTransformResult result = discreteFourierTransformOfDistribution();
    }
}
