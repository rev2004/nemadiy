/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author kriswest
 */
public class NEMAFile implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String trackId;
    private String path;

    public NEMAFile(){
    }

    public NEMAFile(int id){
        this.id = id;
    }

    public NEMAFile(int id, String trackId, String path){
        this.id = id;
        this.trackId = trackId;
        this.path = path;
    }

    public int getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getTrackId(){
        return trackId;
    }

    public void setTrackId(String trackId){
        this.trackId = trackId;
    }

    public String getPath(){
        return path;
    }

    public File getFile(){
        return new File(path);
    }

    public void setPath(String path){
        this.path = path;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof NEMAFile)){
            return false;
        }
        NEMAFile other = (NEMAFile)object;
        if (id == other.id){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "org.imirsel.nema.repository.NEMAFile[id=" + id + "]";
    }

}
