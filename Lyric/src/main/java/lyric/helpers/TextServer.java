package lyric.helpers;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TextServer {
	
	public static void sendString(String str, long chatId, AbsSender bot) {
		try {
			SendMessage message = new SendMessage().
					setChatId(chatId)
					.setText(str);
			bot.sendMessage(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
