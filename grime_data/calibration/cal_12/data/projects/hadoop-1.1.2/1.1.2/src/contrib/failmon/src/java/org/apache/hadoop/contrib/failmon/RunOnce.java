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

import java.util.ArrayList;

/**********************************************************
* Runs a set of monitoring jobs once for the local node. The set of
* jobs to be run is the intersection of the jobs specifed in the
* configuration file and the set of jobs specified in the --only
* command line argument.
 **********************************************************/ 

public class RunOnce {

  LocalStore lstore;

  ArrayList<MonitorJob> monitors;
  
  boolean uploading = true;
  
  public RunOnce(String confFile) {
    
    Environment.prepare(confFile);
    
    String localTmpDir;
    
    // running as a stand-alone application
    localTmpDir = System.getProperty("java.io.tmpdir");
    Environment.setProperty("local.tmp.dir", localTmpDir);
        
    monitors = Environment.getJobs();
    lstore = new LocalStore();
    uploading  = true;
  }

  private void filter (String [] ftypes) {
    ArrayList<MonitorJob> filtered = new ArrayList<MonitorJob>();
    boolean found;
    
    // filter out unwanted monitor jobs
    for (MonitorJob job : monitors) {
      found = false;
      for (String ftype : ftypes)
	if (job.type.equalsIgnoreCase(ftype))
	    found = true;
      if (found)
	filtered.add(job);
    }

    // disable uploading if not requested
    found = false;
    for (String ftype : ftypes)
      if (ftype.equalsIgnoreCase("upload"))
	found = true;

    if (!found)
      uploading = false;
    
    monitors = filtered;
  }
  
  private void run() {
    
    Environment.logInfo("Failmon started successfully.");

    for (int i = 0; i < monitors.size(); i++) {
      Environment.logInfo("Calling " + monitors.get(i).job.getInfo() + "...\t");
      monitors.get(i).job.monitor(lstore);
    }

    if (uploading)
      lstore.upload();

    lstore.close();
  }

  public void cleanup() {
    // nothing to be done
  }

  
  public static void main (String [] args) {

    String configFilePath = "./conf/failmon.properties";
    String [] onlyList = null;
    
    // Parse command-line parameters
    for (int i = 0; i < args.length - 1; i++) {
      if (args[i].equalsIgnoreCase("--config"))
	configFilePath = args[i + 1];
      else if (args[i].equalsIgnoreCase("--only"))
	onlyList = args[i + 1].split(",");
    }

    RunOnce ro = new RunOnce(configFilePath);
    // only keep the requested types of jobs
    if (onlyList != null)
      ro.filter(onlyList);
    // run once only
    ro.run();
  }

}
