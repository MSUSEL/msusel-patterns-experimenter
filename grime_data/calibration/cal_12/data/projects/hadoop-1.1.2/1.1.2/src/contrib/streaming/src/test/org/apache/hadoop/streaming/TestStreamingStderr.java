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
import org.apache.hadoop.fs.Path;

/**
 * Test that streaming consumes stderr from the streaming process
 * (before, during, and after the main processing of mapred input),
 * and that stderr messages count as task progress.
 */
public class TestStreamingStderr extends TestCase
{
  public TestStreamingStderr() throws IOException {
    UtilTest utilTest = new UtilTest(getClass().getName());
    utilTest.checkUserDir();
    utilTest.redirectIfAntJunit();
  }

  protected String[] genArgs(File input, File output, int preLines, int duringLines, int postLines) {
    return new String[] {
      "-input", input.getAbsolutePath(),
      "-output", output.getAbsolutePath(),
      "-mapper", StreamUtil.makeJavaCommand(StderrApp.class,
                                            new String[]{Integer.toString(preLines),
                                                         Integer.toString(duringLines),
                                                         Integer.toString(postLines)}),
      "-reducer", StreamJob.REDUCE_NONE,
      "-jobconf", "keep.failed.task.files=true",
      "-jobconf", "mapred.task.timeout=5000",
      "-jobconf", "stream.tmpdir="+System.getProperty("test.build.data","/tmp")
    };
  }

  protected File setupInput(String base, boolean hasInput) throws IOException {
    File input = new File(base + "-input.txt");
    UtilTest.recursiveDelete(input);
    FileOutputStream in = new FileOutputStream(input.getAbsoluteFile());
    if (hasInput) {
      in.write("hello\n".getBytes());      
    }
    in.close();
    return input;
  }
  
  protected File setupOutput(String base) throws IOException {
    File output = new File(base + "-out");
    UtilTest.recursiveDelete(output);
    return output;
  }

  public void runStreamJob(String baseName, boolean hasInput,
                           int preLines, int duringLines, int postLines) {
    try {
      File input = setupInput(baseName, hasInput);
      File output = setupOutput(baseName);
      boolean mayExit = false;
      int returnStatus = 0;

      StreamJob job = new StreamJob(genArgs(input, output, preLines, duringLines, postLines), mayExit);
      returnStatus = job.go();
      assertEquals("StreamJob success", 0, returnStatus);
    } catch (Exception e) {
      failTrace(e);
    }
  }

  // This test will fail by blocking forever if the stderr isn't
  // consumed by Hadoop for tasks that don't have any input.
  public void testStderrNoInput() throws IOException {
    runStreamJob("stderr-pre", false, 10000, 0, 0);
  }

  // Streaming should continue to read stderr even after all input has
  // been consumed.
  public void testStderrAfterOutput() throws IOException {
    runStreamJob("stderr-post", false, 0, 0, 10000);
  }

  // This test should produce a task timeout if stderr lines aren't
  // counted as progress. This won't actually work until
  // LocalJobRunner supports timeouts.
  public void testStderrCountsAsProgress() throws IOException {
    runStreamJob("stderr-progress", true, 10, 1000, 0);
  }
  
  protected void failTrace(Exception e) {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    fail(sw.toString());
  }
}
