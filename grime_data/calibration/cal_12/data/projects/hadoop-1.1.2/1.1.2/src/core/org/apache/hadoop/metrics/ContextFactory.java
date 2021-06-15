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
package org.apache.hadoop.metrics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics.spi.NullContext;

/**
 * Factory class for creating MetricsContext objects.  To obtain an instance
 * of this class, use the static <code>getFactory()</code> method.
 * @deprecated in favor of <code>org.apache.hadoop.metrics2</code> usage.
 */
@Deprecated
@InterfaceAudience.LimitedPrivate({"HDFS", "MapReduce"})
@InterfaceStability.Evolving
public class ContextFactory {
    
  private static final String PROPERTIES_FILE = 
    "/hadoop-metrics.properties";
  private static final String CONTEXT_CLASS_SUFFIX =
    ".class";
  private static final String DEFAULT_CONTEXT_CLASSNAME =
    "org.apache.hadoop.metrics.spi.NullContext";
    
  private static ContextFactory theFactory = null;
    
  private Map<String,Object> attributeMap = new HashMap<String,Object>();
  private Map<String,MetricsContext> contextMap = 
    new HashMap<String,MetricsContext>();
    
  // Used only when contexts, or the ContextFactory itself, cannot be
  // created.
  private static Map<String,MetricsContext> nullContextMap = 
    new HashMap<String,MetricsContext>();
    
  /** Creates a new instance of ContextFactory */
  protected ContextFactory() {
  }
    
  /**
   * Returns the value of the named attribute, or null if there is no 
   * attribute of that name.
   *
   * @param attributeName the attribute name
   * @return the attribute value
   */
  public Object getAttribute(String attributeName) {
    return attributeMap.get(attributeName);
  }
    
  /**
   * Returns the names of all the factory's attributes.
   * 
   * @return the attribute names
   */
  public String[] getAttributeNames() {
    String[] result = new String[attributeMap.size()];
    int i = 0;
    // for (String attributeName : attributeMap.keySet()) {
    Iterator it = attributeMap.keySet().iterator();
    while (it.hasNext()) {
      result[i++] = (String) it.next();
    }
    return result;
  }
    
  /**
   * Sets the named factory attribute to the specified value, creating it
   * if it did not already exist.  If the value is null, this is the same as
   * calling removeAttribute.
   *
   * @param attributeName the attribute name
   * @param value the new attribute value
   */
  public void setAttribute(String attributeName, Object value) {
    attributeMap.put(attributeName, value);
  }

  /**
   * Removes the named attribute if it exists.
   *
   * @param attributeName the attribute name
   */
  public void removeAttribute(String attributeName) {
    attributeMap.remove(attributeName);
  }
    
  /**
   * Returns the named MetricsContext instance, constructing it if necessary 
   * using the factory's current configuration attributes. <p/>
   * 
   * When constructing the instance, if the factory property 
   * <i>contextName</i>.class</code> exists, 
   * its value is taken to be the name of the class to instantiate.  Otherwise,
   * the default is to create an instance of 
   * <code>org.apache.hadoop.metrics.spi.NullContext</code>, which is a 
   * dummy "no-op" context which will cause all metric data to be discarded.
   * 
   * @param contextName the name of the context
   * @return the named MetricsContext
   */
  public synchronized MetricsContext getContext(String refName, String contextName)
      throws IOException, ClassNotFoundException,
             InstantiationException, IllegalAccessException {
    MetricsContext metricsContext = contextMap.get(refName);
    if (metricsContext == null) {
      String classNameAttribute = refName + CONTEXT_CLASS_SUFFIX;
      String className = (String) getAttribute(classNameAttribute);
      if (className == null) {
        className = DEFAULT_CONTEXT_CLASSNAME;
      }
      Class contextClass = Class.forName(className);
      metricsContext = (MetricsContext) contextClass.newInstance();
      metricsContext.init(contextName, this);
      contextMap.put(contextName, metricsContext);
    }
    return metricsContext;
  }

  public synchronized MetricsContext getContext(String contextName)
    throws IOException, ClassNotFoundException, InstantiationException,
           IllegalAccessException {
    return getContext(contextName, contextName);
  }
  
  /** 
   * Returns all MetricsContexts built by this factory.
   */
  public synchronized Collection<MetricsContext> getAllContexts() {
    // Make a copy to avoid race conditions with creating new contexts.
    return new ArrayList<MetricsContext>(contextMap.values());
  }
    
  /**
   * Returns a "null" context - one which does nothing.
   */
  public static synchronized MetricsContext getNullContext(String contextName) {
    MetricsContext nullContext = nullContextMap.get(contextName);
    if (nullContext == null) {
      nullContext = new NullContext();
      nullContextMap.put(contextName, nullContext);
    }
    return nullContext;
  }
    
  /**
   * Returns the singleton ContextFactory instance, constructing it if 
   * necessary. <p/>
   * 
   * When the instance is constructed, this method checks if the file 
   * <code>hadoop-metrics.properties</code> exists on the class path.  If it 
   * exists, it must be in the format defined by java.util.Properties, and all 
   * the properties in the file are set as attributes on the newly created
   * ContextFactory instance.
   *
   * @return the singleton ContextFactory instance
   */
  public static synchronized ContextFactory getFactory() throws IOException {
    if (theFactory == null) {
      theFactory = new ContextFactory();
      theFactory.setAttributes();
    }
    return theFactory;
  }
    
  private void setAttributes() throws IOException {
    InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE);
    if (is != null) {
      try {
        Properties properties = new Properties();
        properties.load(is);
        //for (Object propertyNameObj : properties.keySet()) {
        Iterator it = properties.keySet().iterator();
        while (it.hasNext()) {
          String propertyName = (String) it.next();
          String propertyValue = properties.getProperty(propertyName);
          setAttribute(propertyName, propertyValue);
        }
      } finally {
        is.close();
      }
    }
  }
    
}
