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
package org.apache.hadoop.fs.s3;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 * A facility for storing and retrieving {@link INode}s and {@link Block}s.
 */
public interface FileSystemStore {
  
  void initialize(URI uri, Configuration conf) throws IOException;
  String getVersion() throws IOException;

  void storeINode(Path path, INode inode) throws IOException;
  void storeBlock(Block block, File file) throws IOException;
  
  boolean inodeExists(Path path) throws IOException;
  boolean blockExists(long blockId) throws IOException;

  INode retrieveINode(Path path) throws IOException;
  File retrieveBlock(Block block, long byteRangeStart) throws IOException;

  void deleteINode(Path path) throws IOException;
  void deleteBlock(Block block) throws IOException;

  Set<Path> listSubPaths(Path path) throws IOException;
  Set<Path> listDeepSubPaths(Path path) throws IOException;

  /**
   * Delete everything. Used for testing.
   * @throws IOException
   */
  void purge() throws IOException;
  
  /**
   * Diagnostic method to dump all INodes to the console.
   * @throws IOException
   */
  void dump() throws IOException;
}
