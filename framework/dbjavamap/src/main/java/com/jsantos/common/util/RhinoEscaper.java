package com.jsantos.common.util;

public class RhinoEscaper{
	
		public static void main(String[] args){
			//System.out.println(RhinoEscaper.js_escape("L'inmobilier"));
		}
	
	    public static String js_escape(String s) {
	        final int
	            URL_XALPHAS = 1,
	            URL_XPALPHAS = 2,
	            URL_PATH = 4;

	        int mask = URL_XALPHAS | URL_XPALPHAS | URL_PATH;

	        StringBuffer sb = null;
	        for (int k = 0, L = s.length(); k != L; ++k) {
	            int c = s.charAt(k);
	            if (mask != 0
	                && ((c >= '0' && c <= '9')
	                    || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
	                    || c == '@' || c == '*' || c == '_' || c == '-' || c == '.'
	                    || (0 != (mask & URL_PATH) && (c == '/' || c == '+'))))
	            {
	                if (sb != null) {
	                    sb.append((char)c);
	                }
	            } else {
	                if (sb == null) {
	                    sb = new StringBuffer(L + 3);
	                    sb.append(s);
	                    sb.setLength(k);
	                }

	                int hexSize;
	                if (c < 256) {
	                    if (c == ' ' && mask == URL_XPALPHAS) {
	                        sb.append('+');
	                        continue;
	                    }
	                    sb.append('%');
	                    hexSize = 2;
	                } else {
	                    sb.append('%');
	                    sb.append('u');
	                    hexSize = 4;
	                }

	                // append hexadecimal form of c left-padded with 0
	                for (int shift = (hexSize - 1) * 4; shift >= 0; shift -= 4) {
	                    int digit = 0xf & (c >> shift);
	                    int hc = (digit < 10) ? '0' + digit : 'A' - 10 + digit;
	                    sb.append((char)hc);
	                }
	            }
	        }

	        return (sb == null) ? s : sb.toString();
	    }		
	    
	    public static String js_unescape(String s)
	    {
	    	if (null ==s) return null;
	    	
	        int firstEscapePos = s.indexOf('%');
	        if (firstEscapePos >= 0) {
	            int L = s.length();
	            char[] buf = s.toCharArray();
	            int destination = firstEscapePos;
	            for (int k = firstEscapePos; k != L;) {
	                char c = buf[k];
	                ++k;
	                if (c == '%' && k != L) {
	                    int end, start;
	                    if (buf[k] == 'u') {
	                        start = k + 1;
	                        end = k + 5;
	                    } else {
	                        start = k;
	                        end = k + 2;
	                    }
	                    if (end <= L) {
	                        int x = 0;
	                        for (int i = start; i != end; ++i) {
	                            x = xDigitToInt(buf[i], x);
	                        }
	                        if (x >= 0) {
	                            c = (char)x;
	                            k = end;
	                        }
	                    }
	                }
	                buf[destination] = c;
	                ++destination;
	            }
	            s = new String(buf, 0, destination);
	        }
	        return s;
	    }
	    
	    public static int xDigitToInt(int c, int accumulator)
	    {
	    	check: {
	    	// Use 0..9 < A..Z < a..z
	    	if (c <= '9') {
	    		c -= '0';
	    		if (0 <= c) { break check; }
	    	} else if (c <= 'F') {
	    		if ('A' <= c) {
	    			c -= ('A' - 10);
	    			break check;
	    		}
	    	} else if (c <= 'f') {
	    		if ('a' <= c) {
	    			c -= ('a' - 10);
	    			break check;
	    		}
	    	}
	    	return -1;
	    }
	    return (accumulator << 4) | c;
	    }
	    
	    
}

