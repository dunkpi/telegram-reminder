package com.glebk.telegramreminder.service.exception;

/**
 * Created by glebk on 7/25/20
 */
public class IncorrectInputException extends ServiceException {
	public IncorrectInputException(String message, String prefix, String info) {
		super(message, prefix, info);
	}
}
