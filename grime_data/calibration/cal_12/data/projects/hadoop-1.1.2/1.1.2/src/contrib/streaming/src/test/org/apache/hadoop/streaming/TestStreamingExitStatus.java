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

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * This class tests if hadoopStreaming fails a job when the mapper or
 * reducers have non-zero exit status and the
 * stream.non.zero.exit.status.is.failure jobconf is set.
 */
public class TestStreamingExitStatus
{
  protected File TEST_DIR =
    new File("TestStreamingExitStatus").getAbsoluteFile();
  protected File INPUT_FILE = new File(TEST_DIR, "input.txt");
  protected File OUTPUT_DIR = new File(TEST_DIR, "out");

  protected String failingTask = StreamUtil.makeJavaCommand(FailApp.class, new String[]{"true"});
  protected String echoTask = StreamUtil.makeJavaCommand(FailApp.class, new String[]{"false"});

  public TestStreamingExitStatus() throws IOException {
    UtilTest utilTest = new UtilTest(getClass().getName());
    utilTest.checkUserDir();
    utilTest.redirectIfAntJunit();
  }

  protected String[] genArgs(boolean exitStatusIsFailure, boolean failMap) {
    return new String[] {
      "-input", INPUT_FILE.getAbsolutePath(),
      "-output", OUTPUT_DIR.getAbsolutePath(),
      "-mapper", (failMap ? failingTask : echoTask),
      "-reducer", (failMap ? echoTask : failingTask),
      "-jobconf", "keep.failed.task.files=true",
      "-jobconf", "stream.non.zero.exit.is.failure=" + exitStatusIsFailure,
      "-jobconf", "stream.tmpdir="+System.getProperty("test.build.data","/tmp"),
      "-jobconf", "io.sort.mb=10"
    };
  }

  @Before
  public void setUp() throws IOException {
    UtilTest.recursiveDelete(TEST_DIR);
    assertTrue(TEST_DIR.mkdirs());

    FileOutputStream out = new FileOutputStream(INPUT_FILE.getAbsoluteFile());
    out.write("hello\n".getBytes());
    out.close();
  }

  public void runStreamJob(boolean exitStatusIsFailure, boolean failMap) throws Exception {
    boolean mayExit = false;
    int returnStatus = 0;

    StreamJob job = new StreamJob(genArgs(exitStatusIsFailure, failMap), mayExit);
    returnStatus = job.go();
    
    if (exitStatusIsFailure) {
      assertEquals("Streaming Job failure code expected", /*job not successful:*/1, returnStatus);
    } else {
      assertEquals("Streaming Job expected to succeed", 0, returnStatus);
    }
  }

  @Test
  public void testMapFailOk() throws Exception {
    runStreamJob(false, true);
  }

  @Test
  public void testMapFailNotOk() throws Exception {
    runStreamJob(true, true);
  }

  @Test
  public void testReduceFailOk() throws Exception {
    runStreamJob(false, false);
  }
  
  @Test
  public void testReduceFailNotOk() throws Exception {
    runStreamJob(true, false);
  }  
  
}
