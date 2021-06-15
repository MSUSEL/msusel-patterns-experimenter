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
package org.apache.hadoop.test.system;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * Class to represent a control action which can be performed on Daemon.<br/>
 * 
 */

public abstract class ControlAction<T extends Writable> implements Writable {

  private T target;

  /**
   * Default constructor of the Control Action, sets the Action type to zero. <br/>
   */
  public ControlAction() {
  }

  /**
   * Constructor which sets the type of the Control action to a specific type. <br/>
   * 
   * @param type
   *          of the control action.
   */
  public ControlAction(T target) {
    this.target = target;
  }

  /**
   * Gets the id of the control action <br/>
   * 
   * @return target of action
   */
  public T getTarget() {
    return target;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    target.readFields(in);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    target.write(out);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ControlAction) {
      ControlAction<T> other = (ControlAction<T>) obj;
      return (this.target.equals(other.getTarget()));
    } else {
      return false;
    }
  }
  
  
  @Override
  public String toString() {
    return "Action Target : " + this.target;
  }
}
