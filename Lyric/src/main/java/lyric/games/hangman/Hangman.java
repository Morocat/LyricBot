package lyric.games.hangman;

import java.util.*;

import lyric.PollingLyric;

public class Hangman {
	
	private final static int STATE_GAME_OVER = 0, STATE_PLAYING = 1;
	private final static int MAX_GUESSES = 10;
	
	private final static Hangman instance = new Hangman();
	private Hangman() {}
	
	public static Hangman getInstance() {
		return instance;
	}
	private PollingLyric bot;
	
	private int state = STATE_GAME_OVER;
	private String wordToGuess;
	private List<Character> guesses = new ArrayList<>();
	private int guessesRemaining; // number of incorrect guesses remaining
	
	public void initialize(PollingLyric bot) {
		this.bot = bot;
	}
	
	public void start() {
		if (state != STATE_GAME_OVER) {
			bot.sendString("Hangman is already running");
			return;
		}
		state = STATE_PLAYING;
		guessesRemaining = MAX_GUESSES;
		guesses.clear();
		wordToGuess = WordList.getRandomWord();
		bot.sendString(formatUi());
	}
	
	public void guess(String[] params) {
		if (state != STATE_PLAYING || params == null || params.length == 0 || params[0].length() > 1)
			return;
		params[0] = params[0].toLowerCase();
		Character c = params[0].charAt(0);
		if (!Character.isLetter(c)) {
			bot.sendString("Invalid guess");
			return;
		}
		if (guesses.contains(c)) {
			bot.sendString("You already guessed that letter!");
		} else {
			guesses.add(c);
			if (!wordToGuess.contains(params[0]))
				guessesRemaining--;
			bot.sendString(formatUi());
			if (checkVictory())
				onGameEnd(true);
			else if (checkDefeat())
				onGameEnd(false);
		}
	}
	
	private void onGameEnd(boolean victory) {
		state = STATE_GAME_OVER;
		if (victory) {
			bot.sendString("You win!");
		} else
			bot.sendString("You lose!");
	}
	
	private boolean checkVictory() {
		for (int i = 0; i < wordToGuess.length(); i++)
			if (!guesses.contains(wordToGuess.charAt(i)))
				return false;
		return true;
	}
	
	private boolean checkDefeat() {
		if (guessesRemaining == 0)
			return true;
		return false;
	}

	private String formatUi() {
		String s = "";
		for (int i = 0; i < wordToGuess.length(); i++) {
			if (guesses.contains(wordToGuess.charAt(i)))
				s += wordToGuess.charAt(i);
			else
				s += "_";
			s += " ";
		}
		s += "\n\n";
		for (int i = 0; i < guesses.size(); i++) {
			if (!wordToGuess.contains(guesses.get(i) + ""))
				s += guesses.get(i) + " ";
		}
		s += "\nGuesses left: " + guessesRemaining;
		return s;
	}

}
