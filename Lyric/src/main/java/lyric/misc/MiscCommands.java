package lyric.misc;

import java.util.*;

import lyric.PollingLyric;

public class MiscCommands {
	private final static MiscCommands instance = new MiscCommands();
	private MiscCommands(){}
	
	public static MiscCommands getInstance() {
		return instance;
	}
	
	private PollingLyric bot;
	
	public void initialize(PollingLyric bot) {
		this.bot = bot;
	}
	
	public void echo(String s) {
		bot.sendString(s);
	}
	
	public void printDate() {
		bot.sendString(new Date().toString());
	}
}
