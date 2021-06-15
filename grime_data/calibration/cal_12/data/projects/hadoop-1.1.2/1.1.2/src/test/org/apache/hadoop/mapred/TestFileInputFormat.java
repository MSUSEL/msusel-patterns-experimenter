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
package org.apache.hadoop.mapred;

import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;

public class TestFileInputFormat extends TestCase {

  Configuration conf = new Configuration();
  MiniDFSCluster dfs = null;
  
  public void setUp() throws Exception {
    dfs = new MiniDFSCluster(conf, 4, true,
                             new String[]{"/rack0", "/rack0", 
                                          "/rack1", "/rack1"},
                             new String[]{"host0", "host1", 
                                          "host2", "host3"});
  }
  
  public void testLocality() throws Exception {
    JobConf job = new JobConf(conf);
    FileSystem fs = dfs.getFileSystem();
    System.out.println("FileSystem " + fs.getUri());

    Path inputDir = new Path("/foo/");
    String fileName = "part-0000";
    createInputs(fs, inputDir, fileName);

    // split it using a file input format
    TextInputFormat.addInputPath(job, inputDir);
    TextInputFormat inFormat = new TextInputFormat();
    inFormat.configure(job);
    InputSplit[] splits = inFormat.getSplits(job, 1);
    FileStatus fileStatus = fs.getFileStatus(new Path(inputDir, fileName));
    BlockLocation[] locations = 
      fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
    System.out.println("Made splits");

    // make sure that each split is a block and the locations match
    for(int i=0; i < splits.length; ++i) {
      FileSplit fileSplit = (FileSplit) splits[i];
      System.out.println("File split: " + fileSplit);
      for (String h: fileSplit.getLocations()) {
        System.out.println("Location: " + h);
      }
      System.out.println("Block: " + locations[i]);
      assertEquals(locations[i].getOffset(), fileSplit.getStart());
      assertEquals(locations[i].getLength(), fileSplit.getLength());
      String[] blockLocs = locations[i].getHosts();
      String[] splitLocs = fileSplit.getLocations();
      assertEquals(2, blockLocs.length);
      assertEquals(2, splitLocs.length);
      assertTrue((blockLocs[0].equals(splitLocs[0]) && 
                  blockLocs[1].equals(splitLocs[1])) ||
                 (blockLocs[1].equals(splitLocs[0]) &&
                  blockLocs[0].equals(splitLocs[1])));
    }

    assertEquals("Expected value of " + FileInputFormat.NUM_INPUT_FILES, 
                 1, job.getLong(FileInputFormat.NUM_INPUT_FILES, 0));
  }

  private void createInputs(FileSystem fs, Path inDir, String fileName) 
  throws IOException {
    // create a multi-block file on hdfs
    DataOutputStream out = fs.create(new Path(inDir, fileName), true, 4096, 
                                     (short) 2, 512, null);
    for(int i=0; i < 1000; ++i) {
      out.writeChars("Hello\n");
    }
    out.close();
    System.out.println("Wrote file");
  }
  
  public void testNumInputs() throws Exception {
    JobConf job = new JobConf(conf);
    FileSystem fs = dfs.getFileSystem();
    System.out.println("FileSystem " + fs.getUri());

    Path inputDir = new Path("/foo/");
    final int numFiles = 10;
    String fileNameBase = "part-0000";
    for (int i=0; i < numFiles; ++i) {
      createInputs(fs, inputDir, fileNameBase + String.valueOf(i));  
    }
    createInputs(fs, inputDir, "_meta");
    createInputs(fs, inputDir, "_temp");

    // split it using a file input format
    TextInputFormat.addInputPath(job, inputDir);
    TextInputFormat inFormat = new TextInputFormat();
    inFormat.configure(job);
    InputSplit[] splits = inFormat.getSplits(job, 1);

    assertEquals("Expected value of " + FileInputFormat.NUM_INPUT_FILES, 
                 numFiles, job.getLong(FileInputFormat.NUM_INPUT_FILES, 0));

  }
  
  public void tearDown() throws Exception {
    if (dfs != null) {
      dfs.shutdown();
    }
  }
}
