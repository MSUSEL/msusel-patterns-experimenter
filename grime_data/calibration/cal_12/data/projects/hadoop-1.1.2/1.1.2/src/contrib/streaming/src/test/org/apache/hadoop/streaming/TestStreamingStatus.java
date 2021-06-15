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
package org.apache.hadoop.streaming;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.File;

import junit.framework.TestCase;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.MiniMRCluster;
import org.apache.hadoop.mapred.TaskReport;

/**
 * Tests for the ability of a streaming task to set the status
 * by writing "reporter:status:" lines to stderr. Uses MiniMR
 * since the local jobtracker doesn't track status.
 */
public class TestStreamingStatus extends TestCase {
  private static String TEST_ROOT_DIR =
    new File(System.getProperty("test.build.data","/tmp"))
    .toURI().toString().replace(' ', '+');
  protected String INPUT_FILE = TEST_ROOT_DIR + "/input.txt";
  protected String OUTPUT_DIR = TEST_ROOT_DIR + "/out";
  protected String input = "roses.are.red\nviolets.are.blue\nbunnies.are.pink\n";
  protected String map = StreamUtil.makeJavaCommand(StderrApp.class, new String[]{"3", "0", "0", "true"});

  protected String[] genArgs(int jobtrackerPort) {
    return new String[] {
      "-input", INPUT_FILE,
      "-output", OUTPUT_DIR,
      "-mapper", map,
      "-jobconf", "mapred.map.tasks=1",
      "-jobconf", "mapred.reduce.tasks=0",      
      "-jobconf", "keep.failed.task.files=true",
      "-jobconf", "stream.tmpdir="+System.getProperty("test.build.data","/tmp"),
      "-jobconf", "mapred.job.tracker=localhost:"+jobtrackerPort,
      "-jobconf", "fs.default.name=file:///"
    };
  }
  
  public void makeInput(FileSystem fs) throws IOException {
    Path inFile = new Path(INPUT_FILE);
    DataOutputStream file = fs.create(inFile);
    file.writeBytes(input);
    file.close();
  }

  public void clean(FileSystem fs) {
    try {
      Path outDir = new Path(OUTPUT_DIR);
      fs.delete(outDir, true);
    } catch (Exception e) {}
    try {
      Path inFile = new Path(INPUT_FILE);    
      fs.delete(inFile, false);
    } catch (Exception e) {}
  }
  
  public void testStreamingStatus() throws Exception {
    MiniMRCluster mr = null;
    FileSystem fs = null;
    try {
      mr = new MiniMRCluster(1, "file:///", 3);

      Path inFile = new Path(INPUT_FILE);
      fs = inFile.getFileSystem(mr.createJobConf());
      clean(fs);
      makeInput(fs);
      
      StreamJob job = new StreamJob();
      int failed = job.run(genArgs(mr.getJobTrackerPort()));
      assertEquals(0, failed);

      TaskReport[] reports = job.jc_.getMapTaskReports(job.jobId_);
      assertEquals(1, reports.length);
      assertEquals("starting echo", reports[0].getState());
    } finally {
      if (fs != null) { clean(fs); }
      if (mr != null) { mr.shutdown(); }
    }
  }
}
