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

import org.apache.hadoop.io.OutputBuffer;
import org.apache.hadoop.io.SequenceFile.Sorter.RawKeyValueIterator;
import org.apache.hadoop.util.Progressable;

/** This class provides a generic sort interface that should be implemented
 * by specific sort algorithms. The use case is the following:
 * A user class writes key/value records to a buffer, and finally wants to
 * sort the buffer. This interface defines methods by which the user class
 * can update the interface implementation with the offsets of the records
 * and the lengths of the keys/values. The user class gives a reference to
 * the buffer when the latter wishes to sort the records written to the buffer
 * so far. Typically, the user class decides the point at which sort should
 * happen based on the memory consumed so far by the buffer and the data
 * structures maintained by an implementation of this interface. That is why
 * a method is provided to get the memory consumed so far by the datastructures
 * in the interface implementation.  
 */
interface BufferSorter extends JobConfigurable {
  
  /** Pass the Progressable object so that sort can call progress while it is sorting
   * @param reporter the Progressable object reference
   */
  public void setProgressable(Progressable reporter);
    
  /** When a key/value is added at a particular offset in the key/value buffer, 
   * this method is invoked by the user class so that the impl of this sort 
   * interface can update its datastructures. 
   * @param recordOffset the offset of the key in the buffer
   * @param keyLength the length of the key
   * @param valLength the length of the val in the buffer
   */
  public void addKeyValue(int recordoffset, int keyLength, int valLength);
  
  /** The user class invokes this method to set the buffer that the specific 
   * sort algorithm should "indirectly" sort (generally, sort algorithm impl 
   * should access this buffer via comparators and sort offset-indices to the
   * buffer).
   * @param buffer the map output buffer
   */
  public void setInputBuffer(OutputBuffer buffer);
  
  /** The framework invokes this method to get the memory consumed so far
   * by an implementation of this interface.
   * @return memoryUsed in bytes 
   */
  public long getMemoryUtilized();
  
  /** Framework decides when to actually sort
   */
  public RawKeyValueIterator sort();
  
  /** Framework invokes this to signal the sorter to cleanup
   */
  public void close();
}
