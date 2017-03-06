package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.servers.TextServer;

public class ChatCmd extends BotCommand {

	public ChatCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		String chatInfo = "Chat id: " + chat.getId() + "\nisChannel: " + chat.isChannelChat() + "\nisGroup: " + chat.isGroupChat() + "\nisSuperGroup: " + chat.isSuperGroupChat() + "\nisUser: " + chat.isUserChat();
		TextServer.sendString(chatInfo, chat.getId());
	}

}
