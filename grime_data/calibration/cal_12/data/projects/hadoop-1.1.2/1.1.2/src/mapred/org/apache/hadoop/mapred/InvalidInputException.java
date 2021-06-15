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

import java.io.IOException;
import java.util.List;
import java.util.Iterator;

/**
 * This class wraps a list of problems with the input, so that the user
 * can get a list of problems together instead of finding and fixing them one 
 * by one.
 */
public class InvalidInputException extends IOException {
 
  private static final long serialVersionUID = 1L;
  private List<IOException> problems;
  
  /**
   * Create the exception with the given list.
   * @param probs the list of problems to report. this list is not copied.
   */
  public InvalidInputException(List<IOException> probs) {
    problems = probs;
  }
  
  /**
   * Get the complete list of the problems reported.
   * @return the list of problems, which must not be modified
   */
  public List<IOException> getProblems() {
    return problems;
  }
  
  /**
   * Get a summary message of the problems found.
   * @return the concatenated messages from all of the problems.
   */
  public String getMessage() {
    StringBuffer result = new StringBuffer();
    Iterator<IOException> itr = problems.iterator();
    while(itr.hasNext()) {
      result.append(itr.next().getMessage());
      if (itr.hasNext()) {
        result.append("\n");
      }
    }
    return result.toString();
  }
}
