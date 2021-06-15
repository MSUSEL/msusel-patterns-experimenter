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
package org.apache.hadoop.fs.ftp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.hadoop.fs.FSInputStream;
import org.apache.hadoop.fs.FileSystem;

public class FTPInputStream extends FSInputStream {

  InputStream wrappedStream;
  FTPClient client;
  FileSystem.Statistics stats;
  boolean closed;
  long pos;

  public FTPInputStream(InputStream stream, FTPClient client,
      FileSystem.Statistics stats) {
    if (stream == null) {
      throw new IllegalArgumentException("Null InputStream");
    }
    if (client == null || !client.isConnected()) {
      throw new IllegalArgumentException("FTP client null or not connected");
    }
    this.wrappedStream = stream;
    this.client = client;
    this.stats = stats;
    this.pos = 0;
    this.closed = false;
  }

  public long getPos() throws IOException {
    return pos;
  }

  // We don't support seek.
  public void seek(long pos) throws IOException {
    throw new IOException("Seek not supported");
  }

  public boolean seekToNewSource(long targetPos) throws IOException {
    throw new IOException("Seek not supported");
  }

  public synchronized int read() throws IOException {
    if (closed) {
      throw new IOException("Stream closed");
    }

    int byteRead = wrappedStream.read();
    if (byteRead >= 0) {
      pos++;
    }
    if (stats != null & byteRead >= 0) {
      stats.incrementBytesRead(1);
    }
    return byteRead;
  }

  public synchronized int read(byte buf[], int off, int len) throws IOException {
    if (closed) {
      throw new IOException("Stream closed");
    }

    int result = wrappedStream.read(buf, off, len);
    if (result > 0) {
      pos += result;
    }
    if (stats != null & result > 0) {
      stats.incrementBytesRead(result);
    }

    return result;
  }

  public synchronized void close() throws IOException {
    if (closed) {
      throw new IOException("Stream closed");
    }
    super.close();
    closed = true;
    if (!client.isConnected()) {
      throw new FTPException("Client not connected");
    }

    boolean cmdCompleted = client.completePendingCommand();
    client.logout();
    client.disconnect();
    if (!cmdCompleted) {
      throw new FTPException("Could not complete transfer, Reply Code - "
          + client.getReplyCode());
    }
  }

  // Not supported.

  public boolean markSupported() {
    return false;
  }

  public void mark(int readLimit) {
    // Do nothing
  }

  public void reset() throws IOException {
    throw new IOException("Mark not supported");
  }
}
