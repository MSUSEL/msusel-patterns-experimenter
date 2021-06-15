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
package com.finalist.jag.template.parser;


/**
 * Class CharQueue
 *
 *
 * @author
 * @version %I%, %G%
 */
public class CharQueue {

   /** Field buffer           */
   protected char[] buffer;

   // buffer.length-1 for quick modulous

   /** Field sizeLessOne           */
   protected int sizeLessOne;

   // physical index of front token

   /** Field offset           */
   protected int offset;

   // number of characters in the queue

   /** Field nbrEntries           */
   protected int nbrEntries;


   /**
    * Constructor CharQueue
    *
    *
    * @param minSize
    *
    */
   public CharQueue(int minSize) {
      // Find first power of 2 >= to requested size
      int size;
      for (size = 2; size < minSize; size *= 2) ;
      init(size);
   }


   /**
    * Add token to end of the queue
    * @param tok The token to add
    */
   public final void append(char tok) {
      if (nbrEntries == buffer.length) {
         expand();
      }
      buffer[(offset + nbrEntries) & sizeLessOne] = tok;
      nbrEntries++;
   }


   /**
    * Fetch a token from the queue by index
    * @param idx The index of the token to fetch, where zero is the token at the front of the queue
    *
    * @return
    */
   public final char elementAt(int idx) {
      return buffer[(offset + idx) & sizeLessOne];
   }


   /** Expand the token buffer by doubling its capacity */
   private final void expand() {
      char[] newBuffer = new char[buffer.length * 2];

      // Copy the contents to the new buffer
      // Note that this will store the first logical item in the
      // first physical array element.
      for (int i = 0; i < buffer.length; i++) {
         newBuffer[i] = elementAt(i);
      }

      // Re-initialize with new contents, keep old nbrEntries
      buffer = newBuffer;
      sizeLessOne = buffer.length - 1;
      offset = 0;
   }


   /**
    * Initialize the queue.
    * @param size The initial size of the queue
    */
   private final void init(int size) {
      buffer = new char[size];
      sizeLessOne = size - 1;
      offset = 0;
      nbrEntries = 0;
   }


   /** Remove char from front of queue */
   public final void removeFirst() {
      offset = (offset + 1) & sizeLessOne;
      nbrEntries--;
   }
}