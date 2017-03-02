package lyric.poll;

import lyric.PollingLyric;

public class Poll {
	private final static Poll instance = new Poll();
	private Poll(){}
	public static Poll getInstance() {
		return instance;
	}
	
	private PollingLyric bot;
	
	public void initialize(PollingLyric bot) {
		this.bot = bot;
	}
	
	/**
	 * Initialize a new poll
	 */
	public void createPoll() {
		
	}
	
	public void respondToPoll() {
		// 
	}

}
