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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * A wrapper for Writable instances.
 * <p>
 * When two sequence files, which have same Key type but different Value
 * types, are mapped out to reduce, multiple Value types is not allowed.
 * In this case, this class can help you wrap instances with different types.
 * </p>
 * 
 * <p>
 * Compared with <code>ObjectWritable</code>, this class is much more effective,
 * because <code>ObjectWritable</code> will append the class declaration as a String 
 * into the output file in every Key-Value pair.
 * </p>
 * 
 * <p>
 * Generic Writable implements {@link Configurable} interface, so that it will be 
 * configured by the framework. The configuration is passed to the wrapped objects
 * implementing {@link Configurable} interface <i>before deserialization</i>. 
 * </p>
 * 
 * how to use it: <br>
 * 1. Write your own class, such as GenericObject, which extends GenericWritable.<br> 
 * 2. Implements the abstract method <code>getTypes()</code>, defines 
 *    the classes which will be wrapped in GenericObject in application.
 *    Attention: this classes defined in <code>getTypes()</code> method, must
 *    implement <code>Writable</code> interface.
 * <br><br>
 * 
 * The code looks like this:
 * <blockquote><pre>
 * public class GenericObject extends GenericWritable {
 * 
 *   private static Class[] CLASSES = {
 *               ClassType1.class, 
 *               ClassType2.class,
 *               ClassType3.class,
 *               };
 *
 *   protected Class[] getTypes() {
 *       return CLASSES;
 *   }
 *
 * }
 * </pre></blockquote>
 * 
 * @since Nov 8, 2006
 */
public abstract class GenericWritable implements Writable, Configurable {

  private static final byte NOT_SET = -1;

  private byte type = NOT_SET;

  private Writable instance;

  private Configuration conf = null;
  
  /**
   * Set the instance that is wrapped.
   * 
   * @param obj
   */
  public void set(Writable obj) {
    instance = obj;
    Class<? extends Writable> instanceClazz = instance.getClass();
    Class<? extends Writable>[] clazzes = getTypes();
    for (int i = 0; i < clazzes.length; i++) {
      Class<? extends Writable> clazz = clazzes[i];
      if (clazz.equals(instanceClazz)) {
        type = (byte) i;
        return;
      }
    }
    throw new RuntimeException("The type of instance is: "
                               + instance.getClass() + ", which is NOT registered.");
  }

  /**
   * Return the wrapped instance.
   */
  public Writable get() {
    return instance;
  }
  
  public String toString() {
    return "GW[" + (instance != null ? ("class=" + instance.getClass().getName() +
        ",value=" + instance.toString()) : "(null)") + "]";
  }

  public void readFields(DataInput in) throws IOException {
    type = in.readByte();
    Class<? extends Writable> clazz = getTypes()[type & 0xff];
    try {
      instance = ReflectionUtils.newInstance(clazz, conf);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("Cannot initialize the class: " + clazz);
    }
    instance.readFields(in);
  }

  public void write(DataOutput out) throws IOException {
    if (type == NOT_SET || instance == null)
      throw new IOException("The GenericWritable has NOT been set correctly. type="
                            + type + ", instance=" + instance);
    out.writeByte(type);
    instance.write(out);
  }

  /**
   * Return all classes that may be wrapped.  Subclasses should implement this
   * to return a constant array of classes.
   */
  abstract protected Class<? extends Writable>[] getTypes();

  public Configuration getConf() {
    return conf;
  }

  public void setConf(Configuration conf) {
    this.conf = conf;
  }
  
}
