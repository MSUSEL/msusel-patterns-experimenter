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
package com.finalist.jag.template;

/**
 * Class TemplateTextBlock
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateTextBlock {

   /** Field buffer           */
   private StringBuffer buffer;

   /** Field filePath           */
   private String filePath;


   /**
    * Constructor TemplateTextBlock
    *
    *
    * @param s
    *
    */
   public TemplateTextBlock(String s) {
      set(s);
   }

   /**
    * Constructor TemplateTextBlock
    *
    *
    * @param c
    *
    */
   public TemplateTextBlock(TemplateTextBlock c) {

      if ((c != null) && (c.buffer != null)) {
         this.buffer = new StringBuffer(new String(c.buffer));
      }
   }

   /**
    * Method set
    *
    *
    */
   public void set() {
      buffer = new StringBuffer();
   }

   /**
    * Method set
    *
    *
    * @param s
    *
    */
   public void set(String s) {
      buffer = new StringBuffer(s);
   }

   /**
    * Method append
    *
    *
    * @param s
    *
    */
   public void append(String s) {
      buffer.append(s);
   }

   /**
    * Method append
    *
    *
    * @param s
    *
    */
   public void append(StringBuffer s) {
      buffer.append(s.toString());
   }

   /**
    * Method getText
    *
    *
    * @return
    *
    */
   public String getText() {
      return new String(buffer);
   }

   /**
    * Method toString
    *
    *
    * @return
    *
    */
   public String toString() {
      return buffer.toString();
   }

   /**
    * Method isEmpty
    *
    *
    * @return
    *
    */
   public boolean isEmpty() {
      return buffer.length() < 1;
   }

   /**
    * Method setFile
    *
    *
    * @param path
    *
    */
   public void setFile(String path) {
      this.filePath = path;
   }

   /**
    * Method getFile
    *
    *
    * @return
    *
    */
   public String getFile() {
      return filePath;
   }

   /**
    * Method newFile
    *
    *
    * @return
    *
    */
   public boolean newFile() {
      return (filePath != null) && (filePath.length() > 0);
   }
}