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
package org.apache.hadoop.metrics2;

/**
 * The metrics system interface
 */
public interface MetricsSystem extends MetricsSystemMXBean {

  /**
   * Register a metrics source
   * @param <T>   the type of the source
   * @param source  to register
   * @param name  of the source. Must be unique.
   * @param desc  the description of the source.
   * @return the source
   * @exception MetricsException
   */
  <T extends MetricsSource> T register(String name, String desc, T source);

  /**
   * Register a metrics sink
   * @param <T>   the type of the sink
   * @param sink  to register
   * @param name  of the sink. Must be unique.
   * @param desc  the description of the sink
   * @return the sink
   * @exception MetricsException
   */
  <T extends MetricsSink> T register(String name, String desc, T sink);

  /**
   * Register a callback interface for JMX events
   * @param callback  the callback object implementing the MBean interface.
   */
  void register(Callback callback);

  /**
   * Shutdown the metrics system completely (usually during server shutdown.)
   * The MetricsSystemMXBean will be unregistered.
   */
  void shutdown();

  /**
   * The metrics system callback interface
   */
  @SuppressWarnings("PublicInnerClass")
  static interface Callback {

    /**
     * Called before start()
     */
    void preStart();

    /**
     * Called after start()
     */
    void postStart();

    /**
     * Called before stop()
     */
    void preStop();

    /**
     * Called after stop()
     */
    void postStop();

  }

  /**
   * Convenient abstract class for implementing callback interface
   */
  @SuppressWarnings("PublicInnerClass")
  public static abstract class AbstractCallback implements Callback {

    public void preStart() {}
    public void postStart() {}
    public void preStop() {}
    public void postStop() {}

  }

}
