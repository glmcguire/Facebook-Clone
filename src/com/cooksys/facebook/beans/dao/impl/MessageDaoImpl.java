package com.cooksys.facebook.beans.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cooksys.facebook.beans.dao.GroupDao;
import com.cooksys.facebook.beans.dao.MessageDao;
import com.cooksys.facebook.beans.dao.UserDao;
import com.cooksys.facebook.model.Group;
import com.cooksys.facebook.model.Message;
import com.cooksys.facebook.model.Tag;
import com.cooksys.facebook.model.User;

@Repository
@Transactional
@SuppressWarnings("unchecked")
public class MessageDaoImpl implements MessageDao {
	private Logger log = LoggerFactory.getLogger(MessageDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserDao userDao;
	@Autowired
	private GroupDao groupDao;

	@Override
	public List<Message> listMessages() {
		log.info("List<Message> listMessages()");
		Session session = sessionFactory.getCurrentSession();
		log.info("return session {}", session);
		return session.createQuery("from Message").list();
	}

	@Override
	public Message getMessage(Short id) {
		log.info("getMessage(Short id)");
		Session session = sessionFactory.getCurrentSession();
		log.info("return session {}", session);
		return (Message) session.get(Message.class, id);
	}

	@Override
	public List<Message> searchMessagesForString(String messageFragement) {
		log.info("List<Message> searchMessagesForString(String messageFragement)");
		Session session = sessionFactory.getCurrentSession();
		log.info("return session {}", session);
		return session
				.createQuery(
						"select t from Message t where t.message like :messageFragment")
				.setString("messageFragment", "%" + messageFragement + "%")
				.list();
	}

	@Override
	public List<Message> listRootMessages() {
		log.info("List<Message> listRootMessages()");
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery(
				"          select m from Message m     "
						+ "  left join fetch m.user u  "
						+ " where m.isRoot = true      "
						+ " order by m.lastUpdate desc ").list();
	}

	@Override
	public List<Message> listMessageResponses(Message message) {
		log.info("List<Message> listMessageResponses(Message message)");
		Session session = sessionFactory.getCurrentSession();
		log.info("return session {}", session);
		// TODO left join fetch t.tag ?
		return session
				.createQuery(
						"select m from Response r"
								+ "left join fetch r.messageByMessageId m "
								+ "left join fetch m.user u "
								+ "where r.messageByRootId = :message "
								+ "order by m.lastUpdate")
				.setEntity("message", message).list();
	}

	@Override
	public Message postNewMessage(Message message, String tags) {
		log.info("Message postMessage(Message message)");
		Session session = sessionFactory.getCurrentSession();
		Message result = null;
		try {
			log.info("session.save(message)");
			log.info(message.getMessage());
			log.info(message.getUser().getName());
			session.save(message);

			String[] tagArray = tags.split(",", -1);
			for (String singletag : tagArray) {
				Tag newtag = new Tag();
				newtag.setMessage(message);
				newtag = searchForTag(singletag);

				if (newtag.getUser() != null || newtag.getGroupId() != null) {
					session.save(newtag);
				}
			}

			result = message;
		} catch (Throwable t) {
			log.warn("could not post message", t);
		}
		return result;
	}

	private Tag searchForTag(String singletag) {
		Tag newtag = new Tag();
		List<User> users = userDao.getUserByName(singletag.trim());
		if (users.size() == 1) {
			newtag.setUser(users.get(0));
		}

		List<Group> groups = groupDao.searchGroupByName(singletag.trim());
		if (groups.size() == 1) {
			newtag.setGroupId(groups.get(0).getGroupId());
		}
		return newtag;
	}

	/**
	 * below methods for REST service
	 * 
	 */
	@Override
	public void makeMessage(Message message) {
		Session session = sessionFactory.getCurrentSession();
		log.info("session.save(mesage)");
		session.save(message);
	}

}
