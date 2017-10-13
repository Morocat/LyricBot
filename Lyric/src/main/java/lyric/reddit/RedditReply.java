package lyric.reddit;

import java.util.ArrayList;

public class RedditReply {
	public final ArrayList<String> urls = new ArrayList<>();
    public String caption;
    public String subreddit;

    public RedditReply() {
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RedditReply))
            return false;
        if (o == this)
            return true;
        if (o.hashCode() == this.hashCode())
        	return true;
        RedditReply t = (RedditReply) o;
        return urls.equals(t.urls)
                && caption.equals(t.caption)
                && subreddit.equals(t.subreddit);
    }

    @Override
    public int hashCode() {
        return (urls.hashCode()) ^ (caption.hashCode()) ^ (subreddit.hashCode());
    }
}
