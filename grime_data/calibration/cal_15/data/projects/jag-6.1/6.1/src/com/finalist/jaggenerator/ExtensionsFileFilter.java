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
package com.finalist.jaggenerator;

import javax.swing.filechooser.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A FileFilter that accepts/rejects files depending on their extension (part of filename following the last 'dot').
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class ExtensionsFileFilter extends FileFilter {

   private static final char DOT = '.';
   private final ArrayList acceptFilter = new ArrayList();
   private String description;

   /**
    * Creates a filter with one extension.
    * @param acceptableExtension
    */
   public ExtensionsFileFilter(String acceptableExtension) {
      acceptFilter.add(acceptableExtension);
      description = "*." + acceptableExtension;
   }


   /**
    * Creates a filter with possibly more than one extension.
    * @param acceptableExtensions
    */
   public ExtensionsFileFilter(String[] acceptableExtensions) {
      acceptFilter.addAll(Arrays.asList(acceptableExtensions));

      StringBuffer sb = new StringBuffer();
      Iterator i = acceptFilter.iterator();
      while (i.hasNext()) {
         String extension = (String) i.next();
         sb.append("*.");
         sb.append(extension);
         if (i.hasNext()) {
            sb.append(", ");
         }
      }
      description = sb.toString();
   }

   /** @see {@link FileFilter#accept}. */
   public boolean accept(File file) {
      if (file.isDirectory()) return true;
      String filename = file.toString().toLowerCase();
      int lastDotPos = filename.lastIndexOf(DOT) + 1;
      if (lastDotPos != 0) {
         String extension = filename.substring(lastDotPos);
         return acceptFilter.contains(extension);
      }

      return false; //maybe this should be true..?  if a file has no extension, we don't know what it is...
   }

   /** @see {@link FileFilter#getDescription}. */
   public String getDescription() {
      return description;
   }
}