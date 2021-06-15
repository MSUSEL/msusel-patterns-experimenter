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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics.spi.OutputRecord;

/**
 * The main interface to the metrics package. 
 * @deprecated in favor of <code>org.apache.hadoop.metrics2</code> usage.
 */
@Deprecated
@InterfaceAudience.Private
@InterfaceStability.Evolving
public interface MetricsContext {
    
  /**
   * Default period in seconds at which data is sent to the metrics system.
   */
  public static final int DEFAULT_PERIOD = 5;

  /**
   * Initialize this context.
   * @param contextName The given name for this context
   * @param factory The creator of this context
   */
  public void init(String contextName, ContextFactory factory);

  /**
   * Returns the context name.
   *
   * @return the context name
   */
  public abstract String getContextName();
    
  /**
   * Starts or restarts monitoring, the emitting of metrics records as they are 
   * updated. 
   */
  public abstract void startMonitoring()
    throws IOException;

  /**
   * Stops monitoring.  This does not free any data that the implementation
   * may have buffered for sending at the next timer event. It
   * is OK to call <code>startMonitoring()</code> again after calling 
   * this.
   * @see #close()
   */
  public abstract void stopMonitoring();
    
  /**
   * Returns true if monitoring is currently in progress.
   */
  public abstract boolean isMonitoring();
    
  /**
   * Stops monitoring and also frees any buffered data, returning this 
   * object to its initial state.  
   */
  public abstract void close();
    
  /**
   * Creates a new MetricsRecord instance with the given <code>recordName</code>.
   * Throws an exception if the metrics implementation is configured with a fixed
   * set of record names and <code>recordName</code> is not in that set.
   *
   * @param recordName the name of the record
   * @throws MetricsException if recordName conflicts with configuration data
   */
  public abstract MetricsRecord createRecord(String recordName);
    
  /**
   * Registers a callback to be called at regular time intervals, as 
   * determined by the implementation-class specific configuration.
   *
   * @param updater object to be run periodically; it should updated
   * some metrics records and then return
   */
  public abstract void registerUpdater(Updater updater);

  /**
   * Removes a callback, if it exists.
   * 
   * @param updater object to be removed from the callback list
   */
  public abstract void unregisterUpdater(Updater updater);
  
  /**
   * Returns the timer period.
   */
  public abstract int getPeriod();
  
  /**
   * Retrieves all the records managed by this MetricsContext.
   * Useful for monitoring systems that are polling-based.
   * 
   * @return A non-null map from all record names to the records managed.
   */
   Map<String, Collection<OutputRecord>> getAllRecords();
}
