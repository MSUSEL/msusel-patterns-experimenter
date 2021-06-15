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
package org.apache.hadoop.mapreduce.split;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import junit.framework.TestCase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Progressable;
import org.apache.hadoop.util.StringUtils;

/**
 * A JUnit test to test limits on block locations
 */
public class TestBlockLimits extends TestCase {
  private static String TEST_ROOT_DIR =
    new File(System.getProperty("test.build.data","/tmp"))
    .toURI().toString().replace(' ', '+');
    
  public void testWithLimits()
      throws IOException, InterruptedException, ClassNotFoundException {
    MiniMRCluster mr = null;
    try {
      mr = new MiniMRCluster(2, "file:///", 3);
      Configuration conf = new Configuration();
      conf.setInt(JobSplitWriter.MAX_SPLIT_LOCATIONS, 10);
      mr = new MiniMRCluster(2, "file:///", 3, null, null, new JobConf(conf));
      runCustomFormat(mr);
    } finally {
      if (mr != null) { mr.shutdown(); }
    }
  }
  
  private void runCustomFormat(MiniMRCluster mr) throws IOException {
    JobConf job = new JobConf(mr.createJobConf());
    job.setInt(JobSplitWriter.MAX_SPLIT_LOCATIONS, 100);
    FileSystem fileSys = FileSystem.get(job);
    Path testDir = new Path(TEST_ROOT_DIR + "/test_mini_mr_local");
    Path outDir = new Path(testDir, "out");
    System.out.println("testDir= " + testDir);
    fileSys.delete(testDir, true);
    job.setInputFormat(MyInputFormat.class);
    job.setOutputFormat(MyOutputFormat.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    
    job.setMapperClass(MyMapper.class);        
    job.setNumReduceTasks(0);
    job.set("non.std.out", outDir.toString());
    try {
      JobClient.runJob(job);
      fail("JobTracker neglected to fail misconfigured job");
    } catch(IOException ie) {
      System.out.println("Failed job " + StringUtils.stringifyException(ie));
    } finally {
      fileSys.delete(testDir, true);
    }
    
  }
  
  static class MyMapper extends MapReduceBase
    implements Mapper<WritableComparable, Writable,
                    WritableComparable, Writable> {

    public void map(WritableComparable key, Writable value,
                  OutputCollector<WritableComparable, Writable> out,
                  Reporter reporter) throws IOException {
    }
  }

  private static class MyInputFormat
    implements InputFormat<Text, Text> {
    
    private static class MySplit implements InputSplit {
      int first;
      int length;

      public MySplit() { }

      public MySplit(int first, int length) {
        this.first = first;
        this.length = length;
      }

      public String[] getLocations() {
        final String[] ret = new String[200];
        Arrays.fill(ret, "SPLIT");
        return ret;
      }

      public long getLength() {
        return length;
      }

      public void write(DataOutput out) throws IOException {
        WritableUtils.writeVInt(out, first);
        WritableUtils.writeVInt(out, length);
      }

      public void readFields(DataInput in) throws IOException {
        first = WritableUtils.readVInt(in);
        length = WritableUtils.readVInt(in);
      }
    }
    
    public InputSplit[] getSplits(JobConf job, 
                                  int numSplits) throws IOException {
      return new MySplit[]{new MySplit(0, 1), new MySplit(1, 3),
                           new MySplit(4, 2)};
    }

    public RecordReader<Text, Text> getRecordReader(InputSplit split,
                                                           JobConf job, 
                                                           Reporter reporter)
                                                           throws IOException {
      MySplit sp = (MySplit) split;
      return new RecordReader<Text,Text>() {
        @Override public boolean next(Text key, Text value) { return false; }
        @Override public Text createKey() { return new Text(); }
        @Override public Text createValue() { return new Text(); }
        @Override public long getPos() throws IOException { return 0; }
        @Override public void close() throws IOException { }
        @Override public float getProgress() throws IOException { return 1.0f; }
      };
    }
    
  }
  

  static class MyOutputFormat implements OutputFormat {
    static class MyRecordWriter implements RecordWriter<Object, Object> {
      private DataOutputStream out;
      
      public MyRecordWriter(Path outputFile, JobConf job) throws IOException {
      }
      
      public void write(Object key, Object value) throws IOException {
        return;
      }

      public void close(Reporter reporter) throws IOException {
      }
    }
    
    public RecordWriter getRecordWriter(FileSystem ignored, JobConf job, 
                                        String name,
                                        Progressable progress
                                        ) throws IOException {
      return new MyRecordWriter(new Path(job.get("non.std.out")), job);
    }

    public void checkOutputSpecs(FileSystem ignored, 
                                 JobConf job) throws IOException {
    }
  }

}
