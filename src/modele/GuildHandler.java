package modele;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import main.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.OrmInstance;

@Entity
public class GuildHandler {
	@Id
	@Column(name = "guild_id")
	private String guildID;
	@OneToMany(orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	@MapKey(name = "channelID")
	private Map<String, Channel> channels = new HashMap<>();
	@Transient
	private Map<String, Player> players = new HashMap<>();

	public GuildHandler() {
		super();
	}

	public GuildHandler(String id) {
		super();
		this.guildID = id;
		if (!OrmInstance.objectExists(GuildHandler.class, guildID)) {
			OrmInstance.persist(this);
		}
	}

	private void addChannel(String channel) {
		if (channels.containsKey(channel))
			return;
		channels.put(channel, new Channel(channel));
		OrmInstance.persist(channels.get(channel));
		OrmInstance.update(this);
	}

	private void removeChannel(String channel) {
		if (!channels.containsKey(channel)) {
			System.out.println("not exist");
			return;
		}
		channels.remove(channel);
		OrmInstance.remove(Channel.class, channel);
		OrmInstance.update(this);
	}

	public void commandHandler(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			String msg = event.getMessage().getContentDisplay();
			switch (msg) {
			case ("!top"): {
				List<Player> top = OrmInstance.getObject(Player.class);
				Collections.sort(top);
				top=top.stream().limit(10).collect(Collectors.toList());
				System.out.println(top.size());
				String topPlayers=event.getAuthor().getAsMention()+"Top players:\n";
				for(Player p:top) {
					topPlayers+=p.getUsername()+" - Score: "+p.getHighestScore()+"\n";
				}
				System.out.println(topPlayers);
				event.getChannel().sendMessage(topPlayers).queue();
				break;
			}
			case ("!addchannel"): {
				if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR))
					addChannel(event.getChannel().getId());
				break;
			}
			case ("!removechannel"): {
				if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR))
					removeChannel(event.getChannel().getId());

				break;
			}
			case ("!play"): {
				if (!channels.containsKey(event.getChannel().getId()))
					break;
				if (channels.get(event.getChannel().getId()).isClearing())
					break;
				if (!players.containsKey(event.getAuthor().getId())) {
					if (!OrmInstance.objectExists(Player.class, event.getAuthor().getId()))
						players.put(event.getAuthor().getId(), new Player(event.getAuthor()));
					else
						players.put(event.getAuthor().getId(),
								OrmInstance.getObject(Player.class, event.getAuthor().getId()));
					players.get(event.getAuthor().getId()).setGuildHandler(this);
					Game g = new Game(players.get(event.getAuthor().getId()), event.getChannel());
					Main.getBot().addEventListener(g);
					Thread thread = new Thread(g);
					thread.start();
				}
				break;
			}
			case ("!clear"): {
				if (!channels.containsKey(event.getChannel().getId()))
					break;
				if (channels.get(event.getChannel().getId()).isClearing())
					break;
				if (players.isEmpty()) {
					channels.get(event.getChannel().getId()).setClearing(true);
					for (Message iter : event.getChannel().getIterableHistory()) {
						iter.delete().queue();
					}
				}
				channels.get(event.getChannel().getId()).setClearing(false);
				break;
			}
			case ("!help"): {
				if (!channels.containsKey(event.getChannel().getId()))
					break;
				event.getChannel().sendMessage("Voici les commandes disponible pour le moment "
						+ event.getAuthor().getAsMention()
						+ ":\n!top - Montre la liste des 10 meilleurs joueurs.\n!clear - Supprime tout les messages dans un channel.\n!play - Lance une partie de Whack a mole.\n--> Lors d'une partie de whack a mole vous pourrez gagner des point en assomant les taupe pour cela taper dans le chat le numero correspondant à la position de la taupe.\n Chaque taupe assomer vous donnera 15 points."
						+ "--> Si la taupe n'est pas encore sortie du trou et que vous la taper vous perdez 5 points."
						+ "\n!stop - Arrête la partie en cours.").queue();
				break;
			}
			}
			System.out.println(event.getAuthor());
		}
	}

	public Map<String, Player> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, Player> players) {
		this.players = players;
	}

	public String getGuildID() {
		return guildID;
	}

	public void setGuildID(String guildID) {
		this.guildID = guildID;
	}

}
