/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.model;

import java.io.Serializable;

/**
 * A class representing a set of tracks from the NEMA repository. Sets are primarily used to 
 * provide lists of tracks for use with <code>NEMADataset</code>s. Hence, <code>NEMASet</code>
 * Objects have a unique ID, a <code>NEMADataset</code> ID to which they belong, set type information
 * and a split number (identifying which split within the dataset that the set belongs to). 
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

    /**
     * Constructor. Sets only the set ID. All other parameters must be manually set if they are used.
     * @param id The ID to set.
     */
    public NEMASet(int id){
        this.id = id;
    }

    /**
     * Constructor. Sets the ID, dataset ID, set type ID, set type name and split number.
     * @param id The set ID.
     * @param datasetId The dataset ID to which this set belongs.
     * @param setTypeId The type ID of the set.
     * @param setTypeName The type name corresponding to the set type ID (e.g. subset, train, test etc.)
     * @param splitNumber The split number within the dataset to which the set belongs. 
     */
    public NEMASet(int id, int datasetId, int setTypeId, String setTypeName,
                   int splitNumber){
        this.id = id;
        this.datasetId = datasetId;
        this.setTypeId = setTypeId;
        this.setTypeName = setTypeName;
        this.splitNumber = splitNumber;
    }

    /**
     * Returns the ID of the set.
     * @return the ID.
     */
    public int getId(){
        return id;
    }

    /**
     * Sets the ID of the set.
     * @param id the ID to set.
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Returns the ID of the <code>NEMADataset</code> that the set belongs to.
     * @return the ID of the dataset.
     */
    public int getDatasetId(){
        return datasetId;
    }

    /**
     * Sets the ID of the <code>NEMADataset</code> that the set belongs to.
     * @param datasetId the dataset ID to set.
     */
    public void setDatasetId(int datasetId){
        this.datasetId = datasetId;
    }

    /** 
     * Returns the set type ID.
     * @return the set type ID.
     */
    public int getSetTypeId(){
        return setTypeId;
    }

    /**
     * Sets the set type ID.
     * @param setTypeId the set type ID to set.
     */
    public void setSetTypeId(int setTypeId){
        this.setTypeId = setTypeId;
    }

    /**
     * Returns the set type name.
     * @return the setTypeName.
     */
    public String getSetTypeName(){
        return setTypeName;
    }

    /**
     * Sets the set type name.
     * @param setTypeName the setTypeName to set
     */
    public void setSetTypeName(String setTypeName){
        this.setTypeName = setTypeName;
    }
    
    /**
     * Returns the split number within the dataset that the set belongs to.
     * @return the split number.
     */
    public int getSplitNumber(){
        return splitNumber;
    }

    /**
     * Sets the split number within the dataset that the set belongs to.
     * @param splitNumber the split number to set.
     */
    public void setSplitNumber(int splitNumber){
        this.splitNumber = splitNumber;
    }

    @Override
    /**
     * HashCodes are based on the set ID which should be uniquely assigned by the repository DB.
     * @return the HashCode.
     */
    public int hashCode(){
        return id;
    }

    @Override
    /**
     * Returns true if the other object is an instance of <code>NEMASet</code> and has the same ID.
     * @param object The Object to compare to.
     * @return a boolean indicating equality.
     */
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
        return "org.imirsel.nema.repository.NEMASet[id=" + id + ", datasetID=" + datasetId + ", splitNum=" + splitNumber + "," +
        		" setTypeID=" + setTypeId + ", setTypeName=" + setTypeName + "]";
    }

}
