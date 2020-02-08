package com.sk.download.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sk.download.exception.DownloadException;
import com.sk.download.service.DownloadService;
import com.sk.download.util.FilesUtil;

/**
 * 
 * @author sunil
 *
 */
public class DownloadHTTPFileServiceImpl implements DownloadService {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private URL url;
	HttpURLConnection httpConn;

	@Override
	public void download(String inputUrl, String outputLocation) throws IOException, DownloadException {

		int responseCode = getRepornseFromURL(inputUrl);
		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = "";
			fileName = getFileName(inputUrl, fileName);
			String saveFilePath = "";
			if (outputLocation.endsWith("/")) {
				saveFilePath = outputLocation + fileName;
			} else {
				saveFilePath = outputLocation + File.separator + fileName;
			}

			try (BufferedInputStream in = new BufferedInputStream(url.openStream());
					FileOutputStream fileOutputStream = new FileOutputStream(saveFilePath)) {
				byte dataBuffer[] = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
					fileOutputStream.write(dataBuffer, 0, bytesRead);
				}
				LOGGER.info("Download file from HTTP server successful.");
			} catch (IOException e) {
				LOGGER.error("Error while downloading from HTTP server", inputUrl);
				File newFile = new File(saveFilePath);
				FilesUtil.deleteFile(newFile);
				throw new DownloadException("Error while downloading from HTTP server" + inputUrl);
			}

		} else {
			throw new DownloadException("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
	}

	public String getFileName(String inputUrl, String fileName) {
		String disposition = httpConn.getHeaderField("Content-Disposition");
		String contentType = httpConn.getContentType();
		int contentLength = httpConn.getContentLength();
		if (disposition != null) {

			// extracts file name from header field
			int index = disposition.indexOf("filename=");
			if (index > 0) {
				fileName = disposition.substring(index + 10, disposition.length() - 1);
			}
		} else {

			// extracts file name from URL
			fileName = inputUrl.substring(inputUrl.lastIndexOf("/") + 1, inputUrl.length());
		}
		LOGGER.info("Content-Type = " + contentType);
		LOGGER.info("Content-Disposition = " + disposition);
		LOGGER.info("Content-Length = " + contentLength);
		LOGGER.info("fileName = " + fileName);
		return fileName;
	}

	public int getRepornseFromURL(String inputUrl) throws IOException {
		int responseCode = 0;
		try {
			url = new URL(inputUrl);
			httpConn = (HttpURLConnection) url.openConnection();
			responseCode = httpConn.getResponseCode();
		} catch (IOException e) {
			throw new IOException("Excpetion in getting reponse from URL " + inputUrl);
		}
		return responseCode;
	}

}
