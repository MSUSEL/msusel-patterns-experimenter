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
package com.finalist.util;

import com.lowagie.text.html.HtmlEncoder;

/**
 * This class represents a line of text from a source file that conflicted during the diff process.
 *
 * @author Michael O'Connor - Finalist IT Group.
 */
public class DiffConflictLine {

   private String line;
   private int number;
   private boolean firstFile;

   /** A special case of DiffConflictLine, used to represent the last line in a file. */
   public static final DiffConflictLine EOF = new DiffConflictLine();


   /**
    * Constructs a DiffConflictLine.
    * @param firstFile <code>true</code> if this line comes from the 'first' file (a diff involves 2 files).
    * @param number the line number within the original file.
    * @param line the text of the line.
    */
   public DiffConflictLine(boolean firstFile, int number, String line) {
      this.number = number;
      this.firstFile = firstFile;
      this.line = line;
   }

   private DiffConflictLine() {
   }


   /**
    * Checks if the given line has the same text as this one (ignoring whitespace).
    * @param line2 the other line.
    * @return <code>true</code> if equal.
    */
   public boolean lineEquals(DiffConflictLine line2) {
      return line.trim().equals(line2.getLine().trim());
   }

   public String getLine() {
      return line;
   }

   public boolean isEof() {
      return (this == EOF);
   }

   public int getLineNumber() {
      return number;
   }

   public boolean isFirstFile() {
      return firstFile;
   }
   /**
    * By default this renders a HTML result.
    * @return
    */
   public String toString() {
      return "<font class='file" + (firstFile ? "1" : "2") + "-code'>" +
            (firstFile ? "&lt;" : "&gt;") + HtmlEncoder.encode(getLine()) + "</font><br>";
   }

   /**
    * Checks if a given conflict line precedes this one.
    *
    * @param next
    * @return
    */
   public boolean precedes(DiffConflictLine next) {
      return next != null &&
               (firstFile == next.firstFile) &&
               next.getLineNumber() == number + 1;
   }

}
