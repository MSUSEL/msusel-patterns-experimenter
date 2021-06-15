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
package org.apache.hadoop.metrics;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;

/**
 * Utility class to simplify creation and reporting of hadoop metrics.
 *
 * For examples of usage, see NameNodeMetrics.
 * @see org.apache.hadoop.metrics.MetricsRecord
 * @see org.apache.hadoop.metrics.MetricsContext
 * @see org.apache.hadoop.metrics.ContextFactory
 * @deprecated in favor of <code>org.apache.hadoop.metrics2</code> usage.
 */
@Deprecated
@InterfaceAudience.LimitedPrivate({"HDFS", "MapReduce"})
@InterfaceStability.Evolving
public class MetricsUtil {
    
  public static final Log LOG =
    LogFactory.getLog(MetricsUtil.class);

  /**
   * Don't allow creation of a new instance of Metrics
   */
  private MetricsUtil() {}
    
  public static MetricsContext getContext(String contextName) {
    return getContext(contextName, contextName);
  }

  /**
   * Utility method to return the named context.
   * If the desired context cannot be created for any reason, the exception
   * is logged, and a null context is returned.
   */
  public static MetricsContext getContext(String refName, String contextName) {
    MetricsContext metricsContext;
    try {
      metricsContext =
        ContextFactory.getFactory().getContext(refName, contextName);
      if (!metricsContext.isMonitoring()) {
        metricsContext.startMonitoring();
      }
    } catch (Exception ex) {
      LOG.error("Unable to create metrics context " + contextName, ex);
      metricsContext = ContextFactory.getNullContext(contextName);
    }
    return metricsContext;
  }

  /**
   * Utility method to create and return new metrics record instance within the
   * given context. This record is tagged with the host name.
   *
   * @param context the context
   * @param recordName name of the record
   * @return newly created metrics record
   */
  public static MetricsRecord createRecord(MetricsContext context, 
                                           String recordName) 
  {
    MetricsRecord metricsRecord = context.createRecord(recordName);
    metricsRecord.setTag("hostName", getHostName());
    return metricsRecord;        
  }
    
  /**
   * Returns the host name.  If the host name is unobtainable, logs the
   * exception and returns "unknown".
   */
  private static String getHostName() {
    String hostName = null;
    try {
      hostName = InetAddress.getLocalHost().getHostName();
    } 
    catch (UnknownHostException ex) {
      LOG.info("Unable to obtain hostName", ex);
      hostName = "unknown";
    }
    return hostName;
  }

}
