package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.servers.TextServer;

public class HelpCmd extends BotCommand {

	public HelpCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
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
		s += "/poll - display the current poll\n";
		s += "/poll start - start a new poll\n";
		s += "/poll end - end the current poll\n";
		s += "/poll rm [char] - remove a poll option while building a poll\n";
		s += "/poll results - show the current poll results\n";
		TextServer.sendString(s, chat.getId());
	}

}
