package com.glebk.telegramreminder.repository.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by glebk on 7/28/20
 */
@Document(collection = "reminders")
@Getter
@Setter
public class Reminder {

	@NotBlank
	private Long chatId;
	@NotBlank
	private Integer year;
	@NotBlank
	private Integer month;
	@NotBlank
	private Integer day;
	@NotBlank
	private Integer hour;
	@NotBlank
	private Integer minute;
	@Size(max = 255)
	private String text;
	@NotBlank
	@Size(max = 100)
	private String timeZoneId;
	@NotBlank
	@Size(max = 10)
	private String locale;

}
