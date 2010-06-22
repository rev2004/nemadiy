/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.imirsel.nema.repositoryservice.*;
import org.imirsel.nema.model.*;

/**
 * 
 * @author kris.west@gmail.com
 * @since 0.1.0
 */
public class RepositoryClientImpl implements RepositoryClientInterface{

    public static final String GET_COLLECTIONS_QUERY = "SELECT * FROM collection";
    private PreparedStatement getCollections;

//    public static final String GET_DATA_SETS_FOR_COLLECTION_QUERY = "SELECT * FROM dataset WHERE collection_id=?";
//    private PreparedStatement getDatasetsByCollection;

    public static final String GET_DATA_SETS_QUERY = "SELECT * FROM dataset";
    private PreparedStatement getDatasets;

    public static final String GET_DATA_SET_BY_ID_QUERY = "SELECT * FROM dataset WHERE id=?";
    private PreparedStatement getDatasetByID;
    
    public static final String GET_TASKS_QUERY = "SELECT * FROM task";
    private PreparedStatement getTasks;

    public static final String GET_TASKS_FOR_METADATA_QUERY = "SELECT * FROM task WHERE subject_track_metadata=?";
    private PreparedStatement getTasksForMeta;

    public static final String GET_TASK_BY_ID_QUERY = "SELECT * FROM task WHERE id=?";
    private PreparedStatement getTaskByID;

    public static final String GET_SUBSET_TRACKLIST_FOR_DATASET_QUERY = "SELECT trackList.* FROM trackList,dataset WHERE dataset.id=? AND trackList.id=dataset.subset_set_id";
    private PreparedStatement getSubsetTrackListForDataset;

    public static final String GET_EXPERIMENT_TRACKLISTS_FOR_DATASET_QUERY = "SELECT trackList.* FROM trackList,dataset WHERE trackList.dataset_id=? AND dataset.id=trackList.dataset_id AND trackList.id!=dataset.subset_set_id";
    private PreparedStatement getExpTrackListsForDataset;

    public static final String GET_TRACKLIST_TRACKS_QUERY = "SELECT track_id FROM trackList_track_link where set_id=?";
    private PreparedStatement getTrackListTracks;

    public static final String GET_FILE_METADATA_QUERY = "SELECT ffml.file_id, fm.metadata_type_id,fm.value FROM file_file_metadata_link ffml, file_metadata fm WHERE ffml.file_id=? AND ffml.file_metadata_id=fm.id";
    private PreparedStatement getFileMetadata;

    public static final String GET_TRACK_METADATA_QUERY = "SELECT tm.metadata_type_id, tm.value FROM track_track_metadata_link ttml, track_metadata tm WHERE ttml.track_id=? AND ttml.track_metadata_id=tm.id";
    private PreparedStatement getTrackMetadata;

    public static final String GET_ALL_TRACK_METADATA_QUERY = "SELECT ttml.track_id, tm.metadata_type_id,tm.value FROM track_track_metadata_link ttml, track_metadata tm WHERE ttml.track_metadata_id=tm.id";
    private PreparedStatement getAllTrackMetadata;

    public static final String GET_TRACK_METADATA_SPECIFIC_QUERY = "SELECT tm.metadata_type_id, tm.value FROM track_track_metadata_link ttml, track_metadata tm WHERE ttml.track_id=? AND tm.metadata_type_id=? AND ttml.track_metadata_id=tm.id";
    private PreparedStatement getTrackMetadataSpecific;

    public static final String GET_ALL_TRACK_METADATA_SPECIFIC_QUERY = "SELECT ttml.track_id, tm.metadata_type_id, tm.value FROM track_track_metadata_link ttml, track_metadata tm WHERE tm.metadata_type_id=? AND ttml.track_metadata_id=tm.id";
    private PreparedStatement getAllTrackMetadataSpecific;

//    public static final String GET_FILE_FOR_TRACK = "SELECT file.* FROM file WHERE file.track_id=? AND ";
    public static final String GET_CONSTRAINED_FILE_FOR_TRACK = 
            "SELECT file.*, site.site_name from file,site WHERE file.id IN (\n" +
            "SELECT file_id from file,file_metadata,file_file_metadata_link WHERE file.track_id=? AND file.id=file_file_metadata_link.file_id AND file.site=site.id\n";

    public static final String GET_CONSTRAINED_FILES =
        "SELECT file.*, site.site_name from file,site WHERE file.id IN (\n" +
        "SELECT file_id from file,file_metadata,file_file_metadata_link WHERE file.id=file_file_metadata_link.file_id AND file.site=site.id\n";

    public static final String GET_TRACKLIST_FILES =
        "SELECT file.*, site.site_name from file,site WHERE file.track_id IN (SELECT track_id from trackList_track_link WHERE set_id=?) AND file.site=site.id";
    private PreparedStatement getTracklistFiles;
    
//    public static final String GET_CONSTRAINED_TRACKLIST_FILES =
//        "SELECT file.* from file WHERE file.track_id IN (SELECT track_id from trackList_track_link WHERE set_id=?)";
    
    
    public static final String GET_FILE_METADATA = "SELECT * FROM file_metadata";
    private PreparedStatement getFileMeta;

    public static final String GET_ALL_TRACKS = "SELECT * FROM track";
    private PreparedStatement getAllTracks;


    public static final String INSERT_PUBLISHED_RESULT = "INSERT INTO published_results(dataset_id,username,system_name,result_path) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE result_path=VALUES(result_path)";
    private PreparedStatement insertPublishedResult;

    public static final String GET_PUBLISHED_RESULTS_FOR_DATASET = "SELECT * FROM published_results WHERE dataset_id=?";
    private PreparedStatement getPublishedResultsForDataset;

    public static final String GET_PUBLISHED_RESULTS_FOR_USERNAME = "SELECT * FROM published_results WHERE username=?";
    private PreparedStatement getPublishedResultsForUsername;

    public static final String DELETE_PUBLISHED_RESULT = "DELETE FROM published_results WHERE id=?";
    private PreparedStatement deletePublishedResult;

    
    //cached types maps
    public static final String GET_TRACK_METADATA_TYPES = "SELECT * FROM track_metadata_definitions";
    public static final String GET_FILE_METADATA_TYPES = "SELECT * FROM file_metadata_definitions";
    public static final String GET_TRACKLIST_TYPES = "SELECT * FROM trackList_type_definitions";
    public static final String GET_SITES = "SELECT * FROM site";
    private Map<Integer,String> trackMetadataTypeMap;
    private Map<Integer,String> fileMetadataTypeMap;
    private Map<Integer,String> trackListTypeMap;
    private Map<String,Integer> trackMetadataTypeMapRev;
    private Map<String,Integer> fileMetadataTypeMapRev;
    private Map<String,Integer> trackListTypeMapRev;
    private Map<Integer,String> siteNameMap;
    private Map<String,Integer> siteNameMapRev;

    protected DatabaseConnector dbCon;

    private static final Logger logger = Logger.getLogger(RepositoryClientImpl.class.getName());
	
    /**
     * Constructor. Establishes a connection to the repository database using the properties
     * represented in <code>org.imirsel.nema.repository.RepositoryProperties</code> and then
     * instantiates all the prepared statements that it is likely to use and that are fully
     * known in advance. Finally,several type maps are instantiated to speed up type resolution.
     * 
     * @throws SQLException
     */
    public RepositoryClientImpl() throws SQLException{
        //setup DB connection
        dbCon = new DatabaseConnector(
                RepositoryProperties.getProperty(RepositoryProperties.DB_NAME),
                RepositoryProperties.getProperty(RepositoryProperties.DB_LOCATOR),
                RepositoryProperties.getProperty(RepositoryProperties.DB_USER),
                RepositoryProperties.getProperty(RepositoryProperties.DB_PASS)
            );
        dbCon.connect();

        //prepare queries
        getCollections = dbCon.con.prepareStatement(GET_COLLECTIONS_QUERY);
//        getDatasetsByCollection = dbCon.con.prepareStatement(GET_DATA_SETS_FOR_COLLECTION_QUERY);
        getDatasets = dbCon.con.prepareStatement(GET_DATA_SETS_QUERY);
        getDatasetByID = dbCon.con.prepareStatement(GET_DATA_SET_BY_ID_QUERY);
        getTasks = dbCon.con.prepareStatement(GET_TASKS_QUERY);
        getTasksForMeta = dbCon.con.prepareStatement(GET_TASKS_FOR_METADATA_QUERY);
        getTaskByID = dbCon.con.prepareStatement(GET_TASK_BY_ID_QUERY);
        getSubsetTrackListForDataset = dbCon.con.prepareStatement(GET_SUBSET_TRACKLIST_FOR_DATASET_QUERY);
        getExpTrackListsForDataset = dbCon.con.prepareStatement(GET_EXPERIMENT_TRACKLISTS_FOR_DATASET_QUERY);
        getTrackListTracks = dbCon.con.prepareStatement(GET_TRACKLIST_TRACKS_QUERY);
        getFileMetadata = dbCon.con.prepareStatement(GET_FILE_METADATA_QUERY);
        getTrackMetadata = dbCon.con.prepareStatement(GET_TRACK_METADATA_QUERY);
        getAllTrackMetadata = dbCon.con.prepareStatement(GET_ALL_TRACK_METADATA_QUERY);
        getTrackMetadataSpecific = dbCon.con.prepareStatement(GET_TRACK_METADATA_SPECIFIC_QUERY);
        getAllTrackMetadataSpecific = dbCon.con.prepareStatement(GET_ALL_TRACK_METADATA_SPECIFIC_QUERY);
        getTracklistFiles = dbCon.con.prepareStatement(GET_TRACKLIST_FILES);
        
        getAllTracks = dbCon.con.prepareStatement(GET_ALL_TRACKS);
        getFileMeta = dbCon.con.prepareStatement(GET_FILE_METADATA);
        
        insertPublishedResult = dbCon.con.prepareStatement(INSERT_PUBLISHED_RESULT);
        getPublishedResultsForDataset = dbCon.con.prepareStatement(GET_PUBLISHED_RESULTS_FOR_DATASET);
        getPublishedResultsForUsername = dbCon.con.prepareStatement(GET_PUBLISHED_RESULTS_FOR_USERNAME);
        deletePublishedResult = dbCon.con.prepareStatement(DELETE_PUBLISHED_RESULT);

        initTypesMaps();
    }

    public DatabaseConnector getDbCon() {
		return dbCon;
	}

	public void setDbCon(DatabaseConnector dbCon) {
		this.dbCon = dbCon;
	}

	/**
     * Closes the database connection. References to this Object should be cut so that it can be garbage
     * collected as it cannot be brought back into a working state after a call to this method.
     */
    public void close(){
        dbCon.close();
        dbCon = null;
    }

    public void setAutocommit(boolean val) throws SQLException{
        dbCon.con.setAutoCommit(val);
    }

    public void commit() throws SQLException{
        dbCon.con.commit();
    }

    public void initTypesMaps() throws SQLException{
        trackMetadataTypeMap = populateTypesMap(GET_TRACK_METADATA_TYPES);
        fileMetadataTypeMap = populateTypesMap(GET_FILE_METADATA_TYPES);
        trackListTypeMap = populateTypesMap(GET_TRACKLIST_TYPES);
        siteNameMap = populateTypesMap(GET_SITES);
        trackMetadataTypeMapRev = reverseTypesMap(trackMetadataTypeMap);
        fileMetadataTypeMapRev = reverseTypesMap(fileMetadataTypeMap);
        trackListTypeMapRev = reverseTypesMap(trackListTypeMap);
        siteNameMapRev = reverseTypesMap(siteNameMap);
    }

    public List<String> getAllTracks() throws SQLException{
        ResultSet rs = getAllTracks.executeQuery();
        ArrayList<String> tracks = new ArrayList<String>();
        while(rs.next()){
            tracks.add(rs.getString(1));
        }
        return tracks;
    }

    public Map<Integer,Map<String,Integer>> getFileMetadataValueIDs() throws SQLException{
        List<Map<String,String>> data = executePreparedStatement(getFileMeta);

        Map<Integer,Map<String,Integer>> out = new HashMap<Integer,Map<String,Integer>>();

        for (Iterator<Map<String, String>> it = data.iterator(); it.hasNext();){
            Map<String, String> map = it.next();
            int id = Integer.parseInt(map.get("id"));
            int type_id = Integer.parseInt(map.get("metadata_type_id"));
            String val = map.get("value");

            Map<String,Integer> valueToID = out.get(type_id);
            if (valueToID == null){
                valueToID = new HashMap<String, Integer>();
                out.put(type_id,valueToID);
            }
            valueToID.put(val, id);
        }
        return out;
    }



    public Map<String,Integer> getTrackMetadataNameMap(){
    	return Collections.unmodifiableMap(trackMetadataTypeMapRev);
    }
    
    public Map<String,Integer> getFileMetadataNameMap(){
    	return Collections.unmodifiableMap(fileMetadataTypeMapRev);
    }
    
    public Map<String,Integer> getTrackListTypeMap(){
    	return Collections.unmodifiableMap(trackListTypeMapRev);
    }
    
    public Map<String,Integer> getSiteNameMap(){
    	return Collections.unmodifiableMap(siteNameMapRev);
    }
    
    

    public String getTrackMetadataName(int typeId){
        return trackMetadataTypeMap.get(typeId);
    }

    public String getFileMetadataName(int typeId){
        return fileMetadataTypeMap.get(typeId);
    }

    public String getTrackListTypeName(int typeId){
        return trackListTypeMap.get(typeId);
    }

    public String getSiteName(int siteId){
        return siteNameMap.get(siteId);
    }

    public int getTrackMetadataID(String typeName){
    	Integer out = trackMetadataTypeMapRev.get(typeName);
        if (out == null){
        	return -1;
        }else{
        	return out;
        }
    }

    public int getFileMetadataID(String typeName){
        Integer out = fileMetadataTypeMapRev.get(typeName);
        if (out == null){
        	return -1;
        }else{
        	return out;
        }
    }

    public int getTrackListTypeID(String typeName){
    	Integer out = trackListTypeMapRev.get(typeName);
        if (out == null){
        	return -1;
        }else{
        	return out;
        }
    }

    public int getSiteId(String siteName){
    	Integer out = siteNameMapRev.get(siteName);
        if (out == null){
        	return -1;
        }else{
        	return out;
        }
    }



    public List<NemaCollection> getCollections() throws SQLException{
        List<Map<String, String>> results = executePreparedStatement(getCollections);
        return buildNEMACollections(results);
    }

    public List<NemaTask> getTasks() throws SQLException{
    	List<Map<String, String>> results = executePreparedStatement(getTasks);
        return buildNEMATask(results);
    }

    public List<NemaTask> getTasks(int id) throws SQLException{
    	List<Map<String, String>> results = executeStatement(getTasksForMeta,id);
    	return buildNEMATask(results);
    }
    
    public NemaTask getTask(int task_id) throws SQLException{
    	List<Map<String, String>> results = executeStatement(getTaskByID, task_id);
        if(results.size() > 0){
            return buildNEMATask(results.get(0));
        }else{
            return null;
        }
    }

    public List<NemaDataset> getDatasets() throws SQLException{
        List<Map<String, String>> results = executePreparedStatement(getDatasets);
        return buildNEMADataset(results);
    }

    public NemaDataset getDataset(int dataset_id) throws SQLException{
        List<Map<String, String>> results = executeStatement(getDatasetByID, dataset_id);
        if(results.size() > 0){
            return buildNEMADataset(results.get(0));
        }else{
            return null;
        }
    }

    public NemaTrackList getDatasetSubset(NemaDataset dataset) throws SQLException{
        return getDatasetSubset(dataset.getId());
    }

    public NemaTrackList getDatasetSubset(int datasetId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getSubsetTrackListForDataset, datasetId);
        if(results.size() > 0){
            return buildNEMATrackList(results.get(0));
        }else{
            return null;
        }
    }


    public List<List<NemaTrackList>> getExperimentTrackLists(NemaDataset dataset) throws SQLException{
        return getExperimentTrackLists(dataset.getId());
    }

    public List<List<NemaTrackList>> getExperimentTrackLists(int datasetID) throws SQLException{
        List<Map<String, String>> results = executeStatement(getExpTrackListsForDataset, datasetID);

        Map<Integer,List<NemaTrackList>> sortMap = new HashMap<Integer,List<NemaTrackList>>();
        List<NemaTrackList> setList = buildNEMATrackList(results);
        List<NemaTrackList> aSetList;
        for (Iterator<NemaTrackList> it = setList.iterator(); it.hasNext();){
            NemaTrackList nemaSet = it.next();
            aSetList = sortMap.get(nemaSet.getFoldNumber());
            if (aSetList == null){
                aSetList = new ArrayList<NemaTrackList>();
                sortMap.put(nemaSet.getFoldNumber(), aSetList);
            }
            aSetList.add(nemaSet);
        }
        List<Integer> splitNumbers = new ArrayList<Integer>(sortMap.keySet());
        Collections.sort(splitNumbers);
        List<List<NemaTrackList>> out = new ArrayList<List<NemaTrackList>>();
        for (Iterator<Integer> it = splitNumbers.iterator(); it.hasNext();){
            out.add(sortMap.get(it.next()));
        }
        return out;
    }


    public List<NemaTrack> getTracks(NemaTrackList set) throws SQLException{
        return getTracks(set.getId());
    }

    public List<NemaTrack> getTracks(int setId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getTrackListTracks, setId);
        return buildNEMATrack(results);
    }

    public List<String> getTrackIDs(NemaTrackList set) throws SQLException{
        return getTrackIDs(set.getId());
    }

    public List<String> getTrackIDs(int setId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getTrackListTracks, setId);
        List<String> ids = new ArrayList<String>(results.size());
        for (Iterator<Map<String, String>> it = results.iterator(); it.hasNext();){
            String id = it.next().get("track_id");
//            System.out.println(id);
            ids.add(id);
        }
        return ids;
    }


    public List<NemaMetadataEntry> getFileMetadata(NemaFile file) throws SQLException{
        return getFileMetadataByID(file.getId());
    }

    public List<NemaMetadataEntry> getFileMetadataByID(int fileId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getFileMetadata, fileId);
        return buildNEMAMetadataEntry(results,fileMetadataTypeMap);
    }


    public Map<Integer,List<NemaMetadataEntry>> getFileMetadata(List<NemaFile> files)
            throws SQLException{
        HashMap<Integer,List<NemaMetadataEntry>> out = new HashMap<Integer, List<NemaMetadataEntry>>();
        int id;
        for (Iterator<NemaFile> it = files.iterator(); it.hasNext();){
            id = it.next().getId();
            out.put(id, getFileMetadataByID(id));
        }
        return out;
    }

    public Map<Integer,List<NemaMetadataEntry>> getFileMetadataByID(List<Integer> fileIDs)
            throws SQLException{
        HashMap<Integer,List<NemaMetadataEntry>> out = new HashMap<Integer, List<NemaMetadataEntry>>();
        int id;
        for (Iterator<Integer> it = fileIDs.iterator(); it.hasNext();){
            id = it.next();
            out.put(id, getFileMetadataByID(id));
        }
        return out;
    }


    public List<NemaMetadataEntry> getTrackMetadata(NemaTrack track) throws SQLException{
        return getTrackMetadataByID(track.getId());
    }

    public List<NemaMetadataEntry> getTrackMetadataByID(String trackId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getTrackMetadata, trackId);
        return buildNEMAMetadataEntry(results,trackMetadataTypeMap);
    }


    public Map<String,List<NemaMetadataEntry>> getTrackMetadata(List<NemaTrack> trackList)
            throws SQLException{
        HashMap<String,List<NemaMetadataEntry>> out = new HashMap<String, List<NemaMetadataEntry>>();
        String id;
        for (Iterator<NemaTrack> it = trackList.iterator(); it.hasNext();){
            id = it.next().getId();
            out.put(id, getTrackMetadataByID(id));
        }
        return out;
    }

    public Map<String,List<NemaMetadataEntry>> getTrackMetadataByID(List<String> tracks)
            throws SQLException{
        HashMap<String,List<NemaMetadataEntry>> out = new HashMap<String, List<NemaMetadataEntry>>();
        Set<String> trackSet = new HashSet<String>(tracks);

        List<Map<String, String>> results = executePreparedStatement(getAllTrackMetadata);
        NemaMetadataEntry entry;
        List<NemaMetadataEntry> list;
        String track_id;
        for (Iterator<Map<String, String>> it = results.iterator(); it.hasNext();){
            Map<String, String> map = it.next();
            track_id = map.get("track_id");
            if (trackSet.contains(track_id)){
                entry = buildNEMAMetadataEntry(map, trackMetadataTypeMap);
                list = out.get(track_id);
                if (list == null){
                    list = new ArrayList<NemaMetadataEntry>(5);
                    out.put(track_id,list);
                }
                list.add(entry);
            }
        }
        return out;
    }


    public List<NemaMetadataEntry> getTrackMetadataByID(String trackId, int metadataId) throws SQLException{
        getTrackMetadataSpecific.setString(1, trackId);
        getTrackMetadataSpecific.setInt(2, metadataId);
        List<Map<String, String>> results = executePreparedStatement(getTrackMetadataSpecific);
        if (results.size() > 0){
        	List<NemaMetadataEntry> out = new ArrayList<NemaMetadataEntry>(results.size());
            for(Iterator<Map<String,String>> it = results.iterator(); it.hasNext();){
            	out.add(buildNEMAMetadataEntry(it.next(), trackMetadataTypeMap));
            }
        	return out;
        }else{
            return null;
        }
    }

    public List<NemaMetadataEntry> getTrackMetadata(NemaTrack track, int metadataId) throws SQLException{
        return getTrackMetadataByID(track.getId(),metadataId);
    }

    public Map<String,List<NemaMetadataEntry>> getTrackMetadataByID(List<String> tracks, int metadataId) throws SQLException{
        HashMap<String,List<NemaMetadataEntry>> out = new HashMap<String, List<NemaMetadataEntry>>();
        Set<String> trackSet = new HashSet<String>(tracks);

        getAllTrackMetadataSpecific.setInt(1, metadataId);
        List<Map<String, String>> results = executePreparedStatement(getAllTrackMetadataSpecific);
        String trackId;
        List<NemaMetadataEntry> tmp;
        for (Iterator<Map<String, String>> it = results.iterator(); it.hasNext();){
            Map<String, String> map = it.next();
            trackId = map.get("track_id");
            if (trackSet.contains(trackId)){
            	tmp = out.get(trackId);
            	if(tmp == null){
            		tmp = new ArrayList<NemaMetadataEntry>(5);
            		out.put(trackId, tmp);
            	}
                tmp.add(buildNEMAMetadataEntry(map, trackMetadataTypeMap));
            }
        }
        return out;
    }

    public Map<String,List<NemaMetadataEntry>> getTrackMetadata(List<NemaTrack> tracks, int metadataId) throws SQLException{
        HashMap<String,List<NemaMetadataEntry>> out = new HashMap<String, List<NemaMetadataEntry>>();
        String id;
        for (Iterator<NemaTrack> it = tracks.iterator(); it.hasNext();){
            id = it.next().getId();
            out.put(id, getTrackMetadataByID(id, metadataId));
        }
        return out;
    }

    private List<Map<String, String>> getFileDataForTrackList(int trackListId, Set<NemaMetadataEntry> constraint) throws SQLException{
        String query = GET_CONSTRAINED_FILES;
        NemaMetadataEntry nemaMetadataEntry;
        if(constraint != null){
	        for (Iterator<NemaMetadataEntry> it = constraint.iterator(); it.hasNext();){
	            nemaMetadataEntry = it.next();
	            query += "AND EXISTS (SELECT file_id FROM file_file_metadata_link WHERE file.id=file_file_metadata_link.file_id AND file_file_metadata_link.file_metadata_id=(SELECT id FROM file_metadata WHERE metadata_type_id=" + fileMetadataTypeMapRev.get(nemaMetadataEntry.getType()) + " AND value='" + nemaMetadataEntry.getValue() + "')) ";
	            if (it.hasNext()){
	                query += "\n";
	            }
	        }
        }
        query += ")";
//        System.out.println("Executing constructed query: " + query);
        PreparedStatement st = dbCon.con.prepareStatement(query);
        List<Map<String, String>> results = executePreparedStatement(st);

        return results;
    }

    private List<Map<String, String>> getFileData(Set<NemaMetadataEntry> constraint) throws SQLException{
        String query = GET_CONSTRAINED_FILES;
        NemaMetadataEntry nemaMetadataEntry;
        if(constraint != null){
	        for (Iterator<NemaMetadataEntry> it = constraint.iterator(); it.hasNext();){
	            nemaMetadataEntry = it.next();
	            query += "AND EXISTS (SELECT file_id FROM file_file_metadata_link WHERE file.id=file_file_metadata_link.file_id AND file_file_metadata_link.file_metadata_id=(SELECT id FROM file_metadata WHERE metadata_type_id=" + fileMetadataTypeMapRev.get(nemaMetadataEntry.getType()) + " AND value='" + nemaMetadataEntry.getValue() + "')) ";
	            if (it.hasNext()){
	                query += "\n";
	            }
	        }
        }
        query += ")";
//        System.out.println("Executing constructed query: " + query);
        PreparedStatement st = dbCon.con.prepareStatement(query);
        List<Map<String, String>> results = executePreparedStatement(st);

        return results;
    }

    private List<Map<String, String>> getFileData(Set<NemaMetadataEntry> constraint,
                                                  String trackId) throws SQLException{
        String query = GET_CONSTRAINED_FILE_FOR_TRACK;
        NemaMetadataEntry nemaMetadataEntry;
        for (Iterator<NemaMetadataEntry> it = constraint.iterator(); it.hasNext();){
            nemaMetadataEntry = it.next();
            query += "AND EXISTS (SELECT file_id FROM file_file_metadata_link WHERE file.id=file_file_metadata_link.file_id AND file_file_metadata_link.file_metadata_id=(SELECT id FROM file_metadata WHERE metadata_type_id=" + fileMetadataTypeMapRev.get(nemaMetadataEntry.getType()) + " AND value='" + nemaMetadataEntry.getValue() + "')) ";
            if (it.hasNext()){
                query += "\n";
            }
        }
        query += ")";
        System.out.println("Executing constructed query: " + query);
        PreparedStatement st = dbCon.con.prepareStatement(query);
        List<Map<String, String>> results = executeStatement(st, trackId);

        return results;
    }
    
    
    
    public NemaFile getFile(NemaTrack track,
                            Set<NemaMetadataEntry> constraint) throws SQLException{
        return getFile(track.getId(), constraint);
    }

    public NemaFile getFile(String trackId,
                            Set<NemaMetadataEntry> constraint) throws SQLException{
        List<Map<String, String>> results = getFileData(constraint, trackId);

        if(results.size() > 0){
            return buildNEMAFile(results.get(0));
        }else{
            return null;
        }
    }

    public Map<NemaFile, List<NemaMetadataEntry>> getFileFuzzy(NemaTrack track,
                                                              Set<NemaMetadataEntry> constraint)
            throws SQLException{
        return getFileFuzzy(track.getId(), constraint);
    }

    public Map<NemaFile, List<NemaMetadataEntry>> getFileFuzzy(String trackId,
                                                              Set<NemaMetadataEntry> constraint)
            throws SQLException{
        List<Map<String, String>> results = getFileData(constraint, trackId);

        List<NemaFile> files = buildNEMAFile(results);
        Map<NemaFile, List<NemaMetadataEntry>> out = new HashMap<NemaFile, List<NemaMetadataEntry>>(files.size());
        for (Iterator<NemaFile> it = files.iterator(); it.hasNext();){
            NemaFile nemaFile = it.next();
            out.put(nemaFile, getFileMetadata(nemaFile));
        }
        return out;
    }

    public List<NemaFile> getFiles(int trackListId) throws SQLException{
    	getTracklistFiles.setInt(1, trackListId);
    	List<Map<String,String>> results = executePreparedStatement(getTracklistFiles);
    	return buildNEMAFile(results);
    }
    
    public List<NemaFile> getFiles(int trackListId,
            Set<NemaMetadataEntry> constraint) throws SQLException{
    	String query = GET_TRACKLIST_FILES;
        NemaMetadataEntry nemaMetadataEntry;
        if(constraint != null){
	        for (Iterator<NemaMetadataEntry> it = constraint.iterator(); it.hasNext();){
	            nemaMetadataEntry = it.next();
	            query += " AND EXISTS (SELECT * FROM file_file_metadata_link WHERE file.id=file_file_metadata_link.file_id AND file_file_metadata_link.file_metadata_id=(SELECT id FROM file_metadata WHERE metadata_type_id=" + fileMetadataTypeMapRev.get(nemaMetadataEntry.getType()) + " AND value='" + nemaMetadataEntry.getValue() + "'))";
	            if (it.hasNext()){
	                query += "\n";
	            }
	        }
        }
        System.out.println("Executing constructed query: " + query);
        PreparedStatement st = dbCon.con.prepareStatement(query);
        st.setInt(1, trackListId);
        List<Map<String, String>> results = executePreparedStatement(st);
    	return buildNEMAFile(results);
    }
    
    

    public List<NemaFile> getFiles(List<NemaTrack> trackList,
                                   Set<NemaMetadataEntry> constraint) throws SQLException{
        List<String> trackIDList = new ArrayList<String>(trackList.size());
        for (Iterator<NemaTrack> it = trackList.iterator(); it.hasNext();){
            trackIDList.add(it.next().getId());
        }
        
        return getFilesByID(trackIDList, constraint);
    }

    public List<NemaFile> getFilesByID(List<String> trackIDList, Set<NemaMetadataEntry> constraint) throws SQLException{
        logger.info("Resolving files for " + trackIDList.size() + " tracks");
        Set<String> trackSet = new HashSet<String>();
        trackSet.addAll(trackIDList);
        logger.info("tracks in set:");

        Map<String,NemaFile> fileMap = new HashMap<String, NemaFile>(trackIDList.size());
        List<Map<String, String>> data = getFileData(constraint);
        logger.info("Query returned data on " + data.size() + " files, filtering");
        Map<String, String> map;
        String trackID;
        for (Iterator<Map<String, String>> it = data.iterator(); it.hasNext();){
            map = it.next();
            trackID = map.get("track_id");

            if(trackSet.contains(trackID)){
                fileMap.put(trackID, buildNEMAFile(map));
            }
        }

        logger.info("mapped " + fileMap.size() + " files to tracks");

        List<NemaFile> out = new ArrayList<NemaFile>();
        for (Iterator<String> it = trackIDList.iterator(); it.hasNext();){
            out.add(fileMap.get(it.next()));
        }

        logger.info("returning file list length: " + out.size());

        return out;
    }

    public List<NemaData> resolveTracksToFiles(List<NemaData> trackDataList, Set<NemaMetadataEntry> constraint) throws SQLException, IllegalArgumentException{
    	logger.info("Resolving files for " + trackDataList.size() + " tracks");
        
    	Map<String,NemaData> trackMap = new HashMap<String,NemaData>(trackDataList.size());
        NemaData tmp;
        for (Iterator<NemaData> it = trackDataList.iterator(); it.hasNext();) {
			tmp = it.next();
        	trackMap.put(tmp.getId(),tmp);
		}
        int setSize = trackMap.size();
        logger.info("tracks in set: " + setSize);
        List<Map<String, String>> data = getFileData(constraint);
        logger.info("Query returned data on " + data.size() + " files, filtering");
        Map<String, String> map;
        String trackID;
        int done = 0;
        for (Iterator<Map<String, String>> it = data.iterator(); it.hasNext() && done < setSize;){
            map = it.next();
            trackID = map.get("track_id");
            tmp = trackMap.get(trackID);
            if(tmp != null){
            	tmp.setMetadata(NemaDataConstants.PROP_FILE_LOCATION, map.get("path"));
                done++;
            }
        }

        logger.info("mapped " + done + " / " + setSize + " files to tracks");

        return trackDataList;
    }

    public Map<NemaTrackList,List<NemaData>> resolveTracksToFiles(Map<NemaTrackList,List<NemaData>> trackDataMap, Set<NemaMetadataEntry> constraint) throws SQLException, IllegalArgumentException{
    	logger.info("Resolving files for " + trackDataMap.size() + " lists of tracks");
//        Map<String,NemaData> trackMap = new HashMap<String,NemaData>();
//        NemaData tmp;
//        for (Iterator<NemaTrackList> trackListIt = trackDataMap.keySet().iterator(); trackListIt
//				.hasNext();) {
//			NemaTrackList trackList = trackListIt.next();
//			List<NemaData> list = trackDataMap.get(trackList);
//			logger.info("\ttrack list '" + trackList.getId() + "' has " + list.size() + " tracks");
//			String trackStr = "";
//			for (Iterator<NemaData> nemaDataIt = list.iterator(); nemaDataIt.hasNext();) {
//				tmp = nemaDataIt.next();
//				trackStr += "\t\t" + tmp.getId() + "\n";
//	        	trackMap.put(tmp.getId(),tmp);
//			}
//			logger.info(trackStr);
//		}
//        
//        int setSize = trackMap.size();
//        logger.info("total num tracks in set: " + setSize);
//        List<Map<String, String>> data = getFileData(constraint);
//        logger.info("Query returned data on " + data.size() + " files, filtering");
//        Map<String, String> map;
//        String trackID;
//        int done = 0;
//        for (Iterator<Map<String, String>> fileDataIt = data.iterator(); fileDataIt.hasNext() && done < setSize;){
//            map = fileDataIt.next();
//            trackID = map.get("track_id");
//            tmp = trackMap.get(trackID);
//            if(tmp != null){
//            	tmp.setMetadata(NemaDataConstants.PROP_FILE_LOCATION, map.get("path"));
//                done++;
//            }
//        }
//
//        logger.info("mapped " + done + " / " + setSize + " files to tracks");
    	int resolved = 0;
    	int failed = 0;
    	for (Iterator<NemaTrackList> trackListIt = trackDataMap.keySet().iterator(); trackListIt.hasNext();) {
			NemaTrackList trackList = trackListIt.next();
			List<NemaData> list = trackDataMap.get(trackList);
			logger.info("\ttrack list '" + trackList.getId() + "' has " + list.size() + " tracks");
			List<NemaFile> files = getFiles(trackList.getId(),constraint);
			Map<String, NemaFile> filesMap = new HashMap<String, NemaFile>(files.size());
			for (Iterator<NemaFile> iterator = files.iterator(); iterator.hasNext();) {
				NemaFile file = iterator.next();
				filesMap.put(file.getTrackId(), file);
			}
			for (Iterator<NemaData> iterator = list.iterator(); iterator.hasNext();) {
				NemaData data = iterator.next();
				NemaFile file = filesMap.get(data.getId());
				if(file == null){
					logger.warning("Failed to resolve track ID: " + data.getId() + " in the requested format");
					failed++;
				}else{
					data.setMetadata(NemaDataConstants.PROP_FILE_LOCATION, file.getPath());
					resolved++;
				}
				
			}
    	}
    	logger.info("Finished resolving files for " + trackDataMap.size() + 
    			" lists of tracks, " + resolved + " tracks resolved, " + failed 
    			+ " failed to resolve"); 
        return trackDataMap;
    }
    

    public List<List<NemaMetadataEntry>> getCollectionVersions(NemaCollection collection)
            throws SQLException{
        return getCollectionVersions(collection.getId());
    }

    public List<List<NemaMetadataEntry>> getCollectionVersions(int collectionId)
            throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    }
    //this is not right it will return a row for ANY metadata rows for the collection
    //SELECT DISTINCT * FROM file_metadata WHERE id IN (SELECT file_metadata_id FROM file_file_metadata_link WHERE file_id IN (SELECT id FROM file WHERE track_id IN (SELECT id FROM track WHERE collection_id=?)))

    public List<List<NemaMetadataEntry>> getTrackListVersions(NemaTrackList trackList) throws SQLException{
        return getTrackListVersions(trackList.getId());
    }

    public List<List<NemaMetadataEntry>> getTrackListVersions(int trackListId) throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    }



    private List<Map<String, String>> executeStatement(PreparedStatement query, int id) throws SQLException {
        query.setInt(1, id);
        return executePreparedStatement(query);
    }

    private List<Map<String, String>> executeStatement(PreparedStatement query, String id) throws SQLException {
        query.setString(1, id);
        return executePreparedStatement(query);
    }

//    private List<Map<String, String>> executeStatement(PreparedStatement query, int []ids) throws SQLException {
//        if (ids != null) {
//            for(int i = 0; i < ids.length; i++) {
//                query.setInt(i + 1, ids[i]);
//            }
//        }
//        return executePreparedStatement(query);
//    }

    private List<Map<String, String>> executePreparedStatement(PreparedStatement st) throws SQLException {
        List<Map<String, String>> retVal = new ArrayList<Map<String,String>>();
        ResultSet rs = null;
        try {
            rs = st.executeQuery();
            int idx = 0;
            while (rs.next()) {
                ResultSetMetaData rsm = rs.getMetaData();
                Map<String, String> map = new HashMap<String, String>(rsm.getColumnCount());
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    map.put(rsm.getColumnLabel(i), rs.getString(i));
                }
                retVal.add(map);
                idx++;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return retVal;
    }

    private NemaCollection buildNEMACollection(Map<String, String> map){
        return new NemaCollection(
                Integer.valueOf(map.get("id")),
                map.get("name"),
                map.get("description")
            );
    }
    private List<NemaCollection> buildNEMACollections(List<Map<String, String>> maps){
        List<NemaCollection> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NemaCollection>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMACollection(data));
            }
        }
        return retVal;
    }

    private NemaDataset buildNEMADataset(Map<String, String> map){
        int subject_meta_type = Integer.parseInt(map.get("subject_track_metadata_type_id"));
        int filter_meta_type = Integer.parseInt(map.get("filter_track_metadata_type_id"));
        return new NemaDataset(
                Integer.valueOf(map.get("id")),
                map.get("name"),
                map.get("description"),
                Integer.valueOf(map.get("subset_set_id")),
                Integer.valueOf(map.get("num_splits")),
                Integer.valueOf(map.get("num_set_per_split")),
                map.get("split_class"),
                map.get("split_parameters_string"),
                subject_meta_type,
                trackMetadataTypeMap.get(subject_meta_type),
                filter_meta_type,
                trackMetadataTypeMap.get(filter_meta_type)
            );
    }
    private List<NemaDataset> buildNEMADataset(List<Map<String, String>> maps){
        List<NemaDataset> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NemaDataset>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMADataset(data));
            }
        }
        return retVal;
    }
    
    private NemaTask buildNEMATask(Map<String, String> map){
        int subject_meta_type = Integer.parseInt(map.get("subject_track_metadata"));
        return new NemaTask(
                Integer.valueOf(map.get("id")),
                map.get("name"),
                map.get("description"),
                subject_meta_type,
                trackMetadataTypeMap.get(subject_meta_type),
                Integer.valueOf(map.get("dataset_id"))
            );
    }
    private List<NemaTask> buildNEMATask(List<Map<String, String>> maps){
        List<NemaTask> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NemaTask>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMATask(data));
            }
        }
        return retVal;
    }


    private NemaTrackList buildNEMATrackList(Map<String, String> map){
        int trackList_type = Integer.valueOf(map.get("set_type_id"));

        return new NemaTrackList(
                Integer.valueOf(map.get("id")),
                Integer.valueOf(map.get("dataset_id")),
                trackList_type,
                trackListTypeMap.get(trackList_type),
                Integer.valueOf(map.get("split_number"))
            );
    }
    private List<NemaTrackList> buildNEMATrackList(List<Map<String, String>> maps){
        List<NemaTrackList> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NemaTrackList>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMATrackList(data));
            }
        }
        return retVal;
    }


    private NemaTrack buildNEMATrack(Map<String, String> map){
        return new NemaTrack(
                map.get("track_id")
            );
    }
    private List<NemaTrack> buildNEMATrack(List<Map<String, String>> maps){
        List<NemaTrack> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NemaTrack>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMATrack(data));
            }
        }
        return retVal;
    }


    private NemaFile buildNEMAFile(Map<String, String> map){
        return new NemaFile(
                Integer.parseInt(map.get("id")),
                map.get("track_id"),
                map.get("path"),
                map.get("site_name")
            );
    }
    private List<NemaFile> buildNEMAFile(List<Map<String, String>> maps){
        List<NemaFile> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NemaFile>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMAFile(data));
            }
        }
        return retVal;
    }


    private NemaMetadataEntry buildNEMAMetadataEntry(Map<String, String> map, Map<Integer,String> typesMap){
        int type = Integer.parseInt(map.get("metadata_type_id"));
        return new NemaMetadataEntry(
            typesMap.get(type),
            map.get("value")
        );
    }
    private List<NemaMetadataEntry> buildNEMAMetadataEntry(List<Map<String, String>> maps, Map<Integer,String> typesMap){
        List<NemaMetadataEntry> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NemaMetadataEntry>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMAMetadataEntry(data,typesMap));
            }
        }
        return retVal;
    }


    public Map<Integer,String> populateTypesMap(String query) throws SQLException{
        //System.out.println("Populating types map with query: " + query);
        PreparedStatement st = dbCon.con.prepareStatement(query);
        Map<Integer,String> retVal = new HashMap<Integer,String>();
        ResultSet rs = null;
        int count=0;
        try {
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                //System.out.println(id + ": " + name);
                retVal.put(id, name);
                count++;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        logger.info("cached "+ count + " objects with query "+ query);
        return retVal;
    }

    public Map<String,Integer> reverseTypesMap(Map<Integer,String> map) throws SQLException{
        Map<String,Integer> rev = new HashMap<String, Integer>();
        int key;
        String val;
        for (Iterator<Integer> it = map.keySet().iterator(); it.hasNext();){
            key = it.next();
            val = map.get(key);
            rev.put(val,key);
        }
        return rev;
    }

    public void publishResultForDataset(int dataset_id, String username, String systemName,
                                        String result_path) throws SQLException{
        insertPublishedResult.setInt(1,dataset_id);
        insertPublishedResult.setString(2, username);
        insertPublishedResult.setString(3, systemName);
        insertPublishedResult.setString(4, result_path);
        insertPublishedResult.executeUpdate();
    }

    public List<NemaPublishedResult> getPublishedResultsForDataset(int dataset_id)
            throws SQLException{
        getPublishedResultsForDataset.setInt(1,dataset_id);
        ResultSet rs = getPublishedResultsForDataset.executeQuery();
        List<NemaPublishedResult> out = new ArrayList<NemaPublishedResult>();
        while(rs.next()){
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String name = rs.getString("system_name");
            String path = rs.getString("result_path");
            Timestamp time = rs.getTimestamp("last_updated");
            out.add(new NemaPublishedResult(id, username, name, path, time));
        }
        return out;
    }

    public List<NemaPublishedResult> getPublishedResultsForDataset(String username)
            throws SQLException{
        getPublishedResultsForUsername.setString(1,username);
        ResultSet rs = getPublishedResultsForUsername.executeQuery();
        List<NemaPublishedResult> out = new ArrayList<NemaPublishedResult>();
        while(rs.next()){
            int id = rs.getInt("id");
            String username2 = rs.getString("username");
            String name = rs.getString("system_name");
            String path = rs.getString("result_path");
            Timestamp time = rs.getTimestamp("last_updated");
            out.add(new NemaPublishedResult(id, username2, name, path, time));
        }
        return out;
    }

    public void deletePublishedResult(int result_id) throws SQLException{
        deletePublishedResult.setInt(1, result_id);
        deletePublishedResult.execute();
    }

    public void deletePublishedResult(NemaPublishedResult result) throws SQLException{
        deletePublishedResult(result.getId());
    }

    public List<NemaPublishedResult> getPublishedResultsForUsername(String username)
            throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
