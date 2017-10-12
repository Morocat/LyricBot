package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.admin.BotAdmin;
import lyric.admin.Nsfw;
import lyric.servers.TextServer;

public class NsfwCmd extends BotCommand {

	public NsfwCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		//System.out.println("Is user chat: " + chat.isUserChat());
		//System.out.println("Is group chat: " + chat.isGroupChat());
		if (arguments == null || arguments.length == 0) {
			TextServer.sendString("NSFW mode is " + (Nsfw.getInstance().isChatAllowNsfw(chat.getId()) ? "on" : "off"), chat.getId());
			return;
		}
		if (!BotAdmin.getInstance().isUserAdmin(absSender, chat.getId(), user.getId(), chat.isUserChat())) {
			TextServer.sendString("Only bot admins may use that command", chat.getId());
			return;
		}
		if (arguments == null || arguments.length < 1)
			return;
		arguments[0] = arguments[0].toLowerCase().trim();
		boolean setting;
		if (arguments[0].equals("on"))
			setting = true;
		else if (arguments[0].equals("off"))
			setting = false;
		else {
			TextServer.sendString("Invalid parameter", chat.getId());
			return;
		}
			
		Nsfw.getInstance().setNsfw(chat.getId(), setting);
		TextServer.sendString("NSFW mode turned " + (setting ? "on" : "off"), chat.getId());
	}

}
