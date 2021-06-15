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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TestJobClient extends ClusterMapReduceTestCase {
  
  private static final Log LOG = LogFactory.getLog(TestJobClient.class);
  
  private String runJob() throws Exception {
    OutputStream os = getFileSystem().create(new Path(getInputDir(), "text.txt"));
    Writer wr = new OutputStreamWriter(os);
    wr.write("hello1\n");
    wr.write("hello2\n");
    wr.write("hello3\n");
    wr.close();

    JobConf conf = createJobConf();
    conf.setJobName("mr");
    conf.setJobPriority(JobPriority.HIGH);
    
    conf.setInputFormat(TextInputFormat.class);

    conf.setMapOutputKeyClass(LongWritable.class);
    conf.setMapOutputValueClass(Text.class);

    conf.setOutputFormat(TextOutputFormat.class);
    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapperClass(org.apache.hadoop.mapred.lib.IdentityMapper.class);
    conf.setReducerClass(org.apache.hadoop.mapred.lib.IdentityReducer.class);

    FileInputFormat.setInputPaths(conf, getInputDir());
    FileOutputFormat.setOutputPath(conf, getOutputDir());

    return JobClient.runJob(conf).getID().toString();
  }
  
  private int runTool(Configuration conf, Tool tool, String[] args, OutputStream out) throws Exception {
    PrintStream oldOut = System.out;
    PrintStream newOut = new PrintStream(out, true);
    try {
      System.setOut(newOut);
      return ToolRunner.run(conf, tool, args);
    } finally {
      System.setOut(oldOut);
    }
  }

  public void testGetCounter() throws Exception {
    String jobId = runJob();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int exitCode = runTool(createJobConf(), new JobClient(),
        new String[] { "-counter", jobId,
        "org.apache.hadoop.mapred.Task$Counter", "MAP_INPUT_RECORDS" },
        out);
    assertEquals("Exit code", 0, exitCode);
    assertEquals("Counter", "3", out.toString().trim());
  }

  public void testJobList() throws Exception {
    String jobId = runJob();
    verifyJobPriority(jobId, "HIGH");
  }

  private void verifyJobPriority(String jobId, String priority)
                            throws Exception {
    PipedInputStream pis = new PipedInputStream();
    PipedOutputStream pos = new PipedOutputStream(pis);
    int exitCode = runTool(createJobConf(), new JobClient(),
        new String[] { "-list", "all" },
        pos);
    assertEquals("Exit code", 0, exitCode);
    BufferedReader br = new BufferedReader(new InputStreamReader(pis));
    String line = null;
    while ((line=br.readLine()) != null) {
      LOG.info("line = " + line);
      if (!line.startsWith(jobId)) {
        continue;
      }
      assertTrue(line.contains(priority));
      break;
    }
    pis.close();
  }
  
  public void testChangingJobPriority() throws Exception {
    String jobId = runJob();
    int exitCode = runTool(createJobConf(), new JobClient(),
        new String[] { "-set-priority", jobId, "VERY_LOW" },
        new ByteArrayOutputStream());
    assertEquals("Exit code", 0, exitCode);
    verifyJobPriority(jobId, "VERY_LOW");
  }
}
