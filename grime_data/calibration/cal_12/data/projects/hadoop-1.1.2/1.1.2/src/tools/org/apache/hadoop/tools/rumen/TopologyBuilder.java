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

import java.util.Set;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Building the cluster topology.
 */
public class TopologyBuilder {
  private Set<ParsedHost> allHosts = new HashSet<ParsedHost>();

  /**
   * Process one {@link HistoryEvent}
   * 
   * @param event
   *          The {@link HistoryEvent} to be processed.
   */
  public void process(HistoryEvent event) {
    if (event instanceof TaskAttemptFinishedEvent) {
      processTaskAttemptFinishedEvent((TaskAttemptFinishedEvent) event);
    } else if (event instanceof TaskAttemptUnsuccessfulCompletionEvent) {
      processTaskAttemptUnsuccessfulCompletionEvent((TaskAttemptUnsuccessfulCompletionEvent) event);
    } else if (event instanceof TaskStartedEvent) {
      processTaskStartedEvent((TaskStartedEvent) event);
    }

    // I do NOT expect these if statements to be exhaustive.
  }

  /**
   * Process a collection of JobConf {@link Properties}. We do not restrict it
   * to be called once.
   * 
   * @param conf
   *          The job conf properties to be added.
   */
  public void process(Properties conf) {
    // no code
  }

  /**
   * Request the builder to build the final object. Once called, the
   * {@link TopologyBuilder} would accept no more events or job-conf properties.
   * 
   * @return Parsed {@link LoggedNetworkTopology} object.
   */
  public LoggedNetworkTopology build() {
    return new LoggedNetworkTopology(allHosts);
  }

  private void processTaskStartedEvent(TaskStartedEvent event) {
    preferredLocationForSplits(event.getSplitLocations());
  }

  private void processTaskAttemptUnsuccessfulCompletionEvent(
      TaskAttemptUnsuccessfulCompletionEvent event) {
    recordParsedHost(event.getHostname());
  }

  private void processTaskAttemptFinishedEvent(TaskAttemptFinishedEvent event) {
    recordParsedHost(event.getHostname());
  }

  private void recordParsedHost(String hostName) {
    ParsedHost result = ParsedHost.parse(hostName);

    if (result != null && !allHosts.contains(result)) {
      allHosts.add(result);
    }
  }

  private void preferredLocationForSplits(String splits) {
    if (splits != null) {
      StringTokenizer tok = new StringTokenizer(splits, ",", false);

      while (tok.hasMoreTokens()) {
        String nextSplit = tok.nextToken();

        recordParsedHost(nextSplit);
      }
    }
  }
}
