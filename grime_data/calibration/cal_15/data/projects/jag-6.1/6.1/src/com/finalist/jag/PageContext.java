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
package com.finalist.jag;


import com.finalist.jag.template.*;


/**
 * Class PageContext
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class PageContext {

   /** Field sessionContext */
   private SessionContext sessionContext = null;

   /** Field headerCollection */
   private TemplateHeaderCollection headerCollection = null;

   /** Field pageAttributes */
   private AttributeMap pageAttributes = new AttributeMap();

   /** Field templateData */
   private TemplateStructure templateData = null;


   /**
    * Constructor PageContext
    *
    *
    * @param sessionContext
    *
    */
   public PageContext(SessionContext sessionContext) {
      this.sessionContext = sessionContext;
   }


   /**
    * Method setHeaderCollection
    *
    *
    * @param headerCollection
    *
    */
   public void setHeaderCollection(
      TemplateHeaderCollection headerCollection) {
      this.headerCollection = headerCollection;
   }


   /**
    * Method setTemplateData
    *
    *
    * @param templateData
    *
    */
   public void setTemplateData(TemplateStructure templateData) {
      this.templateData = templateData;
   }


   /**
    * Method getTextCollection
    *
    *
    * @return
    *
    */
   public TemplateTextBlockList getTextCollection() {

      if (templateData == null) {
         return null;
      }

      return templateData.getTextBlockList();
   }


   /**
    * Method getHeaderCollection
    *
    *
    * @return
    *
    */
   public TemplateHeaderCollection getHeaderCollection() {
      return headerCollection;
   }


   /**
    * Method setAttribute
    *
    *
    * @param name
    * @param obj
    *
    */
   public void setAttribute(String name, Object obj) {
      pageAttributes.setAttribute(name, obj);
   }


   /**
    * Method removeAttribute
    *
    *
    * @param name
    *
    */
   public void removeAttribute(String name) {
      pageAttributes.removeAttribute(name);
   }


   /**
    * Method getSessionContext
    *
    *
    * @return
    *
    */
   public SessionContext getSessionContext() {
      return sessionContext;
   }


   /**
    * Method getTemplateData
    *
    *
    * @return
    *
    */
   public TemplateStructure getTemplateData() {
      return templateData;
   }


   /**
    * Method getAttribute
    *
    *
    * @param name
    *
    * @return
    *
    */
   public Object getAttribute(String name) {
      return pageAttributes.getAttribute(name);
   }
}

;
