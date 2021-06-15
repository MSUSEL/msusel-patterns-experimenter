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
package org.apache.hadoop.examples.terasort;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Generate 1 mapper per a file that checks to make sure the keys
 * are sorted within each file. The mapper also generates 
 * "$file:begin", first key and "$file:end", last key. The reduce verifies that
 * all of the start/end items are in order.
 * Any output from the reduce is problem report.
 * <p>
 * To run the program: 
 * <b>bin/hadoop jar hadoop-examples-*.jar teravalidate out-dir report-dir</b>
 * <p>
 * If there is any output, something is wrong and the output of the reduce
 * will have the problem report.
 */
public class TeraValidate extends Configured implements Tool {
  private static final Text error = new Text("error");

  static class ValidateMapper extends MapReduceBase 
      implements Mapper<Text,Text,Text,Text> {
    private Text lastKey;
    private OutputCollector<Text,Text> output;
    private String filename;
    
    /**
     * Get the final part of the input name
     * @param split the input split
     * @return the "part-00000" for the input
     */
    private String getFilename(FileSplit split) {
      return split.getPath().getName();
    }

    public void map(Text key, Text value, OutputCollector<Text,Text> output,
                    Reporter reporter) throws IOException {
      if (lastKey == null) {
        filename = getFilename((FileSplit) reporter.getInputSplit());
        output.collect(new Text(filename + ":begin"), key);
        lastKey = new Text();
        this.output = output;
      } else {
        if (key.compareTo(lastKey) < 0) {
          output.collect(error, new Text("misorder in " + filename + 
                                         " last: '" + lastKey + 
                                         "' current: '" + key + "'"));
        }
      }
      lastKey.set(key);
    }
    
    public void close() throws IOException {
      if (lastKey != null) {
        output.collect(new Text(filename + ":end"), lastKey);
      }
    }
  }

  /**
   * Check the boundaries between the output files by making sure that the
   * boundary keys are always increasing.
   * Also passes any error reports along intact.
   */
  static class ValidateReducer extends MapReduceBase 
      implements Reducer<Text,Text,Text,Text> {
    private boolean firstKey = true;
    private Text lastKey = new Text();
    private Text lastValue = new Text();
    public void reduce(Text key, Iterator<Text> values,
                       OutputCollector<Text, Text> output, 
                       Reporter reporter) throws IOException {
      if (error.equals(key)) {
        while(values.hasNext()) {
          output.collect(key, values.next());
        }
      } else {
        Text value = values.next();
        if (firstKey) {
          firstKey = false;
        } else {
          if (value.compareTo(lastValue) < 0) {
            output.collect(error, 
                           new Text("misordered keys last: " + 
                                    lastKey + " '" + lastValue +
                                    "' current: " + key + " '" + value + "'"));
          }
        }
        lastKey.set(key);
        lastValue.set(value);
      }
    }
    
  }

  public int run(String[] args) throws Exception {
    JobConf job = (JobConf) getConf();
    TeraInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    job.setJobName("TeraValidate");
    job.setJarByClass(TeraValidate.class);
    job.setMapperClass(ValidateMapper.class);
    job.setReducerClass(ValidateReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    // force a single reducer
    job.setNumReduceTasks(1);
    // force a single split 
    job.setLong("mapred.min.split.size", Long.MAX_VALUE);
    job.setInputFormat(TeraInputFormat.class);
    JobClient.runJob(job);
    return 0;
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new JobConf(), new TeraValidate(), args);
    System.exit(res);
  }

}
