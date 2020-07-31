package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.repository.model.Reminder;
import com.glebk.telegramreminder.service.model.DataCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created by glebk on 7/29/20
 */
@Service
public class ChatDataCacheService {

	private final DataCache dataCache;

	public ChatDataCacheService(DataCache dataCache) {
		this.dataCache = dataCache;
	}

	public void initChatDataCache(long chatId) {
		Reminder reminder = dataCache.getChatNewReminderData(chatId);
		if (reminder.getChatId() == null) {
			reminder.setChatId(chatId);
			dataCache.setChatNewReminderData(chatId, reminder);
		}
		dataCache.setChatLastActivity(chatId, LocalDateTime.now());
	}
}
