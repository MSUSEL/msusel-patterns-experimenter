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
package org.apache.hadoop.record.compiler;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Container for the Hadoop Record DDL.
 * The main components of the file are filename, list of included files,
 * and records defined in that file.
 */
public class JFile {
  /** Possibly full name of the file */
  private String mName;
  /** Ordered list of included files */
  private ArrayList<JFile> mInclFiles;
  /** Ordered list of records declared in this file */
  private ArrayList<JRecord> mRecords;
    
  /** Creates a new instance of JFile
   *
   * @param name possibly full pathname to the file
   * @param inclFiles included files (as JFile)
   * @param recList List of records defined within this file
   */
  public JFile(String name, ArrayList<JFile> inclFiles,
               ArrayList<JRecord> recList) {
    mName = name;
    mInclFiles = inclFiles;
    mRecords = recList;
  }
    
  /** Strip the other pathname components and return the basename */
  String getName() {
    int idx = mName.lastIndexOf('/');
    return (idx > 0) ? mName.substring(idx) : mName; 
  }
    
  /** Generate record code in given language. Language should be all
   *  lowercase.
   */
  public int genCode(String language, String destDir, ArrayList<String> options)
    throws IOException {
    CodeGenerator gen = CodeGenerator.get(language);
    if (gen != null) {
      gen.genCode(mName, mInclFiles, mRecords, destDir, options);
    } else {
      System.err.println("Cannnot recognize language:"+language);
      return 1;
    }
    return 0;
  }
}
