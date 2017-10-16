package lyric.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lyric.servers.FileHost;

public class Nsfw {
	private final static Nsfw instance = new Nsfw();
	private Nsfw(){
		loadNsfw();
	}
	public static Nsfw getInstance() {
		return instance;
	}
	
	private final static String NSFW_FILE_PATH = "NSFW.txt";
	
	private HashMap<Long, Boolean> nsfwMap = new HashMap<>();
	
	public void loadNsfw() {
		try {
			JSONArray arr = getNsfw();
			if (arr == null)
				return;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				nsfwMap.put(o.getLong("key"), o.getBoolean("value"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private JSONArray getNsfw() throws JSONException {
		String json = "";
		try {
			json = FileHost.readFile(NSFW_FILE_PATH);
			/*FileReader fr = new FileReader(new File(NSFW_FILE_PATH));
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
	
	public void saveNsfw() throws IOException {
		JSONArray arr = new JSONArray();
		for (Map.Entry<Long, Boolean> en : nsfwMap.entrySet()) {
			JSONObject o = new JSONObject();
			o.put("key", en.getKey());
			o.put("value", en.getValue());
			arr.put(o);
		}
		FileHost.writeFile(NSFW_FILE_PATH, arr.toString());
		/*BufferedWriter bw = new BufferedWriter(new FileWriter(NSFW_FILE_PATH));
		bw.write(arr.toString());
		bw.close();*/
	}
	
	public boolean isChatAllowNsfw(long chatId) {
		if (nsfwMap.get(chatId) == null)
			return false;
		return nsfwMap.get(chatId);
	}
	
	public void setNsfw(long chatId, boolean nsfw) {
		nsfwMap.put(chatId, nsfw);
		try {
			saveNsfw();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
