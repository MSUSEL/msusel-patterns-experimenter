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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class which centralizes proxy configuration, in an effort to reduce clutter in the {@link WebClient}
 * class. One instance of this class exists for each <tt>WebClient</tt> instance.
 *
 * @version $Revision: 5746 $
 * @author Daniel Gredler
 * @see WebClient#getProxyConfig()
 */
public class ProxyConfig implements Serializable {

    private static final long serialVersionUID = -9164636437071690421L;

    private String proxyHost_;
    private int proxyPort_;
    private boolean isSocksProxy_;
    private final Map<String, Pattern> proxyBypassHosts_ = new HashMap<String, Pattern>();
    private String proxyAutoConfigUrl_;
    private String proxyAutoConfigContent_;

    /**
     * Creates a new instance.
     */
    public ProxyConfig() {
        this(null, 0);
    }

    /**
     * Creates a new instance.
     * @param proxyHost the proxy host
     * @param proxyPort the proxy port
     */
    public ProxyConfig(final String proxyHost, final int proxyPort) {
        this(proxyHost, proxyPort, false);
    }

    /**
     * Creates a new instance.
     * @param proxyHost the proxy host
     * @param proxyPort the proxy port
     * @param isSocks whether SOCKS proxy or not
     */
    public ProxyConfig(final String proxyHost, final int proxyPort, final boolean isSocks) {
        proxyHost_ = proxyHost;
        proxyPort_ = proxyPort;
        isSocksProxy_ = isSocks;
    }

    /**
     * Returns the proxy host used to perform HTTP requests.
     * @return the proxy host used to perform HTTP requests
     */
    public String getProxyHost() {
        return proxyHost_;
    }

    /**
     * Sets the proxy host used to perform HTTP requests.
     * @param proxyHost the proxy host used to perform HTTP requests
     */
    public void setProxyHost(final String proxyHost) {
        proxyHost_ = proxyHost;
    }

    /**
     * Returns the proxy port used to perform HTTP requests.
     * @return the proxy port used to perform HTTP requests
     */
    public int getProxyPort() {
        return proxyPort_;
    }

    /**
     * Sets the proxy port used to perform HTTP requests.
     * @param proxyPort the proxy port used to perform HTTP requests
     */
    public void setProxyPort(final int proxyPort) {
        proxyPort_ = proxyPort;
    }

    /**
     * Returns whether SOCKS proxy or not.
     * @return whether SOCKS proxy or not
     */
    public boolean isSocksProxy() {
        return isSocksProxy_;
    }

    /**
     * Sets whether SOCKS proxy or not.
     * @param isSocksProxy whether SOCKS proxy or not
     */
    public void setSocksProxy(final boolean isSocksProxy) {
        isSocksProxy_ = isSocksProxy;
    }

    /**
     * Any hosts matched by the specified regular expression pattern will bypass the configured proxy.
     * @param pattern a regular expression pattern that matches the hostnames of the hosts which should
     *                bypass the configured proxy.
     * @see Pattern
     */
    public void addHostsToProxyBypass(final String pattern) {
        proxyBypassHosts_.put(pattern, Pattern.compile(pattern));
    }

    /**
     * Any hosts matched by the specified regular expression pattern will no longer bypass the configured proxy.
     * @param pattern the previously added regular expression pattern
     * @see Pattern
     */
    public void removeHostsFromProxyBypass(final String pattern) {
        proxyBypassHosts_.remove(pattern);
    }

    /**
     * Returns <tt>true</tt> if the host with the specified hostname should be accessed bypassing the
     * configured proxy.
     * @param hostname the name of the host to check
     * @return <tt>true</tt> if the host with the specified hostname should be accessed bypassing the
     * configured proxy, <tt>false</tt> otherwise.
     */
    protected boolean shouldBypassProxy(final String hostname) {
        boolean bypass = false;
        for (final Pattern p : proxyBypassHosts_.values()) {
            if (p.matcher(hostname).find()) {
                bypass = true;
                break;
            }
        }
        return bypass;
    }

    /**
     * Returns the proxy auto-config URL.
     * @return the proxy auto-config URL
     */
    public String getProxyAutoConfigUrl() {
        return proxyAutoConfigUrl_;
    }

    /**
     * Sets the proxy auto-config URL.
     * @param proxyAutoConfigUrl the proxy auto-config URL
     */
    public void setProxyAutoConfigUrl(final String proxyAutoConfigUrl) {
        proxyAutoConfigUrl_ = proxyAutoConfigUrl;
        setProxyAutoConfigContent(null);
    }

    /**
     * Returns the proxy auto-config content.
     * @return the proxy auto-config content
     */
    protected String getProxyAutoConfigContent() {
        return proxyAutoConfigContent_;
    }

    /**
     * Sets the proxy auto-config content.
     * @param proxyAutoConfigContent the proxy auto-config content
     */
    protected void setProxyAutoConfigContent(final String proxyAutoConfigContent) {
        proxyAutoConfigContent_ = proxyAutoConfigContent;
    }
}
