package modele;

import net.dv8tion.jda.api.entities.User;

public class Player {
	private String userID;
	private String username;
	private String usertag;
	private int highestScore;
	private transient GuildHandler guildHandler;

	public Player(User user, GuildHandler gh) {
		super();
		this.userID = user.getId();
		this.username = user.getName();
		this.usertag = user.getAsMention();
		guildHandler = gh;
	}

	public void finish(int score) {
		if (score > highestScore) {
			highestScore = score;
		}
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

}
