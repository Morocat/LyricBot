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
		s += "/roll [i]d[j] - rolls [i] amount of [j]-sided dice\n";
		s += "/reddit [subreddit] - displays a random image from [subreddit]\n";
		s += "/skynet - generates a random sentence\n";
		
		s += "/hangman - starts a game of hangman\n";
		s += "\t\t/letter [letter] - guesses a letter\n";
		
		s += "/randit - starts a game of Randit";
		s += "/randit score - brags about your randit score for all of chat to be jealous about";
		s += "\t\t/guess - guesses a subreddit";
		
		//s += "/meme - displays a random meme\n";
		//s += "/react - displays a random reaction gif\n";
		
		s += "/poll - display the current poll\n";
		s += "\t\tresults - show the current poll results\n";
		s += "\t\tstart - start a new poll\n";
		s += "\t\tend - end the current poll\n";
		s += "\t\tq [question] - set the poll's question\n";
		s += "\t\tadd - add an option to the poll\n";
		s += "\t\trm [char] - remove a poll option while building a poll\n";
		
		s += "/greeting\n";
		s += "\t\t[custom string] - sets a custom string for your greeting\n";
		s += "\t\t[time] [4 digit 24hr time HHMM] - sets the time each day to display the greeting\n";
		s += "\t\tstop - stops your greeting service\n";
		
		if (BotAdmin.getInstance().isUserAdmin(bot, chat.getId(), user.getId(), chat.isUserChat())) {
			s += "/admin [userId] - adds [userId] to the list of bot admins for this chat\n";
			s += "/nsfw [on/off] - Enables/Disables nsfw features\n";
			//TextServer.sendString(s, user.getId());
		} //else
			//TextServer.sendString(s, chat.getId());
		TextServer.sendString(s, user.getId());
	}
	
	/**
	 * This string for updating command list with botfather using /setcommands
	 * 
	 * 
help - Display the list of commands
echo - Echos back a string
date - Display the current time and date
user - Displays user info
chat - Displays chat info
hangman - Start Hangman
letter - Guess a letter in Hangman
reddit - Display an image from [subreddit]
skynet - Generate a random sentence
poll - Displays the current poll
roll - Roll the dice
randit - Starts Randit
guess - guesses a subreddit
greeting - Set up your daily greeting
	 */
	
	//meme - Display a meme
	//react - Displays a reaction gif

}
