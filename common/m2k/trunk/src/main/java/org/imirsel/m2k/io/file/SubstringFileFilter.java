package org.imirsel.m2k.io.file;

import java.io.File;
import java.io.FileFilter;

public class SubstringFileFilter implements FileFilter {
    String[] Substring;
    
    public SubstringFileFilter(String Substrings) {
        this.Substring = Substrings.toLowerCase().split("[,]");
        for(int i=0;i<this.Substring.length;i++) {
            this.Substring[i].trim();
        }
    }
    
    public boolean accept(File file) {
        for (int i=0;i<this.Substring.length;i++) {
            if (file.getName().toLowerCase().indexOf(Substring[i]) != -1)
                return true;
        }
        return false;
    }
}
