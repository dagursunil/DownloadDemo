package com.sk.download.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
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
public class DownloadFTPFileServiceImpl implements DownloadService {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private String server;
	private String port;
	private String username;
	private String password;
	private String path;

	FTPClient ftpClient;

	@Override
	public void download(String inputUrl, String outputLocation) throws IOException, DownloadException {
		String[] urlArr = inputUrl.split("//");
		if (urlArr.length > 1) {
			Map<String, String> attributesMap = FilesUtil.parseUrl(urlArr[1]);
			if (attributesMap.size() > 0) {
				setDownloadAttributes(attributesMap);
				downloadFromFTPServer(outputLocation);
			} else {
				throw new DownloadException("Input URL incorrect .Please verify the same.");
			}
		} else {
			throw new DownloadException("Input URL incorrect .Please verify the same.");
		}
	}

	public void setDownloadAttributes(Map<String, String> attributesMap) {
		this.server = attributesMap.get(FilesUtil.SERVER);
		this.port = attributesMap.get(FilesUtil.PORT);
		this.path = attributesMap.get(FilesUtil.PATH);
		this.username = attributesMap.get(FilesUtil.USERNAME);
		this.password = attributesMap.get(FilesUtil.PASSWORD);
	}

	private void downloadFromFTPServer(String outputLocation) throws DownloadException, IOException {
		File downloadFile = null;
		try {

			boolean isConnected = connectFTPServer();
			if (isConnected) {
				String remoteFile = this.path;
				String targetFile = this.path.substring(path.lastIndexOf("/"), path.length());
				if (!outputLocation.endsWith("/")) {
					downloadFile = new File(outputLocation + "/" + targetFile);
				} else {
					downloadFile = new File(outputLocation + targetFile);
				}

				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
				InputStream inputStream = ftpClient.retrieveFileStream(remoteFile);
				byte[] bytesArray = new byte[4096];
				int bytesRead = -1;
				while ((bytesRead = inputStream.read(bytesArray)) != -1) {
					outputStream.write(bytesArray, 0, bytesRead);
				}
				boolean success = ftpClient.completePendingCommand();
				if (success) {
					LOGGER.info("Download from FTP Server successful.");
				}
				outputStream.close();
				inputStream.close();
			} else {
				throw new DownloadException("Cound not connect to the server " + server);
			}

		} catch (Exception e) {
			LOGGER.error("File download failed from server ", this.server);
			FilesUtil.deleteFile(downloadFile);
			throw new DownloadException(e.getMessage());
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				throw new IOException("FTP Connection failed from the server" + this.server);
			}
		}

	}

	public boolean connectFTPServer() throws SocketException, IOException {
		ftpClient = new FTPClient();
		if (this.port != null && !this.port.isEmpty()) {
			this.port = this.port.trim();
			ftpClient.connect(server.trim(), Integer.parseInt(this.port));
		} else {
			ftpClient.connect(server.trim());
		}

		if (ftpClient.isConnected()) {
			LOGGER.info("Connected to server");
		}
		ftpClient.login(this.username, this.password);
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		return ftpClient.isConnected();
	}

	public String getServer() {
		return server;
	}

	public String getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPath() {
		return path;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}
}
