package com.sk.download.service;

import java.io.IOException;

public interface DownloadService {

	public void download(String inputUrl,String outputLocation) throws IOException;
}
