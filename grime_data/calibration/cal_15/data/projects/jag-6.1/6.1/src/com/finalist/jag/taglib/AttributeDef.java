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


import java.util.Collection;


/**
 * Class AttributeDef
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class AttributeDef {

   /** Field name           */
   private String name = null;

   /** Field required           */
   private boolean required = false;


   /**
    * Constructor AttributeDef
    *
    *
    */
   public AttributeDef() {
   }


   /**
    * Constructor AttributeDef
    *
    *
    * @param n
    *
    */
   public AttributeDef(AttributeDef n) {
      this.name = n.name;
      this.required = n.required;
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
    * Method setRequired
    *
    *
    * @param required
    *
    */
   public void setRequired(boolean required) {
      this.required = required;
   }


   /**
    * Method setRequired
    *
    *
    * @param required
    *
    */
   public void setRequired(String required) {

      this.required = !((required == null)
         || required.equalsIgnoreCase("false")
         || required.equalsIgnoreCase("0"));
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
    * Method getRequired
    *
    *
    * @return
    *
    */
   public boolean getRequired() {
      return (this.required);
   }


   /**
    * Method toString
    *
    *
    * @return
    *
    */
   public String toString() {
      StringBuffer toString = new StringBuffer();
      toString.append("\name : ");
      toString.append(name);
      toString.append("\required : ");
      toString.append(required);

      return new String(toString);
   }
}