package modele;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class Game implements Runnable, EventListener {
	private Message msg;
	private Player author;
	private MessageChannel channel;
	private boolean playing = true;
	private int score = 0;
	private String line1;
	private String line2;
	private String line3;
	private int timer = 0;
	private String action;

	public Game(Player author, MessageChannel channel) {
		super();
		this.author = author;
		this.channel = channel;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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

	private void start() throws InterruptedException {
		msg = channel.sendMessage(author.getUser().getAsMention() + "\nLet's play!").complete();
		for (int i = 3; i >= 1; i--) {
			Thread.sleep(1000);
			channel.editMessageById(msg.getId(), author.getUser().getAsMention() + "\nStart in " + i).queue();
		}
	}

	@Override
	public void run() {
		System.out.println("playing with " + author.getUser().getName());
		try {
			start();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (playing) {
			System.out.println("playing with " + author.getUser().getName());
			try {
				if (timer == 20000) {
					playing = false;
					break;
				}
				game();
				msg.editMessage(line1 + "\n" + line2 + "\n" + line3).queue();
				timer += 100;
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				author.finish(score);
			}
		}

	}

	private void game() {
		line1 = author.getUser().getAsMention() + " Score: " + score;
		line3 = "";
	}

	@Override
	public void onEvent(GenericEvent event) {
		if (event instanceof MessageReceivedEvent) {
			String content = ((MessageReceivedEvent) event).getMessage().getContentDisplay();
			if (content.equals("1") || content.equals("2") || content.equals("3")) {
				action = content;
			} 
			else if(content.equals("!stop")) {
				playing=false;
			}
					else {
				line2 = "Enter 1, 2 or 3 to hit the mole";
			}
			if (((MessageReceivedEvent) event).getAuthor().equals(author.getUser()) && playing)
				((MessageReceivedEvent) event).getMessage().delete().queue();
		}
	}
}
