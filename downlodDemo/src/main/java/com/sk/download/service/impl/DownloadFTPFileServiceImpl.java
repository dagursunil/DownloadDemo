package com.sk.download.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

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
	public void download(String inputUrl, String outputLocation) {
		String[] urlArr = inputUrl.split("//");
		if (urlArr.length > 1) {
			parseUrl(urlArr[1]);
			downloadFromFTPServer(outputLocation);
		}

	}

	private void downloadFromFTPServer(String outputLocation) {
		File downloadFile = null;
		try {

			boolean isConnected = connectFTPServer();
			if (isConnected) {
				String remoteFile = this.path;
				String targetFile = this.path.substring(path.lastIndexOf("/"), path.length());
				downloadFile = new File(outputLocation + "/" + targetFile);
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

		} catch(DownloadException de) {
			LOGGER.error(de.getMessage());
		}
		catch (Exception e) {
			LOGGER.error("File download failed from server ",this.server);
			FilesUtil.deleteFile(downloadFile);
			e.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
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

	public void parseUrl(String url) {
		url = url.trim();
		String params[] = url.split("@");
		if (params[0].compareTo("") != 0) {
			String paramsfst[] = params[0].split(":");
			if (params.length == 1) {
				if (paramsfst.length >= 1) {
					this.server = parsePort(paramsfst[0]);
					if (paramsfst.length == 2) {
						this.port = parsePort(paramsfst[1]);
					}
				}
			} else {
				String paramssec[] = params[1].split(":");
				if (paramssec.length >= 1) {
					this.server = parsePort(paramssec[0]);
					if (paramssec.length == 2) {
						this.port = parsePort(paramssec[1]);
					}
				}
				if (paramsfst.length == 2) {
					this.username = paramsfst[0];
					this.password = paramsfst[1];
				}
			}
			if (url.contains("/")) {
				this.path = url.substring(url.indexOf("/"), url.length());
			}

		}

	}

	private String parsePort(String port) {
		int i = port.indexOf("/");
		if (i == -1) {
			return port;
		}
		return port.substring(0, i);
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
