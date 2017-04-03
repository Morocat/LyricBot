package lyric.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {
	private final static String FILENAME = "DevLog.txt";

	public static void write(String str) {
		str = new Date().toString() + ": " + str;
		try {
			writeFile(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static synchronized void writeFile(String str) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME, true));
		bw.write(str + "\n");
		bw.close();
	}

}
