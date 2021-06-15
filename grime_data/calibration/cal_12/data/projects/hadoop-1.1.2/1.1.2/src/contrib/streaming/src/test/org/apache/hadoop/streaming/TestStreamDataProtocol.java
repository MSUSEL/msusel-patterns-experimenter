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
package org.apache.hadoop.streaming;

import junit.framework.TestCase;
import java.io.*;
import java.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner;

/**
 * This class tests hadoopStreaming in MapReduce local mode.
 */
public class TestStreamDataProtocol extends TestCase
{

  // "map" command: grep -E (red|green|blue)
  // reduce command: uniq
  protected File INPUT_FILE = new File("input_for_data_protocol_test.txt");
  protected File OUTPUT_DIR = new File("out_for_data_protocol_test");
  protected String input = "roses.smell.good\nroses.look.good\nroses.need.care\nroses.attract.bees\nroses.are.red\nroses.are.not.blue\nbunnies.are.pink\nbunnies.run.fast\nbunnies.have.short.tail\nbunnies.have.long.ears\n";
  // map behaves like "/usr/bin/cat"; 
  protected String map = StreamUtil.makeJavaCommand(TrApp.class, new String[]{".", "."});
  // reduce counts the number of values for each key
  protected String reduce = "org.apache.hadoop.streaming.ValueCountReduce";
  protected String outputExpect = "bunnies.are\t1\nbunnies.have\t2\nbunnies.run\t1\nroses.are\t2\nroses.attract\t1\nroses.look\t1\nroses.need\t1\nroses.smell\t1\n";

  private StreamJob job;

  public TestStreamDataProtocol() throws IOException
  {
    UtilTest utilTest = new UtilTest(getClass().getName());
    utilTest.checkUserDir();
    utilTest.redirectIfAntJunit();
  }

  protected void createInput() throws IOException
  {
    DataOutputStream out = new DataOutputStream(
                                                new FileOutputStream(INPUT_FILE.getAbsoluteFile()));
    out.write(input.getBytes("UTF-8"));
    out.close();
  }

  protected String[] genArgs() {
    return new String[] {
      "-input", INPUT_FILE.getAbsolutePath(),
      "-output", OUTPUT_DIR.getAbsolutePath(),
      "-mapper", map,
      "-reducer", reduce,
      "-partitioner", KeyFieldBasedPartitioner.class.getCanonicalName(),
      //"-verbose",
      "-jobconf", "stream.map.output.field.separator=.",
      "-jobconf", "stream.num.map.output.key.fields=2",
      "-jobconf", "map.output.key.field.separator=.",
      "-jobconf", "num.key.fields.for.partition=1",
      "-jobconf", "mapred.reduce.tasks=2",
      "-jobconf", "keep.failed.task.files=true",
      "-jobconf", "stream.tmpdir="+System.getProperty("test.build.data","/tmp")
    };
  }
  
  public void testCommandLine()
  {
    try {
      try {
        FileUtil.fullyDelete(OUTPUT_DIR.getAbsoluteFile());
      } catch (Exception e) {
      }

      createInput();
      boolean mayExit = false;

      // During tests, the default Configuration will use a local mapred
      // So don't specify -config or -cluster
      job = new StreamJob(genArgs(), mayExit);      
      job.go();
      File outFile = new File(OUTPUT_DIR, "part-00000").getAbsoluteFile();
      String output = StreamUtil.slurp(outFile);
      outFile.delete();
      System.err.println("outEx1=" + outputExpect);
      System.err.println("  out1=" + output);
      System.err.println("  equals=" + outputExpect.compareTo(output));
      assertEquals(outputExpect, output);
    } catch(Exception e) {
      failTrace(e);
    } finally {
      try {
        INPUT_FILE.delete();
        FileUtil.fullyDelete(OUTPUT_DIR.getAbsoluteFile());
      } catch(Exception e) {
        failTrace(e);
      }
    }
  }

  private void failTrace(Exception e)
  {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    fail(sw.toString());
  }

  public static void main(String[]args) throws Exception
  {
    new TestStreamDataProtocol().testCommandLine();
  }

}
