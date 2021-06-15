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
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InetAddress utility.
 * @author stack
 * @version $Date: 2009-08-27 23:45:31 +0000 (Thu, 27 Aug 2009) $, $Revision: 6477 $
 */
public class InetAddressUtil {
    private static Logger logger =
        Logger.getLogger(InetAddressUtil.class.getName());
    
    /**
     * ipv4 address.
     */
    public static Pattern IPV4_QUADS = Pattern.compile(
        "([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})");
    
    private InetAddressUtil () {
        super();
    }
    
    /**
     * Returns InetAddress for passed <code>host</code> IF its in
     * IPV4 quads format (e.g. 128.128.128.128).
     * <p>TODO: Move to an AddressParsingUtil class.
     * @param host Host name to examine.
     * @return InetAddress IF the passed name was an IP address, else null.
     */
    public static InetAddress getIPHostAddress(String host) {
        InetAddress result = null;
        Matcher matcher = IPV4_QUADS.matcher(host);
        if (matcher == null || !matcher.matches()) {
            return result;
        }
        try {
            // Doing an Inet.getByAddress() avoids a lookup.
            result = InetAddress.getByAddress(host,
                    new byte[] {
                    (byte)(new Integer(matcher.group(1)).intValue()),
                    (byte)(new Integer(matcher.group(2)).intValue()),
                    (byte)(new Integer(matcher.group(3)).intValue()),
                    (byte)(new Integer(matcher.group(4)).intValue())});
        } catch (NumberFormatException e) {
            logger.warning(e.getMessage());
        } catch (UnknownHostException e) {
            logger.warning(e.getMessage());
        }
        return result;
    }
    
    /**
     * @return All known local names for this host or null if none found.
     */
    public static List<String> getAllLocalHostNames() {
        List<String> localNames = new ArrayList<String>();
        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch(SocketException exception) {
            throw new RuntimeException(exception);
        }
        for (; e.hasMoreElements();) {
            for (Enumeration ee =
                ((NetworkInterface)e.nextElement()).getInetAddresses();
                    ee.hasMoreElements();) {
                InetAddress ia = (InetAddress)ee.nextElement();
                if (ia != null) {
                    if (ia.getHostName() != null) {
                        localNames.add(ia.getCanonicalHostName());
                    }
                    if (ia.getHostAddress() !=  null) {
                        localNames.add(ia.getHostAddress());
                    }
                }
            }
        }
        final String localhost = "localhost";
        if (!localNames.contains(localhost)) {
            localNames.add(localhost);
        }
        final String localhostLocaldomain = "localhost.localdomain";
        if (!localNames.contains(localhostLocaldomain)) {
            localNames.add(localhostLocaldomain);
        }
        return localNames;
    }
}