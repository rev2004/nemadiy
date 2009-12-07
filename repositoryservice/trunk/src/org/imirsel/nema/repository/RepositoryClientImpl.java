/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author kriswest
 */
public class RepositoryClientImpl implements RepositoryClientInterface{

    public static final String GET_COLLECTIONS_QUERY = "SELECT * FROM collection";
    private PreparedStatement getCollections;

    public static final String GET_TASKS_QUERY = "SELECT * FROM task";
    private PreparedStatement getTasks;

    public static final String GET_DATA_SETS_FOR_COLLECTION_QUERY = "SELECT * FROM dataset WHERE collection_id=?";
    private PreparedStatement getDatasetsByCollection;

    public static final String GET_DATA_SETS_FOR_TASK_QUERY = "SELECT * FROM dataset WHERE task_id=?";
    private PreparedStatement getDatasetsByTask;

    public static final String GET_COLLECTION_SUBSET_FOR_DATASET_QUERY = "SELECT set.* FROM set,dataset WHERE dataset.id=? AND set.id=dataset.subset_set_id";
    private PreparedStatement getSubsetForDataset;

    public static final String GET_EXPERIMENT_SETS_FOR_DATASET_QUERY = "SELECT set.* FROM set,dataset WHERE set.dataset_id=? AND dataset.id=set.dataset_id AND set.id!=dataset.subset_set_id";
    private PreparedStatement getExpSetsForDataset;

    public static final String GET_SET_TRACKS_QUERY = "SELECT track_id FROM set_track_link where set_id=?";
    private PreparedStatement getSetTracks;

    public static final String GET_FILE_METADATA_QUERY = "SELECT * FROM file_metadata where file_id=?";
    private PreparedStatement getFileMetadata;

    public static final String GET_TRACK_METADATA_QUERY = "SELECT * FROM track_metadata where track_id=?";
    private PreparedStatement getTrackMetadata;

    public static final String GET_FILE_FOR_TRACK = "SELECT file.* FROM file where file.track_id=?";
    public static final String GET_CONSTRAINED_FILE_FOR_TRACK = "SELECT file.* from file WHERE file.id IN (SELECT file_id from file,file_file_metadata_link WHERE file.track_id=? AND file.id=file_file_metadata_link.file_id AND file_metadata_id ALL (SELECT id FROM file_metadata WHERE ";



    //public static final String GET_VERSIONS_FOR_COLLECTION =



    //cached types maps
    public static final String GET_TRACK_METADATA_TYPES = "SELECT * FROM track_metadata_definitions";
    public static final String GET_FILE_METADATA_TYPES = "SELECT * FROM file_metadata_definitions";
    public static final String GET_TASK_TYPES = "SELECT * FROM task_type";
    public static final String GET_SET_TYPES = "SELECT * set_type_definitions";
    private Map<Integer,String> trackMetadataTypeMap;
    private Map<Integer,String> fileMetadataTypeMap;
    private Map<String,Integer> trackMetadataTypeMapRev;
    private Map<String,Integer> fileMetadataTypeMapRev;
    private Map<Integer,String> taskTypeMap;
    private Map<Integer,String> setTypeMap;


    DatabaseConnector dbCon;


    public RepositoryClientImpl() throws SQLException{
        //setup DB connection
        dbCon = new DatabaseConnector(
                RepositoryProperties.getProperty(RepositoryProperties.DB_NAME),
                RepositoryProperties.getProperty(RepositoryProperties.DB_LOCATOR),
                RepositoryProperties.getProperty(RepositoryProperties.DB_NAME),
                RepositoryProperties.getProperty(RepositoryProperties.DB_PASS)
            );
        dbCon.connect();

        //prepare queries
        getCollections = dbCon.con.prepareStatement(GET_COLLECTIONS_QUERY);
        getTasks = dbCon.con.prepareStatement(GET_TASKS_QUERY);
        getDatasetsByCollection = dbCon.con.prepareStatement(GET_DATA_SETS_FOR_COLLECTION_QUERY);
        getDatasetsByTask = dbCon.con.prepareStatement(GET_DATA_SETS_FOR_TASK_QUERY);
        getSubsetForDataset = dbCon.con.prepareStatement(GET_COLLECTION_SUBSET_FOR_DATASET_QUERY);
        getExpSetsForDataset = dbCon.con.prepareStatement(GET_EXPERIMENT_SETS_FOR_DATASET_QUERY);
        getSetTracks = dbCon.con.prepareStatement(GET_SET_TRACKS_QUERY);
        getFileMetadata = dbCon.con.prepareStatement(GET_FILE_METADATA_QUERY);
        getTrackMetadata = dbCon.con.prepareStatement(GET_TRACK_METADATA_QUERY);


        //init types maps
        trackMetadataTypeMap = populateTypesMap(GET_TRACK_METADATA_TYPES);
        fileMetadataTypeMap = populateTypesMap(GET_FILE_METADATA_TYPES);
        trackMetadataTypeMapRev = reverseTypesMap(trackMetadataTypeMap);
        fileMetadataTypeMapRev = reverseTypesMap(fileMetadataTypeMap);
        taskTypeMap = populateTypesMap(GET_TASK_TYPES);
        setTypeMap = populateTypesMap(GET_SET_TYPES);
    }

    public void close(){
        dbCon.close();
        dbCon = null;
    }


    public List<NEMACollection> getCollections() throws SQLException{
        List<Map<String, String>> results = executePreparedStatement(getCollections);
        return buildNEMACollections(results);
    }


    public List<NEMATask> getTasks() throws SQLException{
        List<Map<String, String>> results = executePreparedStatement(getDatasetsByTask);
        return buildNEMATask(results);
    }


    public List<NEMADataset> getDatasetsForCollection(NEMACollection collection) throws SQLException{
        return getDatasetsForCollection(collection.getId());
    }

    public List<NEMADataset> getDatasetsForCollection(int collectionId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getDatasetsByCollection, collectionId);
        return buildNEMADataset(results);
    }


    public List<NEMADataset> getDatasetsForTask(NEMATask task) throws SQLException{
        return getDatasetsForTask(task.getId());
    }

    public List<NEMADataset> getDatasetsForTask(int taskId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getDatasetsByTask, taskId);
        return buildNEMADataset(results);
    }


    public NEMASet getCollectionSubset(NEMADataset dataset) throws SQLException{
        return getCollectionSubset(dataset.getId());
    }

    public NEMASet getCollectionSubset(int datasetId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getSubsetForDataset, datasetId);
        if(results.size() > 0){
            return buildNEMASet(results.get(0));
        }else{
            return null;
        }
    }


    public List<List<NEMASet>> getExperimentSets(NEMADataset dataset) throws SQLException{
        return getExperimentSets(dataset.getId());
    }

    public List<List<NEMASet>> getExperimentSets(int datasetID) throws SQLException{
        List<Map<String, String>> results = executeStatement(getExpSetsForDataset, datasetID);

        Map<Integer,List<NEMASet>> sortMap = new HashMap<Integer,List<NEMASet>>();
        List<NEMASet> setList = buildNEMASet(results);
        List<NEMASet> aSetList;
        for (Iterator<NEMASet> it = setList.iterator(); it.hasNext();){
            NEMASet nemaSet = it.next();
            aSetList = sortMap.get(nemaSet.getSplitNumber());
            if (aSetList == null){
                aSetList = new ArrayList<NEMASet>();
                sortMap.put(nemaSet.getSplitNumber(), aSetList);
            }
            aSetList.add(nemaSet);
        }
        List<Integer> splitNumbers = new ArrayList<Integer>(sortMap.keySet());
        Collections.sort(splitNumbers);
        List<List<NEMASet>> out = new ArrayList<List<NEMASet>>();
        for (Iterator<Integer> it = splitNumbers.iterator(); it.hasNext();){
            out.add(sortMap.get(it.next()));
        }
        return out;
    }


    public List<NEMATrack> getTracks(NEMASet set) throws SQLException{
        return getTracks(set.getId());
    }

    public List<NEMATrack> getTracks(int setId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getSetTracks, setId);
        return buildNEMATrack(results);
    }


    public List<NEMAMetadataEntry> getFileMetadata(NEMAFile file) throws SQLException{
        return getFileMetadataByID(file.getId());
    }

    public List<NEMAMetadataEntry> getFileMetadataByID(int fileId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getFileMetadata, fileId);
        return buildNEMAMetadataEntry(results,fileMetadataTypeMap);
    }


    public Map<Integer,List<NEMAMetadataEntry>> getFileMetadata(List<NEMAFile> files)
            throws SQLException{
        HashMap<Integer,List<NEMAMetadataEntry>> out = new HashMap<Integer, List<NEMAMetadataEntry>>();
        int id;
        for (Iterator<NEMAFile> it = files.iterator(); it.hasNext();){
            id = it.next().getId();
            out.put(id, getFileMetadataByID(id));
        }
        return out;
    }

    public Map<Integer,List<NEMAMetadataEntry>> getFileMetadataByID(List<Integer> fileIDs)
            throws SQLException{
        HashMap<Integer,List<NEMAMetadataEntry>> out = new HashMap<Integer, List<NEMAMetadataEntry>>();
        int id;
        for (Iterator<Integer> it = fileIDs.iterator(); it.hasNext();){
            id = it.next();
            out.put(id, getFileMetadataByID(id));
        }
        return out;
    }


    public List<NEMAMetadataEntry> getTrackMetadata(NEMATrack track) throws SQLException{
        return getTrackMetadataByID(track.getId());
    }

    public List<NEMAMetadataEntry> getTrackMetadataByID(String trackId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getTrackMetadata, trackId);
        return buildNEMAMetadataEntry(results,trackMetadataTypeMap);
    }


    public Map<String,List<NEMAMetadataEntry>> getTrackMetadata(List<NEMATrack> trackList)
            throws SQLException{
        HashMap<String,List<NEMAMetadataEntry>> out = new HashMap<String, List<NEMAMetadataEntry>>();
        String id;
        for (Iterator<NEMATrack> it = trackList.iterator(); it.hasNext();){
            id = it.next().getId();
            out.put(id, getTrackMetadataByID(id));
        }
        return out;
    }

    public Map<String,List<NEMAMetadataEntry>> getTrackMetadataByID(List<String> tracks)
            throws SQLException{
        HashMap<String,List<NEMAMetadataEntry>> out = new HashMap<String, List<NEMAMetadataEntry>>();
        String id;
        for (Iterator<String> it = tracks.iterator(); it.hasNext();){
            id = it.next();
            out.put(id, getTrackMetadataByID(id));
        }
        return out;
    }



    private List<Map<String, String>> getFileData(Set<NEMAMetadataEntry> constraint,
                                                  String trackId) throws SQLException{
        String query = GET_FILE_FOR_TRACK;
        NEMAMetadataEntry nemaMetadataEntry;
        for (Iterator<NEMAMetadataEntry> it = constraint.iterator(); it.hasNext();){
            nemaMetadataEntry = it.next();
            query += "(metadata_type_id=" + fileMetadataTypeMapRev.get(nemaMetadataEntry.getType()) + " AND value='" + nemaMetadataEntry.getValue() + "')";
            if (it.hasNext()){
                query += " OR ";
            }
        }
        query += "))";
        PreparedStatement st = dbCon.con.prepareStatement(query);
        List<Map<String, String>> results = executeStatement(st, trackId);
        return results;
    }
    
    public NEMAFile getFile(NEMATrack track,
                            Set<NEMAMetadataEntry> constraint) throws SQLException{
        return getFile(track.getId(), constraint);
    }

    public NEMAFile getFile(String trackId,
                            Set<NEMAMetadataEntry> constraint) throws SQLException{
        List<Map<String, String>> results = getFileData(constraint, trackId);

        if(results.size() > 0){
            return buildNEMAFile(results.get(0));
        }else{
            return null;
        }
    }

    public Map<NEMAFile, List<NEMAMetadataEntry>> getFileFuzzy(NEMATrack track,
                                                              Set<NEMAMetadataEntry> constraint)
            throws SQLException{
        return getFileFuzzy(track.getId(), constraint);
    }

    public Map<NEMAFile, List<NEMAMetadataEntry>> getFileFuzzy(String trackId,
                                                              Set<NEMAMetadataEntry> constraint)
            throws SQLException{
        List<Map<String, String>> results = getFileData(constraint, trackId);

        List<NEMAFile> files = buildNEMAFile(results);
        Map<NEMAFile, List<NEMAMetadataEntry>> out = new HashMap<NEMAFile, List<NEMAMetadataEntry>>(files.size());
        for (Iterator<NEMAFile> it = files.iterator(); it.hasNext();){
            NEMAFile nemaFile = it.next();
            out.put(nemaFile, getFileMetadata(nemaFile));
        }
        return out;
    }


    public List<NEMAFile> getFiles(List<NEMATrack> trackList,
                                   Set<NEMAMetadataEntry> constraint) throws SQLException{
        List<NEMAFile> files = new ArrayList<NEMAFile>();
        for (Iterator<NEMATrack> it = trackList.iterator(); it.hasNext();){
            files.add(getFile(it.next().getId(), constraint));
        }
        return files;
    }

    public List<NEMAFile> getFilesByID(List<String> trackIDList, Set<NEMAMetadataEntry> constraint) throws SQLException{
        List<NEMAFile> files = new ArrayList<NEMAFile>();
        for (Iterator<String> it = trackIDList.iterator(); it.hasNext();){
            files.add(getFile(it.next(), constraint));
        }
        return files;
    }



    public List<List<NEMAMetadataEntry>> getCollectionVersions(NEMACollection collection)
            throws SQLException{
        return getCollectionVersions(collection.getId());
    }

    public List<List<NEMAMetadataEntry>> getCollectionVersions(int collectionId)
            throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    }
    //this is not right it will return a row for ANY metadata rows for the collection
    //SELECT DISTINCT * FROM file_metadata WHERE id IN (SELECT file_metadata_id FROM file_file_metadata_link WHERE file_id IN (SELECT id FROM file WHERE track_id IN (SELECT id FROM track WHERE collection_id=?)))

    public List<List<NEMAMetadataEntry>> getSetVersions(NEMASet set) throws SQLException{
        return getSetVersions(set.getId());
    }

    public List<List<NEMAMetadataEntry>> getSetVersions(int setId) throws SQLException{
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

    private List<Map<String, String>> executeStatement(PreparedStatement query, int []ids) throws SQLException {
        if (ids != null) {
            for(int i = 0; i < ids.length; i++) {
                query.setInt(i + 1, ids[i]);
            }
        }
        return executePreparedStatement(query);
    }

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

    private NEMACollection buildNEMACollection(Map<String, String> map){
        return new NEMACollection(
                Integer.valueOf(map.get("id")),
                map.get("name"),
                map.get("description")
            );
    }
    private List<NEMACollection> buildNEMACollections(List<Map<String, String>> maps){
        List<NEMACollection> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NEMACollection>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMACollection(data));
            }
        }
        return retVal;
    }


    private NEMATask buildNEMATask(Map<String, String> map){
        int meta_type = Integer.valueOf(map.get("subject_track_metadata_type_id"));
        int task_type = Integer.valueOf(map.get("task_type_id"));

        return new NEMATask(
                Integer.valueOf(map.get("id")),
                map.get("name"),
                map.get("description"),
                meta_type,
                trackMetadataTypeMap.get(meta_type),
                task_type,
                taskTypeMap.get(task_type)
            );
    }
    private List<NEMATask> buildNEMATask(List<Map<String, String>> maps){
        List<NEMATask> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NEMATask>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMATask(data));
            }
        }
        return retVal;
    }


    private NEMADataset buildNEMADataset(Map<String, String> map){
        int subject_meta_type = Integer.parseInt(map.get("subject_track_metadata_type_id"));
        int filter_meta_type = Integer.parseInt(map.get("filter_track_metadata_type_id"));
        int task = Integer.parseInt(map.get("task_id"));
        return new NEMADataset(
                Integer.valueOf(map.get("id")),
                map.get("name"),
                map.get("description"),
                Integer.valueOf(map.get("collection_id")),
                Integer.valueOf(map.get("subset_set_id")),
                Integer.valueOf(map.get("num_splits")),
                Integer.valueOf(map.get("num_set_per_split")),
                map.get("split_class"),
                map.get("split_parameters_string"),
                subject_meta_type,
                trackMetadataTypeMap.get(subject_meta_type),
                filter_meta_type,
                trackMetadataTypeMap.get(filter_meta_type),
                task,
                taskTypeMap.get(task)
            );
    }
    private List<NEMADataset> buildNEMADataset(List<Map<String, String>> maps){
        List<NEMADataset> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NEMADataset>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMADataset(data));
            }
        }
        return retVal;
    }


    private NEMASet buildNEMASet(Map<String, String> map){
        int set_type = Integer.valueOf(map.get("set_type_id"));

        return new NEMASet(
                Integer.valueOf(map.get("id")),
                Integer.valueOf(map.get("dataset_id")),
                set_type,
                setTypeMap.get(set_type),
                Integer.valueOf(map.get("split_number"))
            );
    }
    private List<NEMASet> buildNEMASet(List<Map<String, String>> maps){
        List<NEMASet> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NEMASet>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMASet(data));
            }
        }
        return retVal;
    }


    private NEMATrack buildNEMATrack(Map<String, String> map){
        return new NEMATrack(
                map.get("id")
            );
    }
    private List<NEMATrack> buildNEMATrack(List<Map<String, String>> maps){
        List<NEMATrack> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NEMATrack>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMATrack(data));
            }
        }
        return retVal;
    }


    private NEMAFile buildNEMAFile(Map<String, String> map){
        return new NEMAFile(
                Integer.parseInt(map.get("id")),
                map.get("track_id"),
                map.get("path")
            );
    }
    private List<NEMAFile> buildNEMAFile(List<Map<String, String>> maps){
        List<NEMAFile> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NEMAFile>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMAFile(data));
            }
        }
        return retVal;
    }


    private NEMAMetadataEntry buildNEMAMetadataEntry(Map<String, String> map, Map<Integer,String> typesMap){
        int type = Integer.parseInt(map.get("metadata_type_id"));
        return new NEMAMetadataEntry(
                typesMap.get(type),
                map.get("value")
            );
    }
    private List<NEMAMetadataEntry> buildNEMAMetadataEntry(List<Map<String, String>> maps, Map<Integer,String> typesMap){
        List<NEMAMetadataEntry> retVal = null;
        if(maps != null) {
            retVal = new ArrayList<NEMAMetadataEntry>(maps.size());
            for(Map<String, String> data : maps) {
                retVal.add(buildNEMAMetadataEntry(data,typesMap));
            }
        }
        return retVal;
    }


    private Map<Integer,String> populateTypesMap(String query) throws SQLException{
        PreparedStatement st = dbCon.con.prepareStatement(query);
        Map<Integer,String> retVal = new HashMap<Integer,String>();
        ResultSet rs = null;
        try {
            rs = st.executeQuery();
            while (rs.next()) {
                retVal.put(rs.getInt("id"), rs.getString("name"));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return retVal;
    }

    private Map<String,Integer> reverseTypesMap(Map<Integer,String> map) throws SQLException{
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
}
