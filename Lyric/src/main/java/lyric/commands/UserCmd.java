package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.servers.TextServer;

public class UserCmd extends BotCommand {

	public UserCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		String s = "Info about " + user.getFirstName();
		if (user.getLastName() != null && !user.getLastName().equals(""))
			s += " " + user.getLastName();
		s += ": ";
		s += "\nUsername: " + user.getUserName();
		s += "\nUser ID: " + user.getId();
		TextServer.sendString(s, chat.getId());
	}

}
