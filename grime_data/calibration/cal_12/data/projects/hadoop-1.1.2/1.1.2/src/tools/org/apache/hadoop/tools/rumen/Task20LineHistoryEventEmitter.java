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

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.TaskID;
import org.apache.hadoop.mapreduce.TaskType;

public class Task20LineHistoryEventEmitter extends HistoryEventEmitter {

  static List<SingleEventEmitter> nonFinals =
      new LinkedList<SingleEventEmitter>();
  static List<SingleEventEmitter> finals = new LinkedList<SingleEventEmitter>();

  Long originalStartTime = null;
  TaskType originalTaskType = null;

  static {
    nonFinals.add(new TaskStartedEventEmitter());
    nonFinals.add(new TaskUpdatedEventEmitter());

    finals.add(new TaskFinishedEventEmitter());
    finals.add(new TaskFailedEventEmitter());
  }

  protected Task20LineHistoryEventEmitter() {
    super();
  }

  static private class TaskStartedEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String taskIDName,
        HistoryEventEmitter thatg) {
      if (taskIDName == null) {
        return null;
      }

      TaskID taskID = TaskID.forName(taskIDName);

      String taskType = line.get("TASK_TYPE");
      String startTime = line.get("START_TIME");
      String splits = line.get("SPLITS");

      if (startTime != null && taskType != null) {
        Task20LineHistoryEventEmitter that =
            (Task20LineHistoryEventEmitter) thatg;

        that.originalStartTime = Long.parseLong(startTime);
        that.originalTaskType =
            Version20LogInterfaceUtils.get20TaskType(taskType);

        return new TaskStartedEvent(taskID, that.originalStartTime,
            that.originalTaskType, splits);
      }

      return null;
    }
  }

  static private class TaskUpdatedEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String taskIDName,
        HistoryEventEmitter thatg) {
      if (taskIDName == null) {
        return null;
      }

      TaskID taskID = TaskID.forName(taskIDName);

      String finishTime = line.get("FINISH_TIME");

      if (finishTime != null) {
        return new TaskUpdatedEvent(taskID, Long.parseLong(finishTime));
      }

      return null;
    }
  }

  static private class TaskFinishedEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String taskIDName,
        HistoryEventEmitter thatg) {
      if (taskIDName == null) {
        return null;
      }

      TaskID taskID = TaskID.forName(taskIDName);

      String status = line.get("TASK_STATUS");
      String finishTime = line.get("FINISH_TIME");

      String error = line.get("ERROR");

      String counters = line.get("COUNTERS");

      if (finishTime != null && error == null
          && (status != null && status.equalsIgnoreCase("success"))) {
        Counters eventCounters = maybeParseCounters(counters);

        Task20LineHistoryEventEmitter that =
            (Task20LineHistoryEventEmitter) thatg;

        if (that.originalTaskType == null) {
          return null;
        }

        return new TaskFinishedEvent(taskID, Long.parseLong(finishTime),
            that.originalTaskType, status, eventCounters);
      }

      return null;
    }
  }

  static private class TaskFailedEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String taskIDName,
        HistoryEventEmitter thatg) {
      if (taskIDName == null) {
        return null;
      }

      TaskID taskID = TaskID.forName(taskIDName);

      String status = line.get("TASK_STATUS");
      String finishTime = line.get("FINISH_TIME");

      String taskType = line.get("TASK_TYPE");

      String error = line.get("ERROR");

      if (finishTime != null
          && (error != null || (status != null && !status
              .equalsIgnoreCase("success")))) {
        Task20LineHistoryEventEmitter that =
            (Task20LineHistoryEventEmitter) thatg;

        TaskType originalTaskType =
            that.originalTaskType == null ? Version20LogInterfaceUtils
                .get20TaskType(taskType) : that.originalTaskType;

        return new TaskFailedEvent(taskID, Long.parseLong(finishTime),
            originalTaskType, error, status, null);
      }

      return null;
    }
  }

  @Override
  List<SingleEventEmitter> finalSEEs() {
    return finals;
  }

  @Override
  List<SingleEventEmitter> nonFinalSEEs() {
    return nonFinals;
  }
}
