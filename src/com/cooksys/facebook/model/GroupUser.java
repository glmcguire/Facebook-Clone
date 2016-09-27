package com.cooksys.facebook.model;

// Generated May 29, 2015 12:10:16 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * GroupUser generated by hbm2java
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupUser implements java.io.Serializable {

	private Integer groupUserId;
	private Group group;
	private User user;
	private boolean isApproved;
	private boolean isLeader;
	private Date lastUpdate;

	public GroupUser() {
	}

	public GroupUser(Group group, User user) {
		this.group = group;
		this.user = user;
	}
	
	public GroupUser(Group group, User user, boolean isApproved,
			boolean isLeader, Date lastUpdate) {
		this.group = group;
		this.user = user;
		this.isApproved = isApproved;
		this.isLeader = isLeader;
		this.lastUpdate = lastUpdate;
	}

	public Integer getGroupUserId() {
		return this.groupUserId;
	}

	public void setGroupUserId(Integer groupUserId) {
		this.groupUserId = groupUserId;
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isIsApproved() {
		return this.isApproved;
	}

	public void setIsApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public boolean isIsLeader() {
		return this.isLeader;
	}

	public void setIsLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}