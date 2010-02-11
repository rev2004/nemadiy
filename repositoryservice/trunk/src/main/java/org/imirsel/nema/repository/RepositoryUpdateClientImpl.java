package org.imirsel.nema.repository;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.nema.repositoryservice.RepositoryUpdateClientInterface;

import org.imirsel.m2k.evaluation.classification.ClassificationTextFile;

public class RepositoryUpdateClientImpl extends RepositoryClientImpl implements RepositoryUpdateClientInterface{

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

    public static final String INSERT_FILE_METADATA_LINK = "INSERT INTO file_file_metadata_link(file_id,file_metadata_id) VALUES(?,?)";
    private PreparedStatement insertFileMetaLink;


    public static final String INSERT_TRACK_METADATA_DEFINITIONS = "INSERT IGNORE INTO track_metadata_definitions(name) VALUES(?)";
    private PreparedStatement insertTrackMetaDef;

    public static final String INSERT_TRACK_METADATA = "INSERT INTO track_metadata(metadata_type_id,value) VALUES(?,?) ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)";
    private PreparedStatement insertTrackMeta;

    public static final String INSERT_TRACK_METADATA_LINK = "INSERT INTO track_track_metadata_link(track_id,track_metadata_id) VALUES(?,?)";
    private PreparedStatement insertTrackMetaLink;


    public static final String INSERT_DATASET = "INSERT INTO dataset(name,description,subject_track_metadata_type_id,filter_track_metadata_type_id) values(?,?,?,?)";
    private PreparedStatement insertDataset;
    
    public static final String UPDATE_DATASET_WITH_SUBSET = "UPDATE dataset SET subset_set_id=? WHERE id=?";
    private PreparedStatement updateDatasetWithSubset;
    
    public static final String INSERT_SET = "INSERT INTO nemadatarepository.set(dataset_id, set_type_id, split_number) VALUES(?,?,?)";
    private PreparedStatement insertSet;
    
    public static final String INSERT_SET_TRACK_LINK = "INSERT INTO set_track_link(set_id,track_id) VALUES(?,?)";
    private PreparedStatement insertSetTrackLink;
    
    public static final String UPDATE_DATASET_WITH_NUM_SPLITS = "UPDATE dataset SET num_splits=?, num_set_per_split=? WHERE id=?";
    private PreparedStatement updateDatasetWithNumSplits;
    
    private static final Logger logger = Logger.getLogger(RepositoryUpdateClientImpl.class.getName());
	
    
	public RepositoryUpdateClientImpl() throws SQLException {
		super();
		
		insertTrack = dbCon.con.prepareStatement(INSERT_TRACK);
        insertTrackCollectionLink = dbCon.con.prepareStatement(INSERT_TRACK_COLLECTION_LINK);
        insertFile = dbCon.con.prepareStatement(INSERT_FILE, Statement.RETURN_GENERATED_KEYS);
        insertFileMetaDef = dbCon.con.prepareStatement(INSERT_FILE_METADATA_DEFINITIONS);
        insertFileMeta = dbCon.con.prepareStatement(INSERT_FILE_METADATA);
        insertFileMetaLink = dbCon.con.prepareStatement(INSERT_FILE_METADATA_LINK);
        insertTrackMetaDef = dbCon.con.prepareStatement(INSERT_TRACK_METADATA_DEFINITIONS);
        insertTrackMeta = dbCon.con.prepareStatement(INSERT_TRACK_METADATA, Statement.RETURN_GENERATED_KEYS);
        insertTrackMetaLink = dbCon.con.prepareStatement(INSERT_TRACK_METADATA_LINK);
        insertDataset = dbCon.con.prepareStatement(INSERT_DATASET, Statement.RETURN_GENERATED_KEYS);
        updateDatasetWithSubset = dbCon.con.prepareStatement(UPDATE_DATASET_WITH_SUBSET);
        insertSet = dbCon.con.prepareStatement(INSERT_SET, Statement.RETURN_GENERATED_KEYS);
        insertSetTrackLink = dbCon.con.prepareStatement(INSERT_SET_TRACK_LINK);
        updateDatasetWithNumSplits = dbCon.con.prepareStatement(UPDATE_DATASET_WITH_NUM_SPLITS);

	}


    public void insertTrackMetaDef(String name) throws SQLException{
        logger.info("Inserting Track metadata type: " + name);
        insertTrackMetaDef.setString(1, name);
        insertTrackMetaDef.executeUpdate();
        initTypesMaps();
    }

    public int insertTrackMeta(int metadata_type_id, String value) throws SQLException{
        logger.info("Inserting Track metadata value: " + metadata_type_id + "=" + value);
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
        logger.info("Inserting File metadata type: " + name);
        insertFileMetaDef.setString(1, name);
        insertFileMetaDef.executeUpdate();
        initTypesMaps();
    }

    public void insertFileMeta(int metadata_type_id, String value) throws SQLException{
        logger.info("Inserting file metadata value: " + metadata_type_id + "=" + value);
        insertFileMeta.setInt(1, metadata_type_id);
        insertFileMeta.setString(2, value);
        insertFileMeta.executeUpdate();
    }

    public void insertFileMetaLink(int file_id, int file_metadata_id) throws SQLException{
        insertFileMetaLink.setInt(1, file_id);
        insertFileMetaLink.setInt(2, file_metadata_id);
        insertFileMetaLink.executeUpdate();
    }

    public int insertTestOnlyDataset(String name,
            String description,
            int subject_track_metadata_type_id,
            int filter_track_metadata_type_id,
            File dataset_subset_file) throws SQLException, IOException{
    	
        //read up subset tracks
    	ClassificationTextFile reader = new ClassificationTextFile(getTrackMetadataName(subject_track_metadata_type_id));
        List<String> subsetList = reader.readClassificationFileAsList(dataset_subset_file, true);

        return insertTestOnlyDataset(name, description, subject_track_metadata_type_id, filter_track_metadata_type_id, subsetList);
    }
    
    public int insertTestOnlyDataset(String name,
            String description,
            int subject_track_metadata_type_id,
            int filter_track_metadata_type_id,
            List<String> datasetTrackIDList) throws SQLException{
        
        startTransation();
        int datasetId = -1;
        try{

            //insert dataset description
            datasetId = insertDataset(name, description, subject_track_metadata_type_id, filter_track_metadata_type_id, datasetTrackIDList);

            int testType = getSetTypeID("test");
            int trainType = getSetTypeID("train");

            logger.info("Inserting test set size: " + datasetTrackIDList.size());

            //insert test and training sets
            int testSetId = insertSetDescription(datasetId, testType, 1);
            insertSetTracks(testSetId, datasetTrackIDList);

            //update dataset description with number of splits and number of sets per split
            //UPDATE dataset SET num_splits=?, num_set_per_split=? WHERE id=?
            updateDatasetWithNumSplits.setInt(1, 1);
            updateDatasetWithNumSplits.setInt(2, 1);
            updateDatasetWithNumSplits.setInt(3,datasetId);
            updateDatasetWithNumSplits.executeUpdate();
            
            endTransation();
        }catch(SQLException e){
            rollback();
            throw e;
        }
        

        return datasetId;
    }

    public int insertTestTrainDataset(String name,
            String description,
            int subject_track_metadata_type_id,
            int filter_track_metadata_type_id,
            List<String> subsetList,
            List<List<String>> testLists) throws SQLException{
        
        startTransation();
        int datasetId = -1;
        try{

            //insert dataset description
        	logger.info("Inserting dataset description");
            datasetId = insertDataset(name, description, subject_track_metadata_type_id, filter_track_metadata_type_id, subsetList);
            logger.info("Dataset id generated: " + datasetId);
            
            int testType = getSetTypeID("test");
            int trainType = getSetTypeID("train");

            //for each split test set
            List<String> testList, trainList;
            int setNum = 0;
            for (Iterator<List<String>> it = testLists.iterator(); it.hasNext();){
                testList = it.next();

                //subtract test set tracks from subset for dataset
                trainList = new ArrayList<String>(subsetList);
                trainList.removeAll(testList);

                logger.info("Inserting training set size: " + trainList.size() + ", test set size: " + testList.size());

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
            
            endTransation();
        }catch(SQLException e){
            rollback();
            throw e;
        }
        

        return datasetId;
    }
    
    public void startTransation() throws SQLException{
    	logger.info(this.getClass().getName() + ": Starting transaction");
    	dbCon.con.setAutoCommit(false);
	}
    
    public void endTransation() throws SQLException{
    	logger.info(this.getClass().getName() + ": Commiting transaction");
    	dbCon.con.commit();
    	dbCon.con.setAutoCommit(true);
	}
        
    public void rollback() throws SQLException{
    	logger.info(this.getClass().getName() + ": Rolling-back transaction");
    	dbCon.con.rollback();
    	dbCon.con.setAutoCommit(true);
	}
    
    public int insertTestTrainDataset(String name,
            String description,
            int subject_track_metadata_type_id,
            int filter_track_metadata_type_id,
            File dataset_subset_file,
            List<File> testset_files) throws SQLException,IOException{
        //read up subset tracks
    	logger.info("Reading subset file: " + dataset_subset_file.getAbsolutePath());
    	ClassificationTextFile reader = new ClassificationTextFile(getTrackMetadataName(subject_track_metadata_type_id));
        List<String> subsetList = reader.readClassificationFileAsList(dataset_subset_file, true);
        List<List<String>> testLists = new ArrayList<List<String>>(testset_files.size());
        
        for (Iterator<File> it = testset_files.iterator(); it.hasNext();){
            File testSetFile = it.next();
            logger.info("Reading test set file: " + testSetFile.getAbsolutePath());
            
            //read up test set tracks
            testLists.add(reader.readClassificationFileAsList(testSetFile, true));
        }

        return insertTestTrainDataset(name, description, subject_track_metadata_type_id, filter_track_metadata_type_id, subsetList, testLists);
    }

    public int insertDataset(
            String name,
            String description,
            int subject_track_metadata_type_id,
            int filter_track_metadata_type_id,
            List<String> subsetList) throws SQLException{

        int datasetId = -1;
        logger.info("\tinserting descriptive data");
        insertDataset.setString(1, name);
        insertDataset.setString(2, description);
        insertDataset.setInt(3, subject_track_metadata_type_id);
        insertDataset.setInt(4, filter_track_metadata_type_id);
        insertDataset.executeUpdate();
       
        logger.info("\tretrieving generated id");
        ResultSet rs = insertDataset.getGeneratedKeys();
        if (rs.next()){
            datasetId = rs.getInt(1);
        }else{
            throw new SQLException("The dataset insertion did not return an inserted id!");
        }
        logger.info("\tdataset id: " + datasetId);

        //insert subset and link to Dataset
        int setType = getSetTypeID("collection_subset");
        
        logger.info("\tinserting collection subset");
        int subsetId = insertSetDescription(datasetId, setType, -1);
        logger.info("\tsubset id: " + subsetId);
        
        logger.info("\tinserting subset track list (" + subsetList.size() + " trackIDs)");
        insertSetTracks(subsetId, subsetList);

        //link subset to dataset
        logger.info("\tLinking subset to dataset");
        updateDatasetWithSubset.setInt(1,subsetId);
        updateDatasetWithSubset.setInt(2,datasetId);
        updateDatasetWithSubset.executeUpdate();
        
        logger.info("\tdone");
        
        return datasetId;
    }

    public int insertSetDescription(int datasetId, int setTypeId, int splitNumber) throws SQLException{
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

    public void insertSetTracks(int setID, List<String> tracks) throws SQLException{
        //INSERT INTO set_track_link(set_id,track_id) VALUES(?,?)
    	int done = 0;
        for (Iterator<String> it = tracks.iterator(); it.hasNext();){
            String track = it.next();
            insertSetTrackLink.setInt(1,setID);
            insertSetTrackLink.setString(2,track);
            insertSetTrackLink.executeUpdate();
            done++;
            if (done % 500 == 0){
            	logger.info("\t\tdone " + done + " of " + tracks.size());
            }
        }
    }
	
}
