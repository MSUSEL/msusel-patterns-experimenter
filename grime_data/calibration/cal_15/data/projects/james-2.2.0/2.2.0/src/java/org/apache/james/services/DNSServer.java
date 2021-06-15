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
package org.apache.james.services;

import java.util.Collection;
import java.util.Iterator;

/**
 * Provides abstraction for DNS resolutions. The interface is Mail specific.
 * It may be a good idea to make the interface more generic or expose 
 * commonly needed DNS methods.
 *
 */
public interface DNSServer {

    /**
     * The component role used by components implementing this service
     */
    String ROLE = "org.apache.james.services.DNSServer";

    /**
     * <p>Get a priority-sorted collection of DNS MX records for a given hostname</p>
     *
     * <p>TODO: Change this to a list, as not all collections are sortable</p>
     *
     * @param hostname the hostname to check
     * @return collection of strings representing MX record values. 
     */
    Collection findMXRecords(String hostname);


    /**
     * Performs DNS lookups as needed to find servers which should or might
     * support SMTP.  Returns one SMTPHostAddresses for each such host
     * discovered by DNS.  If no host is found for domainName, the Iterator
     * returned will be empty and the first call to hasNext() will return
     * false.
     * @param domainName the String domain for which SMTP host addresses are
     * sought.
     * @return an Enumeration in which the Objects returned by next()
     * are instances of SMTPHostAddresses.
     */
    Iterator getSMTPHostAddresses(String domainName);
    
}
