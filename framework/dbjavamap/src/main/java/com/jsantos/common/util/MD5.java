package com.jsantos.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5 {
	private static final Logger log = LoggerFactory.getLogger(MD5.class);
	
	public static void main(String[] args){
		String sample = "\u6D4B\u8BD5";
		//sample = "idc";
		String algorithm = "MD5";
		log.info(digest(algorithm, sample,"ISO-8859-1"));
		log.info(digest(algorithm, sample,"UTF-16"));
		log.info(digest(algorithm, sample,"UTF-16BE"));
		log.info(digest(algorithm, sample,"UTF-16LE"));
		log.info(digest(algorithm, sample,"UTF-8"));
		
		sample = "867";//c856e1a9e036f11ac964b36c34c6bd64
		String salt = "circlepix123";
		String salted = sample + salt;
		log.info(sample + " " + digest(algorithm, salted, "UTF-8"));
	}

	private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
	
    public static String digest(String algorithm, String text, String encoding) {
    	try{
	        MessageDigest md = MessageDigest.getInstance(algorithm);
	        md.reset();
	        md.update(text.getBytes(encoding));
	        final byte[] md5hash = md.digest();
	        return convertToHex(md5hash);
	    }
	    catch(NoSuchAlgorithmException e){
	    	throw new RuntimeException(e);
	    } catch (UnsupportedEncodingException e) {
	    	throw new RuntimeException(e);
		}
    }
    public static String digest(String text) {
    	return digest("MD5", text,"UTF-16" );
    }
}
