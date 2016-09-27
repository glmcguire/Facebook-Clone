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
public class UserWallBean {
	private Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private AuthenticationBean authenticationBean;

	private Boolean isLoggedIn = false;
	private Boolean isViewingSelf = false;

	private User user;
	private Follower friend;
	private List<Message> usersMessages;
	private List<Follower> usersFriends;

	@PostConstruct
	public void init() {
		log.info("UserWallBean.init()");
		user = new User();
		friend = null;
	}

	public boolean showBefriend() {
		log.info("boolean UserWallBean.showBefriend()");
		if (!isLoggedIn) {
			log.info("!isLoggedIn()");
			log.info("don't show follow");
			return false;
		}
		if (isViewingSelf) {
			log.info("don't show follow");
			return false;
		}
		if (hasFriendship()) {
			log.info("hasFriendship()");
			log.info("don't show follow");
			return false;
		}
		if (isFriendshipRequested()) {
			log.info("isFriendshipRequested()");
			log.info("don't show follow");
			return false;
		}
		log.info("show follow");
		return true;
	}

	public boolean showUnfriend() {
		log.info("boolean UserWallBean.showUnfriend()");
		if (!isLoggedIn) {
			log.info("!isLoggedIn");
			log.info("don't show unfollow");
			return false;
		}
		if (isViewingSelf) {
			log.info("don't show unfollow");
			return false;
		}
		if (!hasFriendship()) {
			log.info("!hasFriendship()");
			log.info("don't show unfollow");
			return false;
		}
		log.info("show unfollow");
		return true;
	}

	public boolean showCancelRequest() {
		log.info("boolean UserWallBean.showCancelRequest()");
		if (!isLoggedIn) {
			log.info("!isLoggedIn");
			log.info("don't show cancel request");
			return false;
		}
		if (isViewingSelf) {
			log.info("don't show cancel request");
			return false;
		}
		if (hasFriendship()) {
			log.info("hasFriendship()");
			log.info("don't show cancel request");
			return false;
		}
		if (!isFriendshipRequested()) {
			log.info("isFriendshipRequested()");
			log.info("don't show cancel request");
			return false;
		}
		log.info("show cancel request");
		return true;
	}

	public boolean showMessagePanel() {
		log.info("boolean UserWallBean.showMessagePanel()");
		if (!isLoggedIn) {
			log.info("!isLoggedIn");
			log.info("don't show tweet");
			return false;
		}
		if (!isViewingSelf) {
			log.info("don't show tweet");
			return false;
		}
		log.info("show tweet");
		return true;
	}

	/**
	 * If friendship exists already, assigns existing friendship to instance
	 * variable Note: a friendship will already exist even if not yet approved
	 * 
	 * If no friendship exists, will create a new friend instance variable
	 * 
	 * @return True when auth user is already following user
	 */
	public boolean hasFriendship() {
		log.info("boolean UserWallBean.hasFriendship()");
		if ((authenticationBean.getUser() != null) && (user != null)) {
			log.info(user.toString());
			friend = userDao.getFriendship(user, authenticationBean.getUser());
			if (friend != null) {
				log.info("{} is following {}", authenticationBean.userName,
						user.getName());
				return true;
			}
		} else {
			log.info("one user was null");
		}
		log.info("not following");
		friend = new Follower(user, authenticationBean.getUser());
		return false;
	}

	public boolean isFriendshipRequested() {
		log.info("boolean UserWallBean.isFriendshipRequested()");
		if ((authenticationBean.getUser() != null) && (user != null)) {

			log.info(user.toString());
			log.info(authenticationBean.getUserEmail());

			friend = userDao.getFriendshipRequest(user,
					authenticationBean.getUser());
			if (friend != null) {
				log.info("{} is following {}", authenticationBean.userName,
						user.getName());
				return true;
			}
		} else {
			log.info("one user was null");
		}
		friend = new Follower(user, authenticationBean.getUser());
		log.info("not following");
		return false;
	}

	public String befriend(User user) {
		this.user = user;
		log.info("follow()");
		if (!hasFriendship()) {
			log.info("follower: {}", friend.toString());
			userDao.befriend(friend);
			setUsersFriends(); // updates list of friends
			return "profile-update";
		}
		return "profile-error";
	}

	public String unfriend(User user) {
		this.user = user;
		log.info("unfriend()");
		if (hasFriendship() || isFriendshipRequested()) {
			log.info("follower: {}", friend.toString());
			userDao.unfriend(friend);
			setUsersFriends(); // updates list of friends
			return "profile-update";
		}
		return "profile-error";
	}

	public String reject(User user) {
		this.user = user;
		log.info("reject(User user)");
		if (isFriendshipRequested()) {
			log.info("follower: {}", friend.toString());
			userDao.unfriend(friend);
			setUsersFriends(); // updates list of friends
			return "profile-update";
		}
		return "profile-error";
	}

	public String acceptRequest(User user) {
		this.user = user;
		log.info("acceptRequest()");
		if (isFriendshipRequested()) {
			log.info("follower: {}", friend.toString());
			userDao.acceptRequest(friend);
			setUsersFriends(); // updates list of friends
			return "profile-update";
		}
		return "profile-error";
	}

	public boolean userIdEqualsFriendId(Follower follower) {
		log.info("follower.getUserByFriendId().getEmail() = {}", follower.getUserByFriendId().getEmail());
		log.info("user.getUserEmail() = {}", user.getEmail());
		if (follower.getUserByFriendId().getEmail()
				.equals(user.getEmail())) {
			log.info("userIdEqualsFriendId(follower) = true");
			return true;
		} else {
			log.info("userIdEqualsFriendId(follower) = false");
			return false;
		}
	}

	public String viewUser(User user) {
		this.setUser(user);
		return "users";
	}

	public List<Message> getUsersMessages() {
		return usersMessages;
	}

	public void setUsersMessages() {
		usersMessages = userDao.getUsersMessages(user.getUserId());
	}

	public void setUsersFriends() {
		usersFriends = userDao.getUsersFriends(user.getUserId());
	}

	public List<Follower> getUsersFriends() {
		return usersFriends;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		log.info("setUser(User user): {}", user.getName());
		this.user = user;
		this.isViewingSelf = authenticationBean.getUser().getEmail()
				.equals(user.getEmail());
		log.info("isViewingSelf = {}", isViewingSelf.toString());
		setUsersFriends(); // updates list of friends
		setUsersMessages(); // updates list of messages
	}

	public Follower getFollower() {
		return friend;
	}

	public void setFollower(Follower follower) {
		this.friend = follower;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public AuthenticationBean getAuthenticationBean() {
		return authenticationBean;
	}

	public void setAuthenticationBean(AuthenticationBean authenticationBean) {
		this.authenticationBean = authenticationBean;
	}

	public void setUsersMessages(List<Message> usersMessages) {
		this.usersMessages = usersMessages;
	}

	public void setUsersFriends(List<Follower> usersFriends) {
		this.usersFriends = usersFriends;
	}

	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public Boolean getIsViewingSelf() {
		return isViewingSelf;
	}

	public void setIsViewingSelf(Boolean isViewingSelf) {
		this.isViewingSelf = isViewingSelf;
	}

	public Follower getFriend() {
		return friend;
	}

	public void setFriend(Follower friend) {
		this.friend = friend;
	}
}
