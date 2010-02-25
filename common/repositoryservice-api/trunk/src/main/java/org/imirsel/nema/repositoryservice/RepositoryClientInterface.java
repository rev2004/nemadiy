/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repositoryservice;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.imirsel.nema.model.NemaCollection;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaFile;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrack;
import org.imirsel.nema.model.NemaPublishedResult;

/**
 * An interface defining the methods of a query client for the temporary NEMA metadata
 * repository. Additionally, result publicaiton methods are defined.
 *
 * @author kriswest
 */
public interface RepositoryClientInterface {

    /**
     * Close any DB connections held.
     */
    public void close();

    /**
     * Retrieves a list of NemaCollection Objects describing the available
     * collections.
     *
     * @return a list of NemaCollection Objects describing the available
     * collections.
     *
     * @throws SQLException
     */
    public List<NemaCollection> getCollections() throws SQLException;

    /**
     * Retrieves a Set containing Sets of NemaMetadataEntry Objects which define
     * the different file types that files in the collection are available in.
     * The inner set defines a unique combination of different metadata values
     * that appears in the collection. If one of more files with a particular
     * combination of metadata values appears in the specified collection that
     * combination will be returned by this method.
     *
     * @param collection The collection to retrieve the Set of versions for.
     *
     * @return A Set containing Sets of NemaMetadataEntry Objects which define
     * the different file types that files in the collection are available in.
     *
     * @throws SQLException
     */
    public List<List<NemaMetadataEntry>> getCollectionVersions(NemaCollection collection) throws SQLException;
    /**
     * Retrieves a Set containing Sets of NemaMetadataEntry Objects which define
     * the different file types that files in the collection are available in.
     * The inner set defines a unique combination of different metadata values
     * that appears in the collection. If one of more files with a particular
     * combination of metadata values appears in the specified collection that
     * combination will be returned by this method.
     *
     * @param collectionId The collection id to retrieve the Set of versions for.
     *
     * @return A Set containing Sets of NemaMetadataEntry Objects which define
     * the different file types that files in the collection are available in.
     *
     * @throws SQLException
     */
    public List<List<NemaMetadataEntry>> getCollectionVersions(int collectionId) throws SQLException;


    /**
     * Retrieves a List of NemaDataset Objects describing the datasets that are
     * available for a particular Collection. No guarantee is given that the
     * datasets described have complete audio file sets in any particular
     * file version.
     *
     * @param collection The collection to retrieve the list of datasets for.
     *
     * @return a List of NemaDataset Objects describing the datasets that are
     * available for the specified Collection.
     *
     * @throws SQLException
     */
    public List<NemaDataset> getDatasets() throws SQLException;

    /**
     * Retrieves a List of NemaDataset Objects describing the datasets that are
     * available for a particular Collection. No guarantee is given that the
     * datasets described have complete audio file sets in any particular
     * file version.
     * @param dataset_id 
     *
     * @param collection The collection to retrieve the list of datasets for.
     *
     * @return a List of NemaDataset Objects describing the datasets that are
     * available for the specified Collection.
     *
     * @throws SQLException
     */
    public NemaDataset getDataset(int dataset_id) throws SQLException;

    //Deprecated these as currently dataset links to task, where task should link to dataset;
    //    dataset 1 -> * task
    // and collection is really just an organisational unit and datasets should be able to span collections
//    /**
//     * Retrieves a List of NemaDataset Objects describing the datasets that are
//     * available for a particular Collection. No guarantee is given that the
//     * datasets described have complete audio file sets in any particular
//     * file version.
//     *
//     * @param collection The collection to retrieve the list of datasets for.
//     *
//     * @return a List of NemaDataset Objects describing the datasets that are
//     * available for the specified Collection.
//     *
//     * @throws SQLException
//     */
//    public List<NemaDataset> getDatasetsForCollection(NemaCollection collection) throws SQLException;
//    /**
//     * Retrieves a List of NemaDataset Objects describing the datasets that are
//     * available for a particular Collection. No guarantee is given that the
//     * datasets described have complete audio file sets in any particular
//     * file version.
//     *
//     * @param collectionId The collection ID to retrieve the list of datasets
//     * for.
//     *
//     * @return a List of NemaDataset Objects describing the datasets that are
//     * available for the specified Collection.
//     *
//     * @throws SQLException
//     */
//    public List<NemaDataset> getDatasetsForCollection(int collectionId) throws SQLException;
//
//    /**
//     * Retrieves a List of NemaDataset Objects describing the datasets that are
//     * available for a particular task. No guarantee is given that the
//     * datasets described have complete audio file sets in any particular
//     * file version.
//     *
//     * @param task The task to retrieve the list of datasets for.
//     *
//     * @return a List of NemaDataset Objects describing the datasets that are
//     * available for the specified task.
//     *
//     * @throws SQLException
//     */
//    public List<NemaDataset> getDatasetsForTask(NemaTask task) throws SQLException;
//    /**
//     * Retrieves a List of NemaDataset Objects describing the datasets that are
//     * available for a particular task. No guarantee is given that the
//     * datasets described have complete audio file sets in any particular
//     * file version.
//     *
//     * @param taskId The task ID to retrieve the list of datasets
//     * for.
//     *
//     * @return a List of NemaDataset Objects describing the datasets that are
//     * available for the specified task.
//     *
//     * @throws SQLException
//     */
//    public List<NemaDataset> getDatasetsForTask(int taskId) throws SQLException;
//
//    /**
//     * Retrieves a NemaTrackList Object describing the subset of a Collection that
//     * is relevant to a dataset. This may be resolved to a list of tracks using
//     * <code>getTracks(NemaTrackList set)</code> and that to a list of files using
//     * <code>getFiles(List<NemaTrack> trackList, Set<NemaMetadataEntry> constraint)</code>.
//     * Finally, the file type versions (each defined by a set of metadata
//     * entries) that the dataset is available in may be retrieved using
//     * <code>getSetVersions(NemaTrackList set)</code>.
//     *
//     * @param dataset The dataset to retrieve the collection subset for.
//     *
//     * @return A NemaTrackList Object describing the collection subset.
//     *
//     * @throws SQLException
//     */
    
    public NemaTrackList getCollectionSubset(NemaDataset dataset) throws SQLException;
    /**
     * Retrieves a NemaTrackList Object describing the subset of a Collection that
     * is relevant to a dataset. This may be resolved to a list of tracks using
     * <code>getTracks(NemaTrackList set)</code> and that to a list of files using
     * <code>getFiles(List<NemaTrack> trackList, Set<NemaMetadataEntry> constraint)</code>.
     * Finally, the file type versions (each defined by a set of metadata
     * entries) that the dataset is available in may be retrieved using
     * <code>getSetVersions(NemaTrackList set)</code>.
     *
     * @param datasetId The dataset ID to retrieve the collection subset for.
     *
     * @return A NemaTrackList Object describing the collection subset.
     *
     * @throws SQLException
     */
    public NemaTrackList getCollectionSubset(int datasetId) throws SQLException;


    /**
     * Retrieves a Set containing Sets of NemaMetadataEntry Objects which define
     * the different file types that the <emph>complete</emph> set of tracks,
     * corresponding to the NemaTrackList, are available in.
     * The inner Set defines a unique combination of different metadata values
     * that appears in the NemaTrackList. To be returned by this method a file
     * with that combination of metadata values must exist for all tracks
     * in the NemaTrackList.
     *
     * Can be used in conjunction with <code>getCollectionSubset(int datasetId)</code>
     * to get the different versions that a complete dataset is available in.
     *
     * @param set The NemaTrackList to find complete file type version lists for.
     *
     * @return a Set containing Sets of NemaMetadataEntry Objects which define
     * the different file types that the <emph>complete</emph> set of tracks
     * for the NemaTrackList are available in.
     *
     * @throws SQLException
     */
    public List<List<NemaMetadataEntry>> getSetVersions(NemaTrackList set) throws SQLException;
    /**
     * Retrieves a Set containing Sets of NemaMetadataEntry Objects which define
     * the different file types that the <emph>complete</emph> set of tracks,
     * corresponding to the NemaTrackList, are available in.
     * The inner Set defines a unique combination of different metadata values
     * that appears in the NemaTrackList. To be returned by this method a file
     * with that combination of metadata values must exist for all tracks
     * in the NemaTrackList.
     *
     * Can be used in conjunction with <code>getCollectionSubset(int datasetId)</code>
     * to get the different versions that a complete dataset is available in.
     * 
     * @param setId The NemaTrackList ID to find complete file type version lists for.
     *
     * @return a Set containing Sets of NemaMetadataEntry Objects which define
     * the different file types that the <emph>complete</emph> set of tracks
     * for the NemaTrackList are available in.
     *
     * @throws SQLException
     */
    public List<List<NemaMetadataEntry>> getSetVersions(int setId) throws SQLException;


    /**
     * Retrieves a List of Lists of NemaTrackList Objects that describe the
     * experimental track sets for each iteration of the experiment defined by
     * the NemaDataset. The outer list enumerates the sets by iteration and the
     * inner List groups the sets by relationship. Hence, for a 3-fold
     * cross-validated classification experiment the outer list would
     * contain 3 lists each containing two sets defining the test and training
     * sets.
     *
     * @param dataset The dataset to retrieve the experiment sets for.
     *
     * @return a List of Lists of NemaTrackList Objects that describe the
     * experimental track sets for each iteration of the experiment defined by
     * the NemaDataset.
     *
     * @throws SQLException
     */
    public List<List<NemaTrackList>> getExperimentSets(NemaDataset dataset) throws SQLException;
    /**
     * Retrieves a List of Lists of NemaTrackList Objects that describe the
     * experimental track sets for each iteration of the experiment defined by
     * the NemaDataset corresponding to the datasetID passed. The outer list
     * enumerates the sets by iteration and the inner List groups the sets by
     * relationship. Hence, for a 3-fold cross-validated classification
     * experiment the outer list would contain 3 lists each containing two sets
     * defining the test and training sets.
     *
     * @param datasetID The dataset ID to retrieve the experiment sets for.
     *
     * @return a List of Lists of NemaTrackList Objects that describe the
     * experimental track sets for each iteration of the experiment defined by
     * the NemaDataset corresponding to the datasetID passed..
     *
     * @throws SQLException
     */
    public List<List<NemaTrackList>> getExperimentSets(int datasetID) throws SQLException;


    /**
     * Retrieves a List of NemaTrack Objects defining the tracks corresponding
     * to a NemaTrackList Object.
     *
     * @param set The NemaTrackList to retreve a track list for.
     *
     * @return a List of NemaTrack Objects.
     *
     * @throws SQLException
     */
    public List<NemaTrack> getTracks(NemaTrackList set) throws SQLException;
    /**
     * Retrieves a List of NemaTrack Objects defining the tracks corresponding
     * to a NemaTrackList Object.
     *
     * @param setId The NemaTrackList to retreve a track list for.
     *
     * @return a List of NemaTrack Objects.
     *
     * @throws SQLException
     */
    public List<NemaTrack> getTracks(int setId) throws SQLException;

    /**
     * Retrieves a List of NemaTrack Objects defining the tracks corresponding
     * to a NemaTrackList Object.
     *
     * @param set The NemaTrackList to retreve a track list for.
     *
     * @return a List of NemaTrack Objects.
     *
     * @throws SQLException
     */
    public List<String> getTrackIDs(NemaTrackList set) throws SQLException;
    /**
     * Retrieves a List of NemaTrack Objects defining the tracks corresponding
     * to a NemaTrackList Object.
     *
     * @param setId The NemaTrackList to retreve a track list for.
     *
     * @return a List of NemaTrack Objects.
     *
     * @throws SQLException
     */
    public List<String> getTrackIDs(int setId) throws SQLException;

    /**
     * Returns a NemaFile matching the the NemaTrack specified and having
     * the metadata values specified. If more than one NemaFile matches the
     * NemaTrack and constraint then no guarantee is provided as to which is
     * returned. If no NemaFile matches then null is returned.
     *
     * @param track The NemaTrack to retrieve a File for.
     * @param constraint The File metadata based constraint used to select the
     * NemaFile.
     *
     * @return A NemaFile matching the NemaTrack and File metadata constraint.
     *
     * @throws SQLException
     */
    public NemaFile getFile(NemaTrack track, Set<NemaMetadataEntry> constraint) throws SQLException;
    /**
     * Returns a NemaFile matching the the NemaTrack specified and having
     * the metadata values specified. If more than one NemaFile matches the
     * NemaTrack and constraint then no guarantee is provided as to which is
     * returned. If no NemaFile matches then null is returned.
     *
     * @param trackId The ID of the NemaTrack to retrieve a File for.
     * @param constraint The File metadata based cpnstraint used to select the
     * NemaFile.
     *
     * @return A NemaFile matching the NemaTrack and File metadata constraint.
     *
     * @throws SQLException
     */
    public NemaFile getFile(String trackId, Set<NemaMetadataEntry> constraint) throws SQLException;

    /**
     * Returns a map linking NemaFile Objects (keys) to Sets of
     * NemaMetadataEntry Objects (values) which define the metadata of that NemaFile
     * Object. An entry in the map will be made for each file that matches
     * the constraint passed. If the constrant is null or empty then entries
     * will be given for each file that corresponds to the NemaTrack.
     *
     * @param track The NemaTrack to retrieve NemaFile's for.
     * @param constraint The File metadata based cpnstraint used to select the
     * NemaFile.
     * @return A map where the keys are NemaFile Objects matching the constraint
     * and the specified NemaTrack, the values are Sets of NemaMetadataEntry
     * Objects defining describing the File type.
     * @throws SQLException
     */
    public Map<NemaFile,List<NemaMetadataEntry>> getFileFuzzy(NemaTrack track, Set<NemaMetadataEntry> constraint) throws SQLException;
    /**
     * Returns a map linking NemaFile Objects (keys) to Sets of
     * NemaMetadataEntry Objects (values) which define the metadata of that NemaFile
     * Object. An entry in the map will be made for each file that matches
     * the constraint passed. If the constrant is null or empty then entries
     * will be given for each file that corresponds to the NemaTrack.
     *
     * @param trackId The ID of the NemaTrack to retrieve NemaFile's for.
     * @param constraint The File metadata based cpnstraint used to select the
     * NemaFile.
     * @return A map where the keys are NemaFile Objects matching the constraint
     * and the specified NemaTrack, the values are Sets of NemaMetadataEntry
     * Objects defining describing the File type.
     * @throws SQLException
     */
    public Map<NemaFile,List<NemaMetadataEntry>> getFileFuzzy(String trackId, Set<NemaMetadataEntry> constraint) throws SQLException;

    /**
     * Returns a NemaFile matching the the NemaTrack specified and having
     * the metadata values specified in the list passed.
     * If more than one NemaFile matches each NemaTrack and constraint then no
     * guarantee is  provided as to which is returned. If no NemaFile matches
     * then null is returned (The ArrayList clas supports null entries).
     *
     * @param trackList The list of NemaTrack Objects to retrieve NEMAFiles for.
     * @param constraint The file metadata constraint to use in selecting the
     * files.
     *
     * @return A list of NemaFile Objects corresponding to the NemaTrack Objects
     * with null entires where no NemaFile could be found that matched the
     * constraint.
     *
     * @throws SQLException
     */
    public List<NemaFile> getFiles(List<NemaTrack> trackList, Set<NemaMetadataEntry> constraint) throws SQLException;
    /**
     * Returns a NemaFile matching the the NemaTrack with the specified ID and
     * having the metadata values specified in the list passed.
     * If more than one NemaFile matches each NemaTrack and constraint then no
     * guarantee is  provided as to which is returned. If no NemaFile matches
     * then null is returned (The ArrayList clas supports null entries).
     *
     * @param trackIDList The list of IDs to retrieve NEMAFiles for.
     * @param constraint The file metadata constraint to use in selecting the
     * files.
     *
     * @return A list of NemaFile Objects corresponding to the track IDs
     * with null entries where no NemaFile could be found that matched the
     * constraint.
     *
     * @throws SQLException
     */
    public List<NemaFile> getFilesByID(List<String> trackIDList, Set<NemaMetadataEntry> constraint) throws SQLException;

    /**
     * Logs a result directory path or identifier and system name against a 
     * specific dataset to facilitate group evaluation and comparison of all
     * published results for a dataset.
     * 
     * @param dataset_id The id of the dataset.
     * @param systemName The system name, will be used to identify the system in
     * evaluations.
     * @param username The user name, will be used to list the published
     * results for a user.
     * @param result_path The path or identifier that will be used to retrieve
     * the reslt directory.
     * 
     * @throws SQLException
     */
    public void publishResultForDataset(int dataset_id, String username, String systemName, String result_path) throws SQLException;

    /**This is a HACK it returns all the published results for a user.
     * It might be from any dataset. Added by Amit at the last minute.
     * 
     * @param username
     * @return List<NemaPublishedResult> list of published results
     * @throws SQLException
     */
    public List<NemaPublishedResult> getPublishedResultsForDataset(String username) throws SQLException;;
    /**
     * Returns a list of the published results for a username.
     *
     * @param username The user retrieve published results for.
     * @return a list of the published results for a username.
     * @throws SQLException
     */
    public List<NemaPublishedResult> getPublishedResultsForUsername(String username) throws SQLException;

    /**
     * Returns a list of the published results for a dataset.
     *
     * @param dataset_id The id of the dataset
     * @return a list of the published results for a dataset.
     * @throws SQLException
     */
    public List<NemaPublishedResult> getPublishedResultsForDataset(int dataset_id) throws SQLException;

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
    public void deletePublishedResult(NemaPublishedResult result) throws SQLException;



    /**
     * Retrieve File metadata values for a specified file id.
     * @param fileId The ID of the file to get metadata values for.
     * @return A List of the metdata values, represented as <code><NemaMetadataEntry/code> Objects, for the file id.
     * @throws SQLException
     */
    public List<NemaMetadataEntry> getFileMetadataByID(int fileId) throws SQLException;
    /**
     * Retrieve File metadata values for a specified <code>NemaFile</code> Object.
     * @param file The <code>NemaFile</code> to retrieve metadata values for.
     * @return A List of the metdata values, represented as <code><NemaMetadataEntry/code> Objects, for the file id.
     * @throws SQLException
     */
    public List<NemaMetadataEntry> getFileMetadata(NemaFile file) throws SQLException;
    
    /**
     * Retrieve File metadata values for a specified List of file IDs.
     * @param fileIDs A List of the IDs to get metadata values for.
     * @return A Map from file ID to a List of the metdata values, represented as <code><NemaMetadataEntry/code> Objects, for the file id.
     * @throws SQLException
     */
    public Map<Integer,List<NemaMetadataEntry>> getFileMetadataByID(List<Integer> fileIDs) throws SQLException;
    /**
     * Retrieve File metadata values for a specified List of <code>NemaFile</code> Object.
     * @param files A List of the <code>NemaFile</code> Objects to retrieve metadata values for.
     * @return A Map from file ID to a List of the metdata values, represented as <code><NemaMetadataEntry/code> Objects, for the file id.
     * @throws SQLException
     */
    public Map<Integer,List<NemaMetadataEntry>> getFileMetadata(List<NemaFile> files) throws SQLException;

    /**
     * Retrieve Track metadata values for a specified Track id.
     * @param trackId The ID of the track to get metadata values for.
     * @return A List of the metdata values, represented as <code><NemaMetadataEntry/code> Objects, for the track id.
     * @throws SQLException
     */
    public List<NemaMetadataEntry> getTrackMetadataByID(String trackId) throws SQLException;
    /**
     * Retrieve File metadata values for a specified <code>NemaTrack</code> Object.
     * @param track The <code>NemaTrack</code> to retrieve metadata values for.
     * @return A List of the metdata values, represented as <code><NemaMetadataEntry/code> Objects, for the track id.
     * @throws SQLException
     */
    public List<NemaMetadataEntry> getTrackMetadata(NemaTrack track) throws SQLException;
    
    /**
     * Retrieve Track metadata values for a specified List of track IDs.
     * @param tracks A List of the IDs to get metadata values for.
     * @return A Map from track ID to a List of the metdata values, represented as <code><NemaMetadataEntry/code> Objects, for the track id.
     * @throws SQLException
     */
    public Map<String,List<NemaMetadataEntry>> getTrackMetadataByID(List<String> tracks) throws SQLException;
    /**
     * Retrieve Track metadata values for a specified List of <code>NemaTrack</code> Object.
     * @param tracks A List of the <code>NemaTrack</code> Objects to retrieve metadata values for.
     * @return A Map from track ID to a List of the metdata values, represented as <code><NemaMetadataEntry/code> Objects, for the track id.
     * @throws SQLException
     */
    public Map<String,List<NemaMetadataEntry>> getTrackMetadata(List<NemaTrack> tracks) throws SQLException;

    /**
     * Retrieve metadata of a particular type for a track.
     * @param trackId The track ID to retrieve data for.
     * @param metadataId The metadata type ID to retrieve.
     * @return The list of metadata values of the specified type represented as a <code>NemaMetadataEntry</code> Object.
     * @throws SQLException
     */
    public List<NemaMetadataEntry> getTrackMetadataByID(String trackId, int metadataId) throws SQLException;
    
    /**
     * Retrieve the first metadata of a particular type for a track.
     * @param track the <code>NemaTrack</code> Object to retreive metadata for.
     * @param metadataId The metadata type ID to retrieve.
     * @return The metadata value represented as a <code>NemaMetadataEntry</code> Object.
     * @throws SQLException
     */
    public List<NemaMetadataEntry> getTrackMetadata(NemaTrack track, int metadataId) throws SQLException;
    
    /**
     * Retrieve the first metadata of a particular type for each of a list of tracks.
     * @param tracks The list of track IDs to retrieve data for.
     * @param metadataId The metadata type ID to retrieve.
     * @return A map of track ID to the metadata value represented as a <code>NemaMetadataEntry</code> Object.
     * @throws SQLException
     */
    public Map<String,List<NemaMetadataEntry>> getTrackMetadataByID(List<String> tracks, int metadataId) throws SQLException;
    /**
     * Retrieve the first metadata of a particular type for each of a list of tracks.
     * @param tracks The list of <code>NemaTrack</code> Objects to retrieve data for.
     * @param metadataId The metadata type ID to retrieve.
     * @return A map of track ID to the metadata value represented as a <code>NemaMetadataEntry</code> Object.
     * @throws SQLException
     */
    public Map<String,List<NemaMetadataEntry>> getTrackMetadata(List<NemaTrack> tracks, int metadataId) throws SQLException;

    /**
     * Returns an unmodifiable map linking track metadata type names to their IDs.
     * @return an unmodifiable map.
     */
    public Map<String,Integer> getTrackMetadataNameMap();
    
    /**
     * Returns an unmodifiable map linking file metadata type names to their IDs.
     * @return an unmodifiable map.
     */
    public Map<String,Integer> getFileMetadataNameMap();
    
    /**
     * Returns an unmodifiable map linking set type names to their IDs.
     * @return an unmodifiable map.
     */
    public Map<String,Integer> getSetTypeMap();
    
    /**
     * Returns the name for a track metadata type ID.
     * @param typeId metadata type ID to retrieve name for.
     * @return metadata type name.
     */
    public String getTrackMetadataName(int typeId);

    /**
     * Returns the name for a file metadata type ID.
     * @param typeId metadata type ID to retrieve name for.
     * @return metadata type name.
     */
    public String getFileMetadataName(int typeId);

    /**
     * Returns the name for a set metadata type ID.
     * @param typeId metadata type ID to retrieve name for.
     * @return metadata type name.
     */
    public String getSetTypeName(int typeId);

    /**
     * Returns the integer ID for the specified Track metadata type name.
     * @param typeName metadata type name to retrieve ID for.
     * @return metadata type ID.
     */
    public int getTrackMetadataID(String typeName);

    /**
     * Returns the integer ID for the specified File metadata type name.
     * @param typeName metadata type name to retrieve ID for.
     * @return metadata type ID.
     */
    public int getFileMetadataID(String typeName);

    /**
     * Returns the integer ID for the specified Set metadata type name.
     * @param typeName metadata type name to retrieve ID for.
     * @return metadata type ID.
     */
    public int getSetTypeID(String typeName);


}
