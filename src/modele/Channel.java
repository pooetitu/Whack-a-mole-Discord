package modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Channel {
	@Id
	@Column(name = "channel_id")
	private String channelID;

	public Channel() {
		super();
	}

	public Channel(String channelID, GuildHandler guild) {
		super();
		this.channelID = channelID;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

}
