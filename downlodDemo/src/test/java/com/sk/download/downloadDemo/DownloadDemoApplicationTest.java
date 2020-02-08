package com.sk.download.downloadDemo;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.sk.download.downlodDemo.DownloadDemoApplication;

@SpringBootTest
@SpringBootConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {
		"input.urls=https://google.com/doodles,ftp://demo-user:demo-user@demo.wftpserver.com/download/manual_en.pdf,sftp://demo:password@test.rebex.net:22/pub/example/readme.txt" })
public class DownloadDemoApplicationTest {

	@Value("${input.urls}")
	private String inputurl;

	DownloadDemoApplication downloadDemoApplication;

	long timeoutExpiredMs = System.currentTimeMillis() + 15000;

	@Before
	public void setup() {

		downloadDemoApplication = new DownloadDemoApplication();
		org.springframework.test.util.ReflectionTestUtils.setField(downloadDemoApplication, "outputLocation",
				"E:/output");
	}

	@Test
	public void main() {
		DownloadDemoApplication.main(new String[] {});
	}

	@Test
	public void testDownloadFromInputList() throws Exception {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		int threadCountBefore = bean.getThreadCount();
		downloadDemoApplication.downloadFromInputList(inputurl);
		int threadCountAfter = bean.getThreadCount();
		// As three new threads have been launched ,so checking for there execution
		// completion
		while (threadCountAfter != threadCountBefore) {
			long waitMillis = timeoutExpiredMs - System.currentTimeMillis();
			if (waitMillis <= 0) {
				System.out.println("Time out reached");
				break;
			}
			Thread.sleep(1000);
			threadCountAfter = bean.getThreadCount();
		}

	}
}
