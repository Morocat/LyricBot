package lyric.parsing;

import org.telegram.telegrambots.api.objects.Update;

import lyric.PollingLyric;
import lyric.games.hangman.Hangman;
import lyric.meme.MemeGenerator;
import lyric.misc.MiscCommands;
import lyric.skynet.SentenceGenerator;

public class BaseCommandParser {

	private final PollingLyric bot;

	public BaseCommandParser(PollingLyric bot) {
		this.bot = bot;
	}

	public void parseInput(Update u) {
		String msg = u.getMessage().getText().toLowerCase();
		// Commands
		if (u.getMessage().isCommand()) {
			String cmd = msg.substring(1, msg.contains(" ") ? msg.indexOf(' ') : msg.length());
			String[] params = msg.contains(" ") ? msg.substring(msg.indexOf(' ') + 1).split(" ") : new String[0];
			if (cmd.equals("help"))
				showHelp();
			else if (cmd.equals("echo"))
				MiscCommands.getInstance().echo(msg.substring(msg.indexOf(' ')));
			else if (cmd.equals("date"))
				MiscCommands.getInstance().printDate();
			else if (cmd.equals("hangman"))
				Hangman.getInstance().start();
			else if (cmd.equals("guess"))
				Hangman.getInstance().guess(params);
			else if (cmd.equals("meme"))
				MemeGenerator.getInstance().showMeme();
			else if (cmd.equals("react"))
				MemeGenerator.getInstance().showReaction();
			else if (cmd.equals("image"))
				MemeGenerator.getInstance().showMemeFromSubreddit(params[0]);
			else if (cmd.equals("gen"))
				SentenceGenerator.getInstance().generateSentence();
		} else if (msg.contains("http:") || msg.contains("https:") || msg.contains("www.")){ // web address, ignore for now
			
		} else { // at this point the msg is probably just a regular conversation, add to the Markov chain
			SentenceGenerator.getInstance().recordString(msg);
		}
		
	}

	private void showHelp() {
		String s = "What's this owo?\n";
		s += "/help - shows this help message\n";
		s += "/echo [string] - echos back the string\n";
		s += "/date - displays the current date and time\n";
		s += "/hangman - starts a game of hangman\n";
		s += "\t\t/guess [letter] - guesses a letter\n";
		s += "/meme - displays a random meme\n";
		s += "/react - displays a random reaction gif\n";
		s += "/image [subreddit] - displays a random image from [subreddit]\n";
		s += "/gen - generates a random sentence\n";
		s += "/startpoll - start a new poll\n";
		s += "/endpoll - end the current poll\n";
		s += "/resp [letter] - respond to the current poll\n";
		s += "/results - show the current poll results\n";
		s += "/image [subreddit] - displays a random image from [subreddit]";
		s += "/gen - generates a random sentence";
		s += "/startpoll - start a new poll";
		s += "/endpoll - end the current poll";
		s += "/resp [letter] - respond to the current poll";
		s += "/results - show the current poll results";
		bot.sendString(s);
	}

}
