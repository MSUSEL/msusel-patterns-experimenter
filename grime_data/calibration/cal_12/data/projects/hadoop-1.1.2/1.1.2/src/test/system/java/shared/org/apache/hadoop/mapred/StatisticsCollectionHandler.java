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

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/*
 * Object which stores all the tasktracker info
 */
public class StatisticsCollectionHandler implements Writable{

  private int sinceStartTotalTasks = 0;
  private int sinceStartSucceededTasks = 0;
  private int lastHourTotalTasks = 0;
  private int lastHourSucceededTasks = 0;
  private int lastDayTotalTasks = 0;
  private int lastDaySucceededTasks = 0;

  public int getSinceStartTotalTasks() {
    return sinceStartTotalTasks;
  }

  public int getSinceStartSucceededTasks() {
    return sinceStartSucceededTasks;
  }

  public int getLastHourTotalTasks() {
    return lastHourTotalTasks;
  }

  public int getLastHourSucceededTasks() {
    return lastHourSucceededTasks;
  }

  public int getLastDayTotalTasks() {
    return lastDayTotalTasks;
  }

  public int getLastDaySucceededTasks() {
    return lastDaySucceededTasks;
  }

  public void setSinceStartTotalTasks(int value) {
    sinceStartTotalTasks = value;
  }

  public void setSinceStartSucceededTasks(int value) {
    sinceStartSucceededTasks = value;
  }

  public void setLastHourTotalTasks(int value) {
    lastHourTotalTasks = value;
  }

  public void setLastHourSucceededTasks(int value) {
    lastHourSucceededTasks = value;
  }

  public void setLastDayTotalTasks(int value) {
    lastDayTotalTasks = value;
  }

  public void setLastDaySucceededTasks(int value) {
    lastDaySucceededTasks = value;
  }
  
  @Override
  public void readFields(DataInput in) throws IOException {
    sinceStartTotalTasks = WritableUtils.readVInt(in);
    sinceStartSucceededTasks = WritableUtils.readVInt(in);
    lastHourTotalTasks = WritableUtils.readVInt(in);
    lastHourSucceededTasks = WritableUtils.readVInt(in);
    lastDayTotalTasks = WritableUtils.readVInt(in);
    lastDaySucceededTasks = WritableUtils.readVInt(in);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    WritableUtils.writeVInt(out, sinceStartTotalTasks);
    WritableUtils.writeVInt(out, sinceStartSucceededTasks);
    WritableUtils.writeVInt(out, lastHourTotalTasks);
    WritableUtils.writeVInt(out, lastHourSucceededTasks);
    WritableUtils.writeVInt(out, lastDayTotalTasks);
    WritableUtils.writeVInt(out, lastDaySucceededTasks);
  }
  
}

