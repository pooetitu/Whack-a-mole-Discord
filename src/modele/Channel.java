package modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Channel {
	@Id
	@Column(name = "channel_id")
	private String channelID;
	@Transient
	private boolean playing = false;
	@Transient
	private boolean clearing = false;

	public Channel() {
		super();
	}

	public Channel(String channelID) {
		super();
		this.channelID = channelID;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public boolean isClearing() {
		return clearing;
	}

	public void setClearing(boolean clearing) {
		this.clearing = clearing;
	}

	@Override
	public boolean equals(Object o) {
		return (((Channel) o).getChannelID() == channelID ? true : false);
	}

}
