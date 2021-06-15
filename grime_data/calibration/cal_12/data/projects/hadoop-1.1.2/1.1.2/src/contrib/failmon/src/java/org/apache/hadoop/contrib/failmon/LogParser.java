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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

/**********************************************************
 * This class represents objects that provide log parsing 
 * functionality. Typically, such objects read log files line
 * by line and for each log entry they identify, they create a 
 * corresponding EventRecord. In this way, disparate log files
 * can be merged using the uniform format of EventRecords and can,
 * thus, be processed in a uniform way.
 * 
 **********************************************************/

public abstract class LogParser implements Monitored {

  File file;

  BufferedReader reader;

  String hostname;

  Object [] ips;

  String dateformat;

  String timeformat;

  private String firstLine;
  private long offset;

  /**
   * Create a parser that will read from the specified log file.
   * 
   * @param fname the filename of the log file to be read
   */
  public LogParser(String fname) {
    file = new File(fname);

    ParseState ps = PersistentState.getState(file.getAbsolutePath());
    firstLine = ps.firstLine;
    offset = ps.offset;
    
    try {
      reader = new BufferedReader(new FileReader(file));
      checkForRotation();
      Environment.logInfo("Checked for rotation...");
      reader.skip(offset);
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }

    setNetworkProperties();
  }

  protected void setNetworkProperties() {
    // determine hostname and ip addresses for the node
    try {
      // Get hostname
      hostname = InetAddress.getLocalHost().getCanonicalHostName();
      // Get all associated ip addresses
      ips = InetAddress.getAllByName(hostname);

    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  /**
   * Insert all EventRecords that can be extracted for
   * the represented hardware component into a LocalStore.
   * 
   * @param ls the LocalStore into which the EventRecords 
   * are to be stored.
   */
  public void monitor(LocalStore ls) {
    int in = 0;
    EventRecord er = null;
    Environment.logInfo("Started processing log...");

    while ((er = getNext()) != null) {
      // Environment.logInfo("Processing log line:\t" + in++);
      if (er.isValid()) {
        ls.insert(er);
      }
    }

    PersistentState.updateState(file.getAbsolutePath(), firstLine, offset);
    PersistentState.writeState("conf/parsing.state");
  }

  /**
   * Get an array of all EventRecords that can be extracted for
   * the represented hardware component.
   * 
   * @return The array of EventRecords
   */
  public EventRecord[] monitor() {

    ArrayList<EventRecord> recs = new ArrayList<EventRecord>();
    EventRecord er;

    while ((er = getNext()) != null)
      recs.add(er);

    EventRecord[] T = new EventRecord[recs.size()];

    return recs.toArray(T);
  }

  /**
   * Continue parsing the log file until a valid log entry is identified.
   * When one such entry is found, parse it and return a corresponding EventRecord.
   * 
   *  
   * @return The EventRecord corresponding to the next log entry
   */
  public EventRecord getNext() {
    try {
	String line = reader.readLine();
	if (line != null) {
	    if (firstLine == null)
		firstLine = new String(line);
	    offset += line.length() + 1;
	    return parseLine(line);
	}
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Return the BufferedReader, that reads the log file
   *  
   * @return The BufferedReader that reads the log file
   */
  public BufferedReader getReader() {
    return reader;
  }

  /**
   * Check whether the log file has been rotated. If so,
   * start reading the file from the beginning.
   *  
   */
  public void checkForRotation() {
    try {
      BufferedReader probe = new BufferedReader(new FileReader(file.getAbsoluteFile()));
      if (firstLine == null || (!firstLine.equals(probe.readLine()))) {
	probe.close();
	// start reading the file from the beginning
        reader.close();
        reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
	firstLine = null;
	offset = 0;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Parses one line of the log. If the line contains a valid 
   * log entry, then an appropriate EventRecord is returned, after all
   * relevant fields have been parsed.
   *
   *  @param line the log line to be parsed
   *
   *  @return the EventRecord representing the log entry of the line. If 
   *  the line does not contain a valid log entry, then the EventRecord 
   *  returned has isValid() = false. When the end-of-file has been reached,
   *  null is returned to the caller.
   */
  abstract public EventRecord parseLine(String line) throws IOException;

  /**
   * Parse a date found in Hadoop log file.
   * 
   * @return a Calendar representing the date
   */
  abstract protected Calendar parseDate(String strDate, String strTime);

}
