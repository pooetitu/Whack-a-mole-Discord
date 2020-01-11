package modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import net.dv8tion.jda.api.entities.User;
import utils.OrmInstance;

@Entity
public class Player implements Comparable<Player> {
	@Id
	private String userID;
	@Column
	private String username;
	@Column
	private String usertag;
	@Column
	private int highestScore;
	@Transient
	private GuildHandler guildHandler;

	public Player() {
		super();
	}

	public Player(User user) {
		super();
		this.userID = user.getId();
		this.username = user.getName();
		this.usertag = user.getAsMention();
		OrmInstance.persist(this);
	}

	public void finish(int score) {
		if (score > highestScore) {
			highestScore = score;
		}
		OrmInstance.update(this);
		guildHandler.getPlayers().remove(userID);
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertag() {
		return usertag;
	}

	public void setUsertag(String usertag) {
		this.usertag = usertag;
	}

	public int getHighestScore() {
		return highestScore;
	}

	public void setHighestScore(int highestScore) {
		this.highestScore = highestScore;
	}

	public GuildHandler getGuildHandler() {
		return guildHandler;
	}

	public void setGuildHandler(GuildHandler guildHandler) {
		this.guildHandler = guildHandler;
	}

	@Override
	public int compareTo(Player o) {
		if (o.getHighestScore() == highestScore)
			return 0;
		else if (o.getHighestScore() < highestScore)
			return 1;
		else
			return -1;

	}

}
