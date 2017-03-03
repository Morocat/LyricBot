package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.helpers.ImageServer;
import lyric.helpers.TextServer;
import lyric.reddit.RedditApi;
import lyric.utils.Pair;

public class ReactionCmd extends BotCommand {

	public ReactionCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (arguments == null || arguments.length == 0)
			return;
		Pair<String, String> urls = RedditApi.getInstance().getRandomImageFromSubreddit("reactiongifs", "gif");
		if (urls == null) 
			TextServer.sendString("Could not find an image to display", chat.getId(), absSender);
		else
			try {
				ImageServer.sendImageFromUrl(urls, chat.getId(), absSender);
			} catch (Exception e) {
				TextServer.sendString("Error displaying image", chat.getId(), absSender);
			}
	}

}
