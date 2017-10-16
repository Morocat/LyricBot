package lyric.servers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHost {

	public static void writeFile(String path, String text) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		bw.write(text);
		bw.close();
	}
	
	public static String readFile(String path) throws Exception {
		String text = "";
		FileReader fr = new FileReader(new File(path));
		BufferedReader br = new BufferedReader(fr);
		String line;
		while((line = br.readLine()) != null)
			text += line;
		br.close();
		return text;
	}
}
