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

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * A couple of file-related utilities.
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class FileUtils {

   /**
    * Just calling File.delete() only works sporadically on my system.  It's a strange problem,
    * but after a bit of trial and error, here's a solution that works for me..
    *
    * @param file the file to be deleted.
    */
   public static void deleteFile(File file) {
      if (file.exists() && !file.delete()) {
         System.gc();
         if (!file.delete()) {
            try {
               Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            deleteFile(file);
         }
      }
   }

   /**
    * Creates a file and writes the specified content into it.
    *
    * @param file
    * @param content
    * @throws IOException
    */
   public static void createFile(File file, String content) throws IOException {
      file.getParentFile().mkdirs();
      FileWriter writer = new FileWriter(file);
      PrintWriter prwriter = new PrintWriter(writer);
      prwriter.print(content);
      prwriter.flush();
      prwriter.close();
      writer.close();
   }

}
