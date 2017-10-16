package lyric.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import lyric.servers.FileHost;

public class BotAdmin {
	private final static BotAdmin instance = new BotAdmin();
	private BotAdmin(){
		loadAdmins();
	}
	public static BotAdmin getInstance() {
		return instance;
	}
	
	private final static String BOT_ADMINS_FILE_PATH = "Bot Admins.txt";
	
	// <ChatId, UserIds>
	private HashMap<Long, List<Integer>> admins = new HashMap<>();
	
	public void loadAdmins() {
		try {
			JSONArray arr = getAdmins();
			if (arr == null)
				return;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				JSONArray val = o.getJSONArray("value");
				List<Integer> list = new ArrayList<>();
				for (int j = 0; j < val.length(); j++)
					list.add(val.getInt(j));
				admins.put(o.getLong("key"), list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private JSONArray getAdmins() throws JSONException {
		String json = "";
		try {
			json = FileHost.readFile(BOT_ADMINS_FILE_PATH);
			/*FileReader fr = new FileReader(new File(FILENAME));
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null)
				json += line;
			br.close();*/
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new JSONArray(json);
	}
	
	public void saveAdmins() throws IOException {
		JSONArray arr = new JSONArray();
		for (Map.Entry<Long, List<Integer>> en : admins.entrySet()) {
			JSONObject o = new JSONObject();
			o.put("key", en.getKey());
			o.put("value", en.getValue());
			arr.put(o);
		}
		FileHost.writeFile(BOT_ADMINS_FILE_PATH, arr.toString());
		/*BufferedWriter bw = new BufferedWriter(new FileWriter(BOT_ADMINS_FILE_PATH));
		bw.write(arr.toString());
		bw.close();*/
	}
	
	public boolean isUserAdmin(AbsSender bot, long chatId, int userId, boolean isUserChat) {
		if (isUserChat)
			return true;
		if (userId == 114800779) // that's me! @Morororo
			return true;
		List<Integer> adminList = getChatAdmins(chatId, bot);
		if (adminList == null)
			adminList = new ArrayList<>();
		if (admins.get(chatId) != null)
			adminList.addAll(admins.get(chatId));
		return adminList.contains(userId);
	}
	
	private List<Integer> getChatAdmins(long chatId, AbsSender bot) {
		try {
			GetChatAdministrators g = new GetChatAdministrators();
			g.setChatId(chatId);
			List<Integer> admins = new ArrayList<>();
			for (ChatMember cm : bot.getChatAdministrators(g))
				admins.add(cm.getUser().getId());
			return admins;
		} catch (TelegramApiException e) {
			//e.printStackTrace();
			return null;
		}
	}
	
	public void addAdmin(String userId, long chatId) {
		if (admins.get(chatId) == null)
			admins.put(chatId, new ArrayList<Integer>());
		admins.get(chatId).add(Integer.parseInt(userId));
		try {
			saveAdmins();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
