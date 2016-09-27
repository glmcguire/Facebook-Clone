package com.cooksys.facebook.beans;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.cooksys.facebook.beans.dao.GroupDao;
import com.cooksys.facebook.beans.dao.UserDao;
import com.cooksys.facebook.beans.dao.impl.PasswordEncoder;
import com.cooksys.facebook.model.Group;
import com.cooksys.facebook.model.User;

@Service
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class AuthenticationBean {
    private Logger log = LoggerFactory.getLogger(AuthenticationBean.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    private User user;
    public String userEmail = "session cleared";
    public String userName = "empty";

    private PasswordEncoder encoder = new PasswordEncoder();

    @PostConstruct
    public void init() {
        log.info("AuthenticationBean.init()");
        user = new User();
    }

    public boolean login(User user) {
        log.info("boolean AuthenticationBean.login(User user)");
        if (user != null && user.getEmail() != null
                && user.getPassword() != null) {

            String email = user.getEmail().trim();
            // This returns the typed password
            String password = user.getPassword().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                User dbUser = userDao.getUserByEmail(email);
                log.info(dbUser.toString());
                // after the &&, this compares the hashed and salted pw in the
                // db with the raw password typed in
                if (dbUser != null
                        && encoder.getPasswordEncoder().matches(password,
                                dbUser.getPassword())) {
                    log.info("login successfully");
                    this.userEmail = dbUser.getEmail().trim();
                    this.userName = dbUser.getName();
                    this.user = dbUser;
                    return true;
                } else {
                    log.info("passwords don't match");
                }
            } else {
                log.info("user or password are empty");
            }
        } else {
            log.info("user or password are null");
        }
        log.info("login unsuccessfully");
        return false;
    }

    public boolean isLoggedIn() {
        log.info("boolean AuthenticationBean.isLoggedIn()");
        if ((user != null) && (userEmail != "session cleared")) {
            log.info("return is logged in");
            return true;
        } else {
            log.info("return is not logged in");
            return false;
        }
    }

    public String logout() {
        log.info("String AuthenticationBean.logout()");
        user = null;
        FacesContext.getCurrentInstance().getExternalContext()
                .invalidateSession();
        log.info("logged-out");
        userEmail = "session cleared";
        return "logout";
    }

    public String getUserEmail() {
        log.info("String AuthenticationBean.getUserEmail()");
        if ((user != null) && (userEmail != "session cleared")) {
            log.info("return " + user.getEmail());
            return user.getEmail();
        } else {
            log.info("return null");
            return null;
        }
    }

    public Boolean isGroupLeader(Group group, User user) {
        log.info("Boolean isGroupLeader(Group group, User user)");
        if (group != null) {
            log.info("return " + user.getEmail());
            return (groupDao.getGroupLeader(group, user) != null);
        } else {
            log.info("return false");
            return false;
        }
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}