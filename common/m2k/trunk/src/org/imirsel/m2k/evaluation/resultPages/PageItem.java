/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation.resultPages;

/**
 *
 * @author kriswest
 */
public abstract class PageItem {
    protected String name;
    protected String caption;

    public PageItem(String name, String caption){
        this.name = name;
        this.caption = caption;

        if(this.name.contains("\\s")){
            throw new IllegalArgumentException("The item name should be suitable for use as an identifier in html and should not contain reserved characters or spaces");
        }
    }

    /**
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @return the caption
     */
    public String getCaption(){
        return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption){
        this.caption = caption;
    }

    public abstract String getHeadData();

    public abstract String getBodyData(boolean topLink);

}
