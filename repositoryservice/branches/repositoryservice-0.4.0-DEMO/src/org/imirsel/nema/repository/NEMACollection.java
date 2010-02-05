package org.imirsel.nema.repository;

import java.io.Serializable;

/**
 *
 * @author kriswest
 */
public class NEMACollection implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String description;

    public NEMACollection(int id){
        this.id = id;
    }

    public NEMACollection(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof NEMACollection)){
            return false;
        }
        NEMACollection other = (NEMACollection)object;
        if (id == other.id){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "org.imirsel.nema.repository.NEMACollection[id=" + id + "]";
    }

}
