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
package org.apache.hadoop.tools.rumen;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a wrapper class around {@link LoggedTaskAttempt}. This provides
 * also the extra information about the task attempt obtained from
 * job history which is not written to the JSON trace file.
 */
public class ParsedTaskAttempt extends LoggedTaskAttempt {

  private static final Log LOG = LogFactory.getLog(ParsedTaskAttempt.class);

  private String diagnosticInfo;
  private String stateString;
  private String trackerName;
  private Integer httpPort;
  private Map<String, Long> countersMap = new HashMap<String, Long>();

  ParsedTaskAttempt() {
    super();
  }

  /** incorporate event counters */
  public void incorporateCounters(JhCounters counters) {

    Map<String, Long> countersMap =
      JobHistoryUtils.extractCounters(counters);
    putCounters(countersMap);

    super.incorporateCounters(counters);
  }

  /** Set the task attempt counters */
  public void putCounters(Map<String, Long> counters) {
    this.countersMap = counters;
  }

  /**
   * @return the task attempt counters
   */
  public Map<String, Long> obtainCounters() {
    return countersMap;
  }

  /** Set the task attempt diagnostic-info */
  public void putDiagnosticInfo(String msg) {
    diagnosticInfo = msg;
  }

  /**
   * @return the diagnostic-info of this task attempt.
   *         If the attempt is successful, returns null.
   */
  public String obtainDiagnosticInfo() {
    return diagnosticInfo;
  }

  void putTrackerName(String trackerName) {
    this.trackerName = trackerName;
  }

  /**
   * @return the tracker name where the attempt was run.
   */
  public String obtainTrackerName() {
    return trackerName;
  }

  void putHttpPort(int port) {
    httpPort = port;
  }

  /**
   * @return http port if set. Returns null otherwise.
   */
  public Integer obtainHttpPort() {
    return httpPort;
  }

  void putStateString(String state) {
    stateString = state;
  }

  /**
   * @return state string of the task attempt.
   */
  public String obtainStateString() {
    return stateString;
  }

  /** Dump the extra info of ParsedTaskAttempt */
  void dumpParsedTaskAttempt() {
    LOG.info("ParsedTaskAttempt details:" + obtainCounters()
        + ";DiagnosticInfo=" + obtainDiagnosticInfo() + "\n"
        + obtainTrackerName() + ";" + obtainHttpPort()
        + ";host=" + getHostName());
  }
}
