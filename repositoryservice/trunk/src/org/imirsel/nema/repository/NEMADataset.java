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
public class NEMADataset implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String description;
    private int collectionId;
    private int subsetSetId;
    private int numSplits;
    private int numSetPerSplit;
    private String splitClass;
    private String splitParametersString;
    private int subjectTrackMetadataId;
    private int filterTrackMetadataId;

    public NEMADataset(int id){
        this.id = id;
    }

    public NEMADataset(int id, String name, String description,
                       int collectionId, int numSplits,
                       int subjectTrackMetadataId){
        this.id = id;
        this.name = name;
        this.description = description;
        this.collectionId = collectionId;
        this.numSplits = numSplits;
        this.subjectTrackMetadataId = subjectTrackMetadataId;
    }

    public int getId(){
        return id;
    }

    public void setId(Integer id){
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

    public int getCollectionId(){
        return collectionId;
    }

    public void setCollectionId(int collectionId){
        this.collectionId = collectionId;
    }

    public int getSubsetSetId(){
        return subsetSetId;
    }

    public void setSubsetSetId(int subsetSetId){
        this.subsetSetId = subsetSetId;
    }

    public int getNumSplits(){
        return numSplits;
    }

    public void setNumSplits(int numSplits){
        this.numSplits = numSplits;
    }

    public int getNumSetPerSplit(){
        return numSetPerSplit;
    }

    public void setNumSetPerSplit(int numSetPerSplit){
        this.numSetPerSplit = numSetPerSplit;
    }

    public String getSplitClass(){
        return splitClass;
    }

    public void setSplitClass(String splitClass){
        this.splitClass = splitClass;
    }

    public String getSplitParametersString(){
        return splitParametersString;
    }

    public void setSplitParametersString(String splitParametersString){
        this.splitParametersString = splitParametersString;
    }

    public int getSubjectTrackMetadataId(){
        return subjectTrackMetadataId;
    }

    public void setSubjectTrackMetadataId(int subjectTrackMetadataId){
        this.subjectTrackMetadataId = subjectTrackMetadataId;
    }

    public int getFilterTrackMetadataId(){
        return filterTrackMetadataId;
    }

    public void setFilterTrackMetadataId(int filterTrackMetadataId){
        this.filterTrackMetadataId = filterTrackMetadataId;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof NEMADataset)){
            return false;
        }
        NEMADataset other = (NEMADataset)object;
        if (id == other.id){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "org.imirsel.nema.repository.NEMADataset[id=" + id + "]";
    }

}
