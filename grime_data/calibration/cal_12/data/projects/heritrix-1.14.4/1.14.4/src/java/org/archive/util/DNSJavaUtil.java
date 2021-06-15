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
package org.archive.util;

import java.net.InetAddress;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import org.xbill.DNS.Lookup;;

/**
 * Utility methods based on DNSJava.
 * Use these utilities to avoid having to use the native InetAddress lookup.
 * @author stack
 * @version $Date: 2007-01-06 05:17:35 +0000 (Sat, 06 Jan 2007) $, $Revision: 4837 $
 */
public class DNSJavaUtil {
    private DNSJavaUtil() {
        super();
    }
    
    /**
     * Return an InetAddress for passed <code>host</code>.
     * 
     * If passed host is an IPv4 address, we'll not do a DNSJava
     * lookup.
     * 
     * @param host Host to lookup in dnsjava.
     * @return A host address or null if not found.
     */
    public static InetAddress getHostAddress(String host) {
        InetAddress hostAddress = InetAddressUtil.getIPHostAddress(host);
        if (hostAddress != null) {
            return hostAddress;
        }
        
        // Ask dnsjava for the inetaddress.  Should be in its cache.
        Record[] rrecordSet;
        try {
            rrecordSet = (new Lookup(host, Type.A, DClass.IN)).run();
        } catch (TextParseException e) {
            rrecordSet = null;
        }
        if (rrecordSet != null) {
            // Get TTL and IP info from the first A record (there may be
            // multiple, e.g. www.washington.edu).
            for (int i = 0; i < rrecordSet.length; i++) {
                if (rrecordSet[i].getType() != Type.A) {
                    continue;
                }
                hostAddress = ((ARecord)rrecordSet[i]).getAddress();
                break;
            }
        }
        return hostAddress;
    }
}
