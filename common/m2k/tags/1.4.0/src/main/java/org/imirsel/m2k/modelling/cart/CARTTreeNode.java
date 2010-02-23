package org.imirsel.m2k.modelling.cart;

import java.io.Serializable;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.util.ArrayList;
import java.util.Collections;



/**
 * Class that implements a single node in a simple binary CART Tree
 * (CART Leo Breiman et al.).
 * @author Kris West
 */
public class CARTTreeNode implements Serializable {
    /** Constant definitions for <code>leafNodeType</code> pure node.*/
    public final static int PURE = 0;
    /** Constant definitions for <code>leafNodeType</code> too small to split.*/
    public final static int TOO_SMALL = 1;
    /** Constant definitions for <code>leafNodeType</code> identical measurement vectors.*/
    public final static int IDENTICAL_VECTORS = 2;
    /** Constant definitions for <code>leafNodeType</code> unsplitable node.*/
    public final static int UNSPLITABLE = 3;
    /** Constant definitions for <code>leafNodeType</code> pruned node.*/
    public final static int PRUNED = 4;
    /** Constant definitions for <code>leafNodeType</code> non-leaf node*/
    public final static int NON_LEAF_NODE = 5;
    
    /** Constant definition for left subtree. */
    public final static int LEFT = 0;
    /** Constant definition for right subtree. */
    public final static int RIGHT = 1;
    
    /** Used to identify terminal nodes. */
    public boolean isLeafNode;
    /** used for calculating statistics. */
    public int leafNodeType;
    /** Denotes location in tree, somewhat inefficient, could be calculated instead of stored. */
    public String NodeID;
    /** Dominant class at node. */
    public int dominantClass;
    /** Total number of examples in node. */
    public int numExamples;
    /** Number of each class in node, used to calculate probabilities. */
    public int [] numExamplesPerClass;
    /** Strength/weakness of node, used for pruning. */
    public double nodeResubstitutionEstimate;
    /** Used to select best split amongst a series of possible splits. */
    public double impurity;
    /** Feature column to split node on. */
    public int splitCol = -1;
    /** Point on feature axis to split node at. */
    public double splitPoint;
    /** The container for the tree. */
    public CART container;
    /** The left child node. */
    public CARTTreeNode leftTree;
    /** The right child node. */
    public CARTTreeNode rightTree;
    
    /**
     * Basic constructor.
     */
    public CARTTreeNode() {
        leftTree = null;
        rightTree = null;
        NodeID = "r";
        container = null;
    }
    
    /**
     * Explicit constructor.
     * @param isLeafNode_ Used to identify terminal nodes.
     * @param leafNodeType_ used for calculating statistics.
     * @param NodeID_ Denotes location in tree, somewhat inefficient, could be calculated instead of stored.
     * @param dominantClass_ Dominant class at node.
     * @param numExamples_ Total number of examples in node.
     * @param numExamplesPerClass_ Number of each class in node, used to calculate probabilities.
     * @param nodeResubstitutionEstimate_ Strength/weakness of node, used for pruning.
     * @param impurity_ Used to select best split amongst a series of possible splits.
     * @param container_ The container for the tree.
     * @param splitCol_ Feature column to split node on.
     * @param splitPoint_ Point on feature axis to split node at.
     */
    public CARTTreeNode( boolean isLeafNode_, int leafNodeType_, String NodeID_, int dominantClass_, int numExamples_, int[] numExamplesPerClass_, double nodeResubstitutionEstimate_, double impurity_, CART container_, int splitCol_, double splitPoint_) {
        isLeafNode = isLeafNode_;
        leafNodeType = leafNodeType_;
        NodeID = NodeID_;
        dominantClass = dominantClass_;
        numExamples = numExamples_;
        numExamplesPerClass = numExamplesPerClass_;
        nodeResubstitutionEstimate = nodeResubstitutionEstimate_;
        impurity = impurity_;
        container = container_;
        leftTree = null;
        rightTree = null;
        splitCol = splitCol_;
        splitPoint = splitPoint_;
    }
    
    /**
     * Copy constructor
     * @param newContainer The container for the tree that this node will belong to.
     * @param oldNode The node to copy
     */
    public CARTTreeNode(CARTTreeNode oldNode, CART newContainer) {
        isLeafNode = oldNode.isLeafNode;
        leafNodeType = oldNode.leafNodeType;
        NodeID = oldNode.NodeID;
        dominantClass = oldNode.dominantClass;
        numExamples = oldNode.numExamples;
        numExamplesPerClass = oldNode.numExamplesPerClass;
        nodeResubstitutionEstimate = oldNode.nodeResubstitutionEstimate;
        impurity = oldNode.impurity;
        container = newContainer;
        leftTree = null;
        rightTree = null;
        splitCol = oldNode.splitCol;
        splitPoint = oldNode.splitPoint;
    }
    
    /**
     * Main constructor method, uses data from matrix to populate fields.
     * @param signals Data to be used to populate node.
     * @param nodeID The <code>NodeID</code> of the new tree node.
     * @param container_ The <code>CART</code> for this node.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public CARTTreeNode(Signal[] signals, String nodeID, CART container_) throws noMetadataException {
        isLeafNode = false;
        container = container_;
        numExamplesPerClass = new int[container.getNumClasses()];
        //numExamples = 0;
        for (int i = 0; i < signals.length; i++) {
            numExamples += signals[i].getNumRows();
            numExamplesPerClass[container.getClassNames().indexOf(signals[i].getStringMetadata(Signal.PROP_CLASS))] += signals[i].getNumRows();
        }
        
        int max = -1;
        dominantClass = 0;
        for (int i = 0; i < container.getNumClasses(); i++) {
            if (numExamplesPerClass[i] > max) {
                max = numExamplesPerClass[i];
                dominantClass = i;
            }
        }
        NodeID = nodeID;
        
        if (container.splittingCriteria == container.GINI) {
            impurity = this.calcGiniIndex(numExamplesPerClass, numExamples);
        } else if(container.splittingCriteria == container.ENTROPY) {
            impurity = this.calcEntropyIndex(numExamplesPerClass, numExamples);
        }else{
            throw new RuntimeException("Unknown splitting criteria!");
        }
        
        int misclassifications = 0;
        for (int i = 0; i < container.getNumClasses(); i++) {
            if (dominantClass != i) {
                misclassifications += numExamplesPerClass[i];
            }
        }
        nodeResubstitutionEstimate = (double)misclassifications / (double)numExamples;
    }
    
    /**
     * Recursively duplicates the tree
     * @param aContainer The container for the duplicated nodes.
     * @return the duplicate tree
     */
    public synchronized CARTTreeNode duplicateTree(CART aContainer) {
        CARTTreeNode newTree = new CARTTreeNode(this,aContainer);
        if (isLeafNode == false) {
            newTree.leftTree = leftTree.duplicateTree(aContainer);
            newTree.rightTree = rightTree.duplicateTree(aContainer);
        }
        return newTree;
    }
    
    /**
     * Returns number of leaf nodes beneath this node
     * @return The number of leaf nodes beneath this node
     */
    public int getNumLeaves() {
        if (isLeafNode == true) {
            return 1;
        } else {
            return (leftTree.getNumLeaves() + rightTree.getNumLeaves());
        }
    }
    
    /**
     * Returns the entropy of the leaf nodes beneath this node. Note this will
     * only be correct at the root node of the tree as the probabilities used
     * are based on the root node's number of examples.
     * @return The entropy of leaf nodes beneath this node
     */
    public double getEntropy() {
        if (isLeafNode == true) {
            double prob = (((double)this.numExamples) / ((double)this.container.root.numExamples));
            return -(prob * Math.log(prob));
        } else {
            return (leftTree.getEntropy() + rightTree.getEntropy());
        }
    }
    
    /**
     * Returns the number of training examples misclassified by this node and its subtrees
     * @return The number of training examples misclassified by this node and its subtrees
     */
    public int getTreeMisclassifications() {
        if (isLeafNode == true) {
            int misclassifications = 0;
            for (int i = 0; i < this.container.getNumClasses(); i++) {
                if (dominantClass != i) {
                    misclassifications += numExamplesPerClass[i];
                }
            }
            return misclassifications;
        } else {
            return (leftTree.getTreeMisclassifications() + rightTree.getTreeMisclassifications());
        }
    }
    
    /**
     * Function G (see Classification and Regression Trees by Leo Breiman et al)
     * used to identify the weakest subtree
     * @return G
     */
    public double G() {
        return ((nodeResubstitutionEstimate - (getTreeMisclassifications()/numExamples))/(getNumLeaves()-1));
    }
    
    /**
     * Find the subtree of node that reduces node resubstitution estimate by the least.
     * @return A <code>treeScore</code> identifying the weakest subtree.
     */
    public treeScore findWeakestSubtree() {
        if (isLeafNode == true) {
            throw new RuntimeException("Can't find weakest subtree of a Leaf node (it has no subtrees)!");
        }
        
        double score = G();
        treeScore currentLowestScore = new treeScore(NodeID,score);
        
        if (leftTree.isLeafNode == false) {
            treeScore weakestLeft = leftTree.findWeakestSubtree();
            if (weakestLeft.score < currentLowestScore.score) {
                currentLowestScore.score = weakestLeft.score;
                currentLowestScore.ID = weakestLeft.ID;
            }
        }
        
        if (rightTree.isLeafNode == false) {
            treeScore weakestRight = rightTree.findWeakestSubtree();
            if (weakestRight.score < currentLowestScore.score) {
                currentLowestScore.score = weakestRight.score;
                currentLowestScore.ID = weakestRight.ID;
            }
        }
        
        return currentLowestScore;
    }
    
    /**
     * Recursively delete the subtrees of this node.
     */
    public void pruneSubtrees() {
        if (isLeafNode == true) {
            throw new RuntimeException("Can't prune subtrees of a Leaf node (it has no subtrees)!");
        }
        
        if (leftTree.isLeafNode == false) {
            leftTree.pruneSubtrees();
        }
        if (rightTree.isLeafNode == false) {
            rightTree.pruneSubtrees();
        }
        leftTree = null;
        rightTree = null;
        isLeafNode = true;
        leafNodeType = PRUNED;
    }
    
    /**
     * Identifies the weakest node in the subtree and prunes its subtrees
     * NOTE: duplicate tree before pruning if original is to be retained
     * and only call on root of tree.
     * @return true if the tree was succesfully pruned, false if we are at
     * the root or no further subtrees to prune.
     */
    public boolean pruneTree() {
        if (!NodeID.equals("r")) {
            throw new RuntimeException("Tried to call pruneTree at a subtree, it must be called at the root of the tree!");
        }
        treeScore weakestNode = findWeakestSubtree();
        if (weakestNode.ID.equals("r")) {
            return false;
        } else {
            // traverse to weakest node using the NodeIDs
            CARTTreeNode node = this;
            for (int i = 1; i < weakestNode.ID.length(); i++) {
                if (weakestNode.ID.charAt(i) == '0') {
                    node = node.leftTree;
                } else {
                    node = node.rightTree;
                }
            }
            node.pruneSubtrees();
            return true;
        }
    }
    
    /**
     * Evaluates a tree based on an array of Signal Objects, used in pruning.
     * NOTE: Only call on root of tree.
     * @return Classification accuracy of individual frames taken from the
     * Signal Objects(%).
     * @param evalData Data to evaluate the tree on.
     * @throws org.imirsel.m2k.util.noMetadataException Thrown if required metadata is not found.
     */
    public double evaluateTreeWithVectors(Signal[] evalData) throws noMetadataException {
        if (!NodeID.equals("r")) {
            throw new RuntimeException("Tried to call evaluateTreeWithVectors(Signal[] evalData) at a subtree, it must be called at the root of the tree!");
        }
        int correct = 0;
        int total = 0;
        for (int i=0;i<evalData.length;i++){
            total += evalData[i].getNumRows();
            String truth = evalData[i].getStringMetadata(Signal.PROP_CLASS);
            for (int j=0;j<evalData[i].getNumRows();j++) {
                if (truth.equals(this.classify(evalData[i].getDataRow(j)))) {
                    correct++;
                }
            }
        }
        double acc = ((double)correct)/((double)total);
        return acc;
    }
    
    /**
     * Evaluates a tree based on an array of Signal Objects, used in pruning.
     * NOTE: Only call on root of tree.
     * @return Classification accuracy of whole Signal Objects (%).
     */
    public double evaluateTreeWithSignals(Signal[] evalData) throws noMetadataException {
        if (!NodeID.equals("r")) {
            throw new RuntimeException("Tried to call evaluateTreeWithVectors(Signal[] evalData) at a subtree, it must be called at the root of the tree!");
        }
        
        int correct = 0;
        for (int i=0;i<evalData.length;i++){
            String truth = evalData[i].getStringMetadata(Signal.PROP_CLASS);
            if (truth.equals(this.classify(evalData[i]))) {
                correct++;
            }
        }
        double acc = ((double)correct)/((double)evalData.length);
        return acc;
    }
    
    
    /**
     * Attach a subtree to this node
     * @param side Identifies which side to attach tree
     * @param subtree The subtree to attach
     */
    public synchronized void attachSubtree(int side, CARTTreeNode subtree) {
        if (side == LEFT) {
            leftTree = subtree;
        } else {
            rightTree = subtree;
        }
    }
    
    /**
     * Classify a vector of novel data
     * @param theVector The data to be classified
     * @return The classification
     */
    public int classifyVector(double[] theVector) {
        if (isLeafNode == true) {
            return dominantClass;
        } else {
            if (theVector[this.splitCol] < this.splitPoint) {
                return leftTree.classifyVector(theVector);
            } else {
                return rightTree.classifyVector(theVector);
            }
        }
    }
    
    /**
     * Classify a single vector and return a class name.
     * @param input Vector to classify
     * @return A String indicating class.
     */
    public String classify(double[] input) {
        if (!NodeID.equals("r")) {
            throw new RuntimeException("Tried to call classify(double[] input) at a subtree, it must be called at the root of the tree!");
        }
        int classNum = this.classifyVector(input);
        return (String)this.container.getClassNames().get(classNum);
    }
    
    /**
     * Classifies the input Signal object and outputs the label of the output class
     * @param inputSignal The Signal object to classify
     * @return The String label of the output class
     */
    public String classify(Signal inputSignal) {
        if (!NodeID.equals("r")) {
            throw new RuntimeException("Tried to call classify(Signal inputSignal) at a subtree, it must be called at the root of the tree!");
        }
        //Pick most likely class
        double max = Double.NEGATIVE_INFINITY;
        int outputClass = -1;
        double[] probs = this.probabilities(inputSignal);
        if ((probs.length==0)||(probs.length!=this.container.getNumClasses())) {
            throw new RuntimeException("An inappropiate number of probabilities was returned!\nprobs.length: " + probs.length + ", numClasses: " + this.container.getNumClasses());
        }
        for(int i = 0; i<probs.length; i++) {
            if (probs[i] >= max) {
                max = probs[i];
                outputClass = i;
            }
        }
        return (String)this.container.getClassNames().get(outputClass);
    }
    
    public double[] probabilities(double[] theVector) {
        if (isLeafNode == true) {
            //System.out.print("L");
            double[] probs = new double[container.getNumClasses()];
            for (int i = 0; i<container.getNumClasses(); i++) {
                //Smooth probs using Lidstone's law
                probs[i] = ((double)numExamplesPerClass[i] + 0.5) / ((double)numExamples + (0.5 * container.getNumClasses()));
            }
            
            return probs;
        } else {
            //System.out.print(".");
            double[] probs;
            // return probability at leaf node
            if (theVector[this.splitCol] <= this.splitPoint) {
                probs = leftTree.probabilities(theVector);
            } else {
                probs = rightTree.probabilities(theVector);
            }
            
            return probs;
        }
    }
    
    /**
     * Calculates the probability of class membership of a Signal Object.
     * @param inputSignal The Signal object to calculate the probabilities of
     * class membership for.
     * @return An array of the probabilities of class membership
     */
    public double[] probabilities(Signal inputSignal) {
        if (!NodeID.equals("r")) {
            throw new RuntimeException("Tried to call probabilities(Signal inputSignal) at a subtree, it must be called at the root of the tree!");
        }
        double[] probsAccum = new double[this.container.getNumClasses()];
        for (int i=0;i<inputSignal.getNumRows();i++) {
            double[] temp = this.probabilities(inputSignal.getDataRow(i));
            for (int j=0;j<this.container.getNumClasses();j++) {
                probsAccum[j] += Math.log(temp[j]);
            }
        }
        
        if(this.container.isNormaliseProbsByPriors()) {
            for (int i=0;i<this.container.getNumClasses();i++) {
                double prior = Math.log((((double)this.numExamplesPerClass[i]))/((double)this.numExamples));
                double equalPrior = Math.log(((((double)this.numExamples))/((double)this.container.getNumClasses()))/((double)this.numExamples));
                probsAccum[i] = (probsAccum[i] - prior) + equalPrior;
            }
        }
        
        //normalise to range 0:1
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (int j=0;j<this.container.getNumClasses();j++) {
            if(probsAccum[j] > max) {
                max = probsAccum[j];
            }
            if(probsAccum[j] < min) {
                min = probsAccum[j];
            }
        }
        for (int j=0;j<this.container.getNumClasses();j++) {
            probsAccum[j] -= min;
            probsAccum[j] /= (max - min);
        }
        //normalise to sum to 1
        double total = 0.0;
        for (int j=0;j<this.container.getNumClasses();j++) {
            total += probsAccum[j];
        }
        
        return probsAccum;
    }
    
    public int[] probabilityCounts(double[] theVector) {
        if (isLeafNode == true) {
            return numExamplesPerClass;
        } else {
            int[] counts;
            // return counts at leaf node
            if (theVector[this.splitCol] <= this.splitPoint) {
                counts = leftTree.probabilityCounts(theVector);
            } else {
                counts = rightTree.probabilityCounts(theVector);
            }
            return counts;
        }
    }
    
    /**
     * Split the node
     * @param theData The data to split with the classifier
     * @return A partition indicating whether examples should pass to the left
     * or right child node, null if the node was unsplittable
     */
    public int[][] split(Signal[] theData, int nodeThreshold) throws noMetadataException {
        int bestSplitCol = -1;
        double bestSplitPoint = 0.0;
        double highestChangeInSplittingCriteria = Double.NEGATIVE_INFINITY;
        for (int c=0;c<theData[0].getNumCols();c++) {//Find best column and split
            ArrayList vals = new ArrayList();
            for (int s=0;s<theData.length;s++) {
                int tmpClass = container.classNames.indexOf(theData[s].getStringMetadata(Signal.PROP_CLASS));
                for (int r=0;r<theData[s].getNumRows();r++) {
                    vals.add(new splitVal(tmpClass, theData[s].getData()[c][r]));
                }
            }
            Collections.sort(vals);
            
            //find best split point
            int[] leftNum = new int[container.getNumClasses()];
            int[] rightNum = new int[container.getNumClasses()];
            for(int i=0;i<this.numExamplesPerClass.length;i++)
            {
                leftNum[i] = 0;
                rightNum[i] = this.numExamplesPerClass[i];
            }
            splitVal theVal = (splitVal)vals.get(0);
            rightNum[theVal.ID]--;
            leftNum[theVal.ID]++;
            int left = 0;
            for (int v=1;v<vals.size()-1;v++) {
                rightNum[theVal.ID]--;
                leftNum[theVal.ID]++;
                left++;
                theVal = (splitVal)vals.get(v);
                if (((splitVal)vals.get(v)).val != ((splitVal)vals.get(v+1)).val){
                    double changeInSplittingCriteria = 0.0;

                    if (container.splittingCriteria == container.GINI) {
                        double leftImp = ((double)v / (double)vals.size()) * calcGiniIndex(leftNum, left);
                        double rightImp = ((double)(vals.size() - v) / (double)vals.size()) * calcGiniIndex(rightNum, vals.size() - left);
                        changeInSplittingCriteria = (this.impurity - (leftImp + rightImp));
                    } else //ENTROPY
                    {
                        double leftImp = ((double)v / (double)vals.size()) * calcEntropyIndex(leftNum, left);
                        double rightImp = ((double)(vals.size() - v) / (double)vals.size()) * calcEntropyIndex(rightNum, vals.size() - left);
                        changeInSplittingCriteria = (this.impurity - (leftImp + rightImp));
                    }

                    //System.out.println("\tsplit: " + changeInSplittingCriteria);
                    if ((changeInSplittingCriteria > highestChangeInSplittingCriteria)&&(left > nodeThreshold)&&((vals.size() - left)> nodeThreshold)) {
                        highestChangeInSplittingCriteria = changeInSplittingCriteria;
                        bestSplitCol = c;
                        bestSplitPoint = (((splitVal)vals.get(v-1)).val + ((splitVal)vals.get(v)).val)/2.0;
                        //System.out.println("new best split, index: " + v + ", col: " + c + ", split val: " + bestSplitPoint + ", change in criteria: " + changeInSplittingCriteria);
                    }
                }
            }
           
        }
        if(highestChangeInSplittingCriteria > 0.000001) {
            int[][] bestPartition = new int[theData.length][];
            for (int s=0;s<theData.length;s++) {
                bestPartition[s] = new int[theData[s].getNumRows()];
            }
            this.splitCol = bestSplitCol;
            this.splitPoint = bestSplitPoint;
            boolean hasRight = false;
            boolean hasLeft = false;
            for(int i=0;i<theData.length;i++) {
                for(int j=0;j<theData[i].getNumRows();j++) {
                    if (theData[i].getData()[this.splitCol][j] <= this.splitPoint){
                        bestPartition[i][j] = this.LEFT;
                        hasLeft = true;
                    }else{
                        bestPartition[i][j] = this.RIGHT;
                        hasRight = true;
                    }
                }
            }
            if ((!hasRight)||(!hasLeft))
            {
                String str = "node size: " + this.numExamples + "\nclass sizes of node split: ";
                for (int i=0;i<this.numExamplesPerClass.length;i++)
                {
                    str += this.numExamplesPerClass[i];
                }
                System.out.println("CART: Pointless split produced!\n" + str);
                return null;
            }
            return bestPartition;
        } else {
            return null;
        }
    }
    
    
    /*
    public double testPartition(ArrayList vals, double splitPoint) throws noMetadataException {
        
        int[][] numExPerClass = new int[2][container.getNumClasses()];
        //count left
        splitVal tmp = null;
        for (int i = 0; i < vals.size(); i++) {
            tmp = (splitVal)vals.get(i);
            if (tmp.score < splitPoint){
                numExPerClass[0][tmp.ID]++;
            }
        }
        //calc right
        for (int i=0;i<this.numExamplesPerClass.length;i++){
            numExPerClass[1][i] = this.numExamplesPerClass[i] - numExPerClass[0][i];
        }
        
        if (container.splittingCriteria == container.GINI) {
            double leftImp = calcGiniIndex(numExPerClass[LEFT]);
            double rightImp = calcGiniIndex(numExPerClass[RIGHT]);
            return (this.impurity - (leftImp + rightImp));
        } else //ENTROPY
        {
            double leftImp = calcEntropyIndex(numExPerClass[LEFT]);
            double rightImp = calcEntropyIndex(numExPerClass[RIGHT]);
            return (this.impurity - (leftImp + rightImp));
        }
    }*/
    
    public double calcGiniIndex(int[] numExPerClass, int total) {
        //int total = 0;
        //for (int i=0;i<numExPerClass.length;i++) {
        //    total += numExPerClass[i];
        //}
        double sum = 1.0;
        for (int i=0;i<numExPerClass.length;i++) {
            sum -= Math.pow(((double)numExPerClass[i] / (double)total),2);
        }
        //System.out.println("Gini: " + sum);
        return sum;    
    }
    
    public double calcEntropyIndex(int[] numExPerClass, int total) {
        //int total = 0;
        //for (int i=0;i<numExPerClass.length;i++) {
        //    total += numExPerClass[i];
        //}
        double sum = 0.0;
        for (int i=0;i<numExPerClass.length;i++) {
            double prob = (double)numExPerClass[i] / (double)total;
            //0Log0 = 0.0
            if (prob > 0.0){
                sum -= prob * Math.log(prob);
            }
        }
        return sum;
    }
}

/** A class to support <code>CARTTreeNode.findWeakestSubtree()</code>
 *  by representing a node in the tree and its strength/weakness.
 * @author  Kris West
 */
class treeScore {
    public String ID = "";
    public double score = 999999;
    /** Creates a new instance of treeScore */
    public treeScore(String ID_, double score_) {
        ID = ID_;
        score = score_;
    }
}

/** A class to support <code>CARTTreeNode.split()</code>
 *  by representing a data value and its class.
 * @author  Kris West
 */
class splitVal implements Comparable{
    public int ID = -1;
    public double val = 0.0;
    /** Creates a new instance of splitScore */
    public splitVal(int ID_, double val_) {
        ID = ID_;
        val = val_;
    }
    
    public int compareTo(Object t){
        if (t.getClass() != this.getClass()){
            throw new RuntimeException(t.getClass().getName() + " is not Comparable with " + this.getClass().getName());
        }
        if (val == ((splitVal)t).val) {
            return 0;
        }else if(val < ((splitVal)t).val) {
            return -1;
        } else{
            return 1;
        }
    }
}