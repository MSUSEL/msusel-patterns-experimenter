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
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.mapred.Task.TaskReporter;
import org.apache.hadoop.mapreduce.test.system.FinishTaskControlAction;
import org.apache.hadoop.test.system.ControlAction;
import org.apache.hadoop.test.system.DaemonProtocol;
import org.apache.hadoop.mapreduce.test.system.TTProtocol;

public privileged aspect TaskAspect {

  private static final Log LOG = LogFactory.getLog(TaskAspect.class);
  
  private Object waitObject = new Object();
  private AtomicBoolean isWaitingForSignal = new AtomicBoolean(false);
  
  private DaemonProtocol daemonProxy;

  pointcut taskDoneIntercept(Task task) : execution(
      public void Task.done(..)) && target(task);
  
  void around(Task task) : taskDoneIntercept(task) {
    if(task.isJobCleanupTask() || task.isJobSetupTask() || task.isTaskCleanupTask()) {
      proceed(task);
      return;
    }
    Configuration conf = task.getConf();
    boolean controlEnabled = FinishTaskControlAction.isControlActionEnabled(conf);
    if(controlEnabled) {
      LOG.info("Task control enabled, waiting till client sends signal to " +
      "complete");
      try {
        synchronized (waitObject) {
          isWaitingForSignal.set(true);
          waitObject.wait();
        }
      } catch (InterruptedException e) {
      }
    }
    proceed(task);
    return;
  }
  
  pointcut taskStatusUpdate(TaskReporter reporter, TaskAttemptID id, JvmContext context) : 
    call(public boolean TaskUmbilicalProtocol.ping(TaskAttemptID, JvmContext))
          && this(reporter) && args(id, context);
  
  after(TaskReporter reporter, TaskAttemptID id, JvmContext context) throws IOException : 
    taskStatusUpdate(reporter, id, context)  {
    synchronized (waitObject) {
      if(isWaitingForSignal.get()) {
        ControlAction[] actions = daemonProxy.getActions(
            id.getTaskID());
        if(actions.length == 0) {
          return;
        }
        boolean shouldProceed = false;
        for(ControlAction action : actions) {
          if (action instanceof FinishTaskControlAction) {
            LOG.info("Recv : Control task action to finish task id: " 
                + action.getTarget());
            shouldProceed = true;
            daemonProxy.removeAction(action);
            LOG.info("Removed the control action from TaskTracker");
            break;
          }
        }
        if(shouldProceed) {
          LOG.info("Notifying the task to completion");
          waitObject.notify();
        }
      }
    }
  }
  
  
  pointcut rpcInterceptor(Class k, long version,InetSocketAddress addr, 
      Configuration conf) : call(
          public static * RPC.getProxy(Class, long ,InetSocketAddress,
              Configuration)) && args(k, version,addr, conf) && 
              within(org.apache.hadoop.mapred.Child) ;
  
  after(Class k, long version, InetSocketAddress addr, Configuration conf) 
    throws IOException : rpcInterceptor(k, version, addr, conf) {
    daemonProxy = 
      (TTProtocol) RPC.getProxy(
          TTProtocol.class, TTProtocol.versionID, addr, conf);
  }
  
}
