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

import java.net.URL;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.localserver.LocalTestServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for insecure SSL.
 *
 * @version $Revision: 5903 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnectionInsecureSSLTest extends WebTestCase {

    private LocalTestServer localServer_;

    /**
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        final URL url = getClass().getClassLoader().getResource("insecureSSL.keystore");
        final KeyStore keystore  = KeyStore.getInstance("jks");
        final char[] pwd = "nopassword".toCharArray();
        keystore.load(url.openStream(), pwd);

        final TrustManagerFactory trustManagerFactory = createTrustManagerFactory();
        trustManagerFactory.init(keystore);
        final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        final KeyManagerFactory keyManagerFactory = createKeyManagerFactory();
        keyManagerFactory.init(keystore, pwd);
        final KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

        final SSLContext serverSSLContext = SSLContext.getInstance("TLS");
        serverSSLContext.init(keyManagers, trustManagers, null);

        localServer_ = new LocalTestServer(null, null, null, serverSSLContext);
        localServer_.registerDefaultHandlers();

        localServer_.start();
    }

    private KeyManagerFactory createKeyManagerFactory() throws NoSuchAlgorithmException {
        final String algorithm = KeyManagerFactory.getDefaultAlgorithm();
        try {
            return KeyManagerFactory.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
            return KeyManagerFactory.getInstance("SunX509");
        }
    }

    private TrustManagerFactory createTrustManagerFactory() throws NoSuchAlgorithmException {
        final String algorithm = TrustManagerFactory.getDefaultAlgorithm();
        try {
            return TrustManagerFactory.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
            return TrustManagerFactory.getInstance("SunX509");
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        if (localServer_ != null) {
            localServer_.stop();
        }
        localServer_ = null;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test(expected = SSLPeerUnverifiedException.class)
    public void normal() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getPage("https://" + localServer_.getServiceHostName() + ':' + localServer_.getServicePort()
                + "/random/100");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.setUseInsecureSSL(true);
        webClient.getPage("https://" + localServer_.getServiceHostName() + ':' + localServer_.getServicePort()
                + "/random/100");
    }
}
