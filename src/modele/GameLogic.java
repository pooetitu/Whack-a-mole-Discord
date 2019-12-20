package modele;

import java.util.Random;

public class GameLogic {
	private boolean[] moles=new boolean[3];;
	private int score;

	public GameLogic() {
		super();
		score = 0;
	}

	public void turn() {
		Random r = new Random();
		int randNb = r.nextInt((2 - 0) + 1) + 0;
		moles[randNb]=true;
	}

	public String moleDisplay() {
		String mole = "<:Mole:657652684073205760>";
		String hole = ":hole:";
		String str = "";
		if (moles[0]) {
			str += mole;
		} else {
			str += hole;
		}
		if (moles[1]) {
			str += mole;
		} else {
			str += hole;
		}
		if (moles[2]) {
			str += mole;
		} else {
			str += hole;
		}
		return str;
	}

	public String checkingAction(String action) {
		if (action.equals("1") && moles[0]) {
			score += 15;
			moles[0] = false;
			return "Score +15";
		}
		if (action.equals("2") && moles[1]) {
			score += 15;
			moles[1] = false;
			return "Score +15";
		}
		if (action.equals("3") && moles[2]) {
			score += 15;
			moles[2] = false;
			return "Score +15";
		}
		return "Enter 1, 2 or 3 to hit the mole";
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
