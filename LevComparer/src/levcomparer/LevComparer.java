/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levcomparer;

import java.io.*;
import lev.Ln;

/**
 *
 * @author Justin Swanson
 */
public class LevComparer {

    static BufferedWriter out;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        try {
            // TODO code application logic here
            out = new BufferedWriter(new FileWriter("Compare.txt"));
            redirectSystemOutStream();
	    int numPrint = 15;
	    if (args.length > 0)
		numPrint = Integer.valueOf(args[0]);
            System.out.println("Num Print: " + numPrint + "\n");

            File root = new File("./");
	    File f1 = null, f2 = null;
	    for (File f : root.listFiles()) {
		if (!f.getName().endsWith(".jar") && !f.getName().equals("Compare.txt")) {
		    if (f1 == null) {
			f1 = f;
		    } else if (f2 == null) {
			f2 = f;
			break;
		    }
		}
	    }

	    System.out.println("File 1: " + f1);
	    System.out.println("File 2: " + f2);

            if (Ln.validateCompare(f1.getPath(), f2.getPath(), numPrint)) {
                System.out.println("Passed");
            } else {
                System.out.println("Failed");
            }
        } catch (Exception ex) {
        }


        out.close();
    }

    private static void redirectSystemOutStream() throws FileNotFoundException, IOException {
        OutputStream outToDebug = new OutputStream() {

            @Override
            public void write(final int b) throws IOException {
                if (b != 116) {
                    out.write(String.valueOf((char) b));
                }
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                String output = new String(b, off, len);
                output = output.trim();
                if (output.length() > 2) {
                    out.write(output + "\n");
                }
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(outToDebug, true));
    }
}
