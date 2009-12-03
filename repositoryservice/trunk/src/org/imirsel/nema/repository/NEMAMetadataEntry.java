/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository;

import java.io.Serializable;

/**
 *
 * @author kriswest
 */
public class NEMAMetadataEntry implements Serializable{
    private String type;
    private String value;

    /**
     * @return the type
     */
    public String getType(){
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type){
        this.type = type;
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
