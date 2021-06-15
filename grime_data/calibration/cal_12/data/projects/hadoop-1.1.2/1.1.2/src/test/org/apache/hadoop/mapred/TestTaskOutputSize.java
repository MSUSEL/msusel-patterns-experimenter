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

import java.io.File;

import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MapReduceTestUtil;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestTaskOutputSize {
  private static Path rootDir = new Path(System.getProperty("test.build.data",
      "/tmp"), "test");

  @After
  public void tearDown() throws Exception {
    FileUtil.fullyDelete(new File(rootDir.toString()));
  }

  @Test
  public void testTaskOutputSize() throws Exception {
    MiniMRCluster mr = new MiniMRCluster(1, "file:///", 1);
    Path inDir = new Path(rootDir, "input");
    Path outDir = new Path(rootDir, "output");
    Job job = MapReduceTestUtil.createJob(mr.createJobConf(), inDir, outDir, 1, 1);
    job.waitForCompletion(true);
    assertTrue("Job failed", job.isSuccessful());
    JobTracker jt = mr.getJobTrackerRunner().getJobTracker();
    for (TaskCompletionEvent tce : job.getTaskCompletionEvents(0)) {
      TaskStatus ts = jt.getTaskStatus(tce.getTaskAttemptId());
      if (tce.isMapTask()) {
        assertTrue(
            "map output size is not found for " + tce.getTaskAttemptId(), ts
                .getOutputSize() > 0);
      } else {
        assertEquals("task output size not expected for "
            + tce.getTaskAttemptId(), -1, ts.getOutputSize());
      }
    }

    // test output sizes for job with no reduces
    job = MapReduceTestUtil.createJob(mr.createJobConf(), inDir, outDir, 1, 0);
    job.waitForCompletion(true);
    assertTrue("Job failed", job.isSuccessful());
    for (TaskCompletionEvent tce : job.getTaskCompletionEvents(0)) {
      TaskStatus ts = jt.getTaskStatus(tce.getTaskAttemptId());
      assertEquals("task output size not expected for "
          + tce.getTaskAttemptId(), -1, ts.getOutputSize());
    }

    // test output sizes for failed job
    job = MapReduceTestUtil.createFailJob(mr.createJobConf(), outDir, inDir);
    job.waitForCompletion(true);
    assertFalse("Job not failed", job.isSuccessful());
    for (TaskCompletionEvent tce : job.getTaskCompletionEvents(0)) {
      TaskStatus ts = jt.getTaskStatus(tce.getTaskAttemptId());
      assertEquals("task output size not expected for "
          + tce.getTaskAttemptId(), -1, ts.getOutputSize());
    }
  }

}
