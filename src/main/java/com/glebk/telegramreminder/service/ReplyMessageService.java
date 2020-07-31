package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.repository.model.Reminder;
import com.glebk.telegramreminder.service.model.BotState;
import com.glebk.telegramreminder.service.model.DataCache;
import com.glebk.telegramreminder.service.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Created by glebk on 7/17/20
 */
@Service
public class ReplyMessageService {

	private final DataCache dataCache;
	private final ReplyMarkupService replyMarkupService;
	private final ReplyTextService replyTextService;

	public ReplyMessageService(DataCache dataCache,
							   ReplyMarkupService replyMarkupService,
							   ReplyTextService replyTextService) {
		this.dataCache = dataCache;
		this.replyMarkupService = replyMarkupService;
		this.replyTextService = replyTextService;
	}

	public SendMessage getReplyMessage(long chatId) {
		return getReplyMessage(dataCache.getChatCurrentBotState(chatId), dataCache.getChatNewReminderData(chatId), null);
	}

	public SendMessage getReplyMessage(long chatId, ServiceException exception) {
		return getReplyMessage(dataCache.getChatCurrentBotState(chatId), dataCache.getChatNewReminderData(chatId), exception);
	}

	public SendMessage getReplyMessage(BotState botState, Reminder reminder, ServiceException exception) {
		SendMessage sendMessage = new SendMessage(reminder.getChatId(), replyTextService.getReplyText(botState, reminder, exception));
		sendMessage.setReplyMarkup(replyMarkupService.getInlineKeyboardMarkup(botState, reminder));
		return sendMessage;
	}

}
