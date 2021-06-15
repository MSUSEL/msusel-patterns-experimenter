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
package com.finalist.jag.skelet;


import java.util.*;


/**
 * Class ModuleData
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class ModuleData {

   /** Field COLLECTION           */
   private static final short COLLECTION = 1;

   /** Field STRING           */
   private static final short STRING = 2;

   /** Field name           */
   private String name;

   /** Field value           */
   private Object value;

   /** Field valueType           */
   private short valueType;


   /**
    * Constructor ModuleData
    *
    *
    * @param name
    * @param value
    *
    */
   public ModuleData(String name, String value) {
      setName(name);
      setValue(value);
   }


   /**
    * Constructor ModuleData
    *
    *
    * @param name
    * @param value
    *
    */
   public ModuleData(String name, Collection value) {
      setName(name);
      setValue(value);
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
   public Object getValue() {
      return (this.value);
   }


   /**
    * Method setValue
    *
    *
    * @param value
    *
    */
   public void setValue(String value) {
      this.valueType = STRING;
      this.value = value;
   }


   /**
    * Method setValue
    *
    *
    * @param value
    *
    */
   public void setValue(Collection value) {
      this.valueType = COLLECTION;
      this.value = value;
   }


   /**
    * Method isValueCollection
    *
    *
    * @return
    *
    */
   public boolean isValueCollection() {
      return this.valueType == COLLECTION;
   }


   /**
    * Method isValueString
    *
    *
    * @return
    *
    */
   public boolean isValueString() {
      return this.valueType == STRING;
   }
}