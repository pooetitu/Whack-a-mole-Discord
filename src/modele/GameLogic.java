package modele;

import java.util.Random;

public class GameLogic {
	private boolean[] moles = new boolean[3];;
	private int score;

	public GameLogic() {
		super();
		score = 0;
	}

	public void turn() {
		Random r = new Random();
		int randNb = r.nextInt((2 - 0) + 1) + 0;
		moles[randNb] = true;
	}

	public String moleDisplay() {
		String mole = "<:Mole:657652684073205760>";
		String hole = ":hole:";
		String str = "";
		for (boolean b : moles) {
			if (b) {
				str += mole;
			} else {
				str += hole;
			}
		}
		return str;
	}

	public String checkingAction(String action) {
		int index = Integer.valueOf(action) - 1;
		if (moles[index]) {
			score += 15;
			moles[index] = false;
			return "```css\r\n" + "Score +15\r\n" + "```";
		} else {
			score -= 5;
			return "```diff\r\n" + "Score -5\r\n" + "```";
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
