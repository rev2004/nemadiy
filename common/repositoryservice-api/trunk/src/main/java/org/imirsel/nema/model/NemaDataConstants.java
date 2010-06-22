package org.imirsel.nema.model;

public class NemaDataConstants {

    //Metadata key constants
    /** Constant definition for metadata key: column label     */
    public final static String PROP_COLUMN_LABELS = "columnLabels";
    /** Constant definition for metadata key: file location     */
    public final static String PROP_ID  = "id";
    /** Constant definition for metadata key: file location     */
    public final static String PROP_FILE_LOCATION  = "file location";
    
    /** Constant definition for metadata key: Raw audio file data. */
    public static final String FILE_DATA = "Raw file byte data";
    

//    /** Constant definition for performance metadata.     */
//    public final static String PROP_PERF = "Performance";
//    /** Constant definition for algorithm name (used as an identifier in evaluations).     */
//    public final static String PROP_ALG_NAME = "Algorithm name";
//    /** Constant definition for      */
//    public final static String PATH_TO_ARTIST_MAP = "Path to artist map";
//    
//    
//    //Evaluation results constants
//    /** Constant definition for metadata key: Evaluation report*/
//    public static final String SYSTEM_RESULTS_REPORT = "Single system rsult evaluation report";
//    
    
    //File format constants
    /** Constant definition for section divider used in ASCII file     */
    public final static String DIVIDER = "-===-";
    /** Constant definition for SEPARATOR used in ASCII file     */
    public final static String SEPARATOR = "\t";
    /** Constant definition for header used in ASCII file     */
    public final static String fileHeader = "nema-analytics NemaData (8th Feb 2010)";
    
    //Similarity evaluator constants
    public final static String SEARCH_TRACK_DISTANCE_LIST = "Similar track list";
    public final static String SEARCH_DISTANCE_MATRIX_NAME = "Distance matrix name";
    
    public final static String SEARCH_ARTIST_FILTERED_GENRE_NEIGHBOURHOOD = "Search Artist-Filtered Genre Neighbourhood clustering";
    public final static String SEARCH_TEST_LEVELS = "Number of results search statisitcs calculated at";
    public final static String SEARCH_GENRE_NEIGHBOURHOOD = "Search Genre Neighbourhood clustering";
    public final static String SEARCH_ARTIST_NEIGHBOURHOOD = "Search Artist Neighbourhood clustering";
    public final static String SEARCH_ALBUM_NEIGHBOURHOOD = "Search Album Neighbourhood clustering";
    public final static String SEARCH_TRIANGULAR_INEQUALITY = "Search Triangular inequality holds";
    public final static String SEARCH_PEAK_SIMILAR_TRACKS = "Peak number of tracks a single track similar to";
    public final static String SEARCH_TRACKS_NEVER_SIMILAR = "Percantage of tracks never similar to another track";
    
    
    //Test/Train classification evaluator constants
    public final static String CLASSIFICATION_EXPERIMENT_CLASSNAMES = "Classification Experiment Classnames";
    public final static String CLASSIFICATION_CONFUSION_MATRIX_RAW = "Classification Confusion Matrix - raw";
    public final static String CLASSIFICATION_CONFUSION_MATRIX_PERCENT = "Classification Confusion Matrix - percent";
    public final static String CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW = "Classification Discounted Confusion Matrix - raw";
    public final static String CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT = "Classification Discounted Confusion Matrix - percent";
    public final static String CLASSIFICATION_ACCURACY = "Classification Accuracy";
    public final static String CLASSIFICATION_DISCOUNTED_ACCURACY = "Classification Discounted Accuracy";
    public final static String CLASSIFICATION_NORMALISED_ACCURACY = "Normalised Classification Accuracy";
    public final static String CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY = "Normalised Classification Discounted Accuracy";
    
    //classification types
    public final static String CLASSIFICATION_DUMMY = "Classification";
    public final static String CLASSIFICATION_ALBUM = "Album";
    public final static String CLASSIFICATION_ARTIST = "Artist";
    public final static String CLASSIFICATION_COMPOSER = "Composer";
    public final static String CLASSIFICATION_GENRE = "Genre";
    public final static String CLASSIFICATION_TITLE = "Title";
    public final static String CLASSIFICATION_MOOD = "Mood";
    
    
    
    
    
    //Tag classification evaluator constants
    /** Constant definition for tag classification data in the form of a 
     * {@code HashMap<String,HashSet<String>>} - mapping paths to a set
     * of relevant tags. */
    public static final String TAG_BINARY_RELEVANCE_MAP = "Tag classification binary relevance map";
    /** Constant definition for the list of tag names appearing in the data in 
     * the form of a {@code HashSet<String>}. */
    public static final String TAG_NAME_SET = "Tag name set";
    /** Constant definition for tag classification data in the form of a 
     * {@code HashMap<String,HashMap<String,Double>>} - mapping paths
     * to a map linking tags to their affinity values. */
    public static final String TAG_AFFINITY_MAP = "Tag classification affinity map";
    
    
    public static final String TAG_LIST = "Tag list";
    
    public static final String TAG_BINARY_ACCURACY_MAP = "Tag classification binary relevance accuracy map";
    public static final String TAG_BINARY_POS_ACCURACY_MAP = "Tag classification binary relevance positive example accuracy map";
    public static final String TAG_BINARY_NEG_ACCURACY_MAP = "Tag classification binary relevance negative example accuracy map";
    public static final String TAG_BINARY_PRECISION_MAP = "Tag classification binary relevance precision map";
    public static final String TAG_BINARY_RECALL_MAP = "Tag classification binary relevance recall map";
    public static final String TAG_BINARY_FMEASURE_MAP = "Tag classification binary relevance fMeasure map";
    
    public static final String TAG_BINARY_TRACK_ACCURACY_MAP = "Tag track classification binary relevance accuracy map";
    public static final String TAG_BINARY_TRACK_POS_ACCURACY_MAP = "Tag track classification binary relevance positive example accuracy map";
    public static final String TAG_BINARY_TRACK_NEG_ACCURACY_MAP = "Tag track classification binary relevance negative example accuracy map";
    public static final String TAG_BINARY_TRACK_PRECISION_MAP = "Tag track classification binary relevance precision map";
    public static final String TAG_BINARY_TRACK_RECALL_MAP = "Tag track classification binary relevance recall map";
    public static final String TAG_BINARY_TRACK_FMEASURE_MAP = "Tag track classification binary relevance fMeasure map";
    
    
    public static final String TAG_BINARY_OVERALL_ACCURACY = "Tag classification binary relevance overall accuracy";
    public static final String TAG_BINARY_OVERALL_PRECISION = "Tag classification binary relevance overall precision";
    public static final String TAG_BINARY_OVERALL_RECALL = "Tag classification binary relevance overall recall";
    public static final String TAG_BINARY_OVERALL_FMEASURE = "Tag classification binary relevance overall fMeasure";
    
    public static final String TAG_AFFINITY_AUC_ROC = "Tag classification affinity tag AUC-ROC map";
    public static final String TAG_AFFINITY_ROC_DATA = "Tag classification affinity tag ROC data points map";
    public static final String TAG_AFFINITY_CLIP_AUC_ROC = "Tag classification affinity clip AUC-ROC map";
    public static final String TAG_AFFINITY_CLIP_ROC_DATA = "Tag classification affinity clip ROC data points map";
    public static final String TAG_AFFINITY_OVERALL_AUC_ROC = "Tag classification affinity overall AUC-ROC";
    public static final String TAG_AFFINITY_OVERALL_ROC_DATA = "Tag classification affinity overall ROC data points";
    public static final String TAG_AFFINITY_TAG_AFFINITY_DATAPOINTS = "Tag classification affinity data points map";
    
    public static final String TAG_AFFINITY_CLIP_PRECISION_AT_N = "Tag classification affinity clip precision at N map";
    public static final String TAG_AFFINITY_OVERALL_PRECISION_AT_N = "Tag classification affinity overall precision at N";
    
    public static final String TAG_NUM_POSITIVE_EXAMPLES = "Tag classification number of positive examples map";
    public static final String TAG_NUM_NEGATIVE_EXAMPLES = "Tag classification number of negative examples map";
    
    public static final String OVERALL_NUM_POSITIVE_EXAMPLES = "Tag classification number of positive examples overall";
    public static final String OVERALL_NUM_NEGATIVE_EXAMPLES = "Tag classification number of negative examples overall";
    
    // Melody Extraction Evaluator Constants
    public static final String MELODY_EXTRACTION_DATA = "Raw melody transcription data with timestamps and F0 in Hz";
    public static final String MELODY_RAW_PITCH_ACCURACY = "Raw Pitch Accuracy";
    public static final String MELODY_RAW_CHROMA_ACCURACY = "Raw Chroma Accuracy";
    public static final String MELODY_VOICING_RECALL = "Voicing Recall Rate";
    public static final String MELODY_VOICING_FALSE_ALARM = "Voicing False-Alarm Rate";
    public static final String MELODY_OVERALL_ACCURACY = "Overall Accuracy";
    public static final double MELODY_TIME_INC = 0.01;		// MIREX-spec 10ms time-increment
    
    // Multi-F0 Estimation Evaluator Constants
    public static final double MULTI_F0_EST_TIME_INC = 0.01; // 10ms time-increment
    public static final String MULTI_F0_EST_DATA = "Raw multi-F0 estimation data with timestamps and F0 in Hz";

    // Key Detection Evaluator Constants
    public static final String KEY_DETECTION_DATA = "The musical key (tonic/mode)";
    public static final String KEY_DETECTION_WEIGHTED_SCORE ="Weighted Key Score";
    public static final String KEY_DETECTION_CORRECT = "Correct Key(s)";
    public static final String KEY_DETECTION_PERFECT_FIFTH_ERROR = "Perfect Fifth Error(s)";
    public static final String KEY_DETECTION_RELATIVE_ERROR = "Relative Major/Minor Error(s)";
    public static final String KEY_DETECTION_PARALLEL_ERROR = "Parallel Major/Minor Error(s)";
    public static final String KEY_DETECTION_ERROR = "Pure Non-disconounted Error(s)";
    
    // Tempo Extraction Evaluator Constants
    public static final String TEMPO_EXTRACTION_DATA = "Two predominant tempi, and the salience of the first reported tempo";
    public static final String TEMPO_EXTRACTION_P_SCORE ="Tempo P-Score";
    public static final String TEMPO_EXTRACTION_ONE_CORRECT = "Atleast One Tempo Correct";
    public static final String TEMPO_EXTRACTION_TWO_CORRECT = "Both Tempi Correct";    
    
    // Chord Estimation Evaluator Constants
    public static final String CHORD_LABEL_SEQUENCE = "Chord label sequence";
    public static final String CHORD_OVERLAP_RATIO = "Chord Overlap ratio";
    public static final String CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO = "Chord weighted average overlap ratio";
	
    // Structure Segmentation Evaluator Constants
    public static final String STRUCTURE_SEGMENTATION_DATA = "Structural segmentation";
    public static final String STRUCTURE_SEGMENTATION_OVERSEGSCORE = "Normalised conditional entropy based over-segmentation score";
    public static final String STRUCTURE_SEGMENTATION_UNDERSEGSCORE = "Normalised conditional entropy based under-segmentation score";
    public static final String STRUCTURE_SEGMENTATION_PWF = "Frame pair clustering F-measure";
    public static final String STRUCTURE_SEGMENTATION_PWPRECISION = "Frame pair clustering precision rate";
    public static final String STRUCTURE_SEGMENTATION_PWRECALL = "Frame pair clustering recall rate";
    public static final String STRUCTURE_SEGMENTATION_R = "Random clustering index";
    public static final String STRUCTURE_SEGMENTATION_FMEASUREATPOINTFIVE = "Segment boundary recovery evaluation measure @ 0.5sec";
    public static final String STRUCTURE_SEGMENTATION_PRECRATEATPOINTFIVE = "Segment boundary recovery precision rate @ 0.5sec";
    public static final String STRUCTURE_SEGMENTATION_RECRATEATPOINTFIVE = "Segment boundary recovery recall rate @ 0.5sec";
    public static final String STRUCTURE_SEGMENTATION_FMEASUREATTHREE = "Segment boundary recovery evaluation measure @ 3sec";
    public static final String STRUCTURE_SEGMENTATION_PRECRATEATTHREE  = "Segment boundary recovery precision rate @ 3sec";
    public static final String STRUCTURE_SEGMENTATION_RECRATEATTHREE  = "Segment boundary recovery recall rate @ 3sec";
    public static final String STRUCTURE_SEGMENTATION_MEDCLAIM2TRUE = "Median distance from an annotated segment boundary to the closest found boundary";
    public static final String STRUCTURE_SEGMENTATION_MEDTRUE2CLAIM = "Median distance from a found segment boundary to the closest annotated one";
    
    //Onset Detection Evaluator Constants
    public static final String ONSET_DETECTION_DATA = "List of onset times";
    public static final String ONSET_DETECTION_CLASS = "Instrumentation class";
    public static final String ONSET_DETECTION_ANNOTATORS = "Annotators";
    public static final String ONSET_DETECTION_AVG_PRECISION ="Average Precision";
    public static final String ONSET_DETECTION_AVG_RECALL = "Average Recall";
    public static final String ONSET_DETECTION_AVG_FMEASURE = "Average F-Measure";
}
