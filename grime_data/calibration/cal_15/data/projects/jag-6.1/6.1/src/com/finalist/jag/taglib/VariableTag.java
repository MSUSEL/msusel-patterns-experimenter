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

import java.util.*;


/**
 * This tag allows you to specify the value of a run-time 'variable' by evaluating other JAG tags, and then use that
 * variable as a parameter to further tags.  For example you can have a <code>&lt;jag:equals&gt;</code> tag where you check if
 * a property is equal to some <b>static</b> string, but if you want to check if it's equal to the run-time output of a
 * <code>&lt;jag:write&gt;</code> tag, you're screwed!
 * <p>
 * Using this tag, you could prepare a variable with the output of the <code>&lt;jag:write&gt;</code> tag, and then use
 * that variable in the <code>&lt;jag:equals&gt;</code> tag.
 * <p>
 * The <code>list</code> parameter may be set to "true" to enable the variable to be built up from a list of values.
 * For example, the following would output the body content only if the property 'beanProperty' of bean 'someBean'
 * was equal to "one" or "two":
 * <p><code>
 * &lt;jag:variable name="myVariable" list="true"&gt;one&lt;/jag:variable&gt;<br>
 * &lt;jag:variable name="myVariable" list="true"&gt;two&lt;/jag:variable&gt;<br>
 * &lt;jag:equal name="someBean" property="beanProperty" variable="myVariable"&gt;<br>
 * ..body content..<br>
 * &lt;/jag:equal%gt;
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class VariableTag extends TagBodySupport {

   /** Field name */
   private String name = null;

   private String list;


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

   public String getList() {
      return list;
   }

   public void setList(String list) {
      this.list = list;
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
      return (EVAL_PAGE);
   }

   /**
    * A closing <code>&lt;/jag:variable&gt;</code> tag is processed twice; first time the body text contains the
    * <code>&lt;jag:variable&gt;</code> start and end tags, along with the stuff we want to evaluate - so we strip
    * away the <code>&lt;jag:variable&gt;</code> start and end tags and resubmit for processing.
    * The second time, the body text contains the evaluated body - so we put this on the PageSession and we're done.
    *
    * @return a return value from TagConstants.
    *
    * @throws JagException
    *
    */
   public int doAfterBodyTag() throws JagException {
      Object result;
      String body = getBodyText();
      int opentagPos = body.indexOf("jag:variable");
      int closetagPos = body.indexOf("/jag:variable");
      if (opentagPos != -1 && closetagPos != -1) {
         //the body text contains the jag:variable start and end tags, go reprocess!
         body = body.substring(opentagPos + 2, closetagPos - 1);
         setBodyText(body);
         return EVAL_BODY_TAG;

      } else if (opentagPos != -1 && closetagPos == -1) {
         //this is the case of something like <jag:variable name="check" />,
         //where there is no end tag and no body content to evaluate - so just set a token value.
         result = "true";

      } else {
         Set previous = null;
         if (isList()) {
            previous = ((Set) getPageContext().getAttribute(name));
            if (previous == null) {
               previous = new HashSet();
            }
            previous.add(body.trim());
         }

         if (previous == null) {
            result = body.trim();
         } else {
            result = previous;
         }
      }
      getPageContext().setAttribute(name, result);
      return SKIP_CLEAR_BODY;
   }

   private boolean isList() {
      return list != null && "true".equalsIgnoreCase(list.trim());
   }
}