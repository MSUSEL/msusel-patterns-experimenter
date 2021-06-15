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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Counters;

abstract class HistoryEventEmitter {
  static final private Log LOG = LogFactory.getLog(HistoryEventEmitter.class);

  abstract List<SingleEventEmitter> nonFinalSEEs();

  abstract List<SingleEventEmitter> finalSEEs();

  protected HistoryEventEmitter() {
    // no code
  }

  enum PostEmitAction {
    NONE, REMOVE_HEE
  };

  final Pair<Queue<HistoryEvent>, PostEmitAction> emitterCore(ParsedLine line,
      String name) {
    Queue<HistoryEvent> results = new LinkedList<HistoryEvent>();
    PostEmitAction removeEmitter = PostEmitAction.NONE;
    for (SingleEventEmitter see : nonFinalSEEs()) {
      HistoryEvent event = see.maybeEmitEvent(line, name, this);
      if (event != null) {
        results.add(event);
      }
    }
    for (SingleEventEmitter see : finalSEEs()) {
      HistoryEvent event = see.maybeEmitEvent(line, name, this);
      if (event != null) {
        results.add(event);
        removeEmitter = PostEmitAction.REMOVE_HEE;
        break;
      }
    }
    return new Pair<Queue<HistoryEvent>, PostEmitAction>(results, removeEmitter);
  }

  protected static Counters maybeParseCounters(String counters) {
    try {
      return parseCounters(counters);
    } catch (ParseException e) {
      LOG.warn("The counter string, \"" + counters + "\" is badly formatted.");
      return null;
    }
  }

  protected static Counters parseCounters(String counters)
      throws ParseException {
    if (counters == null) {
      LOG.warn("HistoryEventEmitters: null counter detected:");
      return null;
    }

    counters = counters.replace("\\.", "\\\\.");
    counters = counters.replace("\\\\(", "\\(");
    counters = counters.replace("\\\\)", "\\)");
    counters = counters.replace("\\\\[", "\\[");
    counters = counters.replace("\\\\]", "\\]");

    org.apache.hadoop.mapred.Counters depForm =
        org.apache.hadoop.mapred.Counters.fromEscapedCompactString(counters);

    return new Counters(depForm);
  }
}
