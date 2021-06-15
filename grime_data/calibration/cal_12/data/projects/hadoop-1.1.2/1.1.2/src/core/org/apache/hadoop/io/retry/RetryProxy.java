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

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * <p>
 * A factory for creating retry proxies.
 * </p>
 */
public class RetryProxy {
  /**
   * <p>
   * Create a proxy for an interface of an implementation class
   * using the same retry policy for each method in the interface. 
   * </p>
   * @param iface the interface that the retry will implement
   * @param implementation the instance whose methods should be retried
   * @param retryPolicy the policy for retirying method call failures
   * @return the retry proxy
   */
  public static Object create(Class<?> iface, Object implementation,
                              RetryPolicy retryPolicy) {
    return Proxy.newProxyInstance(
                                  implementation.getClass().getClassLoader(),
                                  new Class<?>[] { iface },
                                  new RetryInvocationHandler(implementation, retryPolicy)
                                  );
  }  
  
  /**
   * <p>
   * Create a proxy for an interface of an implementation class
   * using the a set of retry policies specified by method name.
   * If no retry policy is defined for a method then a default of
   * {@link RetryPolicies#TRY_ONCE_THEN_FAIL} is used.
   * </p>
   * @param iface the interface that the retry will implement
   * @param implementation the instance whose methods should be retried
   * @param methodNameToPolicyMap a map of method names to retry policies
   * @return the retry proxy
   */
  public static Object create(Class<?> iface, Object implementation,
                              Map<String,RetryPolicy> methodNameToPolicyMap) {
    return Proxy.newProxyInstance(
                                  implementation.getClass().getClassLoader(),
                                  new Class<?>[] { iface },
                                  new RetryInvocationHandler(implementation, methodNameToPolicyMap)
                                  );
  }

  public static Object create(Class<?> iface, Object implementation,
      RetryPolicy defaultPolicy, Map<String,RetryPolicy> methodNameToPolicyMap) {
    return Proxy.newProxyInstance(
        implementation.getClass().getClassLoader(),
        new Class<?>[] { iface },
        new RetryInvocationHandler(implementation, defaultPolicy, methodNameToPolicyMap)
        );
  }
}
