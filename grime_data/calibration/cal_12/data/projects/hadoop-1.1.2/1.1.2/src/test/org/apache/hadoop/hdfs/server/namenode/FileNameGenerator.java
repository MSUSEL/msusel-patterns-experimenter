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
package org.apache.hadoop.hdfs.server.namenode;

import java.util.Arrays;

/**
 * File name generator.
 * 
 * Each directory contains not more than a fixed number (filesPerDir) 
 * of files and directories.
 * When the number of files in one directory reaches the maximum,
 * the generator creates a new directory and proceeds generating files in it.
 * The generated namespace tree is balanced that is any path to a leaf
 * file is not less than the height of the tree minus one.
 */
public class FileNameGenerator {
  private static final int DEFAULT_FILES_PER_DIRECTORY = 32;
  
  private int[] pathIndecies = new int[20]; // this will support up to 32**20 = 2**100 = 10**30 files
  private String baseDir;
  private String currentDir;
  private int filesPerDirectory;
  private long fileCount;

  FileNameGenerator(String baseDir) {
    this(baseDir, DEFAULT_FILES_PER_DIRECTORY);
  }
  
  FileNameGenerator(String baseDir, int filesPerDir) {
    this.baseDir = baseDir;
    this.filesPerDirectory = filesPerDir;
    reset();
  }

  String getNextDirName(String prefix) {
    int depth = 0;
    while(pathIndecies[depth] >= 0)
      depth++;
    int level;
    for(level = depth-1; 
        level >= 0 && pathIndecies[level] == filesPerDirectory-1; level--)
      pathIndecies[level] = 0;
    if(level < 0)
      pathIndecies[depth] = 0;
    else
      pathIndecies[level]++;
    level = 0;
    String next = baseDir;
    while(pathIndecies[level] >= 0)
      next = next + "/" + prefix + pathIndecies[level++];
    return next; 
  }

  synchronized String getNextFileName(String fileNamePrefix) {
    long fNum = fileCount % filesPerDirectory;
    if(fNum == 0) {
      currentDir = getNextDirName(fileNamePrefix + "Dir");
    }
    String fn = currentDir + "/" + fileNamePrefix + fileCount;
    fileCount++;
    return fn;
  }

  private synchronized void reset() {
    Arrays.fill(pathIndecies, -1);
    fileCount = 0L;
    currentDir = "";
  }

  synchronized int getFilesPerDirectory() {
    return filesPerDirectory;
  }

  synchronized String getCurrentDir() {
    return currentDir;
  }
}
