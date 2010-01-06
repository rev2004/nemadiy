/* file name  : FeatureGate.java
 * authors    : Paul Lamere (Paul.Lamere@sun.com)
 * created    : Mon Jul 25 06:19:42 2005
 *
 * modifications:
 *
 */

package org.imirsel.m2k.io.file;

import java.nio.channels.FileLock;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/** 
 * This class uses an 'inProcess' file lock to coordinate
 * the distribution of feature processing across multiple instances
 * of M2K/D2K. 
 *
 * The method 'testAndSetInProcess' will attempt to atomically
 * test for the existance of an 'InProcess' file. If it doesn't
 * exist it will create and lock it, otherwise the testAndSetInProcess
 * method will a flag indicating that the inProcess lock could
 * not be obtained.
 *
 * The method 'releaseInProcess' will release a previously obtained
 * lock file.
 *
 * @author Paul Lamere (Paul.Lamere@sun.com)
 */
public class FeatureGate {
    public final static int STAT_ALREADY_IN_PROCESS = 0;
    public final static int STAT_COMPLETE = 1;
    public final static int STAT_INCOMPLETE = 2;
    private final static String IN_PROCESS_SUFFIX = ".InProcess";
    private final static boolean verbose = true;
    private Map heldLocks = new HashMap();
    private Map heldFiles = new HashMap();

    // in test mode we simulate a system that holds the locks
    // for a long time, just to make sure that the locks are
    // working properly. Normally, the flag is false.

    private final static boolean testMode = false;

    /** 
     * Atomically tests to see if the in-process file is set
     * for this feature file. If the file doesn't exist, the
     * file is created.
     * @param force  if true force recalculation
     * @return STAT_ALREADY_IN_PROCESS if the file is already in proces,
     * returns STAT_COMPLETE if the file is already complete and
     * returns STAT_INCOMPLETE if the file needs processing. In the
     * STAT_INCOMPLETE state, the lock will be held and must be
     * released
     */
    public synchronized int testAndSetInProcess(boolean force, File featureFile) {
        int status;

        File inProcessFile = getInProcessFile(featureFile);

        try {
            if (verbose) {
                System.out.println("Trying to get: " + inProcessFile);
            }

            // try to get the lock on the inProcessFile. If we get it, then we 'own the lock'
            // we also keep track of the locks we own so we can release them later.

            RandomAccessFile raf =  null;
            
            if (!inProcessFile.exists())
            {
                if (!force && featureFile.exists()) {
                    // since the feature file already exists no need to set a lock
                    status = STAT_COMPLETE;
                } else {
                    status = STAT_INCOMPLETE;
                    try {
                        raf = new RandomAccessFile(inProcessFile,"rw");
                    } catch (IOException ioe) {
                        // wait a bit and try again
                        try {
                            Thread.sleep(3000l);
                        } catch (InterruptedException ie) { }
                        raf = new RandomAccessFile(inProcessFile,"rw");
                    }
                    //heldLocks.put(inProcessFile.getAbsolutePath(), lock);
                    heldFiles.put(inProcessFile.getAbsolutePath(), raf);
                    inProcessFile.deleteOnExit();
                }
            } else {
                try { //wait a bit and try again
                    Thread.sleep(500);
                } catch (InterruptedException ie) { }
                if (!inProcessFile.exists())
                {
                    if (!force && featureFile.exists()) {
                    // since the feature file already exists no need to set a lock
                        status = STAT_COMPLETE;
                    } else {
                        try {
                            raf = new RandomAccessFile(inProcessFile,"rw");
                        } catch (IOException ioe) {
                            // wait a bit and try again
                            try {
                                Thread.sleep(3000l);
                            } catch (InterruptedException ie) { }
                            raf = new RandomAccessFile(inProcessFile,"rw");
                        }
                        status = STAT_INCOMPLETE;
                        //heldLocks.put(inProcessFile.getAbsolutePath(), lock);
                        heldFiles.put(inProcessFile.getAbsolutePath(), raf);
                        inProcessFile.deleteOnExit();
                    }
                } else {
                    status = STAT_ALREADY_IN_PROCESS;
                }
            }


            if (verbose) {
                switch (status) {
                    case STAT_ALREADY_IN_PROCESS:
                        System.out.println("Already in process " + featureFile);
                            break;
                    case STAT_INCOMPLETE:
                        System.out.println("Processing " + featureFile);
                            break;
                    case STAT_COMPLETE:
                        System.out.println("Already complete " + featureFile);
                            break;
                }
            }
        } catch (IOException ioe) {
            
            throw new RuntimeException("Can't manipulate lock "
                    + inProcessFile + "\n" + ioe, ioe);
        }
        return status;
    }

    /** 
     * Releases the in process file
     * @param featureFile the file testAndSetInProcess
     */
    public void releaseInProcess(File featureFile) {
        File inProcessFile = getInProcessFile(featureFile);
        try {
            //FileLock lock = (FileLock) heldLocks.get(inProcessFile.getAbsolutePath());

            //if (lock == null) {
            //    throw new RuntimeException("Warning: trying to release " 
            //       + inProcessFile + " but it doesn't exist");
            //} else {
            //    lock.release();
            //    heldLocks.remove(inProcessFile.getAbsolutePath());
                RandomAccessFile raf = (RandomAccessFile) heldFiles.get(inProcessFile.getAbsolutePath());
                raf.close();
                heldFiles.remove(inProcessFile.getAbsolutePath());
                inProcessFile.delete();
            //}

            if (verbose) {
                System.out.println("Releasing " + inProcessFile);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Can't release lock "
                    + inProcessFile, ioe);
        }
    }


    /** 
     * Gets the 'in process' file associated with the feature file
     * @param file the feature fiel
     * @return the in process file
     */
    private File getInProcessFile(File file) {
        File dir = file.getParentFile();
        if (dir != null) {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
        }
        return new File(file.getAbsolutePath() +  IN_PROCESS_SUFFIX);
    }

    /** 
     * Clears all locks, returns the number of locks held
     * @return  the number of locks held
     */
    public int clearLocks() {
        int count = heldFiles.size();
//        for (Iterator i = heldLocks.keySet().iterator(); i.hasNext(); ) {
//            String path = (String) i.next();
//            File file = new File(path);
//            FileLock lock = (FileLock) heldLocks.get(path);
//            try {
//                lock.release();
//            } catch (IOException ioe) {
//                System.out.println("Couldn't release lock on" + path);
//            }
//            file.delete();
//        }
//        heldLocks = new HashMap();
        for (Iterator i = heldFiles.keySet().iterator(); i.hasNext();) {
            String path = (String) i.next();
            RandomAccessFile raf = (RandomAccessFile) heldFiles.remove(path);
            try {
                raf.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            File inProcessFile = new File(path);
            inProcessFile.delete();
        }
        return count;
    }
}
