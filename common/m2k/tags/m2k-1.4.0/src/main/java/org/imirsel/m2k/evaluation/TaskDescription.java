package org.imirsel.m2k.evaluation;

/**
 * Data-structure encoding the description of a particular task including an ID, name
 * and description for the task, an ID name and description for the dataset used and
 * the metadata type that is the subject of the task.
 * 
 * @author kriswest
 *
 */
public class TaskDescription {
	private int taskID = -1;
	private String taskName = "<none given>";
	private String taskDescription = "";
	private String metadataPredicted = "genre"; 
	private int datasetId = -1;
	private String datasetName = "<none given>";
	private String datasetDescription = "";
	
	public TaskDescription(){
		
	}
	
	public TaskDescription(int taskID_, String taskName_, String taskDescription_,
			String metadataPredicted_, int datasetId_, String datasetName_,
			String datasetDescription_) {
		taskID = taskID_;
		taskName = taskName_;
		taskDescription = taskDescription_;
		metadataPredicted = metadataPredicted_;
		datasetId = datasetId_;
		datasetName = datasetName_;
		datasetDescription = datasetDescription_;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setMetadataPredicted(String metadataPredicted) {
		this.metadataPredicted = metadataPredicted;
	}

	public String getMetadataPredicted() {
		return metadataPredicted;
	}

	public void setDatasetId(int datasetId) {
		this.datasetId = datasetId;
	}

	public int getDatasetId() {
		return datasetId;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetDescription(String datasetDescription) {
		this.datasetDescription = datasetDescription;
	}

	public String getDatasetDescription() {
		return datasetDescription;
	}
	
	@Override
	public String toString() {
		String out = 
			   "\tTask ID:             " + taskID + "\n";
		out += "\tTask name:           " + taskName + "\n";
		out += "\tTask description:    " + taskDescription + "\n";
		out += "\tDataset ID:          " + datasetId + "\n";
		out += "\tDataset name:        " + datasetName + "\n";
		out += "\tDataset description: " + datasetDescription + "\n";
		out += "\tSubject metadata:    " + metadataPredicted + "\n";
		return out;
	}
	
}
