package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.repository.model.Reminder;
import com.glebk.telegramreminder.service.exception.DuplicateNotificationException;
import com.glebk.telegramreminder.service.exception.IncorrectInputException;
import com.glebk.telegramreminder.service.exception.ServiceException;
import com.glebk.telegramreminder.service.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Created by glebk on 7/28/20
 */
@Service
public class UserInputValidationService {

	private final DataCache dataCache;
	private final LocaleMessageService localeMessageService;
	private final RemindersService remindersService;

	public UserInputValidationService(DataCache dataCache, LocaleMessageService localeMessageService, RemindersService remindersService) {
		this.dataCache = dataCache;
		this.localeMessageService = localeMessageService;
		this.remindersService = remindersService;
	}

	public void validate(long chatId, String input) throws ServiceException {
		BotState botState = dataCache.getChatCurrentBotState(chatId);
		if (Arrays.stream(botState.getInputMessages())
				.map(m -> localeMessageService.getMessage(m, dataCache.getChatNewReminderData(chatId).getLocale()).toLowerCase())
				.collect(Collectors.toList())
				.contains(input.toLowerCase())) {
			return;
		}
		switch (botState) {
			case START:
				validateStart(input);
				return;
			case CHOOSE_LANGUAGE:
				validateLanguage(input);
				return;
			case NEW_REMINDER_LOCATION:
				validateLocation(input);
				return;
			case NEW_REMINDER_YEAR:
				validateYear(dataCache.getChatNewReminderData(chatId), input);
				return;
			case NEW_REMINDER_MONTH:
				validateMonth(dataCache.getChatNewReminderData(chatId), input);
				return;
			case NEW_REMINDER_DAY:
				validateDay(dataCache.getChatNewReminderData(chatId), input);
				return;
			case NEW_REMINDER_HOUR:
				validateHour(dataCache.getChatNewReminderData(chatId), input);
				return;
			case NEW_REMINDER_MINUTE:
				validateMinute(dataCache.getChatNewReminderData(chatId), input);
				return;
			case NEW_REMINDER_TEXT:
				validateText(input);
				return;
			case DELETE_REMINDER:
				validateDelete(dataCache.getChatReminders(chatId), input);
				return;
			default:
		}
		throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
	}

	private void validateStart(String input) throws IncorrectInputException {
		if (!Arrays.stream(BotState.START.getInputMessages())
				.map(m -> localeMessageService.getMessage(m, null).toLowerCase())
				.collect(Collectors.toList())
				.contains(input.toLowerCase())) {
			throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
		}
	}

	private void validateLanguage(String input) throws IncorrectInputException {
		for (String locale : DataCache.LOCALES) {
			if (Arrays.stream(BotState.CHOOSE_LANGUAGE.getInputMessages())
					.map(m -> localeMessageService.getMessage(m, locale).toLowerCase())
					.collect(Collectors.toList())
					.contains(input.toLowerCase())) {
				return;
			}
		}
		throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
	}

	private void validateLocation(String input) throws IncorrectInputException {
		if (TimeZone.getTimeZone(input) == null) {
			throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
		}
	}

	private void validateYear(Reminder reminder, String input) throws IncorrectInputException {
		try {
			int inputYear = Integer.parseInt(input);
			if (inputYear == -1) {
				return;
			}
			LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
			if (inputYear < now.getYear()) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
		} catch (NumberFormatException e) {
			throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
		}
	}

	private void validateMonth(Reminder reminder, String input) throws IncorrectInputException {
		try {
			int inputMonth = Integer.parseInt(input);
			if (inputMonth == -1) {
				return;
			}
			if (inputMonth < 1 || inputMonth > 12) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
			LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
			if (reminder.getYear() == now.getYear() && inputMonth < now.getMonthValue()) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
		} catch (NumberFormatException e) {
			throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
		}
	}

	private void validateDay(Reminder reminder, String input) throws IncorrectInputException {
		try {
			int inputDay = Integer.parseInt(input);
			if (inputDay == -1) {
				return;
			}
			LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
			int monthLength = LocalDateTime.of(now.getYear(), reminder.getMonth() > 0 ? reminder.getMonth() : 1, 1, 1, 1)
					.getMonth().length(reminder.getYear() < 0 || TimeZoneService.isLeapYear(reminder.getYear()));
			if (inputDay < 1 || inputDay > monthLength) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
			if (reminder.getYear() == now.getYear()
					&& reminder.getMonth() == now.getMonthValue()
					&& inputDay < now.getDayOfMonth()) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
		} catch (NumberFormatException e) {
			throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
		}
	}

	private void validateHour(Reminder reminder, String input) throws IncorrectInputException {
		try {
			int inputHour = Integer.parseInt(input);
			if (inputHour == -1) {
				return;
			}
			if (inputHour < 0 || inputHour > 23) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
			LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
			if (reminder.getYear() == now.getYear()
					&& reminder.getMonth() == now.getMonthValue()
					&& reminder.getDay() == now.getDayOfMonth()
					&& inputHour < now.getHour()) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
		} catch (NumberFormatException e) {
			throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
		}
	}

	private void validateMinute(Reminder reminder, String input) throws ServiceException {
		try {
			int inputMinute = Integer.parseInt(input);
			if (inputMinute == -1) {
				return;
			}
			if (inputMinute < 0 || inputMinute > 59) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
			LocalDateTime now = LocalDateTime.now(ZoneId.of(reminder.getTimeZoneId()));
			if (reminder.getYear() == now.getYear()
					&& reminder.getMonth() == now.getMonthValue()
					&& reminder.getDay() == now.getDayOfMonth()
					&& reminder.getHour() == now.getHour()
					&& inputMinute <= now.getMinute()) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
			Reminder testReminder = new Reminder();
			testReminder.setChatId(reminder.getChatId());
			testReminder.setTimeZoneId(reminder.getTimeZoneId());
			testReminder.setYear(reminder.getYear());
			testReminder.setMonth(reminder.getMonth());
			testReminder.setDay(reminder.getDay());
			testReminder.setHour(reminder.getHour());
			testReminder.setMinute(inputMinute);
			Reminder existingReminder = remindersService.getReminder(testReminder);
			if (existingReminder != null) {
				throw new DuplicateNotificationException(
						DataCache.ERROR_DUPLICATE_NOTIFICATION,
						DataCache.ERROR_DUPLICATE_NOTIFICATION_TEXT_PREFIX,
						" " + existingReminder.getText());
			}
		} catch (NumberFormatException e) {
			throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
		}
	}

	private void validateText(String input) throws IncorrectInputException {
		if (input.length() > 255) {
			throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
		}
	}

	private void validateDelete(List<Reminder> chatNotifications, String input) throws IncorrectInputException {
		try {
			int notificationNumber = Integer.parseInt(input);
			if (chatNotifications.size() < notificationNumber || notificationNumber <= 0) {
				throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
			}
		} catch (NumberFormatException e) {
			throw new IncorrectInputException(DataCache.ERROR_INCORRECT_INPUT, null, null);
		}
	}
}
