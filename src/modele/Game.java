package modele;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class Game implements Runnable, EventListener {
	private Message msg;
	private User author;
	private MessageChannel channel;
	private boolean playing = true;
	private int score;
	private String line1;
	private String line2;
	private String line3;
	private int timer;
	private String action;

	public Game(Message msg, User author, MessageChannel channel) {
		super();
		this.msg = msg;
		this.author = author;
		this.channel = channel;
	}

	public Message getMsg() {
		return msg;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public MessageChannel getChannel() {
		return channel;
	}

	public void setChannel(MessageChannel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		while (playing) {
			try {
				if (timer == 60000) {
					playing = false;
					break;
				}
				line1=author.getAsMention()+" Score: "+score;
				channel.sendMessage(line1+"\n"+line2+"\n"+line3);
				timer += 100;
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onEvent(GenericEvent event) {
		if (event instanceof MessageReceivedEvent) {
			String content = ((MessageReceivedEvent) event).getMessage().getContentDisplay();
			if (content.equals("1") || content.equals("2") || content.equals("3")) {
				action=content;
			}else {
				line2="Enter 1, 2 or 3 to hit the mole";
			}
			((MessageReceivedEvent) event).getMessage().delete().queue();
			return;
		}
	}
}
