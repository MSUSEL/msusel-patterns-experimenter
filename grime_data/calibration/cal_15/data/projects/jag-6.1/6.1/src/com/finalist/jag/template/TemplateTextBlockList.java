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


import java.util.*;


/**
 * Class TemplateTextBlockCollection
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateTextBlockList extends LinkedList {

   /**
    * Method setCollection
    *
    *
    *
    */
   public TemplateTextBlockList() {
   }


   /**
    * Method add
    *
    *
    * @param block
    *
    */
   public void add(TemplateTextBlock block) {
      this.addLast(block);
   }


   /**
    * Method add
    *
    *
    *
    * @param block1
    * @param block2
    *
    *
    * @return
    */
   public TemplateTextBlockList cut(TemplateTextBlock block1,
      TemplateTextBlock block2) {

      TemplateTextBlockList list = new TemplateTextBlockList();
      int nIndex1 = indexOf(block1);
      int nIndex2 = indexOf(block2);

      while ((nIndex1 != -1) && (nIndex1 <= nIndex2)) {
         list.add(list.get(nIndex1));
         remove(nIndex1++);
      }

      return list;
   }


   /**
    * Method getBefore
    *
    *
    * @param block
    *
    * @return
    *
    */
   public TemplateTextBlock getBefore(TemplateTextBlock block) {

      int nIndex = indexOf(block);

      if (nIndex == -1) {
         return null;
      }

      ListIterator iterator = listIterator(nIndex);

      return iterator.hasPrevious()
         ? (TemplateTextBlock) iterator.previous()
         : null;
   }


   /**
    * Method getAfter
    *
    *
    * @param block
    *
    * @return
    *
    */
   public TemplateTextBlock getAfter(TemplateTextBlock block) {

      int nIndex = indexOf(block);

      if (nIndex == -1) {
         return null;
      }

      ListIterator iterator = listIterator(nIndex);

      return iterator.hasNext()
         ? (TemplateTextBlock) iterator.next()
         : null;
   }


   /**
    * Method getStringBuffer
    *
    *
    * @return
    *
    */
   public StringBuffer getStringBuffer() {

      StringBuffer returnValue = new StringBuffer();
      ListIterator iterator = listIterator();

      while (iterator.hasNext()) {
         returnValue
            .append(((TemplateTextBlock) iterator.next()).getText());
      }

      return returnValue;
   }


   /**
    * Method toArray
    *
    *
    * @return
    *
    */
   public TemplateTextBlock[] toTextBlockArray() {
      return (TemplateTextBlock[]) super
         .toArray(new TemplateTextBlock[size()]);
   }
}