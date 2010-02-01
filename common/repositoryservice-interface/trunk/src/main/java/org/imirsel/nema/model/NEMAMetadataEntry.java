/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.model;

import java.io.Serializable;

/**
 *
 * @author kriswest
 */
public class NEMAMetadataEntry implements Serializable{
    private static final long serialVersionUID = 1L;

    private String type;
    private String value;

    public NEMAMetadataEntry(String type, String value){
        this.type = type;
        this.value = value;
    }

    /**
     * @return the type
     */
    public String getType(){
        return type;
    }

    /**
     * @param type the type to set
     * @param value 
     */
    public void setType(String type, String value){
        this.type = type;
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue(){
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value){
        this.value = value;
    }

    @Override
    public int hashCode(){
        return (type + "--" + value).hashCode();
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof NEMAMetadataEntry)){
            return false;
        }
        NEMAMetadataEntry other = (NEMAMetadataEntry)object;
        if (type.equals(other.type) && value.equals(other.value)){
            return true;
        }
        return false;
    }

}
