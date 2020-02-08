package com.sk.download.service;

import java.io.IOException;

import com.sk.download.exception.DownloadException;

/**
 * 
 * @author sunil
 *
 */
public interface DownloadService {

	public void download(String inputUrl, String outputLocation) throws IOException, DownloadException;
}
