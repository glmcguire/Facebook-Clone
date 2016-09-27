package com.cooksys.facebook.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.facebook.beans.dao.GroupDao;
import com.cooksys.facebook.beans.dao.MessageDao;
import com.cooksys.facebook.beans.dao.UserDao;
import com.cooksys.facebook.model.Follower;
import com.cooksys.facebook.model.Message;
import com.cooksys.facebook.model.User;
import com.cooksys.facebook.model.wrappers.FollowerList;
import com.cooksys.facebook.model.wrappers.GroupList;
import com.cooksys.facebook.model.wrappers.MessageList;
import com.cooksys.facebook.model.wrappers.UserList;

@RestController
public class UserServiceController {


    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private GroupDao groupDao;

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable int userId) {

        User result = userDao.getUserById(userId);
        return result;
    }


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public UserList getUsers() {
        UserList results = new UserList();
        results.setUsers(userDao.listUsers());
        return results;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User makeUser(@RequestBody User user) {
        userDao.makeUser(user);
        return user;
    }

    @RequestMapping(value = "/users/{userId}/friends", method = RequestMethod.GET)
    public FollowerList getUsersFriends(@PathVariable int userId) {

        FollowerList result = new FollowerList();
        result.setFollowers(userDao.getUsersFriends(userId));
        return result;
    }

    @RequestMapping(value = "/users/{userId}/friends", method = RequestMethod.POST)
    public Follower requestFriend(@PathVariable int userId,
            @RequestBody Follower follower) {
        userDao.befriend(follower);
        return follower;
    }

    @RequestMapping(value = "/users/{userId}/friends", method = RequestMethod.PUT)
    public Follower acceptRequest(@PathVariable int userId,
            @RequestBody Follower follower) {
        userDao.acceptRequest(follower);
        return follower;
    }

    @RequestMapping(value = "/users/{userId}/friends", method = RequestMethod.PATCH)
    public Follower ignoreFriend(@PathVariable int userId,
            @RequestBody Follower follower) {
//        TODO after blacklist is implemented
//        userDao.ignoreFriend(follower);
        return follower;
    }

    @RequestMapping(value = "/users/{userId}/posts", method = RequestMethod.GET)
    public MessageList getUsersPosts(@PathVariable int userId) {

        MessageList result = new MessageList();
        result.setMessages(userDao.getUsersMessages(userId));
        return result;
    }

    @RequestMapping(value = "/users/{userId}/posts", method = RequestMethod.POST)
    public Message makeMessage(@PathVariable int userId,
            @RequestBody Message message) {
        message.setUser(userDao.getUserById(userId));
        message.setIsRoot(true);
        messageDao.makeMessage(message);
        return message;
    }
    @RequestMapping(value = "/users/{userId}/groups", method = RequestMethod.GET)
    public GroupList getMemberships(@PathVariable int userId) {
        GroupList groupList = new GroupList();
        groupList.setGroups(groupDao.getGroupsByUser(userId));
        return groupList;
    }
}
