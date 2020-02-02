package com.sk.download.exception;

/**
 * 
 * @author sunil
 *
 */
public class DownloadException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1870486180864565774L;
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
