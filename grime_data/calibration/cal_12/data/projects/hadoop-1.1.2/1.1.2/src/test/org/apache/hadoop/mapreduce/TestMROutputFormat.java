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
package org.apache.hadoop.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobConf;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestMROutputFormat {

  @Test
  public void testJobSubmission() throws Exception {
    JobConf conf = new JobConf();
    Job job = new Job(conf);
    job.setInputFormatClass(TestInputFormat.class);
    job.setMapperClass(TestMapper.class);
    job.setOutputFormatClass(TestOutputFormat.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);
    job.waitForCompletion(true);
    assertTrue(job.isSuccessful());
  }
  
  public static class TestMapper
  extends Mapper<IntWritable, IntWritable, IntWritable, IntWritable> {
    public void map(IntWritable key, IntWritable value, Context context) 
    throws IOException, InterruptedException {
      context.write(key, value);
    }
  }
}

class TestInputFormat extends InputFormat<IntWritable, IntWritable> {

  @Override
  public RecordReader<IntWritable, IntWritable> createRecordReader(
      InputSplit split, TaskAttemptContext context) throws IOException,
      InterruptedException {
    return new RecordReader<IntWritable, IntWritable>() {

      private boolean done = false;
      
      @Override
      public void close() throws IOException {
      }

      @Override
      public IntWritable getCurrentKey() throws IOException,
          InterruptedException {
	return new IntWritable(0);
      }

      @Override
      public IntWritable getCurrentValue() throws IOException,
          InterruptedException {
	return new IntWritable(0);
      }

      @Override
      public float getProgress() throws IOException, InterruptedException {
	return done ? 0 : 1;
      }

      @Override
      public void initialize(InputSplit split, TaskAttemptContext context)
          throws IOException, InterruptedException {
      }

      @Override
      public boolean nextKeyValue() throws IOException, InterruptedException {
	if (!done) {
	  done = true;
	  return true;
	}
	return false;
      }
    };
  }

  @Override
  public List<InputSplit> getSplits(JobContext context) throws IOException,
      InterruptedException {    
    List<InputSplit> list = new ArrayList<InputSplit>();
    list.add(new TestInputSplit());
    return list;
  }
}

class TestInputSplit extends InputSplit implements Writable {
  
  @Override
  public long getLength() throws IOException, InterruptedException {
	return 1;
  }

  @Override
  public String[] getLocations() throws IOException, InterruptedException {
	String[] hosts = {"localhost"};
	return hosts;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
  }

  @Override
  public void write(DataOutput out) throws IOException {
  }	
}

class TestOutputFormat extends OutputFormat<IntWritable, IntWritable> 
implements Configurable {

  public static final String TEST_CONFIG_NAME = "mapred.test.jobsubmission";
  private Configuration conf;
  
  @Override
  public void checkOutputSpecs(JobContext context) throws IOException,
      InterruptedException {
    conf.setBoolean(TEST_CONFIG_NAME, true);
  }

  @Override
  public OutputCommitter getOutputCommitter(TaskAttemptContext context)
      throws IOException, InterruptedException {
    return new OutputCommitter() {

      @Override
      public void abortTask(TaskAttemptContext taskContext) throws IOException {
      }

      @Override
      public void commitTask(TaskAttemptContext taskContext) throws IOException {
      }

      @Override
      public boolean needsTaskCommit(TaskAttemptContext taskContext)
          throws IOException {
	return false;
      }

      @Override
      public void setupJob(JobContext jobContext) throws IOException {
      }

      @Override
      public void setupTask(TaskAttemptContext taskContext) throws IOException {
      }
    };
  }

  @Override
  public RecordWriter<IntWritable, IntWritable> getRecordWriter(
      TaskAttemptContext context) throws IOException, InterruptedException {
    assertTrue(context.getConfiguration().getBoolean(TEST_CONFIG_NAME, false));
    return new RecordWriter<IntWritable, IntWritable>() {

      @Override
      public void close(TaskAttemptContext context) throws IOException,
          InterruptedException {	
      }

      @Override
      public void write(IntWritable key, IntWritable value) throws IOException,
          InterruptedException {	
      }
    }; 
  }
  
  @Override
  public Configuration getConf() {
      return conf;
  }

  @Override
  public void setConf(Configuration conf) {
      this.conf = conf;        
  }
}