package com.sk.download.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.sk.download.exception.DownloadException;
import com.sk.download.util.FilesUtil;

public class DownloadFTPFileServiceImplTests {
	
	@InjectMocks
	FTPClient ftpClient;

	@Autowired
	DownloadFTPFileServiceImpl service;
	String url="ftp://demo-user:demo-user@demo.wftpserver.com/download/manual_en.pdf";
	private Map<String,String> attributesMap=new HashMap<>();
	private String outputLocation="E:/output";

	@Before
	public void setup() {
		service=new DownloadFTPFileServiceImpl();
	   String [] urlArr=url.split("//");
	   attributesMap=FilesUtil.parseUrl(urlArr[1]);
	   ftpClient = Mockito.mock(FTPClient.class);
	   
	}
	
	@Test
	public void testConnectFTPServer() throws SocketException, IOException {
		
	    Mockito.when(ftpClient.getReplyCode()).thenReturn(200);
		Mockito.when(ftpClient.login(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(ftpClient.isConnected()).thenReturn(true);
		Mockito.doNothing().when(ftpClient).connect(Mockito.anyString());
		
		//attributesMap.put(FilesUtil.SERVER, "abcde.com");
		service.setDownloadAttributes(attributesMap);
		service.setFtpClient(ftpClient);
    	boolean isConnected=service.connectFTPServer();
		assertEquals(true, isConnected);
	}
	
	@Test
	public void testConnectFTPServerFailed() throws SocketException, IOException {
		attributesMap.put(FilesUtil.PORT, "228");
		service.setDownloadAttributes(attributesMap);
		service.setFtpClient(ftpClient);
		Mockito.when(ftpClient.getReplyCode()).thenReturn(200);
		Mockito.when(ftpClient.login(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(ftpClient.isConnected()).thenReturn(false);
		Mockito.doNothing().when(ftpClient).connect(Mockito.anyString(),Mockito.anyInt());
		boolean isConnected=service.connectFTPServer();
		assertEquals(false, isConnected);
	}
	
	@Test
	public void testdownloadFromFTPServer() throws IOException, DownloadException {
		Mockito.when(ftpClient.getReplyCode()).thenReturn(200);
		Mockito.when(ftpClient.login(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(ftpClient.isConnected()).thenReturn(true);
		Mockito.doNothing().when(ftpClient).connect(Mockito.anyString());
		Mockito.doNothing().when(ftpClient).connect(Mockito.anyString(),Mockito.anyInt());
		String initialString = "ftp Text file";
	    InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
		Mockito.when(ftpClient.retrieveFileStream(Mockito.anyString())).thenReturn(targetStream);
		Mockito.when(ftpClient.completePendingCommand()).thenReturn(true);
		service.setDownloadAttributes(attributesMap);
		service.setFtpClient(ftpClient);
		boolean isDownloaded=service.downloadFromFTPServer(outputLocation);
		assertEquals(true, isDownloaded);
	}
	
	@Test(expected = DownloadException.class)
	public void testdownloadFailedFromFTPServer() throws IOException, DownloadException {
		Mockito.when(ftpClient.getReplyCode()).thenReturn(200);
		Mockito.when(ftpClient.login(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(ftpClient.isConnected()).thenReturn(true);
		Mockito.doNothing().when(ftpClient).connect(Mockito.anyString());
		Mockito.doNothing().when(ftpClient).connect(Mockito.anyString(),Mockito.anyInt());
		attributesMap.put(FilesUtil.SERVER,"test.ftp.com");
		service.setDownloadAttributes(attributesMap);
		service.setFtpClient(ftpClient);
		boolean isDownloaded=service.downloadFromFTPServer(outputLocation);
		assertEquals(true, isDownloaded);
	}
}
