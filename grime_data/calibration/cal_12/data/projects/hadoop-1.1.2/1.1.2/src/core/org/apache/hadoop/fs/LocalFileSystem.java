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
package org.apache.hadoop.fs;

import java.io.*;
import java.net.URI;
import java.util.*;

/****************************************************************
 * Implement the FileSystem API for the checksumed local filesystem.
 *
 *****************************************************************/
public class LocalFileSystem extends ChecksumFileSystem {
  static final URI NAME = URI.create("file:///");
  static private Random rand = new Random();
  FileSystem rfs;
  
  public LocalFileSystem() {
    this(new RawLocalFileSystem());
  }
  
  public FileSystem getRaw() {
    return rfs;
  }
    
  public LocalFileSystem(FileSystem rawLocalFileSystem) {
    super(rawLocalFileSystem);
    rfs = rawLocalFileSystem;
  }
  
  /**
   * Get file status.
   */
  public boolean exists(Path f) throws IOException {
    File path = pathToFile(f);
    if (path.exists()) {
      return true;
    } else {
      return false;
    }
  }
    
  /** Convert a path to a File. */
  public File pathToFile(Path path) {
    return ((RawLocalFileSystem)fs).pathToFile(path);
  }

  @Override
  public void copyFromLocalFile(boolean delSrc, Path src, Path dst)
    throws IOException {
    FileUtil.copy(this, src, this, dst, delSrc, getConf());
  }

  @Override
  public void copyToLocalFile(boolean delSrc, Path src, Path dst)
    throws IOException {
    FileUtil.copy(this, src, this, dst, delSrc, getConf());
  }

  /**
   * Moves files to a bad file directory on the same device, so that their
   * storage will not be reused.
   */
  public boolean reportChecksumFailure(Path p, FSDataInputStream in,
                                       long inPos,
                                       FSDataInputStream sums, long sumsPos) {
    try {
      // canonicalize f
      File f = ((RawLocalFileSystem)fs).pathToFile(p).getCanonicalFile();
      
      // find highest writable parent dir of f on the same device
      String device = new DF(f, getConf()).getMount();
      File parent = f.getParentFile();
      File dir = null;
      while (parent!=null && parent.canWrite() && parent.toString().startsWith(device)) {
        dir = parent;
        parent = parent.getParentFile();
      }

      if (dir==null) {
        throw new IOException(
                              "not able to find the highest writable parent dir");
      }
        
      // move the file there
      File badDir = new File(dir, "bad_files");
      if (!badDir.mkdirs()) {
        if (!badDir.isDirectory()) {
          throw new IOException("Mkdirs failed to create " + badDir.toString());
        }
      }
      String suffix = "." + rand.nextInt();
      File badFile = new File(badDir, f.getName()+suffix);
      LOG.warn("Moving bad file " + f + " to " + badFile);
      in.close();                               // close it first
      f.renameTo(badFile);                      // rename it

      // move checksum file too
      File checkFile = ((RawLocalFileSystem)fs).pathToFile(getChecksumFile(p));
      checkFile.renameTo(new File(badDir, checkFile.getName()+suffix));

    } catch (IOException e) {
      LOG.warn("Error moving bad file " + p + ": " + e);
    }
    return false;
  }
}
