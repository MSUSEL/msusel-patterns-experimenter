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
package org.apache.hadoop.metrics.spi;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics.ContextFactory;
import org.apache.hadoop.metrics.MetricsException;

/**
 * A null context which has a thread calling 
 * periodically when monitoring is started. This keeps the data sampled 
 * correctly.
 * In all other respects, this is like the NULL context: No data is emitted.
 * This is suitable for Monitoring systems like JMX which reads the metrics
 *  when someone reads the data from JMX.
 * 
 * The default impl of start and stop monitoring:
 *  is the AbstractMetricsContext is good enough.
 * 
 * @deprecated in favor of <code>org.apache.hadoop.metrics2</code> usage.
 */
@Deprecated
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class NullContextWithUpdateThread extends AbstractMetricsContext {
  
  private static final String PERIOD_PROPERTY = "period";
    
  /** Creates a new instance of NullContextWithUpdateThread */
  @InterfaceAudience.Private
  public NullContextWithUpdateThread() {
  }
  
  @InterfaceAudience.Private
  public void init(String contextName, ContextFactory factory) {
    super.init(contextName, factory);
    parseAndSetPeriod(PERIOD_PROPERTY);
  }
   
    
  /**
   * Do-nothing version of emitRecord
   */
  @InterfaceAudience.Private
  protected void emitRecord(String contextName, String recordName,
                            OutputRecord outRec) 
  {}
    
  /**
   * Do-nothing version of update
   */
  @InterfaceAudience.Private
  protected void update(MetricsRecordImpl record) {
  }
    
  /**
   * Do-nothing version of remove
   */
  @InterfaceAudience.Private
  protected void remove(MetricsRecordImpl record) {
  }
}
