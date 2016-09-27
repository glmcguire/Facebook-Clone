package com.cooksys.facebook.beans;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.facebook.beans.dao.MessageDao;
import com.cooksys.facebook.model.Message;

@Component
@Scope("request")
public class MessageBean {
    private Logger log = LoggerFactory.getLogger(MessageBean.class);

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private AuthenticationBean authenticationBean;
    @Autowired
    private UserWallBean userWallBean;

    private Message message;
    private List<Message> rootMessages;
    private String tags;
    private String searchType;
    private String searchString;

    @PostConstruct
    public void init() {
        log.info("MessageBean.init()");
        message = new Message();
        setRootMessages();
    }

    public String postNewMessage() {
        log.info("String postNewMessage()");
        if (authenticationBean.isLoggedIn()) {
            log.info("message.setUser(authenticationBean.getUser())");
            message.setIsRoot(true);
            message.setUser(authenticationBean.getUser());

            Message dbMessage = messageDao.postNewMessage(message, tags);
            if (dbMessage != null) {
                log.info("post-success");
                setRootMessages();
                return "post-success";
            } else {
                log.info("dbMessage = null");
            }
        } else {
            log.info("not logged in");
        }
        log.info("message post failed");
        return "post-failure";
    }

    public List<Message> getRootMessages() {
        return rootMessages;
    }

    public void setRootMessages() {
        log.info("setRootMessages()");
        rootMessages = messageDao.listRootMessages();
        userWallBean.setUsersMessages();
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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