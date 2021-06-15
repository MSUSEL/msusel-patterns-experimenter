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


import com.finalist.jag.template.parser.*;
import com.finalist.jag.taglib.*;
import com.finalist.jag.*;

/**
 * Class TemplateTag
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateTag {
   /** TemplateTag definition retrieved from the tag librariy file */
   private TagDef tagDef = null;

   /** The pageContext which contains this TemplateTag */
   private PageContext pageContext = null;

   /** Field tagLib */
   private String tagLib;

   /** Field tagName */
   private String tagName;

   /** Field paramArray */
   private JagParameter[] paramArray = new JagParameter[0];

   /** Field textBuffer */
   private TemplateTextBlock textBuffer = null;

   /** Field closing textBuffer */
   private TemplateTextBlock closeTagBuffer = null;

   /** Field processed           */
   private boolean processed = false;


   /**
    * Constructor TemplateTag
    *
    *
    * @param tagLib
    * @param tagName
    * @param textBuffer
    *
    */
   public TemplateTag(String tagLib, String tagName, TemplateTextBlock textBuffer) {

      this.tagLib = tagLib;
      this.tagName = tagName;
      this.textBuffer = textBuffer;
   }


   /**
    * Constructor TemplateTag
    *
    * Creates a copy of the references, except for the field 'pageContext'.
    * The field 'processed' is set to false.
    *
    * @param n
    *
    */
   public TemplateTag(TemplateTag n) {
      clone(n);

      this.textBuffer = new TemplateTextBlock(n.textBuffer);
      this.closeTagBuffer = new TemplateTextBlock(n.closeTagBuffer);
      this.tagDef = (n.tagDef != null) ? new TagDef(n.tagDef) : null;
      this.paramArray = new JagParameter[n.paramArray.length];

      for (int i = 0; i < paramArray.length; i++) {
         this.paramArray[i] = new JagParameter(n.paramArray[i]);
      }
   }


   /**
    * Method getTextBuffer
    *
    *
    * @return
    *
    */
   public TemplateTextBlock getTextBuffer() {
      return textBuffer;
   }


   /**
    * Constructor TemplateTag
    *
    *
    * @param tagLib
    * @param tagName
    * @param paramArray
    * @param textBuffer
    *
    */
   public TemplateTag(String tagLib, String tagName, JagParameter[] paramArray,
      TemplateTextBlock textBuffer) {

      this(tagLib, tagName, textBuffer);

      this.paramArray = paramArray;
   }


   /**
    * Method clone
    *
    * Clones all the references.
    *
    * @param other
    *
    */
   public void clone(TemplateTag other) {

      this.tagDef = other.tagDef;
      this.tagLib = other.tagLib;
      this.tagName = other.tagName;
      this.paramArray = other.paramArray;
      this.textBuffer = other.textBuffer;
      this.closeTagBuffer = other.closeTagBuffer;
      this.pageContext = other.pageContext;
   }


   /**
    * Method setTagDefinition
    *
    *
    * @param tagDef
    *
    */
   public void setTagDefinition(TagDef tagDef) {
      this.tagDef = tagDef;
   }


   /**
    * Method setPageContext
    *
    *
    * @param pageContext
    *
    */
   public void setPageContext(PageContext pageContext) {
      this.pageContext = pageContext;
   }


   /**
    * Method setTagLib
    *
    *
    * @param tagLib
    *
    */
   public void setTagLib(String tagLib) {
      this.tagLib = tagLib;
   }


   /**
    * Method setTagName
    *
    *
    * @param tagName
    *
    */
   public void setTagName(String tagName) {
      this.tagName = tagName;
   }


   /**
    * Method setParamArray
    *
    *
    * @param paramArray
    *
    */
   public void setParamArray(JagParameter[] paramArray) {
      this.paramArray = paramArray;
   }


   /**
    * Method setTextBuffer
    *
    *
    * @param textBuffer
    *
    */
   public void setTextBuffer(TemplateTextBlock textBuffer) {
      this.textBuffer = textBuffer;
   }


   /**
    * Method getTagDefinition
    *
    *
    * @return
    *
    */
   public TagDef getTagDefinition() {
      return (this.tagDef);
   }


   /**
    * Method getTagLib
    *
    *
    * @return
    *
    */
   public String getTagLib() {
      return (this.tagLib);
   }


   /**
    * Method getTagName
    *
    *
    * @return
    *
    */
   public String getTagName() {
      return (this.tagName);
   }


   /**
    * Method getParamArray
    *
    *
    * @return
    *
    */
   public JagParameter[] getParamArray() {
      return (this.paramArray);
   }


   /**
    * Method getPageContext
    *
    *
    * @return
    *
    */
   public PageContext getPageContext() {
      return (this.pageContext);
   }


   /**
    * Method setClosingTextBuffer
    *
    *
    * @param endBuffer
    *
    */
   public void setClosingTextBuffer(TemplateTextBlock endBuffer) {
      closeTagBuffer = endBuffer;
   }


   /**
    * Method clearCloseTag
    *
    *
    */
   public void clearCloseTag() {

      if (closeTagBuffer != null) {
         closeTagBuffer.set();
      }
   }


   /**
    * Method clearTag
    *
    *
    */
   public void clearTag() {

      if (textBuffer != null) {
         textBuffer.set();
      }
   }


   /**
    * Method setProcessed
    *
    *
    * @param b
    *
    */
   public void setProcessed(boolean b) {
      processed = b;
   }


   /**
    * Method isProcessed
    *
    *
    * @return
    *
    */
   public boolean isProcessed() {
      return (this.processed);
   }


   /**
    * Method getClosingTextBuffer
    *
    *
    * @return
    *
    */
   public TemplateTextBlock getClosingTextBuffer() {
      return (this.closeTagBuffer);
   }
}

;