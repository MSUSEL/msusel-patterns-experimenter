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

import org.apache.hadoop.mapred.Counters.Counter;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.util.Progressable;

/** 
 * A facility for Map-Reduce applications to report progress and update 
 * counters, status information etc.
 * 
 * <p>{@link Mapper} and {@link Reducer} can use the <code>Reporter</code>
 * provided to report progress or just indicate that they are alive. In 
 * scenarios where the application takes an insignificant amount of time to 
 * process individual key/value pairs, this is crucial since the framework 
 * might assume that the task has timed-out and kill that task.
 *
 * <p>Applications can also update {@link Counters} via the provided 
 * <code>Reporter</code> .</p>
 * 
 * @see Progressable
 * @see Counters
 */
public interface Reporter extends Progressable {
  
  /**
   * A constant of Reporter type that does nothing.
   */
  public static final Reporter NULL = new Reporter() {
      public void setStatus(String s) {
      }
      public void progress() {
      }
      public Counter getCounter(Enum<?> name) {
        return null;
      }
      public Counter getCounter(String group, String name) {
        return null;
      }
      public void incrCounter(Enum<?> key, long amount) {
      }
      public void incrCounter(String group, String counter, long amount) {
      }
      public InputSplit getInputSplit() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("NULL reporter has no input");
      }
      @Override
      public float getProgress() {
        return 0;
      }
    };

  /**
   * Set the status description for the task.
   * 
   * @param status brief description of the current status.
   */
  public abstract void setStatus(String status);
  
  /**
   * Get the {@link Counter} of the given group with the given name.
   * 
   * @param name counter name
   * @return the <code>Counter</code> of the given group/name.
   */
  public abstract Counter getCounter(Enum<?> name);

  /**
   * Get the {@link Counter} of the given group with the given name.
   * 
   * @param group counter group
   * @param name counter name
   * @return the <code>Counter</code> of the given group/name.
   */
  public abstract Counter getCounter(String group, String name);
  
  /**
   * Increments the counter identified by the key, which can be of
   * any {@link Enum} type, by the specified amount.
   * 
   * @param key key to identify the counter to be incremented. The key can be
   *            be any <code>Enum</code>. 
   * @param amount A non-negative amount by which the counter is to 
   *               be incremented.
   */
  public abstract void incrCounter(Enum<?> key, long amount);
  
  /**
   * Increments the counter identified by the group and counter name
   * by the specified amount.
   * 
   * @param group name to identify the group of the counter to be incremented.
   * @param counter name to identify the counter within the group.
   * @param amount A non-negative amount by which the counter is to 
   *               be incremented.
   */
  public abstract void incrCounter(String group, String counter, long amount);
  
  /**
   * Get the {@link InputSplit} object for a map.
   * 
   * @return the <code>InputSplit</code> that the map is reading from.
   * @throws UnsupportedOperationException if called outside a mapper
   */
  public abstract InputSplit getInputSplit() 
    throws UnsupportedOperationException;
  
  /**
   * Get the progress of the task. Progress is represented as a number between
   * 0 and 1 (inclusive).
   */
  public float getProgress();
}
