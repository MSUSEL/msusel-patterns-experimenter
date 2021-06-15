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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This code supports Ganglia 3.1
 *
 */
public class GangliaSink31 extends GangliaSink30 {

  public final Log LOG = LogFactory.getLog(this.getClass());

  /**
   * The method sends metrics to Ganglia servers. The method has been taken from
   * org.apache.hadoop.metrics.ganglia.GangliaContext31 with minimal changes in
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
      LOG.warn("Metric name " + name +" was emitted with a null value.");
      return;
    } else if (type == null) {
      LOG.warn("Metric name " + name + ", value " + value + " has no type.");
      return;
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Emitting metric " + name + ", type " + type + ", value " + value
          + ", slope " + gSlope.name()+ " from hostname " + getHostName());
    }

    // The following XDR recipe was done through a careful reading of
    // gm_protocol.x in Ganglia 3.1 and carefully examining the output of
    // the gmetric utility with strace.

    // First we send out a metadata message
    xdr_int(128);               // metric_id = metadata_msg
    xdr_string(getHostName());       // hostname
    xdr_string(name);           // metric name
    xdr_int(0);                 // spoof = False
    xdr_string(type);           // metric type
    xdr_string(name);           // metric name
    xdr_string(gConf.getUnits());    // units
    xdr_int(gSlope.ordinal());  // slope
    xdr_int(gConf.getTmax());        // tmax, the maximum time between metrics
    xdr_int(gConf.getDmax());        // dmax, the maximum data value
    xdr_int(1);                 /*Num of the entries in extra_value field for
                                  Ganglia 3.1.x*/
    xdr_string("GROUP");        /*Group attribute*/
    xdr_string(groupName);      /*Group value*/

    // send the metric to Ganglia hosts
    emitToGangliaHosts();

    // Now we send out a message with the actual value.
    // Technically, we only need to send out the metadata message once for
    // each metric, but I don't want to have to record which metrics we did and
    // did not send.
    xdr_int(133);         // we are sending a string value
    xdr_string(getHostName()); // hostName
    xdr_string(name);     // metric name
    xdr_int(0);           // spoof = False
    xdr_string("%s");     // format field
    xdr_string(value);    // metric value

    // send the metric to Ganglia hosts
    emitToGangliaHosts();
  }
}
