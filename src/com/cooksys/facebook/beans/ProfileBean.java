package com.cooksys.facebook.beans;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.facebook.beans.dao.GroupDao;
import com.cooksys.facebook.beans.dao.UserDao;
import com.cooksys.facebook.model.Follower;
import com.cooksys.facebook.model.Group;
import com.cooksys.facebook.model.User;

@Component
@Scope("request")
public class ProfileBean {
	private Logger log = LoggerFactory.getLogger(ProfileBean.class);

	@Autowired
	private AuthenticationBean auth;

	@Autowired
	private UserDao userDao;
	@Autowired
	private GroupDao groupDao;

	private User user;

	private List<Follower> friendships;
	private List<Group> memberships;

	@PostConstruct
	public void init() {
		log.info("ProfileBean.init()");
		if (auth.isLoggedIn()) {
			log.info("auth.isLoggedIn() = true");
			String email = auth.getUserEmail();
			user = userDao.getUserByEmail(email);
			friendships = userDao.getUsersFriends(user.getUserId());
			memberships = groupDao.getGroupsByUser(user.getUserId());
			log.info(Integer.toString(friendships.size()));
		} else {
			log.info("auth.isLoggedIn() = false");
			user = new User();
		}
	}

	public boolean showBefriend(Follower follower) {
		log.info("boolean ProfileBean.showBefriend()");
		if (follower.isIsApproved()) {
			log.info("follower.isIsApproved()");
			log.info("don't show Befriend");
			return false;
		}
		if (!userIdEqualsFriendId(follower)) {
			log.info("userIdEqualsFriendId");
			log.info("don't show Befriend");
			return false;
		}
		log.info("show Befriend");
		return true;
	}

	public boolean userIdEqualsFriendId(Follower follower) {
		log.info("userIdEqualsFriendId(Follower follower)");
		log.info(follower.toString());
		log.info(follower.getUserByFriendId().toString());
		log.info(auth.toString());
		if (follower.getUserByFriendId().getEmail().equals(auth.getUserEmail()))
			return true;
		return false;
	}

	public List<Follower> getFriendships() {
		return friendships;
	}

	public void setFriendships(List<Follower> friendships) {
		this.friendships = friendships;
	}

	public List<Group> getMemberships() {
		return memberships;
	}

	public void setMemberships(List<Group> memberships) {
		this.memberships = memberships;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AuthenticationBean getAuth() {
		return auth;
	}

	public void setAuth(AuthenticationBean auth) {
		this.auth = auth;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
