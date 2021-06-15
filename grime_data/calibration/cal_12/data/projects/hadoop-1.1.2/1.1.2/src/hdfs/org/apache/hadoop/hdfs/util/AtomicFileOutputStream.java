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
package org.apache.hadoop.hdfs.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.IOUtils;

/**
 * A FileOutputStream that has the property that it will only show
 * up at its destination once it has been entirely written and flushed
 * to disk. While being written, it will use a .tmp suffix.
 * 
 * When the output stream is closed, it is flushed, fsynced, and
 * will be moved into place, overwriting any file that already
 * exists at that location.
 * 
 * <b>NOTE</b>: on Windows platforms, it will not atomically
 * replace the target file - instead the target file is deleted
 * before this one is moved into place.
 */
public class AtomicFileOutputStream extends FilterOutputStream {

  private static final String TMP_EXTENSION = ".tmp";
  
  private final static Log LOG = LogFactory.getLog(
      AtomicFileOutputStream.class);
  
  private final File origFile;
  private final File tmpFile;
  
  public AtomicFileOutputStream(File f) throws FileNotFoundException {
    // Code unfortunately must be duplicated below since we can't assign anything
    // before calling super
    super(new FileOutputStream(new File(f.getParentFile(), f.getName() + TMP_EXTENSION)));
    origFile = f.getAbsoluteFile();
    tmpFile = new File(f.getParentFile(), f.getName() + TMP_EXTENSION).getAbsoluteFile();
  }

  @Override
  public void close() throws IOException {
    boolean triedToClose = false, success = false;
    try {
      flush();
      ((FileOutputStream)out).getChannel().force(true);

      triedToClose = true;
      super.close();
      success = true;
    } finally {
      if (success) {
        boolean renamed = tmpFile.renameTo(origFile);
        if (!renamed) {
          // On windows, renameTo does not replace.
          if (!origFile.delete() || !tmpFile.renameTo(origFile)) {
            throw new IOException("Could not rename temporary file " +
                tmpFile + " to " + origFile);
          }
        }
      } else {
        if (!triedToClose) {
          // If we failed when flushing, try to close it to not leak an FD
          IOUtils.closeStream(out);
        }
        // close wasn't successful, try to delete the tmp file
        if (!tmpFile.delete()) {
          LOG.warn("Unable to delete tmp file " + tmpFile);
        }
      }
    }
  }

}
