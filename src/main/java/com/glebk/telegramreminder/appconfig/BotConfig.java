package com.glebk.telegramreminder.appconfig;

import com.glebk.telegramreminder.ReminderTelegramBot;
import com.glebk.telegramreminder.service.CommunicationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Created by glebk on 7/17/20
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot")
public class BotConfig {

	private String botUsername;
	private String botToken;
	private String botPath;

	@Bean
	public ReminderTelegramBot reminderTelegramBot(CommunicationService communicationService) {
		return new ReminderTelegramBot(botUsername, botToken, botPath, communicationService);
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
