package modele;

import java.util.Random;

public class GameLogic {
	private boolean[] moles = new boolean[3];
	private int score;
	private String line2="Press 1, 2 or 3 to hit the mole";

	public GameLogic() {
		super();
		score = 0;
	}

	public void turn() {
		Random r = new Random();
		int randNb = r.nextInt((2 - 0) + 1) + 0;
		if (!moles[randNb]) {
			moles[randNb] = true;
		} else {
			moles[randNb] = false;
			score -= 10;
			line2= "```diff\r\n" + "Score -10\r\n" + "```";
		}
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

	public boolean checkingAction(String action) {
		if (action.equals("1") || action.equals("2") || action.equals("3")) {
			int index = Integer.valueOf(action) - 1;
			if (moles[index]) {
				score += 15;
				moles[index] = false;
				line2 = "```css\r\n" + "Score +15\r\n" + "```";
			} else {
				score -= 5;
				line2 = "```diff\r\n" + "Score -5\r\n" + "```";
			}
			return true;
		}
		line2="Press 1, 2 or 3 to hit the mole";
		return false;
	}

	public String messageDisplay() {
		return line2 + "\n" + moleDisplay();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
