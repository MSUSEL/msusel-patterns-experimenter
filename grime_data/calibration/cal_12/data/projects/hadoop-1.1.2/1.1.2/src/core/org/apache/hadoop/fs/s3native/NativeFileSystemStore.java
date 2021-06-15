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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;

/**
 * <p>
 * An abstraction for a key-based {@link File} store.
 * </p>
 */
interface NativeFileSystemStore {
  
  void initialize(URI uri, Configuration conf) throws IOException;
  
  void storeFile(String key, File file, byte[] md5Hash) throws IOException;
  void storeEmptyFile(String key) throws IOException;
  
  FileMetadata retrieveMetadata(String key) throws IOException;
  InputStream retrieve(String key) throws IOException;
  InputStream retrieve(String key, long byteRangeStart) throws IOException;
  
  PartialListing list(String prefix, int maxListingLength) throws IOException;
  PartialListing list(String prefix, int maxListingLength, String priorLastKey, boolean recursive)
    throws IOException;
  
  void delete(String key) throws IOException;

  void copy(String srcKey, String dstKey) throws IOException;
  
  /**
   * Delete all keys with the given prefix. Used for testing.
   * @throws IOException
   */
  void purge(String prefix) throws IOException;
  
  /**
   * Diagnostic method to dump state to the console.
   * @throws IOException
   */
  void dump() throws IOException;
}
