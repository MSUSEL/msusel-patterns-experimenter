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

import org.apache.hadoop.metrics2.MetricsBuilder;
import org.apache.hadoop.metrics2.MetricsSource;
import org.apache.hadoop.metrics2.lib.MetricMutableCounterInt;
import org.apache.hadoop.metrics2.lib.MetricMutableGaugeInt;
import org.apache.hadoop.metrics2.lib.MetricsRegistry;
import org.apache.hadoop.metrics2.source.JvmMetricsSource;

/**
 * Instrumentation for metrics v2
 */
@SuppressWarnings("deprecation")
public class TaskTrackerMetricsSource extends TaskTrackerInstrumentation
                                  implements MetricsSource {

  final MetricsRegistry registry = new MetricsRegistry("tasktracker");
  final MetricMutableGaugeInt mapsRunning =
      registry.newGauge("maps_running", "", 0);
  final MetricMutableGaugeInt redsRunning =
      registry.newGauge("reduces_running", "", 0);
  final MetricMutableGaugeInt mapSlots =
      registry.newGauge("mapTaskSlots", "", 0);
  final MetricMutableGaugeInt redSlots =
      registry.newGauge("reduceTaskSlots", "", 0);
  final MetricMutableGaugeInt failedDirs =
      registry.newGauge("failedDirs", "", 0);
  final MetricMutableCounterInt completedTasks =
      registry.newCounter("tasks_completed", "", 0);
  final MetricMutableCounterInt timedoutTasks =
      registry.newCounter("tasks_failed_timeout", "", 0);
  final MetricMutableCounterInt pingFailedTasks =
      registry.newCounter("tasks_failed_ping", "", 0);

  public TaskTrackerMetricsSource(TaskTracker tt) {
    super(tt);
    String sessionId = tt.getJobConf().getSessionId();
    JvmMetricsSource.create("TaskTracker", sessionId);
    registry.setContext("mapred").tag("sessionId", "", sessionId);
  }

  @Override
  public void getMetrics(MetricsBuilder builder, boolean all) {
    mapsRunning.set(tt.mapTotal);
    redsRunning.set(tt.reduceTotal);
    mapSlots.set(tt.getMaxCurrentMapTasks());
    redSlots.set(tt.getMaxCurrentReduceTasks());
    failedDirs.set(tt.getNumDirFailures());
    registry.snapshot(builder.addRecord(registry.name()), all);
  }

  @Override
  public void completeTask(TaskAttemptID t) {
    completedTasks.incr();
  }

  @Override
  public void timedoutTask(TaskAttemptID t) {
    timedoutTasks.incr();
  }

  @Override
  public void taskFailedPing(TaskAttemptID t) {
    pingFailedTasks.incr();
  }

}
