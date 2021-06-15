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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.net.URL;

import org.junit.Test;

public class TestCapacitySchedulerServlet extends
    ClusterWithCapacityScheduler {

  /**
   * Test case checks CapacitySchedulerServlet. Check if queues are 
   * initialized {@link CapacityTaskScheduler} 
   * 
   * @throws IOException
   */
  @Test
  public void testCapacitySchedulerServlet() throws IOException {
    Properties schedulerProps = new Properties();
    String[] queues = new String[] { "Q1", "Q2" };
    for (String q : queues) {
      schedulerProps.put(CapacitySchedulerConf
          .toFullPropertyName(q, "capacity"), "50");
      schedulerProps.put(CapacitySchedulerConf.toFullPropertyName(q,
          "minimum-user-limit-percent"), "100");
    }
    Properties clusterProps = new Properties();
    clusterProps.put("mapred.tasktracker.map.tasks.maximum", String.valueOf(2));
    clusterProps.put("mapred.tasktracker.reduce.tasks.maximum", String
        .valueOf(2));
    clusterProps.put("mapred.queue.names", queues[0] + "," + queues[1]);
    startCluster(2, clusterProps, schedulerProps);

    JobTracker jt = getJobTracker();
    int port = jt.getInfoPort();
    String host = jt.getJobTrackerMachine();
    URL url = new URL("http://" + host + ":" + port + "/scheduler");
    String queueData = readOutput(url);
    assertTrue(queueData.contains("Q1"));
    assertTrue(queueData.contains("Q2"));
    assertTrue(queueData.contains("50.0%"));
  }

  private String readOutput(URL url) throws IOException {
    StringBuilder out = new StringBuilder();
    InputStream in = url.openConnection().getInputStream();
    byte[] buffer = new byte[64 * 1024];
    int len = in.read(buffer);
    while (len > 0) {
      out.append(new String(buffer, 0, len));
      len = in.read(buffer);
    }
    return out.toString();
  }
}
