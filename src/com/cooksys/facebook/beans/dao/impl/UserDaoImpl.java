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

import com.cooksys.facebook.beans.dao.UserDao;
import com.cooksys.facebook.model.Follower;
import com.cooksys.facebook.model.Group;
import com.cooksys.facebook.model.Message;
import com.cooksys.facebook.model.User;

@Repository
@Transactional
@SuppressWarnings("unchecked")
public class UserDaoImpl implements UserDao {

	private Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

	private PasswordEncoder encoder = new PasswordEncoder();

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public User getUserByEmail(String email) {
		return (User) sessionFactory.getCurrentSession()
				.createQuery("from User u where u.email = :email")
				.setString("email", email).uniqueResult();
	}

	@Override
	public User getUserById(Integer id) {
		return (User) sessionFactory.getCurrentSession()
				.createQuery("from User u where u.userId = :id")
				.setInteger("id", id).uniqueResult();
	}

	@Override
	public List<User> getUserByName(String name) {
		log.info("searchme getUserByName");
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"          select u                                             "
								+ "  from User u                                        "
								+ " where u.firstName like :name                        "
								+ "    or u.lastName like :name                         "
								+ "    or concat(u.firstName,' ',u.lastName) like :name ")
				.setString("name", "%" + name + "%").list();

	}

	@Override
	public User registerUser(User user) {
		log.info("registerUser(User user)");
		Session session = sessionFactory.getCurrentSession();
		User result = null;
		try {
			log.info("session.save(user)");
			log.info(user.getPassword());
			String encodedPassword = encoder
					.encryptPassword(user.getPassword());
			log.info(encodedPassword);
			user.setPassword(encodedPassword);
			log.info(encodedPassword);
			session.save(user);
			result = user;
		} catch (Throwable t) {
			log.warn("could not register user", t);
		}
		return result;
	}

	@Override
	public List<User> listUsers() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from User").list();
	}

	// TODO Blacklisted
	@Override
	public List<Message> getUsersMessages(Integer userId) {
		log.info("List<Message> getUsersMessages(Integer userId)");
		Session session = sessionFactory.getCurrentSession();
		// TODO left join fetch t.tag?
		List<Message> list = session
				.createQuery(
						"           select m                             "
								+ "	  from Message m                     "
								+ "	  left join fetch m.user u           "
								+ "	 where m.isRoot = true               "
								+ "    and m.messageId in (              "
								+ "		 select m.messageId              "
								+ "		   from Message m                "
								+ "		  where m.user.userId = :userId) "
								+ "		or m.messageId in (              "
								+ "		select t.message.messageId       "
								+ "		  from Tag t                     "
								+ "		 where t.user.userId = :userId)  "
								+ "	 order by m.lastUpdate desc          ")
				.setInteger("userId", userId).list();
		Hibernate.initialize(list);

		return list;
	}

	@Override
	public List<Follower> getUsersFriends(Integer userId) {
		Session session = sessionFactory.getCurrentSession();
		return session
				.createQuery(
						"           select f                             "
								+ "  from Follower f                     "
								+ "  left join fetch f.userByUserId uu   "
								+ "  left join fetch f.userByFriendId fu "
								+ " where f.userByUserId = :userId       "
								+ "   and f.isBlacklisted = false        "
								+ "    or f.userByFriendId = :userId     "
								+ " order by f.isApproved                ")
				.setInteger("userId", userId).list();
	}

	@Override
	public List<Group> getUsersGroups(Integer userId) {
		Session session = sessionFactory.getCurrentSession();
		return session
				.createQuery(
						"          select gu.group                 "
								+ "  from GroupUser gu             "
								+ " where gu.user.userId = :userId ")
				.setInteger("userId", userId).list();
	}

	@Override
	public Follower getFriendship(User friend, User user) {
		log.info("Follower getFriendship(User friend, User user)");
		log.info(user.toString());
		log.info(friend.toString());
		Follower result = null;
		Session session = sessionFactory.getCurrentSession();
		result = (Follower) session
				.createQuery(
						"            from Follower f "
								+ " where f.isApproved = true "
								+ "   and f.isBlacklisted = false "
								+ "   and ((f.userByUserId.userId = :user   and f.userByFriendId.userId = :friend) "
								+ "    or  (f.userByUserId.userId = :friend and f.userByFriendId.userId = :user))   ")
				.setInteger("user", user.getUserId())
				.setInteger("friend", friend.getUserId()).uniqueResult();
		if (result == null) {
			log.info("getFriendship returning null follower");
		} else {
			log.info("getFriendship returning follower");
		}
		return result;
	}

	@Override
	public Follower getFriendshipRequest(User friend, User user) {
		log.info("Follower getFriendshipRequest(User friend, User user)");
		log.info(user.toString());
		log.info(friend.toString());
		Follower result = null;
		Session session = sessionFactory.getCurrentSession();
		result = (Follower) session
				.createQuery(
						"            from Follower f "
								+ " where f.isApproved = false "
								+ "   and f.isBlacklisted = false "
								+ "   and ((f.userByUserId.userId = :user   and f.userByFriendId.userId = :friend) "
								+ "    or  (f.userByUserId.userId = :friend and f.userByFriendId.userId = :user))   ")
				.setInteger("user", user.getUserId())
				.setInteger("friend", friend.getUserId()).uniqueResult();
		if (result == null) {
			log.info("getFriendshipRequest returning null follower");
		} else {
			log.info("getFriendshipRequest returning follower");
		}
		return result;
	}

	@Override
	public void unfriend(Follower follower) {
		log.info("unfriend(Follower follower)");
		log.info(follower.toString());

		Session session = sessionFactory.getCurrentSession();
		log.info("session.delete(follower)");
		session.delete(follower);
	}

	@Override
	public void befriend(Follower follower) {
		log.info("befriend(Follower follower)");
		log.info(follower.toString());
		Session session = sessionFactory.getCurrentSession();
		log.info("session.save(follower)");
		session.save(follower);
	}

	@Override
	public void acceptRequest(Follower follower) {
		log.info("acceptRequest(Follower follower)");
		log.info(follower.toString());
		follower.setIsApproved(true);
		Session session = sessionFactory.getCurrentSession();
		log.info("session.update(follower).setIsApproved(true)");
		session.update(follower);
	}

	/**
	 * below methods for REST service
	 * 
	 */
	@Override
	public void makeUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		log.info("session.save(user)");
		log.info(user.getPassword());
		String encodedPassword = encoder.encryptPassword(user.getPassword());
		log.info(encodedPassword);

		user.setPassword(encodedPassword);
		log.info(encodedPassword);
		session.save(user);
	}

	@Override
	public User getUserById(int userId) {
		Session session = sessionFactory.getCurrentSession();
		User result = (User) session
				.createQuery("FROM User WHERE id = :userId")
				.setInteger("userId", userId).uniqueResult();

		return result;

	}

}