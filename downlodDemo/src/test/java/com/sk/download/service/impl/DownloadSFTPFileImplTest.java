package com.sk.download.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.jcraft.jsch.JSchException;

public class DownloadSFTPFileImplTest {

	DownloadSFTPFileImpl service;
	
   private String url="sftp://demo:password@test.rebex.net:22/pub/example/readme.txt";
	
//	@Test
//	public void testParseUrl() {
//		service=new DownloadSFTPFileImpl();
//		String [] urlArr=url.split("//");
//		service.parseUrl(urlArr[1]);
//		assertEquals("test.rebex.net",service.getServer());
//		assertEquals("/pub/example/readme.txt",service.getPath());
//	}
//	
//	@Test
//	public void testConnectSFTPServer() throws Exception {
//		testParseUrl();
//		boolean isConnected=service.connectSFTPServer();
//		assertEquals(true,isConnected);
//	}
}
