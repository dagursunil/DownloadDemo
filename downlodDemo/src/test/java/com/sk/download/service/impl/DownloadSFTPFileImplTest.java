package com.sk.download.service.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.sk.download.exception.DownloadException;
import com.sk.download.util.FilesUtil;

public class DownloadSFTPFileImplTest {

	DownloadSFTPFileImpl service;
	
   private String url="sftp://demo:password@test.rebex.net:22/pub/example/readme.txt";
	
   private Map<String,String> attributesMap=new HashMap<>();
   private String outputLocation="E:/output";
	
   @Before
	public void setup() {
	   service=new DownloadSFTPFileImpl();
	   String [] urlArr=url.split("//");
	   attributesMap=FilesUtil.parseUrl(urlArr[1]);
	}
   
	@Test
	public void testConnectSFTPServer() throws Exception {
		service.setDownloadAttributes(attributesMap);
		boolean isConnected=service.connectSFTPServer();
		assertEquals(true,isConnected);
	}
	
	@Test
	public void testConnectFailureSFTPServer() throws Exception {
		attributesMap.put(FilesUtil.USERNAME, null);
		service.setDownloadAttributes(attributesMap);
		boolean isConnected=service.connectSFTPServer();
		assertEquals(false,isConnected);
	}
	
	@Test
	public void testdownloadFromFTPServer() throws IOException, DownloadException {
		
		service.setDownloadAttributes(attributesMap);
		boolean isDownloaded=service.downloadFromSFTPServer(outputLocation);
		assertEquals(true, isDownloaded);
	}
	
	@Test(expected = DownloadException.class)
	public void testdownloadFailedFromFTPServer() throws IOException, DownloadException {
		
		attributesMap.put(FilesUtil.SERVER,"test.sftp.com");
		service.setDownloadAttributes(attributesMap);
		
		boolean isDownloaded=service.downloadFromSFTPServer(outputLocation);
		assertEquals(false, isDownloaded);
	}
}
