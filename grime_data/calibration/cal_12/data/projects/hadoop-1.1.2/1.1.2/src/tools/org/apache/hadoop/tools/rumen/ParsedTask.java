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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a wrapper class around {@link LoggedTask}. This provides also the
 * extra information about the task obtained from job history which is not
 * written to the JSON trace file.
 */
public class ParsedTask extends LoggedTask {

  private static final Log LOG = LogFactory.getLog(ParsedTask.class);

  private String diagnosticInfo;
  private String failedDueToAttempt;
  private Map<String, Long> countersMap = new HashMap<String, Long>();

  ParsedTask() {
    super();
  }

  public void incorporateCounters(JhCounters counters) {
    Map<String, Long> countersMap =
        JobHistoryUtils.extractCounters(counters);
    putCounters(countersMap);

    super.incorporateCounters(counters);
  }

  /** Set the task counters */
  public void putCounters(Map<String, Long> counters) {
    this.countersMap = counters;
  }

  /**
   * @return the task counters
   */
  public Map<String, Long> obtainCounters() {
    return countersMap;
  }

  /** Set the task diagnostic-info */
  public void putDiagnosticInfo(String msg) {
    diagnosticInfo = msg;
  }

  /**
   * @return the diagnostic-info of this task.
   *         If the task is successful, returns null.
   */
  public String obtainDiagnosticInfo() {
    return diagnosticInfo;
  }

  /**
   * Set the failed-due-to-attemptId info of this task.
   */
  public void putFailedDueToAttemptId(String attempt) {
    failedDueToAttempt = attempt;
  }

  /**
   * @return the failed-due-to-attemptId info of this task.
   *         If the task is successful, returns null.
   */
  public String obtainFailedDueToAttemptId() {
    return failedDueToAttempt;
  }

  public List<ParsedTaskAttempt> obtainTaskAttempts() {
    List<LoggedTaskAttempt> attempts = getAttempts();
    return convertTaskAttempts(attempts);
  }

  List<ParsedTaskAttempt> convertTaskAttempts(
      List<LoggedTaskAttempt> attempts) {
    List<ParsedTaskAttempt> result = new ArrayList<ParsedTaskAttempt>();

    for (LoggedTaskAttempt t : attempts) {
      if (t instanceof ParsedTaskAttempt) {
        result.add((ParsedTaskAttempt)t);
      } else {
        throw new RuntimeException(
            "Unexpected type of taskAttempts in the list...");
      }
    }
    return result;
  }

  /** Dump the extra info of ParsedTask */
  void dumpParsedTask() {
    LOG.info("ParsedTask details:" + obtainCounters()
        + "\n" + obtainFailedDueToAttemptId()
        + "\nPreferred Locations are:");
    List<LoggedLocation> loc = getPreferredLocations();
    for (LoggedLocation l : loc) {
      LOG.info(l.getLayers() + ";" + l.toString());
    }
    List<ParsedTaskAttempt> attempts = obtainTaskAttempts();
    for (ParsedTaskAttempt attempt : attempts) {
      attempt.dumpParsedTaskAttempt();
    }
  }
}
