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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RemoteException;

public class RetryUtils {
  public static final Log LOG = LogFactory.getLog(RetryUtils.class);
  
  /**
   * Return the default retry policy set in conf.
   * 
   * If the value retryPolicyEnabledKey is set to false in conf,
   * use TRY_ONCE_THEN_FAIL.
   * 
   * Otherwise, get the MultipleLinearRandomRetry policy specified in the conf
   * and then
   * (1) use multipleLinearRandomRetry for
   *     - remoteExceptionToRetry, or
   *     - IOException other than RemoteException; and
   * (2) use TRY_ONCE_THEN_FAIL for
   *     - RemoteException other than remoteExceptionToRetry, or
   *     - non-IOException.
   *
   * @param conf
   * @param retryPolicyEnabledKey     conf property key for enabling retry
   * @param defaultRetryPolicyEnabled default retryPolicyEnabledKey conf value 
   * @param retryPolicySpecKey        conf property key for retry policy spec
   * @param defaultRetryPolicySpec    default retryPolicySpecKey conf value
   * @param remoteExceptionToRetry    The particular RemoteException to retry
   * @return the default retry policy.
   */
  public static RetryPolicy getDefaultRetryPolicy(
      Configuration conf,
      String retryPolicyEnabledKey,
      boolean defaultRetryPolicyEnabled,
      String retryPolicySpecKey,
      String defaultRetryPolicySpec,
      final Class<? extends Exception> remoteExceptionToRetry
      ) {
    
    final RetryPolicy multipleLinearRandomRetry = 
        getMultipleLinearRandomRetry(
            conf, 
            retryPolicyEnabledKey, defaultRetryPolicyEnabled, 
            retryPolicySpecKey, defaultRetryPolicySpec
            );
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("multipleLinearRandomRetry = " + multipleLinearRandomRetry);
    }

    if (multipleLinearRandomRetry == null) {
      //no retry
      return RetryPolicies.TRY_ONCE_THEN_FAIL;
    } else {
      return new RetryPolicy() {
        @Override
        public boolean shouldRetry(Exception e, int retries) throws Exception {
          //see (1) and (2) in the javadoc of this method.
          final RetryPolicy p;
          if (e instanceof RemoteException) {
            final RemoteException re = (RemoteException)e;
            p = remoteExceptionToRetry.getName().equals(re.getClassName())?
                multipleLinearRandomRetry: RetryPolicies.TRY_ONCE_THEN_FAIL;
          } else if (e instanceof IOException) {
            p = multipleLinearRandomRetry;
          } else { //non-IOException
            p = RetryPolicies.TRY_ONCE_THEN_FAIL;
          }

          if (LOG.isDebugEnabled()) {
            LOG.debug("RETRY " + retries + ") policy="
                + p.getClass().getSimpleName() + ", exception=" + e);
          }
          return p.shouldRetry(e, retries);
        }
        
        @Override
        public String toString() {
          return "RetryPolicy[" + multipleLinearRandomRetry + ", "
              + RetryPolicies.TRY_ONCE_THEN_FAIL.getClass().getSimpleName()
              + "]";
        }
      };
    }
  }

  /**
   * Return the MultipleLinearRandomRetry policy specified in the conf,
   * or null if the feature is disabled.
   * If the policy is specified in the conf but the policy cannot be parsed,
   * the default policy is returned.
   * 
   * Retry policy spec:
   *   N pairs of sleep-time and number-of-retries "s1,n1,s2,n2,..."
   * 
   * @param conf
   * @param retryPolicyEnabledKey     conf property key for enabling retry
   * @param defaultRetryPolicyEnabled default retryPolicyEnabledKey conf value 
   * @param retryPolicySpecKey        conf property key for retry policy spec
   * @param defaultRetryPolicySpec    default retryPolicySpecKey conf value
   * @return the MultipleLinearRandomRetry policy specified in the conf,
   *         or null if the feature is disabled.
   */
  public static RetryPolicy getMultipleLinearRandomRetry(
      Configuration conf,
      String retryPolicyEnabledKey,
      boolean defaultRetryPolicyEnabled,
      String retryPolicySpecKey,
      String defaultRetryPolicySpec
      ) {
    final boolean enabled = 
        conf.getBoolean(retryPolicyEnabledKey, defaultRetryPolicyEnabled);
    if (!enabled) {
      return null;
    }

    final String policy = conf.get(retryPolicySpecKey, defaultRetryPolicySpec);

    final RetryPolicy r = 
        RetryPolicies.MultipleLinearRandomRetry.parseCommaSeparatedString(
            policy);
    return (r != null) ? 
        r : 
        RetryPolicies.MultipleLinearRandomRetry.parseCommaSeparatedString(
        defaultRetryPolicySpec);
  }
}
