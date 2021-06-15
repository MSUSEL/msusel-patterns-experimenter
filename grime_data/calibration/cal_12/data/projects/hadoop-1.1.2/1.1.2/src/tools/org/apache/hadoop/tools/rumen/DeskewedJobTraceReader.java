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

import java.io.Closeable;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DeskewedJobTraceReader implements Closeable {
  // underlying engine
  private final JobTraceReader reader;

  // configuration variables
  private final int skewBufferLength;

  private final boolean abortOnUnfixableSkew;

  // state variables
  private long skewMeasurementLatestSubmitTime = Long.MIN_VALUE;

  private long returnedLatestSubmitTime = Long.MIN_VALUE;

  private int maxSkewBufferNeeded = 0;

  // a submit time will NOT be in countedRepeatedSubmitTimesSoFar if
  // it only occurs once. This situation is represented by having the
  // time in submitTimesSoFar only. A submit time that occurs twice or more
  // appears in countedRepeatedSubmitTimesSoFar [with the appropriate range
  // value] AND submitTimesSoFar
  private TreeMap<Long, Integer> countedRepeatedSubmitTimesSoFar =
      new TreeMap<Long, Integer>();
  private TreeSet<Long> submitTimesSoFar = new TreeSet<Long>();

  private final PriorityQueue<LoggedJob> skewBuffer;

  static final private Log LOG =
      LogFactory.getLog(DeskewedJobTraceReader.class);

  static private class JobComparator implements Comparator<LoggedJob> {
    @Override
    public int compare(LoggedJob j1, LoggedJob j2) {
      return (j1.getSubmitTime() < j2.getSubmitTime()) ? -1 : (j1
          .getSubmitTime() == j2.getSubmitTime()) ? 0 : 1;
    }
  }

  /**
   * Constructor.
   * 
   * @param reader
   *          the {@link JobTraceReader} that's being protected
   * @param skewBufferLength
   *          [the number of late jobs that can preced a later out-of-order
   *          earlier job
   * @throws IOException
   */
  public DeskewedJobTraceReader(JobTraceReader reader, int skewBufferLength,
      boolean abortOnUnfixableSkew) throws IOException {
    this.reader = reader;

    this.skewBufferLength = skewBufferLength;

    this.abortOnUnfixableSkew = abortOnUnfixableSkew;

    skewBuffer =
        new PriorityQueue<LoggedJob>(skewBufferLength + 1, new JobComparator());

    fillSkewBuffer();
  }

  public DeskewedJobTraceReader(JobTraceReader reader) throws IOException {
    this(reader, 0, true);
  }

  private LoggedJob rawNextJob() throws IOException {
    LoggedJob result = reader.getNext();

    if ((!abortOnUnfixableSkew || skewBufferLength > 0) && result != null) {
      long thisTime = result.getSubmitTime();

      if (submitTimesSoFar.contains(thisTime)) {
        Integer myCount = countedRepeatedSubmitTimesSoFar.get(thisTime);

        countedRepeatedSubmitTimesSoFar.put(thisTime, myCount == null ? 2
            : myCount + 1);
      } else {
        submitTimesSoFar.add(thisTime);
      }

      if (thisTime < skewMeasurementLatestSubmitTime) {
        Iterator<Long> endCursor = submitTimesSoFar.descendingIterator();

        int thisJobNeedsSkew = 0;

        Long keyNeedingSkew;

        while (endCursor.hasNext()
            && (keyNeedingSkew = endCursor.next()) > thisTime) {
          Integer keyNeedsSkewAmount =
              countedRepeatedSubmitTimesSoFar.get(keyNeedingSkew);

          thisJobNeedsSkew +=
              keyNeedsSkewAmount == null ? 1 : keyNeedsSkewAmount;
        }

        maxSkewBufferNeeded = Math.max(maxSkewBufferNeeded, thisJobNeedsSkew);
      }

      skewMeasurementLatestSubmitTime =
          Math.max(thisTime, skewMeasurementLatestSubmitTime);
    }

    return result;
  }

  static class OutOfOrderException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public OutOfOrderException(String text) {
      super(text);
    }
  }

  LoggedJob nextJob() throws IOException, OutOfOrderException {
    LoggedJob newJob = rawNextJob();

    if (newJob != null) {
      skewBuffer.add(newJob);
    }

    LoggedJob result = skewBuffer.poll();

    while (result != null && result.getSubmitTime() < returnedLatestSubmitTime) {
      LOG.error("The current job was submitted earlier than the previous one");
      LOG.error("Its jobID is " + result.getJobID());
      LOG.error("Its submit time is " + result.getSubmitTime()
          + ",but the previous one was " + returnedLatestSubmitTime);

      if (abortOnUnfixableSkew) {
        throw new OutOfOrderException("Job submit time is "
            + result.getSubmitTime() + ",but the previous one was "
            + returnedLatestSubmitTime);
      }

      result = rawNextJob();
    }

    if (result != null) {
      returnedLatestSubmitTime = result.getSubmitTime();
    }

    return result;
  }

  private void fillSkewBuffer() throws IOException {
    for (int i = 0; i < skewBufferLength; ++i) {
      LoggedJob newJob = rawNextJob();

      if (newJob == null) {
        return;
      }

      skewBuffer.add(newJob);
    }
  }

  int neededSkewBufferSize() {
    return maxSkewBufferNeeded;
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }

}
