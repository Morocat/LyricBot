package lyric;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

import lyric.commands.*;
import lyric.parsing.BaseCommandParser;
import lyric.servers.ImageServer;
import lyric.servers.TextServer;

public class PollingLyric extends TelegramLongPollingCommandBot {
	private final BaseCommandParser parser = new BaseCommandParser(this);
	
	//public final MemeCmd memeCmd = new MemeCmd("meme", "Displays a random meme from r/memes");
	//public final ReactionCmd reactCmd = new ReactionCmd("react", "Displays a reaction gif");
	public final RedditCmd redditCmd = new RedditCmd("reddit", "Displays a random meme from [subreddit]");
	public final SentienceCmd sentCmd = new SentienceCmd("skynet", "Generates a random sentence");
	public final Hangman hangman = new Hangman("hangman", "Starts a game of Hangman");
	public final Hangman.GuessCmd letterCmd = hangman.new GuessCmd("letter", "Guesses a letter in hangman");
	public final DateCmd dateCmd = new DateCmd("date", "Displays the current time & date");
	public final EchoCmd echoCmd = new EchoCmd("echo", "Echos back the given string");
	public final PollCmd pollCmd = new PollCmd("poll", "Starts a new poll");
	public final HelpCmd helpCmd = new HelpCmd("help", "Displays the list of commands");
	public final UserCmd userCmd = new UserCmd("user", "Displays user info");
	public final ChatCmd chatCmd = new ChatCmd("chat", "Displays chat info");
	public final AdminCmd adminCmd = new AdminCmd("admin", "Add a user as a bot admin for this chat");
	public final NsfwCmd nsfwCmd = new NsfwCmd("nsfw", "Sets whether this chat can use NSFW features");
	public final DiceCmd diceCmd = new DiceCmd("roll", "Rolls dice");
	public final RanditCmd randitCmd = new RanditCmd("randit", "Starts a game of Randit");
	public final RanditCmd.GuessCmd randitGuessCmd = randitCmd.new GuessCmd("guess", "Guess a subreddit");
	
	public PollingLyric() {
		TextServer.initialize(this);
		ImageServer.initialize(this);
		
		//register(memeCmd);
		//register(reactCmd);
		register(redditCmd);
		register(sentCmd);
		register(hangman);
		register(letterCmd);
		register(dateCmd);
		register(echoCmd);
		register(pollCmd);
		register(helpCmd);
		register(userCmd);
		register(chatCmd);
		register(adminCmd);
		register(nsfwCmd);
		register(diceCmd);
		register(randitCmd);
		register(randitGuessCmd);
		
		System.out.println("Initialization done");
	}

	@Override
	public String getBotUsername() {
		return "Lyric";
	}

	@Override
	public String getBotToken() {
		return "351737494:AAE7ie6cqDgG-Jj28WwxXZvcmSFTqHlidyg"; // production @LyricBot
		//return "333708864:AAEIiYkM9hzFWNk2rd05JiaqdlBgGF27NhQ"; // development @LyricBotDev
	}

	@Override
	public void processNonCommandUpdate(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			parser.parseInput(update);
	    }
	}

}
