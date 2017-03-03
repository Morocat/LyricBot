package lyric.helpers;

import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import lyric.utils.Pair;

public class ImageServer {

	public static void sendImageFromUrl(Pair<String, String> urls, long chatId, AbsSender bot) throws TelegramApiException {
		if (urls == null)
			return;

		try {
			System.out.println("Sending long url: " + urls.first);
			sendImg(urls.first, chatId, bot);
		} catch (Exception e) {
			// if long url doesn't work try the short one
			System.out.println("Sending short url: " + urls.second);
			sendImg(urls.second, chatId, bot);
		}
	}
	
	private static void sendImg(String url, long chatId, AbsSender bot) throws TelegramApiException {
		if (url.endsWith("gif")) {
			SendDocument req = new SendDocument();
			req.setChatId(chatId);
			req.setDocument(url);
			bot.sendDocument(req);
		} else if (url.endsWith("gifv") || url.endsWith("mp4")) {
			SendVideo req = new SendVideo();
			req.setChatId(chatId);
			req.setVideo(url);
			bot.sendVideo(req);
		} else {
			SendPhoto req = new SendPhoto();
			req.setChatId(chatId);
			req.setPhoto(url);
			bot.sendPhoto(req);
		}
	}

}
