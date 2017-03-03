package lyric.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.helpers.TextServer;
import rita.RiMarkov;

public class SentienceCmd extends BotCommand {

	private final static boolean SCALABLE_MODE = false;
	private final static String FILENAME = "Chat History.txt";
	private final int nFactor = 3; // 3 seems to work well, can change later if desired
	private final Pattern p = Pattern.compile("\\[\\[(.*)\\]\\]"); // matches any line with text inside of [[ ]] such as [[Photo]]
	private final Pattern nonAlpha = Pattern.compile("([a-z]|[A-Z]).*"); // matches any line containing at least one alphabetical character
	private String chatHistory = "", historyDelta = "";
	
	public SentienceCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
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
	
	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
				RiMarkov m = new RiMarkov(nFactor);
				if (SCALABLE_MODE)
					m.loadText(chatHistory + historyDelta);
				else
					m.loadText(loadFile(FILENAME));
				TextServer.sendString(m.generateSentence(), chat.getId(), absSender);
				} catch (Exception e) {
					e.printStackTrace();
					TextServer.sendString("Error generating sentence", chat.getId(), absSender);
				}
			}
		}).start();
	}
	
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
