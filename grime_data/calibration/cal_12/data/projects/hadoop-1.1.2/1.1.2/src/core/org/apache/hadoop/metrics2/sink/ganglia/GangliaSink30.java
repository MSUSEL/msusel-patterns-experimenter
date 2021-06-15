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
package org.apache.hadoop.metrics2.sink.ganglia;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricsException;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.util.MetricsCache;
import org.apache.hadoop.metrics2.util.MetricsCache.Record;

/**
 * This code supports Ganglia 3.0
 *
 */
public class GangliaSink30 extends AbstractGangliaSink {

  public final Log LOG = LogFactory.getLog(this.getClass());

  protected MetricsCache metricsCache = new MetricsCache();

  /*
   *
   * (non-Javadoc)
   *
   * @see
   * org.apache.hadoop.metrics2.MetricsSink#putMetrics(org.apache.hadoop.metrics2
   * .MetricsRecord)
   */
  @Override
  public void putMetrics(MetricsRecord record) {
    // The method handles both cases whether Ganglia support dense publish of
    // metrics of sparse (only on change) publish of metrics
    try {
      String recordName = record.name();
      String contextName = record.context();

      StringBuilder sb = new StringBuilder();
      sb.append(contextName);
      sb.append('.');
      sb.append(recordName);

      String groupName = sb.toString();
      sb.append('.');
      int sbBaseLen = sb.length();

      String type = null;
      GangliaSlope slopeFromMetric = null;
      GangliaSlope calculatedSlope = null;
      Record cachedMetrics = null;
      if (!isSupportSparseMetrics()) {
        // for sending dense metrics, update metrics cache
        // and get the updated data
        cachedMetrics = metricsCache.update(record);

        if (cachedMetrics != null && cachedMetrics.metricsEntrySet() != null) {
          for (Map.Entry<String, Metric> entry : cachedMetrics.metricsEntrySet()) {
            Metric metric = entry.getValue();
            sb.append(metric.name());
            String name = sb.toString();

            // visit the metric to identify the Ganglia type and slope
            metric.visit(gangliaMetricVisitor);
            type = gangliaMetricVisitor.getType();
            slopeFromMetric = gangliaMetricVisitor.getSlope();


            GangliaConf gConf = getGangliaConfForMetric(name);
            calculatedSlope = calculateSlope(gConf, slopeFromMetric);

            // send metric to Ganglia
            emitMetric(groupName, name, type, metric.value().toString(),
                gConf, calculatedSlope);

            // reset the length of the buffer for next iteration
            sb.setLength(sbBaseLen);
          }
        }
      } else {
        // we support sparse updates

        Collection<Metric> metrics = (Collection<Metric>) record.metrics();
        if (metrics.size() > 0) {
          // we got metrics. so send the latest
          for (Metric metric : record.metrics()) {
            sb.append(metric.name());
            String name = sb.toString();

            // visit the metric to identify the Ganglia type and slope
            metric.visit(gangliaMetricVisitor);
            type = gangliaMetricVisitor.getType();
            slopeFromMetric = gangliaMetricVisitor.getSlope();


            GangliaConf gConf = getGangliaConfForMetric(name);
            calculatedSlope = calculateSlope(gConf, slopeFromMetric);

            // send metric to Ganglia
            emitMetric(groupName, name, type, metric.value().toString(),
                gConf, calculatedSlope);

            // reset the length of the buffer for next iteration
            sb.setLength(sbBaseLen);
          }
        }
      }
    } catch (IOException io) {
      throw new MetricsException("Failed to putMetrics", io);
    }
  }


  /**
   * Calculate the slope from properties and metric
   *
   * @param gConf Pass
   * @param slopeFromMetric
   * @return
   */
  private GangliaSlope calculateSlope(GangliaConf gConf, GangliaSlope slopeFromMetric) {
    if (gConf.getSlope() != null) {
      // if slope has been specified in properties, use that
      return gConf.getSlope();
    } else if (slopeFromMetric != null) {
      // slope not specified in properties, use derived from Metric
      return slopeFromMetric;
    } else {
      return DEFAULT_SLOPE;
    }
  }

  /**
   * The method sends metrics to Ganglia servers. The method has been taken from
   * org.apache.hadoop.metrics.ganglia.GangliaContext30 with minimal changes in
   * order to keep it in sync.

   * @param groupName The group name of the metric
   * @param name The metric name
   * @param type The type of the metric
   * @param value The value of the metric
   * @param gConf The GangliaConf for this metric
   * @param gSlope The slope for this metric
   * @throws IOException
   */
  protected void emitMetric(String groupName, String name, String type,
      String value, GangliaConf gConf, GangliaSlope gSlope)
    throws IOException {

    if (name == null) {
      LOG.warn("Metric was emitted with no name.");
      return;
    } else if (value == null) {
      LOG.warn("Metric name " + name + " was emitted with a null value.");
      return;
    } else if (type == null) {
      LOG.warn("Metric name " + name + ", value " + value + " has no type.");
      return;
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Emitting metric " + name + ", type " + type + ", value " + value
          + ", slope " + gSlope.name()+ " from hostname " + getHostName());
    }

    xdr_int(0); // metric_user_defined
    xdr_string(type);
    xdr_string(name);
    xdr_string(value);
    xdr_string(gConf.getUnits());
    xdr_int(gSlope.ordinal());
    xdr_int(gConf.getTmax());
    xdr_int(gConf.getDmax());

    // send the metric to Ganglia hosts
    emitToGangliaHosts();
  }
}
