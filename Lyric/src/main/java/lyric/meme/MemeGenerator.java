package lyric.meme;

import lyric.PollingLyric;
import lyric.image.ImageServer;
import lyric.reddit.RedditApi;
import lyric.utils.Pair;

public class MemeGenerator {
	private final static MemeGenerator instance = new MemeGenerator();
	private MemeGenerator(){}
	
	public static MemeGenerator getInstance() {
		return instance;
	}
	
	private PollingLyric bot;
	
	public void initialize(PollingLyric bot) {
		this.bot = bot;
	}
	
	public void showMeme() {
		Pair<String, String> urls = RedditApi.getInstance().getRandomImageFromSubreddit("memes");
		if (urls == null) 
			bot.sendString("Could not find an image to display");
		else
			try {
				ImageServer.getInstance().sendImageFromUrl(urls);
			} catch (Exception e) {
				bot.sendString("Error displaying image");
			}
	}

	public void showMemeFromSubreddit(String sub) {
		Pair<String, String> urls = RedditApi.getInstance().getRandomImageFromSubreddit(sub);
		if (urls == null) 
			bot.sendString("Could not find an image to display");
		else
			try {
				ImageServer.getInstance().sendImageFromUrl(urls);
			} catch (Exception e) {
				bot.sendString("Error displaying image");
			}
	}

	public void showReaction() {
		Pair<String, String> urls = RedditApi.getInstance().getRandomImageFromSubreddit("reactiongifs", "gif");
		if (urls == null) 
			bot.sendString("Could not find an image to display");
		else
			try {
				ImageServer.getInstance().sendImageFromUrl(urls);
			} catch (Exception e) {
				bot.sendString("Error displaying image");
			}
	}
}
