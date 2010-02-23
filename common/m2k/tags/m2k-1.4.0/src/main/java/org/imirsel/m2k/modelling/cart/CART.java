/*
 * CART.java
 *
 * Classification and Regression Tree (Leo Breiman et al.)
 */

package org.imirsel.m2k.modelling.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.imirsel.m2k.modelling.SignalClassifier;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.text.DecimalFormat;
/**
 * Container for a simple CART Tree (CART Leo Breiman et al.)
 * @author Kris West
 */
public class CART implements SignalClassifier {
    /** Constant definition for Gini index splittingCriteria */
    public final static int GINI = 0;
    /** Constant definition for Entropy splittingCriteria */
    public final static int ENTROPY = 1;
    
    /** Constant definition for left path*/
    public final static char CHARLEFT = '0';
    /** Constant definition for right path*/
    public final static char CHARRIGHT = '1';
    
    /** The minimum percentage of the input data that must be present at a node in order to split it **/
    double thresholdPercent = 0.0001;
    /** The actual number of vectors to be used as the minimum size for a split threshold **/
    public int threshold = 1;
    /** The actual number of vectors to be used as the minimum size for a split threshold **/
    public int nodethreshold = 1;
    
    /** Root node */
    public CARTTreeNode root = null;
    /** Splitting criteria used to build tree */
    public int splittingCriteria = ENTROPY;
    /** The classifier name. **/
    public static final String ClassifierName = "CART Tree";
    /** A flag indicating whether a classifier has been trained or not. **/
    public boolean isTrained = false;
    /** ArrayList of class names. **/
    public List classNames;
    /** determines whether debug info is output */
    public boolean verbose = false;
    /** Determines whether any probabilities output are normalised by the prior probabilites (useful for variable length segments */
    private boolean normaliseProbsByPriors = true;
    /** Determines whether single data vectors or whole Signal Objects are used to prune the tree. */
    public boolean pruneWithVectors = true;
    
    private ArrayList signalStore = null;
    private ArrayList nodesToSplit = null;
    
    private boolean abort = false;
    
    /**
     * Creates a new instance of CART
     * @param splitCriteria Criteria to use evaluate splits.
     * @param thresholdPercent_ The percentage of the total dataset that must present at a node in order to 
     * split it.
     * @param verbose_ Determines whether debugging output is sent to the console.
     */
    public CART(int splitCriteria, double thresholdPercent_, boolean verbose_) {
        isTrained = false;
        verbose = verbose_;
        classNames = null;
        splittingCriteria = splitCriteria;
        thresholdPercent = thresholdPercent_;
    }
    
    /**
     * Copy constructor
     * @param oldClassifier The <code>CART</code> to be copied
     */
    public CART(CART oldClassifier) {
        this.isTrained = oldClassifier.isTrained;
        this.classNames = oldClassifier.classNames;
        this.splittingCriteria = oldClassifier.splittingCriteria;
        this.threshold = oldClassifier.threshold;
        this.verbose = oldClassifier.verbose;
        this.normaliseProbsByPriors = oldClassifier.normaliseProbsByPriors;
        this.root = oldClassifier.root.duplicateTree(this);
        
    }
    
    /**
     * Returns a String representing the name of the classifier.
     * @return a String representing the name of the classifier
     */
    public String getClassifierName() {
        return ClassifierName;
    }
    
    /**
     * Returns a flag indicating whether a classifier has been trained or not.
     * @return a flag indicating whether a classifier has been trained or not.
     */
    public boolean isTrained() {
        return this.isTrained;
    }
    
    /**
     * Sets a flag indicating whether a classifier has been trained or not.
     * @param val a flag indicating whether a classifier has been trained or not.
     */
    public void setIsTrained(boolean val) {
        this.isTrained = val;
    }
    
    /**
     * Returns the number of classes that the classifier is trained on. Should
     * return -1 if untrained.
     * @return the number of classes that the classifier is trained on.
     */
    public int getNumClasses() {
        return this.classNames.size();
    }
    
    /**
     * Returns an ArrayList of the class names found in the data that the classifier
     * was trained on. This ArrayList should be ordered such that the integers returned
     * from the classification methods index the correct class names. Should return
     * null if untrained.
     * @return an ArrayList containing the class names.
     */
    public List getClassNames() {
        return this.classNames;
    }
    
    /**
     * Sets the List of the class namesin the data that the classifier
     * will be trained on. This List should be ordered such that the integers returned
     * from the classification methods index the correct class names. `
     * @param classNames_ a List containing the class names.
     */
    public void setClassNames(List classNames_) {
        Collections.sort(classNames_);
        this.classNames = classNames_;
    }
    
    /** Returns a flag indicating whether single data vectors or whole Signal Objects are used to prune the tree. 
        @return flag indicating whether single data vectors or whole Signal Objects are used to prune the tree.*/
    public boolean getPruneWithVectors()
    {
        return this.pruneWithVectors;
    }
    
    /** Sets a flag indicating whether single data vectors or whole Signal Objects are used to prune the tree. 
        @param val flag indicating whether single data vectors or whole Signal Objects are used to prune the tree.*/
    public void setPruneWithVectors(boolean val)
    {
        this.pruneWithVectors = val;
    }
    
    /**
     * Classify a single vector.
     * @param input Vector to classify
     * @return Integer indicating the class.
     */
    public int classifyVector(double[] input) {
        return root.classifyVector(input);
    }
    
    /**
     * Classify a single vector and return a class name.
     * @param input Vector to classify
     * @return A String indicating class.
     */
    public String classify(double[] input) {
        int classNum = this.classifyVector(input);
        return (String)this.getClassNames().get(classNum);
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @param inputSignal The Signal object to calculate the probabilities of
     * class membership for.
     * @return An array of the probabilities of class membership
     */
    public double[] probabilities(Signal inputSignal) {
        double[] probsAccum = new double[this.getNumClasses()];
        for (int i=0;i<inputSignal.getNumRows();i++)
        {
            double[] temp = root.probabilities(inputSignal.getDataRow(i));
            for (int j=0;j<this.getNumClasses();j++)
            {
                 probsAccum[j] += Math.log(temp[j]);
            }
        }
        
        if(normaliseProbsByPriors)
        {
            for (int i=0;i<this.getNumClasses();i++)
            {
                double prior = Math.log((((double)root.numExamplesPerClass[i]))/((double)root.numExamples));
                double equalPrior = Math.log(((((double)root.numExamples))/((double)this.getNumClasses()))/((double)root.numExamples));
                probsAccum[i] = (probsAccum[i] - prior) + equalPrior;
            }
        }
        
        //normalise to range 0:1
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (int j=0;j<this.getNumClasses();j++)
        {
            if(probsAccum[j] > max)
            {
                max = probsAccum[j];
            }
            if(probsAccum[j] < min)
            {
                min = probsAccum[j];
            }
        }
        for (int j=0;j<this.getNumClasses();j++)
        {
            probsAccum[j] -= min;
            probsAccum[j] /= (max - min);
        }
        //normalise to sum to 1
        double total = 0.0;
        for (int j=0;j<this.getNumClasses();j++)
        {
            total += probsAccum[j];
        }
        if(verbose){
            DecimalFormat dec = new DecimalFormat();
            dec.setMaximumFractionDigits(3);
            if(this.verbose){
                System.out.print("Probabilities:" );
            }
            
            for (int j=0;j<this.getNumClasses();j++)
            {
                probsAccum[j] /= total;
                if(verbose){
                    System.out.print(" " + dec.format(probsAccum[j]));
                }
            }
            if(verbose){
                try{
            
                System.out.println("\t" + inputSignal.getStringMetadata(Signal.PROP_FILE_LOCATION));
                }catch(noMetadataException nme)
                {

                }
            }
        }
        
        return probsAccum;
    }
    
    /**
     * Calculates the probability of class membership of a single vector.
     * @param input The vector to calculate probabilities of class membership for.
     * @return An array of the probabilities of class membership.
     */
    public double[] probabilities(double[] input){
        double[] probs = root.probabilities(input);
        if(normaliseProbsByPriors)
        {
            for (int i=0;i<this.getNumClasses();i++)
            {
                double prior = Math.log((((double)root.numExamplesPerClass[i]))/((double)root.numExamples));
                double equalPrior = Math.log(((((double)root.numExamples))/((double)this.getNumClasses()))/((double)root.numExamples));
                probs[i] = (probs[i] - prior) + equalPrior;
            }
        }
        return probs;
    }
    
    /**
     * Classifies the input Signal object and outputs the label of the output class
     * @param inputSignal The Signal object to classify
     * @return The String label of the output class
     */
    public String classify(Signal inputSignal) {
        if (this.getNumClasses() < 2) {
            throw new RuntimeException("Can't perform classification, number of classes is less then 2! (" + this.getNumClasses() + ")");
        }
        //Pick most likely class
        double max = Double.NEGATIVE_INFINITY;
        int outputClass = -1;
        double[] probs = this.probabilities(inputSignal);
        if ((probs.length==0)||(probs.length!=this.getNumClasses())) {
            throw new RuntimeException("An inappropiate number of probabilities was returned!\nprobs.length: " + probs.length + ", numClasses: " + this.getNumClasses());
        }
        for(int i = 0; i<probs.length; i++) {
            if (probs[i] >= max) {
                max = probs[i];
                outputClass = i;
            }
        }
        return (String)this.getClassNames().get(outputClass);
    }
    
    /**
     * Trains the classifier model on the array of Signal objects.
     * @param inputData The array of Signal Objects to train the classifier on.
     * @throws noMetadataException Thrown if there is no class metadata to train the classifier with.
     */
    public void train(Signal[] inputData) throws noMetadataException {
        abort = false;
        ArrayList theClassNames = new ArrayList();
        for (int i=0;i<inputData.length;i++) {
            if (theClassNames.contains(inputData[i].getStringMetadata(Signal.PROP_CLASS)) == false) {
                //System.out.println("multiClassLinearDiscriminantAnalysis: Adding class: " + signals[i].getStringMetadata(Signal.PROP_CLASS));
                theClassNames.add(inputData[i].getStringMetadata(Signal.PROP_CLASS));
            }
        }
        Collections.sort(theClassNames);
        this.setClassNames(theClassNames);
        
        root = new CARTTreeNode(inputData, "r", this);
        
        //count number of vectors 
        int numVecs = 0;
        for (int i=0;i<inputData.length;i++)
        {
            numVecs += inputData[i].getNumRows();
        }
        
        if(verbose){
            System.out.println("Creating root node (" + numVecs + " vectors in " + inputData.length + " Signal Objects, impurity: " + root.impurity + ") and starting splitting process...");
        }
        threshold = Math.round((float)(this.thresholdPercent * (double)numVecs));
        
        nodethreshold = threshold / 5;
        if (threshold < 1)
        {
            threshold = 1;
        }
        if (nodethreshold < 1)
        {
            nodethreshold = 1;
        }
        
        signalStore = new ArrayList();
        nodesToSplit = new ArrayList();
        
        
        nodesToSplit.add(root);
        signalStore.add(inputData);
        if (!verbose){
            inputData = null;
        }
        
        SplitThread[] workerThreads = new SplitThread[2];
        boolean cycle = true;
        
        while(cycle) {
            if (abort){
                this.setIsTrained(false);
                signalStore = null;
                nodesToSplit = null;
                root = null;
                return;
            }
            if (nodesToSplit.size() > 0) {
                
                if ((workerThreads[0] == null) || (!workerThreads[0].isAlive())) {
                    SplitThread theThread = getNextSplitJob();
                    workerThreads[0] = theThread;
                    workerThreads[0].start();
                }else if((workerThreads[1] == null) || (!workerThreads[1].isAlive())) {
                    SplitThread theThread = getNextSplitJob();
                    workerThreads[1] = theThread;
                    workerThreads[1].start();
                }else{
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                
            }else{
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            
            
            cycle = false;
            if (workerThreads[0] != null){
                if (workerThreads[0].isAlive()) {
                    cycle = true;
                }
            }
            if (workerThreads[1] != null){
                if (workerThreads[1].isAlive()) {
                    cycle = true;
                }
            }
            if (nodesToSplit.size() > 0){
                cycle = true;
            }
        }
        /*while(nodesToSplit.size() > 0) {
            CARTTreeNode newNode = (CARTTreeNode)nodesToSplit.remove(0);
            Signal[] data = (Signal[])signalStore.remove(0);
            //int[][] outputPartition = null;
            // Do not split if pure node
            if (newNode.impurity == 0) {
                newNode.leafNodeType = newNode.PURE;
                if(verbose){
                    System.out.println("Branch terminated - Pure node (" + newNode.numExamples + " vectors)");
                }
                newNode.isLeafNode = true;
                
            }else if (newNode.numExamples < threshold) {
                newNode.leafNodeType = newNode.TOO_SMALL;
                newNode.isLeafNode = true;
                if(verbose){
                    System.out.println("Branch terminated - Node too small (" + newNode.numExamples + " examples, required " + threshold + ")");
                }
            }
            if (newNode.isLeafNode == false) {
                //Split node
                int[][] theSplit = newNode.split(data);
                
                if (theSplit == null) {
                    newNode.leafNodeType = newNode.UNSPLITABLE;
                    if(verbose){
                        System.out.println("Branch terminated - Unsplitable (" + newNode.numExamples + " vectors)");
                    }
                    newNode.isLeafNode = true;
                } else{
                    int[] signalPartition = new int[theSplit.length];
                    int leftDataCount = 0;
                    int rightDataCount = 0;
                    int leftCount = 0;
                    int rightCount = 0;
                    for (int i = 0; i < theSplit.length; i++) {
                        boolean hasLeft = false;
                        boolean hasRight = false;
                        for (int j = 0; j < theSplit[i].length; j++) {
                            if (theSplit[i][j] == newNode.LEFT) {
                                hasLeft = true;
                                signalPartition[i] = newNode.LEFT;
                                if ((hasLeft == true) && (hasRight == true)) {
                                    signalPartition[i] = 2;
                                    break;
                                }
                            } else if (theSplit[i][j] == newNode.RIGHT){
                                hasRight = true;
                                signalPartition[i] = newNode.RIGHT;
                                if ((hasLeft == true) && (hasRight == true)) {
                                    signalPartition[i] = 2;
                                    break;
                                }
                            }else {//Something went wrong
                                throw new RuntimeException("Corrupted split returned!");
                            }
                        }
                        
                        if (hasLeft == true) {
                            leftDataCount++;
                        }
                        if (hasRight == true) {
                            rightDataCount++;
                        }
                        if ((!hasLeft)&&(!hasRight)){
                            signalPartition[i] = Integer.MAX_VALUE;
                            throw new RuntimeException("Corrupted split returned!");
                        }
                    }
                    
                    //init temp storage
                    Signal[] leftData = new Signal[leftDataCount];
                    String leftID = (newNode.NodeID + CHARLEFT);
                    
                    //separate data according to partition
                    leftCount = 0;
                    for (int i = 0; i < signalPartition.length; i++) {
                        if ((signalPartition[i] == newNode.LEFT)||(signalPartition[i] == 2)) {
                            //System.out.println("leftCount: " + leftCount + ", leftDataCount: " + leftDataCount);
                            leftData[leftCount] = new Signal(data[i],theSplit[i],newNode.LEFT);
                            leftCount++;
                        }
                    }
                    
                    //create and attach new left Node
                    CARTTreeNode newLeft = new CARTTreeNode(leftData, leftID, this);
                    //traverse to position in tree
                    CARTTreeNode traversalNode = root;
                    for (int i = 1; i < (newLeft.NodeID.length()-1); i++) {
                        if (newLeft.NodeID.charAt(i) == CHARLEFT) {
                            traversalNode = traversalNode.leftTree;
                        } else {
                            traversalNode = traversalNode.rightTree;
                        }
                    }
                    traversalNode.attachSubtree(newNode.LEFT, newLeft);
                    //add data to stores
                    signalStore.add(leftData);
                    nodesToSplit.add(newLeft);
                    
                    if(this.verbose) {
                        System.out.println("  created node, node id: " + newLeft.NodeID + ", impurity: " + newLeft.impurity + ", size: " + newLeft.numExamples + ", open branches: " + nodesToSplit.size());
                    }
                    
                    //init temp storage
                    Signal[] rightData = new Signal[rightDataCount];
                    String rightID = (newNode.NodeID + CHARRIGHT);
                    
                    //separate data according to partition
                    rightCount = 0;
                    for (int i = 0; i < signalPartition.length; i++) {
                        if ((signalPartition[i] == newNode.RIGHT)||(signalPartition[i] == 2)) {
                            rightData[rightCount] = new Signal(data[i],theSplit[i],newNode.RIGHT);
                            rightCount++;
                        }
                    }
                    data = null;
                    //create and attach new right Node
                    CARTTreeNode newRight = new CARTTreeNode(rightData, rightID, this);
                    //traverse to position in tree
                    traversalNode = root;
                    for (int i = 1; i < (newLeft.NodeID.length()-1); i++) {
                        if (newLeft.NodeID.charAt(i) == CHARLEFT) {
                            traversalNode = traversalNode.leftTree;
                        } else {
                            traversalNode = traversalNode.rightTree;
                        }
                    }
                    traversalNode.attachSubtree(newNode.RIGHT, newRight);
                    //add data to stores
                    signalStore.add(rightData);
                    nodesToSplit.add(newRight);
                    if(this.verbose) {
                        System.out.println("  created node, node id: " + newRight.NodeID + ", impurity: " + newRight.impurity + ", size: " + newRight.numExamples + ", open branches: " + nodesToSplit.size());
                    }
                }
            } else {//Already a leaf node
                data = null;
            }
        }*/
        if(this.verbose) {
            double resubAcc = 100.0 * (1.0 - ((double)root.getTreeMisclassifications() / (double)root.numExamples));
            int correct = 0;
            for (int i=0;i<inputData.length;i++)
            {
                if (this.classify(inputData[i]).equals(inputData[i].getStringMetadata(Signal.PROP_CLASS)))
                {
                    correct++;
                }
            }
            double resubSigAcc = 100.0 * ((double)correct) / ((double)inputData.length);
            DecimalFormat dec = new DecimalFormat();
            dec.setMaximumFractionDigits(3);
            System.out.println("  FINISHED TRAINING CART TREE - " + root.getNumLeaves() + " leaf nodes, training accuracy (vectors) " + dec.format(resubAcc) + "% (Signals) " + dec.format(resubSigAcc) + "%, entropy of leaf nodes " + root.getEntropy());
        }
        this.setIsTrained(true);
        signalStore = null;
        nodesToSplit = null;
    }
    
    public synchronized SplitThread getNextSplitJob(){
        CARTTreeNode newNode = (CARTTreeNode)nodesToSplit.remove(0);
        Signal[] data = (Signal[])signalStore.remove(0);
        return new SplitThread(newNode, data);
    }
    
    private void runSplitIteration(CARTTreeNode newNode, Signal[] data) throws noMetadataException {
        // Do not split if pure node
        if (newNode.impurity == 0) {
            newNode.leafNodeType = newNode.PURE;
            if(verbose){
                System.out.println("Branch terminated - Pure node (" + newNode.numExamples + " vectors)");
            }
            newNode.isLeafNode = true;
            return;
        }
        //Do not split if too small
        if (newNode.numExamples < threshold)
        {
            if(verbose){
                System.out.println("Branch terminated - Node too small (" + newNode.numExamples + " vectors)");
            }
            newNode.isLeafNode = true;
            return;
        }
        
        //Split node
        int[][] theSplit = newNode.split(data,nodethreshold);
        
        if (theSplit == null) {
            newNode.leafNodeType = newNode.UNSPLITABLE;
            if(verbose){
                System.out.println("Branch terminated - Unsplitable (" + newNode.numExamples + " vectors)");
            }
            newNode.isLeafNode = true;
            return;
        } else{
            int[] signalPartition = new int[theSplit.length];
            int leftDataCount = 0;
            int rightDataCount = 0;
            int leftCount = 0;
            int rightCount = 0;
            for (int i = 0; i < theSplit.length; i++) {
                boolean hasLeft = false;
                boolean hasRight = false;
                for (int j = 0; j < theSplit[i].length; j++) {
                    if (theSplit[i][j] == newNode.LEFT) {
                        hasLeft = true;
                        signalPartition[i] = newNode.LEFT;
                        if ((hasLeft == true) && (hasRight == true)) {
                            signalPartition[i] = 2;
                            break;
                        }
                    } else if (theSplit[i][j] == newNode.RIGHT){
                        hasRight = true;
                        signalPartition[i] = newNode.RIGHT;
                        if ((hasLeft == true) && (hasRight == true)) {
                            signalPartition[i] = 2;
                            break;
                        }
                    }else {
                        signalPartition[i] = Integer.MAX_VALUE;
                    }
                }
                
                if (hasLeft == true) {
                    leftDataCount++;
                }
                if (hasRight == true) {
                    rightDataCount++;
                }
                if ((!hasLeft)&&(!hasRight)){
                    signalPartition[i] = Integer.MAX_VALUE;
                }
            }
            
            synchronized(this){
                
                //init temp storage
                Signal[] leftData = new Signal[leftDataCount];
                String leftID = (newNode.NodeID + CHARLEFT);
                
                //separate data according to partition
                leftCount = 0;
                for (int i = 0; i < signalPartition.length; i++) {
                    if ((signalPartition[i] == newNode.LEFT)||(signalPartition[i] == 2)) {
                        //System.out.println("leftCount: " + leftCount + ", leftDataCount: " + leftDataCount);
                        leftData[leftCount] = new Signal(data[i],theSplit[i],newNode.LEFT);
                        leftCount++;
                    }
                }
                
                //create and attach new left Node
                CARTTreeNode newLeft = new CARTTreeNode(leftData, leftID, this);
                //traverse to position in tree
                CARTTreeNode traversalNode = root;
                for (int i = 1; i < (newLeft.NodeID.length()-1); i++) {
                    if (newLeft.NodeID.charAt(i) == CHARLEFT) {
                        traversalNode = traversalNode.leftTree;
                    } else {
                        traversalNode = traversalNode.rightTree;
                    }
                }
                traversalNode.attachSubtree(newNode.LEFT, newLeft);
                
                //add data to stores
                signalStore.add(leftData);
                nodesToSplit.add(newLeft);
                
                if(this.verbose) {
                    System.out.println("  created node, node id: " + newLeft.NodeID + ", size: " + newLeft.numExamples + ", open branches: " + nodesToSplit.size());
                }
                
                //init temp storage
                Signal[] rightData = new Signal[rightDataCount];
                String rightID = (newNode.NodeID + CHARRIGHT);
                
                //separate data according to partition
                rightCount = 0;
                for (int i = 0; i < signalPartition.length; i++) {
                    if ((signalPartition[i] == newNode.RIGHT)||(signalPartition[i] == 2)) {
                        rightData[rightCount] = new Signal(data[i],theSplit[i],newNode.RIGHT);
                        rightCount++;
                    }
                }
                data = null;
                //create and attach new right Node
                CARTTreeNode newRight = new CARTTreeNode(rightData, rightID, this);
                //traverse to position in tree
                traversalNode = root;
                for (int i = 1; i < (newLeft.NodeID.length()-1); i++) {
                    if (newLeft.NodeID.charAt(i) == CHARLEFT) {
                        traversalNode = traversalNode.leftTree;
                    } else {
                        traversalNode = traversalNode.rightTree;
                    }
                }
                traversalNode.attachSubtree(newNode.RIGHT, newRight);
                //add data to stores
                signalStore.add(rightData);
                nodesToSplit.add(newRight);
                if(this.verbose) {
                    System.out.println("  created node, node id: " + newRight.NodeID + ", size: " + newRight.numExamples + ", open branches: " + nodesToSplit.size());
                }
            }
            
        }
    }
    
    
    
    /**
     * Returns a nested sequence of pruned trees, which are evaluated on the supplied
     * Signal array. The best is retained.
     * @param evalData Data to evaluate the quality of pruned trees on.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public void pruneTree(Signal[] evalData) throws noMetadataException
    {
        CARTTreeNode bestTree = root;
        CARTTreeNode currentTree = root;
        double bestScore = Double.NEGATIVE_INFINITY;
        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(5);
        
        boolean continuePruning = true;
        while (continuePruning)
        {
            //evaluate
            double score = 0;
            double vectorScore = currentTree.evaluateTreeWithVectors(evalData);
            double signalScore = currentTree.evaluateTreeWithSignals(evalData);

            if (pruneWithVectors)
            {
                score = vectorScore;
            }else{
                score = signalScore;
            }
            if (verbose)
            {
                System.out.println("\tTree size: " + currentTree.getNumLeaves() + ", entropy: " + dec.format(currentTree.getEntropy()) + ", Vector accuracy: " + dec.format(vectorScore) + ", Signal accuracy: " + dec.format(signalScore));
            }
            
            if (score >= bestScore)
            {
                bestTree = currentTree;
                bestScore = score;
                currentTree = bestTree.duplicateTree(this);
            }
            continuePruning = currentTree.pruneTree();
        }
        if(verbose)
        {
            System.out.println("\tFinished Pruning, selected: Tree size: " + bestTree.getNumLeaves() + ", entropy: " + dec.format(bestTree.getEntropy()) + ", score: " + dec.format(bestScore));
        }
        root = bestTree;
        
    }
    
    public class SplitThread extends Thread {
            CARTTreeNode newNode;
            Signal[] data;
            
            public SplitThread(CARTTreeNode newNode_, Signal[] data_) {
                newNode = newNode_;
                data = data_;
            }
            
            public void run() {
                try {
                    runSplitIteration(newNode, data);
                    newNode = null;
                    data = null;
                } catch (noMetadataException ex) {
                    System.out.println("Split failed!\n" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }

    public boolean isAbort() {
        return abort;
    }

    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    public boolean isNormaliseProbsByPriors() {
        return normaliseProbsByPriors;
    }

    public void setNormaliseProbsByPriors(boolean normaliseProbsByPriors) {
        this.normaliseProbsByPriors = normaliseProbsByPriors;
    }
    
}
