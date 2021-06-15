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

import java.util.*;


/**
 * Class TemplateHeaderFactory
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateHeaderFactory implements JagParserConstants {

   /** Field headers           */
   private TemplateHeaderCollection headers = null;


   /**
    * Method getHeaderCollection
    *
    *
    * @return
    *
    */
   public TemplateHeaderCollection getHeaderCollection() {
      return headers;
   }


   /**
    * Method create
    *
    *
    * @param jagblocks
    *
    * @return
    *
    */
   public TemplateHeaderCollection create(JagBlockCollection jagblocks) {

      headers = null;

      Collection headerLines = new ArrayList();
      JagBlock childBlock[] = {jagblocks.getFirstChild(), null, null};

      for (; childBlock[0] != null; childBlock[0] = childBlock[0].getNextSibling()) {
         if (childBlock[0].getType() != HEADERDEF_BEGIN) {
            continue;
         }

         Collection identList = new ArrayList();
         Collection paramList = new ArrayList();

         childBlock[1] = childBlock[0].getFirstChild();

         while (childBlock[1] != null) {
            if (childBlock[1].getType() == PARAMDEF) {
               String ident = null, value = null;

               childBlock[2] = childBlock[1].getFirstChild();

               while (childBlock[2] != null) {
                  if (childBlock[2].getType() == IDENT) {
                     ident = childBlock[2].getText();
                  }
                  else if (childBlock[2].getType() == STRING) {
                     value = childBlock[2].getText();
                  }

                  childBlock[2] = childBlock[2].getNextSibling();
               }

               if ((ident != null) && (value != null)) {
                  paramList.add(new JagParameter(ident, value));
               }
            }
            else if (childBlock[1].getType() == IDENT) {
               identList.add(childBlock[1].getText());
            }

            childBlock[1] = childBlock[1].getNextSibling();
         }

         String[] identArray = (String[]) identList.toArray(new String[identList.size()]);
         JagParameter[] paramArray = (JagParameter[]) paramList.toArray(new JagParameter[paramList.size()]);

         headerLines.add(create(identArray, paramArray));
      }

      headers = new TemplateHeaderCollection((HeaderLine[]) headerLines
         .toArray(new HeaderLine[headerLines.size()]));

      return headers;
   }


   /**
    * Method create
    *
    *
    * @param identArray
    * @param paramArray
    *
    * @return
    *
    */
   protected HeaderLine create(String[] identArray,
      JagParameter[] paramArray) {

      HeaderLine headerLine = null;

      for (int i = 0; i < identArray.length; i++) {
         String ident = identArray[i];

         if (ident.equals("taglib")) {
            headerLine = createUrlHeaderLine(identArray, paramArray);

            if (headerLine != null) {
               return headerLine;
            }
         }
      }

      if (headerLine == null) {
         headerLine = new HeaderLine(identArray, paramArray);
      }

      return headerLine;
   }


   /**
    * Method createUrlHeaderLine
    *
    *
    * @param identArray
    * @param paramArray
    *
    * @return
    *
    */
   protected HeaderLine createUrlHeaderLine(String[] identArray,
      JagParameter[] paramArray) {

      // assert(identArray[0].equals("taglib"));
      String url = null;
      String prefix = null;

      for (int i = 0; i < paramArray.length; i++) {
         if ((url == null) && paramArray[i].getIdent().equals("uri")) {
            url = paramArray[i].getValue();
         }
         else if ((prefix == null) && paramArray[i].getIdent().equals("prefix")) {
            prefix = paramArray[i].getValue();
         }
      }

      return ((url != null) && (prefix != null))
         ? new UrlHeaderLine(identArray, paramArray, url, prefix)
         : null;
   }
}