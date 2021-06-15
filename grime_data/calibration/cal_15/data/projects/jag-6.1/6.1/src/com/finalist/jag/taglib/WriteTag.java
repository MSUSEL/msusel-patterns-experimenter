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


import com.finalist.jag.*;
import com.finalist.jag.taglib.util.RequestUtil;
import com.finalist.jaggenerator.Utils;

import java.util.*;


/**
 * Class WriteTag
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class WriteTag extends TagSupport {

   /** Field name           */
   private String name = null;

   /** Field property           */
   private String property = null;

   /** Field display           */
   private String display = null;

   /** Field pathEnable           */
   private String pathEnable = null;

   /////////////////////////////////////

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
    * Method getSensitive
    *
    *
    * @return
    *
    */
   public String getDisplay() {
      return (this.display);
   }

   /**
    * Method setDisplay
    *
    *
    * @param display
    *
    */
   public void setDisplay(String display) {
      this.display = display;
   }

   /**
    * Method getPathenable
    *
    *
    * @return
    *
    */
   public String getPathenable() {
      return (this.pathEnable);
   }

   /**
    * Method setPathenable
    *
    *
    * @param pathEnable
    *
    */
   public void setPathenable(String pathEnable) {
      this.pathEnable = pathEnable;
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

      SessionContext session = getPageContext().getSessionContext();
      String value = RequestUtil.lookupString(getPageContext(),
            name, property);

      if (value == null) {
         throw new JagException("WriteTag: Invalid name field \"" + name
               + "\" (property=\"" + property + "\")");
      }

      if (display != null) {
         display = display.toLowerCase();

         if (display.equals("lower")) {
            value = value.toLowerCase();
         }
         else if (display.equals("upper")) {
            value = value.toUpperCase();
         }
         else if (display.equals("sentensize")) {
            if (value.length() > 1) {
               String tmp = value.substring(0, 1).toUpperCase();

               value = value.substring(1);
               value = tmp + value;
            } else {
               value = value.toUpperCase();
            }
         }
         else if (display.equals("desentensize")) {
            if (value.length() > 1) {
               String tmp = value.substring(0, 1).toLowerCase();

               value = value.substring(1);
               value = tmp + value;
            } else {
               value = value.toLowerCase();
            }
         }
         else if (display.equals("crazy_struts")) {
            //anElephant --> anElephant
            //aHorse --> AHorse
            if (value.length() > 1 &&
                  Character.isLowerCase(value.charAt(0)) && Character.isUpperCase(value.charAt(1))) {
               value = Character.toUpperCase(value.charAt(0)) + value.substring(1);
            }
         }
      }

      if ((pathEnable != null) && pathEnable.equals("true")) {
         value = value.replace('.', '/');
      }

      getWriter().print(value);

      return (EVAL_PAGE);
   }

}