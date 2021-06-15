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

import com.finalist.jag.JagException;
import com.finalist.jag.template.*;

/**
 * Class TagLibrary
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TagLibrary {
   /** Field libVersion           */
   private String libVersion = null;

   /** Field jagVersion           */
   private String jagVersion = null;

   /** Field shortName           */
   private String shortName = null;

   /** Field info           */
   private String info = null;

   /** Field tagDefs           */
   private Collection tagDefs = null;


   /**
    * Method setLibVersion
    *
    *
    * @param libVersion
    *
    */
   public void setLibVersion(String libVersion) {
      this.libVersion = libVersion;
   }


   /**
    * Method setJagVersion
    *
    * @param jagVersion
    *
    */
   public void setJagVersion(String jagVersion) {
      this.jagVersion = jagVersion;
   }


   /**
    * Method setShortName
    *
    *
    * @param shortName
    *
    */
   public void setShortName(String shortName) {
      this.shortName = shortName;
   }


   /**
    * Method setInfo
    *
    *
    * @param info
    *
    */
   public void setInfo(String info) {
      this.info = info;
   }


   /**
    * Method setTagDefs
    *
    *
    * @param tagDefs
    *
    */
   public void setTagDefs(Collection tagDefs) {
      this.tagDefs = tagDefs;
   }


   /**
    * Method getLibVersion
    *
    *
    * @return
    *
    */
   public String getLibVersion() {
      return (this.libVersion);
   }


   /**
    * Method getJagVersion
    *
    *
    * @return
    *
    */
   public String getJagVersion() {
      return (this.jagVersion);
   }


   /**
    * Method getShortName
    *
    *
    * @return
    *
    */
   public String getShortName() {
      return (this.shortName);
   }


   /**
    * Method getInfo
    *
    *
    * @return
    *
    */
   public String getInfo() {
      return (this.info);
   }


   /**
    * Method getTagDefs
    *
    *
    * @return
    *
    */
   public Collection getTagDefs() {
      if (tagDefs == null)
         tagDefs = new java.util.ArrayList();

      return (this.tagDefs);
   }


   /**
    * Method getTagDef
    *
    *
    * @param tagRef
    *
    * @return
    *
    * @throws JagException
    *
    */
   public TagDef getTagDef(TemplateTag tagRef) throws JagException {
      TagDef tagdef = findTagDef(tagRef);

      if (tagdef == null) {
         throw new JagException("" + tagRef.getTagName()
            + " doesn't exist in the tag library "
            + shortName);
      }
      return tagdef;
   }


   /**
    * Method findTagDef
    *
    *
    * @param tagRef
    *
    * @return
    *
    */
   public TagDef findTagDef(TemplateTag tagRef) {
      java.util.Iterator iterator = tagDefs.iterator();

      while (iterator.hasNext()) {
         TagDef tagDef = (TagDef) iterator.next();

         if (!(getShortName().equals(tagRef.getTagLib())))
            continue;    // should be impossible

         if (!(tagDef.getName().equals(tagRef.getTagName())))
            continue;

         return tagDef;
      }

      return null;
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
      toString.append("\nlibVersion : ");
      toString.append(libVersion);
      toString.append("\njagVersion : ");
      toString.append(jagVersion);
      toString.append("\nshortName : ");
      toString.append(shortName);
      toString.append("\ninfo : ");
      toString.append(info);
      toString.append("\ntagDefs : ");
      toString.append(tagDefs);

      Collection col = getTagDefs();
      java.util.Iterator iterator = col.iterator();

      while (iterator.hasNext()) {
         TagDef tagDef = (TagDef) iterator.next();
         toString.append("[TagDef]" + tagDef.toString());
      }
      return new String(toString);
   }
}

