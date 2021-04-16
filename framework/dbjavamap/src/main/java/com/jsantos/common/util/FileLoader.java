package com.jsantos.common.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileLoader {
	private static final Logger log = LoggerFactory.getLogger(FileLoader.class);
	
	public String loadFile(String resourcePath) throws IOException {
		try{
			ClassLoader classLoader = getClass().getClassLoader();
			 File file = new File(classLoader.getResource(resourcePath).getFile());
			 try(InputStream inputStream = new FileInputStream(file)){
				 return  inputStream.toString();
			 }
		}
		catch (Exception e){ 
			log.info("Exception: " + e + " trying to recover file: " + resourcePath + " from resources ");
			throw e;
		}
	}
	
	public  File loadFile(String resourcePath, String path) throws IOException {
			
		try{
			ClassLoader classLoader = getClass().getClassLoader();
			 File file = new File(classLoader.getResource("fileName").getFile());
			 InputStream inputStream = new FileInputStream(file);
			 File targetFile = new File(new File(resourcePath).getName());
			 copyInputStreamToFile(inputStream,targetFile);
			 return targetFile;
			 
			
		}
		catch (Exception e){
			log.info("Exception: " + e + " trying to recover file: " + resourcePath + " from resources ");
			throw e;
		}
		
	}

	private static void copyInputStreamToFile( InputStream in, File file ) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        log.error("ERROR STACKTRACE:",e);
	    }
	}
}
