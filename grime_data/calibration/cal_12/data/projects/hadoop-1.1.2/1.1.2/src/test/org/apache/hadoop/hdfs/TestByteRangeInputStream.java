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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class TestByteRangeInputStream {
public static class MockHttpURLConnection extends HttpURLConnection {
  public MockHttpURLConnection(URL u) {
    super(u);
  }
  
  @Override
  public boolean usingProxy(){
    return false;
  }
  
  @Override
  public void disconnect() {
  }
  
  @Override
  public void connect() {
  }
  
  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream("asdf".getBytes());
  } 

  @Override
  public URL getURL() {
    URL u = null;
    try {
      u = new URL("http://resolvedurl/");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return u;
  }
  
  @Override
  public int getResponseCode() {
    if (responseCode != -1) {
      return responseCode;
    } else {
      if (getRequestProperty("Range") == null) {
        return 200;
      } else {
        return 206;
      }
    }
  }

  public void setResponseCode(int resCode) {
    responseCode = resCode;
  }
}

  @Test
  public void testByteRange() throws IOException {
  }
}
