package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.helpers.TextServer;

public class EchoCmd extends BotCommand {

	public EchoCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (arguments == null || arguments.length == 0)
			return;
		String s = "";
		for (String a : arguments)
			s += a + " ";
		TextServer.sendString(s, chat.getId(), absSender);
	}

}
