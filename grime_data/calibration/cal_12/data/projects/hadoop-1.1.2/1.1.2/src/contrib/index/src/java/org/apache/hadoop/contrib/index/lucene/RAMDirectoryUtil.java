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
package org.apache.hadoop.contrib.index.lucene;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.RAMDirectory;

/**
 * A utility class which writes an index in a ram dir into a DataOutput and
 * read from a DataInput an index into a ram dir.
 */
public class RAMDirectoryUtil {
  private static final int BUFFER_SIZE = 1024; // RAMOutputStream.BUFFER_SIZE;

  /**
   * Write a number of files from a ram directory to a data output.
   * @param out  the data output
   * @param dir  the ram directory
   * @param names  the names of the files to write
   * @throws IOException
   */
  public static void writeRAMFiles(DataOutput out, RAMDirectory dir,
      String[] names) throws IOException {
    out.writeInt(names.length);

    for (int i = 0; i < names.length; i++) {
      Text.writeString(out, names[i]);
      long length = dir.fileLength(names[i]);
      out.writeLong(length);

      if (length > 0) {
        // can we avoid the extra copy?
        IndexInput input = null;
        try {
          input = dir.openInput(names[i], BUFFER_SIZE);

          int position = 0;
          byte[] buffer = new byte[BUFFER_SIZE];

          while (position < length) {
            int len =
                position + BUFFER_SIZE <= length ? BUFFER_SIZE
                    : (int) (length - position);
            input.readBytes(buffer, 0, len);
            out.write(buffer, 0, len);
            position += len;
          }
        } finally {
          if (input != null) {
            input.close();
          }
        }
      }
    }
  }

  /**
   * Read a number of files from a data input to a ram directory.
   * @param in  the data input
   * @param dir  the ram directory
   * @throws IOException
   */
  public static void readRAMFiles(DataInput in, RAMDirectory dir)
      throws IOException {
    int numFiles = in.readInt();

    for (int i = 0; i < numFiles; i++) {
      String name = Text.readString(in);
      long length = in.readLong();

      if (length > 0) {
        // can we avoid the extra copy?
        IndexOutput output = null;
        try {
          output = dir.createOutput(name);

          int position = 0;
          byte[] buffer = new byte[BUFFER_SIZE];

          while (position < length) {
            int len =
                position + BUFFER_SIZE <= length ? BUFFER_SIZE
                    : (int) (length - position);
            in.readFully(buffer, 0, len);
            output.writeBytes(buffer, 0, len);
            position += len;
          }
        } finally {
          if (output != null) {
            output.close();
          }
        }
      }
    }
  }

}
