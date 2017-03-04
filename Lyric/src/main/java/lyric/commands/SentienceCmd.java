package lyric.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;
=======
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.servers.TextServer;
import rita.RiMarkov;

public class SentienceCmd extends BotCommand {

	private final static boolean SCALABLE_MODE = false;
<<<<<<< HEAD
	private final static String FILENAME = "Chat History_";
	private final int nFactor = 3; // 3 seems to work well, can change later if desired
	private final Pattern p = Pattern.compile("\\[\\[(.*)\\]\\]"); // matches any line with text inside of [[ ]] such as [[Photo]]
	private final Pattern nonAlpha = Pattern.compile("([a-z]|[A-Z]).*"); // matches any line containing at least one alphabetical character
	private final HashMap<Long, String> chatHistory = new HashMap<>();
	private final HashMap<Long, String> historyDelta = new HashMap<>();
=======
	private final static String FILENAME = "Chat History.txt";
	private final int nFactor = 3; // 3 seems to work well, can change later if desired
	private final Pattern p = Pattern.compile("\\[\\[(.*)\\]\\]"); // matches any line with text inside of [[ ]] such as [[Photo]]
	private final Pattern nonAlpha = Pattern.compile("([a-z]|[A-Z]).*"); // matches any line containing at least one alphabetical character
	private String chatHistory = "", historyDelta = "";
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
	
	public SentienceCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
		if (SCALABLE_MODE) {
<<<<<<< HEAD
=======
			try {
				chatHistory = loadFile(FILENAME);
			} catch (IOException e) {
				e.printStackTrace();
			}
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
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
<<<<<<< HEAD
					for (Map.Entry<Long, String> en : historyDelta.entrySet()) {
						writeFile(en.getValue(), en.getKey(), true);
						chatHistory.put(en.getKey(), chatHistory.get(en.getKey()) + en.getValue());
						historyDelta.put(en.getKey(), "");
					}
=======
					writeFile(historyDelta, true);
					chatHistory += historyDelta;
					historyDelta = "";
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	});
	
	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
				RiMarkov m = new RiMarkov(nFactor);
				if (SCALABLE_MODE)
<<<<<<< HEAD
					m.loadText(chatHistory.get(chat.getId()) + historyDelta);
				else
					m.loadText(loadFile(chat.getId()));
=======
					m.loadText(chatHistory + historyDelta);
				else
					m.loadText(loadFile(FILENAME));
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
				TextServer.sendString(m.generateSentence(), chat.getId());
				} catch (Exception e) {
					e.printStackTrace();
					TextServer.sendString("Error generating sentence", chat.getId());
				}
			}
		}).start();
	}
	
<<<<<<< HEAD
	public void recordString(String s, long chatId) {
=======
	public void recordString(String s) {
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
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
<<<<<<< HEAD
					if (chatHistory.get(chatId) == null)
						chatHistory.put(chatId, loadFile(chatId));
					if (SCALABLE_MODE)
						historyDelta.put(chatId, historyDelta.get(chatId) + s);
					else
						writeFile(s, chatId, true);
=======
					if (SCALABLE_MODE)
						historyDelta += s;
					else
						writeFile(s, true);
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();	
	}
	
<<<<<<< HEAD
	private synchronized String loadFile(long chatId) throws IOException {
		FileReader fr = new FileReader(new File(FILENAME + chatId + ".txt"));
=======
	private synchronized String loadFile(String name) throws IOException {
		FileReader fr = new FileReader(new File(name));
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
		BufferedReader br = new BufferedReader(fr);
		String s = "", line;
		while((line = br.readLine()) != null)
			s += line + "\n";
		br.close();
		return s;
	}
	
<<<<<<< HEAD
	private synchronized void writeFile(String str, long chatId, boolean append) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME + chatId + ".txt", append));
=======
	private synchronized void writeFile(String str, boolean append) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME, append));
>>>>>>> 06ba76652b8ea572f8a19609cfc1a7d956147385
		bw.write(str);
		bw.close();
	}
	
}
