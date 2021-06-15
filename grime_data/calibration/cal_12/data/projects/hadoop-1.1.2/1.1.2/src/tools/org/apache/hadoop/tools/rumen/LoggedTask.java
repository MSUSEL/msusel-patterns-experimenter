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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.jackson.annotate.JsonAnySetter;

/**
 * A {@link LoggedTask} represents a [hadoop] task that is part of a hadoop job.
 * It knows about the [pssibly empty] sequence of attempts, its I/O footprint,
 * and its runtime.
 * 
 * All of the public methods are simply accessors for the instance variables we
 * want to write out in the JSON files.
 * 
 */
public class LoggedTask implements DeepCompare {
  long inputBytes = -1L;
  long inputRecords = -1L;
  long outputBytes = -1L;
  long outputRecords = -1L;
  String taskID;
  long startTime = -1L;
  long finishTime = -1L;
  Pre21JobHistoryConstants.Values taskType;
  Pre21JobHistoryConstants.Values taskStatus;
  List<LoggedTaskAttempt> attempts = new ArrayList<LoggedTaskAttempt>();
  List<LoggedLocation> preferredLocations = Collections.emptyList();

  static private Set<String> alreadySeenAnySetterAttributes =
      new TreeSet<String>();

  @SuppressWarnings("unused")
  // for input parameter ignored.
  @JsonAnySetter
  public void setUnknownAttribute(String attributeName, Object ignored) {
    if (!alreadySeenAnySetterAttributes.contains(attributeName)) {
      alreadySeenAnySetterAttributes.add(attributeName);
      System.err.println("In LoggedJob, we saw the unknown attribute "
          + attributeName + ".");
    }
  }

  LoggedTask() {
    super();
  }

  void adjustTimes(long adjustment) {
    startTime += adjustment;
    finishTime += adjustment;

    for (LoggedTaskAttempt attempt : attempts) {
      attempt.adjustTimes(adjustment);
    }
  }

  public long getInputBytes() {
    return inputBytes;
  }

  void setInputBytes(long inputBytes) {
    this.inputBytes = inputBytes;
  }

  public long getInputRecords() {
    return inputRecords;
  }

  void setInputRecords(long inputRecords) {
    this.inputRecords = inputRecords;
  }

  public long getOutputBytes() {
    return outputBytes;
  }

  void setOutputBytes(long outputBytes) {
    this.outputBytes = outputBytes;
  }

  public long getOutputRecords() {
    return outputRecords;
  }

  void setOutputRecords(long outputRecords) {
    this.outputRecords = outputRecords;
  }

  public String getTaskID() {
    return taskID;
  }

  void setTaskID(String taskID) {
    this.taskID = taskID;
  }

  public long getStartTime() {
    return startTime;
  }

  void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getFinishTime() {
    return finishTime;
  }

  void setFinishTime(long finishTime) {
    this.finishTime = finishTime;
  }

  public List<LoggedTaskAttempt> getAttempts() {
    return attempts;
  }

  void setAttempts(List<LoggedTaskAttempt> attempts) {
    if (attempts == null) {
      this.attempts = new ArrayList<LoggedTaskAttempt>();
    } else {
      this.attempts = attempts;
    }
  }

  public List<LoggedLocation> getPreferredLocations() {
    return preferredLocations;
  }

  void setPreferredLocations(List<LoggedLocation> preferredLocations) {
    if (preferredLocations == null || preferredLocations.isEmpty()) {
      this.preferredLocations = Collections.emptyList();
    } else {
      this.preferredLocations = preferredLocations;
    }
  }

  public Pre21JobHistoryConstants.Values getTaskStatus() {
    return taskStatus;
  }

  void setTaskStatus(Pre21JobHistoryConstants.Values taskStatus) {
    this.taskStatus = taskStatus;
  }

  public Pre21JobHistoryConstants.Values getTaskType() {
    return taskType;
  }

  void setTaskType(Pre21JobHistoryConstants.Values taskType) {
    this.taskType = taskType;
  }

  private void incorporateMapCounters(JhCounters counters) {
    incorporateCounter(new SetField(this) {
      @Override
      void set(long val) {
        task.inputBytes = val;
      }
    }, counters, "HDFS_BYTES_READ");
    incorporateCounter(new SetField(this) {
      @Override
      void set(long val) {
        task.outputBytes = val;
      }
    }, counters, "FILE_BYTES_WRITTEN");
    incorporateCounter(new SetField(this) {
      @Override
      void set(long val) {
        task.inputRecords = val;
      }
    }, counters, "MAP_INPUT_RECORDS");
    incorporateCounter(new SetField(this) {
      @Override
      void set(long val) {
        task.outputRecords = val;
      }
    }, counters, "MAP_OUTPUT_RECORDS");
  }

  private void incorporateReduceCounters(JhCounters counters) {
    incorporateCounter(new SetField(this) {
      @Override
      void set(long val) {
        task.inputBytes = val;
      }
    }, counters, "REDUCE_SHUFFLE_BYTES");
    incorporateCounter(new SetField(this) {
      @Override
      void set(long val) {
        task.outputBytes = val;
      }
    }, counters, "HDFS_BYTES_WRITTEN");
    incorporateCounter(new SetField(this) {
      @Override
      void set(long val) {
        task.inputRecords = val;
      }
    }, counters, "REDUCE_INPUT_RECORDS");
    incorporateCounter(new SetField(this) {
      @Override
      void set(long val) {
        task.outputRecords = val;
      }
    }, counters, "REDUCE_OUTPUT_RECORDS");
  }

  // incorporate event counters
  // LoggedTask MUST KNOW ITS TYPE BEFORE THIS CALL
  public void incorporateCounters(JhCounters counters) {
    switch (taskType) {
    case MAP:
      incorporateMapCounters(counters);
      return;
    case REDUCE:
      incorporateReduceCounters(counters);
      return;
      // NOT exhaustive
    }
  }

  private static String canonicalizeCounterName(String nonCanonicalName) {
    String result = nonCanonicalName.toLowerCase();

    result = result.replace(' ', '|');
    result = result.replace('-', '|');
    result = result.replace('_', '|');
    result = result.replace('.', '|');

    return result;
  }

  private abstract class SetField {
    LoggedTask task;

    SetField(LoggedTask task) {
      this.task = task;
    }

    abstract void set(long value);
  }

  private static void incorporateCounter(SetField thunk, JhCounters counters,
      String counterName) {
    counterName = canonicalizeCounterName(counterName);

    for (JhCounterGroup group : counters.groups) {
      for (JhCounter counter : group.counts) {
        if (counterName
            .equals(canonicalizeCounterName(counter.name.toString()))) {
          thunk.set(counter.value);
          return;
        }
      }
    }
  }

  private void compare1(long c1, long c2, TreePath loc, String eltname)
      throws DeepInequalityException {
    if (c1 != c2) {
      throw new DeepInequalityException(eltname + " miscompared", new TreePath(
          loc, eltname));
    }
  }

  private void compare1(String c1, String c2, TreePath loc, String eltname)
      throws DeepInequalityException {
    if (c1 == null && c2 == null) {
      return;
    }
    if (c1 == null || c2 == null || !c1.equals(c2)) {
      throw new DeepInequalityException(eltname + " miscompared", new TreePath(
          loc, eltname));
    }
  }

  private void compare1(Pre21JobHistoryConstants.Values c1,
      Pre21JobHistoryConstants.Values c2, TreePath loc, String eltname)
      throws DeepInequalityException {
    if (c1 == null && c2 == null) {
      return;
    }
    if (c1 == null || c2 == null || !c1.equals(c2)) {
      throw new DeepInequalityException(eltname + " miscompared", new TreePath(
          loc, eltname));
    }
  }

  private void compareLoggedLocations(List<LoggedLocation> c1,
      List<LoggedLocation> c2, TreePath loc, String eltname)
      throws DeepInequalityException {
    if (c1 == null && c2 == null) {
      return;
    }

    if (c1 == null || c2 == null || c1.size() != c2.size()) {
      throw new DeepInequalityException(eltname + " miscompared", new TreePath(
          loc, eltname));
    }

    for (int i = 0; i < c1.size(); ++i) {
      c1.get(i).deepCompare(c2.get(i), new TreePath(loc, eltname, i));
    }
  }

  private void compareLoggedTaskAttempts(List<LoggedTaskAttempt> c1,
      List<LoggedTaskAttempt> c2, TreePath loc, String eltname)
      throws DeepInequalityException {
    if (c1 == null && c2 == null) {
      return;
    }

    if (c1 == null || c2 == null || c1.size() != c2.size()) {
      throw new DeepInequalityException(eltname + " miscompared", new TreePath(
          loc, eltname));
    }

    for (int i = 0; i < c1.size(); ++i) {
      c1.get(i).deepCompare(c2.get(i), new TreePath(loc, eltname, i));
    }
  }

  public void deepCompare(DeepCompare comparand, TreePath loc)
      throws DeepInequalityException {
    if (!(comparand instanceof LoggedTask)) {
      throw new DeepInequalityException("comparand has wrong type", loc);
    }

    LoggedTask other = (LoggedTask) comparand;

    compare1(inputBytes, other.inputBytes, loc, "inputBytes");
    compare1(inputRecords, other.inputRecords, loc, "inputRecords");
    compare1(outputBytes, other.outputBytes, loc, "outputBytes");
    compare1(outputRecords, other.outputRecords, loc, "outputRecords");

    compare1(taskID, other.taskID, loc, "taskID");

    compare1(startTime, other.startTime, loc, "startTime");
    compare1(finishTime, other.finishTime, loc, "finishTime");

    compare1(taskType, other.taskType, loc, "taskType");
    compare1(taskStatus, other.taskStatus, loc, "taskStatus");

    compareLoggedTaskAttempts(attempts, other.attempts, loc, "attempts");
    compareLoggedLocations(preferredLocations, other.preferredLocations, loc,
        "preferredLocations");
  }
}
