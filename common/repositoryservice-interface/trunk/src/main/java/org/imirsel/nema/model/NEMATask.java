/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.model;

import java.io.Serializable;

/**
 * To be redesigned.... probably with a linked dataset and evaluator type.
 * @author kriswest
 */
public class NEMATask implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String description;
    private int subjectTrackMetadataID;
    private String subjectTrackMetadataName;
    private int taskTypeID;
    private String taskType;

    public NEMATask(){
    }

    public NEMATask(Integer id){
        this.id = id;
    }

    public NEMATask(int id, String name, String description, int subjectTrackMetadataID,
            String subjectTrackMetadataName, int taskTypeID, String taskType){
        this.id = id;
        this.name = name;
        this.description = description;
        this.subjectTrackMetadataName = subjectTrackMetadataName;
        this.subjectTrackMetadataID = subjectTrackMetadataID;
        this.taskTypeID = taskTypeID;
        this.taskType = taskType;
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

    public String getSubjectTrackMetadataName(){
        return subjectTrackMetadataName;
    }

    public void setSubjectTrackMetadataName(String subjectTrackMetadataName){
        this.subjectTrackMetadataName = subjectTrackMetadataName;
    }

    public String getTaskType(){
        return taskType;
    }

    public void setTaskType(String taskType){
        this.taskType = taskType;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof NEMATask)){
            return false;
        }
        NEMATask other = (NEMATask)object;
        if (this.id == other.id){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "org.imirsel.nema.repository.NEMATask[id=" + id + "]";
    }

    /**
     * @return the subjectTrackMetadataID
     */
    public int getSubjectTrackMetadataID(){
        return subjectTrackMetadataID;
    }

    /**
     * @param subjectTrackMetadataID the subjectTrackMetadataID to set
     */
    public void setSubjectTrackMetadataID(int subjectTrackMetadataID){
        this.subjectTrackMetadataID = subjectTrackMetadataID;
    }

    /**
     * @return the taskTypeID
     */
    public int getTaskTypeID(){
        return taskTypeID;
    }

    /**
     * @param taskTypeID the taskTypeID to set
     */
    public void setTaskTypeID(int taskTypeID){
        this.taskTypeID = taskTypeID;
    }

}
