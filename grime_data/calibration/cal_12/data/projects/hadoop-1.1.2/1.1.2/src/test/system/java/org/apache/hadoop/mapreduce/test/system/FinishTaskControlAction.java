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
package org.apache.hadoop.mapreduce.test.system;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.TaskID;
import org.apache.hadoop.test.system.ControlAction;

/**
 * Control Action which signals a controlled task to proceed to completion. <br/>
 */
public class FinishTaskControlAction extends ControlAction<TaskID> {

  private static final String ENABLE_CONTROLLED_TASK_COMPLETION =
      "test.system.enabled.task.completion.control";

  /**
   * Create a default control action. <br/>
   * 
   */
  public FinishTaskControlAction() {
    super(new TaskID());
  }

  /**
   * Create a control action specific to a particular task. <br/>
   * 
   * @param id
   *          of the task.
   */
  public FinishTaskControlAction(TaskID id) {
    super(id);
  }

  /**
   * Sets up the job to be controlled using the finish task control action. 
   * <br/>
   * 
   * @param conf
   *          configuration to be used submit the job.
   */
  public static void configureControlActionForJob(Configuration conf) {
    conf.setBoolean(ENABLE_CONTROLLED_TASK_COMPLETION, true);
  }
  
  /**
   * Checks if the control action is enabled in the passed configuration. <br/>
   * @param conf configuration
   * @return true if action is enabled.
   */
  public static boolean isControlActionEnabled(Configuration conf) {
    return conf.getBoolean(ENABLE_CONTROLLED_TASK_COMPLETION, false);
  }
}
