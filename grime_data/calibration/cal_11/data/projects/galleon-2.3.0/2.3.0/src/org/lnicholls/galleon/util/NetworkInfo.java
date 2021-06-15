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
package org.lnicholls.galleon.util;

/*
 * Original source from: http://forum.java.sun.com/thread.jsp?forum=4&thread=245711
 */

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NetworkInfo {

    public abstract String parseMacAddress() throws ParseException;

    /** Universal entry for retrieving MAC Address */
    
    public final static String getMacAddress() throws IOException {
        NetInfo info = getNetInfo(null);
        return info.getPhysicalAddress();
    }    

    public final static String getMacAddress(String address) throws IOException {
        NetInfo info = getNetInfo(address);
        return info.getPhysicalAddress();
    }
    
    public final static String getSubnetMask(String address) throws IOException {
        NetInfo info = getNetInfo(address);
        return info.getSubnetMask();
    }
    
    /** Universal entry for retrieving domain info */
    public final static String getNetworkDomain() throws IOException {
        try {
            NetInfo info = getNetInfo(null);
            String domain = info.parseDomain();
            return domain;
        } catch (ParseException ex) {
            //ex.printStackTrace();
            throw new IOException(ex.getMessage());
        }
    }

    private static NetInfo getNetInfo(String address) throws IOException {
        String os = System.getProperty("os.name");

        if (os.startsWith("Windows")) {
        	NetInfo netInfo = new WindowsNetworkInfo(address);
        	netInfo.parse();
            return netInfo; 
        } else if (os.startsWith("Linux")) {
        	NetInfo netInfo = new LinuxNetworkInfo(address);
        	netInfo.parse();
            return netInfo;
        } else if (os.startsWith("Mac")) {
        	NetInfo netInfo = new MacNetworkInfo(address);
        	netInfo.parse();
            return netInfo;
        } else {
        	NetInfo netInfo = new LinuxNetworkInfo(address);
        	netInfo.parse();
            return netInfo;
        }
    }

    public final static void main(String[] args) {

        try {
            System.out.println("Network infos");
            System.out.println("  Operating System: " + System.getProperty("os.name"));
            System.out.println("  IP/Localhost: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("  Subnet Mask: " + getSubnetMask(InetAddress.getLocalHost().getHostAddress()));
            System.out.println("  MAC Address: " + getMacAddress(InetAddress.getLocalHost().getHostAddress()));
            //System.out.println("  MAC Address: " + getMacAddress(null));
            System.out.println("  Domain: " + getNetworkDomain());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}