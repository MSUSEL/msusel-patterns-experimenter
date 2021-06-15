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
import java.util.Date;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.mapred.SortValidator.RecordStatsChecker.NonSplitableSequenceFileInputFormat;
import org.apache.hadoop.mapred.lib.IdentityMapper;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class BigMapOutput extends Configured implements Tool {
  public static final Log LOG =
    LogFactory.getLog(BigMapOutput.class.getName());
  private static Random random = new Random();
  
  private static void randomizeBytes(byte[] data, int offset, int length) {
    for(int i=offset + length - 1; i >= offset; --i) {
      data[i] = (byte) random.nextInt(256);
    }
  }

  private static void createBigMapInputFile(Configuration conf, FileSystem fs, 
                                            Path dir, long fileSizeInMB) 
  throws IOException {
    // Check if the input path exists and is non-empty
    if (fs.exists(dir)) {
      FileStatus[] list = fs.listStatus(dir);
      if (list != null && list.length > 0) {
        throw new IOException("Input path: " + dir + " already exists... ");
      }
    }
    
    Path file = new Path(dir, "part-0");
    SequenceFile.Writer writer = 
      SequenceFile.createWriter(fs, conf, file, 
                                BytesWritable.class, BytesWritable.class,
                                CompressionType.NONE);
    long numBytesToWrite = fileSizeInMB * 1024 * 1024;
    int minKeySize = conf.getInt("test.bmo.min_key", 10);;
    int keySizeRange = 
      conf.getInt("test.bmo.max_key", 1000) - minKeySize;
    int minValueSize = conf.getInt("test.bmo.min_value", 0);
    int valueSizeRange = 
      conf.getInt("test.bmo.max_value", 20000) - minValueSize;
    BytesWritable randomKey = new BytesWritable();
    BytesWritable randomValue = new BytesWritable();

    LOG.info("Writing " + numBytesToWrite + " bytes to " + file + " with " +
             "minKeySize: " + minKeySize + " keySizeRange: " + keySizeRange +
             " minValueSize: " + minValueSize + " valueSizeRange: " + valueSizeRange);
    long start = System.currentTimeMillis();
    while (numBytesToWrite > 0) {
      int keyLength = minKeySize + 
        (keySizeRange != 0 ? random.nextInt(keySizeRange) : 0);
      randomKey.setSize(keyLength);
      randomizeBytes(randomKey.getBytes(), 0, randomKey.getLength());
      int valueLength = minValueSize +
        (valueSizeRange != 0 ? random.nextInt(valueSizeRange) : 0);
      randomValue.setSize(valueLength);
      randomizeBytes(randomValue.getBytes(), 0, randomValue.getLength());
      writer.append(randomKey, randomValue);
      numBytesToWrite -= keyLength + valueLength;
    }
    writer.close();
    long end = System.currentTimeMillis();

    LOG.info("Created " + file + " of size: " + fileSizeInMB + "MB in " + 
             (end-start)/1000 + "secs");
  }
  
  private static void usage() {
    System.err.println("BigMapOutput -input <input-dir> -output <output-dir> " +
                       "[-create <filesize in MB>]");
    ToolRunner.printGenericCommandUsage(System.err);
    System.exit(1);
  }
  public int run(String[] args) throws Exception {    
    if (args.length < 4) { //input-dir should contain a huge file ( > 2GB)
      usage();
    } 
    Path bigMapInput = null;
    Path outputPath = null;
    boolean createInput = false;
    long fileSizeInMB = 3 * 1024;         // default of 3GB (>2GB)
    for(int i=0; i < args.length; ++i) {
      if ("-input".equals(args[i])){
        bigMapInput = new Path(args[++i]);
      } else if ("-output".equals(args[i])){
        outputPath = new Path(args[++i]);
      } else if ("-create".equals(args[i])) {
        createInput = true;
        fileSizeInMB = Long.parseLong(args[++i]);
      } else {
        usage();
      }
    }
    
    FileSystem fs = FileSystem.get(getConf());
    JobConf jobConf = new JobConf(getConf(), BigMapOutput.class);

    jobConf.setJobName("BigMapOutput");
    jobConf.setInputFormat(NonSplitableSequenceFileInputFormat.class);
    jobConf.setOutputFormat(SequenceFileOutputFormat.class);
    FileInputFormat.setInputPaths(jobConf, bigMapInput);
    if (fs.exists(outputPath)) {
      fs.delete(outputPath, true);
    }
    FileOutputFormat.setOutputPath(jobConf, outputPath);
    jobConf.setMapperClass(IdentityMapper.class);
    jobConf.setReducerClass(IdentityReducer.class);
    jobConf.setOutputKeyClass(BytesWritable.class);
    jobConf.setOutputValueClass(BytesWritable.class);
    
    if (createInput) {
      createBigMapInputFile(jobConf, fs, bigMapInput, fileSizeInMB);
    }
    
    Date startTime = new Date();
    System.out.println("Job started: " + startTime);
    JobClient.runJob(jobConf);
    Date end_time = new Date();
    System.out.println("Job ended: " + end_time);
    
    return 0;
  }

  public static void main(String argv[]) throws Exception {
    int res = ToolRunner.run(new Configuration(), new BigMapOutput(), argv);
    System.exit(res);
  }

}
