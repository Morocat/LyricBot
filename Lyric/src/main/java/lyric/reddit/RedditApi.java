package lyric.reddit;

import lyric.admin.Nsfw;
import lyric.utils.Pair;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit.SubmissionType;

public class RedditApi {
	private final static RedditApi instance = new RedditApi();
	private RedditApi() {
		UserAgent agent = UserAgent.of("desktop", "LyricBot", "v0.1", "time_cat");
		client = new RedditClient(agent);
		Credentials creds = Credentials.script("time_cat", "rocketman", "0aLW0wi7S-Fz9Q", "-XyLm1yLuzuWUHGuxMQyp5OYtOc");
		try {
			OAuthData authData = client.getOAuthHelper().easyAuth(creds);
			client.authenticate(authData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static RedditApi getInstance() {
		return instance;
	}
	
	private final RedditClient client;
	
	public Pair<String, String> getRandomImageFromSubreddit(String subreddit, long chatId) {
		return getRandomImageFromSubreddit(subreddit, chatId, new String[0]);
	}
	
	public Pair<String, String> getRandomImageFromSubreddit(String subreddit, long chatId, String... imageType) {
		try {
			if (client.getSubreddit(subreddit).getAllowedSubmissionType() == SubmissionType.SELF) {
				System.out.println("Allowed submission types is self only");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (client.getSubreddit(subreddit).isNsfw() && !Nsfw.getInstance().isChatAllowNsfw(chatId)) {
			System.out.println("NSFW mode is off");
			return null;
		}
		
		Submission s = null;
		for (int i = 0; i < 10; i++) {
			try {
				s = client.getRandomSubmission(subreddit);
				if (s.isSelfPost())
					continue;
				if (s.isNsfw() && !Nsfw.getInstance().isChatAllowNsfw(chatId))
					continue;
				if (imageType.length == 0) {
					break;
				} else {
					for (int j = 0; j < imageType.length; j++)
						if (s.getUrl().endsWith(imageType[j]))
							return new Pair<String, String>(s.getUrl(), s.getShortURL());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		if (s == null)
			return null;
		return s.isSelfPost() ? null : new Pair<String, String>(s.getUrl(), s.getShortURL());
	}
	
}
