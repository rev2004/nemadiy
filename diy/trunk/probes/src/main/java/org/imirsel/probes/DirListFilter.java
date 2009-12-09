package org.imirsel.probes;

import java.io.File;
import java.io.FilenameFilter;

public class DirListFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		if(new File(dir,name).isDirectory()){
			return true;
		}
		return false;
	}
}
