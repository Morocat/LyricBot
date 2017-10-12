package lyric.servers;

import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import lyric.reddit.RedditReply;

public class ImageServer {
	
	private static AbsSender bot;
	
	public static void initialize(AbsSender bot) {
		ImageServer.bot = bot;
	}

	public static void sendImageFromUrl(RedditReply rreply, long chatId) throws TelegramApiException {
		if (rreply == null)
			return;

		for (String url : rreply.urls) {
			try {
				System.out.println("Sending url: " + url);
				sendImg(url, rreply.caption, chatId);
				return; // only retry if unsuccessful
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void sendImg(String url, String caption, long chatId) throws TelegramApiException {
		if (url.endsWith("gif")) {
			SendDocument req = new SendDocument();
			req.setCaption(caption);
			req.setChatId(chatId);
			req.setDocument(url);
			bot.sendDocument(req);
		} else if (url.endsWith("gifv") || url.endsWith("mp4")) {
			SendVideo req = new SendVideo();
			req.setCaption(caption);
			req.setChatId(chatId);
			req.setVideo(url);
			bot.sendVideo(req);
		} else {
			SendPhoto req = new SendPhoto();
			req.setCaption(caption);
			req.setChatId(chatId);
			req.setPhoto(url);
			bot.sendPhoto(req);
		}
	}

}
