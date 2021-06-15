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
package org.apache.james.fetchmail;

import javax.mail.Session;

import org.apache.avalon.framework.configuration.ConfigurationException;

public class DynamicAccount extends Account
{

    /**
     * Constructor for DynamicAccount.
     * @param sequenceNumber
     * @param parsedConfiguration
     * @param user
     * @param password
     * @param recipient
     * @param ignoreRecipientHeader
     * @param session
     * @throws ConfigurationException
     */
    private DynamicAccount(
        int sequenceNumber,
        ParsedConfiguration parsedConfiguration,
        String user,
        String password,
        String recipient,
        boolean ignoreRecipientHeader,
        Session session)
        throws ConfigurationException
    {
        super(
            sequenceNumber,
            parsedConfiguration,
            user,
            password,
            recipient,
            ignoreRecipientHeader,
            session);
    }

    /**
     * Constructor for DynamicAccount.
     * @param sequenceNumber
     * @param parsedConfiguration
     * @param userName
     * @param userPrefix 
     * @param userSuffix
     * @param password
     * @param recipientPrefix 
     * @param recipientSuffix  
     * @param ignoreRecipientHeader
     * @param session
     * @throws ConfigurationException
     */
    public DynamicAccount(
        int sequenceNumber,
        ParsedConfiguration parsedConfiguration,
        String userName,
        String userPrefix,
        String userSuffix,
        String password,
        String recipientPrefix,
        String recipientSuffix,
        boolean ignoreRecipientHeader,
        Session session)
        throws ConfigurationException
    {
        this(
            sequenceNumber,
            parsedConfiguration,
            null,
            password,
            null,
            ignoreRecipientHeader,
            session);

        StringBuffer userBuffer = new StringBuffer(userPrefix);
        userBuffer.append(userName);
        userBuffer.append(userSuffix);
        setUser(userBuffer.toString());

        StringBuffer recipientBuffer = new StringBuffer(recipientPrefix);
        recipientBuffer.append(userName);
        recipientBuffer.append(recipientSuffix);
        setRecipient(recipientBuffer.toString());
    }
}
