#Overview of I/O  and calling formats for various MIREX tasks


# Introduction #

Add your content here.


# Details #

Add your content here.  Format your content with:
  * Text in **bold** or _italic_
  * Headings, paragraphs, and lists
  * Automatic links to other wiki pages

# Multiple-F0 Estimation and Tracking #

## Submission Format ##

Submissions have to conform to the specified format below:

> ''doMultiF0 "path/to/file.wav"  "path/to/output/file.F0" ''

path/to/file.wav: Path to the input audio file.

path/to/output/file.F0: The output file.

## I/O File Format ##
For each sub task, the format of the output file is going to be different:
For the first task, F0-estimation on frame basis,  the output will be a file where each row has a  time stamp and a number of active F0s in that frame, separated by a tab for every 10ms increments.

Example :
> ''time	F01	F02	F03	''

> ''time	F01	F02	F03	F04''

> ''time	...	...	...	...''

which might look like:

> ''0.78	146.83	220.00	349.23''

> ''0.79	349.23	146.83	369.99	220.00	''

> ''0.80	...	...	...	...''

For the second subtask,  for each row, the file should contain  the onset, offset and the F0 of each note event separated by a tab, ordered in terms of onset times:

> onset	offset F01

> onset	offset F02

> ...	... ...

which might look like:

> 0.68	1.20	349.23

> 0.72	1.02	220.00

> ...	...	...

## Examples from past MIREX ##

**Submissions Directory**: /data/raid3/mirex2009/subs/multfiF0/NEOS/NEOS1/mnakano2009MIREXn1

**Run script**: runMultiF0.sh

**Results Dir**:  /data/raid3/mirex2009/subs/multfiF0/NEOS/NEOS1/mnakano2009MIREXn1/results


# Audio Tag Classification #

### Calling Format ###

> extractFeatures.sh /path/to/scratch/folder /path/to/featureExtractionListFile.txt

> TrainAndClassify.sh /path/to/scratch/folder /path/to/trainListFile.txt  path/to/testListFile.txt /path/to/outputAffinityFile.txt  path/to/outputBinaryRelevanceFile.txt<br><br></li></ul>

or

> extractFeatures.sh -numThreads 8 /path/to/scratch/folder /path/to/featureExtractionListFile.txt

> TrainAndClassify.sh -numThreads 8 /path/to/scratch/folder /path/to/trainListFile.txt /path/to/testListFile.txt /path/to/outputAffinityFile.txt /path/to/outputBinaryRelevanceFile.txt

or

> extractFeatures.sh /path/to/scratch/folder /path/to/featureExtractionListFile.txt

> Train.sh /path/to/scratch/folder /path/to/trainListFile.txt

> Classify.sh /path/to/scratch/folder /path/to/testListFile.txt /path/to/outputAffinityFile.txt /path/to/outputBinaryRelevanceFile.txt

or


> myAlgo.sh -extract -numThreads 8 /path/to/scratch/folder /path/to/featureExtractionListFile.txt
> myAlgo.sh -TrainAndClassify -numThreads 8 /path/to/scratch/folder /path/to/trainListFile.txt /path/to/testListFile.txt /path/to/outputAffinityFile.txt /path/to/outputBinaryRelevanceFile.txt.

or

> myAlgo.sh -extract /path/to/scratch/folder /path/to/featureExtractionListFile.txt
> myAlgo.sh -train /path/to/scratch/folder /path/to/trainListFile.txt
> myAlgo.sh -classify /path/to/scratch/folder /path/to/testListFile.txt /path/to/outputAffinityFile.txt /path/to/outputBinaryRelevanceFile.txt


### I/O File  formats ###

In this section the input and output files used in this task are described as are the command line calling format requirements for submissions.

#### Feature extraction list file ####

The list file passed for feature extraction will be a simple ASCII list file. This file will contain one path per line with no header line.

#### Training list file ####

The list file passed for model training will be a simple ASCII list file. This file will contain one path per line, followed by a tab character and a tag label, again with no header line.

E.g.

> <example path and filename>\t<tag classification>\n

In this way, the input file will represent the sparse ground truth matrix.  While no line will be duplicated, multiple lines may contain the same path, one for each tag associated with that clip. Any tag that is not specified as applying to a clip does not apply to that clip.  The ordering of the lines is arbitrary and should not be depended upon.

#### Test (classification) list file ####

The list file passed for testing classification will be a simple ASCII list file identical in format to the Feature extraction list file. This file will contain one path per line with no header line.

#### Classification output files ####

Participating algorithms should produce '''two''' simple ASCII list files similar in format to the Training list file. The path to which each list file should be written must be accepted as a parameter on the command line.

##### Tag Affinity file #####

The first file will contain one path per line, followed by a tab character and the tag label, followed by another tab character and the affinity of that tag for that file, again with no header line.

I.e.:

> <example path and filename>\t<tag classification>\t

&lt;affinity&gt;

\n

E.g.:

> /data/file1.wav    rock      0.9
> /data/file1.wav    guitar    0.7
> /data/file1.wav    vocal     0.3
> /data/file2.wav    rock      0.5
> ...

In this way, the output file will represent the sparse classification matrix.  A path should be repeated on a separate line for each tag that the submission deems applies to it.  If a (path, tag) pair is not specified, it will be assumed to have an affinity of 0.  The ordering of the lines is not important and can be arbitrary.

The affinity will be used for retrieval evaluation metrics, and its only specification is that for a given tag, larger (closer to +infinity) numbers indicate that the tag is more appropriate to a clip than smaller (closer to -infinity) numbers.  As submissions are asked to also return a binary relevance listing, submissions that do not compute an affinity should provide only the binary relevance listing file.

##### Binary relevance file #####

The second file to be produced is a binary version of the tag classifications, where a tag must be marked as relevant or not relevant to a track. This file will contain one path per line, followed by a tab character and the tag label, followed by another tab character and either a 1 or a 0 indicating the relevance of that tag for that file, again with no header line.

I.e.:

> <example path and filename>\t<tag classification>\t<relevant? [| 1](0.md)>\n

E.g.:

> /data/file1.wav    rock      1
> /data/file1.wav    guitar    1
> /data/file1.wav    vocal     0
> /data/file2.wav    rock      1
> ...

If a (path, tag) pair is not specified, it will be assumed to be non-relevant (0). Any line with path but no numerical value will be assumed to be relevant (1).

Hence, the following is equivalent to the example above:

> /data/file1.wav    rock
> /data/file1.wav    guitar
> /data/file2.wav    rock

The ordering of the lines is not important and can be arbitrary.