package com.sk.download.util;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class FilesUtil {

	public static boolean isValidPath(String path) {
	    try {
	        Paths.get(path);
	    } catch (InvalidPathException | NullPointerException ex) {
	        return false;
	    }
	    return true;
	}
	
	public static boolean locationExists(String path) {
	    File f = new File(path);
	    if(f.exists()) {
	       return true;
	       }
	       return false;
	  }
	
	public static void deleteFile(File downloadedFile) {
		if (downloadedFile != null && downloadedFile.exists())
			downloadedFile.delete();
	}
}
