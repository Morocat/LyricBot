package lyric.parsing;

import org.telegram.telegrambots.api.objects.Update;

import lyric.PollingLyric;

public class BaseCommandParser {

	private final PollingLyric bot;

	public BaseCommandParser(PollingLyric bot) {
		this.bot = bot;
	}

	public void parseInput(Update u) {
		String msg = u.getMessage().getText().toLowerCase();
		// Commands
		if (u.getMessage().isCommand()) {
			// do nothing
		} else if (msg.contains("http:") || msg.contains("https:") || msg.contains("www.")){ // web address, ignore for now
			
		} else { // at this point the msg is probably just a regular conversation, add to the Markov chain
			bot.sentCmd.recordString(msg);
		}
		
	}

}
