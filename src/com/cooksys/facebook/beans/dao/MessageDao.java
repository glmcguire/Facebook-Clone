package com.cooksys.facebook.beans.dao;

import java.util.List;

import com.cooksys.facebook.model.Message;

public interface MessageDao {
	public List<Message> listMessages();
	public List<Message> listRootMessages();
	public List<Message> searchMessagesForString(String messageFragement);
	public Message getMessage(Short id);
	public Message postNewMessage(Message message, String tags);
	public List<Message> listMessageResponses(Message message);
	public void makeMessage(Message message);
}
