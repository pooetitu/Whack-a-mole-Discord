package main;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import modele.Player;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {
	private static Map<String, Player> players = new HashMap<>();

	public static void main(String[] args) throws LoginException {
		new JDABuilder("NjU3MzQ1MzQ4MTQxODQyNDMy.Xf0asg.2MhmTdML8u0YyetyjyKHY49SMfk").addEventListeners(new Main())
				.setActivity(Activity.playing("§help,§play")).build();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			String msg = event.getMessage().getContentDisplay();
			switch (msg) {
			case ("§play"): {
				if (!players.containsKey(event.getAuthor().getId())) {
					players.put(event.getAuthor().getId(), new Player()).play(event);
					event.getMessage().delete().queue();
				}
				break;
			}
			case ("§clear"): {
				for (Message iter : event.getChannel().getIterableHistory()) {
					iter.delete().queue();
				}
				break;
			}
			}
			System.out.println(event.getAuthor());
		}
	}

}
