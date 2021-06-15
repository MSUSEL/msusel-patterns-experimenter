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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.TaskTrackerStatus.TaskTrackerHealthStatus;

import junit.framework.TestCase;

public class TestNodeHealthService extends TestCase {

  private static volatile Log LOG = LogFactory
      .getLog(TestNodeHealthService.class);

  private static final String nodeHealthConfigPath = System.getProperty(
      "test.build.extraconf", "build/test/extraconf");

  final static File nodeHealthConfigFile = new File(nodeHealthConfigPath,
      "mapred-site.xml");

  private String testRootDir = new File(System.getProperty("test.build.data",
      "/tmp")).getAbsolutePath();

  private File nodeHealthscriptFile = new File(testRootDir, "failingscript.sh");

  @Override
  protected void tearDown() throws Exception {
    if (nodeHealthConfigFile.exists()) {
      nodeHealthConfigFile.delete();
    }
    if (nodeHealthscriptFile.exists()) {
      nodeHealthscriptFile.delete();
    }
    super.tearDown();
  }

  private Configuration getConfForNodeHealthScript() {
    Configuration conf = new Configuration();
    conf.set(NodeHealthCheckerService.HEALTH_CHECK_SCRIPT_PROPERTY,
        nodeHealthscriptFile.getAbsolutePath());
    conf.setLong(NodeHealthCheckerService.HEALTH_CHECK_INTERVAL_PROPERTY, 500);
    conf.setLong(
        NodeHealthCheckerService.HEALTH_CHECK_FAILURE_INTERVAL_PROPERTY, 1000);
    return conf;
  }

  private void writeNodeHealthScriptFile(String scriptStr, boolean setExecutable)
      throws IOException {
    PrintWriter pw = new PrintWriter(new FileOutputStream(nodeHealthscriptFile));
    pw.println(scriptStr);
    pw.flush();
    pw.close();
    nodeHealthscriptFile.setExecutable(setExecutable);
  }

  public void testNodeHealthScriptShouldRun() throws IOException {
    // Node health script should not start if there is no property called
    // node health script path.
    assertFalse("Health checker should not have started",
        NodeHealthCheckerService.shouldRun(new Configuration()));
    Configuration conf = getConfForNodeHealthScript();
    // Node health script should not start if the node health script does not
    // exists
    assertFalse("Node health script should start", NodeHealthCheckerService
        .shouldRun(conf));
    // Create script path.
    conf.writeXml(new FileOutputStream(nodeHealthConfigFile));
    writeNodeHealthScriptFile("", false);
    // Node health script should not start if the node health script is not
    // executable.
    assertFalse("Node health script should start", NodeHealthCheckerService
        .shouldRun(conf));
    writeNodeHealthScriptFile("", true);
    assertTrue("Node health script should start", NodeHealthCheckerService
        .shouldRun(conf));
  }

  public void testNodeHealthScript() throws Exception {
    TaskTrackerHealthStatus healthStatus = new TaskTrackerHealthStatus();
    String errorScript = "echo ERROR\n echo \"Tracker not healthy\"";
    String normalScript = "echo \"I am all fine\"";
    String timeOutScript = "sleep 4\n echo\"I am fine\"";
    Configuration conf = getConfForNodeHealthScript();
    conf.writeXml(new FileOutputStream(nodeHealthConfigFile));

    NodeHealthCheckerService nodeHealthChecker = new NodeHealthCheckerService(
        conf);
    TimerTask timer = nodeHealthChecker.getTimer();
    writeNodeHealthScriptFile(normalScript, true);
    timer.run();

    nodeHealthChecker.setHealthStatus(healthStatus);
    LOG.info("Checking initial healthy condition");
    // Check proper report conditions.
    assertTrue("Node health status reported unhealthy", healthStatus
        .isNodeHealthy());
    assertTrue("Node health status reported unhealthy", healthStatus
        .getHealthReport().isEmpty());

    // write out error file.
    // Healthy to unhealthy transition
    writeNodeHealthScriptFile(errorScript, true);
    // Run timer
    timer.run();
    // update health status
    nodeHealthChecker.setHealthStatus(healthStatus);
    LOG.info("Checking Healthy--->Unhealthy");
    assertFalse("Node health status reported healthy", healthStatus
        .isNodeHealthy());
    assertFalse("Node health status reported healthy", healthStatus
        .getHealthReport().isEmpty());
    
    // Check unhealthy to healthy transitions.
    writeNodeHealthScriptFile(normalScript, true);
    timer.run();
    nodeHealthChecker.setHealthStatus(healthStatus);
    LOG.info("Checking UnHealthy--->healthy");
    // Check proper report conditions.
    assertTrue("Node health status reported unhealthy", healthStatus
        .isNodeHealthy());
    assertTrue("Node health status reported unhealthy", healthStatus
        .getHealthReport().isEmpty());

    // Healthy to timeout transition.
    writeNodeHealthScriptFile(timeOutScript, true);
    timer.run();
    nodeHealthChecker.setHealthStatus(healthStatus);
    LOG.info("Checking Healthy--->timeout");
    assertFalse("Node health status reported healthy even after timeout",
        healthStatus.isNodeHealthy());
    assertEquals("Node time out message not propogated", healthStatus
        .getHealthReport(),
        NodeHealthCheckerService.NODE_HEALTH_SCRIPT_TIMED_OUT_MSG);
  }

}
