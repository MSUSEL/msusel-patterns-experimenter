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


import java.util.*;

import com.finalist.jag.*;


/**
 * Class TemplateHeaderCollection
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateHeaderCollection {

   /** Field headerLines           */
   private HeaderLine[] headerLines = null;


   /**
    * Constructor TemplateHeaderCollection
    *
    *
    * @param headerLines
    *
    */
   public TemplateHeaderCollection(HeaderLine[] headerLines) {
      this.headerLines = headerLines;
   }


   /**
    * Method getHeaderUrl
    *
    *
    * @param s
    *
    * @return
    *
    * @throws JagException
    *
    */
   public UrlHeaderLine getHeaderUrl(String s) throws JagException {

      UrlHeaderLine line = findHeaderUrl(s);

      if (line == null) {
         throw new JagException(
            "Missing header definition for the taglibrary " + s);
      }

      return line;
   }


   /**
    * Method findHeaderUrl
    *
    *
    * @param s
    *
    * @return
    *
    */
   public UrlHeaderLine findHeaderUrl(String s) {

      for (int i = 0; i < headerLines.length; i++) {
         HeaderLine headerLine = headerLines[i];

         if (!(headerLine instanceof UrlHeaderLine)) {
            continue;
         }

         UrlHeaderLine urlHeader = (UrlHeaderLine) headerLine;

         if (s.equals(urlHeader.getPrefix())) {
            return urlHeader;
         }
      }

      return null;
   }
}

;