package com.sk.download.downloadDemo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.sk.download.service.impl.DownloadFTPFileServiceImplTests;
import com.sk.download.service.impl.DownloadHTTPFileServiceImplTests;
import com.sk.download.service.impl.DownloadSFTPFileImplTest;
import com.sk.download.util.FileUtilsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    DownloadFTPFileServiceImplTests.class, 
    DownloadSFTPFileImplTest.class,     
    DownloadHTTPFileServiceImplTests.class,
    FileUtilsTest.class
})
public class DownloadDemoTestSuite {

}
