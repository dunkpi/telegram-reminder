package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.repository.model.Reminder;
import com.glebk.telegramreminder.service.exception.ServiceException;
import com.glebk.telegramreminder.service.model.BotState;
import com.glebk.telegramreminder.service.model.DataCache;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/**
 * Created by glebk on 7/29/20
 */
@Service
public class ReplyTextService {

	private final LocaleMessageService localeMessageService;
	private final DataCache dataCache;

	public ReplyTextService(LocaleMessageService localeMessageService, DataCache dataCache) {
		this.localeMessageService = localeMessageService;
		this.dataCache = dataCache;
	}

	public String getReplyText(BotState botState, Reminder reminder, ServiceException exception) {
		if (exception == null || botState == BotState.START) {
			return getMessageText(botState, reminder);
		}
		return getErrorText(reminder, exception) + DataCache.TWO_STRING_SPLIT + getMessageText(botState, reminder);
	}

	private String getMessageText(BotState botState, Reminder reminder) {
		StringBuilder sb = new StringBuilder();
		if (botState == BotState.NEW_REMINDER_MONTH
				|| botState == BotState.NEW_REMINDER_DAY
				|| botState == BotState.NEW_REMINDER_HOUR
				|| botState == BotState.NEW_REMINDER_MINUTE
				|| botState == BotState.NEW_REMINDER_TEXT
				|| botState == BotState.NEW_REMINDER_FINISH) {
			sb.append(getNewReminderInfoText(reminder));
		}
		sb.append(localeMessageService.getMessage(botState.getReplyMessage(), reminder.getLocale()));
		if (botState == BotState.REMINDERS_LIST) {
			sb.append(getNotificationsList(reminder));
		}
		if (botState == BotState.NOTIFICATION) {
			sb.append(DataCache.STRING_SPLIT);
			sb.append(reminder.getText());
		}
		return sb.toString();
	}

	private String getErrorText(Reminder reminder, ServiceException exception) {
		if (exception.getInfo() == null) {
			return localeMessageService.getMessage(exception.getMessage(), reminder.getLocale());
		}
		if (exception.getPrefix() == null) {
			return  localeMessageService.getMessage(exception.getMessage(), reminder.getLocale()) + "\n" + exception.getInfo();
		}
		return localeMessageService.getMessage(exception.getMessage(), reminder.getLocale()) + "\n"
				+ localeMessageService.getMessage(exception.getPrefix(), reminder.getLocale()) + exception.getInfo();
	}

	private String getNewReminderInfoText(Reminder reminder) {
		StringBuilder sb = new StringBuilder();
		sb.append(localeMessageService.getMessage(DataCache.INFO_HEADER, reminder.getLocale()));
		if (reminder.getYear() != null) {
			appendPeriod(sb, reminder.getYear(), DataCache.INFO_YEAR, reminder.getLocale());
		}
		if (reminder.getMonth() != null) {
			appendPeriod(sb, reminder.getMonth(), DataCache.INFO_MONTH, reminder.getLocale());
		}
		if (reminder.getDay() != null) {
			appendPeriod(sb, reminder.getDay(), DataCache.INFO_DAY, reminder.getLocale());
		}
		if (reminder.getHour() != null) {
			appendPeriod(sb, reminder.getHour(), DataCache.INFO_HOUR, reminder.getLocale());
		}
		if (reminder.getMinute() != null) {
			appendPeriod(sb, reminder.getMinute(), DataCache.INFO_MINUTE, reminder.getLocale());
		}
		if (reminder.getText() != null) {
			sb.append(DataCache.STRING_SPLIT);
			sb.append(localeMessageService.getMessage(DataCache.INFO_TEXT, reminder.getLocale())).append(DataCache.SPACE);
			sb.append(reminder.getText());
		}
		sb.append(DataCache.TWO_STRING_SPLIT);
		return sb.toString();
	}

	private void appendPeriod(StringBuilder sb, int period, String periodName, String locale) {
		sb.append(DataCache.STRING_SPLIT);
		sb.append(localeMessageService.getMessage(periodName, locale));
		sb.append(DataCache.SPACE);
		sb.append(period < 0 ? localeMessageService.getMessage(getPeriodReplyMessage(periodName), locale) : period);
	}

	private String getPeriodReplyMessage(String periodName) {
		return DataCache.INFO_MINUTE.equals(periodName) ? DataCache.INPUT_ALL_MINUTES : DataCache.INPUT_ALL;
	}

	private String getNotificationsList(Reminder reminder) {
		List<Reminder> notifications = dataCache.getChatReminders(reminder.getChatId());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < notifications.size(); i++) {
			Reminder notification = notifications.get(i);
			sb.append(getNotificationText(notification, i, reminder.getLocale()));
		}
		return sb.toString();
	}

	private String getNotificationText(Reminder upd, int i, String locale) {
		final TextStyle textStyle = DataCache.EN_US_LOCALE.equals(locale) ? TextStyle.FULL : TextStyle.FULL_STANDALONE;
		StringBuilder sb = new StringBuilder();
		sb.append(DataCache.STRING_SPLIT);
		sb.append(i + 1).append(DataCache.DOT);
		sb.append(localeMessageService.getMessage(DataCache.INFO_YEAR, locale)).append(DataCache.SPACE);
		sb.append(upd.getYear() == -1 ? localeMessageService.getMessage(DataCache.EVERY_YEAR, locale) : upd.getYear());
		sb.append(DataCache.COMMA).append(localeMessageService.getMessage(DataCache.INFO_MONTH, locale)).append(DataCache.SPACE);
		sb.append(upd.getMonth() == -1
				? localeMessageService.getMessage(DataCache.EVERY_MONTH, locale)
				: Month.of(upd.getMonth()).getDisplayName(textStyle, new Locale.Builder().setLanguageTag(locale).build()));
		sb.append(DataCache.COMMA).append(localeMessageService.getMessage(DataCache.INFO_DAY, locale)).append(DataCache.SPACE);
		sb.append(upd.getDay() == -1 ? localeMessageService.getMessage(DataCache.EVERY_DAY, locale) : upd.getDay());
		sb.append(DataCache.COMMA).append(localeMessageService.getMessage(DataCache.INFO_HOUR, locale)).append(DataCache.SPACE);
		sb.append(upd.getHour() == -1 ? localeMessageService.getMessage(DataCache.EVERY_HOUR, locale) : upd.getHour());
		sb.append(DataCache.COMMA).append(localeMessageService.getMessage(DataCache.INFO_MINUTE, locale)).append(DataCache.SPACE);
		sb.append(upd.getMinute() == -1 ? localeMessageService.getMessage(DataCache.EVERY_MINUTE, locale) : upd.getMinute());
		sb.append(DataCache.COMMA).append(localeMessageService.getMessage(DataCache.INFO_TEXT, locale)).append(DataCache.SPACE);
		sb.append(upd.getText());
		return sb.toString();
	}

}
