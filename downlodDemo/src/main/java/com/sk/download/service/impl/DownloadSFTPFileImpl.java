package com.sk.download.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sk.download.exception.DownloadException;
import com.sk.download.service.DownloadService;
import com.sk.download.util.FilesUtil;

/**
 * 
 * @author sunil
 *
 */
public class DownloadSFTPFileImpl implements DownloadService {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private String server;
	private String port;
	private String username;
	private String password;
	private String path;

	Session session = null;
	Channel channel = null;
	ChannelSftp channelSftp = null;

	@Override
	public void download(String inputUrl, String outputLocation) throws IOException, DownloadException {
		String[] urlArr = inputUrl.split("//");
		if (urlArr.length > 1) {
			Map<String, String> attributesMap = FilesUtil.parseUrl(urlArr[1]);
			if (attributesMap.size() > 0) {
				setDownloadAttributes(attributesMap);
				downloadFromSFTPServer(outputLocation);
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

	private void downloadFromSFTPServer(String outputLocation) throws IOException, DownloadException {
		File newFile = null;
		try {
			boolean isConnected = connectSFTPServer();
			if (isConnected) {
				channel = session.openChannel("sftp");
				channel.connect();
				channelSftp = (ChannelSftp) channel;
				String pathValue = this.path.substring(0, this.path.lastIndexOf("/"));
				String file = this.path.substring(path.lastIndexOf("/") + 1, path.length());
				if (!pathValue.isEmpty()) {
					channelSftp.cd(pathValue);
				}
				byte[] buffer = new byte[1024];
				BufferedInputStream bis = new BufferedInputStream(channelSftp.get(file));
				if (outputLocation.endsWith("/")) {
					newFile = new File(outputLocation + file);
				} else {
					newFile = new File(outputLocation + "/" + file);
				}

				OutputStream os = new FileOutputStream(newFile);
				BufferedOutputStream bos = new BufferedOutputStream(os);
				int readCount;
				while ((readCount = bis.read(buffer)) > 0) {
					LOGGER.info("Writing to file: ");
					bos.write(buffer, 0, readCount);
				}
				LOGGER.info("Download successful from sftp server ");
				bis.close();
				bos.close();
			} else {
				throw new DownloadException("Could not connect to Server " + this.server);
			}
		} catch (Exception ex) {
			FilesUtil.deleteFile(newFile);
			LOGGER.error("File download failed from server ", this.server);
			throw new DownloadException(ex.getMessage());
		}

		if (session.isConnected()) {
			session.disconnect();
		}

	}

	public boolean connectSFTPServer() throws Exception {
		JSch jsch = new JSch();
		if (this.port != null && !this.port.isEmpty()) {
			this.port = this.port.trim();
			session = jsch.getSession(this.username, this.server, Integer.parseInt(this.port));
		} else {
			session = jsch.getSession(this.username, this.server);
		}
		session.setPassword(this.password);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		return session.isConnected();
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

}
