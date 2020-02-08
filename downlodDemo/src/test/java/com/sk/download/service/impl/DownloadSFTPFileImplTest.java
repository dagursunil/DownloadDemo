package com.sk.download.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import com.sk.download.util.FilesUtil;

public class DownloadSFTPFileImplTest {

	DownloadSFTPFileImpl service;
	
   private String url="sftp://demo:password@test.rebex.net:22/pub/example/readme.txt";
	
   private Map<String,String> attributesMap=new HashMap<>();
	
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
}
