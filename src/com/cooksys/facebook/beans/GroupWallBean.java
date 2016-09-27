package com.cooksys.facebook.beans;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.facebook.beans.dao.GroupDao;
import com.cooksys.facebook.beans.dao.impl.GroupDaoImpl;
import com.cooksys.facebook.model.GroupUser;
import com.cooksys.facebook.model.Message;
import com.cooksys.facebook.model.Group;

@Component
@Scope("session")
public class GroupWallBean {
	private Logger log = LoggerFactory.getLogger(GroupDaoImpl.class);

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private AuthenticationBean authenticationBean;

	private Boolean isLoggedIn = false;
	private Boolean isGroupLeader = false;

	private Group group;
	private GroupUser groupUser;
	private List<Message> groupsMessages;
	private List<GroupUser> groupMembers;

	@PostConstruct
	public void init() {
		log.info("GroupWallBean.init()");
		// isLoggedIn = authenticationBean.isLoggedIn();
		group = new Group();
		groupUser = null;
	}

	public boolean showRequestMembership() {
		log.info("boolean GroupWallBean.showRequestMembership()");
		if (!isLoggedIn) {
			log.info("!isLoggedIn()");
			log.info("don't show Request Membership");
			return false;
		}
		if (hasMembership()) {
			log.info("hasMembership()");
			log.info("don't show Request Membership");
			return false;
		}
		if (isMembershipRequested()) {
			log.info("isMembershipRequested()");
			log.info("don't show Request Membership");
			return false;
		}
		log.info("show Request Membership");
		return true;
	}

	public boolean showDeleteMembership() {
		log.info("boolean GroupWallBean.showDeleteMembership()");
		if (!isLoggedIn) {
			log.info("!isLoggedIn");
			log.info("don't show Delete Membership");
			return false;
		}
		if (isGroupLeader) {
			log.info("isGroupLeader");
			log.info("don't show Delete Membership");
			return false;
		}
		if (!hasMembership()) {
			log.info("!hasMembership()");
			log.info("don't show Delete Membership");
			return false;
		}
		log.info("show Delete Membership");
		return true;
	}

	public boolean showCancelRequest() {
		log.info("boolean GroupWallBean.showCancelRequest()");
		if (!isLoggedIn) {
			log.info("!isLoggedIn");
			log.info("don't show Cancel Request");
			return false;
		}
		if (!isMembershipRequested()) {
			log.info("isMembershipRequested()");
			log.info("don't show Cancel Request");
			return false;
		}
		log.info("show Cancel Request");
		return true;
	}

	public boolean showMessagePanel() {
		log.info("boolean GroupWallBean.showMessagePanel()");
		if (!isLoggedIn) {
			log.info("!isLoggedIn");
			log.info("don't show message panel");
			return false;
		}
		if (!hasMembership()) {
			log.info("authenticationBean.getGroup() != group");
			log.info("don't show message panel");
			return false;
		}
		log.info("show message panel");
		return true;
	}

	/**
	 * If groupUsership exists already, assigns existing groupUsership to
	 * instance variable Note: a groupUsership will already exist even if not
	 * yet approved
	 * 
	 * If no groupUsership exists, will create a new groupUser instance variable
	 * 
	 * @return True when auth group is already following group
	 */
	public boolean hasMembership() {
		log.info("boolean GroupWallBean.hasMembership()");
		if ((authenticationBean.getUser() != null)) {
			log.info(group.toString());
			groupUser = groupDao.getMembership(group,
					authenticationBean.getUser());
			if (groupUser != null) {
				log.info("{} is a member of {}", authenticationBean.userName,
						group.getName());
				return true;
			}
		} else {
			log.info("authentication user was null");
		}
		log.info("not a group member");
		groupUser = new GroupUser(group, authenticationBean.getUser());
		return false;
	}

	public boolean isMembershipRequested() {
		log.info("boolean GroupWallBean.isMembershipRequested()");
		if ((authenticationBean.getUser() != null) && (group != null)) {
			log.info(group.toString());
			groupUser = groupDao.getMembershipRequest(group,
					authenticationBean.getUser());
			if (groupUser != null) {
				log.info("{} is a member of {}", authenticationBean.userName,
						group.getName());
				return true;
			}
		} else {
			log.info("authentication user was null");
		}
		log.info("not a group member");
		groupUser = new GroupUser(group, authenticationBean.getUser());
		return false;
	}

	public String requestMembership(Group group) {
		this.group = group;
		log.info("follow()");
		if (!hasMembership()) {
			log.info("follower: {}", groupUser.toString());
			groupDao.addMember(groupUser);
			setGroupsMembers(); // updates list of groupUsers
			return "group-update";
		}
		return "group-error";
	}

	public String deleteMembership(Group group) {
		this.group = group;
		log.info("ungroupUser()");
		if (hasMembership() || isMembershipRequested()) {
			log.info("follower: {}", groupUser.toString());
			groupDao.removeMember(groupUser);
			setGroupsMembers(); // updates list of groupUsers
			return "group-update";
		}
		return "group-error";
	}

	public String rejectMembership(Group group) {
		this.group = group;
		log.info("reject(Group group)");
		if (isMembershipRequested()) {
			log.info("follower: {}", groupUser.toString());
			groupDao.removeMember(groupUser);
			setGroupsMembers(); // updates list of groupUsers
			return "group-update";
		}
		return "group-error";
	}

	public String acceptRequest(Group group) {
		this.group = group;
		log.info("acceptRequest()");
		if (isMembershipRequested()) {
			log.info("follower: {}", groupUser.toString());
			groupDao.acceptRequest(groupUser);
			setGroupsMembers(); // updates list of groupUsers
			return "group-update";
		}
		return "group-error";
	}

	public String viewGroup(Group group) {
		this.setGroup(group);
		isLoggedIn = authenticationBean.isLoggedIn();
		return "group";
	}

	public List<Message> getGroupsMessages() {
		return groupsMessages;
	}

	public void setGroupsMessages() {
		groupsMessages = groupDao.getGroupsMessages(group.getGroupId());
	}

	public void setGroupsMembers() {
		groupMembers = groupDao.getGroupsMembers(group.getGroupId());
	}

	public List<GroupUser> getGroupsMembers() {
		return groupMembers;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		log.info("setGroup(Group group): {}", group.getName());
		this.group = group;
		this.isGroupLeader = authenticationBean.isGroupLeader(group,
				authenticationBean.getUser());
		setGroupsMembers(); // updates list of groupUsers
		setGroupsMessages(); // updates list of messages
	}

	public GroupUser getGroupUser() {
		return groupUser;
	}

	public void setGroupUser(GroupUser follower) {
		this.groupUser = follower;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public GroupDao getGroupDao() {
		return groupDao;
	}

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public AuthenticationBean getAuthenticationBean() {
		return authenticationBean;
	}

	public void setAuthenticationBean(AuthenticationBean authenticationBean) {
		this.authenticationBean = authenticationBean;
	}

	public void setGroupsMessages(List<Message> groupsMessages) {
		this.groupsMessages = groupsMessages;
	}

	public void setGroupsMembers(List<GroupUser> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public GroupUser getMember() {
		return groupUser;
	}

	public void setMember(GroupUser groupUser) {
		this.groupUser = groupUser;
	}

	public List<GroupUser> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<GroupUser> groupMembers) {
		this.groupMembers = groupMembers;
	}
}
