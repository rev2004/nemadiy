## Introduction ##

The NEMA project is going to involve the development of a repository that will represent data about tracks, collections, datasets, metadata, features and anything else that NEMA deals with. The effort will begin Jan 2010. In the mean time we need to develop a temporary resource to represent information about collections, datasets and audio files.

## Temporary Repository Design ##

We are primarily interested in Collections and Datasets, where a Collection embodies a collection of tracks and audio files that represent them and a Dataset represents a subset of a collection and one or more partitions of the subset into sets (e.g. test/train).

The collection should represent a list of tracks. Tracks have attached metadata (including NEMA id - IMIRSEL file hash at the mo) and zero to many audio file locations. Metadata about versions of 'tracks' should be represented so that a particular version (e.g. 22khz mono, 44khz stereo 30 second clip, midi) can be selected if available.

The dataset should define a task name and description, subset of the collection (including the whole collection), its division into different sets (training, test and validation, potentially N-sets) and the method used to produce that split (split class, metadata split/to be predicted, use of an artist or other metadata filter, split parameters). A dataset should be able to represent multiple iterations for cross-validation and just a single set.(e.g. for onset detection - 'General collection' in your document). Finally, a dataset embodies a task - independent of the audio file type to be used from the collection.

This structure should simplify the definition of datasets for tasks as it can represent both test/train collections and general collections (Its trivial to allow the dataset to output the data from its set or sets 'file by file' or as block), avoids duplication in the representation of the collection and its metadata (easier to manage) and extends it to support extra sets if we ever need them.

Your visible (but non-editable) attributes for a collection might be:

  * Name and descriptive data,
  * NEMA track ids in the collection,
  * (optionally) metadata types available for collection.

Configurable attributes for collections are:

  * File type (e.g. 22khz mono MP3, 44khz stereo wav, 30 second clip, midi),
  * dataset (each embodies a task and definition of the 'tracks' in each set).

Visible (but non-editable) attributes for a dataset are:

  * Task name and descriptive text,
  * NEMA track IDs for each track in the subset (e.g. for feature extraction list files),
  * NEMA track IDs list for each set grouped by iteration (e.g. for corss-validation),
  * How collection was split (folds, num sets per fold, metadata to be predicted, metadata filtered on, split class (inc. by hand and no split).

Hence, the user selects only collection, file type and dataset and making new datasets over existing collections is simplified.

This can all be represented in DB tables along the lines of:

  * tracks
    * track name
    * track\_id -> IMIRSEL hash
  * file type definitions
    * file\_type\_id
    * name
    * any flags for special handling of metadata (e.g. full track F0 may be little help on a 30 sec clip with no detail on start/end time of the clip)
  * metadata type definitions
    * metdata\_type\_id
    * name (such as length, genre, artist, tags, moodtags, onset times, F0)
    * value type (could all be string encoded initially although types, i.e. string, string sequence, string seq with timestamps, numeric, numeric sequence, numeric seq with timestamps, might be better - although this can still be string encoded, e.g. in XML or some arbitrary syntax, to go in the DB
  * track metadata
    * track\_id
    * metadata\_type\_id
    * value (string)
  * file
    * file\_id
    * track\_id
    * file type id
    * path
  * collection
    * collection\_id
    * name
    * description
  * collection\_track\_link
    * collection\_id
    * track\_id
  * dataset
    * dataset\_id
    * collection\_id
    * name
    * description
    * split details and parameters
      * num sets
      * set names
      * num iterations
      * split class
      * target metadata
      * filter metadata
      * split seed
  * set type
    * set\_type\_id
    * name (train, test, validation, single etc.)
  * set
    * dataset\_id
    * set\_id
    * set\_name
    * set\_type\_id
    * iteration\_number
  * set\_track\_link
    * set\_id
    * track\_id
    * seq\_number (in case tracks need to be ordered)


Queries will be fairly easy to write that can retrieve data based on user parameters of collection, file version type and dataset that can be used to knock out different list file types for each set in the dataset as needed. Doing this on the on the fly will take no more than a second or two.

Simple queries can also be used to determine:

  * the file types available for the collection (by joining with track and file tables),
    * what percentage of each collection is available in each version type?
    * which datasets have complete file sets on each version.
  * Metadata types available for each collection
  * Datasets for each collection