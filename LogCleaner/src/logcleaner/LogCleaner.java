/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logcleaner;

import java.io.*;

/**
 *
 * @author Justin Swanson
 */
public class LogCleaner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
	File dir = new File("Source/");
	if (dir.isDirectory()) {
	    File f = new File("");
	    for (File f2 : dir.listFiles()) {
		if (f2.getName().contains(".txt")) {
		    f = f2;
		    break;
		}
	    }
	    BufferedReader in = new BufferedReader(new FileReader(f));
	    BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
	    while (in.ready()) {
		String line = in.readLine();
		line = line.substring(line.indexOf("]") + 1);
		line = line.substring(line.indexOf("]") + 1);
		line = line.substring(line.indexOf("]") + 1);
		line = line.trim();
		out.write(line + "\n");
	    }

	    in.close();
	    out.close();
	}
    }
}
