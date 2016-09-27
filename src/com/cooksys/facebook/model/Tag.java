package com.cooksys.facebook.model;

// Generated May 29, 2015 12:10:16 PM by Hibernate Tools 4.3.1

/**
 * Tag generated by hbm2java
 */
public class Tag implements java.io.Serializable {

	private Integer tagId;
	private Message message;
	private User user;
	private Integer groupId;

	public Tag() {
	}

	public Tag(Message message) {
		this.message = message;
	}

	public Tag(Message message, User user, Integer groupId) {
		this.message = message;
		this.user = user;
		this.groupId = groupId;
	}

	public Integer getTagId() {
		return this.tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

}
