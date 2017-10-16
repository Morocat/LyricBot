package lyric.reddit;

import java.util.Date;

import org.json.JSONObject;

import lyric.admin.Nsfw;
import lyric.servers.FileHost;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.models.Subreddit.SubmissionType;

public class RedditApi {
	private final static String CREDENTIALS_FILE_PATH = "reddit_credentials.txt";
	private final static RedditApi instance = new RedditApi();
	private String userName, userPassword, clientId, clientSecret;
	
	private RedditApi() {
		try {
			JSONObject o = new JSONObject(FileHost.readFile(CREDENTIALS_FILE_PATH));
			userName = o.getString("userName");
			userPassword = o.getString("userPassword");
			clientId = o.getString("clientId");
			clientSecret = o.getString("clientSecret");
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserAgent agent = UserAgent.of("desktop", "LyricBot", "v0.1", userName);
		client = new RedditClient(agent);
		creds = Credentials.script(userName, userPassword, clientId, clientSecret);
		try {
			OAuthData authData = client.getOAuthHelper().easyAuth(creds);
			client.authenticate(authData);
			expireTime = authData.getExpirationDate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static RedditApi getInstance() {
		return instance;
	}
	
	private Date expireTime;
	private final Credentials creds;
	private final RedditClient client;
	
	public RedditReply getRandomImageFromRandomSubreddit(long chatId) throws RedditException {
		reAuthenticate();
		Subreddit sub = null;
		int i;
		for(i = 0; i < 20; i++) {
			try {
				sub = getSubreddit("random");
				verifySubreddit(sub, chatId);
				if (sub.getSubscriberCount() > 50000)
					break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("It took " + i + " tries to find a valid random subreddit");
		System.out.println("The subreddit is [" + sub.getDisplayName() + "]");
		return getRandomImageFromSubreddit(sub, chatId, new String[0]);
	}
	
	public RedditReply getRandomImageFromSubreddit(String subreddit, long chatId, String... imageType) throws RedditException {
		if (subreddit.equals("random"))
			return getRandomImageFromRandomSubreddit(chatId);
		reAuthenticate();
		Subreddit sub = getSubreddit(subreddit);
		verifySubreddit(sub, chatId);
		return getRandomImageFromSubreddit(sub, chatId, imageType);
	}
	
	private RedditReply getRandomImageFromSubreddit(Subreddit sub, long chatId, String... imageType) throws RedditException {
		RedditReply rreply = new RedditReply();
		Submission s = findImage(sub.getDisplayName(), chatId, rreply, imageType);
		if (rreply.caption != null && imageType.length > 0)
			return rreply;
		
		rreply.urls.add(s.getUrl());
		rreply.urls.add(s.getShortURL());
		rreply.caption = s.getTitle();
		rreply.subreddit = sub.getDisplayName();
		return rreply;
	}
	
	private void reAuthenticate() throws RedditException {
		if (new Date().getTime() > expireTime.getTime()) {
			try {
				OAuthData authData = client.getOAuthHelper().easyAuth(creds);
				client.authenticate(authData);
				expireTime = authData.getExpirationDate();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RedditException("Error authenticating");
			}
		}
	}
	
	private Subreddit getSubreddit(String subreddit) throws RedditException {
		Subreddit sub = null;
		try {
			sub = client.getSubreddit(subreddit);
			if (sub == null)
				throw new RedditException("Error finding subreddit");
		} catch (NetworkException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new RedditException(e.getMessage());
		}
		return sub;
	}
	
	private void verifySubreddit(Subreddit sub, long chatId) throws RedditException {
		try {
			if (sub.getAllowedSubmissionType() == SubmissionType.SELF) {
				throw new RedditException("Allowed submission types is self only");
			}
		} catch (NetworkException e) {
			e.printStackTrace();
			throw new RedditException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new RedditException(e.getMessage(), e);
		}
		if (sub.isNsfw() && !Nsfw.getInstance().isChatAllowNsfw(chatId)) {
			throw new RedditException("NSFW mode is off");
		}
	}
	
	private Submission findImage(String subreddit, long chatId, RedditReply rreply, String... imageType) {
		Submission s = null;
		for (int i = 0; i < 10; i++) {
			try {
				s = client.getRandomSubmission(subreddit);
				if (s.isSelfPost())
					continue;
				if (s.isNsfw() && !Nsfw.getInstance().isChatAllowNsfw(chatId))
					continue;
				if (s.getUrl().contains("youtu.be"))
					continue;
				if (imageType.length == 0) {
					break;
				} else {
					for (int j = 0; j < imageType.length; j++)
						if (s.getUrl().endsWith(imageType[j])) {
							//rreply = new RedditReply();
							rreply.urls.add(s.getUrl());
							rreply.urls.add(s.getShortURL());
							rreply.caption = s.getTitle();
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		if (s == null || s.isSelfPost())
			return null;
		return s;
	}
	
}
