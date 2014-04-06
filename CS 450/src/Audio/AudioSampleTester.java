// http://web.archive.org/web/20120531113946/http://www.builogic.com/java/javasound-read-write.html
package Audio;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class AudioSampleTester {

    public static void lowerSound(String fileName) {
        try {
            AudioSampleReader sampleReader = new AudioSampleReader(new File("audio/" + fileName + ".wav"));
            long nbSamples = sampleReader.getSampleCount();

            AudioFormat format = sampleReader.getFormat();

            System.out.println("nbChannel=" + format.getChannels());
            System.out.println("frameRate=" + format.getFrameRate());
            System.out.println("sampleSize=" + format.getSampleSizeInBits());

            double[] samples = new double[(int)nbSamples];
            sampleReader.getInterleavedSamples(0, nbSamples, samples);
            // lowers sound level
            for (int i = 0; i < samples.length; i++) {
                samples[i] /= 4.;
            }

            File outFile = new File("audio/out/" + fileName + "_quieter.wav");
            AudioSampleWriter audioWriter = new AudioSampleWriter(outFile,
                    sampleReader.getFormat(), AudioFileFormat.Type.WAVE);
            audioWriter.write(samples);
            audioWriter.close();

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
