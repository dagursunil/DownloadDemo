package com.sk.download.util;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author sunil
 *
 */
public class FilesUtil {

	public static String SERVER = "server";
	public static String PORT = "port";
	public static String USERNAME = "username";
	public static String PASSWORD = "password";
	public static String PATH = "path";

	public static boolean isValidPath(String path) {
		try {
			Paths.get(path);
		} catch (InvalidPathException | NullPointerException ex) {
			return false;
		}
		return true;
	}

	public static boolean locationExists(String path) {
		File f = new File(path);
		if (f.exists()) {
			return true;
		}
		return false;
	}

	public static void deleteFile(File downloadedFile) {
		if (downloadedFile != null && downloadedFile.exists())
			downloadedFile.delete();
	}

	public static Map<String, String> parseUrl(String url) {
		String server = null;
		String port = null;
		String username = null;
		String password = null;
		String path = null;

		Map<String, String> urlAttributeMap = new HashMap<>();
		url = url.trim();
		String params[] = url.split("@");
		if (params[0].compareTo("") != 0) {
			String paramsfst[] = params[0].split(":");
			if (params.length == 1) {
				if (paramsfst.length >= 1) {
					server = parsePort(paramsfst[0]);
					if (paramsfst.length == 2) {
						port = parsePort(paramsfst[1]);
					}
				}
			} else {
				String paramssec[] = params[1].split(":");
				if (paramssec.length >= 1) {
					server = parsePort(paramssec[0]);
					if (paramssec.length == 2) {
						port = parsePort(paramssec[1]);
					}
				}
				if (paramsfst.length == 2) {
					username = paramsfst[0];
					password = paramsfst[1];
				}
			}
			if (url.contains("/")) {
				path = url.substring(url.indexOf("/"), url.length());
			}

		}
		urlAttributeMap.put(PORT, port);
		urlAttributeMap.put(PATH, path);
		urlAttributeMap.put(USERNAME, username);
		urlAttributeMap.put(PASSWORD, password);
		urlAttributeMap.put(SERVER, server);

		return urlAttributeMap;
	}

	private static String parsePort(String port) {
		int i = port.indexOf("/");
		if (i == -1) {
			return port;
		}
		return port.substring(0, i);
	}

}
