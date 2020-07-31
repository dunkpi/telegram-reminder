package com.glebk.telegramreminder;

import com.glebk.telegramreminder.service.CommunicationService;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Created by glebk on 7/17/20
 */
public class ReminderTelegramBot extends TelegramWebhookBot {

	private final String botUsername;
	private final String botToken;
	private final String botPath;
	private final CommunicationService communicationService;

	public ReminderTelegramBot(String botUsername, String botToken, String botPath, CommunicationService communicationService) {
		this.botUsername = botUsername;
		this.botToken = botToken;
		this.botPath = botPath;
		this.communicationService = communicationService;
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	public String getBotPath() {
		return botPath;
	}

	@Override
	public SendMessage onWebhookUpdateReceived(Update update) {
		return communicationService.handleUpdate(update);
	}

	public void sendMessage(SendMessage sendMessage) {
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
