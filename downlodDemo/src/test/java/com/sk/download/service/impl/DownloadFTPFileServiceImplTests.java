package com.sk.download.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sk.download.util.FilesUtil;

public class DownloadFTPFileServiceImplTests {

	@Autowired
	DownloadFTPFileServiceImpl service;
	String url="ftp://demo-user:demo-user@demo.wftpserver.com/download/manual_en.pdf";
	private Map<String,String> attributesMap=new HashMap<>();

	@Before
	public void setup() {
		service=new DownloadFTPFileServiceImpl();
	   String [] urlArr=url.split("//");
	   attributesMap=FilesUtil.parseUrl(urlArr[1]);
	}
//	
	@Test
	public void testConnectFTPServer() throws SocketException, IOException {
		service.setDownloadAttributes(attributesMap);
		boolean isConnected=service.connectFTPServer();
		assertEquals(true, isConnected);
	}
	
	@Test
	public void testConnectFTPServerFailed() throws SocketException, IOException {
		attributesMap.put(FilesUtil.PORT, "228");
		service.setDownloadAttributes(attributesMap);
		boolean isConnected=service.connectFTPServer();
		assertEquals(false, isConnected);
	}
}
