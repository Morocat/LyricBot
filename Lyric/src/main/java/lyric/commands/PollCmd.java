package lyric.commands;

import static lyric.poll.Poll.STATE_BUILDING_QUESTION;
import static lyric.poll.Poll.STATE_BUILDING_RESPONSES;
import static lyric.poll.Poll.STATE_INACTIVE;
import static lyric.poll.Poll.STATE_RUNNING;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.poll.Poll;
import lyric.servers.TextServer;

public class PollCmd extends BotCommand {
	private final static String FILENAME = "Poll States.txt";
	
	private HashMap<Long, Poll> polls = new HashMap<>();
	
	public PollCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
		loadPolls();
	}
	
	public void loadPolls() {
		try {
			JSONArray arr = new JSONArray(loadFile());
			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				polls.put(o.getLong("key"), new Poll(o.getJSONObject("value")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void savePolls() {
		JSONArray arr = new JSONArray();
		for (Map.Entry<Long, Poll> en : polls.entrySet()) {
			JSONObject o = new JSONObject();
			o.put("key", en.getKey());
			o.put("value", en.getValue().toJson());
			arr.put(o);
		}
		try {
			writeFile(arr.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized String loadFile() throws IOException {
		FileReader fr = new FileReader(new File(FILENAME));
		BufferedReader br = new BufferedReader(fr);
		String s = "", line;
		while((line = br.readLine()) != null)
			s += line + "\n";
		br.close();
		return s;
	}
	
	private synchronized void writeFile(String str) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME));
		bw.write(str);
		bw.close();
	}
	
	@Override
	public void execute(AbsSender bot, User user, Chat chat, String[] arguments) {
		if (arguments == null || arguments.length == 0) {
			displayPoll(chat.getId(), user.getUserName());
		} else if (arguments[0].equals("start")) {
			createPoll(chat.getId());
		} else if (arguments[0].equals("done")) {
			startPoll(chat.getId());
		} else if (arguments[0].equals("end")) {
			endPoll(chat.getId());
		} else if (arguments[0].equals("rm")) {
			if (arguments.length < 2)
				TextServer.sendString("Missing parameter", chat.getId());
			else
				removePollOption(arguments[1], chat.getId());
		} else if (arguments[0].equals("results")) {
			displayResults(chat.getId(), user.getId());
		}
	}

	private void createPoll(long chatId) {
		Poll poll = polls.get(chatId);
		if (poll != null && poll.state != STATE_INACTIVE) {
			TextServer.sendString("A poll is already either being built or currently running", chatId);
			return;
		}
		polls.put(chatId, new Poll());
		TextServer.sendString("Let's create a new poll. First send me the question.", chatId);
	}
	
	public void setPollQuestion(String msg, long chatId) {
		Poll poll = polls.get(chatId);
		if (poll != null && poll.state != STATE_BUILDING_QUESTION) {
			TextServer.sendString("You must be building a poll to use this command", chatId);
			return;
		}
		poll.setQuestion(msg);
		TextServer.sendString("Creating a new poll: '" + msg + "'" + "\nNow send me the first response option.", chatId);
	}
	
	public void addPollOption(String opt, long chatId) {
		Poll p = polls.get(chatId);
		if (p == null || p.state != STATE_BUILDING_RESPONSES) {
			TextServer.sendString("You must be building a poll to use this command", chatId);
			return;
		}
		p.addOption(opt);
		String s = p.buildPollString();
		s += "\n\nWhen you've added enough options send '/poll done' to publish the poll";
		TextServer.sendString(s, chatId);
	}
	
	private void removePollOption(String opt, long chatId) {
		Poll p = polls.get(chatId);
		if (p == null || p.state != STATE_BUILDING_RESPONSES) {
			TextServer.sendString("You must be building a poll to use this command", chatId);
			return;
		}
		String r = p.removeOption(opt.toLowerCase().charAt(0) - 'a');
		TextServer.sendString(r, chatId);
	}
	
	private void startPoll(long chatId) {
		Poll p = polls.get(chatId);
		if (p == null || p.state != STATE_BUILDING_RESPONSES) {
			TextServer.sendString("You must be building a poll to use this command", chatId);
			return;
		}
		p.publishPoll();
		TextServer.sendString("Poll is now running!", chatId);
		savePolls();
	}
	
	private void endPoll(long chatId) {
		Poll p = polls.get(chatId);
		if (p == null || p.state != STATE_RUNNING) {
			TextServer.sendString("A poll is not currently running", chatId);
			return;
		}
		p.endPoll(chatId);
	}
	
	private void displayPoll(long chatId, String username) {
		Poll p = polls.get(chatId);
		if (p == null || p.state != STATE_RUNNING) {
			TextServer.sendString("A poll is not currently running", chatId);
			return;
		}
		TextServer.sendString(p.buildPollString(), chatId);
		sendCustomKeyboard(chatId, username);
	}
	
	private void displayResults(long chatId, int userId) {
		Poll p = polls.get(chatId);
		if (p == null || (p.state != STATE_RUNNING && p.state != STATE_INACTIVE)) {
			TextServer.sendString("A poll is not currently running", chatId);
			return;
		}
		p.displayResults(chatId, userId);
	}

	public Poll getPoll(long chatId) {
		return polls.get(chatId);
	}
	
	private void sendCustomKeyboard(long chatId, String username) {
		Poll p = polls.get(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("@" + username + " Select a response");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboad(true);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (String opt : p.options) {
        	KeyboardRow row = new KeyboardRow();
        	row.add(opt);
        	keyboard.add(row);
        }
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        TextServer.sendMessage(message, chatId);
    }

	public void recordResponse(String msg, long chatId, User user) {
		if (polls.get(chatId).recordResponse(chatId, user, msg))
			savePolls();
	}
	
}
