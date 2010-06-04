/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Class representing a published result for an algorithm against a dataset. 
 * 
 * @author kris.west@gmail.com
 */
public class NemaPublishedResult implements Serializable{
    public static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String name;
    private String result_path;
    private Date date;

    /**
     * Constructor. An ID for the result, the username of the publishing user, an algorithm name,
     * a path to the results on the filesystem and date of publication must be specified.
     * 
     * @param id The result ID.
     * @param username The username of the publishing user.
     * @param name The name of the system or algorithm that produced the result.
     * @param result_path The path to the results directory on the filesystem/
     * @param date The date of publication.
     */
    public NemaPublishedResult(int id, String username, String name, String result_path, Date date){
        this.id = id;
        this.username = username;
        this.name = name;
        this.result_path = result_path;
        this.date = date;
    }

    /**
     * @return the id
     */
    public int getId(){
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id){
        this.id = id;
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
     * @return the result_path
     */
    public String getResult_path(){
        return result_path;
    }

    /**
     * @param result_path the result_path to set
     */
    public void setResult_path(String result_path){
        this.result_path = result_path;
    }

    /**
     * @return the date
     */
    public Date getDate(){
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     * @return the username
     */
    public String getUsername(){
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username){
        this.username = username;
    }

	@Override
	public String toString() {
		return "org.imirsel.nema.model.NemaPublishedResult [date=" + date + ", id=" + id + ", name="
				+ name + ", result_path=" + result_path + ", username="
				+ username + "]";
	}


}
