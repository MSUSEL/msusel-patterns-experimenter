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
package org.apache.hadoop.metrics.ganglia;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.metrics.ContextFactory;
import org.apache.hadoop.net.DNS;

/**
 * Context for sending metrics to Ganglia version 3.1.x.
 * 
 * 3.1.1 has a slightly different wire portal compared to 3.0.x.
 */
public class GangliaContext31 extends GangliaContext {

  String hostName = "UNKNOWN.example.com";

  private static final Log LOG = 
    LogFactory.getLog("org.apache.hadoop.util.GangliaContext31");

  public void init(String contextName, ContextFactory factory) {
    super.init(contextName, factory);

    LOG.debug("Initializing the GangliaContext31 for Ganglia 3.1 metrics.");

    // Take the hostname from the DNS class.

    Configuration conf = new Configuration();

    if (conf.get("slave.host.name") != null) {
      hostName = conf.get("slave.host.name");
    } else {
      try {
        hostName = DNS.getDefaultHost(
          conf.get("dfs.datanode.dns.interface","default"),
          conf.get("dfs.datanode.dns.nameserver","default"));
      } catch (UnknownHostException uhe) {
        LOG.error(uhe);
    	hostName = "UNKNOWN.example.com";
      }
    }
  }

  protected void emitMetric(String name, String type,  String value) 
    throws IOException
  {
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

    LOG.debug("Emitting metric " + name + ", type " + type + ", value " + 
      value + " from hostname" + hostName);

    String units = getUnits(name);
    if (units == null) {
      LOG.warn("Metric name " + name + ", value " + value
        + " had 'null' units");
      units = "";
    }
    int slope = getSlope(name);
    int tmax = getTmax(name);
    int dmax = getDmax(name);
    offset = 0;
    String groupName = name.substring(0,name.lastIndexOf("."));

    // The following XDR recipe was done through a careful reading of
    // gm_protocol.x in Ganglia 3.1 and carefully examining the output of
    // the gmetric utility with strace.

    // First we send out a metadata message
    xdr_int(128);         // metric_id = metadata_msg
    xdr_string(hostName); // hostname
    xdr_string(name);     // metric name
    xdr_int(0);           // spoof = False
    xdr_string(type);     // metric type
    xdr_string(name);     // metric name
    xdr_string(units);    // units
    xdr_int(slope);       // slope
    xdr_int(tmax);        // tmax, the maximum time between metrics
    xdr_int(dmax);        // dmax, the maximum data value

    xdr_int(1);             /*Num of the entries in extra_value field for 
                              Ganglia 3.1.x*/
    xdr_string("GROUP");    /*Group attribute*/
    xdr_string(groupName);  /*Group value*/

    for (SocketAddress socketAddress : metricsServers) {
      DatagramPacket packet =
        new DatagramPacket(buffer, offset, socketAddress);
      datagramSocket.send(packet);
    }

    // Now we send out a message with the actual value.
    // Technically, we only need to send out the metadata message once for
    // each metric, but I don't want to have to record which metrics we did and
    // did not send.
    offset = 0;
    xdr_int(133);         // we are sending a string value
    xdr_string(hostName); // hostName
    xdr_string(name);     // metric name
    xdr_int(0);           // spoof = False
    xdr_string("%s");     // format field
    xdr_string(value);    // metric value
        
    for (SocketAddress socketAddress : metricsServers) {
      DatagramPacket packet = 
        new DatagramPacket(buffer, offset, socketAddress);
      datagramSocket.send(packet);
    }
  }

}
