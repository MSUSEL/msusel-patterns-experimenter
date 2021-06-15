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
 * Class SkeletModule
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class SkeletModule extends ModuleData {

   /** Field refname */
   private String refname;

   /** Field refs */
   private Collection refs = null;


   /**
    * Constructor SkeletModule
    *
    *
    * @param name
    *
    */
   public SkeletModule(String name) {
      super(name, new ArrayList());
   }


   /**
    * Method getRefname
    *
    *
    * @return
    *
    */
   public String getRefname() {
      return (this.refname);
   }


   /**
    * Method setRefname
    *
    *
    * @param refname
    *
    */
   public void setRefname(String refname) {
      this.refname = refname;
   }


   /**
    * Method getRefs
    *
    *
    * @return
    *
    */
   public Collection getRefs() {

      if (refs == null) {
         refs = new ArrayList();
      }

      return (this.refs);
   }


   /**
    * Method setRefs
    *
    *
    * @param refs
    *
    */
   public void setRefs(Collection refs) {
      this.refs = refs;
   }

   public String toString() {
      return "<SkeletModule: refname=" + refname + " - refs=" + refs + '>';
   }
}