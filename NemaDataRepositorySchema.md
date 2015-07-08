# Introduction #




# Concepts #
  * Task: MIREX task/subtask
  * File: Concrete song file on disk
  * Track: Abstract concept representing a song
  * Dataset:
  * Site: (Not used) Where collections reside
  * Track List: List of tracks in a dataset
  * Collection

## Tables ##

### TRACK ###
Represents a song (abstract concept). See FILE table for concrete song files stored on disk. The MIREX database contains nearly 150,000 tracks.
  * id: Track identifier

### TRACK\_METADATA\_DEFINITIONS ###
The set of possible TRACK metadata fields (e.g., album, artist, genre, composer, etc).
  * id:  Metadata field identifier
  * name: Metadata field name

### TRACK\_METADATA ###
The set of possible TRACK metadata field values.
  * id: Metadata value identifier
  * metadata\_type\_id: TRACK\_METADATA\_DEFINITIONS.ID
  * value: Metadata value

=== TRACK\_TRACK\_METADATA\_LINK
Maps tracks to TRACK\_METADATA values.
  * id: Unique identifier
  * track\_id: TRACK.ID
  * track\_metadata\_id: TRACK\_METADATA.ID

### FILE ###
Represents the concrete file in disk.
  * id: Unique identifier
  * track\_id: TRACK.ID
  * path: File path on disk
  * site: SITE.ID

### FILE\_METADATA ###
The set of possible FILE metadata field values.
  * id: Unique identifier
  * metadata\_type\_id: FILE\_METADATA\_DEFINITIONS.ID
  * value: Metadata value

### FILE\_METADATA\_DEFINITIONS ###
The set of possible FILE metadata fields (e.g., bitrate, channels, encoding).
  * id: Unique identifier
  * name: Field name

### FILE\_FILE\_METADATA\_LINK ###
Maps files to FILE\_METADATA values.
  * id: Unique identifier
  * file\_id: FILE.ID
  * file\_metadata\_id: FILE\_METADATA.ID

### TASK ###
A task is a MIREX task or subtask. There is one TASK record for each MIREX subtask associated with a test corpus. Tasks group the metadata that participants are trying to predict. Subtasks are grouped together by TASK.SUBJECT\_TASK\_METADATA, which keys into TRACK\_METADATA\_DEFINITIONS.ID. For example, the MIREX task "Audio Beat Tracking" includes to TASK records -- "Audio Beat Tracking - MAZ dataset" and "Audio Beat Tracking - MCK Dataset". Both of these subtasks share a common SUBJET\_TASK\_METADATA value "11" -- "List of beat times". Each task links to a DATASET.

Fields:
  * id: Unique identifier
  * name: Task/subtask name
  * description: Task/subtask description
  * subject\_track\_metadata: TRACK\_METADATA\_DEFINITIONS.ID
  * dataset\_id: DATASET.ID

### DATASET ###
There is a dataset for each task. Datasets can be reused. For example, genre classification and mood classification can use the same dataset as long as there is associated metadata.

Fields:
  * id: Unique identifier
  * name: Dataset name
  * description: Dataset description
  * collection\_id: COLLECTION.ID (Not used)
  * subset\_set\_id: TRACKLIST.ID
  * num\_splits: Number of test/training sets (either 1 or 3)
  * num\_set\_per\_split: test/train = 2; test/train/validate = 3
  * split\_class: (Not used, but code exists). See "Splitting" section below
  * split\_parameters\_string = (Not used)
  * subject\_track\_metadata\_type\_id = Redundant -- same as TASK.SUBJECT\_TRACK\_METADATA
  * filter\_track\_metadata\_type\_id: no filter = -1; artist = 2; album = 3

### COLLECTION ###

### TRACKLIST ###
All of the TRACKS in a DATASET -- audio files used for a particular run.
  * id: Unique identifier
  * dataset\_id: DATASET.ID
  * set\_type\_id:
  * split\_number:


## Splitting ##
The DATASET table includes a SPLIT\_CLASS column. Code exists for splitting datasets. The splitting process can be non-trivial.  For example, splits may need to be balanced based on collection statistics.

## Filtering ##
The DATASET table includes a FILTER\_TRACK\_METADATA\_TYPE\_ID field. Need to get a better understanding of this from Andy/Mert. Example, for genre classification, it's easier to classify the artist, so need to make sure that we have each artist in the test/training/validation sets.