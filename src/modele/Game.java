package modele;

import main.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class Game implements Runnable, EventListener {
	private GameLogic gameLogic;
	private Message msg;
	private Player author;
	private MessageChannel channel;
	private boolean playing;
	private int timeLimit;
	private int timer;
	private String line1;
	private String line2;
	private String line3;
	private String action;

	public Game(Player author, MessageChannel channel) {
		super();
		this.author = author;
		this.channel = channel;
		playing = true;
		timeLimit = 30000;
		timer = 0;
		action = "";
		gameLogic = new GameLogic();
	}

	private void start() throws InterruptedException {
		msg = channel.sendMessage(author.getUser().getAsMention() + "\nLet's play!").complete();
		for (int i = 3; i >= 1; i--) {
			Thread.sleep(1000);
			channel.editMessageById(msg.getId(), author.getUser().getAsMention() + "\nStart in " + i).queue();
		}
	}

	@Override
	public void run() {
		try {
			start();
			while (playing) {
				System.out.println("playing with " + author.getUser().getName());
				if (timer >= timeLimit) {
					playing = false;
					break;
				}
				game();
				msg.editMessage(line1 + "\n" + line2 + "\n" + line3).queue();
				gameLogic.turn();
				timer += 1500;
				Thread.sleep(1500);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			end();
		}

	}

	private void game() {
		int timeLeft = (timeLimit / 1500) - (timer / 1500);
		line1 = author.getUser().getAsMention() + " Score: " + gameLogic.getScore() + " Time left: " + timeLeft;
		line2 = gameLogic.checkingAction(action);
		action = "";
		line3 = gameLogic.moleDisplay();
	}

	private void end() {
		System.out.println("game over");
		String mention = author.getUser().getAsMention();
		author.finish(gameLogic.getScore());
		msg.editMessage(mention + "\nGame over your score:" + gameLogic.getScore() + "\nyour best score:"
				+ author.getHighestScore()).complete();
		Main.getBot().removeEventListener(this);
	}

	@Override
	public void onEvent(GenericEvent event) {
		if (event instanceof MessageReceivedEvent) {
			String content = ((MessageReceivedEvent) event).getMessage().getContentDisplay();
			if (content.equals("1") || content.equals("2") || content.equals("3")) {
				action = content;
			} else if (content.equals("!stop")) {
				playing = false;
			} else {
				line2 = "Enter 1, 2 or 3 to hit the mole";
			}
			if (((MessageReceivedEvent) event).getAuthor().equals(author.getUser()) && playing)
				((MessageReceivedEvent) event).getMessage().delete().complete();
		}
	}

	public Message getMsg() {
		return msg;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}

	public Player getAuthor() {
		return author;
	}

	public void setAuthor(Player author) {
		this.author = author;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getLine3() {
		return line3;
	}

	public void setLine3(String line3) {
		this.line3 = line3;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public MessageChannel getChannel() {
		return channel;
	}

	public void setChannel(MessageChannel channel) {
		this.channel = channel;
	}
}
