/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.evaluation2.classification.ClassificationResultReadClass;

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

    public static final String GET_DATA_SETS_QUERY = "SELECT * FROM dataset";
    private PreparedStatement getDatasets;

    public static final String GET_DATA_SETS_FOR_TASK_QUERY = "SELECT * FROM dataset WHERE task_id=?";
    private PreparedStatement getDatasetsByTask;

    public static final String GET_DATA_SET_BY_ID_QUERY = "SELECT * FROM dataset WHERE id=?";
    private PreparedStatement getDatasetByID;

    public static final String GET_COLLECTION_SUBSET_FOR_DATASET_QUERY = "SELECT nemadatarepository.set.* FROM nemadatarepository.set,dataset WHERE dataset.id=? AND nemadatarepository.set.id=dataset.subset_set_id";
    private PreparedStatement getSubsetForDataset;

    public static final String GET_EXPERIMENT_SETS_FOR_DATASET_QUERY = "SELECT nemadatarepository.set.* FROM nemadatarepository.set,dataset WHERE set.dataset_id=? AND dataset.id=set.dataset_id AND nemadatarepository.set.id!=dataset.subset_set_id";
    private PreparedStatement getExpSetsForDataset;

    public static final String GET_SET_TRACKS_QUERY = "SELECT track_id FROM set_track_link where set_id=?";
    private PreparedStatement getSetTracks;

    public static final String GET_FILE_METADATA_QUERY = "SELECT * FROM file_metadata where file_id=?";
    private PreparedStatement getFileMetadata;

    public static final String GET_TRACK_METADATA_QUERY = "SELECT * FROM track_metadata where track_id=?";
    private PreparedStatement getTrackMetadata;

    public static final String GET_TRACK_METADATA_SPECIFIC_QUERY = "SELECT * FROM track_metadata where track_id=? AND metadata_type_id=?";
    private PreparedStatement getTrackMetadataSpecific;

//    public static final String GET_FILE_FOR_TRACK = "SELECT file.* FROM file WHERE file.track_id=? AND ";
    public static final String GET_CONSTRAINED_FILE_FOR_TRACK = "SELECT file.* from file WHERE file.id IN (SELECT file_id from file,file_file_metadata_link WHERE file.track_id=? AND file.id=file_file_metadata_link.file_id AND file_metadata_id ALL (SELECT id FROM file_metadata WHERE ";



    public static final String INSERT_TRACK = "INSERT IGNORE INTO track(id) VALUES(?)";
    private PreparedStatement insertTrack;

    public static final String INSERT_TRACK_COLLECTION_LINK = "INSERT IGNORE INTO collection_track_link(collection_id,track_id) VALUES(?,?)";
    private PreparedStatement insertTrackCollectionLink;

    public static final String INSERT_FILE = "INSERT INTO file(track_id,path) VALUES(?,?)";
    private PreparedStatement insertFile;

    public static final String INSERT_FILE_METADATA_DEFINITIONS = "INSERT IGNORE INTO file_metadata_definitions(name) VALUES(?)";
    private PreparedStatement insertFileMetaDef;

    public static final String INSERT_FILE_METADATA = "INSERT IGNORE INTO file_metadata(metadata_type_id,value) VALUES(?,?)";
    private PreparedStatement insertFileMeta;

    public static final String GET_FILE_METADATA = "SELECT * FROM file_metadata";
    private PreparedStatement getFileMeta;

    public static final String INSERT_FILE_METADATA_LINK = "INSERT INTO file_file_metadata_link(file_id,file_metadata_id) VALUES(?,?)";
    private PreparedStatement insertFileMetaLink;

    public static final String GET_ALL_TRACKS = "SELECT * FROM track";
    private PreparedStatement getAllTracks;




    public static final String INSERT_TRACK_METADATA_DEFINITIONS = "INSERT IGNORE INTO track_metadata_definitions(name) VALUES(?)";
    private PreparedStatement insertTrackMetaDef;

    public static final String INSERT_TRACK_METADATA = "INSERT INTO track_metadata(metadata_type_id,value) VALUES(?,?) ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)";
    private PreparedStatement insertTrackMeta;

    public static final String INSERT_TRACK_METADATA_LINK = "INSERT INTO track_track_metadata_link(track_id,track_metadata_id) VALUES(?,?)";
    private PreparedStatement insertTrackMetaLink;


    public static final String INSERT_DATASET = "INSERT INTO dataset(name,description,collection_id,subject_track_metadata_type_id,filter_track_metadata_type_id,task_id) values(?,?,?,?,?,?)";
    private PreparedStatement insertDataset;
    public static final String UPDATE_DATASET_WITH_SUBSET = "UPDATE dataset SET subset_set_id=? WHERE id=?";
    private PreparedStatement updateDatasetWithSubset;
    public static final String INSERT_SET = "INSERT INTO nemadatarepository.set(dataset_id, set_type_id, split_number) VALUES(?,?,?)";
    private PreparedStatement insertSet;
    public static final String INSERT_SET_TRACK_LINK = "INSERT INTO set_track_link(set_id,track_id) VALUES(?,?)";
    private PreparedStatement insertSetTrackLink;
    public static final String UPDATE_DATASET_WITH_NUM_SPLITS = "UPDATE dataset SET num_splits=?, num_set_per_split=? WHERE id=?";
    private PreparedStatement updateDatasetWithNumSplits;


    public static final String INSERT_PUBLISHED_RESULT = "INSERT INTO published_results(dataset_id,name,result_path) VALUES(?,?,?);";
    private PreparedStatement insertPublishedResult;

    public static final String GET_PUBLISHED_RESULTS = "SELECT * FROM published_results WHERE dataset_id=?";
    private PreparedStatement getPublishedResults;

    public static final String DELETE_PUBLISHED_RESULT = "DELETE FROM published_results WHERE id=?";
    private PreparedStatement deletePublishedResult;


    //public static final String GET_VERSIONS_FOR_COLLECTION =



    //cached types maps
    public static final String GET_TRACK_METADATA_TYPES = "SELECT * FROM track_metadata_definitions";
    public static final String GET_FILE_METADATA_TYPES = "SELECT * FROM file_metadata_definitions";
    public static final String GET_TASK_TYPES = "SELECT * FROM task_type";
    public static final String GET_SET_TYPES = "SELECT * FROM set_type_definitions";
    private Map<Integer,String> trackMetadataTypeMap;
    private Map<Integer,String> fileMetadataTypeMap;
    private Map<Integer,String> taskTypeMap;
    private Map<Integer,String> setTypeMap;
    private Map<String,Integer> trackMetadataTypeMapRev;
    private Map<String,Integer> fileMetadataTypeMapRev;
    private Map<String,Integer> taskTypeMapRev;
    private Map<String,Integer> setTypeMapRev;

    

    DatabaseConnector dbCon;


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
        getTasks = dbCon.con.prepareStatement(GET_TASKS_QUERY);
        getDatasetsByCollection = dbCon.con.prepareStatement(GET_DATA_SETS_FOR_COLLECTION_QUERY);
        getDatasets = dbCon.con.prepareStatement(GET_DATA_SETS_QUERY);
        getDatasetByID = dbCon.con.prepareStatement(GET_DATA_SET_BY_ID_QUERY);
        getDatasetsByTask = dbCon.con.prepareStatement(GET_DATA_SETS_FOR_TASK_QUERY);
        getSubsetForDataset = dbCon.con.prepareStatement(GET_COLLECTION_SUBSET_FOR_DATASET_QUERY);
        getExpSetsForDataset = dbCon.con.prepareStatement(GET_EXPERIMENT_SETS_FOR_DATASET_QUERY);
        getSetTracks = dbCon.con.prepareStatement(GET_SET_TRACKS_QUERY);
        getFileMetadata = dbCon.con.prepareStatement(GET_FILE_METADATA_QUERY);
        getTrackMetadata = dbCon.con.prepareStatement(GET_TRACK_METADATA_QUERY);
        getTrackMetadataSpecific = dbCon.con.prepareStatement(GET_TRACK_METADATA_SPECIFIC_QUERY);

        insertTrack = dbCon.con.prepareStatement(INSERT_TRACK);
        insertTrackCollectionLink = dbCon.con.prepareStatement(INSERT_TRACK_COLLECTION_LINK);
        insertFile = dbCon.con.prepareStatement(INSERT_FILE, Statement.RETURN_GENERATED_KEYS);
        insertFileMetaDef = dbCon.con.prepareStatement(INSERT_FILE_METADATA_DEFINITIONS);
//        getFileMetaDefs = dbCon.con.prepareStatement(GET_FILE_METADATA_DEFINITIONS);
        insertFileMeta = dbCon.con.prepareStatement(INSERT_FILE_METADATA);
        getFileMeta = dbCon.con.prepareStatement(GET_FILE_METADATA);
        insertFileMetaLink = dbCon.con.prepareStatement(INSERT_FILE_METADATA_LINK);
        getAllTracks = dbCon.con.prepareStatement(GET_ALL_TRACKS);
        insertTrackMetaDef = dbCon.con.prepareStatement(INSERT_TRACK_METADATA_DEFINITIONS);
        insertTrackMeta = dbCon.con.prepareStatement(INSERT_TRACK_METADATA, Statement.RETURN_GENERATED_KEYS);
        insertTrackMetaLink = dbCon.con.prepareStatement(INSERT_TRACK_METADATA_LINK);
        insertDataset = dbCon.con.prepareStatement(INSERT_DATASET, Statement.RETURN_GENERATED_KEYS);
        updateDatasetWithSubset = dbCon.con.prepareStatement(UPDATE_DATASET_WITH_SUBSET);
        insertSet = dbCon.con.prepareStatement(INSERT_SET, Statement.RETURN_GENERATED_KEYS);
        insertSetTrackLink = dbCon.con.prepareStatement(INSERT_SET_TRACK_LINK);
        updateDatasetWithNumSplits = dbCon.con.prepareStatement(UPDATE_DATASET_WITH_NUM_SPLITS);

        
        insertPublishedResult = dbCon.con.prepareStatement(INSERT_PUBLISHED_RESULT);
        getPublishedResults = dbCon.con.prepareStatement(GET_PUBLISHED_RESULTS);
        deletePublishedResult = dbCon.con.prepareStatement(DELETE_PUBLISHED_RESULT);

        initTypesMaps();
    }

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
        taskTypeMap = populateTypesMap(GET_TASK_TYPES);
        setTypeMap = populateTypesMap(GET_SET_TYPES);
        trackMetadataTypeMapRev = reverseTypesMap(trackMetadataTypeMap);
        fileMetadataTypeMapRev = reverseTypesMap(fileMetadataTypeMap);
        taskTypeMapRev = reverseTypesMap(taskTypeMap);
        setTypeMapRev = reverseTypesMap(setTypeMap);

    }



    public void insertTrackMetaDef(String name) throws SQLException{
        System.out.println("Inserting Track metadata type: " + name);
        insertTrackMetaDef.setString(1, name);
        insertTrackMetaDef.executeUpdate();
    }

    public int insertTrackMeta(int metadata_type_id, String value) throws SQLException{
        System.out.println("Inserting Track metadata value: " + metadata_type_id + "=" + value);
        insertTrackMeta.setInt(1, metadata_type_id);
        insertTrackMeta.setString(2, value);
        insertTrackMeta.executeUpdate();
        ResultSet rs = insertTrackMeta.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }else{
            Logger.getLogger(RepositoryClientImpl.class.getName()).log(Level.SEVERE, "Failed to get id for inserted track metadata " + metadata_type_id + "=" + value);
            return -1;
        }
    }

    public void insertTrackMetaLink(String track_id, int track_metadata_id) throws SQLException{
        insertTrackMetaLink.setString(1, track_id);
        insertTrackMetaLink.setInt(2, track_metadata_id);
        insertTrackMetaLink.executeUpdate();
    }

    public List<String> getAllTracks() throws SQLException{
        ResultSet rs = getAllTracks.executeQuery();
        ArrayList<String> tracks = new ArrayList<String>();
        while(rs.next()){
            tracks.add(rs.getString(1));
        }
        return tracks;
    }

    public void insertTrack(String id) throws SQLException{
        insertTrack.setString(1, id);
        insertTrack.executeUpdate();
    }

    public void insertTrackCollectionLink(int collection_id, String track_id) throws SQLException{
        insertTrackCollectionLink.setInt(1, collection_id);
        insertTrackCollectionLink.setString(2, track_id);
        insertTrackCollectionLink.executeUpdate();
    }

    public int insertFile(String track_id, String path) throws SQLException{
        insertFile.setString(1, track_id);
        insertFile.setString(2, path);
        insertFile.executeUpdate();
        ResultSet rs = insertFile.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }else{
            Logger.getLogger(RepositoryClientImpl.class.getName()).log(Level.SEVERE, "Failed to get id for inserted file: " + path);
            return -1;
        }
    }

    public void insertFileMetaDef(String name) throws SQLException{
        System.out.println("Inserting File metadata type: " + name);
        insertFileMetaDef.setString(1, name);
        insertFileMetaDef.executeUpdate();
    }

    public void insertFileMeta(int metadata_type_id, String value) throws SQLException{
        System.out.println("Inserting file metadata value: " + metadata_type_id + "=" + value);
        insertFileMeta.setInt(1, metadata_type_id);
        insertFileMeta.setString(2, value);
        insertFileMeta.executeUpdate();
    }

    public void insertFileMetaLink(int file_id, int file_metadata_id) throws SQLException{
        insertFileMetaLink.setInt(1, file_id);
        insertFileMetaLink.setInt(2, file_metadata_id);
        insertFileMetaLink.executeUpdate();
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





    public String getTrackMetadataName(int typeId){
        return trackMetadataTypeMap.get(typeId);
    }

    public String getFileMetadataName(int typeId){
        return fileMetadataTypeMap.get(typeId);
    }

    public String getTaskTypeName(int typeId){
        return taskTypeMap.get(typeId);
    }

    public String getSetTypeName(int typeId){
        return setTypeMap.get(typeId);
    }

    public int getTrackMetadataID(String typeName){
        return trackMetadataTypeMapRev.get(typeName);
    }

    public int getFileMetadataID(String typeName){
        return fileMetadataTypeMapRev.get(typeName);
    }

    public int getTaskTypeID(String typeName){
        return taskTypeMapRev.get(typeName);
    }

    public int getSetTypeID(String typeName){
        return setTypeMapRev.get(typeName);
    }

    public void insertTestOnlyDataset(String name,
            String description,
            int collection_id,
            int subject_track_metadata_type_id,
            int filter_track_metadata_type_id,
            int task_id,
            File dataset_subset_file,
            File testsetFiles) throws SQLException{
        //read up subset tracks
        List<String> subsetList = ClassificationResultReadClass.readClassificationFileAsList(dataset_subset_file, true);

        dbCon.con.setAutoCommit(false);
        try{

            //insert dataset description
            int datasetId = insertDataset(name, description, collection_id, subject_track_metadata_type_id, filter_track_metadata_type_id, task_id, subsetList);

            int testType = getSetTypeID("test");
            int trainType = getSetTypeID("train");

            //for each split test set
            List<String> testList, trainList;

            //read up test set tracks
            testList = ClassificationResultReadClass.readClassificationFileAsList(testsetFiles, true);

            System.out.println("Inserting test set size: " + testList.size());

            //insert test and training sets
            int testSetId = insertSetDescription(datasetId, testType, 1);
            insertSetTracks(testSetId, testList);

            //update dataset description with number of splits and number of sets per split
            //UPDATE dataset SET num_splits=?, num_set_per_split=? WHERE id=?
            updateDatasetWithNumSplits.setInt(1, 1);
            updateDatasetWithNumSplits.setInt(2, 1);
            updateDatasetWithNumSplits.setInt(3,datasetId);
            updateDatasetWithNumSplits.executeUpdate();

        }catch(SQLException e){
            dbCon.con.rollback();
            throw e;
        }
        dbCon.con.setAutoCommit(true);

    }

    public void insertTestTrainDataset(String name,
            String description,
            int collection_id,
            int subject_track_metadata_type_id,
            int filter_track_metadata_type_id,
            int task_id,
            File dataset_subset_file,
            List<File> testset_files) throws SQLException{
        //read up subset tracks
        List<String> subsetList = ClassificationResultReadClass.readClassificationFileAsList(dataset_subset_file, true);

        dbCon.con.setAutoCommit(false);
        try{

            //insert dataset description
            int datasetId = insertDataset(name, description, collection_id, subject_track_metadata_type_id, filter_track_metadata_type_id, task_id, subsetList);

            int testType = getSetTypeID("test");
            int trainType = getSetTypeID("train");

            //for each split test set
            List<String> testList, trainList;
            int setNum = 0;
            for (Iterator<File> it = testset_files.iterator(); it.hasNext();){
                File testSetFile = it.next();
                //read up test set tracks
                testList = ClassificationResultReadClass.readClassificationFileAsList(testSetFile, true);

                //subtract test set tracks from subset for dataset
                trainList = new ArrayList<String>(subsetList);
                trainList.removeAll(testList);

                System.out.println("Inserting training set size: " + trainList.size() + ", test set size: " + testList.size());

                //insert test and training sets
                int testSetId = insertSetDescription(datasetId, testType, setNum);
                int trainSetId = insertSetDescription( datasetId, trainType, setNum);
                insertSetTracks(testSetId, testList);
                insertSetTracks(trainSetId, trainList);

                setNum++;
            }


            //update dataset description with number of splits and number of sets per split
            //UPDATE dataset SET num_splits=?, num_set_per_split=? WHERE id=?
            updateDatasetWithNumSplits.setInt(1, setNum);
            updateDatasetWithNumSplits.setInt(2, 2);
            updateDatasetWithNumSplits.setInt(3,datasetId);
            updateDatasetWithNumSplits.executeUpdate();

        }catch(SQLException e){
            dbCon.con.rollback();
            throw e;
        }
        dbCon.con.setAutoCommit(true);

    }


    private int insertDataset(
            String name,
            String description,
            int collection_id,
            int subject_track_metadata_type_id,
            int filter_track_metadata_type_id,
            int task_id,
            List<String> subsetList) throws SQLException{

        int datasetId = -1;


        insertDataset.setString(1, name);
        insertDataset.setString(2, description);
        insertDataset.setInt(3, collection_id);
        insertDataset.setInt(4, subject_track_metadata_type_id);
        insertDataset.setInt(5, filter_track_metadata_type_id);
        insertDataset.setInt(6, task_id);
        insertDataset.executeUpdate();

        ResultSet rs = insertDataset.getGeneratedKeys();
        if (rs.next()){
            datasetId = rs.getInt(1);
        }else{
            throw new SQLException("The dataset insertion did not return an inserted id!");
        }


        //insert subset and link to Dataset
        int setType = getSetTypeID("collection_subset");
        int subsetId = insertSetDescription(datasetId, setType, -1);
        insertSetTracks(subsetId, subsetList);

        //link subset to dataset
        updateDatasetWithSubset.setInt(1,subsetId);
        updateDatasetWithSubset.setInt(2,datasetId);
        updateDatasetWithSubset.executeUpdate();

        dbCon.con.commit();

        return datasetId;
    }

    private int insertSetDescription(int datasetId, int setTypeId, int splitNumber) throws SQLException{
        //INSERT INTO set(dataset_id, set_type_id, split_number) VALUES(?,?,?)
        insertSet.setInt(1,datasetId);
        insertSet.setInt(2,setTypeId);
        insertSet.setInt(3, splitNumber);
        insertSet.executeUpdate();

        int setId = -1;
        ResultSet rs = insertSet.getGeneratedKeys();
        if (rs.next()){
            setId = rs.getInt(1);
        }else{
            throw new SQLException("The set insertion did not return an inserted id!");
        }
        return setId;
    }

    private void insertSetTracks(int subsetId, List<String> tracks) throws SQLException{
        //INSERT INTO set_track_link(set_id,track_id) VALUES(?,?)
        for (Iterator<String> it = tracks.iterator(); it.hasNext();){
            String track = it.next();
            insertSetTrackLink.setInt(1,subsetId);
            insertSetTrackLink.setString(2,track);
            insertSetTrackLink.executeUpdate();
        }
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

    public List<NEMADataset> getDatasets() throws SQLException{
        List<Map<String, String>> results = executePreparedStatement(getDatasets);
        return buildNEMADataset(results);
    }

    public NEMADataset getDataset(int dataset_id) throws SQLException{
        List<Map<String, String>> results = executeStatement(getDatasetByID, dataset_id);
        if(results.size() > 0){
            return buildNEMADataset(results.get(0));
        }else{
            return null;
        }
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


    public NEMAMetadataEntry getTrackMetadataByID(String trackId, int metadataId) throws SQLException{
        getTrackMetadataSpecific.setString(1, trackId);
        getTrackMetadataSpecific.setInt(2, metadataId);
        List<Map<String, String>> results = executePreparedStatement(getTrackMetadata);
        if (results.size() > 0){
            return buildNEMAMetadataEntry(results.get(0), trackMetadataTypeMap);
        }else{
            return null;
        }
    }

    public NEMAMetadataEntry getTrackMetadata(NEMATrack track, int metadataId) throws SQLException{
        return getTrackMetadataByID(track.getId(),metadataId);
    }

    public Map<String,NEMAMetadataEntry> getTrackMetadataByID(List<String> tracks, int metadataId) throws SQLException{
        HashMap<String,NEMAMetadataEntry> out = new HashMap<String, NEMAMetadataEntry>();
        String id;
        for (Iterator<String> it = tracks.iterator(); it.hasNext();){
            id = it.next();
            out.put(id, getTrackMetadataByID(id, metadataId));
        }
        return out;
    }

    public Map<String,NEMAMetadataEntry> getTrackMetadata(List<NEMATrack> tracks, int metadataId) throws SQLException{
        HashMap<String,NEMAMetadataEntry> out = new HashMap<String, NEMAMetadataEntry>();
        String id;
        for (Iterator<NEMATrack> it = tracks.iterator(); it.hasNext();){
            id = it.next().getId();
            out.put(id, getTrackMetadataByID(id, metadataId));
        }
        return out;
    }




    private List<Map<String, String>> getFileData(Set<NEMAMetadataEntry> constraint,
                                                  String trackId) throws SQLException{
        String query = GET_CONSTRAINED_FILE_FOR_TRACK;
        NEMAMetadataEntry nemaMetadataEntry;
        for (Iterator<NEMAMetadataEntry> it = constraint.iterator(); it.hasNext();){
            nemaMetadataEntry = it.next();
            query += "(metadata_type_id=" + fileMetadataTypeMapRev.get(nemaMetadataEntry.getType()) + " AND value='" + nemaMetadataEntry.getValue() + "')";
            if (it.hasNext()){
                query += " OR ";
            }
        }
        query += "))";
        System.out.println("Executing constructed query: " + query);
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


    public Map<Integer,String> populateTypesMap(String query) throws SQLException{
        System.out.println("Populating types map with query: " + query);
        PreparedStatement st = dbCon.con.prepareStatement(query);
        Map<Integer,String> retVal = new HashMap<Integer,String>();
        ResultSet rs = null;
        try {
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println(id + ": " + name);
                retVal.put(id, name);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
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

    public void publishResultForDataset(int dataset_id, String systemName,
                                        String result_path) throws SQLException{
        insertPublishedResult.setInt(1,dataset_id);
        insertPublishedResult.setString(2, systemName);
        insertPublishedResult.setString(3, result_path);
        insertPublishedResult.executeUpdate();
    }

    public List<PublishedResult> getPublishedResultsForDataset(int dataset_id)
            throws SQLException{
        getPublishedResults.setInt(1,dataset_id);
        ResultSet rs = getPublishedResults.executeQuery();
        List<PublishedResult> out = new ArrayList<PublishedResult>();
        while(rs.next()){
            int id = rs.getInt("id");
            String  name = rs.getString("name");
            String path = rs.getString("result_path");
            out.add(new PublishedResult(id, name, path));
        }
        return out;
    }

    public void deletePublishedResult(int result_id) throws SQLException{
        deletePublishedResult.setInt(1, result_id);
        deletePublishedResult.execute();
    }

    public void deletePublishedResult(PublishedResult result) throws SQLException{
        deletePublishedResult(result.getId());
    }



}
