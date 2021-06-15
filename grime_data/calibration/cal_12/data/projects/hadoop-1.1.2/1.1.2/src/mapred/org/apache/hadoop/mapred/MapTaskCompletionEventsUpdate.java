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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * A class that represents the communication between the tasktracker and child
 * tasks w.r.t the map task completion events. It also indicates whether the 
 * child task should reset its events index.
 */
public class MapTaskCompletionEventsUpdate implements Writable {
  TaskCompletionEvent[] events;
  boolean reset;

  public MapTaskCompletionEventsUpdate() { }

  public MapTaskCompletionEventsUpdate(TaskCompletionEvent[] events,
      boolean reset) {
    this.events = events;
    this.reset = reset;
  }

  public boolean shouldReset() {
    return reset;
  }

  public TaskCompletionEvent[] getMapTaskCompletionEvents() {
    return events;
  }

  public void write(DataOutput out) throws IOException {
    out.writeBoolean(reset);
    out.writeInt(events.length);
    for (TaskCompletionEvent event : events) {
      event.write(out);
    }
  }

  public void readFields(DataInput in) throws IOException {
    reset = in.readBoolean();
    events = new TaskCompletionEvent[in.readInt()];
    for (int i = 0; i < events.length; ++i) {
      events[i] = new TaskCompletionEvent();
      events[i].readFields(in);
    }
  }
}
