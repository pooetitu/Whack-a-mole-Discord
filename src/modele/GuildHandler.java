package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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
	private List<Channel> channels = new ArrayList<>();
	@Transient
	private Map<String, Player> players = new HashMap<>();
	@Transient
	private boolean clearing = false;

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
		if (channels.equals(channel))
			return;
		channels.add(new Channel(channel, this));
		OrmInstance.update(this);
	}

	private void removeChannel(String channel) {
		if (!channels.equals(channel))
			return;
		channels.remove(channel);
		OrmInstance.update(this);
	}

	public void commandHandler(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			String msg = event.getMessage().getContentDisplay();
			switch (msg) {
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
				if (!players.containsKey(event.getAuthor().getId()) && !clearing) {
					players.put(event.getAuthor().getId(), new Player(event.getAuthor(), this));
					Game g = new Game(players.get(event.getAuthor().getId()), event.getChannel());
					Main.getBot().addEventListener(g);
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
						+ "--> Si la taupe n'est pas sortie du trou et que vous la taper vous perdez 5 points"
						+ "\n!stop - Stop la partie en cours").queue();
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

	public boolean isClearing() {
		return clearing;
	}

	public void setClearing(boolean clearing) {
		this.clearing = clearing;
	}

}
