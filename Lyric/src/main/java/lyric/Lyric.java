package lyric;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Lyric extends TelegramWebhookBot {

	@Override
	public String getBotPath() {
		return "https://api.telegram.org/bot351737494:AAE7ie6cqDgG-Jj28WwxXZvcmSFTqHlidyg";
	}

	@Override
	public String getBotUsername() {
		return "Lyric";
	}

	@Override
	public BotApiMethod onWebhookUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
	        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
	                .setChatId(update.getMessage().getChatId())
	                .setText(update.getMessage().getText());
	        try {
	            sendMessage(message); // Call method to send the message
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	        return message;
	    }
		return null;
	}

	@Override
	public String getBotToken() {
		return "351737494:AAE7ie6cqDgG-Jj28WwxXZvcmSFTqHlidyg";
	}

}
