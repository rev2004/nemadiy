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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.nema.repositoryservice.*;
import org.imirsel.nema.model.*;
import org.imirsel.m2k.evaluation.classification.ClassificationTextFile;

/**
 * 
 * @author kriswest
 */
public class RepositoryClientImpl implements RepositoryClientInterface{

    public static final String GET_COLLECTIONS_QUERY = "SELECT * FROM collection";
    private PreparedStatement getCollections;

    public static final String GET_DATA_SETS_FOR_COLLECTION_QUERY = "SELECT * FROM dataset WHERE collection_id=?";
    private PreparedStatement getDatasetsByCollection;

    public static final String GET_DATA_SETS_QUERY = "SELECT * FROM dataset";
    private PreparedStatement getDatasets;

    public static final String GET_DATA_SET_BY_ID_QUERY = "SELECT * FROM dataset WHERE id=?";
    private PreparedStatement getDatasetByID;

    public static final String GET_COLLECTION_SUBSET_FOR_DATASET_QUERY = "SELECT nemadatarepository.set.* FROM nemadatarepository.set,dataset WHERE dataset.id=? AND nemadatarepository.set.id=dataset.subset_set_id";
    private PreparedStatement getSubsetForDataset;

    public static final String GET_EXPERIMENT_SETS_FOR_DATASET_QUERY = "SELECT nemadatarepository.set.* FROM nemadatarepository.set,dataset WHERE set.dataset_id=? AND dataset.id=set.dataset_id AND nemadatarepository.set.id!=dataset.subset_set_id";
    private PreparedStatement getExpSetsForDataset;

    public static final String GET_SET_TRACKS_QUERY = "SELECT track_id FROM set_track_link where set_id=?";
    private PreparedStatement getSetTracks;

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
            "SELECT file.* from file WHERE file.id IN (\n" +
            "SELECT file_id from file,file_metadata,file_file_metadata_link WHERE file.track_id='?' AND file.id=file_file_metadata_link.file_id \n";

    public static final String GET_CONSTRAINED_FILES =
            "SELECT file.* from file WHERE file.id IN (\n" +
            "SELECT file_id from file,file_metadata,file_file_metadata_link WHERE file.id=file_file_metadata_link.file_id \n";

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


    //public static final String GET_VERSIONS_FOR_COLLECTION =



    //cached types maps
    public static final String GET_TRACK_METADATA_TYPES = "SELECT * FROM track_metadata_definitions";
    public static final String GET_FILE_METADATA_TYPES = "SELECT * FROM file_metadata_definitions";
    public static final String GET_SET_TYPES = "SELECT * FROM set_type_definitions";
    private Map<Integer,String> trackMetadataTypeMap;
    private Map<Integer,String> fileMetadataTypeMap;
    private Map<Integer,String> setTypeMap;
    private Map<String,Integer> trackMetadataTypeMapRev;
    private Map<String,Integer> fileMetadataTypeMapRev;
    private Map<String,Integer> setTypeMapRev;

    

    DatabaseConnector dbCon;

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
        getDatasetsByCollection = dbCon.con.prepareStatement(GET_DATA_SETS_FOR_COLLECTION_QUERY);
        getDatasets = dbCon.con.prepareStatement(GET_DATA_SETS_QUERY);
        getDatasetByID = dbCon.con.prepareStatement(GET_DATA_SET_BY_ID_QUERY);
        getSubsetForDataset = dbCon.con.prepareStatement(GET_COLLECTION_SUBSET_FOR_DATASET_QUERY);
        getExpSetsForDataset = dbCon.con.prepareStatement(GET_EXPERIMENT_SETS_FOR_DATASET_QUERY);
        getSetTracks = dbCon.con.prepareStatement(GET_SET_TRACKS_QUERY);
        getFileMetadata = dbCon.con.prepareStatement(GET_FILE_METADATA_QUERY);
        getTrackMetadata = dbCon.con.prepareStatement(GET_TRACK_METADATA_QUERY);
        getAllTrackMetadata = dbCon.con.prepareStatement(GET_ALL_TRACK_METADATA_QUERY);
        getTrackMetadataSpecific = dbCon.con.prepareStatement(GET_TRACK_METADATA_SPECIFIC_QUERY);
        getAllTrackMetadataSpecific = dbCon.con.prepareStatement(GET_ALL_TRACK_METADATA_SPECIFIC_QUERY);

        getAllTracks = dbCon.con.prepareStatement(GET_ALL_TRACKS);
        getFileMeta = dbCon.con.prepareStatement(GET_FILE_METADATA);
        
        insertPublishedResult = dbCon.con.prepareStatement(INSERT_PUBLISHED_RESULT);
        getPublishedResultsForDataset = dbCon.con.prepareStatement(GET_PUBLISHED_RESULTS_FOR_DATASET);
        getPublishedResultsForUsername = dbCon.con.prepareStatement(GET_PUBLISHED_RESULTS_FOR_USERNAME);
        deletePublishedResult = dbCon.con.prepareStatement(DELETE_PUBLISHED_RESULT);

        initTypesMaps();
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
        setTypeMap = populateTypesMap(GET_SET_TYPES);
        trackMetadataTypeMapRev = reverseTypesMap(trackMetadataTypeMap);
        fileMetadataTypeMapRev = reverseTypesMap(fileMetadataTypeMap);
        setTypeMapRev = reverseTypesMap(setTypeMap);

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
    
    public Map<String,Integer> getSetTypeMap(){
    	return Collections.unmodifiableMap(setTypeMapRev);
    }
    
    

    public String getTrackMetadataName(int typeId){
        return trackMetadataTypeMap.get(typeId);
    }

    public String getFileMetadataName(int typeId){
        return fileMetadataTypeMap.get(typeId);
    }

    public String getSetTypeName(int typeId){
        return setTypeMap.get(typeId);
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

    public int getSetTypeID(String typeName){
    	Integer out = setTypeMapRev.get(typeName);
        if (out == null){
        	return -1;
        }else{
        	return out;
        }
    }



    public List<NEMACollection> getCollections() throws SQLException{
        List<Map<String, String>> results = executePreparedStatement(getCollections);
        return buildNEMACollections(results);
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

    public List<String> getTrackIDs(NEMASet set) throws SQLException{
        return getTrackIDs(set.getId());
    }

    public List<String> getTrackIDs(int setId) throws SQLException{
        List<Map<String, String>> results = executeStatement(getSetTracks, setId);
        List<String> ids = new ArrayList<String>(results.size());
        for (Iterator<Map<String, String>> it = results.iterator(); it.hasNext();){
            String id = it.next().get("track_id");
//            System.out.println(id);
            ids.add(id);
        }
        return ids;
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
        Set<String> trackSet = new HashSet<String>(tracks);

        List<Map<String, String>> results = executePreparedStatement(getAllTrackMetadata);
        NEMAMetadataEntry entry;
        List<NEMAMetadataEntry> list;
        String track_id;
        for (Iterator<Map<String, String>> it = results.iterator(); it.hasNext();){
            Map<String, String> map = it.next();
            track_id = map.get("track_id");
            if (trackSet.contains(track_id)){
                entry = buildNEMAMetadataEntry(map, trackMetadataTypeMap);
                list = out.get(track_id);
                if (list == null){
                    list = new ArrayList<NEMAMetadataEntry>(5);
                    out.put(track_id,list);
                }
                list.add(entry);
            }
        }
        return out;
    }


    public List<NEMAMetadataEntry> getTrackMetadataByID(String trackId, int metadataId) throws SQLException{
        getTrackMetadataSpecific.setString(1, trackId);
        getTrackMetadataSpecific.setInt(2, metadataId);
        List<Map<String, String>> results = executePreparedStatement(getTrackMetadataSpecific);
        if (results.size() > 0){
        	List<NEMAMetadataEntry> out = new ArrayList<NEMAMetadataEntry>(results.size());
            for(Iterator<Map<String,String>> it = results.iterator(); it.hasNext();){
            	out.add(buildNEMAMetadataEntry(it.next(), trackMetadataTypeMap));
            }
        	return out;
        }else{
            return null;
        }
    }

    public List<NEMAMetadataEntry> getTrackMetadata(NEMATrack track, int metadataId) throws SQLException{
        return getTrackMetadataByID(track.getId(),metadataId);
    }

    public Map<String,List<NEMAMetadataEntry>> getTrackMetadataByID(List<String> tracks, int metadataId) throws SQLException{
        HashMap<String,List<NEMAMetadataEntry>> out = new HashMap<String, List<NEMAMetadataEntry>>();
        Set<String> trackSet = new HashSet<String>(tracks);

        getAllTrackMetadataSpecific.setInt(1, metadataId);
        List<Map<String, String>> results = executePreparedStatement(getAllTrackMetadataSpecific);
        String trackId;
        List<NEMAMetadataEntry> tmp;
        for (Iterator<Map<String, String>> it = results.iterator(); it.hasNext();){
            Map<String, String> map = it.next();
            trackId = map.get("track_id");
            if (trackSet.contains(trackId)){
            	tmp = out.get(trackId);
            	if(tmp == null){
            		tmp = new ArrayList<NEMAMetadataEntry>(5);
            		out.put(trackId, tmp);
            	}
                tmp.add(buildNEMAMetadataEntry(map, trackMetadataTypeMap));
            }
        }
        return out;
    }

    public Map<String,List<NEMAMetadataEntry>> getTrackMetadata(List<NEMATrack> tracks, int metadataId) throws SQLException{
        HashMap<String,List<NEMAMetadataEntry>> out = new HashMap<String, List<NEMAMetadataEntry>>();
        String id;
        List<NEMAMetadataEntry> tmp;
        for (Iterator<NEMATrack> it = tracks.iterator(); it.hasNext();){
            id = it.next().getId();
            out.put(id, getTrackMetadataByID(id, metadataId));
        }
        return out;
    }


    private List<Map<String, String>> getFileData(Set<NEMAMetadataEntry> constraint) throws SQLException{
        String query = GET_CONSTRAINED_FILES;
        NEMAMetadataEntry nemaMetadataEntry;
        for (Iterator<NEMAMetadataEntry> it = constraint.iterator(); it.hasNext();){
            nemaMetadataEntry = it.next();
            query += "AND EXISTS (SELECT file_id FROM file_file_metadata_link WHERE file.id=file_file_metadata_link.file_id AND file_file_metadata_link.file_metadata_id=(SELECT id FROM file_metadata WHERE metadata_type_id=" + fileMetadataTypeMapRev.get(nemaMetadataEntry.getType()) + " AND value='" + nemaMetadataEntry.getValue() + "')) ";
            if (it.hasNext()){
                query += "\n";
            }
        }
        query += ")";
//        System.out.println("Executing constructed query: " + query);
        PreparedStatement st = dbCon.con.prepareStatement(query);
        List<Map<String, String>> results = executePreparedStatement(st);

        return results;
    }

    private List<Map<String, String>> getFileData(Set<NEMAMetadataEntry> constraint,
                                                  String trackId) throws SQLException{
        String query = GET_CONSTRAINED_FILE_FOR_TRACK;
        NEMAMetadataEntry nemaMetadataEntry;
        for (Iterator<NEMAMetadataEntry> it = constraint.iterator(); it.hasNext();){
            nemaMetadataEntry = it.next();
            query += "AND EXISTS (SELECT file_id FROM file_file_metadata_link WHERE file.id=file_file_metadata_link.file_id AND file_file_metadata_link.file_metadata_id=(SELECT id FROM file_metadata WHERE metadata_type_id=" + fileMetadataTypeMapRev.get(nemaMetadataEntry.getType()) + " AND value='" + nemaMetadataEntry.getValue() + "')) ";
            if (it.hasNext()){
                query += "\n";
            }
        }
        query += ")";
//        System.out.println("Executing constructed query: " + query);
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
        List<String> trackIDList = new ArrayList<String>(trackList.size());
        for (Iterator<NEMATrack> it = trackList.iterator(); it.hasNext();){
            trackIDList.add(it.next().getId());
        }
        
        return getFilesByID(trackIDList, constraint);
    }

    public List<NEMAFile> getFilesByID(List<String> trackIDList, Set<NEMAMetadataEntry> constraint) throws SQLException{
        logger.info("Resolving files for " + trackIDList.size() + " tracks");
        Set<String> trackSet = new HashSet<String>();
        trackSet.addAll(trackIDList);
//        int idx = 0;
        logger.info("tracks in set:");
        for (Iterator<String> it = trackSet.iterator(); it.hasNext();){
            String string = it.next();
//            System.out.println(string);
//            if (idx > 100){
//                break;
//            }
//            idx++;
        }
        Map<String,NEMAFile> fileMap = new HashMap<String, NEMAFile>(trackIDList.size());
        List<Map<String, String>> data = getFileData(constraint);
        logger.info("Query returned data on " + data.size() + " files, filtering");
        Map<String, String> map;
        String trackID;
//        idx = 0;
        for (Iterator<Map<String, String>> it = data.iterator(); it.hasNext();){
            map = it.next();
            trackID = map.get("track_id");
//
//
//            if (idx < 100){
//                System.out.println(trackID);
//            }
//            idx++;

            if(trackSet.contains(trackID)){
                fileMap.put(trackID, buildNEMAFile(map));
            }
        }

        logger.info("mapped " + fileMap.size() + " files to tracks");

        List<NEMAFile> out = new ArrayList<NEMAFile>();
        for (Iterator<String> it = trackIDList.iterator(); it.hasNext();){
            out.add(fileMap.get(it.next()));
        }

        logger.info("returning file list length: " + out.size());

        return out;
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

    private NEMADataset buildNEMADataset(Map<String, String> map){
        int subject_meta_type = Integer.parseInt(map.get("subject_track_metadata_type_id"));
        int filter_meta_type = Integer.parseInt(map.get("filter_track_metadata_type_id"));
        int task = Integer.parseInt(map.get("task_id"));
        return new NEMADataset(
                Integer.valueOf(map.get("id")),
                map.get("name"),
                map.get("description"),
//                Integer.valueOf(map.get("collection_id")),
                Integer.valueOf(map.get("subset_set_id")),
                Integer.valueOf(map.get("num_splits")),
                Integer.valueOf(map.get("num_set_per_split")),
                map.get("split_class"),
                map.get("split_parameters_string"),
                subject_meta_type,
                trackMetadataTypeMap.get(subject_meta_type),
                filter_meta_type,
                trackMetadataTypeMap.get(filter_meta_type)
//                task,
//                taskTypeMap.get(task)
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

    public List<PublishedResult> getPublishedResultsForDataset(int dataset_id)
            throws SQLException{
        getPublishedResultsForDataset.setInt(1,dataset_id);
        ResultSet rs = getPublishedResultsForDataset.executeQuery();
        List<PublishedResult> out = new ArrayList<PublishedResult>();
        while(rs.next()){
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String name = rs.getString("system_name");
            String path = rs.getString("result_path");
            Timestamp time = rs.getTimestamp("last_updated");
            out.add(new PublishedResult(id, username, name, path, time));
        }
        return out;
    }

    public List<PublishedResult> getPublishedResultsForDataset(String username)
            throws SQLException{
        getPublishedResultsForUsername.setString(1,username);
        ResultSet rs = getPublishedResultsForUsername.executeQuery();
        List<PublishedResult> out = new ArrayList<PublishedResult>();
        while(rs.next()){
            int id = rs.getInt("id");
            String username2 = rs.getString("username");
            String name = rs.getString("system_name");
            String path = rs.getString("result_path");
            Timestamp time = rs.getTimestamp("last_updated");
            out.add(new PublishedResult(id, username2, name, path, time));
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

    public List<PublishedResult> getPublishedResultsForUsername(String username)
            throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
