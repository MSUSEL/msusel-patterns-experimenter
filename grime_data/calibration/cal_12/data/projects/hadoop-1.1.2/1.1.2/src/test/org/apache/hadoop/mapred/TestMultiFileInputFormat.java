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

import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

public class TestMultiFileInputFormat extends TestCase{

  private static JobConf job = new JobConf();

  private static final Log LOG = LogFactory.getLog(TestMultiFileInputFormat.class);
  
  private static final int MAX_SPLIT_COUNT  = 10000;
  private static final int SPLIT_COUNT_INCR = 6000;
  private static final int MAX_BYTES = 1024;
  private static final int MAX_NUM_FILES = 10000;
  private static final int NUM_FILES_INCR = 8000;
  
  private Random rand = new Random(System.currentTimeMillis());
  private HashMap<String, Long> lengths = new HashMap<String, Long>();
  
  /** Dummy class to extend MultiFileInputFormat*/
  private class DummyMultiFileInputFormat extends MultiFileInputFormat<Text, Text> {
    @Override
    public RecordReader<Text,Text> getRecordReader(InputSplit split, JobConf job
        , Reporter reporter) throws IOException {
      return null;
    }
  }
  
  private Path initFiles(FileSystem fs, int numFiles, int numBytes) throws IOException{
    Path dir = new Path(System.getProperty("test.build.data",".") + "/mapred");
    Path multiFileDir = new Path(dir, "test.multifile");
    fs.delete(multiFileDir, true);
    fs.mkdirs(multiFileDir);
    LOG.info("Creating " + numFiles + " file(s) in " + multiFileDir);
    for(int i=0; i<numFiles ;i++) {
      Path path = new Path(multiFileDir, "file_" + i);
       FSDataOutputStream out = fs.create(path);
       if (numBytes == -1) {
         numBytes = rand.nextInt(MAX_BYTES);
       }
       for(int j=0; j< numBytes; j++) {
         out.write(rand.nextInt());
       }
       out.close();
       if(LOG.isDebugEnabled()) {
         LOG.debug("Created file " + path + " with length " + numBytes);
       }
       lengths.put(path.getName(), new Long(numBytes));
    }
    FileInputFormat.setInputPaths(job, multiFileDir);
    return multiFileDir;
  }
  
  public void testFormat() throws IOException {
    if(LOG.isInfoEnabled()) {
      LOG.info("Test started");
      LOG.info("Max split count           = " + MAX_SPLIT_COUNT);
      LOG.info("Split count increment     = " + SPLIT_COUNT_INCR);
      LOG.info("Max bytes per file        = " + MAX_BYTES);
      LOG.info("Max number of files       = " + MAX_NUM_FILES);
      LOG.info("Number of files increment = " + NUM_FILES_INCR);
    }
    
    MultiFileInputFormat<Text,Text> format = new DummyMultiFileInputFormat();
    FileSystem fs = FileSystem.getLocal(job);
    
    for(int numFiles = 1; numFiles< MAX_NUM_FILES ; 
      numFiles+= (NUM_FILES_INCR / 2) + rand.nextInt(NUM_FILES_INCR / 2)) {
      
      Path dir = initFiles(fs, numFiles, -1);
      BitSet bits = new BitSet(numFiles);
      for(int i=1;i< MAX_SPLIT_COUNT ;i+= rand.nextInt(SPLIT_COUNT_INCR) + 1) {
        LOG.info("Running for Num Files=" + numFiles + ", split count=" + i);
        
        MultiFileSplit[] splits = (MultiFileSplit[])format.getSplits(job, i);
        bits.clear();
        
        for(MultiFileSplit split : splits) {
          long splitLength = 0;
          for(Path p : split.getPaths()) {
            long length = fs.getContentSummary(p).getLength();
            assertEquals(length, lengths.get(p.getName()).longValue());
            splitLength += length;
            String name = p.getName();
            int index = Integer.parseInt(
                name.substring(name.lastIndexOf("file_") + 5));
            assertFalse(bits.get(index));
            bits.set(index);
          }
          assertEquals(splitLength, split.getLength());
        }
      }
      assertEquals(bits.cardinality(), numFiles);
      fs.delete(dir, true);
    }
    LOG.info("Test Finished");
  }
  
  public void testFormatWithLessPathsThanSplits() throws Exception {
    MultiFileInputFormat<Text,Text> format = new DummyMultiFileInputFormat();
    FileSystem fs = FileSystem.getLocal(job);     
    
    // Test with no path
    initFiles(fs, 0, -1);    
    assertEquals(0, format.getSplits(job, 2).length);
    
    // Test with 2 path and 4 splits
    initFiles(fs, 2, 500);
    assertEquals(2, format.getSplits(job, 4).length);
  }
  
  public static void main(String[] args) throws Exception{
    TestMultiFileInputFormat test = new TestMultiFileInputFormat();
    test.testFormat();
  }
}
