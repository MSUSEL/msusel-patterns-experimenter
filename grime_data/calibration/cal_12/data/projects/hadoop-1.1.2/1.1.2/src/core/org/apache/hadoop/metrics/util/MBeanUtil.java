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
package org.apache.hadoop.metrics.util;

import java.lang.management.ManagementFactory;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.InstanceAlreadyExistsException;

import org.apache.hadoop.classification.InterfaceAudience;


/**
 * This util class provides a method to register an MBean using
 * our standard naming convention as described in the doc
 *  for {@link #registerMBean(String, String, Object)}
 *
 * @deprecated in favor of {@link org.apache.hadoop.metrics2.util.MBeans}.
 */
@Deprecated
@InterfaceAudience.LimitedPrivate({"HDFS", "MapReduce"})
public class MBeanUtil {
	
  /**
   * Register the MBean using our standard MBeanName format
   * "hadoop:service=<serviceName>,name=<nameName>"
   * Where the <serviceName> and <nameName> are the supplied parameters
   *    
   * @param serviceName
   * @param nameName
   * @param theMbean - the MBean to register
   * @return the named used to register the MBean
   */	
  static public ObjectName registerMBean(final String serviceName, 
		  							final String nameName,
		  							final Object theMbean) {
    final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    ObjectName name = getMBeanName(serviceName, nameName);
    try {
      mbs.registerMBean(theMbean, name);
      return name;
    } catch (InstanceAlreadyExistsException ie) {
      // Ignore if instance already exists 
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  static public void unregisterMBean(ObjectName mbeanName) {
    final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    if (mbeanName == null) 
        return;
    try {
      mbs.unregisterMBean(mbeanName);
    } catch (InstanceNotFoundException e ) {
      // ignore
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  static private ObjectName getMBeanName(final String serviceName,
		  								 final String nameName) {
    ObjectName name = null;
    try {
      name = new ObjectName("hadoop:" +
                  "service=" + serviceName + ",name=" + nameName);
    } catch (MalformedObjectNameException e) {
      e.printStackTrace();
    }
    return name;
  }
}
