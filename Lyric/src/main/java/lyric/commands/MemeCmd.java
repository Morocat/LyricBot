package lyric.commands;

import java.util.Random;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.reddit.RedditApi;
import lyric.reddit.RedditException;
import lyric.reddit.RedditReply;
import lyric.servers.ImageServer;
import lyric.servers.TextServer;

public class MemeCmd extends BotCommand {
  private final static Random rand = new Random();

	public MemeCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		RedditReply rreply = null;
    	String lmao = "";
    
		if (rand.nextInt(100) == 69)
			lmao = "lmao";
		
		try {
			rreply = RedditApi.getInstance().getRandomImageFromSubreddit("memes", chat.getId(), new String[0]);
		} catch (RedditException e) {
			TextServer.sendString(e.getMessage() + lmao, chat.getId());
			return;
		}
		if (rreply == null) 
			TextServer.sendString("Could not find an image to display" + lmao, chat.getId());
		else
			try {
				ImageServer.sendImageFromUrl(rreply, chat.getId());
			} catch (Exception e) {
				TextServer.sendString("Error displaying image" + lmao, chat.getId());
			}
	}
	
}
