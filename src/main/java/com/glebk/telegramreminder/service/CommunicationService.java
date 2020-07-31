package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.service.exception.ServiceException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by glebk on 7/17/20
 */
@Component
public class CommunicationService {

	private final ReplyMessageService replyMessageService;
	private final ChatDataCacheService chatDataCacheService;
	private final UserInputValidationService userInputValidationService;
	private final UserInputService userInputService;
	private final BotStateService botStateService;

	public CommunicationService(ReplyMessageService replyMessageService,
								ChatDataCacheService chatDataCacheService,
								UserInputValidationService userInputValidationService,
								UserInputService userInputService,
								BotStateService botStateService) {
		this.replyMessageService = replyMessageService;
		this.chatDataCacheService = chatDataCacheService;
		this.userInputValidationService = userInputValidationService;
		this.userInputService = userInputService;
		this.botStateService = botStateService;
	}

	public SendMessage handleUpdate(Update update) {
		if (update.hasCallbackQuery()) {
			return handleCallBackQueryUpdate(update.getCallbackQuery());
		}
		if (update.getMessage() != null && update.getMessage().hasText()) {
			return handleInputMessage(update.getMessage());
		}
		return null;
	}

	private SendMessage handleCallBackQueryUpdate(CallbackQuery callbackQuery) {
		return handleInput(callbackQuery.getMessage().getChatId(), callbackQuery.getData());
	}

	private SendMessage handleInputMessage(Message message) {
		return handleInput(message.getChatId(), message.getText());
	}

	private SendMessage handleInput(long chatId, String text) {
		chatDataCacheService.initChatDataCache(chatId);
		try {
			userInputValidationService.validate(chatId, text);
		} catch (ServiceException e) {
			return replyMessageService.getReplyMessage(chatId, e);
		}
		userInputService.handleInput(chatId, text);
		botStateService.updateBotState(chatId, text);
		return replyMessageService.getReplyMessage(chatId);
	}
}
