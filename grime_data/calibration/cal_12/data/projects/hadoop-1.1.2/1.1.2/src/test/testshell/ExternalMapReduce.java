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
package testshell;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
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
 * will be in an external jar and used for 
 * test in TestJobShell.java.
 */
public class ExternalMapReduce extends Configured implements Tool {

  public void configure(JobConf job) {
    // do nothing
  }

  public void close()
    throws IOException {

  }

  public static class MapClass extends MapReduceBase 
    implements Mapper<WritableComparable, Writable,
                      WritableComparable, IntWritable> {
    public void map(WritableComparable key, Writable value,
                    OutputCollector<WritableComparable, IntWritable> output,
                    Reporter reporter)
      throws IOException {
      //check for classpath
      String classpath = System.getProperty("java.class.path");
      if (classpath.indexOf("testjob.jar") == -1) {
        throw new IOException("failed to find in the library " + classpath);
      }
      if (classpath.indexOf("test.jar") == -1) {
        throw new IOException("failed to find the library test.jar in" 
            + classpath);
      }
      //fork off ls to see if the file exists.
      // java file.exists() will not work on 
      // cygwin since it is a symlink
      String[] argv = new String[8];
      argv[0] = "ls";
      argv[1] = "files_tmp";
      argv[2] = "localfilelink";
      argv[3] = "dfsfilelink";
      argv[4] = "tarlink";
      argv[5] = "ziplink";
      argv[6] = "test.tgz";
      argv[7] = "jarlink";
      Process p = Runtime.getRuntime().exec(argv);
      int ret = -1;
      try {
        ret = p.waitFor();
      } catch(InterruptedException ie) {
        //do nothing here.
      }
      if (ret != 0) {
        throw new IOException("files_tmp does not exist");
      }
      File file = new File("./jarlink/test.txt");
      if (!file.canExecute()) {
        throw new IOException("jarlink/test.txt is not executable");
      }
    }
  }

  public static class Reduce extends MapReduceBase
    implements Reducer<WritableComparable, Writable,
                       WritableComparable, IntWritable> {
    public void reduce(WritableComparable key, Iterator<Writable> values,
                       OutputCollector<WritableComparable, IntWritable> output,
                       Reporter reporter)
      throws IOException {
     //do nothing
    }
  }
  
  public int run(String[] argv) throws IOException {
    if (argv.length < 2) {
      System.out.println("ExternalMapReduce <input> <output>");
      return -1;
    }
    Path outDir = new Path(argv[1]);
    Path input = new Path(argv[0]);
    JobConf testConf = new JobConf(getConf(), ExternalMapReduce.class);
    
    //try to load a class from libjar
    try {
      testConf.getClassByName("testjar.ClassWordCount");
    } catch (ClassNotFoundException e) {
      System.out.println("Could not find class from libjar");
      return -1;
    }
    
    
    testConf.setJobName("external job");
    FileInputFormat.setInputPaths(testConf, input);
    FileOutputFormat.setOutputPath(testConf, outDir);
    testConf.setMapperClass(MapClass.class);
    testConf.setReducerClass(Reduce.class);
    testConf.setNumReduceTasks(1);
    JobClient.runJob(testConf);
    return 0;
  }
  
  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(),
                     new ExternalMapReduce(), args);
    System.exit(res);
  }
}
