package com.cooksys.facebook.beans;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.facebook.beans.dao.UserDao;
import com.cooksys.facebook.beans.dao.impl.UserDaoImpl;
import com.cooksys.facebook.model.Follower;
import com.cooksys.facebook.model.Message;
import com.cooksys.facebook.model.User;

@Component
@Scope("session")
public class UserListBean {
	private Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private UserDao userDao;
	private List<User> users;

	@PostConstruct
	public void init() {
		users = userDao.listUsers();
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
