package com.cooksys.facebook.beans.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cooksys.facebook.beans.dao.GroupDao;
import com.cooksys.facebook.model.Group;
import com.cooksys.facebook.model.GroupUser;
import com.cooksys.facebook.model.Message;
import com.cooksys.facebook.model.User;

@Repository
@Transactional
@SuppressWarnings("unchecked")
public class GroupDaoImpl implements GroupDao {

	private Logger log = LoggerFactory.getLogger(GroupDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Group getGroupByName(String name) {
		return (Group) sessionFactory.getCurrentSession()
				.createQuery("from Group u where u.name = :name")
				.setString("name", name).uniqueResult();
	}

	@Override
	public List<Group> searchGroupByName(String name) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						" select g                    "
								+ " from Group g                                               "
								+ " where g.name like :name                            ")
				.setString("name", "%" + name + "%").list();
	}

	@Override
	public List<Group> getGroupsByUser(Integer userId) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"Select gu.group " + "from GroupUser gu 		  "
								+ "where gu.user.userId = :userId   ")
				.setInteger("userId", userId).list();
	}

	@Override
	public List<Group> listGroups() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Group").list();
	}

	@Override
	public GroupUser getMembership(Group group, User member) {
		log.info("GroupUser getMembership(Group group, User member)");
		GroupUser result = null;
		Session session = sessionFactory.getCurrentSession();
		result = (GroupUser) session
				.createQuery(
						"           from GroupUser gu "
								+ "where gu.isApproved = true "
								+ "  and gu.user.userId = :member) ")
				.setInteger("user", member.getUserId()).uniqueResult();
		if (result == null) {
			log.info("getFriendship returning null GroupUser");
		} else {
			log.info("getFriendship returning GroupUser");
		}
		return result;
	}

	@Override
	public GroupUser getMembershipRequest(Group group, User member) {
		log.info("GroupUser getMembershipRequest(Group group, User member)");
		log.info(group.toString());
		log.info(member.toString());
		GroupUser result = null;
		Session session = sessionFactory.getCurrentSession();
		result = (GroupUser) session
				.createQuery(
						"           from GroupUser gu "
								+ "where gu.isApproved = false "
								+ "  and gu.user.userId = :member) ")
				.setInteger("member", member.getUserId()).uniqueResult();
		if (result == null) {
			log.info("getMembershipRequest returning null GroupUser");
		} else {
			log.info("getMembershipRequest returning GroupUser");
		}
		return result;
	}

	@Override
	public List<GroupUser> getGroupsMembers(Integer groupId) {
		Session session = sessionFactory.getCurrentSession();
		return session
				.createQuery(
						"           select gu                     "
								+ "	  from GroupUser gu           "
								+ "	  left join fetch gu.group g  "
								+ "	  left join fetch gu.user u   "
								+ "	 where g.groupId = :groupId   "
								+ "	 order by gu.isApproved")
				.setInteger("groupId", groupId).list();
	}

	@Override
	public List<GroupUser> getGroupsLeaders(Integer groupId) {
		Session session = sessionFactory.getCurrentSession();
		return session
				.createQuery(
						"           select gu                     "
								+ "	  from GroupUser gu           "
								+ "	  left join fetch gu.group g  "
								+ "	  left join fetch gu.user u   "
								+ "	 where g.groupId = :groupId   "
								+ "	   and gu.isLeader = true     ")
				.setInteger("groupId", groupId).list();
	}

	@Override
	public GroupUser getGroupLeader(Group group, User user) {
		Session session = sessionFactory.getCurrentSession();
		return (GroupUser) session
				.createQuery(
						"           select gu                     "
								+ "	  from GroupUser gu           "
								+ "	  left join fetch gu.group g  "
								+ "	  left join fetch gu.user u   "
								+ "	 where g.groupId = :groupId   "
								+ "	   and u.userId = :userId     "
								+ "	   and gu.isLeader = true     ")
				.setInteger("groupId", group.getGroupId())
				.setInteger("userId", user.getUserId()).uniqueResult();
	}

	@Override
	public List<Message> getGroupsMessages(Integer groupId) {
		log.info("List<Message> getGroupsMessages(Integer groupId)");
		Session session = sessionFactory.getCurrentSession();
		// TODO left join fetch t.tag?
		List<Message> list = session
				.createQuery(
						"          select t.message "
								+ "from Tag t           			    "
								+ "left join fetch t.message.user  	    "
								+ "where t.groupId = :groupId  			"
								+ "	 order by t.message.lastUpdate desc ")
				.setInteger("groupId", groupId).list();
		Hibernate.initialize(list);

		return list;
	}

	@Override
	public void addMember(GroupUser groupUser) {
		log.info("addMember(GroupUser groupUser)");
		log.info(groupUser.toString());
		Session session = sessionFactory.getCurrentSession();
		log.info("session.save(groupUser)");
		session.save(groupUser);
	}

	@Override
	public void removeMember(GroupUser groupUser) {
		log.info("removeMember(GroupUser groupUser)");
		log.info(groupUser.toString());

		Session session = sessionFactory.getCurrentSession();
		log.info("session.delete(groupUser)");
		session.delete(groupUser);
	}

	@Override
	public void acceptRequest(GroupUser groupUser) {
		log.info("acceptRequest(GroupUser groupUser)");
		log.info(groupUser.toString());
		groupUser.setIsApproved(true);
		Session session = sessionFactory.getCurrentSession();
		log.info("session.update(groupUser).setIsApproved(true)");
		session.update(groupUser);
	}

}