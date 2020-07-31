package com.glebk.telegramreminder.service.model;

import lombok.Getter;

/**
 * Created by glebk on 7/17/20
 */
@Getter
public enum BotState {

	START(null,
			DataCache.REPLY_START,
			new String[]{DataCache.INPUT_START}),
	CHOOSE_LANGUAGE(START,
			DataCache.REPLY_LANGUAGE,
			new String[]{DataCache.INPUT_LANGUAGE}),
	WHAT_SHOULD_I_DO(CHOOSE_LANGUAGE,
			DataCache.REPLY_WHAT_TO_DO,
			new String[]{DataCache.INPUT_NEW_REMINDER, DataCache.INPUT_REMINDERS_LIST, DataCache.INPUT_BACK}),
	NEW_REMINDER_LOCATION(WHAT_SHOULD_I_DO,
			DataCache.REPLY_LOCATION,
			new String[]{DataCache.INPUT_BACK, DataCache.INPUT_CANCEL}),
	NEW_REMINDER_YEAR(NEW_REMINDER_LOCATION,
			DataCache.REPLY_YEAR,
			new String[]{DataCache.INPUT_ALL, DataCache.INPUT_BACK, DataCache.INPUT_CANCEL}),
	NEW_REMINDER_MONTH(NEW_REMINDER_YEAR,
			DataCache.REPLY_MONTH,
			new String[]{DataCache.INPUT_ALL, DataCache.INPUT_BACK, DataCache.INPUT_CANCEL}),
	NEW_REMINDER_DAY(NEW_REMINDER_MONTH,
			DataCache.REPLY_DAY,
			new String[]{DataCache.INPUT_ALL, DataCache.INPUT_BACK, DataCache.INPUT_CANCEL}),
	NEW_REMINDER_HOUR(NEW_REMINDER_DAY,
			DataCache.REPLY_HOUR,
			new String[]{DataCache.INPUT_ALL, DataCache.INPUT_BACK, DataCache.INPUT_CANCEL}),
	NEW_REMINDER_MINUTE(NEW_REMINDER_HOUR,
			DataCache.REPLY_MINUTE,
			new String[]{DataCache.INPUT_ALL_MINUTES, DataCache.INPUT_BACK, DataCache.INPUT_CANCEL}),
	NEW_REMINDER_TEXT(NEW_REMINDER_MINUTE,
			DataCache.REPLY_TEXT,
			new String[]{DataCache.INPUT_BACK, DataCache.INPUT_CANCEL}),
	NEW_REMINDER_FINISH(WHAT_SHOULD_I_DO,
			DataCache.REPLY_FINISH,
			new String[]{DataCache.INPUT_BACK}),
	REMINDERS_LIST(WHAT_SHOULD_I_DO,
			DataCache.REPLY_LIST,
			new String[]{DataCache.INPUT_DELETE_REMINDER, DataCache.INPUT_BACK}),
	DELETE_REMINDER(REMINDERS_LIST,
			DataCache.REPLY_DELETE,
			new String[]{DataCache.INPUT_BACK, DataCache.INPUT_CANCEL}),
	DELETE_REMINDER_FINISH(WHAT_SHOULD_I_DO,
			DataCache.REPLY_DELETED,
			new String[]{DataCache.INPUT_BACK}),
	NOTIFICATION(WHAT_SHOULD_I_DO,
			DataCache.REPLY_NOTIFICATION,
			new String[]{DataCache.INPUT_BACK});

	private final BotState previousState;
	private final String replyMessage;
	private final String[] inputMessages;

	BotState(BotState previousState, String replyMessage, String[] inputMessages) {
		this.previousState = previousState;
		this.replyMessage = replyMessage;
		this.inputMessages = inputMessages;
	}
}
