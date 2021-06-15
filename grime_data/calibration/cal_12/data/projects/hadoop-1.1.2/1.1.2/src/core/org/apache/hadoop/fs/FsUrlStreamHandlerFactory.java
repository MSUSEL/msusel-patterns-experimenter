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

import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;

/**
 * Factory for URL stream handlers.
 * 
 * There is only one handler whose job is to create UrlConnections. A
 * FsUrlConnection relies on FileSystem to choose the appropriate FS
 * implementation.
 * 
 * Before returning our handler, we make sure that FileSystem knows an
 * implementation for the requested scheme/protocol.
 */
public class FsUrlStreamHandlerFactory implements
    URLStreamHandlerFactory {

  // The configuration holds supported FS implementation class names.
  private Configuration conf;

  // This map stores whether a protocol is know or not by FileSystem
  private Map<String, Boolean> protocols = new HashMap<String, Boolean>();

  // The URL Stream handler
  private java.net.URLStreamHandler handler;

  public FsUrlStreamHandlerFactory() {
    this.conf = new Configuration();
    // force the resolution of the configuration files
    // this is required if we want the factory to be able to handle
    // file:// URLs
    this.conf.getClass("fs.file.impl", null);
    this.handler = new FsUrlStreamHandler(this.conf);
  }

  public FsUrlStreamHandlerFactory(Configuration conf) {
    this.conf = new Configuration(conf);
    // force the resolution of the configuration files
    this.conf.getClass("fs.file.impl", null);
    this.handler = new FsUrlStreamHandler(this.conf);
  }

  public java.net.URLStreamHandler createURLStreamHandler(String protocol) {
    if (!protocols.containsKey(protocol)) {
      boolean known =
          (conf.getClass("fs." + protocol + ".impl", null) != null);
      protocols.put(protocol, known);
    }
    if (protocols.get(protocol)) {
      return handler;
    } else {
      // FileSystem does not know the protocol, let the VM handle this
      return null;
    }
  }

}
