package lyric.reddit;

@SuppressWarnings("serial")
public class RedditException extends Exception {

	public RedditException(String string) {
		super(string);
	}

	public RedditException(String message, Throwable cause) {
		super(message, cause);
	}

}
