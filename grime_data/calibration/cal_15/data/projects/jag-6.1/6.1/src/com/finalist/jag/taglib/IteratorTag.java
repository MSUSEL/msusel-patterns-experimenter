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
package com.finalist.jag.taglib;


import java.util.*;

import com.finalist.jag.*;
import com.finalist.jag.taglib.util.RequestUtil;


/**
 * Class IteratorTag
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class IteratorTag extends TagBodySupport {

   /** Field id           */
   private String id = null;

   /** Field name           */
   private String name = null;

   /** Field property           */
   private String property = null;

   /** Field seperator           */
   private String seperator = null;

   /** Field iterator           */
   private Iterator iterator = null;

   /** Field max           */
   protected String max = null;

   /** Field counterId           */
   protected String counterId = null;


   /////////////////////////////////

   /** Field counter           */
   protected int counter = 0;

   /** Field nMax           */
   protected int nMax = 0;

   /**
    * Method getId
    *
    *
    * @return
    *
    */
   public String getId() {
      return (this.id);
   }

   /**
    * Method setId
    *
    *
    * @param id
    *
    */
   public void setId(String id) {
      this.id = id;
   }

   /**
    * Method getName
    *
    *
    * @return
    *
    */
   public String getName() {
      return (this.name);
   }

   /**
    * Method setName
    *
    *
    * @param name
    *
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * Method getValue
    *
    *
    * @return
    *
    */
   public String getProperty() {
      return (this.property);
   }

   /**
    * Method setValue
    *
    *
    * @param property
    *
    */
   public void setProperty(String property) {
      this.property = property;
   }

   /**
    * Method getSeperator
    *
    *
    * @return
    *
    */
   public String getSeperator() {
      return (this.seperator);
   }

   /**
    * Method setSeperator
    *
    *
    * @param seperator
    *
    */
   public void setSeperator(String seperator) {
      this.seperator = seperator;
   }

   /**
    * Method getMax
    *
    *
    * @return
    *
    */
   public String getMax() {
      return (this.max);
   }

   /**
    * Method setMax
    *
    *
    * @param max
    *
    */
   public void setMax(String max) {
      this.max = max;
   }

   /**
    * Method getCounter
    *
    *
    * @return
    *
    */
   public String getCounter() {
      return (this.counterId);
   }

   /**
    * Method setCounter
    *
    *
    * @param counterId
    *
    */
   public void setCounter(String counterId) {
      this.counterId = counterId;
   }

   /**
    * Method doStartTag
    *
    *
    * @return
    *
    * @throws JagException
    *
    */
   public int doStartTag() throws JagException {

      Collection collection = RequestUtil.lookupCollection(getPageContext(),
            name, property);

      if (collection == null) {
         throw new JagException("IteratorTag: Invalid attributes >" + name
               + ", " + property + "<");
      }
      try {
         nMax = (max == null) ? -1 : new Integer(max).intValue();
      } catch (Exception e) {
         throw new JagException(e.getMessage());
      }
      iterator = collection.iterator();

      return (EVAL_PAGE);
   }

   /**
    * Method doAfterBodyTag
    *
    *
    * @return
    *
    * @throws JagException
    *
    */
   public int doAfterBodyTag() throws JagException {
      while ((iterator != null) && iterator.hasNext()) {
         if (counterId != null) {
            getPageContext().setAttribute(counterId, "" + counter);
         }
         if (nMax > -1 && counter >= nMax) break;

         getPageContext().setAttribute(id, iterator.next());
         if (seperator != null && counter > 0) {
            getWriter().print(seperator);
         }
         counter++;
         return (EVAL_BODY_TAG);
      }

      return (SKIP_BODY);
   }

   /**
    * Method release
    *
    *
    */
   public void release() {

      super.release();
      getPageContext().removeAttribute(id);
      if (counterId != null) {
         getPageContext().removeAttribute(counterId);
      }
      iterator = null;
   }
}

