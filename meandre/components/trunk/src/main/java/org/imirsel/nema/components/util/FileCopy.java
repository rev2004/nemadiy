package org.imirsel.nema.components.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopy {
	 public static String copy(String srcname, String dstname) throws IOException {
		File src = new File(srcname);
		File dst = new File(dstname);		
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);
	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
		return dst.getCanonicalPath();
		
	}
}



