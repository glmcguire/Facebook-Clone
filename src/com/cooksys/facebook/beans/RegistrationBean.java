package com.cooksys.facebook.beans;

import javax.annotation.PostConstruct;

import org.primefaces.event.FlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.facebook.beans.dao.UserDao;
import com.cooksys.facebook.model.User;

@Component
@Scope("session")
public class RegistrationBean {

	private Logger log = LoggerFactory.getLogger(RegistrationBean.class);
	
	@Autowired
	private UserDao userDao;
	private User user;

    private boolean skip = false;

	@PostConstruct
	public void init() {
		user = new User();
	}

	public String register() {
		log.info("String register()");
		User dbUser = userDao.registerUser(user);
		if (dbUser != null) {
			log.info("registration-success");
			return "registration-success";
		} else {
			log.info("registration-failure");
			return "registration-failure";
		}
	}

    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
    
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

}
