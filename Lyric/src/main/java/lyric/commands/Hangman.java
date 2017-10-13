package lyric.commands;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.games.hangman.WordList;
import lyric.servers.TextServer;

public class Hangman extends BotCommand {
	
	private final static int STATE_GAME_OVER = 0, STATE_PLAYING = 1;
	private final static int MAX_GUESSES = 10;
	
	private int state = STATE_GAME_OVER;
	private String wordToGuess;
	private List<Character> guesses = new ArrayList<>();
	private int guessesRemaining; // number of incorrect guesses remaining
	
	public Hangman(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}
	
	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (state != STATE_GAME_OVER) {
			TextServer.sendString("Hangman is already running", chat.getId());
			return;
		}
		state = STATE_PLAYING;
		guessesRemaining = MAX_GUESSES;
		guesses.clear();
		wordToGuess = WordList.getRandomWord();
		TextServer.sendString(formatUi(), chat.getId());
	}
	
	private void onGameEnd(boolean victory, long chatId) {
		state = STATE_GAME_OVER;
		if (victory) {
			TextServer.sendString("You win!", chatId);
		} else {
			String w = "You lose!\n\nThe word was: ";
			for (int i = 0; i < wordToGuess.length(); i++)
				w += wordToGuess.charAt(i);
			TextServer.sendString(w, chatId);
		}
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
	
	public class GuessCmd extends BotCommand {

		public GuessCmd(String commandIdentifier, String description) {
			super(commandIdentifier, description);
		}

		@Override
		public void execute(AbsSender absSender, User user, Chat chat, String[] params) {
			if (state != STATE_PLAYING || params == null || params.length == 0)
				return;
			params[0] = params[0].toLowerCase();
			if (params[0].length() > 1) { // guess the whole word
				if (params[0].equals(wordToGuess))
					onGameEnd(true, chat.getId());
				else {
					guessesRemaining--;
					TextServer.sendString(formatUi(), chat.getId());
					if (checkDefeat())
						onGameEnd(false, chat.getId());
				}
				return;
			}
			Character c = params[0].charAt(0);
			if (!Character.isLetter(c)) {
				TextServer.sendString("Invalid guess", chat.getId());
				return;
			}
			if (guesses.contains(c)) {
				TextServer.sendString("You already guessed that letter!", chat.getId());
			} else {
				guesses.add(c);
				if (!wordToGuess.contains(params[0]))
					guessesRemaining--;
				TextServer.sendString(formatUi(), chat.getId());
				if (checkVictory())
					onGameEnd(true, chat.getId());
				else if (checkDefeat())
					onGameEnd(false, chat.getId());
			}
		}
		
	}

}
