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
package com.finalist.jag;


import com.finalist.jag.template.*;


/**
 * Class JagTextBlockWriter
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class JagTextBlockWriter extends JagWriter {

   /** Field buffer */
   private TemplateTextBlock buffer = null;


   /**
    * Constructor JagTextBlockWriter
    *
    *
    */
   public JagTextBlockWriter() {
      buffer = new TemplateTextBlock("");
   }


   /**
    * Constructor JagTextBlockWriter
    *
    *
    * @param buffer
    *
    */
   public JagTextBlockWriter(TemplateTextBlock buffer) {
      this.buffer = buffer;
   }


   /**
    * Clear the contents of the buffer.
    */
   public void clear() {
      buffer.set();
   }


   /**
    * Write a line separator.
    */
   public void newLine() {
      buffer.append(java.io.File.separator);
   }


   /**
    * Print a boolean value.
    *
    * @param b
    */
   public void print(boolean b) {
      buffer.append(new StringBuffer().append(b));
   }


   /**
    * Print a character.
    *
    * @param c
    */
   public void print(char c) {
      buffer.append(new StringBuffer().append(c));
   }


   /**
    * Print an array of characters.
    *
    * @param s
    */
   public void print(char[] s) {
      buffer.append(new StringBuffer().append(s));
   }


   /**
    * Print a double-precision floating-point number.
    *
    * @param d
    */
   public void print(double d) {
      buffer.append(new StringBuffer().append(d));
   }


   /**
    * Print a floating-point number.
    *
    * @param f
    */
   public void print(float f) {
      buffer.append(new StringBuffer().append(f));
   }


   /**
    * Print an integer.
    *
    * @param i
    */
   public void print(int i) {
      buffer.append(new StringBuffer().append(i));
   }


   /**
    * Print a long integer.
    *
    * @param l
    */
   public void print(long l) {
      buffer.append(new StringBuffer().append(l));
   }


   /**
    * Print an object.
    *
    * @param obj
    */
   public void print(Object obj) {
      buffer.append(new StringBuffer().append(obj));
   }


   /**
    * Print a string.
    *
    * @param s
    */
   public void print(String s) {
      buffer.append(s);
   }


   /**
    * Terminate the current line by writing the line separator string.
    */
   public void println() {
      newLine();
   }


   /**
    * Print a boolean value and then terminate the line.
    *
    * @param x
    */
   public void println(boolean x) {
      print(x);
      newLine();
   }


   /**
    * Print a character and then terminate the line.
    *
    * @param x
    */
   public void println(char x) {
      print(x);
      newLine();
   }


   /**
    * Print an array of characters and then terminate the line.
    *
    * @param x
    */
   public void println(char[] x) {
      print(x);
      newLine();
   }


   /**
    * Print a double-precision floating-point number and then terminate the line.
    *
    * @param x
    */
   public void println(double x) {
      print(x);
      newLine();
   }


   /**
    * Print a floating-point number and then terminate the line.
    *
    * @param x
    */
   public void println(float x) {
      print(x);
      newLine();
   }


   /**
    * Print an integer and then terminate the line.
    *
    * @param x
    */
   public void println(int x) {
      print(x);
      newLine();
   }


   /**
    * Print a long integer and then terminate the line.
    *
    * @param x
    */
   public void println(long x) {
      print(x);
      newLine();
   }


   /**
    * Print an Object and then terminate the line.
    *
    * @param x
    */
   public void println(Object x) {
      print(x);
      newLine();
   }


   /**
    * Print a String and then terminate the line.
    *
    * @param x
    */
   public void println(String x) {
      print(x);
      newLine();
   }


   /**
    * Method createNewFile
    *
    *
    * @param path
    *
    */
   public void createNewFile(java.lang.String path) {
      buffer.setFile(path);
   }


   /**
    * Method createNewFile
    *
    *
    * @param path
    *
    */
   public void createNewFile(java.lang.StringBuffer path) {
      createNewFile(new String(path));
   }
}

;