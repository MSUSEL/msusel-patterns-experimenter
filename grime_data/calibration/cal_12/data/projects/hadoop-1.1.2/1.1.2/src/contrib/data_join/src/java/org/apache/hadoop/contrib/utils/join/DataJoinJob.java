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
package org.apache.hadoop.contrib.utils.join;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.JobID;

/**
 * This class implements the main function for creating a map/reduce
 * job to join data of different sources. To create sucn a job, the 
 * user must implement a mapper class that extends DataJoinMapperBase class,
 * and a reducer class that extends DataJoinReducerBase. 
 * 
 */
public class DataJoinJob {

  public static Class getClassByName(String className) {
    Class retv = null;
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      retv = Class.forName(className, true, classLoader);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return retv;
  }

  public static JobConf createDataJoinJob(String args[]) throws IOException {

    String inputDir = args[0];
    String outputDir = args[1];
    Class inputFormat = SequenceFileInputFormat.class;
    if (args[2].compareToIgnoreCase("text") != 0) {
      System.out.println("Using SequenceFileInputFormat: " + args[2]);
    } else {
      System.out.println("Using TextInputFormat: " + args[2]);
      inputFormat = TextInputFormat.class;
    }
    int numOfReducers = Integer.parseInt(args[3]);
    Class mapper = getClassByName(args[4]);
    Class reducer = getClassByName(args[5]);
    Class mapoutputValueClass = getClassByName(args[6]);
    Class outputFormat = TextOutputFormat.class;
    Class outputValueClass = Text.class;
    if (args[7].compareToIgnoreCase("text") != 0) {
      System.out.println("Using SequenceFileOutputFormat: " + args[7]);
      outputFormat = SequenceFileOutputFormat.class;
      outputValueClass = getClassByName(args[7]);
    } else {
      System.out.println("Using TextOutputFormat: " + args[7]);
    }
    long maxNumOfValuesPerGroup = 100;
    String jobName = "";
    if (args.length > 8) {
      maxNumOfValuesPerGroup = Long.parseLong(args[8]);
    }
    if (args.length > 9) {
      jobName = args[9];
    }
    Configuration defaults = new Configuration();
    JobConf job = new JobConf(defaults, DataJoinJob.class);
    job.setJobName("DataJoinJob: " + jobName);

    FileSystem fs = FileSystem.get(defaults);
    fs.delete(new Path(outputDir));
    FileInputFormat.setInputPaths(job, inputDir);

    job.setInputFormat(inputFormat);

    job.setMapperClass(mapper);
    FileOutputFormat.setOutputPath(job, new Path(outputDir));
    job.setOutputFormat(outputFormat);
    SequenceFileOutputFormat.setOutputCompressionType(job,
            SequenceFile.CompressionType.BLOCK);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(mapoutputValueClass);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(outputValueClass);
    job.setReducerClass(reducer);

    job.setNumMapTasks(1);
    job.setNumReduceTasks(numOfReducers);
    job.setLong("datajoin.maxNumOfValuesPerGroup", maxNumOfValuesPerGroup);
    return job;
  }

  /**
   * Submit/run a map/reduce job.
   * 
   * @param job
   * @return true for success
   * @throws IOException
   */
  public static boolean runJob(JobConf job) throws IOException {
    JobClient jc = new JobClient(job);
    boolean sucess = true;
    RunningJob running = null;
    try {
      running = jc.submitJob(job);
      JobID jobId = running.getID();
      System.out.println("Job " + jobId + " is submitted");
      while (!running.isComplete()) {
        System.out.println("Job " + jobId + " is still running.");
        try {
          Thread.sleep(60000);
        } catch (InterruptedException e) {
        }
        running = jc.getJob(jobId);
      }
      sucess = running.isSuccessful();
    } finally {
      if (!sucess && (running != null)) {
        running.killJob();
      }
      jc.close();
    }
    return sucess;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    boolean success;
    if (args.length < 8 || args.length > 10) {
      System.out.println("usage: DataJoinJob " + "inputdirs outputdir map_input_file_format "
                         + "numofParts " + "mapper_class " + "reducer_class "
                         + "map_output_value_class "
                         + "output_value_class [maxNumOfValuesPerGroup [descriptionOfJob]]]");
      System.exit(-1);
    }

    try {
      JobConf job = DataJoinJob.createDataJoinJob(args);
      success = DataJoinJob.runJob(job);
      if (!success) {
        System.out.println("Job failed");
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
