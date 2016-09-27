package com.cooksys.facebook.model.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.cooksys.facebook.model.User;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserList {
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	
}