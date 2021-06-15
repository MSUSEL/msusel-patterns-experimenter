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
package org.apache.hadoop.util;

import java.io.*;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

// Keeps track of which datanodes/tasktrackers are allowed to connect to the 
// namenode/jobtracker.
public class HostsFileReader {
  private Set<String> includes;
  private Set<String> excludes;
  private String includesFile;
  private String excludesFile;
  
  private static final Log LOG = LogFactory.getLog(HostsFileReader.class);

  public HostsFileReader(String inFile, 
                         String exFile) throws IOException {
    includes = new HashSet<String>();
    excludes = new HashSet<String>();
    includesFile = inFile;
    excludesFile = exFile;
    refresh();
  }

  private void readFileToSet(String filename, Set<String> set) throws IOException {
    File file = new File(filename);
    if (!file.exists()) {
      return;
    }
    FileInputStream fis = new FileInputStream(file);
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(fis));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] nodes = line.split("[ \t\n\f\r]+");
        if (nodes != null) {
          for (int i = 0; i < nodes.length; i++) {
            if (!nodes[i].equals("")) {
              set.add(nodes[i]);  // might need to add canonical name
            }
          }
        }
      }   
    } finally {
      if (reader != null) {
        reader.close();
      }
      fis.close();
    }  
  }

  public synchronized void refresh() throws IOException {
    LOG.info("Refreshing hosts (include/exclude) list");
    if (!includesFile.equals("")) {
      Set<String> newIncludes = new HashSet<String>();
      readFileToSet(includesFile, newIncludes);
      // switch the new hosts that are to be included
      includes = newIncludes;
    }
    if (!excludesFile.equals("")) {
      Set<String> newExcludes = new HashSet<String>();
      readFileToSet(excludesFile, newExcludes);
      // switch the excluded hosts
      excludes = newExcludes;
    }
  }

  public synchronized Set<String> getHosts() {
    return includes;
  }

  public synchronized Set<String> getExcludedHosts() {
    return excludes;
  }

  public synchronized void setIncludesFile(String includesFile) {
    LOG.info("Setting the includes file to " + includesFile);
    this.includesFile = includesFile;
  }
  
  public synchronized void setExcludesFile(String excludesFile) {
    LOG.info("Setting the excludes file to " + excludesFile);
    this.excludesFile = excludesFile;
  }

  public synchronized void updateFileNames(String includesFile, 
                                           String excludesFile) 
                                           throws IOException {
    setIncludesFile(includesFile);
    setExcludesFile(excludesFile);
  }
}
