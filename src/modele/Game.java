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

	public Game(Player author, MessageChannel channel) {
		super();
		this.author = author;
		this.channel = channel;
		playing = true;
		timeLimit = 30000;
		timer = 0;
		gameLogic = new GameLogic();
	}

	private void start() throws InterruptedException {
		System.out.println("Starting to play with " + author.getUsername());
		msg = channel.sendMessage(author.getUsertag() + "\nLet's play!").complete();
		for (int i = 3; i >= 1; i--) {
			Thread.sleep(1000);
			channel.editMessageById(msg.getId(), author.getUsertag() + "\nStart in " + i).queue();
		}
	}

	@Override
	public void run() {
		try {
			start();
			while (playing) {
				if (timer >= timeLimit) {
					playing = false;
					break;
				}
				game();
				msg.editMessage(line1 + "\n" + gameLogic.messageDisplay()).queue();
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
		line1 = author.getUsertag() + " Score: " + gameLogic.getScore() + " Time left: " + timeLeft;
		gameLogic.turn();
	}

	private void end() {
		System.out.println("game over");
		String mention = author.getUsertag();
		author.finish(gameLogic.getScore());
		msg.editMessage(mention + "\nGame over your score:" + gameLogic.getScore() + "\nyour best score:"
				+ author.getHighestScore()).complete();
		Main.getBot().removeEventListener(this);
	}

	@Override
	public void onEvent(GenericEvent event) {
		if (event instanceof MessageReceivedEvent) {
			String content = ((MessageReceivedEvent) event).getMessage().getContentDisplay();
			if (content.equals("!stop")) {
				playing = false;
				return;
			}
			if (((MessageReceivedEvent) event).getAuthor().getId().equals(author.getUserID()) && playing
					&& gameLogic.checkingAction(content))
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

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public MessageChannel getChannel() {
		return channel;
	}

	public void setChannel(MessageChannel channel) {
		this.channel = channel;
	}
}
