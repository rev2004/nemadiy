package org.imirsel.nema.contentrepository.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.imirsel.nema.model.FileDataType;
import org.imirsel.nema.model.GroupDataType;
import org.imirsel.nema.model.OsDataType;


/**Returns various resource types
 * 
 * @author kumaramit01
 * @since 0.3.0
 */
final public class ResourceTypeServiceImpl implements ResourceTypeService {
	
	/**Version of this class
	 * 
	 */
	private static final long serialVersionUID = 5186004401706711111L;
	private final List<FileDataType> fileDataTypeList  = new ArrayList<FileDataType>();
	private final List<OsDataType> supportedOsList  = new ArrayList<OsDataType>();
	private final List<GroupDataType> groupList = new ArrayList<GroupDataType>();
	
	/**
	 * 
	 */
	public ResourceTypeServiceImpl(){
		fileDataTypeList.add( new FileDataType("Chord Interval Text File",
				"org.imirsel.nema.analytics.evaluation.chord.ChordIntervalTextFile"));
		fileDataTypeList.add( new FileDataType("Chord Short Hand Text File",
		"org.imirsel.nema.analytics.evaluation.chord.ChordShortHandTextFile"));
		fileDataTypeList.add( new FileDataType("Chord Number Text File",
		"org.imirsel.nema.analytics.evaluation.chord.ChordNumberTextFile"));
		fileDataTypeList.add( new FileDataType("Classification Text File",
		"org.imirsel.nema.analytics.evaluation.classifiction.ClassificationTextFile"));
		fileDataTypeList.add( new FileDataType("Melody Text File",
		"org.imirsel.nema.analytics.evaluation.melody.MelodyTextFile"));
		fileDataTypeList.add( new FileDataType("Key Text File",
		"org.imirsel.nema.analytics.evaluation.key.KeyTextFile"));
		
		supportedOsList.add(new OsDataType("Unix","Unix Like"));
		supportedOsList.add(new OsDataType("Windows","Windows Like"));
		
		groupList.add(new GroupDataType("imirsel","imirsel"));
		
	}
	
	/**
	 * 
	 */
	public final List<FileDataType> getSupportedFileDataTypes(){
		return Collections.unmodifiableList(fileDataTypeList);
	}
	/**
	 * 
	 */
	public final List<OsDataType> getSupportedOperatingSystems(){
		return Collections.unmodifiableList(this.supportedOsList);
	}
	/**
	 * 
	 */
	public final List<GroupDataType> getSupportedGroups(){
		return Collections.unmodifiableList(this.groupList);
	}
	
	
	/**
	 * Returns the OsDataType for a string value
	 * 
	 * @param value
	 * @return OsDataType -returns the OsDataType
	 */
	public final OsDataType getOsDataType(String value){
		if(value.equalsIgnoreCase("Unix Like")){
			OsDataType os = new OsDataType("Unix","Unix Like");
			return os;
		}else if(value.equals("Windows Like")){
			OsDataType os = new OsDataType("Windows","Windows Like");
			return os;
		}else{
			throw new IllegalArgumentException("error invalid Os: " + value);
		}
		
	}

}
