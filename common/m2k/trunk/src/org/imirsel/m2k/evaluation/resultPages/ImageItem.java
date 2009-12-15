/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation.resultPages;

/**
 *
 * @author kriswest
 */
public class ImageItem extends PageItem{
    protected String imagePath;

    public ImageItem(String name, String caption, String imagePath){
        super(name,caption);
        this.imagePath = imagePath;
    }

    /**
     * @return the relative path to the image
     */
    public String getImagePath(){
        return imagePath;
    }

    /**
     * @param imagePath the relative path to the image
     */
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    @Override
    public String getHeadData(){
        return "";
    }

    @Override
    public String getBodyData(boolean topLink){
        String out = "<a name=\"" + getName() + "\"></a>\n" +
                "<h4>" + getCaption();
        if (topLink){
            out += "s&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"toplink\"><a href=\"#top\">[top]</a></span>";
        }
        out += "</h4>\n";
        out += "<a href=\"" + imagePath + "\">\n";
	out += "\t<img width=\"710\" src=\"" + imagePath + "\">\n";
	out += "</a>\n";
        out += "<br>\n\n";
        return out;
    }

    
}
