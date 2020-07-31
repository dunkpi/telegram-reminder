package com.glebk.telegramreminder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by glebk on 7/17/20
 */
@Service
public class LocaleMessageService {

	private final Locale defaultLocale;
	private final MessageSource messageSource;

	public LocaleMessageService(@Value("${localeTag}") Locale defaultLocale, MessageSource messageSource) {
		this.defaultLocale = defaultLocale;
		this.messageSource = messageSource;
	}

	public String getMessage(String message, String locale) {
		return messageSource.getMessage(message,
				null,
				locale == null ? defaultLocale : new Locale.Builder().setLanguageTag(locale).build());
	}
}
