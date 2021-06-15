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
package org.archive.crawler.datamodel;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.util.InetAddressUtil;

/** 
 * Represents a single remote "host".
 *
 * An host is a name for which there is a dns record or an IP-address. This
 * might be a machine or a virtual host.
 *
 * @author gojomo
 */
public class CrawlHost implements Serializable, CrawlSubstats.HasCrawlSubstats {

    private static final long serialVersionUID = -5494573967890942895L;

    private static final Logger logger = Logger.getLogger(CrawlHost.class.getName());
    /** Flag value indicating always-valid IP */
    public static final long IP_NEVER_EXPIRES = -1;
    /** Flag value indicating an IP has not yet been looked up */
    public static final long IP_NEVER_LOOKED_UP = -2;
    private String hostname;
    private String countryCode;
    private InetAddress ip;
    private long ipFetched = IP_NEVER_LOOKED_UP;
    protected CrawlSubstats substats = new CrawlSubstats(); 
    /**
     * TTL gotten from dns record.
     *
     * From rfc2035:
     * <pre>
     * TTL       a 32 bit unsigned integer that specifies the time
     *           interval (in seconds) that the resource record may be
     *           cached before it should be discarded.  Zero values are
     *           interpreted to mean that the RR can only be used for the
     *           transaction in progress, and should not be cached.
     * </pre>
     */
    private long ipTTL = IP_NEVER_LOOKED_UP;

    // Used when bandwith constraint are used
    private long earliestNextURIEmitTime = 0;
    
    /** 
     * Create a new CrawlHost object.
     *
     * @param hostname the host name for this host.
     */
    public CrawlHost(String hostname) {
    		this(hostname, null);
    }

    /** 
     * Create a new CrawlHost object.
     *
     * @param hostname the host name for this host.
     * @param countryCode the country code for this host.
     */
    public CrawlHost(String hostname, String countryCode) {
        this.hostname = hostname;
        this.countryCode = countryCode;
        InetAddress tmp = InetAddressUtil.getIPHostAddress(hostname);
        if (tmp != null) {
            setIP(tmp, IP_NEVER_EXPIRES);
        }
    }

    /** Return true if the IP for this host has been looked up.
     *
     * Returns true even if the lookup failed.
     *
     * @return true if the IP for this host has been looked up.
     */
    public boolean hasBeenLookedUp() {
        return ipFetched != IP_NEVER_LOOKED_UP;
    }

    /**
     * Set the IP address for this host.
     *
     * @param address
     * @param ttl the TTL from the dns record in seconds or -1 if it should live
     * forever (is a numeric IP).
     */
    public void setIP(InetAddress address, long ttl) {
        this.ip = address;
        // Assume that a lookup as occurred by the time
        // a caller decides to set this (even to null)
        this.ipFetched = System.currentTimeMillis();
        this.ipTTL = ttl;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(hostname + ": " +
                ((address != null)? address.toString(): "null"));
        }
    }

    /** Get the IP address for this host.
     *
     * @return the IP address for this host.
     */
    public InetAddress getIP() {
        return ip;
    }

    /** Get the time when the IP address for this host was last looked up.
     *
     * @return the time when the IP address for this host was last looked up.
     */
    public long getIpFetched() {
        return ipFetched;
    }

    /**
     * Get the TTL value from the dns record for this host.
     *
     * @return the TTL value from the dns record for this host -- in seconds --
     * or -1 if this lookup should be valid forever (numeric ip).
     */
    public long getIpTTL() {
        return this.ipTTL;
    }

    public String toString() {
        return "CrawlHost<" + hostname + "(ip:" + ip + ")>";
    }

    @Override
    public int hashCode() {
        return this.hostname != null ? this.hostname.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CrawlHost other = (CrawlHost) obj;
        if (this.hostname != other.hostname   // identity compare
                && (this.hostname == null 
                    || !this.hostname.equals(other.hostname))) {
            return false;
        }
        return true;
    }

    /**
     * Get the host name.
     * @return Returns the host name.
     */
    public String getHostName() {
        return hostname;
    }

    /** 
     * Get the earliest time a URI for this host could be emitted.
     * This only has effect if constraints on bandwidth per host is set.
     *
     * @return Returns the earliestNextURIEmitTime.
     */
    public long getEarliestNextURIEmitTime() {
        return earliestNextURIEmitTime;
    }

    /** 
     * Set the earliest time a URI for this host could be emitted.
     * This only has effect if constraints on bandwidth per host is set.
     *
     * @param earliestNextURIEmitTime The earliestNextURIEmitTime to set.
     */
    public void setEarliestNextURIEmitTime(long earliestNextURIEmitTime) {
        this.earliestNextURIEmitTime = earliestNextURIEmitTime;
    }

    /**
     * Get country code of this host
     * 
     * @return Retruns country code or null if not availabe 
     */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Set country code for this hos
	 * 
	 * @param countryCode The country code of this host
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
    
    /* (non-Javadoc)
     * @see org.archive.crawler.datamodel.CrawlSubstats.HasCrawlSubstats#getSubstats()
     */
    public CrawlSubstats getSubstats() {
        return substats;
    }
}
