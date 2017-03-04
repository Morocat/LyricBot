package lyric.servers;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TextServer {
	
	private static AbsSender bot;
	
	public static void initialize(AbsSender bot) {
		TextServer.bot = bot;
	}
	
	public static void sendString(String str, long chatId) {
		try {
			SendMessage message = new SendMessage().
					setChatId(chatId)
					.setText(str);
			bot.sendMessage(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(SendMessage m, long chatId) {
		try {
			bot.sendMessage(m);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
