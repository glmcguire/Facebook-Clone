package com.cooksys.facebook.beans;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.facebook.beans.dao.GroupDao;
import com.cooksys.facebook.model.Group;

@Component
@Scope("session")
public class GroupListBean {
	private Logger log = LoggerFactory.getLogger(GroupListBean.class);

	@Autowired
	private GroupDao groupDao;
	private List<Group> groups;

	@PostConstruct
	public void init() {
		groups = groupDao.listGroups();
	}

	public GroupDao getGroupDao() {
		return groupDao;
	}

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
}
