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
	private final static String token = "NjU3MzQ1MzQ4MTQxODQyNDMy.Xf1uGA.yJX4nW4CAlM4Kun7__PpSzk1AoA";
	private static boolean clearing = false;

	public static void main(String[] args) throws LoginException {
		bot = new JDABuilder(token).setActivity(Activity.playing("!help pour plus d'infos"))
				.addEventListeners(new Main()).build();

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			String msg = event.getMessage().getContentDisplay();
			switch (msg) {
			case ("!play"): {
				if (!players.containsKey(event.getAuthor().getId()) && !clearing) {
					players.put(event.getAuthor().getId(), new Player(event.getAuthor()));
					Game g = new Game(players.get(event.getAuthor().getId()), event.getChannel());
					bot.addEventListener(g);
					Thread thread = new Thread(g);
					thread.start();
				}
				break;
			}
			case ("!clear"): {
				if (players.isEmpty()) {
					clearing = true;
					for (Message iter : event.getChannel().getIterableHistory()) {
						iter.delete().queue();
					}
				}
				clearing = false;
				break;
			}
			case ("!help"): {
				event.getChannel().sendMessage("Voici les commandes disponible pour le moment "
						+ event.getAuthor().getAsMention()
						+ "\n!clear - Supprime tout les messages dans le channel\n!play - Lance une partie de Whack a mole\n--> Lors d'une partie de whack a mole vous pourrez gagner des point en tapant sur les taupe pour cela taper dans le chat le numero correspondant a la position de la taupe.\n Chaque taupe taper vous donnera 15 points."
						+ "--> Si la taupe n'est pas sortie du trou et que vous la tapez vous perdez 5 points"
						+ "\n!stop - arrete la partie en cours").queue();
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
