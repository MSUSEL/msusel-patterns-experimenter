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
package org.apache.hadoop.io.retry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.util.StringUtils;

class RetryInvocationHandler implements InvocationHandler {
  public static final Log LOG = LogFactory.getLog(RetryInvocationHandler.class);
  private Object implementation;
  
  private RetryPolicy defaultPolicy;
  private Map<String,RetryPolicy> methodNameToPolicyMap;
  
  public RetryInvocationHandler(Object implementation, RetryPolicy retryPolicy) {
    this(implementation, retryPolicy, Collections.<String, RetryPolicy>emptyMap());
  }
  
  public RetryInvocationHandler(Object implementation, Map<String, RetryPolicy> methodNameToPolicyMap) {
    this(implementation, RetryPolicies.TRY_ONCE_THEN_FAIL, methodNameToPolicyMap);
  }

  public RetryInvocationHandler(Object implementation,
      RetryPolicy defaultPolicy, Map<String, RetryPolicy> methodNameToPolicyMap) {
    this.implementation = implementation;
    this.defaultPolicy = defaultPolicy;
    this.methodNameToPolicyMap = methodNameToPolicyMap;
  }

  public Object invoke(Object proxy, Method method, Object[] args)
    throws Throwable {
    RetryPolicy policy = methodNameToPolicyMap.get(method.getName());
    if (policy == null) {
      policy = defaultPolicy;
    }
    
    int retries = 0;
    while (true) {
      try {
        return invokeMethod(method, args);
      } catch (Exception e) {
        if (!policy.shouldRetry(e, retries++)) {
          LOG.info("Exception while invoking " + method.getName()
                   + " of " + implementation.getClass() + ". Not retrying."
                   + StringUtils.stringifyException(e));
          if (!method.getReturnType().equals(Void.TYPE)) {
            throw e; // non-void methods can't fail without an exception
          }
          return null;
        }
        LOG.debug("Exception while invoking " + method.getName()
                 + " of " + implementation.getClass() + ". Retrying."
                 + StringUtils.stringifyException(e));
      }
    }
  }

  private Object invokeMethod(Method method, Object[] args) throws Throwable {
    try {
      if (!method.isAccessible()) {
        method.setAccessible(true);
      }
      return method.invoke(implementation, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

}
