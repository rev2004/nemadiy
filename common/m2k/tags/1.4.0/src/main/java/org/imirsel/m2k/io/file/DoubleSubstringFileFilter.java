package org.imirsel.m2k.io.file;

import java.io.File;
import java.io.FileFilter;

public class DoubleSubstringFileFilter implements FileFilter {
    String Substring[];
    String ExcludeSubstring[];
    
    public DoubleSubstringFileFilter(String Substrings, String ExcludeSubstrings) {
        this.Substring = Substrings.toLowerCase().split("[,]");
        for(int i=0;i<this.Substring.length;i++) {
            this.Substring[i].trim();
        }
        this.ExcludeSubstring = ExcludeSubstrings.toLowerCase().split("[,]");
        for(int i=0;i<this.ExcludeSubstring.length;i++) {
            this.ExcludeSubstring[i].trim();
        }
    }
    
    public boolean accept(File file) {
        boolean accept = false;
        for (int i=0;i<this.Substring.length;i++) {
            if (file.getName().toLowerCase().indexOf(Substring[i]) != -
                    1)
                accept = true;
        }
        for (int i=0;i<this.ExcludeSubstring.length;i++) {
            if (file.getName().toLowerCase().indexOf(ExcludeSubstring[i]) != -1){
                accept = false;
            }
        }
        return accept;
    }
}
