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
import org.apache.hadoop.mapreduce.TaskAttemptID;

public class MapAttempt20LineHistoryEventEmitter extends
    TaskAttempt20LineEventEmitter {

  static List<SingleEventEmitter> nonFinals =
      new LinkedList<SingleEventEmitter>();
  static List<SingleEventEmitter> finals = new LinkedList<SingleEventEmitter>();

  static {
    nonFinals.addAll(taskEventNonFinalSEEs);

    finals.add(new MapAttemptFinishedEventEmitter());
  }

  protected MapAttempt20LineHistoryEventEmitter() {
    super();
  }

  static private class MapAttemptFinishedEventEmitter extends
      SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String taskAttemptIDName,
        HistoryEventEmitter thatg) {
      if (taskAttemptIDName == null) {
        return null;
      }

      TaskAttemptID taskAttemptID = TaskAttemptID.forName(taskAttemptIDName);

      String finishTime = line.get("FINISH_TIME");
      String status = line.get("TASK_STATUS");

      if (finishTime != null && status != null
          && status.equalsIgnoreCase("success")) {
        String hostName = line.get("HOSTNAME");
        String counters = line.get("COUNTERS");
        String state = line.get("STATE_STRING");

        MapAttempt20LineHistoryEventEmitter that =
            (MapAttempt20LineHistoryEventEmitter) thatg;

        if (finishTime != null && "success".equalsIgnoreCase(status)) {
          return new MapAttemptFinishedEvent(taskAttemptID,
              that.originalTaskType, status, Long.parseLong(finishTime), Long
                  .parseLong(finishTime), hostName, state,
              maybeParseCounters(counters));
        }
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
