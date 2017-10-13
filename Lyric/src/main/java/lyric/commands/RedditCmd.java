package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.reddit.RedditApi;
import lyric.reddit.RedditException;
import lyric.reddit.RedditReply;
import lyric.servers.ImageServer;
import lyric.servers.TextServer;

public class RedditCmd extends BotCommand {

	public RedditCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (arguments == null || arguments.length == 0)
			return;
		RedditReply rreply = null;
		try {
			rreply = RedditApi.getInstance().getRandomImageFromSubreddit(arguments[0], chat.getId(), new String[0]);
		} catch (RedditException e) {
			TextServer.sendString(e.getMessage(), chat.getId());
			return;
		}
		if (rreply == null) 
			TextServer.sendString("Could not find an image to display", chat.getId());
		else
			try {
				ImageServer.sendImageFromUrl(rreply, chat.getId());
			} catch (Exception e) {
				TextServer.sendString("Error displaying image", chat.getId());
			}
	}

}
