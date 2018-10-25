import java.io.FileInputStream;
import java.io.FileOutputStream;

public class OpenStreamNotClosed {

    /**
     * A test function that opens and does not close a stream
     */
    public static void testOpenStream() {
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream("input.txt");
            out = new FileOutputStream("output.txt");

            FileInputStream test = new FileInputStream("test.txt");

            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        }finally {
            // nothing...
        }
    }

}