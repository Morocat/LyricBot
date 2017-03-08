package lyric.parsing;

import org.telegram.telegrambots.api.objects.Update;

import lyric.PollingLyric;
import lyric.poll.Poll;

public class BaseCommandParser {

	private final PollingLyric bot;

	public BaseCommandParser(PollingLyric bot) {
		this.bot = bot;
	}

	public void parseInput(Update u) {
		String msg = u.getMessage().getText();
		long chatId = u.getMessage().getChatId();
		Poll poll = bot.pollCmd.getPoll(chatId);
		//int userId = u.getMessage().getFrom().getId();
		
		// Commands
		if (u.getMessage().isCommand()) {
			// do nothing
		} else if (msg.toLowerCase().contains("http:") || msg.toLowerCase().contains("https:") || msg.toLowerCase().contains("www.")){ // web address, ignore for now
			// do nothing
		} /*else if (poll != null && poll.state == Poll.STATE_BUILDING_QUESTION) {
			bot.pollCmd.setPollQuestion(msg, chatId, userId);
		} else if (poll != null && poll.state == Poll.STATE_BUILDING_RESPONSES) {
			bot.pollCmd.addPollOption(msg, chatId, userId);
		}*/ else if (poll != null && poll.isOption(msg)) {
			bot.pollCmd.recordResponse(msg, chatId, u.getMessage().getFrom());
		} else { // at this point the msg is probably just a regular conversation, add to the Markov chain
			bot.sentCmd.recordString(msg, chatId, u.getMessage().getChat().isUserChat());
		}
		
	}

}
