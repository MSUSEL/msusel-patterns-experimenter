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
package org.apache.hadoop.security.authorize;

import java.io.IOException;
import java.net.InetAddress;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.security.KerberosInfo;
import org.apache.hadoop.security.SecurityUtil;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * An authorization manager which handles service-level authorization
 * for incoming service requests.
 */
public class ServiceAuthorizationManager {
  private static final String HADOOP_POLICY_FILE = "hadoop-policy.xml";
  private static final Log LOG = LogFactory
      .getLog(ServiceAuthorizationManager.class);

  private static Map<Class<?>, AccessControlList> protocolToAcl =
    new IdentityHashMap<Class<?>, AccessControlList>();
  
  /**
   * Configuration key for controlling service-level authorization for Hadoop.
   * 
   * @deprecated Use
   *             {@link CommonConfigurationKeys#HADOOP_SECURITY_AUTHORIZATION}
   *             instead.
   */
  @Deprecated
  public static final String SERVICE_AUTHORIZATION_CONFIG = 
    "hadoop.security.authorization";
  
  private static final Log AUDITLOG =
    LogFactory.getLog("SecurityLogger."+ServiceAuthorizationManager.class.getName());

  private static final String AUTHZ_SUCCESSFULL_FOR = "Authorization successfull for ";
  private static final String AUTHZ_FAILED_FOR = "Authorization failed for ";

  /**
   * Authorize the user to access the protocol being used.
   * 
   * @param user user accessing the service 
   * @param protocol service being accessed
   * @param conf configuration to use
   * @param addr InetAddress of the client
   * @throws AuthorizationException on authorization failure
   */
  public static void authorize(UserGroupInformation user, 
                               Class<?> protocol,
                               Configuration conf,
                               InetAddress addr
                               ) throws AuthorizationException {
    AccessControlList acl = protocolToAcl.get(protocol);
    if (acl == null) {
      throw new AuthorizationException("Protocol " + protocol + 
                                       " is not known.");
    }
        
    // get client principal key to verify (if available)
    KerberosInfo krbInfo = protocol.getAnnotation(KerberosInfo.class);
    String clientPrincipal = null; 
    if (krbInfo != null) {
      String clientKey = krbInfo.clientPrincipal();
      if (clientKey != null && !clientKey.equals("")) {
        try {
          clientPrincipal = SecurityUtil.getServerPrincipal(
              conf.get(clientKey), addr);
        } catch (IOException e) {
          throw (AuthorizationException) new AuthorizationException(
              "Can't figure out Kerberos principal name for connection from "
                  + addr + " for user=" + user + " protocol=" + protocol)
              .initCause(e);
        }
      }
    }
    if((clientPrincipal != null && !clientPrincipal.equals(user.getUserName())) || 
        !acl.isUserAllowed(user)) {
      AUDITLOG.warn(AUTHZ_FAILED_FOR + user + " for protocol=" + protocol
          + ", expected client Kerberos principal is " + clientPrincipal);
      throw new AuthorizationException("User " + user + 
          " is not authorized for protocol " + protocol + 
          ", expected client Kerberos principal is " + clientPrincipal);
    }
    AUDITLOG.info(AUTHZ_SUCCESSFULL_FOR + user + " for protocol="+protocol);
  }

  public static synchronized void refresh(Configuration conf,
                                          PolicyProvider provider) {
    // Get the system property 'hadoop.policy.file'
    String policyFile = 
      System.getProperty("hadoop.policy.file", HADOOP_POLICY_FILE);
    
    // Make a copy of the original config, and load the policy file
    Configuration policyConf = new Configuration(conf);
    policyConf.addResource(policyFile);
    
    final Map<Class<?>, AccessControlList> newAcls =
      new IdentityHashMap<Class<?>, AccessControlList>();

    // Parse the config file
    Service[] services = provider.getServices();
    if (services != null) {
      for (Service service : services) {
        AccessControlList acl = 
          new AccessControlList(
              policyConf.get(service.getServiceKey(), 
                             AccessControlList.WILDCARD_ACL_VALUE)
              );
        newAcls.put(service.getProtocol(), acl);
      }
    }

    // Flip to the newly parsed permissions
    protocolToAcl = newAcls;
  }
}
