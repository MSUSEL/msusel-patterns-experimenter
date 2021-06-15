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

import junit.framework.TestCase;

import org.apache.hadoop.examples.SleepJob;

@SuppressWarnings("deprecation")
public class TestJobTrackerInstrumentation extends TestCase {

  public void testSlots() throws IOException {
    MiniMRCluster mr = null;
    try {
      JobConf jtConf = new JobConf();
      jtConf.set("mapred.jobtracker.instrumentation", 
          MyJobTrackerMetricsInst.class.getName());
      mr = new MiniMRCluster(2, "file:///", 3, null, null, jtConf);
      MyJobTrackerMetricsInst instr = (MyJobTrackerMetricsInst) 
        mr.getJobTrackerRunner().getJobTracker().getInstrumentation();

      JobConf conf = mr.createJobConf();
      SleepJob job = new SleepJob();
      job.setConf(conf);
      int numMapTasks = 3;
      int numReduceTasks = 2;
      job.run(numMapTasks, numReduceTasks, 10000, 1, 10000, 1);
      
      synchronized (instr) {
        //after the job completes, incr and decr should be equal
        assertEquals(instr.incrOccupiedMapSlots, 
            instr.decrOccupiedMapSlots);
        assertEquals(instr.incrOccupiedReduceSlots, 
            instr.decrOccupiedReduceSlots);
        assertEquals(instr.incrRunningMaps,
            instr.decrRunningMaps);
        assertEquals(instr.incrRunningReduces,
            instr.decrRunningReduces);
        assertEquals(instr.incrReservedMapSlots,
            instr.decrReservedMapSlots);
        assertEquals(instr.incrReservedReduceSlots,
            instr.decrReservedReduceSlots);
        
        //validate that atleast once the callbacks happened
        assertTrue(instr.incrOccupiedMapSlots > 0);
        assertTrue(instr.incrOccupiedReduceSlots > 0);
        assertTrue(instr.incrRunningMaps > 0);
        assertTrue(instr.incrRunningReduces > 0);
      }
    } finally {
      if (mr != null) {
        mr.shutdown();
      }
    }
  }

  static class MyJobTrackerMetricsInst extends JobTrackerInstrumentation  {
    public MyJobTrackerMetricsInst(JobTracker tracker, JobConf conf) {
      super(tracker, conf);
    }

    private int incrReservedMapSlots = 0;
    private int decrReservedMapSlots = 0;
    private int incrReservedReduceSlots = 0;
    private int decrReservedReduceSlots = 0;
    private int incrOccupiedMapSlots = 0;
    private int decrOccupiedMapSlots = 0;
    private int incrOccupiedReduceSlots = 0;
    private int decrOccupiedReduceSlots = 0;
    private int incrRunningMaps = 0;
    private int decrRunningMaps = 0;
    private int incrRunningReduces = 0;
    private int decrRunningReduces = 0;

    @Override
    public synchronized void addReservedMapSlots(int slots)
    { 
      incrReservedMapSlots += slots;
    }

    @Override
    public synchronized void decReservedMapSlots(int slots)
    {
      decrReservedMapSlots += slots;
    }

    @Override
    public synchronized void addReservedReduceSlots(int slots)
    {
      incrReservedReduceSlots += slots;
    }

    @Override
    public synchronized void decReservedReduceSlots(int slots)
    {
      decrReservedReduceSlots += slots;
    }

    @Override
    public synchronized void addOccupiedMapSlots(int slots)
    {
      incrOccupiedMapSlots += slots;
    }

    @Override
    public synchronized void decOccupiedMapSlots(int slots)
    {
      decrOccupiedMapSlots += slots;
    }

    @Override
    public synchronized void addOccupiedReduceSlots(int slots)
    {
      incrOccupiedReduceSlots += slots;
    }

    @Override
    public synchronized void decOccupiedReduceSlots(int slots)
    {
      decrOccupiedReduceSlots += slots;
    }
    
    @Override
    public synchronized void addRunningMaps(int task)
    {
      incrRunningMaps += task;
    }

    @Override
    public synchronized void decRunningMaps(int task) 
    {
      decrRunningMaps += task;
    }

    @Override
    public synchronized void addRunningReduces(int task)
    {
      incrRunningReduces += task;
    }

    @Override
    public synchronized void decRunningReduces(int task)
    {
      decrRunningReduces += task;
    }
  }
}
