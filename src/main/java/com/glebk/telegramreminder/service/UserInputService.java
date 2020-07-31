package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.repository.model.Reminder;
import com.glebk.telegramreminder.service.model.BotState;
import com.glebk.telegramreminder.service.model.DataCache;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by glebk on 7/19/20
 */
@Service
public class UserInputService {

	private final DataCache dataCache;
	private final LocaleMessageService localeMessageService;
	private final RemindersService remindersService;

	private final List<BotState> newReminderStates = Arrays.asList(
			BotState.NEW_REMINDER_LOCATION,
			BotState.NEW_REMINDER_YEAR,
			BotState.NEW_REMINDER_MONTH,
			BotState.NEW_REMINDER_DAY,
			BotState.NEW_REMINDER_HOUR,
			BotState.NEW_REMINDER_MINUTE,
			BotState.NEW_REMINDER_TEXT);

	public UserInputService(DataCache dataCache,
							LocaleMessageService localeMessageService,
							RemindersService remindersService) {
		this.dataCache = dataCache;
		this.localeMessageService = localeMessageService;
		this.remindersService = remindersService;
	}

	public void handleInput(long chatId, String input) {
		BotState botState = dataCache.getChatCurrentBotState(chatId);
		Reminder reminder = dataCache.getChatNewReminderData(chatId);
		handleInput(botState, reminder, input);
	}

	private void handleInput(BotState botState, Reminder reminder, String input) {
		switch (botState) {
			case CHOOSE_LANGUAGE:
				handleChooseLanguage(reminder, input);
				break;
			case WHAT_SHOULD_I_DO:
				handleWhatShouldIDo(reminder, input);
				break;
			default:
		}
		boolean backOrCancelSelected = backOrCancelSelected(reminder, input);
		if (newReminderStates.contains(botState)) {
			handleNewReminderState(botState, reminder, input, backOrCancelSelected);
			return;
		}
		if (backOrCancelSelected) {
			return;
		}
		if (botState == BotState.DELETE_REMINDER) {
			handleDeleteReminder(reminder, input);
		}
	}

	private boolean backOrCancelSelected(Reminder reminder, String input) {
		return localeMessageService.getMessage(DataCache.INPUT_BACK, reminder.getLocale()).equalsIgnoreCase(input)
				|| localeMessageService.getMessage(DataCache.INPUT_CANCEL, reminder.getLocale()).equalsIgnoreCase(input);
	}

	private void handleNewReminderState(BotState botState, Reminder reminder, String input, boolean clear) {
		switch (botState) {
			case NEW_REMINDER_LOCATION:
				handleNewReminderLocation(reminder, clear ? null : input);
				break;
			case NEW_REMINDER_YEAR:
				handleNewReminderYear(reminder, clear ? null : Integer.parseInt(input));
				break;
			case NEW_REMINDER_MONTH:
				handleNewReminderMonth(reminder, clear ? null : Integer.parseInt(input));
				break;
			case NEW_REMINDER_DAY:
				handleNewReminderDay(reminder, clear ? null : Integer.parseInt(input));
				break;
			case NEW_REMINDER_HOUR:
				handleNewReminderHour(reminder, clear ? null : Integer.parseInt(input));
				break;
			case NEW_REMINDER_MINUTE:
				handleNewReminderMinute(reminder, clear ? null : Integer.parseInt(input));
				break;
			case NEW_REMINDER_TEXT:
				handleNewReminderText(reminder, clear ? null : input);
				break;
			default:
		}
		dataCache.setChatNewReminderData(reminder.getChatId(), reminder);
	}

	private void handleRemindersList(Long chatId) {
		remindersService.fillReminders(chatId);
	}

	private void handleDeleteReminder(Reminder reminder, String input) {
		remindersService.deleteReminder(dataCache.getChatReminders(reminder.getChatId()).get(Integer.parseInt(input) - 1));
	}

	private void handleChooseLanguage(Reminder reminder, String input) {
		for (String locale : DataCache.LOCALES) {
			if (Arrays.stream(BotState.CHOOSE_LANGUAGE.getInputMessages())
					.map(m -> localeMessageService.getMessage(m, locale).toLowerCase())
					.collect(Collectors.toList())
					.contains(input.toLowerCase())) {
				reminder.setLocale(locale);
				return;
			}
		}
	}

	private void handleWhatShouldIDo(Reminder reminder, String input) {
		if (localeMessageService.getMessage(DataCache.INPUT_REMINDERS_LIST, reminder.getLocale()).equalsIgnoreCase(input)) {
			handleRemindersList(reminder.getChatId());
		} else if (localeMessageService.getMessage(DataCache.INPUT_NEW_REMINDER, reminder.getLocale()).equalsIgnoreCase(input)) {
			handleNewReminderLocation(reminder, null);
		}
	}

	private void handleNewReminderLocation(Reminder reminder, String timeZoneId) {
		reminder.setTimeZoneId(timeZoneId);
		handleNewReminderYear(reminder, null);
	}

	private void handleNewReminderYear(Reminder reminder, Integer year) {
		reminder.setYear(year);
		handleNewReminderMonth(reminder, null);
	}

	private void handleNewReminderMonth(Reminder reminder, Integer month) {
		reminder.setMonth(month);
		handleNewReminderDay(reminder, null);
	}

	private void handleNewReminderDay(Reminder reminder, Integer day) {
		reminder.setDay(day);
		handleNewReminderHour(reminder, null);
	}

	private void handleNewReminderHour(Reminder reminder, Integer hour) {
		reminder.setHour(hour);
		handleNewReminderMinute(reminder, null);
	}

	private void handleNewReminderMinute(Reminder reminder, Integer minute) {
		reminder.setMinute(minute);
		reminder.setText(null);
	}

	private void handleNewReminderText(Reminder reminder, String text) {
		reminder.setText(text);
		remindersService.saveReminder(reminder);
	}

}
