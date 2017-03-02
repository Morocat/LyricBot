package lyric;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import lyric.games.hangman.Hangman;
import lyric.image.ImageServer;
import lyric.meme.MemeGenerator;
import lyric.misc.MiscCommands;
import lyric.parsing.BaseCommandParser;
import lyric.poll.Poll;
import lyric.skynet.SentenceGenerator;

public class PollingLyric extends TelegramLongPollingCommandBot {
	private final BaseCommandParser parser = new BaseCommandParser(this);
	
	private Long chatId = 0L;
	
	public PollingLyric() {
		//conversationStarter.start();
		Hangman.getInstance().initialize(this);
		MiscCommands.getInstance().initialize(this);
		ImageServer.getInstance().initialize(this);
		MemeGenerator.getInstance().initialize(this);
		SentenceGenerator.getInstance().initialize(this);
		Poll.getInstance().initialize(this);
		System.out.println("Initialization done");
	}
	
	private final Thread conversationStarter = new Thread(new Runnable() {

		@Override
		public void run() {
			while (!Thread.interrupted()) {
				if (chatId != 0)
					sendString("Are you from Tennesse?\nCause you're the only ten I see!!", chatId);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	});

	/*@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			chatId = update.getMessage().getChatId();
			parser.parseInput(update);
	    }
	}*/

	@Override
	public String getBotUsername() {
		return "Lyric";
	}

	@Override
	public String getBotToken() {
		return "351737494:AAE7ie6cqDgG-Jj28WwxXZvcmSFTqHlidyg";
	}
	
	public void sendString(String str) {
		try {
			SendMessage message = new SendMessage().
					setChatId(chatId)
					.setText(str);
			sendMessage(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	public void sendString(String str, long chatId) {
		try {
			SendMessage message = new SendMessage().
					setChatId(chatId)
					.setText(str);
			sendMessage(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	public long getChatId() {
		return chatId;
	}

	@Override
	public void processNonCommandUpdate(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			chatId = update.getMessage().getChatId();
			parser.parseInput(update);
	    }
	}

}
