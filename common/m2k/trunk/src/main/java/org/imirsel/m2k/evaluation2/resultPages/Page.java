/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.resultPages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.imirsel.m2k.io.file.CopyFileFromClassPathToDisk;
import org.imirsel.m2k.io.file.IOUtil;

/**
 * Data-structure and utility class for writing result HTML pages.
 * 
 * @author kriswest
 */
public class Page {
    private String id;
    private String title;
    private List<PageItem> items;
    private boolean addTOC;

    /** Construct a page.
     * 
     * @param id The id for page (will be used to make the file name). Should not include whitespace
     * or strange characters.
     * @param title The title to display on the page and HTML title in the header.
     * @param items The items to display on the page.
     * @param addTOC A flag that determines if an indexto the content should be added to the top of 
     * the page.
     */
    public Page(String id, String title, List<PageItem> items, boolean addTOC){
        this.id = id;
        this.title = title;
        this.items = items;
        this.addTOC = addTOC;
    }

    /**
     * Writes a set of result pages to a specified directory.
     * 
     * @param set_title The title to use for the page set, which prepended to the page titles
     * in the HTML title tag in the header and displayed on the top of each page.
     * @param directory The directory to write the result pages to.
     * @param pages The list of pages to write out.
     */
    public static void writeResultPages(String set_title, File directory, List<Page> pages){
        if (!directory.exists()){
            directory.mkdirs();
        }

        //setup page names and files
        //String[] names = new String[pages.size()];
        String[] filenames = new String[pages.size()];

        int idx = 0;
        Iterator<Page> it = pages.iterator();
        //make first page index.htm
        Page aPage = it.next();
        filenames[idx] = "index.htm";
        idx++;
        
        //add subsequent pages
        for (; it.hasNext();){
            aPage = it.next();
            //names[idx] = aPage.getName();
            filenames[idx] = aPage.getId().replaceAll("\\s", "_") + ".htm";
            idx++;
        }

        //copy over resource files
        File rscFile = new File(directory.getAbsolutePath() + File.separator + "tableft.gif");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/resultPages/resources/tableft.gif", rscFile);

        rscFile = new File(directory.getAbsolutePath() + File.separator + "tabright.gif");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/resultPages/resources/tabright.gif", rscFile);

        rscFile = new File(directory.getAbsolutePath() + File.separator + "logo.png");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/resultPages/resources/logo.png", rscFile);

        rscFile = new File(directory.getAbsolutePath() + File.separator + "tableblue.css");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/resultPages/resources/tableblue.css", rscFile);

        rscFile = new File(directory.getAbsolutePath() + File.separator + "menu.css");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/resultPages/resources/menu.css", rscFile);


        it = pages.iterator();
        idx = 0;
        for (; it.hasNext();){
            //write out each page
            aPage = it.next();
            File pageFile = new File(directory.getAbsolutePath() + File.separator + filenames[idx]);
            String pageContent = createPageHTML(set_title, aPage, pages, filenames);
            try{
                IOUtil.writeStringToFile(pageFile, pageContent, "utf-8");
            }catch (UnsupportedEncodingException ex){
                Logger.getLogger(Page.class.getName()).log(Level.SEVERE, "UTF-8 encoding was not available!?!", ex);
            }catch (FileNotFoundException ex){
                Logger.getLogger(Page.class.getName()).log(Level.SEVERE, null, "Could not write to file: " + pageFile.getAbsolutePath());
            }

            idx++;
        }
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

    /**
     * Creates the HTML for the page.
     * 
     * @param set_name The name of the page set that the page being written belongs to.
     * @param currPage The page to create HTML for.
     * @param pages The list of pages in the set. Used to create navigation tabs.
     * @param pageFileNames The names of the pages in the set. Used to create navigation 
     * tabs.
     * @return A string containing the HTML data.
     */
    public static String createPageHTML(String set_name, Page currPage, List<Page> pages, String[] pageFileNames){
        String out = createHeader(set_name, currPage.getTitle());
        for (Iterator<PageItem> it = currPage.getItems().iterator(); it.hasNext();){
            out += it.next().getHeadData();
        }
        out += closeheader(set_name);
        out += createTabs(currPage, pages, pageFileNames);
        out += startContent();
        if(currPage.getAddTOC()){
            out += createIndex(currPage);
        }
        Iterator<PageItem> it = currPage.getItems().iterator();
        if(it.hasNext()){
            out += it.next().getBodyData(false);
        }
        for (; it.hasNext();){
            out += it.next().getBodyData(true);
        }
        out += createFooter();

        return out;
    }

    /**
     * Opens a head tag in HTML.
     * 
     * @param set_name The name of the page set to be prepended to the page title in the header title tag.
     * @param title The title of the page.
     * @return The HTML for the start of the head tag.
     */
    private static String createHeader(String set_name, String title){
        String out = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
        "<head>\n" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\n" +
        "<title>";
        out += set_name + " - " + title;
        out += "</title>\n\n";
        out += "<LINK REL=StyleSheet HREF=\"menu.css\" TYPE=\"text/css\" >\n";
        out += "<LINK REL=StyleSheet HREF=\"tableblue.css\" TYPE=\"text/css\" >\n\n";
        out += "<!-- Combo-handled YUI CSS files: --> \n";
        out += "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://yui.yahooapis.com/combo?2.8.0r4/build/datatable/assets/skins/sam/datatable.css\"> \n";
        out += "<!-- Combo-handled YUI JS files: --> \n";
        out += "<script type=\"text/javascript\" src=\"http://yui.yahooapis.com/combo?2.8.0r4/build/yahoo-dom-event/yahoo-dom-event.js&2.8.0r4/build/dragdrop/dragdrop-min.js&2.8.0r4/build/element/element-min.js&2.8.0r4/build/datasource/datasource-min.js&2.8.0r4/build/datatable/datatable-min.js\"></script>\n\n";

        return out;
    }

    /**
     * Closes the head tag in HTML and starts the body with a header giving the name of the
     * page set.
     * 
     * @param set_name The name of the page set to be displayed at the top each page.
     * @return The closing head tag and the first part of the body with the page set header.
     */
    private static String closeheader(String set_name){

        String out = "</head>\n\n";
        out += "<body>\n\n";
        out += "<table id=\"h2table\">\n";
        out += "\t<tr>\n";
        out += "\t\t<td><img src=\"logo.png\" width=\"160\"></td>\n";
        out += "\t\t<td><h2>" + set_name  + "</h2></td>\n";
        out += "\t</tr>\n";
        out += "</table>\n\n";
        return out;
    }

    /**
     * Starts the body content.
     * @return The opening DIV tag in HTML.
     */
    private static String startContent(){
        String out = "<div id=\"content\">\n";
        return out;
    }

    /**
     * Creates the navigation tabs.
     * 
     * @param currPage The current page.
     * @param pages The list of all pages in the set.
     * @param pageFileNames The names of pages in the set.
     * @return The tab HTML.
     */
    private static String createTabs(Page currPage, List<Page> pages, String[] pageFileNames){
        String out = "<div id=\"tabs\">\n";
        out +="\t<ul>\n";
        int idx = 0;
        for (Iterator<Page> it = pages.iterator(); it.hasNext();){
            Page page = it.next();
            String line = "<li><a href=\"" + pageFileNames[idx] + "\" title=\"" + page.getTitle() + "\"><span>" + page.getTitle() + "</span></a></li>";
            if (currPage == page){
                out += "\t\t<div id=\"tabSel\">\n\t\t\t" + line + "\n\t\t</div>\n";
            }else{
                out += "\t\t" + line + "\n";
            }
            idx++;
        }
        out += "\t</ul>\n";
        out += "</div>\n\n<br><a name=\"top\"></a>\n\n";

        return out;
    }

    /**
     * Creates an index to the page content.
     * 
     * @param currPage The page to create an index for.
     * @return The index HTML.
     */
    private static String createIndex(Page currPage){
        String out = "<h3>" + currPage.getTitle() + "</h3>\n";
        out +="<ul>\n";
        for (Iterator<PageItem> it = currPage.getItems().iterator(); it.hasNext();){
            PageItem item = it.next();
            out +="\t<li><a href=\"#" + item.getName() + "\">" + item.getCaption() + "</a></li>\n";
        }

        out += "</ul>\n";
        out += "<br>\n\n";
        return out;
    }

    /**
     * Closes the content DIV, body and HTML tags.
     * 
     * @return the closing tags in HTML.
     */
    private static String createFooter(){
        String out = "</div>\n";
        out += "</body>\n";
        out += "</html>\n";
        return out;
    }
}
