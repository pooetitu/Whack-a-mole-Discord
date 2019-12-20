package modele;

import main.Main;
import net.dv8tion.jda.api.entities.User;

public class Player {

	private User user;
	private int highestScore;

	public Player(User user) {
		super();
		this.user = user;
	}

	public void finish(int score) {
		if (score > highestScore) {
			highestScore = score;
		}
		Main.getPlayers().remove(user.getId());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getHighestScore() {
		return highestScore;
	}

	public void setHighestScore(int highestScore) {
		this.highestScore = highestScore;
	}

}
