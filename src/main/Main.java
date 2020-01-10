package main;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import modele.GuildHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {
	private static Map<String, GuildHandler> guilds = new HashMap<>();
	private static JDA bot;
	private final static String token = "NjU3MzQ1MzQ4MTQxODQyNDMy.Xf1uGA.yJX4nW4CAlM4Kun7__PpSzk1AoA";

	public static void main(String[] args) throws LoginException {
		bot = new JDABuilder(token).setActivity(Activity.playing("!help pour plus d'infos"))
				.addEventListeners(new Main()).build();
		for (Guild iter : bot.getGuilds()) {
			guilds.put(iter.getId(), new GuildHandler(iter.getId()));
		}

	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		super.onGuildJoin(event);
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			if (guilds.containsKey(event.getGuild().getId())) {
				guilds.get(event.getGuild().getId()).commandHandler(event);
				;
			}
		}

	}

	public static JDA getBot() {
		return bot;
	}

}
