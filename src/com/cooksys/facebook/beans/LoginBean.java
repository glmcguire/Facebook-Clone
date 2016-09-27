package com.cooksys.facebook.beans;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.facebook.beans.dao.UserDao;
import com.cooksys.facebook.beans.dao.impl.PasswordEncoder;
import com.cooksys.facebook.model.User;

@Component
@Scope("request")
public class LoginBean {

	private Logger log = LoggerFactory.getLogger(LoginBean.class);
	@Autowired
	private AuthenticationBean authentication;

	@Autowired
	private UserDao userDao;

	private User user;
	private Boolean loginFailed;

	@PostConstruct
	public void init() {
		user = new User();
	}

	public String login() {
		if (authentication.login(user)) {
			loginFailed = false;
			return "login-success";
		} else {
			loginFailed = true;
			return "login-failure";
		}
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getLoginFailed() {
		return loginFailed;
	}

	public void setLoginFailed(Boolean loginFailed) {
		this.loginFailed = loginFailed;
	}

}
