import Audio.AudioSampleReader;
import Audio.AudioSampleWriter;
import com.musicg.graphic.GraphicRender;
import com.musicg.wave.Wave;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

public class HW9 {

    /* The first speech waveform is v1 - in .wav format and raw format
    (Here is the 8 bit version Who_will_enter8bit.raw).
    Listen to the .wav file using any audio player and see if you can recognize the person behind the voice.
    Who do you think it is? */
    public static void main(String[] args) {
//        String audioFile = "Who_will_enter";
        String audioFile = "helpme";
//        reportHighestFrequencyWithNonZeroMagnitude(audioFile);
//        reportHighestNonZero(audioFile);
        butterWorthLowPassFilter(audioFile);
    }

    private static int reportHighestFrequencyWithNonZeroMagnitude(String audioFile) {
        GraphicRender graphicRender = new GraphicRender();


        Wave wave = new Wave("audio/" + audioFile + ".wav");
        short[] amplitudes = wave.getSampleAmplitudes();

        // Determine 2^i to use for N
        double powOfTwo = 0;
        for (int i = 0; powOfTwo < amplitudes.length; i++) {
            powOfTwo = Math.pow(2, i);
        }

        int N = (int) powOfTwo;

        // Construct Complex Array
        Complex2[] data = new Complex2[N];
        for (int i = 0; i < powOfTwo; i++) {
            if (i < amplitudes.length)
                data[i] = new Complex2(amplitudes[i], 0);
            else
                data[i] = new Complex2(0, 0);
        }

        // FFT
        Fourier1D fft = new Fourier1D(N);
        Complex2[] fftData = fft.fft(data);

        // Find highest frequency with non-zero magnitude
        int highestFreq = 0;
        double magnitudeOfHighestFreq = 0;
        for (int freq = 0; freq < amplitudes.length / 2; freq++) {
            double mag = fftData[freq].Betrag();
            mag *= mag;
            if (mag > 0.1) {
                highestFreq = freq;
                magnitudeOfHighestFreq = mag;
            }
        }

        System.out.println(amplitudes.length / 2);
        System.out.println(highestFreq);
        System.out.println(magnitudeOfHighestFreq);
        System.out.println(amplitudes.length / 3);
//        System.out.println(wave);
//        render.renderWaveform(wave, .001f, "img/Wave Forms/who_will_enter_wave.jpg");
        return highestFreq;
    }

    private static int reportHighestNonZero(String audioFile) {
        try {
            AudioSampleReader sampleReader = new AudioSampleReader(new File("audio/" + audioFile + ".wav"));
            long nbSamples = sampleReader.getSampleCount();

            double[] samples = new double[(int) nbSamples];
            sampleReader.getInterleavedSamples(0, nbSamples, samples);

            // Determine 2^i to use for N
            double powOfTwo = 0;
            for (int i = 0; powOfTwo < nbSamples; i++) {
                powOfTwo = Math.pow(2, i);
            }

            int N = (int) powOfTwo;

            // Construct Complex Array
            Complex2[] data = new Complex2[N];
            for (int i = 0; i < powOfTwo; i++) {
                if (i < nbSamples)
                    data[i] = new Complex2(samples[i], 0);
                else
                    data[i] = new Complex2(0, 0);
            }

            // FFT
            Fourier1D fft = new Fourier1D(N);
            Complex2[] fftData = fft.fft(data);

            // Find highest frequency with non-zero magnitude
            int highestFreq = 0;
            double magnitudeOfHighestFreq = 0;
            for (int freq = 0; freq < nbSamples / 2; freq++) {
                double mag = fftData[freq].Betrag();
                mag *= mag;
                if (mag > 0.1) {
                    highestFreq = freq;
                    magnitudeOfHighestFreq = mag;
                }
            }

            System.out.println(nbSamples / 2);
            System.out.println(highestFreq);
            System.out.println(magnitudeOfHighestFreq);
            System.out.println(nbSamples / 3);
//            System.out.println(wave);
//            render.renderWaveform(wave, .001f, "img/Wave Forms/who_will_enter_wave.jpg");
            return highestFreq;

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void butterWorthLowPassFilter(String audioFile) {
        try {
            AudioSampleReader sampleReader = new AudioSampleReader(new File("audio/" + audioFile + ".wav"));
            long nbSamples = sampleReader.getSampleCount();

            AudioFormat format = sampleReader.getFormat();

            double[] samples = new double[(int) nbSamples];
            sampleReader.getInterleavedSamples(0, nbSamples, samples);

            Complex2[] complexSamples = getComplexSamples(samples);

            Fourier1D fourier1D = new Fourier1D(complexSamples.length);
            Complex2[] fft = fourier1D.fft(complexSamples);

            Complex2[] filteredOutput = new Complex2[fft.length];
            double cutoff = 20000;
            double n = 2;
            int halfSamples = complexSamples.length / 2;
            for (int u = 0; u < halfSamples; u++) {
                //Butterworth H(u) = 1 / 1 + (u^2 / Ucutoff^2) ^ n
                double result = 1 / (1 + Math.pow(((u * u) / (cutoff * cutoff)), n));
                filteredOutput[u] = fft[u].mult(result);
                filteredOutput[u + halfSamples] = fft[u].mult(result);
            }

            Complex2[] finalOutput = fourier1D.ifft(filteredOutput);
//            Complex2[] finalOutput = fourier1D.ifft(fft);

            double[] newSamples = new double[samples.length];
            for (int i = 0; i < samples.length; i++) {
                double value = finalOutput[i].real;
                double roundedValue = (double) Math.round(value * 1000000000000000.0) / 1000000000000000.0;

                newSamples[i] = roundedValue;

//                if(newSamples[i] != samples[i])
//                {
//                    System.out.println("i: " + i + " samples[i]: " + samples[i] + " newSamples[i]: " + newSamples[i]);
//                }
            }

            File outFile = new File("audio/out/" + audioFile + "_filtered_" + cutoff + "_" + n + ".wav");
            AudioSampleWriter audioWriter = new AudioSampleWriter(outFile, format, AudioFileFormat.Type.WAVE);
            audioWriter.write(newSamples);
//            audioWriter.write(samples);
            audioWriter.close();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Complex2[] getComplexSamples(double[] samples) {
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
}