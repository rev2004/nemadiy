/*
 * RealTimeHistogramFrame.java
 *
 * Created on February 2, 2008, 5:16 PM
 */
package org.imirsel.m2k.vis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import org.imirsel.m2k.io.file.ThreadedAudioPlayer;
import org.imirsel.m2k.io.file.ThreadedAudioPlayerFactory;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author  kriswest
 */
public class RealTimeClassificationHistogramFrame extends javax.swing.JFrame {

    /** Time display formatting.*/
    private static DecimalFormat timeFormat;
    // playback variables
    private ThreadedAudioPlayer player = null;
    private Thread playbackMonitor = null;
    private boolean playing = false;
    // Time display variables
    private int numFrames = -1;
    private double duration = -1.0;
    private double durationPerFrame = -1.0;
    private ArrayList<Signal> theSignals;
    private HashMap<String, Integer> theSignalsMap;
    private ArrayList<ArrayList<double[]>> likelihoodsReceived;
    private ArrayList<double[]> onsetTimes;
    private int currentMetadataIndex = -1;
    private String[] columnNames;
    private boolean likelihoodsDataTerminated = true;
    //histogram window currently displayed
    private int currentFrame = -1;
    //normalization parameters
    private double UserSpecifiedMin = -1.0;
    private double UserSpecifiedMax = -1.0;
    private boolean normalize = true;
    private static String modelName = "Histogram";
    
    /** Creates new form RealTimeHistogramFrame 
    @param columnNames_ The column names to be displayed by the histogram.
     */
    public RealTimeClassificationHistogramFrame(String modelName_, String[] columnNames_,boolean normalize_,double UserSpecifiedMin_,double UserSpecifiedMax_) {
        System.out.println("Initialising RealTimeClassificationHistogramFrame");
        modelName = modelName_;
        normalize = normalize_;
        UserSpecifiedMin = UserSpecifiedMin_;
        UserSpecifiedMax = UserSpecifiedMax_;
        columnNames = columnNames_;
        theSignals = new ArrayList<Signal>();
        theSignalsMap = new HashMap<String, Integer>();
        likelihoodsReceived = new ArrayList<ArrayList<double[]>>();
        onsetTimes = new ArrayList<double[]>();
        likelihoodsDataTerminated = true;
        playbackMonitor = null;
        timeFormat = new DecimalFormat();
        timeFormat.setMaximumFractionDigits(2);
        
        initComponents();
        histogramPanel1.setClassLabels(columnNames);
    }

    public String getModelName(){
        return modelName;
    }
    
    public void addNewSignal(Signal input) {
        synchronized (this) {
            if (!likelihoodsDataTerminated) {
                throw new RuntimeException("New Signal received before termination of last file's likelihoods data stream!");
            }

            try{
                System.out.println("Likelihoods histogram  - Received Signal: " + input.getStringMetadata(Signal.PROP_FILE_LOCATION));
                theSignalsMap.put(input.getStringMetadata(Signal.PROP_FILE_LOCATION), theSignals.size());
                theSignals.add(input);
                double[] onsets = input.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES);
                onsetTimes.add(onsets);
                System.out.println("Likelihoods Histogram: Onsets received: ");
                for (int i = 0; i < onsets.length; i++) {
                    System.out.print(onsets[i] +",");
                }
                System.out.println("");

                likelihoodsReceived.add(new ArrayList<double[]>());
                likelihoodsDataTerminated = false;
            }catch(noMetadataException nme){
                throw new RuntimeException("File location or onset times missing!",nme);
            }
            
        }
    }

    public void addNewLikelihoodsData(double[] input) {
        synchronized (this) {
            if (likelihoodsDataTerminated) {
                throw new RuntimeException("Likelihoods data received before corresponding Signal Object with metadata!");
            }
            if (input == null) {
                System.out.println("Likelihoods histogram - Reached end likelihoods stream (null)");
                likelihoodsDataTerminated = true;
                return;
            }
            ArrayList<double[]> currentList = likelihoodsReceived.get(likelihoodsReceived.size() - 1);
            if (currentList.size() == 0) {
                //display in file list now we have data
                try{
                ((DefaultListModel) FileList.getModel()).addElement(theSignals.get(theSignals.size() - 1).getStringMetadata(Signal.PROP_FILE_LOCATION));
                }catch(noMetadataException nme){
                    throw new RuntimeException("File location missing!",nme);
                }
            }
            currentList.add(input);
            System.out.println("Likelihoods histogram - Received " + currentList.size() + " vectors so far");
        }
    }

    private boolean initAudioPlayer(int index) {
        System.out.println("Initing audio player for file # " + index);
        currentMetadataIndex = index;
        Signal aSig = theSignals.get(index);
        try{
            player = ThreadedAudioPlayerFactory.createAudioPlayer(aSig.getFile());
        }catch(noMetadataException nme){
            throw new RuntimeException("File location missing!",nme);
        }
        if (player == null) {
            this.DurationValue.setText("0.00 secs");
            this.PositionValue.setText("0.00 secs");
            return false;
        }
        player.pause();
        try {
            player.init();
        } catch (Exception ex) {
            throw new RuntimeException("Exception occurred when initialising player", ex);
        }
        numFrames = player.getNumFrames();
        duration = player.getDuration();
        durationPerFrame = duration / numFrames;
        DurationValue.setText(timeFormat.format(duration) + " secs");
        PositionValue.setText("0.00 secs");
        playing = true;
        currentFrame = -1;
        player.unpause();
        return true;
    }

    private void skipForward(int seconds) {
        System.out.println("skip forwards "+ seconds + " seconds");
        int position = player.getPosition();
        int newPostion = position + (int) (seconds / durationPerFrame);
        boolean moreToPlay = player.setPosition(newPostion);

        if (!moreToPlay) {
            if (AutoPlayButton.isSelected()) {
                playNext();
            } else {
                System.out.println("Ending playback, skipped to end of file");
                playing = false;
                player.close();
            }
        }
    }

    private void playNext() {
        //for autoplay function
        int pos = FileList.getSelectedIndex();
        int length = FileList.getModel().getSize();
        currentFrame = -1;
        
        if (currentMetadataIndex != pos){
            System.out.println("Auto play file # " + pos);
            player.close();
            initAudioPlayer(pos);
        }else if (pos + 1 < length) {
            System.out.println("Auto play file # " + pos + 1);
            FileList.setSelectedIndex(pos + 1);
            player.close();
            initAudioPlayer(pos+1);
        } else {
            System.out.println("Ending playback, Nothing to auto-play");
            //if nothing else to play destroy player and notify monitor
            playing = false;
            player.close();
        }
    }

    public boolean getLikelihoodsDataTerminated() {
        return likelihoodsDataTerminated;
    }

    class PlaybackMonitorThread extends Thread {

        public PlaybackMonitorThread() {
            super();
        }

        public void run() {
            System.out.println("Playback monitor thread started");
            while (playing) {
                synchronized (this) {
                    if (player != null) {
                        if (player.isPlaying()) {
                            int pos = player.getPosition();
                            double playbackTime = pos * durationPerFrame;
                            PositionValue.setText(timeFormat.format(playbackTime) + " secs");
                            
                            //update histogram display here
                            
                            //compute current frame num
                            int frameToDisplay = 0;
                            ArrayList<double[]> likelihoodsList = likelihoodsReceived.get(currentMetadataIndex);
                            double[] currentOnsets = onsetTimes.get(currentMetadataIndex);
                            
                            //System.out.println("Playback Time: " + playbackTime);
                            double onsetTime = currentOnsets[0];
                            while((onsetTime < playbackTime)&&(frameToDisplay < likelihoodsList.size())){
                                frameToDisplay++;
                                onsetTime = currentOnsets[frameToDisplay];
                            }
                            frameToDisplay--;
                            if (frameToDisplay < 0){
                                frameToDisplay = 0;
                            }
                            onsetTime = currentOnsets[frameToDisplay];
                            //System.out.println("Onset time to display: " + onsetTime + ", onset num: " + frameToDisplay);
//                            
//                            for (; ((frameToDisplay < likelihoodsList.size()) && (frameToDisplay < (currentOnsets.length))); frameToDisplay++) {
//                                if (playbackTime > currentOnsets[frameToDisplay]){
//                                    System.out.println("Playback greater than onset at " + currentOnsets[frameToDisplay]);
//                                    break;
//                                }else{
//                                    System.out.println("Playback time less than onset at " + currentOnsets[frameToDisplay]);
//                                }
//                            }
                            //if necessary update histogram frame
                            if ((currentFrame != frameToDisplay)&&(likelihoodsList.size() > 0)){
                                System.out.println("Likelihoods histogram - thread advancing window display to frame " + frameToDisplay);
                                histogramPanel1.addNewData(likelihoodsList.get(frameToDisplay));
                                currentFrame = frameToDisplay;
                            }
                            
                            
                        } else {
                            if (AutoPlayButton.isSelected()) {
                                playNext();
                            } else {
                                playing = false;
                                player.close();
                            }

                        }
                    }
                }
                //sleep till next update
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RealTimeClassificationHistogramFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //cleanup
            histogramPanel1.addNewData(null);
            DurationValue.setText("0.00 secs");
            PositionValue.setText("0.00 secs");
            player = null;  
            System.out.println("Ended playback montor thread.");
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        HistogramDesktop = new javax.swing.JDesktopPane();
        HistogramInternalPanel = new javax.swing.JInternalFrame();
        histogramPanel1 = new HistogramPanel(normalize,UserSpecifiedMin,UserSpecifiedMax);
        FileListFrame = new javax.swing.JInternalFrame();
        FileListScrollPane = new javax.swing.JScrollPane();
        FileList = new javax.swing.JList();
        ControlFrame = new javax.swing.JInternalFrame();
        PlayButton = new javax.swing.JButton();
        StopButton = new javax.swing.JButton();
        AutoPlayButton = new javax.swing.JToggleButton();
        SkipForwardButton = new javax.swing.JButton();
        DurationLabel = new javax.swing.JLabel();
        PositionLabel = new javax.swing.JLabel();
        DurationValue = new javax.swing.JLabel();
        PositionValue = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        HistogramDesktop.setDoubleBuffered(true);

        HistogramInternalPanel.setMaximizable(true);
        HistogramInternalPanel.setResizable(true);
        HistogramInternalPanel.setTitle(getModelName());
        HistogramInternalPanel.setVisible(true);

        org.jdesktop.layout.GroupLayout histogramPanel1Layout = new org.jdesktop.layout.GroupLayout(histogramPanel1);
        histogramPanel1.setLayout(histogramPanel1Layout);
        histogramPanel1Layout.setHorizontalGroup(
            histogramPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 566, Short.MAX_VALUE)
        );
        histogramPanel1Layout.setVerticalGroup(
            histogramPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 520, Short.MAX_VALUE)
        );

        HistogramInternalPanel.getContentPane().add(histogramPanel1, java.awt.BorderLayout.CENTER);

        HistogramInternalPanel.setBounds(180, 0, 570, 560);
        HistogramDesktop.add(HistogramInternalPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        FileListFrame.setIconifiable(true);
        FileListFrame.setResizable(true);
        FileListFrame.setTitle("Audio files");
        FileListFrame.setToolTipText("Select an audio file to visualize");
        FileListFrame.setVisible(true);

        FileListScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        FileList.setModel(new DefaultListModel());
        FileListScrollPane.setViewportView(FileList);

        org.jdesktop.layout.GroupLayout FileListFrameLayout = new org.jdesktop.layout.GroupLayout(FileListFrame.getContentPane());
        FileListFrame.getContentPane().setLayout(FileListFrameLayout);
        FileListFrameLayout.setHorizontalGroup(
            FileListFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(FileListScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
        );
        FileListFrameLayout.setVerticalGroup(
            FileListFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(FileListScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );

        FileListFrame.setBounds(0, 0, 180, 390);
        HistogramDesktop.add(FileListFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        ControlFrame.setTitle("Controls");
        ControlFrame.setVisible(true);

        PlayButton.setText("play");
        PlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayButtonActionPerformed(evt);
            }
        });

        StopButton.setText("stop");
        StopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopButtonActionPerformed(evt);
            }
        });

        AutoPlayButton.setText("auto-play");

        SkipForwardButton.setText("skip-forward");
        SkipForwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SkipForwardButtonActionPerformed(evt);
            }
        });

        DurationLabel.setText("duration:");

        PositionLabel.setText("position:");

        DurationValue.setFont(DurationValue.getFont().deriveFont((float)12));
        DurationValue.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        DurationValue.setText("0.00 sec");

        PositionValue.setFont(PositionValue.getFont().deriveFont((float)12));
        PositionValue.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        PositionValue.setText("0.00 sec");

        org.jdesktop.layout.GroupLayout ControlFrameLayout = new org.jdesktop.layout.GroupLayout(ControlFrame.getContentPane());
        ControlFrame.getContentPane().setLayout(ControlFrameLayout);
        ControlFrameLayout.setHorizontalGroup(
            ControlFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ControlFrameLayout.createSequentialGroup()
                .add(PlayButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(StopButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
            .add(SkipForwardButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
            .add(AutoPlayButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
            .add(ControlFrameLayout.createSequentialGroup()
                .add(8, 8, 8)
                .add(ControlFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(DurationLabel)
                    .add(PositionLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ControlFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(PositionValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .add(DurationValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                .addContainerGap())
        );
        ControlFrameLayout.setVerticalGroup(
            ControlFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ControlFrameLayout.createSequentialGroup()
                .add(ControlFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(StopButton)
                    .add(PlayButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(SkipForwardButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(AutoPlayButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ControlFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(DurationLabel)
                    .add(DurationValue))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ControlFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(PositionLabel)
                    .add(PositionValue))
                .add(40, 40, 40))
        );

        ControlFrame.setBounds(0, 390, 180, 170);
        HistogramDesktop.add(ControlFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(HistogramDesktop, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, HistogramDesktop, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void PlayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayButtonActionPerformed

    if(player != null){
        
        histogramPanel1.addNewData(null);
        currentFrame = -1;
        playing = false;
        player.pause();
        player.close();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(RealTimeClassificationHistogramFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    System.out.println("Playbutton pushed");
    //get audio file to play
    String theFilePath = (String)FileList.getSelectedValue();
    if (theFilePath == null){
        JOptionPane.showMessageDialog(this, "An audio file must be selected in the Audio files window to playback.", "No audio file selected to play!", JOptionPane.ERROR_MESSAGE);
    }
    
    Integer theSig = theSignalsMap.get(theFilePath);
    
    initAudioPlayer(theSig);
    playbackMonitor = new PlaybackMonitorThread();
    playbackMonitor.start();
    
}//GEN-LAST:event_PlayButtonActionPerformed

private void StopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopButtonActionPerformed
    System.out.println("Stop button pushed");
    
    histogramPanel1.addNewData(null);
    currentFrame = -1;
    player.pause();
    player.close();
    playing = false;
    
}//GEN-LAST:event_StopButtonActionPerformed

private void SkipForwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SkipForwardButtonActionPerformed
    System.out.println("Skip button pushed");
    if(player != null){
        skipForward(15);
    }
}//GEN-LAST:event_SkipForwardButtonActionPerformed



    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new RealTimeClassificationHistogramFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton AutoPlayButton;
    private javax.swing.JInternalFrame ControlFrame;
    private javax.swing.JLabel DurationLabel;
    private javax.swing.JLabel DurationValue;
    private javax.swing.JList FileList;
    private javax.swing.JInternalFrame FileListFrame;
    private javax.swing.JScrollPane FileListScrollPane;
    private javax.swing.JDesktopPane HistogramDesktop;
    private javax.swing.JInternalFrame HistogramInternalPanel;
    private javax.swing.JButton PlayButton;
    private javax.swing.JLabel PositionLabel;
    private javax.swing.JLabel PositionValue;
    private javax.swing.JButton SkipForwardButton;
    private javax.swing.JButton StopButton;
    private org.imirsel.m2k.vis.HistogramPanel histogramPanel1;
    // End of variables declaration//GEN-END:variables

}
