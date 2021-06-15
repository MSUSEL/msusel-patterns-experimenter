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
package testjar;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputCommitter;
import org.apache.hadoop.mapred.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class JobKillCommitter {
  /**
   * The class provides a overrided implementation of output committer
   * set up method, which causes the job to fail during set up.
   */
  public static class CommitterWithFailSetup extends FileOutputCommitter {
    @Override
    public void setupJob(JobContext context) throws IOException {
      throw new IOException();
    }
  }

  /**
   * The class provides a dummy implementation of outputcommitter
   * which does nothing
   */
  public static class CommitterWithNoError extends FileOutputCommitter {
    @Override
    public void setupJob(JobContext context) throws IOException {
    }

    @Override
    public void commitJob(JobContext context) throws IOException {
    }
  }

  /**
   * The class provides a overrided implementation of commitJob which
   * causes the clean up method to fail.
   */
  public static class CommitterWithFailCleanup extends FileOutputCommitter {
    @Override
    public void commitJob(JobContext context) throws IOException {
      throw new IOException();
    }
  }

  /**
   * The class is used provides a dummy implementation for mapper method which
   * does nothing.
   */
  public static class MapperPass extends Mapper<LongWritable, Text, Text, Text> {
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
    }
  }
  /**
  * The class provides a sleep implementation for mapper method.
  */
 public static class MapperPassSleep extends 
     Mapper<LongWritable, Text, Text, Text> {
   public void map(LongWritable key, Text value, Context context)
       throws IOException, InterruptedException {
     Thread.sleep(10000);
   }
 }

  /**
   * The class  provides a way for the mapper function to fail by
   * intentionally throwing an IOException
   */
  public static class MapperFail extends Mapper<LongWritable, Text, Text, Text> {
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
      throw new IOException();
    }
  }

  /**
   * The class provides a way for the reduce function to fail by
   * intentionally throwing an IOException
   */
  public static class ReducerFail extends Reducer<Text, Text, Text, Text> {
    public void reduce(Text key, Iterator<Text> values, Context context)
        throws IOException, InterruptedException {
      throw new IOException();
    }
  }

  /**
   * The class provides a empty implementation of reducer method that
   * does nothing
   */
  public static class ReducerPass extends Reducer<Text, Text, Text, Text> {
    public void reduce(Text key, Iterator<Text> values, Context context)
        throws IOException, InterruptedException {
    }
  }
}
