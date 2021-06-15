/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.apache.hadoop.contrib.failmon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**********************************************************
 * This class can be used to anonymize logs independently of
 * Hadoop and the Executor. It parses the specified log file to
 * create log records for it and then passes them to the Anonymizer.
 * After they are anonymized, they are written to a local file,
 * which is then compressed and stored locally.
 * 
 **********************************************************/

public class OfflineAnonymizer {

  public enum LogType {
    HADOOP, SYSTEM
  };

  LogType logtype;

  File logfile;

  LogParser parser;

  /**
   * Creates an OfflineAnonymizer for a specific log file.
   * 
   * @param logtype the type of the log file. This can either be
   * LogFile.HADOOP or LogFile.SYSTEM
   * @param filename the path to the log file
   * 
   */  
  public OfflineAnonymizer(LogType logtype, String filename) {

    logfile = new File(filename);

    if (!logfile.exists()) {
      System.err.println("Input file does not exist!");
      System.exit(0);
    }

    if (logtype == LogType.HADOOP)
      parser = new HadoopLogParser(filename);
    else
      parser = new SystemLogParser(filename);
  }

  /**
   * Performs anonymization for the log file. Log entries are
   * read one by one and EventRecords are created, which are then
   * anonymized and written to the output.
   * 
   */
  public void anonymize() throws Exception {
    EventRecord er = null;
    SerializedRecord sr = null;

    BufferedWriter bfw = new BufferedWriter(new FileWriter(logfile.getName()
        + ".anonymized"));

    System.out.println("Anonymizing log records...");
    while ((er = parser.getNext()) != null) {
      if (er.isValid()) {
        sr = new SerializedRecord(er);
        Anonymizer.anonymize(sr);
        bfw.write(LocalStore.pack(sr).toString());
        bfw.write(LocalStore.RECORD_SEPARATOR);
      }
    }
    bfw.flush();
    bfw.close();
    System.out.println("Anonymized log records written to " + logfile.getName()
        + ".anonymized");

    System.out.println("Compressing output file...");
    LocalStore.zipCompress(logfile.getName() + ".anonymized");
    System.out.println("Compressed output file written to " + logfile.getName()
        + ".anonymized" + LocalStore.COMPRESSION_SUFFIX);
  }

  public static void main(String[] args) {

    if (args.length < 2) {
      System.out.println("Usage: OfflineAnonymizer <log_type> <filename>");
      System.out
          .println("where <log_type> is either \"hadoop\" or \"system\" and <filename> is the path to the log file");
      System.exit(0);
    }

    LogType logtype = null;

    if (args[0].equalsIgnoreCase("-hadoop"))
      logtype = LogType.HADOOP;
    else if (args[0].equalsIgnoreCase("-system"))
      logtype = LogType.SYSTEM;
    else {
      System.err.println("Invalid first argument.");
      System.exit(0);
    }

    OfflineAnonymizer oa = new OfflineAnonymizer(logtype, args[1]);

    try {
      oa.anonymize();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return;
  }
}
