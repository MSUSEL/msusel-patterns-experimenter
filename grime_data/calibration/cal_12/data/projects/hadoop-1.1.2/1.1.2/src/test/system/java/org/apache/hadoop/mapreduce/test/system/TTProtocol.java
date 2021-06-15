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

import org.apache.hadoop.mapred.JobTracker;
import org.apache.hadoop.mapred.TaskTracker;
import org.apache.hadoop.mapred.TaskTrackerStatus;
import org.apache.hadoop.mapreduce.TaskID;
import org.apache.hadoop.mapreduce.security.token.JobTokenSelector;
import org.apache.hadoop.security.KerberosInfo;
import org.apache.hadoop.security.token.TokenInfo;
import org.apache.hadoop.test.system.DaemonProtocol;

import java.io.IOException;

/**
 * TaskTracker RPC interface to be used for cluster tests.
 *
 * The protocol has to be annotated so KerberosInfo can be filled in during
 * creation of a ipc.Client connection
 */
@KerberosInfo(
    serverPrincipal = TaskTracker.TT_USER_NAME)
@TokenInfo(JobTokenSelector.class)
public interface TTProtocol extends DaemonProtocol {

  public static final long versionID = 1L;
  /**
   * Gets latest status which was sent in heartbeat to the {@link JobTracker}. 
   * <br/>
   * 
   * @return status of the TaskTracker daemon
   * @throws IOException in case of errors
   */
  TaskTrackerStatus getStatus() throws IOException;

  /**
   * Gets list of all the tasks in the {@link TaskTracker}.<br/>
   * 
   * @return list of all the tasks
   * @throws IOException in case of errors
   */
  TTTaskInfo[] getTasks() throws IOException;

  /**
   * Gets the task associated with the id.<br/>
   * 
   * @param taskID of the task.
   * 
   * @return returns task info <code>TTTaskInfo</code>
   * @throws IOException in case of errors
   */
  TTTaskInfo getTask(TaskID taskID) throws IOException;

  /**
   * Checks if any of process in the process tree of the task is alive
   * or not. <br/>
   * 
   * @param pid
   *          of the task attempt
   * @return true if task process tree is alive.
   * @throws IOException in case of errors
   */
  boolean isProcessTreeAlive(String pid) throws IOException;
}
