package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.repository.model.Reminder;
import com.glebk.telegramreminder.service.model.BotState;
import com.glebk.telegramreminder.service.model.DataCache;
import org.springframework.stereotype.Service;

/**
 * Created by glebk on 7/29/20
 */
@Service
public class BotStateService {

	private final DataCache dataCache;
	private final LocaleMessageService localeMessageService;

	public BotStateService(DataCache dataCache, LocaleMessageService localeMessageService) {
		this.dataCache = dataCache;
		this.localeMessageService = localeMessageService;
	}

	public void updateBotState(long chatId, String input) {
		BotState botState = dataCache.getChatCurrentBotState(chatId);
		Reminder reminder = dataCache.getChatNewReminderData(chatId);
		if (localeMessageService.getMessage(DataCache.INPUT_BACK, reminder.getLocale()).equalsIgnoreCase(input)) {
			dataCache.setChatCurrentBotState(chatId, botState.getPreviousState());
			return;
		}
		if (localeMessageService.getMessage(DataCache.INPUT_CANCEL, reminder.getLocale()).equalsIgnoreCase(input)) {
			dataCache.setChatCurrentBotState(chatId, BotState.WHAT_SHOULD_I_DO);
			return;
		}
		switch (botState) {
			case START:
				dataCache.setChatCurrentBotState(chatId, BotState.CHOOSE_LANGUAGE);
				break;
			case CHOOSE_LANGUAGE:
				dataCache.setChatCurrentBotState(chatId, BotState.WHAT_SHOULD_I_DO);
				return;
			case WHAT_SHOULD_I_DO:
				if (localeMessageService.getMessage(DataCache.INPUT_REMINDERS_LIST, reminder.getLocale()).equalsIgnoreCase(input)) {
					dataCache.setChatCurrentBotState(chatId, BotState.REMINDERS_LIST);
				} else if (localeMessageService.getMessage(DataCache.INPUT_NEW_REMINDER, reminder.getLocale()).equalsIgnoreCase(input)) {
					dataCache.setChatCurrentBotState(chatId, BotState.NEW_REMINDER_LOCATION);
				}
				return;
			case NEW_REMINDER_LOCATION:
				dataCache.setChatCurrentBotState(chatId, BotState.NEW_REMINDER_YEAR);
				return;
			case NEW_REMINDER_YEAR:
				dataCache.setChatCurrentBotState(chatId, BotState.NEW_REMINDER_MONTH);
				return;
			case NEW_REMINDER_MONTH:
				dataCache.setChatCurrentBotState(chatId, BotState.NEW_REMINDER_DAY);
				return;
			case NEW_REMINDER_DAY:
				dataCache.setChatCurrentBotState(chatId, BotState.NEW_REMINDER_HOUR);
				return;
			case NEW_REMINDER_HOUR:
				dataCache.setChatCurrentBotState(chatId, BotState.NEW_REMINDER_MINUTE);
				return;
			case NEW_REMINDER_MINUTE:
				dataCache.setChatCurrentBotState(chatId, BotState.NEW_REMINDER_TEXT);
				return;
			case NEW_REMINDER_TEXT:
				dataCache.setChatCurrentBotState(chatId, BotState.NEW_REMINDER_FINISH);
				return;
			case REMINDERS_LIST:
				dataCache.setChatCurrentBotState(chatId, BotState.DELETE_REMINDER);
				return;
			case DELETE_REMINDER:
				dataCache.setChatCurrentBotState(chatId, BotState.DELETE_REMINDER_FINISH);
				return;
			default:
		}
	}
}
