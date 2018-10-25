import java.io.FileInputStream;
import java.io.FileOutputStream;

public class OpenStreamClosed {

    /**
     * A test function that opens and closes a stream
     */
    public static void testOpenStream() {
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream("input.txt");
            out = new FileOutputStream("output.txt");

            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        }finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            //test.close();
        }
    }

}