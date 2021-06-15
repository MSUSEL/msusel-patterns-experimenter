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
package org.apache.james.util;

import java.net.InetAddress;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public class NetMatcher
{
    private ArrayList networks;

    public void initInetNetworks(final Collection nets)
    {
        networks = new ArrayList();
        for (Iterator iter = nets.iterator(); iter.hasNext(); ) try
        {
            InetNetwork net = InetNetwork.getFromString((String) iter.next());
            if (!networks.contains(net)) networks.add(net);
        }
        catch (java.net.UnknownHostException uhe)
        {
            log("Cannot resolve address: " + uhe.getMessage());
        }
        networks.trimToSize();
    }

    public void initInetNetworks(final String[] nets)
    {
        networks = new ArrayList();
        for (int i = 0; i < nets.length; i++) try
        {
            InetNetwork net = InetNetwork.getFromString(nets[i]);
            if (!networks.contains(net)) networks.add(net);
        }
        catch (java.net.UnknownHostException uhe)
        {
            log("Cannot resolve address: " + uhe.getMessage());
        }
        networks.trimToSize();
    }

    public boolean matchInetNetwork(final String hostIP)
    {
        InetAddress ip = null;

        try
        {
            ip = org.apache.james.dnsserver.DNSServer.getByName(hostIP);
        }
        catch (java.net.UnknownHostException uhe)
        {
            log("Cannot resolve address for " + hostIP + ": " + uhe.getMessage());
        }

        boolean sameNet = false;

        if (ip != null) for (Iterator iter = networks.iterator(); (!sameNet) && iter.hasNext(); )
        {
            InetNetwork network = (InetNetwork) iter.next();
            sameNet = network.contains(ip);
        }
        return sameNet;
    }

    public boolean matchInetNetwork(final InetAddress ip)
    {
        boolean sameNet = false;

        for (Iterator iter = networks.iterator(); (!sameNet) && iter.hasNext(); )
        {
            InetNetwork network = (InetNetwork) iter.next();
            sameNet = network.contains(ip);
        }
        return sameNet;
    }

    public NetMatcher()
    {
    }

    public NetMatcher(final String[] nets)
    {
        initInetNetworks(nets);
    }

    public NetMatcher(final Collection nets)
    {
        initInetNetworks(nets);
    }

    public String toString() {
        return networks.toString();
    }

    protected void log(String s) { }
}

class InetNetwork
{
    /*
     * Implements network masking, and is compatible with RFC 1518 and
     * RFC 1519, which describe CIDR: Classless Inter-Domain Routing.
     */

    private InetAddress network;
    private InetAddress netmask;

    public InetNetwork(InetAddress ip, InetAddress netmask)
    {
        network = maskIP(ip, netmask);
        this.netmask = netmask;
    }

    public boolean contains(final String name) throws java.net.UnknownHostException
    {
        return network.equals(maskIP(org.apache.james.dnsserver.DNSServer.getByName(name), netmask));
    }

    public boolean contains(final InetAddress ip)
    {
        return network.equals(maskIP(ip, netmask));
    }

    public String toString()
    {
        return network.getHostAddress() + "/" + netmask.getHostAddress();
    }

    public int hashCode()
    {
        return maskIP(network, netmask).hashCode();
    }

    public boolean equals(Object obj)
    {
        return (obj != null) && (obj instanceof InetNetwork) &&
                ((((InetNetwork)obj).network.equals(network)) && (((InetNetwork)obj).netmask.equals(netmask)));
    }

    public static InetNetwork getFromString(String netspec) throws java.net.UnknownHostException
    {
        if (netspec.endsWith("*")) netspec = normalizeFromAsterisk(netspec);
        else
        {
            int iSlash = netspec.indexOf('/');
            if (iSlash == -1) netspec += "/255.255.255.255";
            else if (netspec.indexOf('.', iSlash) == -1) netspec = normalizeFromCIDR(netspec);
        }

        return new InetNetwork(org.apache.james.dnsserver.DNSServer.getByName(netspec.substring(0, netspec.indexOf('/'))),
                               org.apache.james.dnsserver.DNSServer.getByName(netspec.substring(netspec.indexOf('/') + 1)));
    }

    public static InetAddress maskIP(final byte[] ip, final byte[] mask)
    {
        try
        {
            return getByAddress(new byte[]
            {
                (byte) (mask[0] & ip[0]),
                (byte) (mask[1] & ip[1]),
                (byte) (mask[2] & ip[2]),
                (byte) (mask[3] & ip[3])
            });
        }
        catch(Exception _) {}
        {
            return null;
        }
    }

    public static InetAddress maskIP(final InetAddress ip, final InetAddress mask)
    {
        return maskIP(ip.getAddress(), mask.getAddress());
    }

    /*
     * This converts from an uncommon "wildcard" CIDR format
     * to "address + mask" format:
     * 
     *   *               =>  000.000.000.0/000.000.000.0
     *   xxx.*           =>  xxx.000.000.0/255.000.000.0
     *   xxx.xxx.*       =>  xxx.xxx.000.0/255.255.000.0
     *   xxx.xxx.xxx.*   =>  xxx.xxx.xxx.0/255.255.255.0
     */
    static private String normalizeFromAsterisk(final String netspec)
    {
        String[] masks = {  "0.0.0.0/0.0.0.0", "0.0.0/255.0.0.0", "0.0/255.255.0.0", "0/255.255.255.0" };
        char[] srcb = netspec.toCharArray();                
        int octets = 0;
        for (int i = 1; i < netspec.length(); i++) {
            if (srcb[i] == '.') octets++;
        }
        return (octets == 0) ? masks[0] : netspec.substring(0, netspec.length() -1 ).concat(masks[octets]);
    }

    /*
     * RFC 1518, 1519 - Classless Inter-Domain Routing (CIDR)
     * This converts from "prefix + prefix-length" format to
     * "address + mask" format, e.g. from xxx.xxx.xxx.xxx/yy
     * to xxx.xxx.xxx.xxx/yyy.yyy.yyy.yyy.
     */
    static private String normalizeFromCIDR(final String netspec)
    {
        final int bits = 32 - Integer.parseInt(netspec.substring(netspec.indexOf('/')+1));
        final int mask = (bits == 32) ? 0 : 0xFFFFFFFF - ((1 << bits)-1);

        return netspec.substring(0, netspec.indexOf('/') + 1) +
                Integer.toString(mask >> 24 & 0xFF, 10) + "." +
                Integer.toString(mask >> 16 & 0xFF, 10) + "." +
                Integer.toString(mask >>  8 & 0xFF, 10) + "." +
                Integer.toString(mask >>  0 & 0xFF, 10);
    }

    private static java.lang.reflect.Method getByAddress = null;

    static {
        try {
            Class inetAddressClass = Class.forName("java.net.InetAddress");
            Class[] parameterTypes = { byte[].class };
            getByAddress = inetAddressClass.getMethod("getByAddress", parameterTypes);
        } catch (Exception e) {
            getByAddress = null;
        }
    }

    private static InetAddress getByAddress(byte[] ip) throws java.net.UnknownHostException
    {
        InetAddress addr = null;
        if (getByAddress != null) try {
            addr = (InetAddress) getByAddress.invoke(null, new Object[] { ip });
        } catch (IllegalAccessException e) {
        } catch (java.lang.reflect.InvocationTargetException e) {
        }

        if (addr == null) {
            addr = InetAddress.getByName
                   (
                    Integer.toString(ip[0] & 0xFF, 10) + "." +
                    Integer.toString(ip[1] & 0xFF, 10) + "." +
                    Integer.toString(ip[2] & 0xFF, 10) + "." +
                    Integer.toString(ip[3] & 0xFF, 10)
                   );
        }
        return addr;
    }
}
