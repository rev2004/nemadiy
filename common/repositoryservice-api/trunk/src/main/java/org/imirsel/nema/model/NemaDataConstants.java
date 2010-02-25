package org.imirsel.nema.model;

public class NemaDataConstants {

    //Metadata key constants
    /** Constant definition for metadata key: column label     */
    public final static String PROP_COLUMN_LABELS = "columnLabels";
    /** Constant definition for metadata key: file location     */
    public final static String PROP_FILE_LOCATION  = "fileLocation";
    /** Constant definition for metadata key: directory name     */
    public final static String PROP_DIRECTORY_NAME  = "dirName";

    /** Constant definition for performance metadata.     */
    public final static String PROP_PERF = "Performance";
    /** Constant definition for algorithm name (used as an identifier in evaluations).     */
    public final static String PROP_ALG_NAME = "Algorithm name";
    /** Constant definition for      */
    public final static String PATH_TO_ARTIST_MAP = "Path to artist map";
    
    
    //Evaluation results constants
    /** Constant definition for metadata key: Evaluation report*/
    public static final String SYSTEM_RESULTS_REPORT = "Single system rsult evaluation report";
    
    
    //File format constants
    /** Constant definition for section divider used in ASCII file     */
    public final static String DIVIDER = "-===-";
    /** Constant definition for SEPARATOR used in ASCII file     */
    public final static String SEPARATOR = "\t";
    /** Constant definition for header used in ASCII file     */
    public final static String fileHeader = "M2K NemaData (8th Feb 2010)";
    
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
    
    
    //Tag classification evaluator constants
    /** Constant definition for tag classification data in the form of a 
     * <code>HashMap<String,HashSet<String>></code> - mapping paths to a set
     * of relevant tags. */
    public static final String TAG_BINARY_RELEVANCE_MAP = "Tag classification binary relevance map";
    /** Constant definition for the list of tag names appearing in the data in 
     * the form of a <code>HashSet<String></code>. */
    public static final String TAG_NAME_SET = "Tag name set";
    /** Constant definition for tag classification data in the form of a 
     * <code>HashMap<String,HashMap<String,Double>></code> - mapping paths
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
    
}
