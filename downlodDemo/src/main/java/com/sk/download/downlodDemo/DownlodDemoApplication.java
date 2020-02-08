package com.sk.download.downlodDemo;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sk.download.exception.DownloadException;
import com.sk.download.service.DownloadService;
import com.sk.download.service.impl.DownloadFTPFileServiceImpl;
import com.sk.download.service.impl.DownloadHTTPFileServiceImpl;
import com.sk.download.service.impl.DownloadSFTPFileImpl;
import com.sk.download.util.FilesUtil;

/**
 * 
 * @author sunil
 *
 */
@SpringBootApplication
public class DownlodDemoApplication implements CommandLineRunner {

	@Value("${input.urls}")
	private String inputurl;

	@Value("${output.location}")
	private String outputLocation;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(DownlodDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		downloadFromInputList(inputurl);

	}

	public void downloadFromInputList(String inputurl) {
		String[] inputUrlArr = inputurl.split(",");
		ExecutorService service = Executors.newCachedThreadPool();
		if (outputLocation != null && FilesUtil.isValidPath(outputLocation)
				&& FilesUtil.locationExists(outputLocation)) {
			DownloadService downloadService = null;
			for (int i = 0; i < inputUrlArr.length; i++) {
				String inputUrl = inputUrlArr[i];
				if (inputUrl.startsWith("http") || inputUrl.startsWith("https")) {
					downloadService = new DownloadHTTPFileServiceImpl();
					downloadService(inputurl, service, inputUrl, downloadService);

				} else if (inputUrl.startsWith("ftp")) {
					downloadService = new DownloadFTPFileServiceImpl();
					downloadService(inputurl, service, inputUrl, downloadService);
				} else if (inputUrl.startsWith("sftp")) {
					downloadService = new DownloadSFTPFileImpl();
					downloadService(inputurl, service, inputUrl, downloadService);
				}
			}
		} else {
			LOGGER.error("Invalid output location", outputLocation);
		}
		service.shutdown();
	}

	private void downloadService(String inputurl, ExecutorService service, String inputUrl,
			DownloadService downloadService) {
		service.submit(() -> {
			try {
				downloadService.download(inputUrl, outputLocation);
			} catch (DownloadException | IOException e) {
				LOGGER.error("Error in downloading from URL " + inputurl);
				e.printStackTrace();
			}
		});
	}

}
