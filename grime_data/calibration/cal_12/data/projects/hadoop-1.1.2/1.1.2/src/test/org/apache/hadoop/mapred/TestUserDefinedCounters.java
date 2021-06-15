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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Properties;

import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Counters.Counter;
import org.apache.hadoop.mapred.lib.IdentityMapper;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.util.StringUtils;
import org.hsqldb.lib.StringUtil;

public class TestUserDefinedCounters extends ClusterMapReduceTestCase {
  protected void setUp() throws Exception {
    super.setUp();
    Properties prop = new Properties();
    prop.put("mapred.job.tracker.persist.jobstatus.active", "true");
    prop.put("mapred.job.tracker.persist.jobstatus.hours", "1");
    startCluster(true, prop);
  }
  
  enum EnumCounter { MAP_RECORDS }
  
  static class CountingMapper<K, V> extends IdentityMapper<K, V> {
    private JobConf jconf;
    boolean generateUniqueCounters = false;
    
    @Override
    public void configure(JobConf jconf) {
      this.jconf = jconf;
      this.generateUniqueCounters = 
        jconf.getBoolean("task.generate.unique.counters", false);
    }
    
    public void map(K key, V value,
        OutputCollector<K, V> output, Reporter reporter)
        throws IOException {
      output.collect(key, value);
      reporter.incrCounter(EnumCounter.MAP_RECORDS, 1);
      reporter.incrCounter("StringCounter", "MapRecords", 1);
      for (int i =0; i < 50; i++) {
        if (generateUniqueCounters) {
          reporter.incrCounter("StringCounter", "countername_" + 
              jconf.get("mapred.task.id") + "_"+ i, 1);
        } else {
          reporter.incrCounter("StringCounter", "countername_" + 
              i, 1);
        }
      }    
    }
  }
  
  public void testMapReduceJob() throws Exception {
    OutputStream os = getFileSystem().create(new Path(getInputDir(), "text.txt"));
    Writer wr = new OutputStreamWriter(os);
    wr.write("hello1\n");
    wr.write("hello2\n");
    wr.write("hello3\n");
    wr.write("hello4\n");
    wr.close();

    JobConf conf = createJobConf();
    conf.setJobName("counters");
    
    conf.setInputFormat(TextInputFormat.class);

    conf.setMapOutputKeyClass(LongWritable.class);
    conf.setMapOutputValueClass(Text.class);

    conf.setOutputFormat(TextOutputFormat.class);
    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapperClass(CountingMapper.class);
    conf.setReducerClass(IdentityReducer.class);

    FileInputFormat.setInputPaths(conf, getInputDir());

    FileOutputFormat.setOutputPath(conf, getOutputDir());

    RunningJob runningJob = JobClient.runJob(conf);

    Path[] outputFiles = FileUtil.stat2Paths(
                           getFileSystem().listStatus(getOutputDir(),
                           new Utils.OutputFileUtils.OutputFilesFilter()));
    if (outputFiles.length > 0) {
      InputStream is = getFileSystem().open(outputFiles[0]);
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String line = reader.readLine();
      int counter = 0;
      while (line != null) {
        counter++;
        assertTrue(line.contains("hello"));
        line = reader.readLine();
      }
      reader.close();
      assertEquals(4, counter);
    }

    assertEquals(4,
        runningJob.getCounters().getCounter(EnumCounter.MAP_RECORDS));
    Counters counters = runningJob.getCounters();
    assertEquals(4,
        runningJob.getCounters().getGroup("StringCounter")
        .getCounter("MapRecords"));
    assertTrue(counters.getGroupNames().size() <= Counters.MAX_GROUP_LIMIT);
    int i = 0;
    while (counters.size() < Counters.MAX_COUNTER_LIMIT) {
      counters.incrCounter("IncrCounter", "limit " + i, 2);
      i++;
    }
    try {
      counters.incrCounter("IncrCountertest", "test", 2);
      assertTrue(false);
    } catch(RuntimeException re) {
      System.out.println("Exceeded counter " + 
          StringUtils.stringifyException(re));
    }
    conf.setBoolean("task.generate.unique.counters", true);
    FileOutputFormat.setOutputPath(conf, new Path("output-fail"));
    try {
      runningJob = JobClient.runJob(conf);
    } catch(Exception ie) {
      System.out.println(StringUtils.stringifyException(ie));
    }
  }
}
