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
package org.apache.james.transport.matchers;

import org.apache.james.util.NetMatcher;
import javax.mail.MessagingException;
import java.util.StringTokenizer;
import java.util.Collection;

/**
  * AbstractNetworkMatcher makes writing IP Address matchers easier.
  *
  * AbstractNetworkMatcher provides a means for checking to see whether
  * a particular IP address or domain is within a set of subnets
  * These subnets may be expressed in one of several formats:
  * 
  *     Format                          Example
  *     explicit address                127.0.0.1
  *     address with a wildcard         127.0.0.*
  *     domain name                     myHost.com
  *     domain name + prefix-length     myHost.com/24
  *     domain name + mask              myHost.com/255.255.255.0
  *     IP address + prefix-length      127.0.0.0/8
  *     IP + mask                       127.0.0.0/255.0.0.0
  *
  * For more information, see also: RFC 1518 and RFC 1519.
  * 
  * @version $ID$
  */
public abstract class AbstractNetworkMatcher extends org.apache.mailet.GenericMatcher {

    /**
     * This is a Network Matcher that should be configured to contain
     * authorized networks
     */
    private NetMatcher authorizedNetworks = null;

    public void init() throws MessagingException {
        Collection nets = allowedNetworks();
        if (nets != null) {
            authorizedNetworks = new NetMatcher() {
                protected void log(String s) {
                    AbstractNetworkMatcher.this.log(s);
                }
            };
            authorizedNetworks.initInetNetworks(allowedNetworks());
            log("Authorized addresses: " + authorizedNetworks.toString());
        }
    }

    protected Collection allowedNetworks() {
        Collection networks = null;
        if (getCondition() != null) {
            StringTokenizer st = new StringTokenizer(getCondition(), ", ", false);
            networks = new java.util.ArrayList();
            while (st.hasMoreTokens()) networks.add(st.nextToken());
        }
        return networks;
    }

    protected boolean matchNetwork(java.net.InetAddress addr) {
        return authorizedNetworks == null ? false : authorizedNetworks.matchInetNetwork(addr);
    }

    protected boolean matchNetwork(String addr) {
        return authorizedNetworks == null ? false : authorizedNetworks.matchInetNetwork(addr);
    }
}
