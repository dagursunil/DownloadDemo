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
			for (int i = 0; i < inputUrlArr.length; i++) {
				String inputUrl = inputUrlArr[i];
				if (inputUrl.startsWith("http") || inputUrl.startsWith("https")) {
					httpDownloadService(inputurl, service, inputUrl);

				} else if (inputUrl.startsWith("ftp")) {
					ftpDpwnloadService(inputurl, service, inputUrl);
				} else if (inputUrl.startsWith("sftp")) {
					sftpDownloadService(inputurl, service, inputUrl);
				}
			}
		} else {
			LOGGER.error("Invalid output location", outputLocation);
		}
		service.shutdown();
	}

	private void sftpDownloadService(String inputurl, ExecutorService service, String inputUrl) {
		DownloadService downloadService = new DownloadSFTPFileImpl();
		service.submit(() -> {
			try {
				downloadService.download(inputUrl, outputLocation);
			} catch (IOException e) {
				LOGGER.error("Error is downloading from SFTP URL", inputurl);
				e.printStackTrace();
			}
		});
	}

	private void ftpDpwnloadService(String inputurl, ExecutorService service, String inputUrl) {
		DownloadService downloadService = new DownloadFTPFileServiceImpl();
		service.submit(() -> {
			try {
				downloadService.download(inputUrl, outputLocation);
			} catch (IOException e) {
				LOGGER.error("Error is downloading from FTP URL", inputurl);
				e.printStackTrace();
			}
		});
	}

	private void httpDownloadService(String inputurl, ExecutorService service, String inputUrl) {
		DownloadService downloadService = new DownloadHTTPFileServiceImpl();
		service.submit(() -> {
			try {
				downloadService.download(inputUrl, outputLocation);
			} catch (IOException e) {
				LOGGER.error("Error is downloading from HTTP URL", inputurl);
			}
		});
	}
}
