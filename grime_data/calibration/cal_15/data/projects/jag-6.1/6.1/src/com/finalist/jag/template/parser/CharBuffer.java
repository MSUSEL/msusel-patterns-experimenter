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

import java.io.*;


/**
 * Class CharBuffer
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class CharBuffer extends InputBuffer {

   // char source

   /** Field input           */
   private Reader input = null;

   /** Field inputString           */
   private String inputString = null;

   /** Field stringIndex           */
   private int stringIndex = 0;

   /** Field stringLength           */
   private int stringLength = 0;


   /**
    * Create a character buffer
    *
    * @param input
    */
   public CharBuffer(Reader input) {
      super();
      this.input = input;
   }


   /**
    * Create a character buffer
    *
    * @param input
    */
   public CharBuffer(String input) {
      super();
      this.inputString = input;
      this.stringLength = input.length();
   }


   /**
    * Ensure that the character buffer is sufficiently full
    *
    * @param amount
    *
    * @throws CharStreamException
    */
   public void fill(int amount) throws CharStreamException {
      try {
         syncConsume();
         // Fill the buffer sufficiently to hold needed characters
         while (queue.nbrEntries < amount + markerOffset) {
            queue.append(read());
         }
      }
      catch (IOException io) {
         throw new CharStreamIOException(io);
      }
   }


   /**
    * Method read
    *
    *
    * @return
    *
    * @throws IOException
    *
    */
   private char read() throws IOException {
      if (input != null) {
         return (char) input.read();
      }

      if ((inputString != null) && (stringIndex < stringLength)) {
         return (char) inputString.charAt(stringIndex++);
      }
      return (char) -1;
   }
}