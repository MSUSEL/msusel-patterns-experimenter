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

import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.ReflectionUtils;
import java.util.HashMap;

/** Factories for non-public writables.  Defining a factory permits {@link
 * ObjectWritable} to be able to construct instances of non-public classes. */
public class WritableFactories {
  private static final HashMap<Class, WritableFactory> CLASS_TO_FACTORY =
    new HashMap<Class, WritableFactory>();

  private WritableFactories() {}                  // singleton

  /** Define a factory for a class. */
  public static synchronized void setFactory(Class c, WritableFactory factory) {
    CLASS_TO_FACTORY.put(c, factory);
  }

  /** Define a factory for a class. */
  public static synchronized WritableFactory getFactory(Class c) {
    return CLASS_TO_FACTORY.get(c);
  }

  /** Create a new instance of a class with a defined factory. */
  public static Writable newInstance(Class<? extends Writable> c, Configuration conf) {
    WritableFactory factory = WritableFactories.getFactory(c);
    if (factory != null) {
      Writable result = factory.newInstance();
      if (result instanceof Configurable) {
        ((Configurable) result).setConf(conf);
      }
      return result;
    } else {
      return ReflectionUtils.newInstance(c, conf);
    }
  }
  
  /** Create a new instance of a class with a defined factory. */
  public static Writable newInstance(Class<? extends Writable> c) {
    return newInstance(c, null);
  }

}

