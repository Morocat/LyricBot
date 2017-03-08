package lyric.commands;

import java.util.Random;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import lyric.servers.TextServer;

public class DiceCmd extends BotCommand {
	
	private final static Random rand = new Random();

	public DiceCmd(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		if (arguments == null || arguments.length != 1) {
			TextServer.sendString("Invalid parameter", chat.getId());
			return;
		}
		try {
			String[] spl = arguments[0].split("d");
			int diceCount, sides;
			if (spl[0].equals(""))
				diceCount = 1;
			else
				diceCount = Integer.parseInt(spl[0]);
			
			sides = Integer.parseInt(spl[1]);
			String s = user.getFirstName() + " rolls " + arguments[0] + ":\n";
			for (int i = 0; i < diceCount; i++)
				s += String.valueOf(rand.nextInt(sides) + 1) + ", ";
			s = s.substring(0, s.length() - 2);
			TextServer.sendString(s, chat.getId());
		} catch (Exception e) {
			TextServer.sendString("Invalid parameter", chat.getId());
		}
	}

}
