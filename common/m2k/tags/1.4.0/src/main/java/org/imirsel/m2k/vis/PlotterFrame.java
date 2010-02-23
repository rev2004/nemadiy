/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.imirsel.m2k.vis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.imirsel.m2k.io.file.AudioPlayer;
import org.imirsel.m2k.io.file.AudioPlayerFactory;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kriswest
 */
public class PlotterFrame extends JPanel implements ActionListener, ListSelectionListener {
    private DecimalFormat decimal;

    //Current selection info
    private String selectedFile = null;
    private String[] selectedCols = null;
    private String selectedTranscription = null;
    private boolean SegmentationAvailable = false;

    //list and datastores for Signal objects/audio filenames
    private JList fileList;
    private	JScrollPane FileListScrollPane;
    private Vector fileListVector;   //<keep these two synchronised so that there is no need for a hashtable
    private Vector signalListVector; //<

    //list and datastores for a Signal object's columns
    private JList columnList;
    private	JScrollPane ColumnScrollPane;
    private Vector columnListVector;
    private Hashtable fileLocationsToColumnList;
    private Vector fileLocationsToColumnVector;

    //list and datastores for a Signal's plotable metadata
    private JList metadataList;
    private	JScrollPane MetadataScrollPane;
    private Vector metadataListVector;
    private Hashtable fileLocationsToMetadataList;
    private Vector fileLocationsToMetadataVector;

    //JPanels to hold two sides of the plotter window
    /**  Holds file, column and metadata lists*/
    private JPanel leftSide;
    /**  Holds the plots and plot buttons */
    private JPanel rightSide;

    /** Button panel on rightSide*/
    private JPanel buttonPanel;
    /** Plot panel on rightSide*/
    private JPanel plotPanel;
    /** Scroll panel for plots*/
    private JScrollPane scrollPlot;
    /** Storage with scroll port width and height */
    private int scrollWidth, scrollHeight;
    /** Sub panel to hold plots */
    private JPanel subPlotPanel;
    /** A vector to hold the plot objects occupying the plotPanel*/
    private Vector Plots;
    /** Max Plot Grid size  */
    private static final int MAXGRIDSIZE = 7;

    //Buttons
    private JButton ButtonPlotSignal;
    private JButton ButtonPlotSpectrum;
    private JButton ButtonPlotAudioSpectrum;
    private JButton ButtonPlotTranscription;
    private JButton ButtonClearPlots;
    private JButton ButtonPlay;
    private JButton ButtonStop;
    private JButton ButtonChangeTime;
    private JButton ButtonRemovePlot;
    private JTextField FieldRemove;

    //Segment to plot time indices
    //private double currentStart = -1.0;
    //private double currentEnd = -1.0;
    private JTextField FieldCurrentStart;
    private JTextField FieldCurrentEnd;
    private JTextField FileDuration;

    /** ArrayList holding metadata labels for transcription metadatas */
    private ArrayList transcriptions;

    /*Playback device*/
    private AudioPlayer thePlayer = null;

    /* Constructs the plotter window */
    public PlotterFrame(){
        super();
        this.setBounds(0,0,640,480);
        decimal = new DecimalFormat();
        decimal.setMaximumFractionDigits(3);
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setBackground( Color.gray );

        Plots = new Vector();

        //Add transcription metadata labels to transcription labels list
        transcriptions = new ArrayList();
        transcriptions.add(Signal.PROP_DRUM_TRANSCRIPTION);
        transcriptions.add(Signal.PROP_SEGMENTATION_TRANSCRIPTION);
        transcriptions.add(Signal.PROP_CLASSIFIER_TRANSCRIPTION);
        for (int i = 2; i <= 80; i++) {
            transcriptions.add("SegmentTranscription_" + i + "_state");
        }
        
        
//TODO: Add more transcription metadatas to standards list


        //Disabled as this closes D2K
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Get main window panel
        this.setLayout(new GridBagLayout());
        //this.setSize(1000, 800);

        //Create panels for left and right side, add to main window
        this.leftSide = new JPanel();
        this.rightSide = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.fill = c.BOTH;
        leftSide.setMinimumSize(new Dimension(150, 480));
        leftSide.setBackground(Color.white);
        rightSide.setMinimumSize(new Dimension(480, 480));
        this.rightSide.setLayout(new GridBagLayout());
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSide, rightSide);
        split.setDividerLocation(260);
        split.setOneTouchExpandable(true);
        this.add(split,c);

        // Create the list data models
        this.fileListVector = new Vector();
        this.signalListVector = new Vector();
        this.fileList = new JList(this.fileListVector);
        this.fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.fileList.addListSelectionListener(this);
        this.FileListScrollPane = new JScrollPane();
        this.FileListScrollPane.getViewport().add(this.fileList);
        this.FileListScrollPane.getViewport().setMinimumSize(new Dimension(150, 275));
        this.FileListScrollPane.setWheelScrollingEnabled(true);
        Border etch = BorderFactory.createEtchedBorder(  );
        this.FileListScrollPane.setBorder(BorderFactory.createTitledBorder(etch, "Signal Objects"));

        this.columnListVector = new Vector();
        this.columnList = new JList(this.columnListVector);
        this.columnList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.columnList.addListSelectionListener(this);
        this.ColumnScrollPane = new JScrollPane();
        this.ColumnScrollPane.getViewport().add(this.columnList);
        this.ColumnScrollPane.getViewport().setMinimumSize(new Dimension(150, 200));
        this.ColumnScrollPane.setWheelScrollingEnabled(true);
        this.ColumnScrollPane.setBorder(BorderFactory.createTitledBorder(etch, "Data columns"));

        this.metadataListVector = new Vector();
        this.metadataList = new JList(this.metadataListVector);
        this.metadataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.metadataList.addListSelectionListener(this);
        this.MetadataScrollPane = new JScrollPane();
        this.MetadataScrollPane.getViewport().add(this.metadataList);
        this.MetadataScrollPane.getViewport().setMinimumSize(new Dimension(150, 200));
        this.MetadataScrollPane.setWheelScrollingEnabled(true);
        this.MetadataScrollPane.setBorder(BorderFactory.createTitledBorder(etch, "Transcriptions"));

        //Create Hash tables for lists
        this.fileLocationsToColumnList = new Hashtable();
        this.fileLocationsToColumnVector = new Vector();
        this.fileLocationsToMetadataList = new Hashtable();
        this.fileLocationsToMetadataVector = new Vector();




        //Add lists to leftSide
        this.leftSide.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.fill = c.BOTH;
        this.leftSide.add(this.FileListScrollPane,c);
        c.gridy += c.gridheight;
        c.gridheight = 1;
        c.weighty = 0.25;
        this.leftSide.add(this.ColumnScrollPane,c);
        c.gridy += c.gridheight;
        this.leftSide.add(this.MetadataScrollPane,c);

        //Create plot panel and add to right side
        this.plotPanel = new JPanel();
        this.plotPanel.setBorder(BorderFactory.createTitledBorder(etch, "Plots"));
        this.plotPanel.setLayout(new GridBagLayout());
        //this.plotPanel.setOpaque(true);
        this.plotPanel.setMinimumSize(new Dimension(480,480));
        //this.plotPanel.setMaximumSize(new Dimension(1600,1000));

        scrollPlot = new JScrollPane();
        //scrollPlot.setOpaque(true);
        scrollPlot.setWheelScrollingEnabled(true);
        scrollPlot.getVerticalScrollBar().setBlockIncrement(20);
        scrollPlot.getVerticalScrollBar().setUnitIncrement(10);
        scrollPlot.setHorizontalScrollBarPolicy(scrollPlot.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPlot.setVerticalScrollBarPolicy(scrollPlot.VERTICAL_SCROLLBAR_ALWAYS);

        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.fill = c.BOTH;

        this.plotPanel.add(scrollPlot,c);
        scrollWidth = scrollPlot.getWidth();
        scrollHeight = scrollPlot.getHeight();
        this.rightSide.add(this.plotPanel,c);
        this.scrollPlot.setPreferredSize(new Dimension(scrollWidth,scrollHeight));

        //Create button panel and add to right side
        this.buttonPanel = createButtons();
        this.buttonPanel.setOpaque(true);
        this.buttonPanel.setMinimumSize(new Dimension(480,200));
        c.gridy += c.gridheight;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        this.rightSide.add(this.buttonPanel,c);
        this.validate();
        this.setVisible(true);
    }

    /* Creates the buttons used to make plots and play clips
     */
    private JPanel createButtons(){
        //Add buttons to button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.ButtonPlotSignal = new JButton("Plot Signal");
        this.ButtonPlotSignal.addActionListener( this );
        this.ButtonPlotSpectrum = new JButton("Plot Spectrum");
        this.ButtonPlotSpectrum.addActionListener( this );
        this.ButtonPlotAudioSpectrum = new JButton("Plot Audio Spectrum");
        this.ButtonPlotAudioSpectrum.addActionListener( this );
        this.ButtonPlotTranscription = new JButton("Plot Transcription");
        this.ButtonPlotTranscription.addActionListener( this );
        this.ButtonClearPlots = new JButton("Clear Plots");
        this.ButtonClearPlots.addActionListener( this );
        this.ButtonPlay = new JButton("Play");
        this.ButtonPlay.addActionListener( this );
        this.ButtonStop = new JButton("Stop");
        this.ButtonStop.addActionListener( this );

        this.ButtonChangeTime = new JButton("Change time Indices");
        this.ButtonChangeTime.addActionListener( this );
        this.ButtonRemovePlot = new JButton("Remove plot");
        this.ButtonRemovePlot.addActionListener( this );
        this.FieldRemove = new JTextField("");

        //init first row
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = c.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0 / 4.0;
        c.weighty = 0;
        buttonPanel.add(this.ButtonPlotSignal, c);
        c.gridx += c.gridwidth;
        buttonPanel.add(this.ButtonPlotSpectrum, c);
        c.gridx += c.gridwidth;
        buttonPanel.add(this.ButtonPlotAudioSpectrum, c);
        c.gridx += c.gridwidth;
        buttonPanel.add(this.ButtonPlotTranscription, c);

        //init second row
        c.gridwidth = 4;
        c.weightx = 1.0 / 3.0;
        c.gridx = 0;
        c.gridy = 1;
        buttonPanel.add(this.ButtonClearPlots, c);
        c.gridx += c.gridwidth;
        buttonPanel.add(this.ButtonPlay, c);
        c.gridx += c.gridwidth;
        buttonPanel.add(this.ButtonStop, c);

        //init third row
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1.0 / 6.0;
        c.insets = new Insets(2, 5, 2, 5);
        JLabel startLabel = new JLabel("Start time: ");
        buttonPanel.add(startLabel, c);
        c.gridx += c.gridwidth;
        this.FieldCurrentStart = new JTextField("0.00");
        this.FieldCurrentStart.setColumns(6);
        buttonPanel.add(this.FieldCurrentStart, c);
        c.gridx += c.gridwidth;
        JLabel endLabel = new JLabel("End time: ");
        buttonPanel.add(endLabel, c);
        c.gridx += c.gridwidth;
        this.FieldCurrentEnd = new JTextField("-1.0");
        this.FieldCurrentEnd.setColumns(6);
        buttonPanel.add(this.FieldCurrentEnd, c);
        c.gridx += c.gridwidth;
        JLabel DurationLabel = new JLabel("File Duration: ");
        buttonPanel.add(DurationLabel, c);
        c.gridx += c.gridwidth;
        this.FileDuration = new JTextField("");
        this.FileDuration.setColumns(6);
        this.FileDuration.setEditable(false);
        buttonPanel.add(this.FileDuration, c);

        //init fourth row
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 4;
        c.weightx = 1.0 / 3.0;
        c.insets = new Insets(0, 0, 0, 0);
        buttonPanel.add(this.ButtonChangeTime,c);
        c.gridx += c.gridwidth;
        buttonPanel.add(this.ButtonRemovePlot,c);
        c.gridx += c.gridwidth;
        buttonPanel.add(this.FieldRemove,c);

        return buttonPanel;
    }

    /* Plays a segment from a file
       @param fileLocation The file to play
       @param start Start time
       @param end End time
     */
    public void quickPlay(String fileLocation, double start, double end){
        if (fileLocation != null) {
            File anAudioFile = new File(fileLocation);
            System.out.println("Playing: " + anAudioFile.getPath() + ", start: " + start + ", end: " + end);
            if  (thePlayer != null)
            {
                thePlayer.stop();
            }
            thePlayer = AudioPlayerFactory.createAudioPlayer(anAudioFile);
            thePlayer.play(anAudioFile, start, end);
        }
    }

    /* Stops the player */
    public void stop(){
        if (thePlayer != null) {
            thePlayer.stop();
            thePlayer = null;
        }
    }

    /* Called when the file list selection is changed and used to display
     * relevant column and metadata lists.
     * @param file The file that has been selected.
     */
    public void setFileSelection(int selected_index) {
        this.columnListVector = (Vector)this.fileLocationsToColumnVector.get(selected_index);
        if (columnListVector != null) {
            this.columnList.setListData(columnListVector);
        } else {
            this.columnList.setListData(new Vector());
        }
        this.metadataListVector = (Vector)this.fileLocationsToMetadataVector.get(selected_index);
        if (metadataListVector != null) {
            this.metadataList.setListData(metadataListVector);
        } else {
            this.metadataList.setListData(new Vector());
        }
        this.selectedCols = null;
        this.selectedTranscription = null;
        this.FieldCurrentStart.setText("0.00");
        Signal theSig = (Signal)this.signalListVector.get(selected_index);
        try{

            double dur = theSig.getDoubleMetadata(Signal.PROP_DURATION);
            this.FileDuration.setText("" + this.decimal.format(dur));
            this.FieldCurrentEnd.setText("" + this.decimal.format(dur));
        }catch(noMetadataException nme) {
            this.FileDuration.setText("Unknown");
            this.FieldCurrentEnd.setText("Unknown");
        }

        //update display
        this.ColumnScrollPane.revalidate();
        this.MetadataScrollPane.revalidate();
        this.ColumnScrollPane.repaint();
        this.MetadataScrollPane.repaint();
    }

    // Handler for list selection changes
    public void valueChanged( ListSelectionEvent event ) {
        // See if this is a listbox selection and the
        // event stream has settled
        if (!event.getValueIsAdjusting()) {
            if( event.getSource() == this.fileList) {
                // Get the current selection and do something with it
                this.selectedFile = (String)this.fileList.getSelectedValue();
                int SigIdx = this.fileList.getSelectedIndex();
                if( this.selectedFile != null ) {
                    //setFileSelection(selectedFile);
                    setFileSelection(this.fileList.getSelectedIndex());
                    try{
                        double[] onTimes = ((Signal)this.signalListVector.get(SigIdx)).getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES);
                        this.SegmentationAvailable = true;
                    }catch(noMetadataException nme) {
                        this.SegmentationAvailable = false;
                        //do nothing
                    }
                }
            } else if( event.getSource() == this.columnList) {
                Object[] vals = this.columnList.getSelectedValues();
                if ((vals != null)&& (vals.length != 0)) {
                    String[] cols = new String[vals.length];
                    for (int i=0;i<vals.length; i++) {
                        cols[i] = (String)vals[i];
                    }
                    this.selectedCols = cols;
                }
            } else if( event.getSource() == this.metadataList) {
                String val = (String)this.metadataList.getSelectedValue();
                if (val != null) {
                    this.selectedTranscription = val;
                }
            }
        }
    }

    public void addSignal(Signal theSignal) {
        try{
            String fileLocation = theSignal.getStringMetadata(Signal.PROP_FILE_LOCATION);
            System.out.println("Signal Plotter: Adding " + fileLocation);
            //add Signal and file to lists
            double duration = AudioPlayerFactory.createAudioPlayer(theSignal.getFile()).getDuration(theSignal.getFile());
            theSignal.setMetadata(Signal.PROP_DURATION,duration);

            this.fileListVector.add(fileLocation);
            this.signalListVector.add(theSignal);
            //save current file list selection
            int selection = this.fileList.getSelectedIndex();
            int[] colSelection = this.columnList.getSelectedIndices();
            int metaSelection = this.metadataList.getSelectedIndex();
            this.fileList.setListData(this.fileListVector);
            this.fileList.setSelectedIndex(selection);


            //add columns to vector and index with hashmap
            Vector columnsVector = new Vector();
            String[] cols = theSignal.getStringArrayMetadata(Signal.PROP_COLUMN_LABELS);
            for (int i=0;i<cols.length;i++) {
                columnsVector.add(cols[i]);
            }
            this.fileLocationsToColumnList.put(fileLocation, columnsVector);
            this.fileLocationsToColumnVector.add(columnsVector);

            //add transcription metadatas to vector and index with hashmap
            Vector metadataVector = new Vector();
            String[] meta = theSignal.metadataKeys();
            //System.out.println("Metadata keys available:");
            for (int i=0;i<meta.length;i++) {
                //System.out.println("\t" + meta[i]);
                if (transcriptions.indexOf(meta[i]) != -1) {
                    metadataVector.add(meta[i]);
                }
            }
            this.fileLocationsToMetadataList.put(fileLocation, metadataVector);
            this.fileLocationsToMetadataVector.add(metadataVector);

            //this.FileListScrollPane.revalidate();
            this.FileListScrollPane.repaint();

        }catch(noMetadataException nme) {
            throw new RuntimeException("Required metadata not found!\n" + nme.getMessage(), nme);
        }
    }

    // Handler for button presses
    public void actionPerformed( ActionEvent event ) {
        if( event.getSource() == this.ButtonPlay ) {
            //get current segment selections
            //play segment
            this.quickPlay(this.selectedFile, Double.parseDouble(this.FieldCurrentStart.getText()), Double.parseDouble(this.FieldCurrentEnd.getText()));
        }else if( event.getSource() == this.ButtonStop ) {
            if (thePlayer != null) {
                this.stop();
            }
        }else if( event.getSource() == this.ButtonPlotSignal) {
            ///////Plot Signal
            if ((this.selectedFile == null)||(this.selectedCols == null)) {
                JOptionPane.showMessageDialog(this,"Both a file and at least one data column must be selected!", "Unable to plot!", JOptionPane.ERROR_MESSAGE);
            }else {
                LinePlot aPlot = new LinePlot();
                //Signal theSig = (Signal)this.signalListVector.get(this.fileListVector.indexOf(this.selectedFile));
                Signal theSig = (Signal)this.signalListVector.get(this.fileList.getSelectedIndex());
                aPlot.setData(theSig,this.selectedCols);
                if (this.SegmentationAvailable) {
                    try{
                        aPlot.setSegmentation(theSig.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES));
                    } catch(noMetadataException nme) {
                        //do nothing
                    }
                }
                try{
                    double[] gtSegmentation = theSig.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES_GT);
                    aPlot.setGTSegmentation(gtSegmentation); 
                }
                catch(noMetadataException nme)
                {

                }

                try{
                    double start = Double.parseDouble(this.FieldCurrentStart.getText());
                    double end = Double.parseDouble(this.FieldCurrentEnd.getText());
                    aPlot.setPlotTimeIndices(start, end);
                }catch(NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this,"Unable to interpret start or end time!\n" + nfe.getMessage(), "Invalid time index!", JOptionPane.ERROR_MESSAGE);
                    int start = 0;
                    int end = theSig.getNumRows();
                    aPlot.setPlotTimeIndices(start, end);
                }

                //Add the plot and set its size (out of 10)
                this.addPlot(aPlot, 3);
            }
        }else if( event.getSource() == this.ButtonPlotSpectrum) {
            ///////PLOT SPECTRUM
            if ((this.selectedFile == null)||(this.selectedCols == null)) {
                JOptionPane.showMessageDialog(this,"Both a file and some data columns must be selected!", "Unable to plot!", JOptionPane.ERROR_MESSAGE);
            }else if (this.selectedCols.length <= 1) {
                JOptionPane.showMessageDialog(this,"More than one data column must be selected to plot a spectrum!", "Unable to plot!", JOptionPane.ERROR_MESSAGE);
            } else {
                SpectrumPlot aPlot = new SpectrumPlot();
                Signal theSig = (Signal)this.signalListVector.get(this.fileList.getSelectedIndex());
                aPlot.setData(theSig,this.selectedCols);
                if (this.SegmentationAvailable) {
                    try{
                        aPlot.setSegmentation(theSig.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES));
                    } catch(noMetadataException nme) {
                        //do nothing
                    }
                }
                try{
                    double start = Double.parseDouble(this.FieldCurrentStart.getText());
                    double end = Double.parseDouble(this.FieldCurrentEnd.getText());
                    aPlot.setPlotTimeIndices(start, end);
                }catch(NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this,"Unable to interpret start or end time!\n" + nfe.getMessage(), "Invalid time index!", JOptionPane.ERROR_MESSAGE);
                    int start = 0;
                    int end = theSig.getNumRows();
                    aPlot.setPlotTimeIndices(start, end);
                }

                //Add the plot and set its size (out of 10)
                this.addPlot(aPlot, 4);


            }
        }else if( event.getSource() == this.ButtonPlotAudioSpectrum) {
            ///////PLOT AUDIO SPECTRUM FROM FILE
            if (this.selectedFile == null) {
                JOptionPane.showMessageDialog(this,"A file must be selected in order to plot an audio spectrum!", "Unable to plot!", JOptionPane.ERROR_MESSAGE);
            } else {

                Signal theSig = ((Signal)this.signalListVector.get(this.fileList.getSelectedIndex())).cloneNoData();

                try
                {
                    System.out.println("Creating audio spectrum plot...");

                    AudioSpectrumPlot aPlot = new AudioSpectrumPlot();

                    aPlot.setData(theSig);
                    if (this.SegmentationAvailable) {
                        try{
                            aPlot.setSegmentation(theSig.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES));
                        } catch(noMetadataException nme) {
                            //do nothing
                        }
                    }
                    try{
                        double start = Double.parseDouble(this.FieldCurrentStart.getText());
                        double end = Double.parseDouble(this.FieldCurrentEnd.getText());
                        System.out.println("Setting time indices to " + start + " - " + end);
                        aPlot.setPlotTimeIndices(start, end);
                    }catch(NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this,"Unable to interpret start or end time!\n" + nfe.getMessage(), "Invalid time index!", JOptionPane.ERROR_MESSAGE);
                        double start = 0;
                        double end = -1;
                        try {
                            end = theSig.getDoubleMetadata(Signal.PROP_DURATION);
                        } catch (noMetadataException ex) {
                            ex.printStackTrace();
                        }
                        System.out.println("Setting time indices to " + start + " - " + end);
                        aPlot.setPlotTimeIndices(start, end);
                    }

                    //Add the plot and set its size (out of 10)
                    this.addPlot(aPlot, 4);

                }catch(NullPointerException npe){
                    npe.printStackTrace();
                    JOptionPane.showMessageDialog(this,"A Null pointer Exception occured! Perhaps the file could not be decoded:\n" + npe.getMessage(), "Null Pointer Exception!", JOptionPane.ERROR_MESSAGE);
                }


            }
        }else if( event.getSource() == this.ButtonPlotTranscription) {
            ///////PLOT TRANSCRIPTION
            if ((this.selectedFile == null)||(this.selectedTranscription == null)) {
                JOptionPane.showMessageDialog(this,"Both a file and a transcription must be selected!", "Unable to plot!", JOptionPane.ERROR_MESSAGE);
            } else if (!this.SegmentationAvailable) {
                JOptionPane.showMessageDialog(this,"The selected file has no Segmentation. A Segmentation is required in order to plot transcriptions.", "Unable to plot!", JOptionPane.ERROR_MESSAGE);
            } else {
                TranscriptionPlot aPlot = new TranscriptionPlot();
                Signal theSig = (Signal)this.signalListVector.get(this.fileList.getSelectedIndex());
                try{
                    aPlot.setData(theSig, this.selectedTranscription, theSig.getDoubleArrayMetadata(Signal.PROP_ONSET_TIMES));
                } catch(noMetadataException nme) {
                    JOptionPane.showMessageDialog(this,"The selected file has no Segmentation. A Segmentation is required in order to plot transcriptions.", "Unable to plot!", JOptionPane.ERROR_MESSAGE);
                }

                try{
                    double start = Double.parseDouble(this.FieldCurrentStart.getText());
                    double end = Double.parseDouble(this.FieldCurrentEnd.getText());
                    aPlot.setPlotTimeIndices(start, end);
                }catch(NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this,"Unable to interpret start or end time!\n" + nfe.getMessage(), "Invalid time index!", JOptionPane.ERROR_MESSAGE);
                    int start = 0;
                    int end = theSig.getNumRows();
                    aPlot.setPlotTimeIndices(start, end);
                }
                //Add the plot and set its size (out of 10)
                this.addPlot(aPlot, 1);
            }
        }else if( event.getSource() == this.ButtonClearPlots) {
            //////CLEAR PLOTS
            this.Plots.clear();
            this.layoutPlots();
        }else if( event.getSource() == this.ButtonChangeTime ) {
            try{
                double start = Double.parseDouble(this.FieldCurrentStart.getText());
                double end = Double.parseDouble(this.FieldCurrentEnd.getText());
                this.setAllPlotTimeIndices(start, end);
            }catch(NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this,"Unable to interpret start or end time!\n" + nfe.getMessage(), "Invalid time index!", JOptionPane.ERROR_MESSAGE);
            }
        }else if( event.getSource() == this.ButtonRemovePlot ) {
            ///////REMOVE PLOT
            try{
                int toRemove = Integer.parseInt(this.FieldRemove.getText());
                if (Plots.size() > toRemove) {
                    this.removePlot(toRemove);
                } else if (Plots.size() == 0){
                    JOptionPane.showMessageDialog(this, "No plots to remove!", "Unable to remove!", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Supplied index (" + toRemove + ") is is invalid, plots numbers 0 - " + (Plots.size()-1) + " are valid", "Unable to remove!", JOptionPane.ERROR_MESSAGE);
                }


            } catch(NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Unable to interpret plot number to remove", "Unable to remove!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void addPlot(SignalPlotObject aPlot, int plotSize) {
        //System.out.println("add plot");
        Plots.add(aPlot);
        this.layoutPlots();
    }

    public void removePlot(int plotIdx) {
        Plots.remove(plotIdx);
        this.layoutPlots();
    }

    public void setAllPlotTimeIndices(double start, double end) {
        for (int i=0;i<Plots.size();i++) {
            ((SignalPlotObject)Plots.get(i)).setPlotTimeIndices(start, end);
        }
        this.layoutPlots();
    }

    public void layoutPlots() {
        System.out.println("layout plots");
        if (this.subPlotPanel == null) {
            this.subPlotPanel = new JPanel();
            this.subPlotPanel.setLayout(new GridBagLayout());
            this.scrollPlot.setViewportView(subPlotPanel);
        } else {
            this.subPlotPanel.removeAll();
        }

        int total = 0;

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = c.HORIZONTAL;

        for (int i=0;i<Plots.size();i++) {
            //int H = (int)Math.round((((float)this.scrollPlot.getViewport().getHeight() / (float)this.MAXGRIDSIZE)) * ((Integer)this.PlotSizes.get(i)).intValue());
            //int H = 125 * ((Integer)this.PlotSizes.get(i)).intValue();
            int H = ((SignalPlotObject)this.Plots.get(i)).getPlotObjectHeight();
            ((SignalPlotObject)this.Plots.get(i)).setPreferredSize(new Dimension(400,H));//this.scrollPlot.getViewport().getWidth(),H));
            ((SignalPlotObject)this.Plots.get(i)).setMinimumSize(new Dimension(200,H));
            //((SignalPlotObject)this.Plots.get(i)).setSize(new Dimension(this.scrollPlot.getViewport().getWidth(),minHeight));
            subPlotPanel.add(((SignalPlotObject)this.Plots.get(i)),c);
            //System.out.println("minHeight: " + minHeight);
            //this.plotPanel.getViewport().add((SignalPlotObject)this.Plots.get(i),c);
            ((SignalPlotObject)this.Plots.get(i)).validate();
            c.gridy++;
        }
        subPlotPanel.validate();
        subPlotPanel.repaint();
        //plotPanel.validate();
        //plotPanel.repaint();
    }
}