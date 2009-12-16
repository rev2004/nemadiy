/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An interface defining the methods of a client to the temporary NEMA metadata
 * repository.
 *
 * @author kriswest
 */
public interface RepositoryClientInterface {

    /**
     * Retrieves a list of NEMACollection Objects describing the available
     * collections.
     *
     * @return a list of NEMACollection Objects describing the available
     * collections.
     *
     * @throws SQLException
     */
    public List<NEMACollection> getCollections() throws SQLException;

    /**
     * Retrieves a list of NEMATask Objects describing the available
     * tasks.
     *
     * @return a list of NEMATask Objects describing the available
     * tasks.
     *
     * @throws SQLException
     */
    public List<NEMATask> getTasks() throws SQLException;


    /**
     * Retrieves a Set containing Sets of NEMAMetadataEntry Objects which define
     * the different file types that files in the collection are available in.
     * The inner set defines a unique combination of different metadata values
     * that appears in the collection. If one of more files with a particular
     * combination of metadata values appears in the specified collection that
     * combination will be returned by this method.
     *
     * @param collection The collection to retrieve the Set of versions for.
     *
     * @return A Set containing Sets of NEMAMetadataEntry Objects which define
     * the different file types that files in the collection are available in.
     *
     * @throws SQLException
     */
    public List<List<NEMAMetadataEntry>> getCollectionVersions(NEMACollection collection) throws SQLException;
    /**
     * Retrieves a Set containing Sets of NEMAMetadataEntry Objects which define
     * the different file types that files in the collection are available in.
     * The inner set defines a unique combination of different metadata values
     * that appears in the collection. If one of more files with a particular
     * combination of metadata values appears in the specified collection that
     * combination will be returned by this method.
     *
     * @param collectionId The collection id to retrieve the Set of versions for.
     *
     * @return A Set containing Sets of NEMAMetadataEntry Objects which define
     * the different file types that files in the collection are available in.
     *
     * @throws SQLException
     */
    public List<List<NEMAMetadataEntry>> getCollectionVersions(int collectionId) throws SQLException;


    /**
     * Retrieves a List of NEMADataset Objects describing the datasets that are
     * available for a particular Collection. No guarantee is given that the
     * datasets described have complete audio file sets in any particular
     * file version.
     *
     * @param collection The collection to retrieve the list of datasets for.
     *
     * @return a List of NEMADataset Objects describing the datasets that are
     * available for the specified Collection.
     *
     * @throws SQLException
     */
    public List<NEMADataset> getDatasets() throws SQLException;

    /**
     * Retrieves a List of NEMADataset Objects describing the datasets that are
     * available for a particular Collection. No guarantee is given that the
     * datasets described have complete audio file sets in any particular
     * file version.
     *
     * @param collection The collection to retrieve the list of datasets for.
     *
     * @return a List of NEMADataset Objects describing the datasets that are
     * available for the specified Collection.
     *
     * @throws SQLException
     */
    public NEMADataset getDataset(int dataset_id) throws SQLException;

    /**
     * Retrieves a List of NEMADataset Objects describing the datasets that are
     * available for a particular Collection. No guarantee is given that the
     * datasets described have complete audio file sets in any particular
     * file version.
     *
     * @param collection The collection to retrieve the list of datasets for.
     *
     * @return a List of NEMADataset Objects describing the datasets that are
     * available for the specified Collection.
     *
     * @throws SQLException
     */
    public List<NEMADataset> getDatasetsForCollection(NEMACollection collection) throws SQLException;
    /**
     * Retrieves a List of NEMADataset Objects describing the datasets that are
     * available for a particular Collection. No guarantee is given that the
     * datasets described have complete audio file sets in any particular
     * file version.
     *
     * @param collectionId The collection ID to retrieve the list of datasets
     * for.
     *
     * @return a List of NEMADataset Objects describing the datasets that are
     * available for the specified Collection.
     *
     * @throws SQLException
     */
    public List<NEMADataset> getDatasetsForCollection(int collectionId) throws SQLException;

    /**
     * Retrieves a List of NEMADataset Objects describing the datasets that are
     * available for a particular task. No guarantee is given that the
     * datasets described have complete audio file sets in any particular
     * file version.
     *
     * @param task The task to retrieve the list of datasets for.
     *
     * @return a List of NEMADataset Objects describing the datasets that are
     * available for the specified task.
     *
     * @throws SQLException
     */
    public List<NEMADataset> getDatasetsForTask(NEMATask task) throws SQLException;
    /**
     * Retrieves a List of NEMADataset Objects describing the datasets that are
     * available for a particular task. No guarantee is given that the
     * datasets described have complete audio file sets in any particular
     * file version.
     *
     * @param taskId The task ID to retrieve the list of datasets
     * for.
     *
     * @return a List of NEMADataset Objects describing the datasets that are
     * available for the specified task.
     *
     * @throws SQLException
     */
    public List<NEMADataset> getDatasetsForTask(int taskId) throws SQLException;

    /**
     * Retrieves a NEMASet Object describing the subset of a Collection that
     * is relevant to a dataset. This may be resolved to a list of tracks using
     * <code>getTracks(NEMASet set)</code> and that to a list of files using
     * <code>getFiles(List<NEMATrack> trackList, Set<NEMAMetadataEntry> constraint)</code>.
     * Finally, the file type versions (each defined by a set of metadata
     * entries) that the dataset is available in may be retrieved using
     * <code>getSetVersions(NEMASet set)</code>.
     *
     * @param dataset The dataset to retrieve the collection subset for.
     *
     * @return A NEMASet Object describing the collection subset.
     *
     * @throws SQLException
     */
    public NEMASet getCollectionSubset(NEMADataset dataset) throws SQLException;
    /**
     * Retrieves a NEMASet Object describing the subset of a Collection that
     * is relevant to a dataset. This may be resolved to a list of tracks using
     * <code>getTracks(NEMASet set)</code> and that to a list of files using
     * <code>getFiles(List<NEMATrack> trackList, Set<NEMAMetadataEntry> constraint)</code>.
     * Finally, the file type versions (each defined by a set of metadata
     * entries) that the dataset is available in may be retrieved using
     * <code>getSetVersions(NEMASet set)</code>.
     *
     * @param datasetId The dataset ID to retrieve the collection subset for.
     *
     * @return A NEMASet Object describing the collection subset.
     *
     * @throws SQLException
     */
    public NEMASet getCollectionSubset(int datasetId) throws SQLException;


    /**
     * Retrieves a Set containing Sets of NEMAMetadataEntry Objects which define
     * the different file types that the <emph>complete</emph> set of tracks,
     * corresponding to the NEMASet, are available in.
     * The inner Set defines a unique combination of different metadata values
     * that appears in the NEMASet. To be returned by this method a file
     * with that combination of metadata values must exist for all tracks
     * in the NEMASet.
     *
     * Can be used in conjunction with <code>getCollectionSubset(int datasetId)</code>
     * to get the different versions that a complete dataset is available in.
     *
     * @param set The NEMASet to find complete file type version lists for.
     *
     * @return a Set containing Sets of NEMAMetadataEntry Objects which define
     * the different file types that the <emph>complete</emph> set of tracks
     * for the NEMASet are available in.
     *
     * @throws SQLException
     */
    public List<List<NEMAMetadataEntry>> getSetVersions(NEMASet set) throws SQLException;
    /**
     * Retrieves a Set containing Sets of NEMAMetadataEntry Objects which define
     * the different file types that the <emph>complete</emph> set of tracks,
     * corresponding to the NEMASet, are available in.
     * The inner Set defines a unique combination of different metadata values
     * that appears in the NEMASet. To be returned by this method a file
     * with that combination of metadata values must exist for all tracks
     * in the NEMASet.
     *
     * Can be used in conjunction with <code>getCollectionSubset(int datasetId)</code>
     * to get the different versions that a complete dataset is available in.
     * 
     * @param setId The NEMASet ID to find complete file type version lists for.
     *
     * @return a Set containing Sets of NEMAMetadataEntry Objects which define
     * the different file types that the <emph>complete</emph> set of tracks
     * for the NEMASet are available in.
     *
     * @throws SQLException
     */
    public List<List<NEMAMetadataEntry>> getSetVersions(int setId) throws SQLException;


    /**
     * Retrieves a List of Lists of NEMASet Objects that describe the
     * experimental track sets for each iteration of the experiment defined by
     * the NEMADataset. The outer list enumerates the sets by iteration and the
     * inner List groups the sets by relationship. Hence, for a 3-fold
     * cross-validated classification experiment the outer list would
     * contain 3 lists each containing two sets defining the test and training
     * sets.
     *
     * @param dataset The dataset to retrieve the experiment sets for.
     *
     * @return a List of Lists of NEMASet Objects that describe the
     * experimental track sets for each iteration of the experiment defined by
     * the NEMADataset.
     *
     * @throws SQLException
     */
    public List<List<NEMASet>> getExperimentSets(NEMADataset dataset) throws SQLException;
    /**
     * Retrieves a List of Lists of NEMASet Objects that describe the
     * experimental track sets for each iteration of the experiment defined by
     * the NEMADataset corresponding to the datasetID passed. The outer list
     * enumerates the sets by iteration and the inner List groups the sets by
     * relationship. Hence, for a 3-fold cross-validated classification
     * experiment the outer list would contain 3 lists each containing two sets
     * defining the test and training sets.
     *
     * @param datasetID The dataset ID to retrieve the experiment sets for.
     *
     * @return a List of Lists of NEMASet Objects that describe the
     * experimental track sets for each iteration of the experiment defined by
     * the NEMADataset corresponding to the datasetID passed..
     *
     * @throws SQLException
     */
    public List<List<NEMASet>> getExperimentSets(int datasetID) throws SQLException;


    /**
     * Retrieves a List of NEMATrack Objects defining the tracks corresponding
     * to a NEMASet Object.
     *
     * @param set The NEMASet to retreve a track list for.
     *
     * @return a List of NEMATrack Objects.
     *
     * @throws SQLException
     */
    public List<NEMATrack> getTracks(NEMASet set) throws SQLException;
    /**
     * Retrieves a List of NEMATrack Objects defining the tracks corresponding
     * to a NEMASet Object.
     *
     * @param setId The NEMASet to retreve a track list for.
     *
     * @return a List of NEMATrack Objects.
     *
     * @throws SQLException
     */
    public List<NEMATrack> getTracks(int setId) throws SQLException;

    /**
     * Retrieves a List of NEMATrack Objects defining the tracks corresponding
     * to a NEMASet Object.
     *
     * @param set The NEMASet to retreve a track list for.
     *
     * @return a List of NEMATrack Objects.
     *
     * @throws SQLException
     */
    public List<String> getTrackIDs(NEMASet set) throws SQLException;
    /**
     * Retrieves a List of NEMATrack Objects defining the tracks corresponding
     * to a NEMASet Object.
     *
     * @param setId The NEMASet to retreve a track list for.
     *
     * @return a List of NEMATrack Objects.
     *
     * @throws SQLException
     */
    public List<String> getTrackIDs(int setId) throws SQLException;

    /**
     * Returns a NEMAFile matching the the NEMATrack specified and having
     * the metadata values specified. If more than one NEMAFile matches the
     * NEMATrack and constraint then no guarantee is provided as to which is
     * returned. If no NEMAFile matches then null is returned.
     *
     * @param track The NEMATrack to retrieve a File for.
     * @param constraint The File metadata based constraint used to select the
     * NEMAFile.
     *
     * @return A NEMAFile matching the NEMATrack and File metadata constraint.
     *
     * @throws SQLException
     */
    public NEMAFile getFile(NEMATrack track, Set<NEMAMetadataEntry> constraint) throws SQLException;
    /**
     * Returns a NEMAFile matching the the NEMATrack specified and having
     * the metadata values specified. If more than one NEMAFile matches the
     * NEMATrack and constraint then no guarantee is provided as to which is
     * returned. If no NEMAFile matches then null is returned.
     *
     * @param trackId The ID of the NEMATrack to retrieve a File for.
     * @param constraint The File metadata based cpnstraint used to select the
     * NEMAFile.
     *
     * @return A NEMAFile matching the NEMATrack and File metadata constraint.
     *
     * @throws SQLException
     */
    public NEMAFile getFile(String trackId, Set<NEMAMetadataEntry> constraint) throws SQLException;

    /**
     * Returns a map linking NEMAFile Objects (keys) to Sets of
     * NEMAMetadataEntry Objects (values) which define the metadata of that NEMAFile
     * Object. An entry in the map will be made for each file that matches
     * the constraint passed. If the constrant is null or empty then entries
     * will be given for each file that corresponds to the NEMATrack.
     *
     * @param track The NEMATrack to retrieve NEMAFile's for.
     * @param constraint The File metadata based cpnstraint used to select the
     * NEMAFile.
     * @return A map where the keys are NEMAFile Objects matching the constraint
     * and the specified NEMATrack, the values are Sets of NEMAMetadataEntry
     * Objects defining describing the File type.
     * @throws SQLException
     */
    public Map<NEMAFile,List<NEMAMetadataEntry>> getFileFuzzy(NEMATrack track, Set<NEMAMetadataEntry> constraint) throws SQLException;
    /**
     * Returns a map linking NEMAFile Objects (keys) to Sets of
     * NEMAMetadataEntry Objects (values) which define the metadata of that NEMAFile
     * Object. An entry in the map will be made for each file that matches
     * the constraint passed. If the constrant is null or empty then entries
     * will be given for each file that corresponds to the NEMATrack.
     *
     * @param trackId The ID of the NEMATrack to retrieve NEMAFile's for.
     * @param constraint The File metadata based cpnstraint used to select the
     * NEMAFile.
     * @return A map where the keys are NEMAFile Objects matching the constraint
     * and the specified NEMATrack, the values are Sets of NEMAMetadataEntry
     * Objects defining describing the File type.
     * @throws SQLException
     */
    public Map<NEMAFile,List<NEMAMetadataEntry>> getFileFuzzy(String trackId, Set<NEMAMetadataEntry> constraint) throws SQLException;

    /**
     * Returns a NEMAFile matching the the NEMATrack specified and having
     * the metadata values specified in the list passed.
     * If more than one NEMAFile matches each NEMATrack and constraint then no
     * guarantee is  provided as to which is returned. If no NEMAFile matches
     * then null is returned (The ArrayList clas supports null entries).
     *
     * @param trackList The list of NEMATrack Objects to retrieve NEMAFiles for.
     * @param constraint The file metadata constraint to use in selecting the
     * files.
     *
     * @return A list of NEMAFile Objects corresponding to the NEMATrack Objects
     * with null entires where no NEMAFile could be found that matched the
     * constraint.
     *
     * @throws SQLException
     */
    public List<NEMAFile> getFiles(List<NEMATrack> trackList, Set<NEMAMetadataEntry> constraint) throws SQLException;
    /**
     * Returns a NEMAFile matching the the NEMATrack with the specified ID and
     * having the metadata values specified in the list passed.
     * If more than one NEMAFile matches each NEMATrack and constraint then no
     * guarantee is  provided as to which is returned. If no NEMAFile matches
     * then null is returned (The ArrayList clas supports null entries).
     *
     * @param trackIDList The list of IDs to retrieve NEMAFiles for.
     * @param constraint The file metadata constraint to use in selecting the
     * files.
     *
     * @return A list of NEMAFile Objects corresponding to the track IDs
     * with null entries where no NEMAFile could be found that matched the
     * constraint.
     *
     * @throws SQLException
     */
    public List<NEMAFile> getFilesByID(List<String> trackIDList, Set<NEMAMetadataEntry> constraint) throws SQLException;

    /**
     * Logs a result directory path or identifier and system name against a 
     * specific dataset to facilitate group evaluation and comparison of all
     * published results for a dataset.
     * 
     * @param dataset_id The id of the dataset.
     * @param systemName The system name, will be used to dentify the system in 
     * evaluations.
     * @param result_path The path or identifier that will be used to retrieve
     * the reslt directory.
     * 
     * @throws SQLException
     */
    public void publishResultForDataset(int dataset_id, String systemName, String result_path) throws SQLException;

    /**
     * Returns a list of the published results for a username.
     *
     * @param username The user retrieve published results for.
     * @return a list of the published results for a username.
     * @throws SQLException
     */
    public List<PublishedResult> getPublishedResultsForUsername(String username) throws SQLException;

    /**
     * Returns a list of the published results for a dataset.
     *
     * @param dataset_id The id of the dataset
     * @return a list of the published results for a dataset.
     * @throws SQLException
     */
    public List<PublishedResult> getPublishedResultsForDataset(int dataset_id) throws SQLException;

    /**
     * Deletes a published result record from the DB.
     *
     * @param result_id The id of teh result to delete.
     * @throws SQLException
     */
    public void deletePublishedResult(int result_id) throws SQLException;
    /**
     * Deletes a published result record from the DB.
     *
     * @param result The result to delete.
     * @throws SQLException
     */
    public void deletePublishedResult(PublishedResult result) throws SQLException;




    public List<NEMAMetadataEntry> getFileMetadataByID(int fileId) throws SQLException;
    public List<NEMAMetadataEntry> getFileMetadata(NEMAFile file) throws SQLException;
    public Map<Integer,List<NEMAMetadataEntry>> getFileMetadataByID(List<Integer> fileIDs) throws SQLException;
    public Map<Integer,List<NEMAMetadataEntry>> getFileMetadata(List<NEMAFile> files) throws SQLException;

    public List<NEMAMetadataEntry> getTrackMetadataByID(String trackId) throws SQLException;
    public List<NEMAMetadataEntry> getTrackMetadata(NEMATrack track) throws SQLException;
    public Map<String,List<NEMAMetadataEntry>> getTrackMetadataByID(List<String> tracks) throws SQLException;
    public Map<String,List<NEMAMetadataEntry>> getTrackMetadata(List<NEMATrack> tracks) throws SQLException;

    public NEMAMetadataEntry getTrackMetadataByID(String trackId, int metadataId) throws SQLException;
    public NEMAMetadataEntry getTrackMetadata(NEMATrack track, int metadataId) throws SQLException;
    public Map<String,NEMAMetadataEntry> getTrackMetadataByID(List<String> tracks, int metadataId) throws SQLException;
    public Map<String,NEMAMetadataEntry> getTrackMetadata(List<NEMATrack> tracks, int metadataId) throws SQLException;



}
