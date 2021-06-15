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
package org.apache.hadoop.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.nio.channels.Pipe;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

/**
 * This tests timout out from SocketInputStream and
 * SocketOutputStream using pipes.
 * 
 * Normal read and write using these streams are tested by pretty much
 * every DFS unit test.
 */
public class TestSocketIOWithTimeout extends TestCase {

  static Log LOG = LogFactory.getLog(TestSocketIOWithTimeout.class);
  
  private static int TIMEOUT = 1*1000; 
  private static String TEST_STRING = "1234567890";
  
  private void doIO(InputStream in, OutputStream out) throws IOException {
    /* Keep on writing or reading until we get SocketTimeoutException.
     * It expects this exception to occur within 100 millis of TIMEOUT.
     */
    byte buf[] = new byte[4192];
    
    while (true) {
      long start = System.currentTimeMillis();
      try {
        if (in != null) {
          in.read(buf);
        } else {
          out.write(buf);
        }
      } catch (SocketTimeoutException e) {
        long diff = System.currentTimeMillis() - start;
        LOG.info("Got SocketTimeoutException as expected after " + 
                 diff + " millis : " + e.getMessage());
        assertTrue(Math.abs(TIMEOUT - diff) <= 200);
        break;
      }
    }
  }
  
  /**
   * Just reads one byte from the input stream.
   */
  static class ReadRunnable implements Runnable {
    private InputStream in;

    public ReadRunnable(InputStream in) {
      this.in = in;
    }
    public void run() {
      try {
        in.read();
      } catch (IOException e) {
        LOG.info("Got expection while reading as expected : " + 
                 e.getMessage());
        return;
      }
      assertTrue(false);
    }
  }
  
  public void testSocketIOWithTimeout() throws IOException {
    
    // first open pipe:
    Pipe pipe = Pipe.open();
    Pipe.SourceChannel source = pipe.source();
    Pipe.SinkChannel sink = pipe.sink();
    
    try {
      InputStream in = new SocketInputStream(source, TIMEOUT);
      OutputStream out = new SocketOutputStream(sink, TIMEOUT);
      
      byte[] writeBytes = TEST_STRING.getBytes();
      byte[] readBytes = new byte[writeBytes.length];
      
      out.write(writeBytes);
      doIO(null, out);
      
      in.read(readBytes);
      assertTrue(Arrays.equals(writeBytes, readBytes));
      doIO(in, null);
      
      /*
       * Verify that it handles interrupted threads properly.
       * Use a large timeout and expect the thread to return quickly.
       */
      in = new SocketInputStream(source, 0);
      Thread thread = new Thread(new ReadRunnable(in));
      thread.start();
      
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ignored) {}
      
      thread.interrupt();
      
      try {
        thread.join();
      } catch (InterruptedException e) {
        throw new IOException("Unexpected InterruptedException : " + e);
      }
      
      //make sure the channels are still open
      assertTrue(source.isOpen());
      assertTrue(sink.isOpen());

      out.close();
      assertFalse(sink.isOpen());
      
      // close sink and expect -1 from source.read()
      assertEquals(-1, in.read());
      
      // make sure close() closes the underlying channel.
      in.close();
      assertFalse(source.isOpen());
      
    } finally {
      if (source != null) {
        source.close();
      }
      if (sink != null) {
        sink.close();
      }
    }
  }
}
