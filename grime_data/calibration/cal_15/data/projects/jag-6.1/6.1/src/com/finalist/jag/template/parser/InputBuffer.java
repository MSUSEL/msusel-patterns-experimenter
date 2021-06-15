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
 * Class InputBuffer
 *
 *
 * @author
 * @version %I%, %G%
 */
public abstract class InputBuffer {

   // Number of active markers

   /** Field nMarkers           */
   protected int nMarkers = 0;

   // Additional offset used when markers are active

   /** Field markerOffset           */
   protected int markerOffset = 0;

   // Number of calls to consume() since last LA()

   /** Field numToConsume           */
   protected int numToConsume = 0;

   // Circular queue

   /** Field queue           */
   protected CharQueue queue;


   /** Create an input buffer */
   public InputBuffer() {
      queue = new CharQueue(1);
   }


   /**
    * This method updates the state of the input buffer so that
    *  the text matched since the most recent mark() is no longer
    *  held by the buffer.  So, you either do a mark/rewind for
    *  failed predicate or mark/commit to keep on parsing without
    *  rewinding the input.
    */
   public void commit() {
      nMarkers = (nMarkers > 0) ? nMarkers - 1 : 0;
   }


   /** Mark another character for deferred consumption */
   public void consume() {
      numToConsume++;
   }


   /**
    * Ensure that the input buffer is sufficiently full
    *
    * @param amount
    *
    * @throws CharStreamException
    */
   public abstract void fill(int amount) throws CharStreamException;


   /**
    * Method getLAChars
    *
    *
    * @return
    *
    */
   public String getLAChars() {
      StringBuffer la = new StringBuffer();
      for (int i = markerOffset; i < queue.nbrEntries; i++) {
         la.append(queue.elementAt(i));
      }

      return la.toString();
   }


   /**
    * Method getCharsFromMark
    *
    *
    * @param mark
    *
    * @return
    *
    */
   public String getCharsFromMark(int mark) {
      StringBuffer la = new StringBuffer();
      int i = mark;

      mark();
      for (; i < markerOffset; i++) {
         la.append(queue.elementAt(i));
      }
      commit();

      return la.toString();
   }


   /**
    * Method LAChars
    *
    *
    * @param n
    *
    * @return
    *
    * @throws CharStreamException
    *
    */
   public String LAChars(int n) throws CharStreamException {
      StringBuffer la = new StringBuffer();

      for (int i = 0; i < n; i++) {
         la.append(LA(i + 1));
      }

      return la.toString();
   }


   /**
    * Method getMarkedChars
    *
    *
    * @return
    *
    */
   public String getMarkedChars() {
      StringBuffer marked = new StringBuffer();

      for (int i = 0; i < markerOffset; i++) {
         marked.append(queue.elementAt(i));
      }

      return marked.toString();
   }


   /**
    * Method isMarked
    *
    *
    * @return
    *
    */
   public boolean isMarked() {
      return (nMarkers != 0);
   }


   /**
    * Get a lookahead character
    *
    * @param i
    *
    * @return
    *
    * @throws CharStreamException
    */
   public char LA(int i) throws CharStreamException {
      fill(i);
      return queue.elementAt(markerOffset + i - 1);
   }


   /**
    * Return an integer marker that can be used to rewind the buffer to
    * its current state.
    *
    * @return
    */
   public int mark() {
      syncConsume();
      nMarkers++;
      return markerOffset;
   }


   /**
    * Rewind the character buffer to a marker.
    * @param mark Marker returned previously from mark()
    */
   public void rewind(int mark) {
      syncConsume();
      markerOffset = mark;
      nMarkers--;
   }


   /** Sync up deferred consumption */
   protected void syncConsume() {
      while (numToConsume > 0) {
         if (nMarkers > 0) {
            // guess mode -- leave leading characters and bump offset.
            markerOffset++;
         }
         else {
            // normal mode -- remove first character
            queue.removeFirst();
         }
         numToConsume--;
      }
   }
}