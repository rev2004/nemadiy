/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation.resultPages;

import java.util.List;

/**
 *
 * @author kriswest
 */
public class Page {
    private String id;
    private String title;
    private List<PageItem> items;
    private boolean addTOC;

    public Page(String id, String title, List<PageItem> items, boolean addTOC){
        this.id = id;
        this.title = title;
        this.items = items;
        this.addTOC = addTOC;
    }

    public void addItem(PageItem item){
        getItems().add(item);
    }

    /**
     * @return the items
     */
    public List<PageItem> getItems(){
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<PageItem> items){
        this.items = items;
    }

    /**
     * @return the name
     */
    public String getTitle(){
        return title;
    }

    /**
     * @param name the name to set
     */
    public void setTitle(String name){
        this.title = name;
    }

    /**
     * @return the addTOC
     */
    public boolean getAddTOC(){
        return addTOC;
    }

    /**
     * @param addTOC the addTOC to set
     */
    public void setAddTOC(boolean addTOC){
        this.addTOC = addTOC;
    }

    /**
     * @return the id
     */
    public String getId(){
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id){
        this.id = id;
    }

}
