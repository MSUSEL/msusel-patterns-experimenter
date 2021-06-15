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
package org.apache.hadoop.hdfs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.hadoop.fs.FSInputStream;
import org.apache.hadoop.hdfs.server.namenode.StreamFile;

/**
 * To support HTTP byte streams, a new connection to an HTTP server needs to be
 * created each time. This class hides the complexity of those multiple 
 * connections from the client. Whenever seek() is called, a new connection
 * is made on the successive read(). The normal input stream functions are 
 * connected to the currently active input stream. 
 */
public abstract class ByteRangeInputStream extends FSInputStream {
  
  /**
   * This class wraps a URL and provides method to open connection.
   * It can be overridden to change how a connection is opened.
   */
  public static abstract class URLOpener {
    protected URL url;
  
    public URLOpener(URL u) {
      url = u;
    }
  
    public void setURL(URL u) {
      url = u;
    }
  
    public URL getURL() {
      return url;
    }

    /** Connect to server with a data offset. */
    protected abstract HttpURLConnection connect(final long offset,
        final boolean resolved) throws IOException;
  }

  enum StreamStatus {
    NORMAL, SEEK
  }
  protected InputStream in;
  protected URLOpener originalURL;
  protected URLOpener resolvedURL;
  protected long startPos = 0;
  protected long currentPos = 0;
  protected long filelength;

  StreamStatus status = StreamStatus.SEEK;

  /**
   * Create with the specified URLOpeners. Original url is used to open the 
   * stream for the first time. Resolved url is used in subsequent requests.
   * @param o Original url
   * @param r Resolved url
   */
  public ByteRangeInputStream(URLOpener o, URLOpener r) {
    this.originalURL = o;
    this.resolvedURL = r;
  }
  
  protected abstract URL getResolvedUrl(final HttpURLConnection connection
      ) throws IOException;

  private InputStream getInputStream() throws IOException {
    if (status != StreamStatus.NORMAL) {
      
      if (in != null) {
        in.close();
        in = null;
      }
      
      // Use the original url if no resolved url exists, eg. if
      // it's the first time a request is made.
      final boolean resolved = resolvedURL.getURL() != null; 
      final URLOpener opener = resolved? resolvedURL: originalURL;

      final HttpURLConnection connection = opener.connect(startPos, resolved);
      final String cl = connection.getHeaderField(StreamFile.CONTENT_LENGTH);
      filelength = (cl == null) ? -1 : Long.parseLong(cl);
      in = connection.getInputStream();

      resolvedURL.setURL(getResolvedUrl(connection));
      status = StreamStatus.NORMAL;
    }
    
    return in;
  }
  
  private void update(final boolean isEOF, final int n)
      throws IOException {
    if (!isEOF) {
      currentPos += n;
    } else if (currentPos < filelength) {
      throw new IOException("Got EOF but currentPos = " + currentPos
          + " < filelength = " + filelength);
    }
  }

  public int read() throws IOException {
    final int b = getInputStream().read();
    update(b == -1, 1);
    return b;
  }
  
  /**
   * Seek to the given offset from the start of the file.
   * The next read() will be from that location.  Can't
   * seek past the end of the file.
   */
  public void seek(long pos) throws IOException {
    if (pos != currentPos) {
      startPos = pos;
      currentPos = pos;
      status = StreamStatus.SEEK;
    }
  }

  /**
   * Return the current offset from the start of the file
   */
  public long getPos() throws IOException {
    return currentPos;
  }

  /**
   * Seeks a different copy of the data.  Returns true if
   * found a new source, false otherwise.
   */
  public boolean seekToNewSource(long targetPos) throws IOException {
    return false;
  }
}