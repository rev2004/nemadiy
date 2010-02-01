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
    private String subjectTrackMetadataName;
    private int filterTrackMetadataId;
    private String filterTrackMetadataName;
    private int taskId;
    private String taskName;

    public NEMADataset(int id){
        this.id = id;
    }

    public NEMADataset(int id, String name, String description,
                       int collectionId, int numSplits,
                       int subjectTrackMetadataId, String subjectTrackMetadataName, 
                       int taskId, String taskName){
        this.id = id;
        this.name = name;
        this.description = description;
        this.collectionId = collectionId;
        this.numSplits = numSplits;
        this.subjectTrackMetadataId = subjectTrackMetadataId;
        this.subjectTrackMetadataName = subjectTrackMetadataName;
        this.taskId = taskId;
        this.taskName = taskName;
    }

    public NEMADataset(int id, String name, String description, int collectionId,
                       int subsetSetId, int numSplits, int numSetPerSplit,
                       String splitClass, String splitParametersString,
                       int subjectTrackMetadataId, String subjectTrackMetadataName,
                       int filterTrackMetadataId, String filterTrackMetadataName,
                       int taskId, String taskName){
        this.id = id;
        this.name = name;
        this.description = description;
        this.collectionId = collectionId;
        this.subsetSetId = subsetSetId;
        this.numSplits = numSplits;
        this.numSetPerSplit = numSetPerSplit;
        this.splitClass = splitClass;
        this.splitParametersString = splitParametersString;
        this.subjectTrackMetadataId = subjectTrackMetadataId;
        this.filterTrackMetadataId = filterTrackMetadataId;
        this.subjectTrackMetadataName = subjectTrackMetadataName;
        this.filterTrackMetadataName = filterTrackMetadataName;
        this.taskId = taskId;
        this.taskName = taskName;
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

    public int getTaskId(){
        return taskId;
    }

    public void setTaskId(int taskId){
        this.taskId = taskId;
    }

    /**
     * @return the subjectTrackMetadataName
     */
    public String getSubjectTrackMetadataName(){
        return subjectTrackMetadataName;
    }

    /**
     * @param subjectTrackMetadataName the subjectTrackMetadataName to set
     */
    public void setSubjectTrackMetadataName(String subjectTrackMetadataName){
        this.subjectTrackMetadataName = subjectTrackMetadataName;
    }

    /**
     * @return the filterTrackMetadataName
     */
    public String getFilterTrackMetadataName(){
        return filterTrackMetadataName;
    }

    /**
     * @param filterTrackMetadataName the filterTrackMetadataName to set
     */
    public void setFilterTrackMetadataName(String filterTrackMetadataName){
        this.filterTrackMetadataName = filterTrackMetadataName;
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

    /**
     * @return the taskName
     */
    public String getTaskName(){
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

}
