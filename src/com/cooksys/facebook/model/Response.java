package com.cooksys.facebook.model;

// Generated May 29, 2015 12:10:16 PM by Hibernate Tools 4.3.1

import java.util.Date;

/**
 * Response generated by hbm2java
 */
public class Response implements java.io.Serializable {

	private Integer responseId;
	private Message messageByMessageId;
	private Message messageByRootId;
	private Date lastUpdate;

	public Response() {
	}

	public Response(Message messageByMessageId, Message messageByRootId,
			Date lastUpdate) {
		this.messageByMessageId = messageByMessageId;
		this.messageByRootId = messageByRootId;
		this.lastUpdate = lastUpdate;
	}

	public Integer getResponseId() {
		return this.responseId;
	}

	public void setResponseId(Integer responseId) {
		this.responseId = responseId;
	}

	public Message getMessageByMessageId() {
		return this.messageByMessageId;
	}

	public void setMessageByMessageId(Message messageByMessageId) {
		this.messageByMessageId = messageByMessageId;
	}

	public Message getMessageByRootId() {
		return this.messageByRootId;
	}

	public void setMessageByRootId(Message messageByRootId) {
		this.messageByRootId = messageByRootId;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
