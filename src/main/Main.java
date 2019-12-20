package main;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import modele.Game;
import modele.Player;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {
//	private boolean playing;
	private static Map<String, Player> players = new HashMap<>();
	private static JDA bot;

	public static void main(String[] args) throws LoginException {
		bot = new JDABuilder("NjU3MzQ1MzQ4MTQxODQyNDMy.Xf059A.pocnglP6v-YXfClmrgwmLP_SRCc")
				.setActivity(Activity.playing("!play, !clear")).addEventListeners(new Main()).build();

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			String msg = event.getMessage().getContentDisplay();
			switch (msg) {
			case ("!play"): {
				if (!players.containsKey(event.getAuthor().getId())) {
					players.put(event.getAuthor().getId(), new Player(event.getAuthor()));
					Game g = new Game(players.get(event.getAuthor().getId()), event.getChannel());
					bot.addEventListener(g);
					Thread thread = new Thread(g);
					thread.start();
				}
				break;
			}
			case ("!clear"): {
				for (Message iter : event.getChannel().getIterableHistory()) {
					iter.delete().queue();
				}
				break;
			}
			}
			System.out.println(event.getAuthor());
		}
	}

	public static Map<String, Player> getPlayers() {
		return players;
	}

	public static JDA getBot() {
		return bot;
	}

}
