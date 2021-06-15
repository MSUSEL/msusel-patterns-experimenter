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
package com.finalist.jag.util;

import java.io.*;
import java.util.*;


/**
 * Class DirectoryIterator
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class DirectoryIterator {

   /** Field rootDirectoryFile           */
   protected File rootDirectoryFile = null;

   /** Field currentDirecory           */
   protected File currentDirectory = null;

   /** Field bRecursive           */
   protected boolean bRecursive = true;

   /** Field bRecursive           */
   protected boolean bIncludeDirs = false;

   /** Field currInterator           */
   protected DirectoryIterator currInterator = null;

   /** Field currentFiles           */
   protected File[] currentFiles = null;

   /** Field nCurrentFileIndex           */
   protected int nCurrentFileIndex = 0;


   /**
    * Constructor DirectoryIterator
    *
    *
    * @param sRootpath
    *
    */
   public DirectoryIterator(String sRootpath) {
      this(sRootpath, true);
   }


   /**
    * Constructor DirectoryIterator
    *
    *
    * @param sRootpath
    * @param bRecursive
    *
    */
   public DirectoryIterator(String sRootpath, boolean bRecursive) {
      this(sRootpath, true, false);
   }


   public DirectoryIterator(String sRootpath, boolean bRecursive, boolean bIncludeDirs) {
      rootDirectoryFile = new File(sRootpath);
      this.bRecursive = bRecursive;
      this.bIncludeDirs = bIncludeDirs;
   }


   /**
    * Method getNext
    *
    *
    * @return
    *
    */
   public File getNext() {

      if (currInterator != null) {
         File file = null;
         if ((file = currInterator.getNext()) != null) {
            return file;
         }
      }

      if ((currentFiles != null) && (nCurrentFileIndex < currentFiles.length)) {
         File file = currentFiles[nCurrentFileIndex];
         if (file.isDirectory() && currentDirectory != file) {
            currentDirectory = file;
            return file;
         }
         nCurrentFileIndex++;
         if (file.isDirectory()) {
            if (file.compareTo(rootDirectoryFile) != 0) {
               currInterator = new DirectoryIterator(file.getPath(), bRecursive);
            }
            return getNext();
         }
         return file;
      }
      else if (currentFiles != null) {
         return null;
      }
      currentFiles = rootDirectoryFile.listFiles();
      nCurrentFileIndex = 0;

      return (currentFiles != null) ? getNext() : null;
   }
}

;
