/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation.resultPages;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author kriswest
 */
public class FileListItem extends PageItem{

    private List<String> paths;

    public FileListItem(String name, String caption, List<String> paths){
        super(name,caption);
        this.paths = paths;
    }

    /**
     * @return the files
     */
    public List<String> getPaths(){
        return paths;
    }

    /**
     * @param paths the file paths to set
     */
    public void setPaths(List<String> paths){
        this.paths = paths;
    }

    public void addPath(String path){
        this.paths.add(path);
    }

    @Override
    public String getHeadData(){
        return "";
    }

    public String getBodyData(boolean topLink){
        String out = "<a name=\"" + getName() + "\"></a>\n" +
                "<h4>" + getCaption();
        if (topLink){
            out += "s&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"toplink\"><a href=\"#top\">[top]</a></span>";
        }
        out += "</h4>\n";
        out += "<ul>\n";
        String path;
        for (Iterator<String> it = paths.iterator(); it.hasNext();){
            path = it.next();
            out += "\t<li><a href=\"" + path + "\">" + path + "</a>\n";
        }
	out += "</ul>\n";
	out += "<br>\n\n";
	return out;
    }

}
