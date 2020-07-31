package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.repository.RemindersDAL;
import com.glebk.telegramreminder.repository.model.Reminder;
import com.glebk.telegramreminder.service.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by glebk on 7/24/20
 */
@Service
public class RemindersService {

	private final DataCache dataCache;
	private final RemindersDAL remindersDAL;

	public RemindersService(DataCache dataCache, RemindersDAL remindersDAL) {
		this.dataCache = dataCache;
		this.remindersDAL = remindersDAL;
	}

	public List<Reminder> getCurrentTimeReminders() {
		return TimeZoneService.fromDefaultTime(remindersDAL.getCurrentTimeReminders(getNowDefaultTimeReminder()));
	}

	public Reminder getReminder(Reminder reminder) {
		return remindersDAL.get(TimeZoneService.toDefaultTime(reminder));
	}

	public void saveReminder(Reminder reminder) {
		remindersDAL.save(TimeZoneService.toDefaultTime(reminder));
	}

	public void fillReminders(long chatId) {
		Reminder reminder = getNowDefaultTimeReminder();
		reminder.setChatId(chatId);
		dataCache.setChatReminders(chatId, TimeZoneService.fromDefaultTime(remindersDAL.getChatActualReminders(reminder)));
	}

	public void deleteReminder(Reminder reminder) {
		remindersDAL.delete(TimeZoneService.toDefaultTime(reminder));
	}

	public void deleteOutdatedReminders() {
		remindersDAL.deleteOutdatedReminders(getNowDefaultTimeReminder());
	}

	private Reminder getNowDefaultTimeReminder() {
		LocalDateTime now = TimeZoneService.getDefaultTime();
		Reminder reminder = new Reminder();
		reminder.setYear(now.getYear());
		reminder.setMonth(now.getMonthValue());
		reminder.setDay(now.getDayOfMonth());
		reminder.setHour(now.getHour());
		reminder.setMinute(now.getMinute());
		return reminder;
	}
}
