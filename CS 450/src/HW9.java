import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.io.*;

public class HW9 {

    /* The first speech waveform is v1 - in .wav format and raw format
    (Here is the 8 bit version Who_will_enter8bit.raw).
    Listen to the .wav file using any audio player and see if you can recognize the person behind the voice.
    Who do you think it is? */
    public static void main(String[] args) {
        String audioFile1 = "Who_will_enter8bit"; // 16000  - n = 2
        String audioFile2 = "helpme";             // 6000   - n = 2
        String audioFile3 = "v3";                 // 11000  - n = 2

        double cutoff = 24000;
        double n = 2;

        makeFFTChartMagnitudes(audioFile1);
        makeFFTChartPowerSpectrum(audioFile1);

        makeFFTChartMagnitudes(audioFile2);
        makeFFTChartPowerSpectrum(audioFile2);

        while(cutoff > 2000)
        {
            butterWorthBytes(audioFile1, cutoff, n);
            butterWorthBytes(audioFile2, cutoff, n);
            cutoff -= 1000;
        }
        butterWorthBytes(audioFile1, 12000, 3);
        butterWorthBytes(audioFile2, 6000, 2);
        butterWorthBytes(audioFile3, 8000, 2);

        computeSamplingRate(audioFile1, 3);
        computeSamplingRate(audioFile2, 5);
        computeSamplingRate(audioFile3, 5);
    }

    private static void computeSamplingRate(String audioFile, double seconds)
    {
        byte[] data = readByteArray(audioFile);
        System.out.println(data.length / seconds);
    }

    private static void makeChart(String audioFile) {
        Plotter plotter = new Plotter("f(t) of " + audioFile);
        plotter.makeChart(readByteArray(audioFile));
        plotter.pack();
        RefineryUtilities.centerFrameOnScreen(plotter);
        plotter.setVisible(true);
        plotter.setSize(new Dimension(1000, 600));
    }

    private static void makeFFTChartMagnitudes(String audioFile) {
        Complex2[] data = getComplexSamples(readByteArray(audioFile));
        Fourier1D fourier1D = new Fourier1D(data.length);

        Complex2[] fftData = fourier1D.fft(data);
        double[] magnitudes = new double[fftData.length];
        for(int i=0; i<fftData.length; i++) {
            magnitudes[i] = fftData[i].Betrag();
        }

        Plotter plotter = new Plotter("Magnitudes of F(u) for " + audioFile);
        plotter.makeChart(magnitudes);
        plotter.pack();
        RefineryUtilities.centerFrameOnScreen(plotter);
        plotter.setVisible(true);
        plotter.setSize(new Dimension(1000, 600));
    }

    private static void makeFFTChartPowerSpectrum(String audioFile) {
        Complex2[] data = getComplexSamples(readByteArray(audioFile));
        Fourier1D fourier1D = new Fourier1D(data.length);

        Complex2[] fftData = fourier1D.fft(data);
        double[] powerSpectrum = new double[fftData.length];
        for(int i=0; i<fftData.length; i++) {
            double mag = fftData[i].Betrag();
            powerSpectrum[i] = mag * mag;
        }

        Plotter plotter = new Plotter("Power specturm of F(u) for " + audioFile);
        plotter.makeChart(powerSpectrum);
        plotter.pack();
        RefineryUtilities.centerFrameOnScreen(plotter);
        plotter.setVisible(true);
        plotter.setSize(new Dimension(1000, 600));
    }

    private static void makeFilteredPowerSpectrumChart(Complex2[] fftData, String audioFile){
        double[] powerSpectrum = new double[fftData.length];
        for(int i=0; i<fftData.length; i++) {
            double mag = fftData[i].Betrag();
            powerSpectrum[i] = mag * mag;
        }

        Plotter plotter = new Plotter("Power specturm of Filtered F(u) for " + audioFile);
        plotter.makeChart(powerSpectrum);
        plotter.pack();
        RefineryUtilities.centerFrameOnScreen(plotter);
        plotter.setVisible(true);
        plotter.setSize(new Dimension(1000, 600));
    }

    private static void makeButterWorthChart(double[] data, String audioFile) {
        Plotter plotter = new Plotter("ButterWorth " + audioFile);
        plotter.makeChart(data);
        plotter.pack();
        RefineryUtilities.centerFrameOnScreen(plotter);
        plotter.setVisible(true);
        plotter.setSize(new Dimension(1000, 600));
    }

    private static void butterWorthBytes(String audioFile, double cutoff, double n) {
        try {
            double cutoffSquared = cutoff * cutoff;
            byte[] bytes = readByteArray(audioFile);
            int numSamples = bytes.length;

            Complex2[] complexSamples = getComplexSamples(bytes);

            Fourier1D fourier1D = new Fourier1D(complexSamples.length);
            Complex2[] fft = fourier1D.fft(complexSamples);

            Complex2[] filteredOutput = new Complex2[fft.length];
            double[] butterWorth = new double[fft.length / 2];

            int halfSamples = fft.length / 2;
            for (int u = 0; u <= halfSamples; u++) {
                // Butterworth H(u) = 1 / 1 + (u^2 / Ucutoff^2) ^ n
                double result = 1 / (1 + Math.pow(((u * u) / cutoffSquared), n));

                if(u != halfSamples)
                    butterWorth[u] = result;

                Complex2 temp = fft[u];
                double real = temp.real * result;
                double imag = temp.imag * result;
                filteredOutput[u] = new Complex2(real, imag);
                if(u > 0)
                    filteredOutput[fft.length - u] = new Complex2(real, -imag);
            }

//            makeButterWorthChart(butterWorth, audioFile);
//            makeFilteredPowerSpectrumChart(filteredOutput, audioFile);

            Complex2[] finalOutput = fourier1D.ifft(filteredOutput);

            byte[] newFileBytes = new byte[numSamples];
            for (int i = 0; i < numSamples; i++) {
                double roundedValue = Math.round(finalOutput[i].real * 10.0) / 10.0;
                newFileBytes[i] = (byte)roundedValue;
            }

            File outFile = new File("audio/out/" + audioFile + "_" + n + "_filtered_" + cutoff + ".raw");
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outFile));
            outputStream.write(newFileBytes);
            outputStream.flush();
            outputStream.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static Complex2[] getComplexSamples(byte[] samples) {
        // Determine 2^i to use for N
        double powOfTwo = 0;
        for (int i = 0; powOfTwo < samples.length; i++) {
            powOfTwo = Math.pow(2, i);
        }

        int N = (int) powOfTwo;

        // Construct Complex Array
        Complex2[] data = new Complex2[N];
        for (int i = 0; i < N; i++) {
            if (i < samples.length)
                data[i] = new Complex2(samples[i], 0);
            else
                data[i] = new Complex2(0, 0);
        }

        return data;
    }

    private static byte[] readByteArray(String audioFile) {
        try {
            InputStream is = new FileInputStream(new File("audio/" + audioFile + ".raw"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[10240];
            int i = Integer.MAX_VALUE;
            while ((i = is.read(buff, 0, buff.length)) > 0) {
                baos.write(buff, 0, i);
            }

            return baos.toByteArray(); // be sure to close InputStream in calling function
        }
        catch(IOException e) {
            return null;
        }
    }
}