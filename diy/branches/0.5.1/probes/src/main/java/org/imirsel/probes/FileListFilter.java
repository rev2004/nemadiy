package org.imirsel.probes;

import java.io.File;
import java.io.FilenameFilter;

public class FileListFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		if(new File(dir,name).isFile()){
			return true;
		}
		return false;
	}
}
