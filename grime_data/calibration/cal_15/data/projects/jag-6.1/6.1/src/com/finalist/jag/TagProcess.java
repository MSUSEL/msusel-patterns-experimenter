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
import com.finalist.jag.template.parser.*;
import com.finalist.jag.taglib.*;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;



/**
 * Class TagProcess
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TagProcess implements TagConstants {

   /** Field tagInstance */
   private TagSupport tagInstance = null;

   /** Field tagRef */
   private TemplateTag tagRef = null;

   /** Field process */
   private int process = EVAL_PAGE;

   /** Field bodyText           */
   private String bodyText;


   /**
    * Constructor TagProcess
    *
    *
    * @param tagRef
    *
    */
   public TagProcess(TemplateTag tagRef) {
      this.tagRef = tagRef;
   }


   /**
    * Method process
    *
    *
    * @return
    *
    * @throws JagException
    *
    */
   public int process() throws JagException {

      if (tagInstance == null) {
         try {
            TagDef tagDef = tagRef.getTagDefinition();
            Object object =
               Class.forName(tagDef.getTagClass()).newInstance();

            tagInstance = (TagSupport) object;

            // set support properties
            tagInstance.setPageContext(tagRef.getPageContext());

            // Set tag properties.
            JagParameter[] params = tagRef.getParamArray();

            for (int i = 0; i < params.length; i++) {
               String sIdent = params[i].getIdent();
               String sValue = params[i].getValue();
               Object value = ConvertUtils.convert(sValue, String.class);
               PropertyUtils.setProperty(tagInstance, sIdent, value);
            }

            // Validate required properties.
            AttributeDef[] tagAttributes = tagDef.getAttributeDefArray();

            for (int i = 0; i < tagAttributes.length; i++) {
               if (!tagAttributes[i].getRequired()) {
                  continue;
               }

               String sPropertyName = tagAttributes[i].getName();
               Object value = PropertyUtils.getProperty(tagInstance, sPropertyName);

               if ((value == null) || (value.toString().length() < 1)) {
                  throw new JagException("ERROR: Can't find '" + sPropertyName + "' in " + tagInstance);
               }
            }
         }
         catch (Exception exc) {
            throw new JagException(exc.toString());
         }

         if (tagInstance == null) {
            return getProcess();
         }

         if (!tagRef.isProcessed()) {
            tagRef.setProcessed(true);
            tagRef.clearCloseTag();
            tagRef.clearTag();
         }
      }
      // Reset writer. TagEngine has the option to change the TextBuffer of the tag.
      JagTextBlockWriter writer = new JagTextBlockWriter(tagRef.getTextBuffer());
      tagInstance.setWriter(writer);

      process(tagInstance);

      return getProcess();
   }


   /**
    * Method process
    *
    *
    * @param tagInstance
    *
    * @throws JagException
    *
    */
   public void process(TagSupport tagInstance) throws JagException {

      if (process == EVAL_PAGE) {
         process = tagInstance.doStartTag();

         if (process == SKIP_PAGE) {
            return;
         }
      }

      if ((process != SKIP_BODY && process != SKIP_CLEAR_BODY)
         && (tagInstance instanceof TagBodySupport)) {
         TagBodySupport bodyTag = (TagBodySupport) tagInstance;

         bodyTag.setBodyText(bodyText);

         if (process == EVAL_PAGE) {
            bodyTag.doInitBodyTag();
         }

         process = bodyTag.doAfterBodyTag();

         if (process == EVAL_BODY_TAG) {
            return;
         }

         if (process == SKIP_BODY || process == SKIP_CLEAR_BODY) {
            return;
         }
      }

      process = tagInstance.doEndTag();

      tagInstance.release();
   }


   /**
    * Method getProcess
    *
    *
    * @return
    *
    */
   public int getProcess() {
      return process;
   }


   /**
    * Method setBodyText
    *
    *
    * @param bodyText
    *
    */
   public void setBodyText(String bodyText) {
      this.bodyText = bodyText;
   }
}

;