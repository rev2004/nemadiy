package org.imirsel.m2k.evaluation.deprecated;

import java.util.Vector;
import java.util.ArrayList;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.imirsel.m2k.io.file.InputSignalsClass;

/**
 *
 * @author kris
 */
public class OnsetDetectionEvaluatorClass {
    
    /** Evaluation tolerance (seconds) */
    private double tolerance = 0.05;
    
    /** Creates a new instance of OnsetDetectionEvaluatorClass */
    public OnsetDetectionEvaluatorClass(double tolerance_) {
        tolerance = tolerance_;
    }
    
    /** Evaluates the input Signal array with onset times metadata, against a set
     *  of Signal Objects containing one or more Ground-truth onset annotations
     *  for each file.
     *  @param detectorName Name of the onset detector being evaluated.
     *  @param GTSignals List of Ground-truth Signal Object ArrayLists (i.e. one
     *  list of GT Signals for each detection Signal).
     *  @param classList List of class names that appear in ground-truth Signals.
     *  @param DetSignals List of Signals from onset detection to be evaluated.
     *  @param outputReport Determines whether a report is output to the console.
     *  @param reportFile File to write the report to. If null report is not
     *  written to disk.
     *  @return An array containing the F-measure (0), Precsion (1) and Recall (2)
     *  of the onset detection evaluated
     **/
    public double[] evaluate(String detectorName, ArrayList GTSignals, ArrayList classList, ArrayList DetSignals, boolean outputReport, File reportFile) throws noMetadataException {
        int numClasses = classList.size();
        //System.out.println("Num Classes: " + numClasses);
        //Count objects
        if (DetSignals.size() > GTSignals.size()) {
            throw new RuntimeException("ERROR in OnsetDetectionEvaluator:\nThere are more Detected files than groundtruth files!\nGround-truth files: " + GTSignals.size() + ", Detected files: " + DetSignals.size());
        }else if(GTSignals.size() > DetSignals.size())
        {
            System.out.println("OnsetDetectionEvaluatorClass: WARNING: " + (GTSignals.size() - DetSignals.size()) + " Ground-truth files had no corresponding detections!");
        }
        
        //Serialize Signal vectors;
        Signal[] Signals = new Signal[DetSignals.size()];
        for (int i=0; i<DetSignals.size(); i++) {
            Signals[i] = (Signal)DetSignals.get(i);
        }
        boolean output = true;
        if ((reportFile == null)&&(outputReport == false)) {
            output = false;
        }
        
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        Calendar cal = new GregorianCalendar();
        
        String report = "===========================================\n";
        report += "Onset detection evaluation report\n";
        report += "-------------------------------------------\n";
        report += "Detector: " + detectorName + "  " + dateformat.format(cal.getTime()) + "\n";
        report += "===========================================\n";
        
        //Do Evaluation
        double[] FMeasures = new double[GTSignals.size()];
        double totalFMeasure = 0.0;
        int totalCorrect = 0;
        int totalFalsePositives = 0;
        int totalFalseNegatives = 0;
        int totalDoubled = 0;
        int totalMerged = 0;
        double FPsum = 0.0;
        double CDsum = 0.0;
        double qsum = 0.0;
        
        int[] classTotalCorrect = new int[classList.size()];
        int[] classFalsePositives = new int[classList.size()];
        int[] classFalseNegatives = new int[classList.size()];
        int[] classDoubled = new int[classList.size()];
        int[] classMerged = new int[classList.size()];
        int[] classCounts = new int[classList.size()];
        
        double[] classAvgCorrect = new double[classList.size()];
        double[] classAvgFalsePositives = new double[classList.size()];
        double[] classAvgFalseNegatives = new double[classList.size()];
        double[] classAvgDoubled = new double[classList.size()];
        double[] classAvgMerged = new double[classList.size()];
        
        double[] classFPsum = new double[classList.size()];
        double[] classCDsum = new double[classList.size()];
        double[] classQsum = new double[classList.size()];
        double[] classFMeasures = new double[classList.size()];
        double[] classRecalls = new double[classList.size()];
        double[] classPrecisions = new double[classList.size()];
        
        int numInDetFiles = 0;
        int numInGTFiles = 0;
        
        double meanAbsDistance = 0.0;
        double meanDistance = 0.0;
        
        for (int i=0;i<Signals.length;i++) {
            if(output){
                report += "-------------------------------------------\nFile: " + Signals[i].getStringMetadata(Signal.PROP_FILE_LOCATION);
            }
            
            double fMeasureForFile = 0.0;
            double avgCorrectForFile = 0.0;
            double avgFPForFile = 0.0;
            double avgFNForFile = 0.0;
            double avgRecForFile = 0.0;
            double avgPrecForFile = 0.0;
            double avgMergedForFile = 0.0;
            double avgDoubledForFile = 0.0;
            int totCorrectForFile = 0;
            int totFPForFile = 0;
            int totFNForFile = 0;
            int totMergedForFile = 0;
            int totDoubledForFile = 0;
            
            //find correct Ground-truth file
            ArrayList subList = null;
            Signal GTFile = null;
            int GTnum = -1;
            int classNum = -1;
            for (int j=0;j<GTSignals.size();j++) {
                if (((Signal)((ArrayList)GTSignals.get(j)).get(0)).getStringMetadata(Signal.PROP_FILE_LOCATION).compareTo(Signals[i].getStringMetadata(Signal.PROP_FILE_LOCATION)) == 0) {
                    subList = (ArrayList)GTSignals.get(j);
                    GTnum = j;
                    classNum = classList.indexOf( ((Signal)subList.get(0)).getStringMetadata(Signal.PROP_CLASS) );
                }
            }
            for (int curGT = 0; curGT < subList.size(); curGT++) {
                
                int correct = 0;
                int doubled = 0;
                int merged = 0;
                int falsePositives = 0;
                int falseNegatives = 0;
                
                GTFile = (Signal)subList.get(curGT);
                
                int count = 0;
                for (int t=0; t<GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT).length; t++) {
                    double onTime = GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT)[t];
                    // if we've allocated scores for everything in the detection, but there's more to go in the ground truth
                    // they will be falseNegatives
                    if(count > Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length - 1) {
                        falseNegatives++;
                    }
                    // main loop
                    for (int c=count;c<Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length;c++) {
                        if (Math.abs(Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES)[c] - onTime) < tolerance) {
                            correct++;
                            meanAbsDistance += Math.abs(Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES)[c] - onTime);
                            meanDistance += onTime - Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES)[c];
                            count = c+1;
                            for (int c1=count;c1<Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length;c1++) {
                                if (t < GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT).length - 1) {
                                    double onTime2 = GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT)[t+1];
                                    // we're checking for doubles in the next predicted value in regards to the current ground truth.
                                    // first though, we check that the next prediction doesn't lie in the tolerance of the next truth.
                                    if (Math.abs(Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES)[c1] - onTime2) < tolerance) {
                                        break;
                                    }
                                }
                                if (Math.abs(Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES)[c1] - onTime) < tolerance) {
                                    doubled++;
                                }
                            }
                            break;
                        } else if (Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES)[c] > (onTime + tolerance)) {
                            //System.out.println("false neg c: " + c + " t: " + t + " truth time: " + onTime + " test time: " + testSig.getData()[testOnsetCol][c]);
                            falseNegatives++;
                            count = c;
                            break;
                        }
                        // if we've allocated scores for everything in the detection, but there's more to go in the ground truth
                        // they will be falseNegatives
                        if(c == Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length - 1){
                            falseNegatives++;
                        }
                        
                    }
                }
                int count2 = 0;
                for (int c=0;c<Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length;c++) {
                    double onTime = Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES)[c];
                    for (int t=count2; t<GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT).length; t++) {
                        if (Math.abs(GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT)[t] - onTime) < tolerance) {
                            count2 = t+1;
                            for (int c1=count2;c1<GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT).length;c1++) {
                                if(c < Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length - 1) {
                                    double onTime2 = Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES)[c+1];
                                    if (Math.abs(GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT)[c1] - onTime2) < tolerance) {
                                        break;
                                    }
                                }
                                if (Math.abs(GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT)[c1] - onTime) < tolerance) {
                                    merged++;
                                }
                            }
                            break;
                        }
                    }
                }
                falsePositives = Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length - correct;
                totalCorrect += correct;
                totalFalseNegatives += falseNegatives;
                totalFalsePositives += falsePositives;
                totalDoubled += doubled;
                totalMerged += merged;
                
                numInDetFiles += Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length;
                numInGTFiles += GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT).length;
                
                if ((correct + falseNegatives)!=(GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT).length)) {
                    System.out.println("# correct + # falseNegatives is not equal to number in GT file!");
                }
                if ((correct + falsePositives)!=(Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length)) {
                    System.out.println("# correct + # falsePositives is not equal to number in detected file!");
                }
                
                double precision = (((double)correct/(double)Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length));
                double recall = (((double)correct/(double)GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT).length));
                double fmeasure = 0.0;
                if (recall != 0.0 && precision != 0.0) {
                    fmeasure = (2 * recall * precision)/(recall + precision);
                }
                
                double FPRate = (double)falsePositives/(double)correct * 100.0;
                double CDRate = (double)(correct - falseNegatives)/(double)correct * 100.0;
                double q = (double)(correct + falseNegatives - (falseNegatives + falsePositives))/(double)(correct + falseNegatives + falsePositives);
                fMeasureForFile += fmeasure;
                avgRecForFile += recall;
                avgPrecForFile += precision;
                FPsum += FPRate;
                CDsum += CDRate;
                qsum += q;
                totCorrectForFile += correct;
                totFPForFile += falsePositives;
                totFNForFile += falseNegatives;
                totMergedForFile += merged;
                totDoubledForFile += doubled;
                
                if (output){
                    report += "Annotator: " + GTFile.getStringMetadata("annotator") + "\n";
                    report += "Correct: " + correct + "\n";
                    report += "False positives: " + falsePositives + "\n";
                    report += "False negatives: " + falseNegatives + "\n";
                    report += "Merged: " + merged + "\n";
                    report += "Doubled: " + doubled + "\n";
                    report += "Onsets in ground-truth file: " + GTFile.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT).length + "\n";
                    report += "Onsets in detected file: " + Signals[i].getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES).length + "\n";
                    report += "Precision:    " + (precision * 100) + "%" + "\n";
                    report += "Recall:       " + (recall * 100) + "%" + "\n";
                    report += "F-measure:    " + fmeasure + "\n";
                    report += "FP Rate:       " + FPRate + "\n";
                    report += "CD Rate:       " + CDRate + "\n";
                    report += "q (error rate):       " + q + "\n";
                    report += "----\n";
                }
            }
            fMeasureForFile = fMeasureForFile/(double)subList.size();
            FMeasures[GTnum] = fMeasureForFile;
            avgRecForFile = avgRecForFile/(double)subList.size();
            avgPrecForFile = avgPrecForFile/(double)subList.size();
            avgCorrectForFile = (double)totCorrectForFile/(double)subList.size();
            avgFPForFile = (double)totFPForFile/(double)subList.size();
            avgFNForFile = (double)totFNForFile/(double)subList.size();
            avgMergedForFile = (double)totMergedForFile/(double)subList.size();
            avgDoubledForFile = (double)totDoubledForFile/(double)subList.size();
            
            if (output){
                report += "Overall report for file: " + Signals[i].getStringMetadata(Signal.PROP_FILE_LOCATION) + "\n";
                report += "Average correct: " + avgCorrectForFile + "\n";
                report += "Average False positives: " + avgFPForFile + "\n";
                report += "Average False negatives: " + avgFNForFile + "\n";
                report += "Average Merged: " + avgMergedForFile + "\n";
                report += "Average Doubled: " + avgDoubledForFile + "\n";
                report += "Average Precision:    " + (avgPrecForFile * 100) + "%" + "\n";
                report += "Average Recall:       " + (avgRecForFile * 100) + "%" + "\n";
                report += "Average F-measure:    " + fMeasureForFile + "\n";
                report += "----\n";
            }
            
            classTotalCorrect[classNum] += totCorrectForFile;
            classFalsePositives[classNum] += totFPForFile;
            classFalseNegatives[classNum] += totFNForFile;
            classDoubled[classNum] += totDoubledForFile;
            classMerged[classNum] += totMergedForFile;
            classCounts[classNum]++;
            classFMeasures[classNum] = classFMeasures[classNum] + fMeasureForFile;
            classRecalls[classNum] = classRecalls[classNum] + avgRecForFile;
            classPrecisions[classNum] = classPrecisions[classNum] + avgPrecForFile;
            
            classAvgCorrect[classNum] += avgCorrectForFile;
            classAvgFalsePositives[classNum] += avgFPForFile;
            classAvgFalseNegatives[classNum] += avgFNForFile;
            classAvgDoubled[classNum] += avgDoubledForFile;
            classAvgMerged[classNum]  += avgMergedForFile;
            
/*          Signals[i].setMetadata(Signal.PROP_MCNEMAR, mcnemars_full);
            Signals[i].setMetadata(Signal.PROP_PERF, new Double(fMeasureForFile));
            Signals[i].setMetadata(Signal.PROP_ALG_NAME, detectorName);
 */
        }
        if(output){
            report += "\n===========================================\n";
        }
        double avgFmeas = 0.0;
        double avgRec = 0.0;
        double avgPrec = 0.0;
        double avgCorr = 0;
        double avgFP = 0.0;
        double avgFN = 0.0;
        double avgDoub = 0.0;
        double avgMerg = 0.0;
        for (int i = 0; i < classList.size(); i++) {
            avgFmeas += classFMeasures[i]/(double)Signals.length;
            avgRec += classRecalls[i]/(double)Signals.length;
            avgPrec += classPrecisions[i]/(double)Signals.length;
            avgCorr += classAvgCorrect[i]/(double)Signals.length;
            avgFP += classAvgFalsePositives[i]/(double)Signals.length;
            avgFN += classAvgFalseNegatives[i]/(double)Signals.length;
            avgDoub += classAvgDoubled[i]/(double)Signals.length;
            avgMerg += classAvgMerged[i]/(double)Signals.length;
            
            if (output){
                
                report += "Class: " + (String)classList.get(i) + "\n";
                report += "Number of files in this class: " + classCounts[i] + "\n";
                report += "Total Correct accross all Ground-truths and Files in class: " + classTotalCorrect[i] + "\n";
                report += "Total False positives accross all Ground-truths and Files in class: " + classFalsePositives[i] + "\n";
                report += "Total False negatives accross all Ground-truths and Files in class: " + classFalseNegatives[i] + "\n";
                report += "Total Merged accross all Ground-truths and Files in class: " + classMerged[i] + "\n";
                report += "Total Doubled accross all Ground-truths and Files: " + classDoubled[i] + "\n\n";
                report += "Average Correct accross all Ground-truths, total over Files: " + classAvgCorrect[i] + "\n";
                report += "Average False positives accross all Ground-truths, total over Files in class: " + classAvgFalsePositives[i] + "\n";
                report += "Average False negatives accross all Ground-truths, total over Files in class: " + classAvgFalseNegatives[i] + "\n";
                report += "Average Merged accross all Ground-truths, total over Files in class: " + classAvgMerged[i] + "\n";
                report += "Average Doubled accross all Ground-truths, total over Files in class: " + classAvgDoubled[i] + "\n\n";
                report += "Average Precision accross all Ground-truths and Files in class: " + classPrecisions[i]/(double)classCounts[i] + "\n";
                report += "Average Recall accross all Ground-truths and Files in class: " + classRecalls[i]/(double)classCounts[i] + "\n";
                report += "Average F-measure accross all Ground-truths and Files in class: " + classFMeasures[i]/(double)classCounts[i] + "\n";
                report += ("----\n");
            }
        }
        
        if (output){
            report += "\n===================================\nFINAL SUMMARY\n";
            report += "Total Correct over all ground truths and classes: " + totalCorrect + "\n";
            report += "Total False positives over all ground truths and classes: " + totalFalsePositives + "\n";
            report += "Total False negatives over all ground truths and classes: " + totalFalseNegatives + "\n";
            report += "Total Merged over all ground truths and classes: " + totalMerged + "\n";
            report += "Total Doubled over all ground truths and classes: " + totalDoubled + "\n";
            report += "Total in ground-truth files: " + numInGTFiles + "\n";
            report += "Total in detected files: " + numInDetFiles + "\n\n";
            
            report += "Average Correct (averaged over ground-truths, then weighted average accross classes): " + avgCorr + "\n";
            report += "Average False positives (averaged over ground-truths, then weighted average accross classes): " + avgFP + "\n";
            report += "Average False negatives (averaged over ground-truths, then weighted average accross classes): " + avgFN + "\n";
            report += "Average Merged (averaged over ground-truths, then weighted average accross classes): " + avgMerg + "\n";
            report += "Average Doubled (averaged over ground-truths, then weighted average accross classes): " + avgDoub + "\n\n";
            
            report += ("mean absolute distance: " + (meanAbsDistance/totalCorrect) + "\n");
            report += ("mean distance (bias): " + (meanDistance/totalCorrect) + "\n\n");
            
            report += ("Overall Average Precision (Weighted by number of files in each class):    " + (avgPrec) + "\n");
            report += ("Overall Average Recall (Weighted by number of files in each class):      " + (avgRec)  + "\n");
            report += ("Overall Average F-measure (Weighted by number of files in each class):    " + avgFmeas + "\n");
        }
        
        if (outputReport) {
            System.out.println(report);
        }
        if (reportFile != null){
            
            try {
                BufferedWriter writeOutput = new BufferedWriter( new FileWriter(reportFile) );
                writeOutput.write( report );
                writeOutput.close();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        
        double[] out = new double[3];
        out[0] = avgFmeas;
        out[1] = avgPrec;
        out[2] = avgRec;
        return out;
    }
    
    public static void main(String[] args){
        String usage = "Usage: java -jar m2k.jar tolerance audio_files_folder ground_truth_folder_path detection_folder_path [-c | output_file_path ] [detector_name]\n";
        usage += " * tolerance - evaluation tolerance in seconds (0.05 suggested)\n";
        usage += " * audio_files_folder - Path to audio files that detection is being\n" +
                "\tevaluated on, this should be in directory tree containing ONLY \n" +
                "\tthe audio files, and optional annotation files with a .txt \n" +
                "\textension audio file extensions currently accepted: .wav, \n" +
                "\t.mp3, .snd, .aiff & .vob\n";
        usage += " * ground_truth_folder_path - Path to ground-truth folder, \n" +
                "\tfiles should be in a folder per class and have .txt extensions\n";
        usage += " * detection_folder_path - Path to detection results folder  \n" +
                "\tthis should be a flat directory containing ONLY the detection \n" +
                "\tresult files with .txt extnsions\n";
        usage += " * -c - output report to console only\n";
        usage += " * output_file_path - path to write report out to\n";
        usage += " * [Optional] detector_name - Onset detector name for use in \n" +
                "\treporting\n";
        usage += " * --help - print out this message\n";
        
        if (args.length < 5) {
            System.out.println(usage);
            return;
        }
        if ((args[1] == "--info") || (args[1] =="--help") || (args[1] == "/?")){
            System.out.println(usage);
            return;
        }
        
        double tolerance = 0.05;
        tolerance = Double.parseDouble(args[0]);
        
        String audiopath = args[1];
        String gtpath = args[2];
        String detpath = args[3];
        File audiofile = new File(audiopath);
        File gtfile = new File(gtpath);
        File detfile = new File(detpath);
        
        //check audio folder
        if (!audiofile.exists()) {
            System.out.println("Audio folder: " + audiopath + " does not exist!\n" + usage);
            return;
        }
        if (!audiofile.isDirectory()) {
            System.out.println(audiofile.getPath() + " is not a folder!\n" + usage);
            return;
        }
        
        //check gt folder
        if (!gtfile.exists()) {
            System.out.println("Ground-truth folder: " + gtpath + " does not exist!\n" + usage);
            return;
        }
        if (!gtfile.isDirectory()) {
            System.out.println(gtfile.getPath() + " is not a folder!\n" + usage);
            return;
        }
        
        //check det folder
        if (!detfile.exists()) {
            System.out.println("Detection results folder: " + detpath + " does not exist!\n" + usage);
            return;
        }
        if (!gtfile.isDirectory()) {
            System.out.println(gtfile.getPath() + " is not a folder!\n" + usage);
            return;
        }
        
        //check output flags
        String outpath = null;
        File outfile = null;
        if (!args[4].equals("-c")){
            outpath = args[4];
            outfile = new File(outpath);
            if (outfile.exists()) {
                System.out.println("Evaluation report file: " + outpath + " already exists!\n");
                System.out.print("Overwrite (y|n): ");
                String answer = readString();
                while (!(answer.equals("y")||answer.equals("n")))
                {
                    System.out.print("enter \"y\" or \"n\": ");
                    answer = readString();
                }
                if (answer.equals("n")){
                    return;
                }else{
                    outfile.delete();
                }
            }
            try{
                outfile.createNewFile();
            }catch(IOException ioe){
                throw new RuntimeException("Can't create evaluator report file!\nContact Kris West (kw@cmp.uea.ac.uk)\n and report bug (please enclose stack trace)",ioe);
            }
            if (outfile.isDirectory()) {
                System.out.println(outfile.getPath() + " is a folder!\n" + usage);
                return;
            }
            if (!outfile.canWrite()) {
                System.out.println("Can't write to: " + outfile.getPath() + "\n" + usage);
                return;
            }
        }
        
        //read name if given
        String detectorName = "<No name given>";
        if (args.length > 5) {
            detectorName = args[5];
        }
        
        //get list of audio files and make a Signal Object for each
        Signal[] AudioSignals = null;
        InputSignalsClass in = new InputSignalsClass();
        in.addToVectors(audiofile.getPath(),"",true,".wav,.mp3,.snd,.aiff,.vob",".txt","manual");
        try{
            AudioSignals = in.produceSignalList();
        }catch(IOException ioe){
            throw new RuntimeException("IOException occured while reading audio files!\nContact Kris West (kw@cmp.uea.ac.uk)\n and report bug (please enclose stack trace)",ioe);
        }
        //convert to list of file Objects for ReadOnsetTextFileClass
        ArrayList wavFiles = new ArrayList();
        try{
            for (int i = 0; i < AudioSignals.length; i++) {
                wavFiles.add(new File(AudioSignals[i].getStringMetadata(Signal.PROP_FILE_LOCATION)));
            }
            System.out.println("Received " + wavFiles.size() + " audio files");
        }catch(noMetadataException nme)
        {
            throw new RuntimeException("noMetadataException occured while reading audio files!\nContact Kris West (kw@cmp.uea.ac.uk)\n and report bug (please enclose stack trace)",nme);
        }
        //read gt folder and make ground-truth list of lists of annotations for each file
        Signal[] GTSignals = null;
        in = new InputSignalsClass();
        in.addToVectors(gtfile.getPath(),"",true,".txt",".wav,.mp3,.snd,.aiff,.vob","toplevelDir");
        try{
            GTSignals = in.produceSignalList();
            System.out.println("Received " + GTSignals.length + " ground-truth files");
        }catch(IOException ioe){
            throw new RuntimeException("IOException occured while reading ground-truth files!\nContact Kris West (kw@cmp.uea.ac.uk)\n and report bug (please enclose stack trace)",ioe);
        }
        ArrayList GTList = new ArrayList();
        ArrayList classList = new ArrayList();
        
        try{
            //collect class names
            for (int i = 0; i < GTSignals.length; i++) {
                String classname = GTSignals[i].getStringMetadata(Signal.PROP_CLASS);
                if (!classList.contains(classname)) {
                    classList.add(classname);
                }
            }
            
            //read in annotations file,
            ReadOnsetTextFileClass filereader = new ReadOnsetTextFileClass(true,false);
            
            for (int i = 0; i < GTSignals.length; i++) {
                GTSignals[i] = filereader.readTextFile(GTSignals[i].getFile(),wavFiles);
                //check if list contains a list for each file create if necessary, add to list
                boolean hadList = false;
                for (int j = 0; j < GTList.size(); j++) {
                    if (((Signal)((ArrayList)GTList.get(j)).get(0)).getStringMetadata("fileNameToken").equals(GTSignals[i].getStringMetadata("fileNameToken"))) {
                        hadList = true;
                        ((ArrayList)GTList.get(j)).add(GTSignals[i]);
                    }
                }
                if (hadList == false){
                    ArrayList temp = new ArrayList();
                    temp.add(GTSignals[i]);
                    GTList.add(temp);
                }
            }
            
        }catch(noMetadataException nme){
            throw new RuntimeException("noMetadataException occured while reading ground-truth files!\nContact Kris West (kw@cmp.uea.ac.uk)\n and report bug (please enclose stack trace)",nme);
        }
        
        //read detection folder and make detection list
        Signal[] DetSignals = null;
        in = new InputSignalsClass();
        in.addToVectors(detfile.getPath(),"",true,".txt","","manual");
        try{
            DetSignals = in.produceSignalList();
            System.out.println("Received " + DetSignals.length + " onset detection transcripts");
        }catch(IOException ioe){
            throw new RuntimeException("IOExcpetion occured while reading detection files!\nContact Kris West (kw@cmp.uea.ac.uk)\n and report bug (please enclose stack trace)",ioe);
        }
        ArrayList DetList = new ArrayList();
        
        try{
            //read in annotations file,
            ReadOnsetTextFileClass filereader = new ReadOnsetTextFileClass(false,false);
            
            for (int i = 0; i < DetSignals.length; i++) {
                DetSignals[i] = filereader.readTextFile(DetSignals[i].getFile(),wavFiles);
                //check if list contains a list for each file create if necessary, add to list
                DetList.add(DetSignals[i]);
            }
            
        }catch(noMetadataException nme){
            throw new RuntimeException("noMetadataException occured while reading detection files!\nContact Kris West (kw@cmp.uea.ac.uk)\n and report bug (please enclose stack trace)",nme);
        }
        
        //run evaluator
        OnsetDetectionEvaluatorClass evaluator = new OnsetDetectionEvaluatorClass(tolerance);
        try{
            double[] result = evaluator.evaluate(detectorName, GTList, classList, DetList, true, outfile);
        }catch(Exception ex) {
            throw new RuntimeException("Unreported exception! \nContact Kris West (kw@cmp.uea.ac.uk)\n and report bug (please enclose stack trace)",ex);
        }
    }
    
    /** Simple read method to support console input
     *  @return String read from console.
     */
    public static String readString() {
        int ch;
        String r = "";
        boolean done = false;
        while (!done) {
            try {
                ch = System.in.read();
                if (ch < 0 || (char)ch == '\n')
                    done = true;
                else if ((char)ch != '\r') // weird--it used to do \r\n translation
                    r = r + (char) ch;
            } catch(java.io.IOException e) {
                done = true;
            }
        }
        return r;
    }
}
