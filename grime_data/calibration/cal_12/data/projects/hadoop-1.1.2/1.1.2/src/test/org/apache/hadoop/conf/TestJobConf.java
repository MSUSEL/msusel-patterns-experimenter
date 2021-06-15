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
package org.apache.hadoop.conf;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.hadoop.mapred.JobConf;

public class TestJobConf extends TestCase {

  public void testProfileParamsDefaults() {
    JobConf configuration = new JobConf();

    Assert.assertNull(configuration.get("mapred.task.profile.params"));

    String result = configuration.getProfileParams();

    Assert.assertNotNull(result);
    Assert.assertTrue(result.contains("file=%s"));
    Assert.assertTrue(result.startsWith("-agentlib:hprof"));
  }

  public void testProfileParamsSetter() {
    JobConf configuration = new JobConf();

    configuration.setProfileParams("test");
    Assert.assertEquals("test", configuration.get("mapred.task.profile.params"));
  }

  public void testProfileParamsGetter() {
    JobConf configuration = new JobConf();

    configuration.set("mapred.task.profile.params", "test");
    Assert.assertEquals("test", configuration.getProfileParams());
  }

  /**
   * Testing mapred.task.maxvmem replacement with new values
   *
   */
  public void testMemoryConfigForMapOrReduceTask(){
    JobConf configuration = new JobConf();
    configuration.set("mapred.job.map.memory.mb",String.valueOf(300));
    configuration.set("mapred.job.reduce.memory.mb",String.valueOf(300));
    Assert.assertEquals(configuration.getMemoryForMapTask(),300);
    Assert.assertEquals(configuration.getMemoryForReduceTask(),300);

    configuration.set("mapred.task.maxvmem" , String.valueOf(2*1024 * 1024));
    configuration.set("mapred.job.map.memory.mb",String.valueOf(300));
    configuration.set("mapred.job.reduce.memory.mb",String.valueOf(300));
    Assert.assertEquals(configuration.getMemoryForMapTask(),2);
    Assert.assertEquals(configuration.getMemoryForReduceTask(),2);

    configuration = new JobConf();
    configuration.set("mapred.task.maxvmem" , "-1");
    configuration.set("mapred.job.map.memory.mb",String.valueOf(300));
    configuration.set("mapred.job.reduce.memory.mb",String.valueOf(400));
    Assert.assertEquals(configuration.getMemoryForMapTask(), 300);
    Assert.assertEquals(configuration.getMemoryForReduceTask(), 400);

    configuration = new JobConf();
    configuration.set("mapred.task.maxvmem" , String.valueOf(2*1024 * 1024));
    configuration.set("mapred.job.map.memory.mb","-1");
    configuration.set("mapred.job.reduce.memory.mb","-1");
    Assert.assertEquals(configuration.getMemoryForMapTask(),2);
    Assert.assertEquals(configuration.getMemoryForReduceTask(),2);

    configuration = new JobConf();
    configuration.set("mapred.task.maxvmem" , String.valueOf(-1));
    configuration.set("mapred.job.map.memory.mb","-1");
    configuration.set("mapred.job.reduce.memory.mb","-1");
    Assert.assertEquals(configuration.getMemoryForMapTask(),-1);
    Assert.assertEquals(configuration.getMemoryForReduceTask(),-1);    

    configuration = new JobConf();
    configuration.set("mapred.task.maxvmem" , String.valueOf(2*1024 * 1024));
    configuration.set("mapred.job.map.memory.mb","3");
    configuration.set("mapred.job.reduce.memory.mb","3");
    Assert.assertEquals(configuration.getMemoryForMapTask(),2);
    Assert.assertEquals(configuration.getMemoryForReduceTask(),2);
  }
  
  /**
   * Test that negative values for MAPRED_TASK_MAXVMEM_PROPERTY cause
   * new configuration keys' values to be used.
   */
  
  public void testNegativeValueForTaskVmem() {
    JobConf configuration = new JobConf();
    
    configuration.set(JobConf.MAPRED_TASK_MAXVMEM_PROPERTY, "-3");
    configuration.set("mapred.job.map.memory.mb", "4");
    configuration.set("mapred.job.reduce.memory.mb", "5");
    Assert.assertEquals(4, configuration.getMemoryForMapTask());
    Assert.assertEquals(5, configuration.getMemoryForReduceTask());
    
  }
  
  /**
   * Test that negative values for all memory configuration properties causes
   * APIs to disable memory limits
   */
  
  public void testNegativeValuesForMemoryParams() {
    JobConf configuration = new JobConf();
    
    configuration.set(JobConf.MAPRED_TASK_MAXVMEM_PROPERTY, "-4");
    configuration.set("mapred.job.map.memory.mb", "-5");
    configuration.set("mapred.job.reduce.memory.mb", "-6");
    
    Assert.assertEquals(JobConf.DISABLED_MEMORY_LIMIT,
                        configuration.getMemoryForMapTask());
    Assert.assertEquals(JobConf.DISABLED_MEMORY_LIMIT,
                        configuration.getMemoryForReduceTask());
    Assert.assertEquals(JobConf.DISABLED_MEMORY_LIMIT,
                        configuration.getMaxVirtualMemoryForTask());
  }

  /**
   *   Test deprecated accessor and mutator method for mapred.task.maxvmem
   */
  public void testMaxVirtualMemoryForTask() {
    JobConf configuration = new JobConf();

    //get test case
    configuration.set("mapred.job.map.memory.mb", String.valueOf(300));
    configuration.set("mapred.job.reduce.memory.mb", String.valueOf(-1));
    Assert.assertEquals(
      configuration.getMaxVirtualMemoryForTask(), 300 * 1024 * 1024);

    configuration = new JobConf();
    configuration.set("mapred.job.map.memory.mb", String.valueOf(-1));
    configuration.set("mapred.job.reduce.memory.mb", String.valueOf(200));
    Assert.assertEquals(
      configuration.getMaxVirtualMemoryForTask(), 200 * 1024 * 1024);

    configuration = new JobConf();
    configuration.set("mapred.job.map.memory.mb", String.valueOf(-1));
    configuration.set("mapred.job.reduce.memory.mb", String.valueOf(-1));
    configuration.set("mapred.task.maxvmem", String.valueOf(1 * 1024 * 1024));
    Assert.assertEquals(
      configuration.getMaxVirtualMemoryForTask(), 1 * 1024 * 1024);

    configuration = new JobConf();
    configuration.set("mapred.task.maxvmem", String.valueOf(1 * 1024 * 1024));
    Assert.assertEquals(
      configuration.getMaxVirtualMemoryForTask(), 1 * 1024 * 1024);

    //set test case

    configuration = new JobConf();
    configuration.setMaxVirtualMemoryForTask(2 * 1024 * 1024);
    Assert.assertEquals(configuration.getMemoryForMapTask(), 2);
    Assert.assertEquals(configuration.getMemoryForReduceTask(), 2);

    configuration = new JobConf();   
    configuration.set("mapred.job.map.memory.mb", String.valueOf(300));
    configuration.set("mapred.job.reduce.memory.mb", String.valueOf(400));
    configuration.setMaxVirtualMemoryForTask(2 * 1024 * 1024);
    Assert.assertEquals(configuration.getMemoryForMapTask(), 2);
    Assert.assertEquals(configuration.getMemoryForReduceTask(), 2);
  }
}
