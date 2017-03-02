package lyric.skynet;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lyric.PollingLyric;
import rita.RiMarkov;

public class SentenceGenerator {
	private final static SentenceGenerator instance = new SentenceGenerator();
	private SentenceGenerator(){}
	
	public static SentenceGenerator getInstance() {
		return instance;
	}
	private final static boolean SCALABLE_MODE = false;
	private final static String FILENAME = "Chat History.txt";
	private final int nFactor = 3; // 3 seems to work well, can change later if desired
	private final Pattern p = Pattern.compile("\\[\\[(.*)\\]\\]"); // matches any line with text inside of [[ ]] such as [[Photo]]
	private final Pattern nonAlpha = Pattern.compile("([a-z]|[A-Z]).*"); // matches any line containing at least one alphabetical character
	private String chatHistory = "", historyDelta = "";
	private PollingLyric bot;
	
	public void initialize(PollingLyric bot) {
		this.bot = bot;
		if (SCALABLE_MODE) {
			try {
				chatHistory = loadFile(FILENAME);
			} catch (IOException e) {
				e.printStackTrace();
			}
			historyWriter.start();
		}
	}
	
	/**
	 * Saves off the additions to the chat history once/hour
	 */
	private final Thread historyWriter = new Thread(new Runnable() {
		@Override
		public void run() {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(3600000);
					writeFile(historyDelta, true);
					chatHistory += historyDelta;
					historyDelta = "";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	});
	
	public void recordString(String s) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Matcher m, n;
				if (s.equals(""))
					return;
				
				m = p.matcher(s);
				if (m.matches()) // skip stickers, geolocations, etc
					return;
				n = nonAlpha.matcher(s);
				if (!n.matches()) // skip messages without any alphabetical characters
					return;
				try {
					if (SCALABLE_MODE)
						historyDelta += s;
					else
						writeFile(s, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();	
	}
	
	public void generateSentence() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
				RiMarkov m = new RiMarkov(nFactor);
				if (SCALABLE_MODE)
					m.loadText(chatHistory + historyDelta);
				else
					m.loadText(loadFile(FILENAME));
				bot.sendString(m.generateSentence());
				} catch (Exception e) {
					e.printStackTrace();
					bot.sendString("Error generating sentence");
				}
			}
		}).start();
	}
	
	private synchronized String loadFile(String name) throws IOException {
		FileReader fr = new FileReader(new File(name));
		BufferedReader br = new BufferedReader(fr);
		String s = "", line;
		while((line = br.readLine()) != null)
			s += line + "\n";
		br.close();
		return s;
	}
	
	private synchronized void writeFile(String str, boolean append) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME, append));
		bw.write(str);
		bw.close();
	}
	
}
