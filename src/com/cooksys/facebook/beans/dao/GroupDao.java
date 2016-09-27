package com.cooksys.facebook.beans.dao;

import java.util.List;

import com.cooksys.facebook.model.Follower;
import com.cooksys.facebook.model.Group;
import com.cooksys.facebook.model.GroupUser;
import com.cooksys.facebook.model.Message;
import com.cooksys.facebook.model.User;

public interface GroupDao {

	public Group getGroupByName(String name);

	public List<Group> listGroups();

	public GroupUser getMembership(Group group, User member);
	public GroupUser getMembershipRequest(Group group, User member);

	public List<GroupUser> getGroupsMembers(Integer groupId);
	public List<GroupUser> getGroupsLeaders(Integer groupId);
	public GroupUser getGroupLeader(Group group, User user);

	public List<Message> getGroupsMessages(Integer userId);

	public void addMember(GroupUser groupUser);
	public void removeMember(GroupUser groupUser);
	public void acceptRequest(GroupUser groupUser);

	public List<Group> getGroupsByUser(Integer userId);

	public List<Group> searchGroupByName(String name);

	
}
