package com.jsantos.common.util;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDownload {

	
	public static void downloadUrl(String url, String fileName) throws Exception {
	    Files.deleteIfExists(Paths.get(fileName));
		try (InputStream in = URI.create(url).toURL().openStream()) {
	        Files.copy(in, Paths.get(fileName));
	    }
	}
	
	
}
