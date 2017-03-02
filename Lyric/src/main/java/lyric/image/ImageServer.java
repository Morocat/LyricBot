package lyric.image;

import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import lyric.PollingLyric;
import lyric.utils.Pair;

public class ImageServer {
	private final static ImageServer instance = new ImageServer();

	private ImageServer() {
	}

	public static ImageServer getInstance() {
		return instance;
	}

	private PollingLyric bot;

	public void initialize(PollingLyric bot) {
		this.bot = bot;
	}

	public void sendImageFromUrl(Pair<String, String> urls) throws TelegramApiException {
		if (urls == null)
			return;

		try {
			System.out.println("Sending long url: " + urls.first);
			sendImg(urls.first);
		} catch (Exception e) {
			// if long url doesn't work try the short one
			System.out.println("Sending short url: " + urls.second);
			sendImg(urls.second);
		}
	}
	
	private void sendImg(String url) throws TelegramApiException {
		if (url.endsWith("gif")) {
			SendDocument req = new SendDocument();
			req.setChatId(bot.getChatId());
			req.setDocument(url);
			bot.sendDocument(req);
		} else if (url.endsWith("gifv") || url.endsWith("mp4")) {
			SendVideo req = new SendVideo();
			req.setChatId(bot.getChatId());
			req.setVideo(url);
			bot.sendVideo(req);
		} else {
			SendPhoto req = new SendPhoto();
			req.setChatId(bot.getChatId());
			req.setPhoto(url);
			bot.sendPhoto(req);
		}
	}

}
