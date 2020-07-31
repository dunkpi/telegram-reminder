package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.repository.model.Reminder;
import com.glebk.telegramreminder.service.model.BotState;
import com.glebk.telegramreminder.service.model.DataCache;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by glebk on 7/29/20
 */
@Service
public class ReplyMarkupService {

	private final LocaleMessageService localeMessageService;
	private final DataCache dataCache;

	public ReplyMarkupService(LocaleMessageService localeMessageService, DataCache dataCache) {
		this.localeMessageService = localeMessageService;
		this.dataCache = dataCache;
	}

	public ReplyKeyboard getInlineKeyboardMarkup(BotState botState, Reminder reminder) {
		if (botState == BotState.NOTIFICATION) {
			return null;
		}
		List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		switch (botState) {
			case NEW_REMINDER_LOCATION:
				rowList.addAll(getLocationsRows());
				break;
			case NEW_REMINDER_YEAR:
				rowList.add(getYearsRow(reminder));
				break;
			case NEW_REMINDER_MONTH:
				rowList.addAll(getMonthsRows(reminder));
				break;
			case NEW_REMINDER_DAY:
				rowList.addAll(getDaysRows(reminder));
				break;
			case NEW_REMINDER_HOUR:
				rowList.addAll(getHoursRows(reminder));
				break;
			case NEW_REMINDER_MINUTE:
				rowList.addAll(getMinutesRows(reminder));
				break;
			case DELETE_REMINDER:
				rowList.addAll(getRemindersRows(reminder.getChatId()));
				break;
			default:
		}
		rowList.add(getLastButtonRow(botState, reminder));
		inlineKeyboardMarkup.setKeyboard(rowList);
		return inlineKeyboardMarkup;
	}

	private Collection<? extends List<InlineKeyboardButton>> getLocationsRows() {
		List<List<InlineKeyboardButton>> locationRows = new ArrayList<>();
		List<TimeZoneService.TimeZoneWithDisplayNames> returnedZones = TimeZoneService.getInstance().getTimeZones();
		returnedZones.forEach(zone -> {
			ArrayList<InlineKeyboardButton> locationRow = new ArrayList<>();
			InlineKeyboardButton button = new InlineKeyboardButton().setText(zone.getDisplayName());
			button.setCallbackData(zone.getTimeZone().getID());
			locationRow.add(button);
			locationRows.add(locationRow);
		});
		return locationRows;
	}

	private List<InlineKeyboardButton> getYearsRow(Reminder reminder) {
		LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
		ArrayList<InlineKeyboardButton> yearsRow = new ArrayList<>();
		InlineKeyboardButton button = new InlineKeyboardButton().setText(localeMessageService.getMessage(DataCache.YEAR_CURRENT, reminder.getLocale()));
		button.setCallbackData(DataCache.EMPTY + now.getYear());
		yearsRow.add(button);
		button = new InlineKeyboardButton().setText(localeMessageService.getMessage(DataCache.YEAR_NEXT, reminder.getLocale()));
		button.setCallbackData(DataCache.EMPTY + (now.getYear() + 1));
		yearsRow.add(button);
		return yearsRow;
	}

	private Collection<? extends List<InlineKeyboardButton>> getMonthsRows(Reminder reminder) {
		LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
		List<List<InlineKeyboardButton>> monthsRows = new ArrayList<>();
		List<InlineKeyboardButton> monthsRow = new ArrayList<>();
		for (Month month : Month.values()) {
			if (monthsRow.size() > 3) {
				monthsRows.add(monthsRow);
				monthsRow = new ArrayList<>();
			}
			if (reminder.getYear() == now.getYear() && month.getValue() < now.getMonthValue()) {
				continue;
			}
			InlineKeyboardButton button = new InlineKeyboardButton()
					.setText(month.getDisplayName(DataCache.EN_US_LOCALE.equals(reminder.getLocale())
							? TextStyle.FULL
							: TextStyle.FULL_STANDALONE, new Locale.Builder().setLanguageTag(reminder.getLocale()).build()));
			button.setCallbackData(DataCache.EMPTY + month.getValue());
			monthsRow.add(button);
		}
		if (monthsRow.size() > 0) {
			monthsRows.add(monthsRow);
		}
		return monthsRows;
	}

	private Collection<? extends List<InlineKeyboardButton>> getDaysRows(Reminder reminder) {
		LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
		List<List<InlineKeyboardButton>> daysRows = new ArrayList<>();
		List<InlineKeyboardButton> daysRow = new ArrayList<>();
		int monthLength = LocalDateTime.of(now.getYear(), reminder.getMonth() > 0 ? reminder.getMonth() : 1, 1, 1, 1)
				.getMonth().length(reminder.getYear() < 0 || TimeZoneService.isLeapYear(reminder.getYear()));
		for (int i = 1; i <= monthLength; i++) {
			if (daysRow.size() > 7) {
				daysRows.add(daysRow);
				daysRow = new ArrayList<>();
			}
			if (reminder.getYear() == now.getYear() && reminder.getMonth() == now.getMonthValue() && i < now.getDayOfMonth()) {
				continue;
			}
			InlineKeyboardButton button = new InlineKeyboardButton().setText(DataCache.EMPTY + i);
			button.setCallbackData(DataCache.EMPTY + i);
			daysRow.add(button);
		}
		if (daysRow.size() > 0) {
			daysRows.add(daysRow);
		}
		return daysRows;
	}

	private Collection<? extends List<InlineKeyboardButton>> getHoursRows(Reminder reminder) {
		LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
		List<List<InlineKeyboardButton>> hoursRows = new ArrayList<>();
		ArrayList<InlineKeyboardButton> hoursRow = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			if (hoursRow.size() > 7) {
				hoursRows.add(hoursRow);
				hoursRow = new ArrayList<>();
			}
			if (reminder.getYear() == now.getYear()
					&& reminder.getMonth() == now.getMonthValue()
					&& reminder.getDay() == now.getDayOfMonth()
					&& i < now.getHour()) {
				continue;
			}
			InlineKeyboardButton button = new InlineKeyboardButton().setText(DataCache.EMPTY + i);
			button.setCallbackData(DataCache.EMPTY + i);
			hoursRow.add(button);
		}
		if (hoursRow.size() > 0) {
			hoursRows.add(hoursRow);
		}
		return hoursRows;
	}

	private Collection<? extends List<InlineKeyboardButton>> getMinutesRows(Reminder reminder) {
		LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
		List<List<InlineKeyboardButton>> minutesRows = new ArrayList<>();
		ArrayList<InlineKeyboardButton> minutesRow = new ArrayList<>();
		for (int i = 0; i < 60; i++) {
			if (minutesRow.size() > 7) {
				minutesRows.add(minutesRow);
				minutesRow = new ArrayList<>();
			}
			if (reminder.getYear() == now.getYear()
					&& reminder.getMonth() == now.getMonthValue()
					&& reminder.getDay() == now.getDayOfMonth()
					&& reminder.getHour() == now.getHour()
					&& i < now.getMinute()) {
				continue;
			}
			InlineKeyboardButton button = new InlineKeyboardButton().setText(DataCache.EMPTY + i);
			button.setCallbackData(DataCache.EMPTY + i);
			minutesRow.add(button);
		}
		if (minutesRow.size() > 0) {
			minutesRows.add(minutesRow);
		}
		return minutesRows;
	}

	private Collection<? extends List<InlineKeyboardButton>> getRemindersRows(Long chatId) {
		final List<Reminder> chatNotifications = dataCache.getChatReminders(chatId);
		if (chatNotifications.size() > 50) {
			return Collections.emptyList();
		}
		List<List<InlineKeyboardButton>> remindersRows = new ArrayList<>();
		ArrayList<InlineKeyboardButton> remindersRow = new ArrayList<>();
		for (int i = 1; i <= chatNotifications.size(); i++) {
			if (remindersRow.size() > 9) {
				remindersRows.add(remindersRow);
				remindersRow = new ArrayList<>();
			}
			InlineKeyboardButton button = new InlineKeyboardButton().setText(DataCache.EMPTY + i);
			button.setCallbackData(DataCache.EMPTY + i);
			remindersRow.add(button);
		}
		if (remindersRow.size() > 0) {
			remindersRows.add(remindersRow);
		}
		return remindersRows;
	}

	private List<InlineKeyboardButton> getLastButtonRow(BotState botState, Reminder reminder) {
		List<InlineKeyboardButton> lastButtonRow = new ArrayList<>();
		if (botState == BotState.CHOOSE_LANGUAGE) {
			for (String locale : DataCache.LOCALES) {
				Arrays.stream(botState.getInputMessages())
						.map(m -> localeMessageService.getMessage(m, locale))
						.collect(Collectors.toList())
						.forEach(replyMessage -> {
							InlineKeyboardButton button = new InlineKeyboardButton().setText(replyMessage);
							button.setCallbackData(replyMessage);
							lastButtonRow.add(button);
						});
			}
		} else {
			List<String> patternsForEachPeriodMessages = Arrays.stream(botState.getInputMessages())
					.filter(m -> DataCache.INPUT_ALL.equals(m) || DataCache.INPUT_ALL_MINUTES.equals(m))
					.map(m -> localeMessageService.getMessage(m, reminder.getLocale()))
					.collect(Collectors.toList());
			Arrays.stream(botState.getInputMessages())
					.filter(m -> botState != BotState.REMINDERS_LIST
							|| dataCache.getChatReminders(reminder.getChatId()).size() > 0
							|| !DataCache.INPUT_DELETE_REMINDER.equalsIgnoreCase(m))
					.map(m -> localeMessageService.getMessage(m, reminder.getLocale()))
					.collect(Collectors.toList())
					.forEach(replyMessage -> {
						InlineKeyboardButton button = new InlineKeyboardButton().setText(replyMessage);
						button.setCallbackData(patternsForEachPeriodMessages.contains(replyMessage) ? DataCache.MINUS_ONE : replyMessage);
						lastButtonRow.add(button);
					});
		}
		return lastButtonRow;
	}

}
