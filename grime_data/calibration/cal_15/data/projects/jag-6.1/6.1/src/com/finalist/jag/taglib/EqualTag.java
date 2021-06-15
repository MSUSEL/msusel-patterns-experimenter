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

import java.util.*;


/**
 * Class EqualTag
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class EqualTag extends TagBodySupport {

   /** Field name           */
   private String name = null;

   /** Field property           */
   private String property = null;

   /** Field parameter           */
   protected String parameter = null;

   /** Field sensitive           */
   private String sensitive = null;

   /** Field equal           */
   protected boolean equal = false;

   /** Field sensitive           */
   protected int counter = 0;

   protected String variable;

   /////////////////////////////////////

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
    * Method getParameter
    *
    *
    * @return
    *
    */
   public String getParameter() {
      return (this.parameter);
   }

   /**
    * Method setParameter
    *
    *
    * @param parameter
    *
    */
   public void setParameter(String parameter) {
      this.parameter = parameter;
   }


   public String getSensitive() {
      return (this.sensitive);
   }

   public void setSensitive(String sensitive) {
      this.sensitive = sensitive;
   }

   public String getVariable() {
      return variable;
   }

   public void setVariable(String variable) {
      this.variable = variable;
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

      String value = RequestUtil.lookupString(getPageContext(), name, property);
      if (value == null) {
         value = (String) getPageContext().getAttribute(name);
      }

      if (value == null) {
         value = "";
      }

      if (sensitive != null && sensitive.equals("true")) {
         value = value.toLowerCase();
         parameter = parameter.toLowerCase();
      }

      if (variable != null) {
         Object variableValue = getPageContext().getAttribute(variable);

        if (variableValue == null) {
            equal = false;
         } else {
            equal = (variableValue instanceof String) ?
                  value.equals(((String) variableValue)) :
                  ((Set) variableValue).contains(value);
         }

      } else {
         StringTokenizer tokens = new StringTokenizer(parameter, ", ");
         while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            equal = token.equals(value);
            if (equal) {
               break;
            }
         }
      }
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
      return (equal && (counter++ < 1)) ? (EVAL_BODY_TAG) : (SKIP_BODY);
   }
}


