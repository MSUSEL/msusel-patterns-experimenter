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
package org.apache.hadoop.streaming;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

class UtilTest {

  /**
   * Utility routine to recurisvely delete a directory.
   * On normal return, the file does not exist.
   *
   * @param file File or directory to delete.
   *
   * @throws RuntimeException if the file, or some file within
   * it, could not be deleted.
   */
  static void recursiveDelete(File file) {
    file = file.getAbsoluteFile();

    if (!file.exists()) return;
    
    if (file.isDirectory()) {
      for (File child : file.listFiles()) {
	recursiveDelete(child);
      }
    }
    if (!file.delete()) {
      throw new RuntimeException("Failed to delete " + file);
    }
  }
  
  public UtilTest(String testName) {
    testName_ = testName;
    userDir_ = System.getProperty("user.dir");
    antTestDir_ = System.getProperty("test.build.data", userDir_);
    System.out.println("test.build.data-or-user.dir=" + antTestDir_);
  }

  void checkUserDir() {
    // trunk/src/contrib/streaming --> trunk/build/contrib/streaming/test/data
    if (!userDir_.equals(antTestDir_)) {
      // because changes to user.dir are ignored by File static methods.
      throw new IllegalStateException("user.dir != test.build.data. The junit Ant task must be forked.");
    }
  }

  void redirectIfAntJunit() throws IOException
  {
    boolean fromAntJunit = System.getProperty("test.build.data") != null;
    if (fromAntJunit) {
      new File(antTestDir_).mkdirs();
      File outFile = new File(antTestDir_, testName_+".log");
      PrintStream out = new PrintStream(new FileOutputStream(outFile));
      System.setOut(out);
      System.setErr(out);
    }
  }

  private String userDir_;
  private String antTestDir_;
  private String testName_;
}
