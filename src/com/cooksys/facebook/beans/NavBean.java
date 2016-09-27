package com.cooksys.facebook.beans;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.facebook.beans.dao.GroupDao;
import com.cooksys.facebook.beans.dao.UserDao;
import com.cooksys.facebook.model.Group;
import com.cooksys.facebook.model.User;

@Component
@Scope("session")
public class NavBean {
    private Logger log = LoggerFactory.getLogger(AuthenticationBean.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserListBean userListBean;
    @Autowired
    private UserWallBean userWallBean;

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private GroupListBean groupListBean;
    @Autowired
    private GroupWallBean groupWallBean;

    private String searchType = null;
    private String searchString = null;

    public String searchFromHeader() {
        log.info("searchFromHeader()");
        log.info("Searching {}s for '{}'", searchType, searchString.trim());
        String searchResults = "search-error";
        if (searchType == null || searchString == null) {
            log.info("searchType or searchString were null");
            return searchResults;
        }

        switch (searchType) {
        case "User":
            List<User> userResults = userDao.getUserByName(searchString.trim());
            if (userResults.size() == 1) {
                userWallBean.setUser(userResults.get(0));
                searchResults = "user";
            } else {
                userListBean.setUsers(userResults);
                searchResults = "users";
            }
            break;

        case "Group":
            List<Group> groupResults = groupDao.searchGroupByName(searchString
                    .trim());
            if (groupResults.size() == 1) {
                groupWallBean.setGroup(groupResults.get(0));
                searchResults = "group";
            } else {
                groupListBean.setGroups(groupResults);
                searchResults = "groups";
            }
            break;
        }
        return searchResults;
    }

    public String home() {
        return "home";
    }

    public String login() {
        return "login";
    }

    public String register() {
        return "register";
    }

    public String profile() {
        return "profile";
    }

    public String user() {
        return "user";
    }

    public String users() {
        return "users";
    }

    public String tweets() {
        return "tweets";
    }

    public String post() {
        return "post";
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}