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
import org.apache.hadoop.util.ToolRunner;

/**
 * This class tests if hadoopStreaming returns Exception 
 * on failure when submitted an invalid/failed job
 * The test case provides an invalid input file for map/reduce job as
 * a unit test case
 */
public class TestStreamingFailure extends TestStreaming
{

  protected File INVALID_INPUT_FILE;// = new File("invalid_input.txt");
  private StreamJob job;

  public TestStreamingFailure() throws IOException
  {
    INVALID_INPUT_FILE = new File("invalid_input.txt");
  }

  protected String[] genArgs() {
    return new String[] {
      "-input", INVALID_INPUT_FILE.getAbsolutePath(),
      "-output", OUTPUT_DIR.getAbsolutePath(),
      "-mapper", map,
      "-reducer", reduce,
      //"-verbose",
      //"-jobconf", "stream.debug=set"
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

      boolean mayExit = false;
      int returnStatus = 0;

      // During tests, the default Configuration will use a local mapred
      // So don't specify -config or -cluster
      job = new StreamJob(genArgs(), mayExit);      
      returnStatus = job.go();
      assertEquals("Streaming Job Failure code expected", 5, returnStatus);
    } catch(Exception e) {
      // Expecting an exception
    } finally {
      try {
        FileUtil.fullyDelete(OUTPUT_DIR.getAbsoluteFile());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public void testStreamingFailureForFailedProcess() throws Exception {
    int ret = 0;
    try {
      createInput();
      String[] args = {
          "-input", INPUT_FILE.getAbsolutePath(),
          "-output", OUTPUT_DIR.getAbsolutePath(),
          "-mapper", "/bin/ls dsdsdsds-does-not-exist",
          "-jobconf", "stream.tmpdir="+System.getProperty("test.build.data",
              "/tmp"),
      };
      ret = ToolRunner.run(new StreamJob(), args);
    } finally {
      INPUT_FILE.delete();
      FileUtil.fullyDelete(OUTPUT_DIR.getAbsoluteFile());
    }
    assertEquals("Streaming job failure code expected", 1, ret);
  }

  public static void main(String[]args) throws Exception
  {
      new TestStreamingFailure().testCommandLine();
  }
}
