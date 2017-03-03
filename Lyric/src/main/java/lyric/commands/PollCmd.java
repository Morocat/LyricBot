package lyric.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class PollCmd extends BotCommand {
	
	
	
	public PollCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}
	
	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (arguments == null || arguments.length == 0)
			return;
		if (arguments[0].equals("start"))
	}

	/**
	 * Initialize a new poll
	 */
	public void createPoll() {
		
	}
	
	public void respondToPoll() {
		// 
	}

	

}
