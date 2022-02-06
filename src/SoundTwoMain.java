import java.io.*;
import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.util.Random;


public class SoundTwoMain {
    public Clip clip;
    public GeneratorThread generatorThread;

    public static void main(String[] args) {

    }


    public void loadFile() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

//        File file = new File("./samples/Bell2A.wav");
//        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
//
//        clip = AudioSystem.getClip();
//        clip.open(audioStream);
    }

    static class GeneratorThread extends Thread {
        final static public int SAMPLE_SIZE = 10;
        final static public int PACKET_SIZE = 1000;

        SourceDataLine line;
        public boolean exitExecution = false;

        public void startWhiteNoise() {

            try {
                AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, PACKET_SIZE * 2);

                if (!AudioSystem.isLineSupported(info)) {
                    throw new LineUnavailableException();
                }

                line = (SourceDataLine)AudioSystem.getLine(info);
                line.open(format);
                line.start();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);

            // Start noise
            Random random = new Random();
            while (exitExecution == false) {
                buffer.clear();
                for (int i=0; i < PACKET_SIZE / SAMPLE_SIZE; i++) {
                    double value;
                    System.out.println(i);
                    if (i > 30) {
                        value = random.nextGaussian();
                        buffer.putShort((short) (value * Short.MAX_VALUE));
                    } else {
                        buffer.putShort((short) ((i * -1) * Short.MAX_VALUE));
                    }

                    // buffer.putShort((short) (value * Short.MAX_VALUE));
                    // System.out.println((short) (value * Short.MAX_VALUE));


                }
                line.write(buffer.array(), 0, buffer.position());
            }

            line.drain();
            line.close();
        }

        public void exit() {
            exitExecution = true;
        }

    }




    public void playSound() {
        System.out.println("Play!");
        System.out.println("Clip: " + clip);
        clip.start();
    }
}
