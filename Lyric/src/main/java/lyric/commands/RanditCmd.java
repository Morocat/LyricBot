package lyric.commands;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.reddit.RedditApi;
import lyric.reddit.RedditException;
import lyric.reddit.RedditReply;
import lyric.servers.FileHost;
import lyric.servers.ImageServer;
import lyric.servers.TextServer;

public class RanditCmd extends BotCommand {
	private final static String RANDIT_SCORES_PATH = "randit_scores.txt";
	private final static int STATE_GAME_OVER = 0, STATE_PLAYING = 1;
	
	private JSONObject userScores;
	
	private int gameState = STATE_GAME_OVER;
	private int guessesRemaining;
	private RedditReply rreply = null;

	public RanditCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
		loadScores();
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (arguments != null && arguments.length > 0) {
			if (arguments[0].equals("score"))
				showUserScore(user, chat.getId());
			return;
		}
		if (gameState != STATE_GAME_OVER) {
			TextServer.sendString("Randit is already running", chat.getId());
			return;
		}
		
		try {
			rreply = RedditApi.getInstance().getRandomImageFromRandomSubreddit(chat.getId());
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
		guessesRemaining = 3;
		gameState = STATE_PLAYING;
		TextServer.sendString("You have " + (guessesRemaining + 1) + " guesses remaining.", chat.getId());
	}
	
	private void showUserScore(User user, long chatId) {
		int score = userScores.optInt(String.valueOf(user.getId()));
		if (score == 0)
			TextServer.sendString(user.getFirstName() + " will one day reach greatness but for now is crying in the corner with their score of " + score, chatId);
		else
			TextServer.sendString(user.getFirstName() + " is pretty awesome with their score of " + score, chatId);
	}

	public class GuessCmd extends BotCommand {

		public GuessCmd(String commandIdentifier, String description) {
			super(commandIdentifier, description);
		}

		@Override
		public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
			if (arguments == null || arguments.length == 0 || gameState == STATE_GAME_OVER)
				return;
			if (arguments[0].toLowerCase().equals(rreply.subreddit.toLowerCase()))
				guessCorrect(user, chat.getId());
			else
				guessWrong(user, chat.getId(), arguments[0]);
		}
		
		private void guessCorrect(User user, long chatId) {
			int score = userScores.optInt(String.valueOf(user.getId()));
			if (guessesRemaining == 3)
				score++;
			userScores.put(String.valueOf(user.getId()), ++score);
			gameState = STATE_GAME_OVER;
			saveScores();
			if (guessesRemaining == 3) 
				TextServer.sendString(user.getFirstName() + " NAILED IT on their first guess of " + rreply.subreddit + " and now has an astounding " + score + " points!", chatId);
			else
				TextServer.sendString("Nice! " + user.getFirstName() + " is correct with their guess of " + rreply.subreddit + " and now has an astounding " + score + " points!", chatId);
		}
		
		private void guessWrong(User user, long chatId, String guess) {
			if (guessesRemaining == 0) {
				gameState = STATE_GAME_OVER;
				TextServer.sendString("Sorry, the correct sub is " + rreply.subreddit, chatId);
				return;
			}
			guessesRemaining--;
			TextServer.sendString(user.getFirstName() + " is incorrect with their guess of " + guess + ".\nGuesses remaining: " + (guessesRemaining + 1), chatId);
		}
		
	}
	
	private void loadScores() throws JSONException {
		String json = "";
		try {
			json = FileHost.readFile(RANDIT_SCORES_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (json.equals(""))
			json = "{}";
		userScores = new JSONObject(json);
	}
	
	public void saveScores() {
		try {
			FileHost.writeFile(RANDIT_SCORES_PATH, userScores.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
