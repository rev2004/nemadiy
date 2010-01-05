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
public class NEMASet implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Integer datasetId;
    private Integer setTypeId;
    private String setTypeName;
    private Integer splitNumber;

    public NEMASet(int id){
        this.id = id;
    }

    public NEMASet(int id, int datasetId, int setTypeId, String setTypeName,
                   int splitNumber){
        this.id = id;
        this.datasetId = datasetId;
        this.setTypeId = setTypeId;
        this.setTypeName = setTypeName;
        this.splitNumber = splitNumber;
    }



    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getDatasetId(){
        return datasetId;
    }

    public void setDatasetId(int datasetId){
        this.datasetId = datasetId;
    }

    public int getSetTypeId(){
        return setTypeId;
    }

    public void setSetTypeId(int setTypeId){
        this.setTypeId = setTypeId;
    }

    public int getSplitNumber(){
        return splitNumber;
    }

    public void setSplitNumber(int splitNumber){
        this.splitNumber = splitNumber;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof NEMASet)){
            return false;
        }
        NEMASet other = (NEMASet)object;
        if (id == other.id){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "org.imirsel.nema.repository.NEMASet[id=" + id + "]";
    }

    /**
     * @return the setTypeName
     */
    public String getSetTypeName(){
        return setTypeName;
    }

    /**
     * @param setTypeName the setTypeName to set
     */
    public void setSetTypeName(String setTypeName){
        this.setTypeName = setTypeName;
    }

}
