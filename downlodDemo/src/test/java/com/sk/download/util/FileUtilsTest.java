package com.sk.download.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

public class FileUtilsTest {
	
	

	@Test
	public void testParseUrlFTP() {
		String url="ftp://demo-user:demo-user@demo.wftpserver.com/download/manual_en.pdf";
		String [] urlArr=url.split("//");
		Map<String,String> valueMap=FilesUtil.parseUrl(urlArr[1]);
		assertEquals("demo.wftpserver.com",valueMap.get(FilesUtil.SERVER));
		assertEquals("/download/manual_en.pdf",valueMap.get(FilesUtil.PATH));
	}
	
	@Test
	public void testParseUrlSFTP() {
		String url="sftp://demo:password@test.rebex.net:22/pub/example/readme.txt";
		String [] urlArr=url.split("//");
		Map<String,String> valueMap=FilesUtil.parseUrl(urlArr[1]);
		assertEquals("test.rebex.net",valueMap.get(FilesUtil.SERVER));
		assertEquals("/pub/example/readme.txt",valueMap.get(FilesUtil.PATH));
		assertEquals("22",valueMap.get(FilesUtil.PORT));
	}
	
	@Test
	public void testParseUrlSFTPIncorrectUrl()  {
		String url="sftp://demo:password@test.rebex.net&&22/pub/example/readme.txt";
		Map<String,String> valueMap=FilesUtil.parseUrl(url);
		assertNull(valueMap.get(FilesUtil.USERNAME));
		assertNull(valueMap.get(FilesUtil.PASSWORD));
	}
	
	

}
