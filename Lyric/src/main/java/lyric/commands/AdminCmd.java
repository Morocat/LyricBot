package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.admin.BotAdmin;
import lyric.servers.TextServer;

public class AdminCmd extends BotCommand {

	public AdminCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender bot, User user, Chat chat, String[] arguments) {
		if (arguments == null || arguments.length < 1 || arguments[0].length() == 0) {
			TextServer.sendString("Invalid parameter", chat.getId());
			return;
		}
		if (BotAdmin.getInstance().isUserAdmin(bot, chat.getId(), user.getId()))
			BotAdmin.getInstance().addAdmin(arguments[0], chat.getId());
	}

}
