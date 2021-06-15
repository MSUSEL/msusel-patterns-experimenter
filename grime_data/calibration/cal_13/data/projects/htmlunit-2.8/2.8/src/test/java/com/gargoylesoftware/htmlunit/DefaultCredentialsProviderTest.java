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

import static org.junit.Assert.assertNotNull;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.auth.BasicScheme;
import org.junit.Test;

/**
 * Tests for {@link DefaultCredentialsProvider}.
 *
 * @version $Revision: 5736 $
 * @author Marc Guillemot
 */
public class DefaultCredentialsProviderTest extends WebTestCase {

    /**
     * Test for NTLM credentials.
     * @throws Exception if the test fails
     */
    @Test
    public void addNTLMCredentials() throws Exception {
        final String username = "foo";
        final String domain = "MYDOMAIN";
        final String password = "password";
        final String host = "my.host";
        final String clientHost = "CLIENT.HOST";
        final int port = 1234;
        final String realm = AuthScope.ANY_REALM;
        final String scheme = "NTLM";

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addNTLMCredentials(username, password, host, port, clientHost, domain);

        final Credentials credentials = provider.getCredentials(new AuthScope(host, port, realm, scheme));
        assertNotNull(credentials);
        assertTrue(NTCredentials.class.isInstance(credentials));

        final NTCredentials ntCredentials = (NTCredentials) credentials;
        assertEquals(username, ntCredentials.getUserName());
        assertEquals(password, ntCredentials.getPassword());
        assertEquals(clientHost, ntCredentials.getWorkstation());
        assertEquals(domain, ntCredentials.getDomain());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String username = "foo";
        final String password = "password";
        final String host = "my.host";
        final int port = 1234;
        final String realm = "blah";
        final String clientDomain = "MYDOMAIN";
        final String clientHost = "CLIENT.HOST";
        final String scheme = "NTLM";

        DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials(username, password, host, port, realm);
        provider.addNTLMCredentials(username, password, host, port, clientHost, clientDomain);

        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));
        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));

        provider = clone(provider);

        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));
        assertNotNull(provider.getCredentials(new AuthScope(host, port, realm, scheme)));
        assertNull(provider.getCredentials(new AuthScope("invalidHost", port, realm, scheme)));
    }

    /**
     * Test that successive calls to {@link DefaultCredentialsProvider#addCredentials(String, String)}
     * overwrite values previously set.
     * @throws Exception if the test fails
     */
    @Test
    public void overwrite() throws Exception {
        final String realm = "blah";
        final String scheme = new BasicScheme().getSchemeName();

        final DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials("username", "password");

        UsernamePasswordCredentials credentials =
            (UsernamePasswordCredentials) provider.getCredentials(new AuthScope("host", 80, realm, scheme));
        assertEquals("username", credentials.getUserName());
        assertEquals("password", credentials.getPassword());

        provider.addCredentials("username", "new password");
        credentials = (UsernamePasswordCredentials) provider.getCredentials(new AuthScope("host", 80, realm, scheme));
        assertEquals("username", credentials.getUserName());
        assertEquals("new password", credentials.getPassword());

        provider.addCredentials("new username", "other password");
        credentials = (UsernamePasswordCredentials) provider.getCredentials(new AuthScope("host", 80, realm, scheme));
        assertEquals("new username", credentials.getUserName());
        assertEquals("other password", credentials.getPassword());
    }
}
