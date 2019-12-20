package main;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {
//	private boolean playing;

	public static void main(String[] args) throws LoginException {
		new JDABuilder("NjU3MzQ1MzQ4MTQxODQyNDMy.Xfv5AA.rESQ3pCiwnbyq3dH34aOeerB4ec").addEventListeners(new Main())
				.build();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			String msg = event.getMessage().getContentDisplay();
			switch (msg) {
			case ("§play"): {
				try {
					play(event);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			case ("§clear"): {
				for (Message iter : event.getChannel().getIterableHistory()) {
					iter.delete().queue();
				}
				break;
			}
			}
			System.out.println(event.getAuthor());
		}
	}

	private void play(MessageReceivedEvent event) throws InterruptedException {
		Message msg = event.getChannel().sendMessage(event.getAuthor().getAsMention() + "\nLet's play!").complete();
		for (int i = 1; i <= 3; i++) {
			Thread.sleep(1000);
			event.getChannel().editMessageById(msg.getId(), event.getAuthor().getAsMention() + "\nStart in " + i)
					.queue();
		}
	}
}
