package lyric.commands;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.servers.FileHost;
import lyric.servers.TextServer;

public class GreetingCmd extends BotCommand {
	private final static String GREETING_FILE_PATH = "greetings.txt";
	private final static String DEFAULT_GREETING = "Good morning cutie~ You're looking fantastic today!";
	
	/**
	 * userGreetings
	 * 	key  ->userId
	 *  value->JSONObject
	 *  		key  ->greeting
	 *  		value->String
	 *  		key  ->hour
	 *  		value->int
	 *  		key  ->minute
	 *  		value->int
	 *  		key  ->active
	 *  		value->boolean
	 */
	private JSONObject userGreetings;

	public GreetingCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
		loadGreetings();
		greetingThread.start();
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		String greeting = "";
		
		if (arguments == null || arguments.length == 0) {
			greeting = DEFAULT_GREETING;
		} else {
			if (arguments[0].equals("time")) {
				setTimeOfGreeting(user.getId(), arguments);
				return;
			} else if (arguments[0].equals("default")){
				greeting = DEFAULT_GREETING;
			} else if (arguments[0].equals("stop")) {
				stopGreeting(user.getId());
			} else {
				for (String s : arguments) {
					greeting += s + " ";
				}
			}
		}
		setGreeting(user.getId(), greeting);
	}
	
	private void setGreeting(int userId, String greeting) {
		JSONObject o = getUserObject(userId);
		o.put("greeting", greeting);
		o.put("active", true);
		userGreetings.put(String.valueOf(userId), o);
		saveGreetings();
		TextServer.sendString("Greeting has been set", userId);
	}
	
	// /greeting time 0830
	private void setTimeOfGreeting(int userId, String[] args) {
		if (args.length > 1 && args[1].length() == 4) {
			int hour = Integer.parseInt(args[1].substring(0, 2));
			int minute = Integer.parseInt(args[1].substring(2, 4));
			JSONObject o = getUserObject(userId);
			o.put("hour", hour);
			o.put("minute", minute);
			o.put("active", true);
			saveGreetings();
			TextServer.sendString("Greeting set for " + args[1] + " each day.", userId);
		} else {
			TextServer.sendString("Incorrect time format", userId);
		}
	}
	
	private void stopGreeting(int userId) {
		JSONObject o = getUserObject(userId);
		o.put("active", false);
	}
	
	private JSONObject getUserObject(int userId) {
		JSONObject o = userGreetings.optJSONObject(String.valueOf(userId));
		if (o == null) {
			o = new JSONObject();
		}
		return o;
	}
	
	private void loadGreetings() {
		String json = "";
		try {
			json = FileHost.readFile(GREETING_FILE_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (json.equals(""))
			json = "{}";
		userGreetings = new JSONObject(json);
	}
	
	private void saveGreetings() {
		try {
			FileHost.writeFile(GREETING_FILE_PATH, userGreetings.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private final Thread greetingThread = new Thread(new Runnable() {
		@Override
		public void run() {
			HashMap<String, Boolean> latches = new HashMap<>();
			Date date;
			JSONObject o;
			int hour, minute;
			while (!Thread.interrupted()) {
				try {
					date = new Date();
					for (String userId : userGreetings.keySet()) {
						o = userGreetings.getJSONObject(userId);
						if (!o.has("greeting") || !o.has("hour") || !o.has("minute") || !o.has("active"))
							continue;
						if (!o.getBoolean("active"))
							continue;
						if (!latches.containsKey(userId))
							latches.put(userId, false);
						boolean alreadyTriggered = latches.get(userId);
						
						hour = o.getInt("hour");
						minute = o.getInt("minute");
						if (date.getHours() == hour && date.getMinutes() == minute) {
							if (!alreadyTriggered) {
								latches.put(userId, true);
								TextServer.sendString(o.getString("greeting"), Integer.parseInt(userId));
							}
						} else
							latches.put(userId, false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	});

}
