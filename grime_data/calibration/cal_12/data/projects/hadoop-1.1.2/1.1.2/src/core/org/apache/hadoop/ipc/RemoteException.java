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
package org.apache.hadoop.ipc;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.xml.sax.Attributes;

public class RemoteException extends IOException {
  /** For java.io.Serializable */
  private static final long serialVersionUID = 1L;

  private String className;
  
  public RemoteException(String className, String msg) {
    super(msg);
    this.className = className;
  }
  
  public String getClassName() {
    return className;
  }

  /**
   * If this remote exception wraps up one of the lookupTypes
   * then return this exception.
   * <p>
   * Unwraps any IOException.
   * 
   * @param lookupTypes the desired exception class.
   * @return IOException, which is either the lookupClass exception or this.
   */
  public IOException unwrapRemoteException(Class<?>... lookupTypes) {
    if(lookupTypes == null)
      return this;
    for(Class<?> lookupClass : lookupTypes) {
      if(!lookupClass.getName().equals(getClassName()))
        continue;
      try {
        return instantiateException(lookupClass.asSubclass(IOException.class));
      } catch(Exception e) {
        // cannot instantiate lookupClass, just return this
        return this;
      }
    }
    // wrapped up exception is not in lookupTypes, just return this
    return this;
  }

  /**
   * Instantiate and return the exception wrapped up by this remote exception.
   * 
   * <p> This unwraps any <code>Throwable</code> that has a constructor taking
   * a <code>String</code> as a parameter.
   * Otherwise it returns this.
   * 
   * @return <code>Throwable
   */
  public IOException unwrapRemoteException() {
    try {
      Class<?> realClass = Class.forName(getClassName());
      return instantiateException(realClass.asSubclass(IOException.class));
    } catch(Exception e) {
      // cannot instantiate the original exception, just return this
    }
    return this;
  }

  private IOException instantiateException(Class<? extends IOException> cls)
      throws Exception {
    Constructor<? extends IOException> cn = cls.getConstructor(String.class);
    cn.setAccessible(true);
    String firstLine = this.getMessage();
    int eol = firstLine.indexOf('\n');
    if (eol>=0) {
      firstLine = firstLine.substring(0, eol);
    }
    IOException ex = cn.newInstance(firstLine);
    ex.initCause(this);
    return ex;
  }

  /** Create RemoteException from attributes */
  public static RemoteException valueOf(Attributes attrs) {
    return new RemoteException(attrs.getValue("class"),
        attrs.getValue("message")); 
  }
}
