package com.glebk.telegramreminder.service.exception;

import lombok.Getter;

/**
 * Created by glebk on 7/24/20
 */
@Getter
public class ServiceException extends Exception {

	private final String prefix;
	private final String info;

	public ServiceException(String message, String prefix, String info) {
		super(message);
		this.prefix = prefix;
		this.info = info;
	}
}
