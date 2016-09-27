package com.cooksys.facebook.model.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.cooksys.facebook.model.Follower;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FollowerList {
	private List<Follower> followers;

	public List<Follower> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Follower> followers) {
		this.followers = followers;
	}
	
	
}
