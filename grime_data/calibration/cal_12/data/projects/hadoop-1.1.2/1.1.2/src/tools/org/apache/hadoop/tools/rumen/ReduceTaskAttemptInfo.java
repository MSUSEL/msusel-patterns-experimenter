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

import org.apache.hadoop.mapred.TaskStatus.State;

/**
 * {@link ReduceTaskAttemptInfo} represents the information with regard to a
 * reduce task attempt.
 */
public class ReduceTaskAttemptInfo extends TaskAttemptInfo {
  private long shuffleTime;
  private long mergeTime;
  private long reduceTime;

  public ReduceTaskAttemptInfo(State state, TaskInfo taskInfo, long shuffleTime,
      long mergeTime, long reduceTime) {
    super(state, taskInfo);
    this.shuffleTime = shuffleTime;
    this.mergeTime = mergeTime;
    this.reduceTime = reduceTime;
  }

  /**
   * Get the runtime for the <b>reduce</b> phase of the reduce task-attempt.
   * 
   * @return the runtime for the <b>reduce</b> phase of the reduce task-attempt
   */
  public long getReduceRuntime() {
    return reduceTime;
  }

  /**
   * Get the runtime for the <b>shuffle</b> phase of the reduce task-attempt.
   * 
   * @return the runtime for the <b>shuffle</b> phase of the reduce task-attempt
   */
  public long getShuffleRuntime() {
    return shuffleTime;
  }

  /**
   * Get the runtime for the <b>merge</b> phase of the reduce task-attempt
   * 
   * @return the runtime for the <b>merge</b> phase of the reduce task-attempt
   */
  public long getMergeRuntime() {
    return mergeTime;
  }

  @Override
  public long getRuntime() {
    return (getShuffleRuntime() + getMergeRuntime() + getReduceRuntime());
  }

}
