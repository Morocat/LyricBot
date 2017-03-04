package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.reddit.RedditApi;
import lyric.servers.ImageServer;
import lyric.servers.TextServer;
import lyric.utils.Pair;

public class MemeCmd extends BotCommand {

	public MemeCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (arguments == null || arguments.length == 0)
			return;
		Pair<String, String> urls = RedditApi.getInstance().getRandomImageFromSubreddit("memes");
		if (urls == null) 
			TextServer.sendString("Could not find an image to display", chat.getId());
		else
			try {
				ImageServer.sendImageFromUrl(urls, chat.getId());
			} catch (Exception e) {
				TextServer.sendString("Error displaying image", chat.getId());
			}
	}
	
}
