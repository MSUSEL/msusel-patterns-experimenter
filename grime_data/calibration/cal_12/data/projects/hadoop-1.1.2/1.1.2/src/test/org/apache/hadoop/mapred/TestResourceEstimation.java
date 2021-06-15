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

import junit.framework.TestCase;
import org.apache.hadoop.mapreduce.split.JobSplit;

public class TestResourceEstimation extends TestCase {
  

  public void testResourceEstimator() throws Exception {
    final int maps = 100;
    final int reduces = 2;
    final int singleMapOutputSize = 1000;
    JobConf jc = new JobConf();
    JobID jid = new JobID("testJT", 0);
    jc.setNumMapTasks(maps);
    jc.setNumReduceTasks(reduces);
    
    JobInProgress jip = new JobInProgress(jid, jc, 
        UtilsForTests.getJobTracker());
    //unfortunately, we can't set job input size from here.
    ResourceEstimator re = new ResourceEstimator(jip);
    
    for(int i = 0; i < maps / 10 ; ++i) {

      long estOutSize = re.getEstimatedMapOutputSize();
      System.out.println(estOutSize);
      assertEquals(0, estOutSize);
      
      TaskStatus ts = new MapTaskStatus();
      ts.setOutputSize(singleMapOutputSize);
      JobSplit.TaskSplitMetaInfo split =
          new JobSplit.TaskSplitMetaInfo(new String[0], 0, 0);
      TaskInProgress tip = 
        new TaskInProgress(jid, "", split, jip.jobtracker, jc, jip, 0, 1);
      re.updateWithCompletedTask(ts, tip);
    }
    assertEquals(2* singleMapOutputSize, re.getEstimatedMapOutputSize());
    assertEquals(2* singleMapOutputSize * maps / reduces, re.getEstimatedReduceInputSize());
    
  }
  
  public void testWithNonZeroInput() throws Exception {
    final int maps = 100;
    final int reduces = 2;
    final int singleMapOutputSize = 1000;
    final int singleMapInputSize = 500;
    JobConf jc = new JobConf();
    JobID jid = new JobID("testJT", 0);
    jc.setNumMapTasks(maps);
    jc.setNumReduceTasks(reduces);
    
    JobInProgress jip = new JobInProgress(jid, jc, 
        UtilsForTests.getJobTracker()) {
      long getInputLength() {
        return singleMapInputSize*desiredMaps();
      }
    };
    ResourceEstimator re = new ResourceEstimator(jip);
    
    for(int i = 0; i < maps / 10 ; ++i) {

      long estOutSize = re.getEstimatedMapOutputSize();
      System.out.println(estOutSize);
      assertEquals(0, estOutSize);
      
      TaskStatus ts = new MapTaskStatus();
      ts.setOutputSize(singleMapOutputSize);
      JobSplit.TaskSplitMetaInfo split =
              new JobSplit.TaskSplitMetaInfo(new String[0], 0,
                                           singleMapInputSize);
      TaskInProgress tip = 
        new TaskInProgress(jid, "", split, jip.jobtracker, jc, jip, 0, 1);
      re.updateWithCompletedTask(ts, tip);
    }
    
    assertEquals(2* singleMapOutputSize, re.getEstimatedMapOutputSize());
    assertEquals(2* singleMapOutputSize * maps / reduces, re.getEstimatedReduceInputSize());

    //add one more map task with input size as 0
    TaskStatus ts = new MapTaskStatus();
    ts.setOutputSize(singleMapOutputSize);
    JobSplit.TaskSplitMetaInfo split =
        new JobSplit.TaskSplitMetaInfo(new String[0], 0, 0);
    TaskInProgress tip = 
      new TaskInProgress(jid, "", split, jip.jobtracker, jc, jip, 0, 1);
    re.updateWithCompletedTask(ts, tip);
    
    long expectedTotalMapOutSize = (singleMapOutputSize*11) * 
      ((maps*singleMapInputSize)+maps)/((singleMapInputSize+1)*10+1);
    assertEquals(2* expectedTotalMapOutSize/maps, re.getEstimatedMapOutputSize());
  }

}
