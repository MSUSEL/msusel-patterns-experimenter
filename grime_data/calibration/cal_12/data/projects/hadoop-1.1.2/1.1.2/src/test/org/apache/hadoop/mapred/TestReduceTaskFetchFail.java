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

import static org.junit.Assert.*;

import java.io.IOException;
import org.apache.hadoop.mapred.Task.TaskReporter;
import org.apache.hadoop.mapred.TaskUmbilicalProtocol;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.ReduceTask;
import org.junit.Test;
import static org.mockito.Mockito.*;



public class TestReduceTaskFetchFail {

  public static class TestReduceTask extends ReduceTask {
    public TestReduceTask() {
       super();
    }
    public String getJobFile() { return "/foo"; }

    public class TestReduceCopier extends ReduceCopier {
      public TestReduceCopier(TaskUmbilicalProtocol umbilical, JobConf conf,
                        TaskReporter reporter
                        )throws ClassNotFoundException, IOException {
        super(umbilical, conf, reporter);
      }

      public void checkAndInformJobTracker(int failures, TaskAttemptID mapId, boolean readError) {
        super.checkAndInformJobTracker(failures, mapId, readError);
      }

    }

  }


  @SuppressWarnings("deprecation")
  @Test
  public void testcheckAndInformJobTracker() throws Exception {
    //mock creation
    TaskUmbilicalProtocol mockUmbilical = mock(TaskUmbilicalProtocol.class);
    TaskReporter mockTaskReporter = mock(TaskReporter.class);

    JobConf conf = new JobConf();
    conf.setUser("testuser");
    conf.setJobName("testJob");
    conf.setSessionId("testSession");

    TaskAttemptID tid =  new TaskAttemptID();
    TestReduceTask rTask = new TestReduceTask();
    rTask.setConf(conf);

    ReduceTask.ReduceCopier reduceCopier = rTask.new TestReduceCopier(mockUmbilical, conf, mockTaskReporter);
    reduceCopier.checkAndInformJobTracker(1, tid, false);

    verify(mockTaskReporter, never()).progress();

    reduceCopier.checkAndInformJobTracker(10, tid, false);
    verify(mockTaskReporter, times(1)).progress();

    // Test the config setting
    conf.setInt("mapreduce.reduce.shuffle.maxfetchfailures", 3);

    rTask.setConf(conf);
    reduceCopier = rTask.new TestReduceCopier(mockUmbilical, conf, mockTaskReporter);

    reduceCopier.checkAndInformJobTracker(1, tid, false);
    verify(mockTaskReporter, times(1)).progress();

    reduceCopier.checkAndInformJobTracker(3, tid, false);
    verify(mockTaskReporter, times(2)).progress();

    reduceCopier.checkAndInformJobTracker(5, tid, false);
    verify(mockTaskReporter, times(2)).progress();

    reduceCopier.checkAndInformJobTracker(6, tid, false);
    verify(mockTaskReporter, times(3)).progress();

    // test readError and its config
    reduceCopier.checkAndInformJobTracker(7, tid, true);
    verify(mockTaskReporter, times(4)).progress();

    conf.setBoolean("mapreduce.reduce.shuffle.notify.readerror", false);

    rTask.setConf(conf);
    reduceCopier = rTask.new TestReduceCopier(mockUmbilical, conf, mockTaskReporter);

    reduceCopier.checkAndInformJobTracker(7, tid, true);
    verify(mockTaskReporter, times(4)).progress();

  }
}
