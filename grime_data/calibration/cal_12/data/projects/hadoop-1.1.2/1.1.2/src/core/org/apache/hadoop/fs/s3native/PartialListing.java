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
package org.apache.hadoop.fs.s3native;

/**
 * <p>
 * Holds information on a directory listing for a
 * {@link NativeFileSystemStore}.
 * This includes the {@link FileMetadata files} and directories
 * (their names) contained in a directory.
 * </p>
 * <p>
 * This listing may be returned in chunks, so a <code>priorLastKey</code>
 * is provided so that the next chunk may be requested.
 * </p>
 * @see NativeFileSystemStore#list(String, int, String)
 */
class PartialListing {
  
  private final String priorLastKey;
  private final FileMetadata[] files;
  private final String[] commonPrefixes;
  
  public PartialListing(String priorLastKey, FileMetadata[] files,
      String[] commonPrefixes) {
    this.priorLastKey = priorLastKey;
    this.files = files;
    this.commonPrefixes = commonPrefixes;
  }

  public FileMetadata[] getFiles() {
    return files;
  }

  public String[] getCommonPrefixes() {
    return commonPrefixes;
  }

  public String getPriorLastKey() {
    return priorLastKey;
  }
  
}
