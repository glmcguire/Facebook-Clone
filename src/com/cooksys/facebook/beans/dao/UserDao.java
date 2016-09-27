package com.cooksys.facebook.beans.dao;

import java.util.List;

import com.cooksys.facebook.model.Follower;
import com.cooksys.facebook.model.Group;
import com.cooksys.facebook.model.Message;
import com.cooksys.facebook.model.User;

public interface UserDao {

	public User getUserByEmail(String email);

	public List<User> listUsers();
	
	public User registerUser(User user);

	public Follower getFriendship(User user, User friend);
	public Follower getFriendshipRequest(User friend, User user);

	public List<Group> getUsersGroups(Integer userId);
	public List<Message> getUsersMessages(Integer userId);
	public List<Follower> getUsersFriends(Integer userId);

	public void befriend(Follower follower);
	public void unfriend(Follower follower);
	public void acceptRequest(Follower follower);

	public List<User> getUserByName(String name);

	public User getUserById(Integer id);
	
	public void makeUser(User user);
	public User getUserById(int userId); 
	
}
