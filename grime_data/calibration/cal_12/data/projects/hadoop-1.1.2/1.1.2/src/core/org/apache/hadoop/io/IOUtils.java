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
package org.apache.hadoop.io;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.logging.Log;

import org.apache.hadoop.conf.Configuration;

/**
 * An utility class for I/O related functionality. 
 */
public class IOUtils {

  /**
   * Copies from one stream to another.
   * @param in InputStrem to read from
   * @param out OutputStream to write to
   * @param buffSize the size of the buffer 
   * @param close whether or not close the InputStream and 
   * OutputStream at the end. The streams are closed in the finally clause.  
   */
  public static void copyBytes(InputStream in, OutputStream out, int buffSize, boolean close) 
    throws IOException {

    try {
      copyBytes(in, out, buffSize);
    } finally {
      if(close) {
        out.close();
        in.close();
      }
    }
  }
  
  /**
   * Copies from one stream to another.
   * 
   * @param in InputStrem to read from
   * @param out OutputStream to write to
   * @param buffSize the size of the buffer 
   */
  public static void copyBytes(InputStream in, OutputStream out, int buffSize) 
    throws IOException {

    PrintStream ps = out instanceof PrintStream ? (PrintStream)out : null;
    byte buf[] = new byte[buffSize];
    int bytesRead = in.read(buf);
    while (bytesRead >= 0) {
      out.write(buf, 0, bytesRead);
      if ((ps != null) && ps.checkError()) {
        throw new IOException("Unable to write to output stream.");
      }
      bytesRead = in.read(buf);
    }
  }

  /**
   * Copies from one stream to another. <strong>closes the input and output streams 
   * at the end</strong>.
   * @param in InputStrem to read from
   * @param out OutputStream to write to
   * @param conf the Configuration object 
   */
  public static void copyBytes(InputStream in, OutputStream out, Configuration conf)
    throws IOException {
    copyBytes(in, out, conf.getInt("io.file.buffer.size", 4096), true);
  }
  
  /**
   * Copies from one stream to another.
   * @param in InputStrem to read from
   * @param out OutputStream to write to
   * @param conf the Configuration object
   * @param close whether or not close the InputStream and 
   * OutputStream at the end. The streams are closed in the finally clause.
   */
  public static void copyBytes(InputStream in, OutputStream out, Configuration conf, boolean close)
    throws IOException {
    copyBytes(in, out, conf.getInt("io.file.buffer.size", 4096),  close);
  }
  
  /**
   * Copies the specified length of bytes from in to out.
   *
   * @param in InputStream to read from
   * @param out OutputStream to write to
   * @param length number of bytes to copy
   * @param bufferSize the size of the buffer 
   * @param close whether to close the streams
   * @throws IOException if bytes can not be read or written
   */
  public static void copyBytes(InputStream in, OutputStream out,
      final long length, final int bufferSize, final boolean close
      ) throws IOException {
    final byte buf[] = new byte[bufferSize];
    try {
      int n = 0;
      for(long remaining = length; remaining > 0 && n != -1; remaining -= n) {
        final int toRead = remaining < buf.length? (int)remaining : buf.length;
        n = in.read(buf, 0, toRead);
        if (n > 0) {
          out.write(buf, 0, n);
        }
      }

      if (close) {
        out.close();
        out = null;
        in.close();
        in = null;
      }
    } finally {
      if (close) {
        closeStream(out);
        closeStream(in);
      }
    }
  }

  /** Reads len bytes in a loop.
   * @param in The InputStream to read from
   * @param buf The buffer to fill
   * @param off offset from the buffer
   * @param len the length of bytes to read
   * @throws IOException if it could not read requested number of bytes 
   * for any reason (including EOF)
   */
  public static void readFully( InputStream in, byte buf[],
      int off, int len ) throws IOException {
    int toRead = len;
    while ( toRead > 0 ) {
      int ret = in.read( buf, off, toRead );
      if ( ret < 0 ) {
        throw new IOException( "Premature EOF from inputStream");
      }
      toRead -= ret;
      off += ret;
    }
  }

  /** Reads len bytes in a loop using the channel of the stream
   * @param fileChannel a FileChannel to read len bytes into buf
   * @param buf The buffer to fill
   * @param off offset from the buffer
   * @param len the length of bytes to read
   * @throws IOException if it could not read requested number of bytes 
   * for any reason (including EOF)
   */
  public static void readFileChannelFully( FileChannel fileChannel, byte buf[],
      int off, int len ) throws IOException {
    int toRead = len;
    ByteBuffer byteBuffer = ByteBuffer.wrap(buf, off, len);
    while ( toRead > 0 ) {
      int ret = fileChannel.read(byteBuffer);
      if ( ret < 0 ) {
        throw new IOException( "Premeture EOF from inputStream");
      }
      toRead -= ret;
      off += ret;
    }
  }
  
  /** Similar to readFully(). Skips bytes in a loop.
   * @param in The InputStream to skip bytes from
   * @param len number of bytes to skip.
   * @throws IOException if it could not skip requested number of bytes 
   * for any reason (including EOF)
   */
  public static void skipFully( InputStream in, long len ) throws IOException {
    while ( len > 0 ) {
      long ret = in.skip( len );
      if ( ret < 0 ) {
        throw new IOException( "Premature EOF from inputStream");
      }
      len -= ret;
    }
  }
  
  /**
   * Close the Closeable objects and <b>ignore</b> any {@link IOException} or 
   * null pointers. Must only be used for cleanup in exception handlers.
   * @param log the log to record problems to at debug level. Can be null.
   * @param closeables the objects to close
   */
  public static void cleanup(Log log, java.io.Closeable... closeables) {
    for(java.io.Closeable c : closeables) {
      if (c != null) {
        try {
          c.close();
        } catch(IOException e) {
          if (log != null && log.isDebugEnabled()) {
            log.debug("Exception in closing " + c, e);
          }
        }
      }
    }
  }

  /**
   * Closes the stream ignoring {@link IOException}.
   * Must only be called in cleaning up from exception handlers.
   * @param stream the Stream to close
   */
  public static void closeStream( java.io.Closeable stream ) {
    cleanup(null, stream);
  }
  
  /**
   * Closes the socket ignoring {@link IOException} 
   * @param sock the Socket to close
   */
  public static void closeSocket( Socket sock ) {
    // avoids try { close() } dance
    if ( sock != null ) {
      try {
       sock.close();
      } catch ( IOException ignored ) {
      }
    }
  }
  
  /** /dev/null of OutputStreams.
   */
  public static class NullOutputStream extends OutputStream {
    public void write(byte[] b, int off, int len) throws IOException {
    }

    public void write(int b) throws IOException {
    }
  }  
}
