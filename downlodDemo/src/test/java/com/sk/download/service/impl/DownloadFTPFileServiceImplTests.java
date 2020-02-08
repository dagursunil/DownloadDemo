package com.sk.download.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.SocketException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DownloadFTPFileServiceImplTests {

	@Autowired
	DownloadFTPFileServiceImpl service;
	String url="ftp://demo-user:demo-user@demo.wftpserver.com/download/manual_en.pdf";
	
//	@Test
//	public void testParseUrl() {
//		service=new DownloadFTPFileServiceImpl();
//		String [] urlArr=url.split("//");
//		service.parseUrl(urlArr[1]);
//		assertEquals("demo.wftpserver.com",service.getServer());
//		assertEquals("/download/manual_en.pdf",service.getPath());
//	}
//	
//	@Test
//	public void testConnectFTPServer() throws SocketException, IOException {
//		testParseUrl();
//		boolean isConnected=service.connectFTPServer();
//		assertEquals(true, isConnected);
//	}
}
