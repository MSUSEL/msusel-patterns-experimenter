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
package org.apache.hadoop.metrics2.lib;

import org.apache.hadoop.metrics2.MetricsSink;
import org.apache.hadoop.metrics2.MetricsSource;
import org.apache.hadoop.metrics2.MetricsSystem;
import org.apache.hadoop.metrics2.impl.MetricsSystemImpl;

/**
 * The default metrics system singleton
 */
public enum DefaultMetricsSystem implements MetricsSystem {

  /**
   * The singleton instance
   */
  INSTANCE;

  private static final int VERSION = 2;
  private final MetricsSystemImpl impl = new MetricsSystemImpl();

  private MetricsSystem init(String prefix) {
    impl.init(prefix);
    return impl;
  }

  /**
   * Common static convenience method to initialize the metrics system
   * @param prefix  for configuration
   * @return the metrics system instance
   */
  public static MetricsSystem initialize(String prefix) {
    return INSTANCE.init(prefix);
  }

  public <T extends MetricsSource> T
  register(String name, String desc, T source) {
    return impl.register(name, desc, source);
  }

  /**
   * Common static method to register a source
   * @param <T>   type of the source
   * @param name  of the source
   * @param desc  description
   * @param source  the source object to register
   * @return the source object
   */
  public static <T extends MetricsSource> T
  registerSource(String name, String desc, T source) {
    return INSTANCE.register(name, desc, source);
  }

  public <T extends MetricsSink> T register(String name, String desc, T sink) {
    return impl.register(name, desc, sink);
  }

  public void register(Callback callback) {
    impl.register(callback);
  }

  public void start() {
    impl.start();
  }

  public void stop() {
    impl.stop();
  }

  public void refreshMBeans() {
    impl.refreshMBeans();
  }

  public String currentConfig() {
    return impl.currentConfig();
  }

  public void shutdown() {
    impl.shutdown();
  }

}
