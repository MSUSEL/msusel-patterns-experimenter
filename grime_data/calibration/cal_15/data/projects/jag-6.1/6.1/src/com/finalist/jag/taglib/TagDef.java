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
import java.util.*;


/**
 * Class TagDef
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TagDef {

   /** Field name           */
   private String name = null;

   /** Field tagClass           */
   private String tagClass = null;

   /** Field bodyContent           */
   private String bodyContent = null;

   /** Field attrDefs           */
   private Collection attrDefs = null;


   /**
    * Constructor TagDef
    *
    *
    */
   public TagDef() {
   }


   /**
    * Constructor TagDef
    *
    *
    * @param n
    *
    */
   public TagDef(TagDef n) {

      this.name = n.name;
      this.tagClass = n.tagClass;
      this.bodyContent = n.bodyContent;
      attrDefs = new ArrayList();

      AttributeDef[] ar = this.getAttributeDefArray();

      for (int i = 0; i < ar.length; i++) {
         attrDefs.add(new AttributeDef(ar[i]));
      }
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
    * Method setTagClass
    *
    *
    * @param tagClass
    *
    */
   public void setTagClass(String tagClass) {
      this.tagClass = tagClass;
   }


   /**
    * Method setAttributeDefs
    *
    *
    * @param attrDefs
    *
    */
   public void setAttributeDefs(Collection attrDefs) {
      this.attrDefs = attrDefs;
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
    * Method getTagClass
    *
    *
    * @return
    *
    */
   public String getTagClass() {
      return (this.tagClass);
   }


   /**
    * Method getAttributeDefs
    *
    *
    * @return
    *
    */
   public Collection getAttributeDefs() {
      return (this.attrDefs);
   }


   /**
    * Method getAttributeDefArray
    *
    *
    * @return
    *
    */
   public AttributeDef[] getAttributeDefArray() {

      int size = attrDefs.size();

      return (AttributeDef[]) attrDefs.toArray(new AttributeDef[size]);
   }


   /**
    * Method setBodyContent
    *
    *
    * @param bodyContent
    *
    */
   public void setBodyContent(String bodyContent) {
      this.bodyContent = bodyContent;
   }


   /**
    * Method getBodyContent
    *
    *
    * @return
    *
    */
   public String getBodyContent() {
      return (this.bodyContent);
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
      toString.append("\nname : ");
      toString.append(name);
      toString.append("\ntagClass : ");
      toString.append(tagClass);
      toString.append("\nbodyContent : ");
      toString.append(bodyContent);
      toString.append("\nattrDefs : ");
      toString.append(attrDefs);

      return new String(toString);
   }
}