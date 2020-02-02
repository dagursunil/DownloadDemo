package com.sk.download.exception;

/**
 * 
 * @author sunil
 *
 */
public class DownloadException extends Exception {

	private String message;

	public DownloadException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
