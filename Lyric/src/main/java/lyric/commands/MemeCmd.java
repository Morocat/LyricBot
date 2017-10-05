package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.reddit.RedditApi;
import lyric.reddit.RedditException;
import lyric.servers.ImageServer;
import lyric.servers.TextServer;
import lyric.utils.Pair;

import Math;

public class MemeCmd extends BotCommand {

	public MemeCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		Pair<String, String> urls = null;
		int roll;
		roll = Math.random() * 100;
		String lmao = "";
		if (roll == 69) {
			lmao = "lmao";
		}
		try {
			urls = RedditApi.getInstance().getRandomImageFromSubreddit("memes", chat.getId());
		} catch (RedditException e) {
			TextServer.sendString(e.getMessage(), chat.getId());
			return;
		}
		if (urls == null) 
			TextServer.sendString("Could not find an image to display" + lmao, chat.getId());
		else
			try {
				ImageServer.sendImageFromUrl(urls, chat.getId());
			} catch (Exception e) {
				TextServer.sendString("Error displaying image" + lmao, chat.getId());
			}
		lmao = "";
	}
	
}
