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

import org.apache.hadoop.mapred.JobPriority;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.JobACL;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.security.authorize.AccessControlList;

public class Job20LineHistoryEventEmitter extends HistoryEventEmitter {

  static List<SingleEventEmitter> nonFinals =
      new LinkedList<SingleEventEmitter>();
  static List<SingleEventEmitter> finals = new LinkedList<SingleEventEmitter>();

  Long originalSubmitTime = null;

  static {
    nonFinals.add(new JobSubmittedEventEmitter());
    nonFinals.add(new JobPriorityChangeEventEmitter());
    nonFinals.add(new JobStatusChangedEventEmitter());
    nonFinals.add(new JobInitedEventEmitter());
    nonFinals.add(new JobInfoChangeEventEmitter());

    finals.add(new JobUnsuccessfulCompletionEventEmitter());
    finals.add(new JobFinishedEventEmitter());
  }

  Job20LineHistoryEventEmitter() {
    super();
  }

  static private class JobSubmittedEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String jobIDName,
        HistoryEventEmitter thatg) {
      JobID jobID = JobID.forName(jobIDName);

      if (jobIDName == null) {
        return null;
      }

      String submitTime = line.get("SUBMIT_TIME");
      String jobConf = line.get("JOBCONF");
      String user = line.get("USER");
      String jobName = line.get("JOBNAME");
      String queueName = line.get("JOB_QUEUE");

      if (submitTime != null) {
        Job20LineHistoryEventEmitter that =
            (Job20LineHistoryEventEmitter) thatg;

        that.originalSubmitTime = Long.parseLong(submitTime);

        Map<JobACL, AccessControlList> jobACLs =
          new HashMap<JobACL, AccessControlList>();
        return new JobSubmittedEvent(jobID, jobName, user == null ? "nulluser"
            : user, that.originalSubmitTime, jobConf, jobACLs, queueName);
      }

      return null;
    }
  }

  static private class JobPriorityChangeEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String jobIDName,
        HistoryEventEmitter thatg) {
      JobID jobID = JobID.forName(jobIDName);

      if (jobIDName == null) {
        return null;
      }

      String priority = line.get("JOB_PRIORITY");

      if (priority != null) {
        return new JobPriorityChangeEvent(jobID, JobPriority.valueOf(priority));
      }

      return null;
    }
  }

  static private class JobInitedEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String jobIDName,
        HistoryEventEmitter thatg) {
      if (jobIDName == null) {
        return null;
      }

      JobID jobID = JobID.forName(jobIDName);

      String launchTime = line.get("LAUNCH_TIME");
      String status = line.get("JOB_STATUS");
      String totalMaps = line.get("TOTAL_MAPS");
      String totalReduces = line.get("TOTAL_REDUCES");

      if (launchTime != null && totalMaps != null && totalReduces != null) {
        return new JobInitedEvent(jobID, Long.parseLong(launchTime), Integer
            .parseInt(totalMaps), Integer.parseInt(totalReduces), status);
      }

      return null;
    }
  }

  static private class JobStatusChangedEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String jobIDName,
        HistoryEventEmitter thatg) {
      if (jobIDName == null) {
        return null;
      }

      JobID jobID = JobID.forName(jobIDName);

      String status = line.get("JOB_STATUS");

      if (status != null) {
        return new JobStatusChangedEvent(jobID, status);
      }

      return null;
    }
  }

  static private class JobInfoChangeEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String jobIDName,
        HistoryEventEmitter thatg) {
      if (jobIDName == null) {
        return null;
      }

      JobID jobID = JobID.forName(jobIDName);

      String launchTime = line.get("LAUNCH_TIME");

      if (launchTime != null) {
        Job20LineHistoryEventEmitter that =
            (Job20LineHistoryEventEmitter) thatg;
        return new JobInfoChangeEvent(jobID, that.originalSubmitTime, Long
            .parseLong(launchTime));
      }

      return null;
    }
  }

  static private class JobUnsuccessfulCompletionEventEmitter extends
      SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String jobIDName,
        HistoryEventEmitter thatg) {
      if (jobIDName == null) {
        return null;
      }

      JobID jobID = JobID.forName(jobIDName);

      String finishTime = line.get("FINISH_TIME");

      String status = line.get("JOB_STATUS");

      String finishedMaps = line.get("FINISHED_MAPS");
      String finishedReduces = line.get("FINISHED_REDUCES");

      if (status != null && !status.equalsIgnoreCase("success")
          && finishTime != null && finishedMaps != null
          && finishedReduces != null) {
        return new JobUnsuccessfulCompletionEvent(jobID, Long
            .parseLong(finishTime), Integer.parseInt(finishedMaps), Integer
            .parseInt(finishedReduces), status);
      }

      return null;
    }
  }

  static private class JobFinishedEventEmitter extends SingleEventEmitter {
    HistoryEvent maybeEmitEvent(ParsedLine line, String jobIDName,
        HistoryEventEmitter thatg) {
      if (jobIDName == null) {
        return null;
      }

      JobID jobID = JobID.forName(jobIDName);

      String finishTime = line.get("FINISH_TIME");

      String status = line.get("JOB_STATUS");

      String finishedMaps = line.get("FINISHED_MAPS");
      String finishedReduces = line.get("FINISHED_REDUCES");

      String failedMaps = line.get("FAILED_MAPS");
      String failedReduces = line.get("FAILED_REDUCES");

      String mapCounters = line.get("MAP_COUNTERS");
      String reduceCounters = line.get("REDUCE_COUNTERS");
      String counters = line.get("COUNTERS");

      if (status != null && status.equalsIgnoreCase("success")
          && finishTime != null && finishedMaps != null
          && finishedReduces != null) {
        return new JobFinishedEvent(jobID, Long.parseLong(finishTime), Integer
            .parseInt(finishedMaps), Integer.parseInt(finishedReduces), Integer
            .parseInt(failedMaps), Integer.parseInt(failedReduces),
            maybeParseCounters(mapCounters), maybeParseCounters(reduceCounters),
            maybeParseCounters(counters));
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
