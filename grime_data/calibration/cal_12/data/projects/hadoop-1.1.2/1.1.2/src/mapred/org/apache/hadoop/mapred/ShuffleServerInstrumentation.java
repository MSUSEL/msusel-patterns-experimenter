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
import org.apache.hadoop.metrics2.MetricsRecordBuilder;
import org.apache.hadoop.metrics2.MetricsSource;
import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;
import org.apache.hadoop.metrics2.lib.MetricMutableCounterInt;
import org.apache.hadoop.metrics2.lib.MetricMutableCounterLong;
import org.apache.hadoop.metrics2.lib.MetricsRegistry;

class ShuffleServerInstrumentation implements MetricsSource {
  final int ttWorkerThreads;
  final MetricsRegistry registry = new MetricsRegistry("shuffleOutput");
  private volatile int serverHandlerBusy = 0;
  final MetricMutableCounterLong outputBytes =
      registry.newCounter("shuffle_output_bytes", "", 0L);
  final MetricMutableCounterInt failedOutputs =
      registry.newCounter("shuffle_failed_outputs", "", 0);
  final MetricMutableCounterInt successOutputs =
      registry.newCounter("shuffle_success_outputs", "", 0);
  final MetricMutableCounterInt exceptionsCaught =
    registry.newCounter("shuffle_exceptions_caught", "", 0);

  ShuffleServerInstrumentation(TaskTracker tt) {
    ttWorkerThreads = tt.workerThreads;
    registry.setContext("mapred")
        .tag("sessionId", "session id", tt.getJobConf().getSessionId());
  }

  //@Override
  synchronized void serverHandlerBusy() {
    ++serverHandlerBusy;
  }

  //@Override
  synchronized void serverHandlerFree() {
    --serverHandlerBusy;
  }

  //@Override
  void outputBytes(long bytes) {
    outputBytes.incr(bytes);
  }

  //@Override
  void failedOutput() {
    failedOutputs.incr();
  }

  //@Override
  void successOutput() {
    successOutputs.incr();
  }

  //@Override
  void exceptionsCaught() {
    exceptionsCaught.incr();
  }


  @Override
  public void getMetrics(MetricsBuilder builder, boolean all) {
    MetricsRecordBuilder rb = builder.addRecord(registry.name());
    rb.addGauge("shuffle_handler_busy_percent", "", ttWorkerThreads == 0 ? 0
        : 100. * serverHandlerBusy / ttWorkerThreads);
    registry.snapshot(rb, all);
  }

  static ShuffleServerInstrumentation create(TaskTracker tt) {
    return create(tt, DefaultMetricsSystem.INSTANCE);
  }

  static ShuffleServerInstrumentation create(TaskTracker tt, MetricsSystem ms) {
    return ms.register("ShuffleServerMetrics", "Shuffle output metrics",
                      new ShuffleServerInstrumentation(tt));
  }
  
}
