package modele;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Player {
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void play(MessageReceivedEvent event) {
		try {
			Message msg = event.getChannel().sendMessage(event.getAuthor().getAsMention() + "\nLet's play!").complete();
			for (int i = 1; i <= 3; i++) {
				Thread.sleep(1000);
				event.getChannel().editMessageById(msg.getId(), event.getAuthor().getAsMention() + "\nStart in " + i)
						.queue();
				Game game = new Game(msg,user,event.getChannel());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
