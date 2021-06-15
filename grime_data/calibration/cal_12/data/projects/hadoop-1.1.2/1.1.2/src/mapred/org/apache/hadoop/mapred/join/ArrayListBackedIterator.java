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
package org.apache.hadoop.mapred.join;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

/**
 * This class provides an implementation of ResetableIterator. The
 * implementation uses an {@link java.util.ArrayList} to store elements
 * added to it, replaying them as requested.
 * Prefer {@link StreamBackedIterator}.
 */
public class ArrayListBackedIterator<X extends Writable>
    implements ResetableIterator<X> {

  private Iterator<X> iter;
  private ArrayList<X> data;
  private X hold = null;

  public ArrayListBackedIterator() {
    this(new ArrayList<X>());
  }

  public ArrayListBackedIterator(ArrayList<X> data) {
    this.data = data;
    this.iter = this.data.iterator();
  }

  public boolean hasNext() {
    return iter.hasNext();
  }

  public boolean next(X val) throws IOException {
    if (iter.hasNext()) {
      WritableUtils.cloneInto(val, iter.next());
      if (null == hold) {
        hold = WritableUtils.clone(val, null);
      } else {
        WritableUtils.cloneInto(hold, val);
      }
      return true;
    }
    return false;
  }

  public boolean replay(X val) throws IOException {
    WritableUtils.cloneInto(val, hold);
    return true;
  }

  public void reset() {
    iter = data.iterator();
  }

  public void add(X item) throws IOException {
    data.add(WritableUtils.clone(item, null));
  }

  public void close() throws IOException {
    iter = null;
    data = null;
  }

  public void clear() {
    data.clear();
    reset();
  }

}
