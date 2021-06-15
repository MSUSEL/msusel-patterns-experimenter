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
package org.apache.hadoop.ant.condition;

import org.apache.tools.ant.taskdefs.condition.Condition;

/**
 * This wrapper around {@link org.apache.hadoop.ant.DfsTask} implements the
 * Ant &gt;1.5
 * {@link org.apache.tools.ant.taskdefs.condition.Condition Condition}
 * interface for HDFS tests. So one can test conditions like this:
 * {@code
 *   <condition property="precond">
 *     <and>
 *       <hadoop:exists file="fileA" />
 *       <hadoop:exists file="fileB" />
 *       <hadoop:sizezero file="fileB" />
 *     </and>
 *   </condition>
 * }
 * This will define the property precond if fileA exists and fileB has zero
 * length.
 */
public abstract class DfsBaseConditional extends org.apache.hadoop.ant.DfsTask
                       implements Condition {

  protected boolean result;
  String file;

  private void initArgs() {
    setCmd("test");
    setArgs("-"  +  getFlag() + "," + file);
  }

  public void setFile(String file) {
    this.file = file;
  }

  protected abstract char getFlag();

  protected int postCmd(int exit_code) {
    exit_code = super.postCmd(exit_code);
    result = exit_code == 0;
    return exit_code;
  }

  public boolean eval() {
    initArgs();
    execute();
    return result;
  }
}
