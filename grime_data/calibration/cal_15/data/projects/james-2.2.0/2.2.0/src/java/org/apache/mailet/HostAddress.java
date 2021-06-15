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
package org.apache.mailet;

/* A specialized subclass of javax.mail.URLName, which provides location
 * information for servers.
 * 
 * @since Mailet API v2.2.0a16-unstable
 */
public class HostAddress extends javax.mail.URLName
{
    private String hostname;

    public HostAddress(String hostname, String url)
    {
        super(url);
        this.hostname = hostname;
    }

    public String getHostName()
    {
        return hostname;
    }

/*
    public static void main(String[] args) throws Exception
    {
        HostAddress url;
        try
        {
            url = new HostAddress("mail.devtech.com", "smtp://" + "66.112.202.2" + ":25");
            System.out.println("Hostname: " + url.getHostName());
            System.out.println("The protocol is: " + url.getProtocol());
            System.out.println("The host is: " + url.getHost());
            System.out.println("The port is: " + url.getPort());
            System.out.println("The user is: " + url.getUsername());
            System.out.println("The password is: " + url.getPassword());
            System.out.println("The file is: " + url.getFile());
            System.out.println("The ref is: " + url.getRef());
        }
        catch (Exception e)
        {
            System.err.println(e);
        };
    }
*/
}
