package com.glebk.telegramreminder.service.model;

import com.glebk.telegramreminder.repository.model.Reminder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by glebk on 7/17/20
 */
@Getter
@Service
public class DataCache {

	public static final String REPLY_START = "reply.start";
	public static final String REPLY_LANGUAGE = "reply.language";
	public static final String REPLY_WHAT_TO_DO = "reply.whatToDo";
	public static final String REPLY_YEAR = "reply.year";
	public static final String REPLY_MONTH = "reply.month";
	public static final String REPLY_DAY = "reply.day";
	public static final String REPLY_HOUR = "reply.hour";
	public static final String REPLY_MINUTE = "reply.minute";
	public static final String REPLY_TEXT = "reply.text";
	public static final String REPLY_LOCATION = "reply.location";
	public static final String REPLY_FINISH = "reply.finish";
	public static final String REPLY_LIST = "reply.list";
	public static final String REPLY_DELETE = "reply.delete";
	public static final String REPLY_DELETED = "reply.deleted";
	public static final String REPLY_NOTIFICATION = "reply.notification";

	public static final String INFO_HEADER = "info.header";
	public static final String INFO_YEAR = "info.year";
	public static final String INFO_MONTH = "info.month";
	public static final String INFO_DAY = "info.day";
	public static final String INFO_HOUR = "info.hour";
	public static final String INFO_MINUTE = "info.minute";
	public static final String INFO_TEXT = "info.text";

	public static final String INPUT_START = "input.start";
	public static final String INPUT_LANGUAGE = "input.language";
	public static final String INPUT_NEW_REMINDER = "input.newReminder";
	public static final String INPUT_LOCATION_SHARE = "input.locationShare";
	public static final String INPUT_LOCATION_DONT_SHARE = "input.locationDontShare";
	public static final String INPUT_DELETE_REMINDER = "input.deleteReminder";
	public static final String INPUT_REMINDERS_LIST = "input.remindersList";
	public static final String INPUT_BACK = "input.back";
	public static final String INPUT_CANCEL = "input.cancel";
	public static final String INPUT_ALL = "input.all";
	public static final String INPUT_ALL_MINUTES = "input.allMinutes";

	public static final String YEAR_CURRENT = "year.current";
	public static final String YEAR_NEXT = "year.next";

	public static final String EVERY_YEAR = "every.year";
	public static final String EVERY_MONTH = "every.month";
	public static final String EVERY_DAY = "every.day";
	public static final String EVERY_HOUR = "every.hour";
	public static final String EVERY_MINUTE = "every.minute";

	public static final String ERROR_INCORRECT_INPUT = "error.incorrectInput";
	public static final String ERROR_DUPLICATE_NOTIFICATION = "error.duplicateNotification";
	public static final String ERROR_DUPLICATE_NOTIFICATION_TEXT_PREFIX = "error.duplicateNotificationTextPrefix";
	public static final String ERROR_UNEXPECTED_ERROR = "error.unexpectedError";

	public static final String EMPTY = "";
	public static final String SPACE = " ";
	public static final String COMMA = ", ";
	public static final String DOT = ". ";
	public static final String STRING_SPLIT = "\n";
	public static final String TWO_STRING_SPLIT = "\n\n";
	public static final String MINUS_ONE = "-1";

	public static final String EN_US_LOCALE = "en-US";
	public static final String RU_RU_LOCALE = "ru-RU";
	public static final List<String> LOCALES = Arrays.asList(EN_US_LOCALE, RU_RU_LOCALE);

	private final Map<Long, BotState> chatBotState = new HashMap<>();
    private final Map<Long, Reminder> chatNewReminderData = new HashMap<>();
    private final Map<Long, List<Reminder>> chatReminders = new HashMap<>();
    private final Map<Long, LocalDateTime> chatLastActivity = new HashMap<>();

	public BotState getChatCurrentBotState(long chatId) {
		BotState bptState = chatBotState.get(chatId);
		return bptState == null ? BotState.START : bptState;
	}

	public void setChatCurrentBotState(long chatId, BotState botState) {
		this.chatBotState.put(chatId, botState);
	}

	public Reminder getChatNewReminderData(long chatId) {
		Reminder profileData = chatNewReminderData.get(chatId);
		return profileData == null ? new Reminder() : profileData;
	}

	public void setChatNewReminderData(long chatId, Reminder profileData) {
		chatNewReminderData.put(chatId, profileData);
	}

	public List<Reminder> getChatReminders(long chatId) {
		List<Reminder> notifications = chatReminders.get(chatId);
		return notifications == null ? Collections.emptyList() : notifications;
	}

	public void setChatReminders(long chatId, List<Reminder> reminders) {
		chatReminders.put(chatId, reminders);
	}

	public void setChatLastActivity(long chatId, LocalDateTime lastActivity) {
		chatLastActivity.put(chatId, lastActivity);
	}
}
