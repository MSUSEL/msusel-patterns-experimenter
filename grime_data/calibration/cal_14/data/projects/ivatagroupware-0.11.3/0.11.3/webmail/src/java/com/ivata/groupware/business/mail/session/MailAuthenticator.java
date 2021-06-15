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
package com.ivata.groupware.business.mail.session;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


/**
 * <p>Handles email authentication within the {@link
 * com.ivata.groupware.web.tag.LoginTag LoginTag}.</p>
 *
 * @since 2001-09-08
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class MailAuthenticator extends Authenticator implements Serializable {
    /**
     * <p>This username will be used to log into the mail account.</p>
     */
    String userName;

    /**
     * <p>Password used to check user against mail account.</p>
     */
    String password;

    /**
     * <p>Construct an Authenticator with the given username and
     * password.</p>
     *
     * @param userName username used to create a password authentication
     * in
     * <code>getPasswordAuthentication</code>
     * @param password username used to create a password
     * authentication in <code>getPasswordAuthentication</code>
     * @see #getPasswordAuthentication(  )
     *
     */
    public MailAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * <p>Return a PasswordAuthentication for the username and password
     * given in
     * the constructor.</p>
     *
     * @return PasswordAuthentication instantiated using the username and
     * password given in the constructor
     *
     */
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }

    /**
     * <p>Serialize the object to the output stream provided.</p>
     * @exception IOException thrown by
     * <code>ObjectOutputStream.defaultWriteObject(  )</code>
     * @param oos the output stream to serialize the object to
     *
     */
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * <p>Serialize the object from the input stream provided.</p>
     * @exception ClassNotFoundException thrown by
     * <code>ObjectInputStream.defaultReadObject(  )</code>.
     * @exception IOException thrown by
     * <code>ObjectInputStream.defaultReadObject(  )</code>.
     * @param ois the input stream to serialize the object from
     *
     */
    private void readObject(final ObjectInputStream ois)
        throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }
}
