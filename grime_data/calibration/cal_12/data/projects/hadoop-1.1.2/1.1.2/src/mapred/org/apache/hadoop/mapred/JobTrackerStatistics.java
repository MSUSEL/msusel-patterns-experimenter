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

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.mapred.StatisticsCollector.Stat;

/**
 * Collects the job tracker statistics.
 *
 */
class JobTrackerStatistics {

  final StatisticsCollector collector;
  final Map<String, TaskTrackerStat> ttStats = 
    new HashMap<String, TaskTrackerStat>();

  JobTrackerStatistics() {
    collector = new StatisticsCollector();
    collector.start();
  }

  synchronized void taskTrackerAdded(String name) {
    TaskTrackerStat stat = ttStats.get(name);
    if(stat == null) {
      stat =  new TaskTrackerStat(name);
      ttStats.put(name, stat);
    }
  }

  synchronized void taskTrackerRemoved(String name) {
    TaskTrackerStat stat = ttStats.remove(name);
    if(stat != null) {
      stat.remove();
    }
  }

  synchronized TaskTrackerStat getTaskTrackerStat(String name) {
    return ttStats.get(name);
  }

  class TaskTrackerStat {
    final String totalTasksKey;
    final Stat totalTasksStat;

    final String succeededTasksKey;
    final Stat succeededTasksStat;

    TaskTrackerStat(String trackerName) {
      totalTasksKey = trackerName+"-"+"totalTasks";
      totalTasksStat = collector.createStat(totalTasksKey);
      succeededTasksKey = trackerName+"-"+"succeededTasks";
      succeededTasksStat = collector.createStat(succeededTasksKey);
    }

    synchronized void incrTotalTasks() {
      totalTasksStat.inc();
    }

    synchronized void incrSucceededTasks() {
      succeededTasksStat.inc();
    }

    synchronized void remove() {
      collector.removeStat(totalTasksKey);
      collector.removeStat(succeededTasksKey);
    }

  }
}
