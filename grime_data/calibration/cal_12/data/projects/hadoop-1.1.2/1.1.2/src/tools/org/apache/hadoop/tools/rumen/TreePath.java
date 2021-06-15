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

/**
 * This describes a path from a node to the root. We use it when we compare two
 * trees during rumen unit tests. If the trees are not identical, this chain
 * will be converted to a string which describes the path from the root to the
 * fields that did not compare.
 * 
 */
public class TreePath {
  final TreePath parent;

  final String fieldName;

  final int index;

  public TreePath(TreePath parent, String fieldName) {
    super();

    this.parent = parent;
    this.fieldName = fieldName;
    this.index = -1;
  }

  public TreePath(TreePath parent, String fieldName, int index) {
    super();

    this.parent = parent;
    this.fieldName = fieldName;
    this.index = index;
  }

  @Override
  public String toString() {
    String mySegment = fieldName + (index == -1 ? "" : ("[" + index + "]"));

    return ((parent == null) ? "" : parent.toString() + "-->") + mySegment;
  }
}
