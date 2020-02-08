package com.sk.download.service.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DownloadHTTPFileServiceImplTests {

	@Autowired
	private DownloadHTTPFileServiceImpl httpService;
	String inputUrl = "https://www.google.com/doodles";

	@Test
	public void testResponseFromUrl() throws IOException {

		httpService = new DownloadHTTPFileServiceImpl();
		int responseCode = httpService.getRepornseFromURL(inputUrl.trim());
		assertEquals(200, responseCode);

	}

	@Test(expected = IOException.class)
	public void testResponseFromUrlWhenUrlUnknown() throws IOException {
		String inputUrlUn = "https://www.testone.com/";
		httpService = new DownloadHTTPFileServiceImpl();
		httpService.getRepornseFromURL(inputUrlUn.trim());
	}

	@Test
	public void testgetFileName() throws IOException {
		testResponseFromUrl();
		String fileName = "";
		fileName = httpService.getFileName(inputUrl, fileName);
		assertEquals("doodles", fileName);

	}
}
