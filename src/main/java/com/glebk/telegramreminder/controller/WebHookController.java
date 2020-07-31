package com.glebk.telegramreminder.controller;

import com.glebk.telegramreminder.ReminderTelegramBot;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by glebk on 7/17/20
 */
@RestController
public class WebHookController {
	private final ReminderTelegramBot telegramBot;

	public WebHookController(ReminderTelegramBot telegramBot) {
		this.telegramBot = telegramBot;
	}

	@PostMapping("/telegram-reminder")
	public SendMessage onUpdateReceived(@RequestBody Update update) {
		return telegramBot.onWebhookUpdateReceived(update);
	}

	@GetMapping("/test")
	public String test() {
		return "Test controller accessed";
	}
	
}
