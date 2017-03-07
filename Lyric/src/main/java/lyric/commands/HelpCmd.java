package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.admin.BotAdmin;
import lyric.servers.TextServer;

public class HelpCmd extends BotCommand {

	public HelpCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender bot, User user, Chat chat, String[] arguments) {
		String s = "What's this? owo\n";
		s += "/help - shows this help message\n";
		s += "/echo [string] - echos back the string\n";
		s += "/date - displays the current date and time\n";
		s += "/user - displays user info\n";
		s += "/chat - displays chat info\n";
		s += "/hangman - starts a game of hangman\n";
		s += "\t\t/guess [letter] - guesses a letter\n";
		s += "/meme - displays a random meme\n";
		s += "/react - displays a random reaction gif\n";
		s += "/image [subreddit] - displays a random image from [subreddit]\n";
		s += "/skynet - generates a random sentence\n";
		s += "/poll - display the current poll\n";
		s += "/poll results - show the current poll results\n";
		if (BotAdmin.getInstance().isUserAdmin(bot, chat.getId(), user.getId())) {
			s += "/admin [userId] - adds [userId] to the list of bot admins for this chat\n";
			s += "/poll start - start a new poll\n";
			s += "/poll end - end the current poll\n";
			s += "/poll q [question] - set the poll's question\n";
			s += "/poll add - add an option to the poll\n";
			s += "/poll rm [char] - remove a poll option while building a poll\n";
			TextServer.sendString(s, user.getId());
		} else
			TextServer.sendString(s, chat.getId());
	}
	
	/**
	 * This string for updating command list with botfather using /setcommands
	 * 
	 * help - Display the list of commands
echo - Echos back a string
date - Display the current time and date
user - Displays user info
chat - Displays chat info
hangman - Start Hangman
guess - Guess a letter in Hangman
meme - Display a meme
react - Display a reaction gif
image - Display an image from [subreddit]
skynet - Generate a random sentence
poll - Displays the current poll
	 */

}
