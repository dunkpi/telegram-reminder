package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.ReminderTelegramBot;
import com.glebk.telegramreminder.service.model.BotState;
import com.glebk.telegramreminder.service.model.DataCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by glebk on 7/22/20
 */
@Service
public class SchedulerService {

	private static final int SESSION_TIME_MINUTES = 5;

	private final DataCache dataCache;
	private final ReplyMessageService replyMessageService;
	private final ReminderTelegramBot telegramBot;
	private final RemindersService remindersService;

	public SchedulerService(DataCache dataCache,
							ReplyMessageService replyMessageService,
							ReminderTelegramBot telegramBot,
							RemindersService remindersService) {
		this.dataCache = dataCache;
		this.replyMessageService = replyMessageService;
		this.telegramBot = telegramBot;
		this.remindersService = remindersService;
	}

	@Scheduled(fixedRate = 60000)
	public void sendNotifications() {
		remindersService.getCurrentTimeReminders().forEach(n ->
				telegramBot.sendMessage(replyMessageService.getReplyMessage(BotState.NOTIFICATION, n, null)));
	}

	@Scheduled(fixedRate = SESSION_TIME_MINUTES * 60000)
	public void dropOutdatedSessions() {
		LocalDateTime time = LocalDateTime.now().minusMinutes(SESSION_TIME_MINUTES);
		List<Long> chatIds = dataCache.getChatLastActivity().entrySet().stream()
				.filter(e -> time.isAfter(e.getValue()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		dataCache.getChatBotState().entrySet().removeIf(e -> chatIds.contains(e.getKey()));
		dataCache.getChatNewReminderData().entrySet().removeIf(e -> chatIds.contains(e.getKey()));
		dataCache.getChatReminders().entrySet().removeIf(e -> chatIds.contains(e.getKey()));
		dataCache.getChatLastActivity().entrySet().removeIf(e -> chatIds.contains(e.getKey()));
	}

	@Scheduled(fixedRate = 86400000)
	public void deleteOutdatedReminders() {
		remindersService.deleteOutdatedReminders();
	}
}
