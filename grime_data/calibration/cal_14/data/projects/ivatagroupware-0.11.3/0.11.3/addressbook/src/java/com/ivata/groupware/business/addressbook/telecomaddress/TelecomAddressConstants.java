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
package com.ivata.groupware.business.addressbook.telecomaddress;


/**
 * <p>Contains all constants and constant conversion in use for telecom addresses.
 * Use these constats to get the clear text name of the telecom address type, or
 * to find the correct type to store in the EJB.</p>
 *
 * @since 2002-05-14
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class TelecomAddressConstants {

    /**
     * <p>Represents a home telephone number.</p>
     */
    public final static int TYPE_HOME = 0;

    /**
     * <p>Represents a work telephone number.</p>
     */
    public final static int TYPE_WORK = 1;

    /**
     * <p>Represents a mobile telephone number.</p>
     */
    public final static int TYPE_MOBILE = 2;

    /**
     * <p>Represents a fax telephone number.</p>
     */
    public final static int TYPE_FAX = 3;

    /**
     * <p>Represents an email address.</p>
     */
    public final static int TYPE_EMAIL = 4;

    /**
     * <p>Represents a web site address.</p>
     */
    public final static int TYPE_URL = 5;

    /**
     * <p>This is used to translate the different address types.</p>
     *
     * @see #getTelecomAddressTypeName( int telecomAddressType )
     */
    private final static String[  ] typeNames = {
        "person.label.telecomaddress.home",
        "person.label.telecomaddress.work",
        "person.label.telecomaddress.mobile",
        "person.label.telecomaddress.fax",
        "person.label.telecomaddress.email",
        "person.label.telecomaddress.uRL"};

    /**
     * <p>Tell client routines how many telecom address types there are.</p>
     *
     * @return the number of telecom address types.
     */
    public static int countTypes() {
        return typeNames.length;
    }

    /**
     * <p>Translates the code assigned to a a telecom address type into a text
     * representing it.<p>
     *
     * @param type the numeric type to be converted into a text.
     * @return the code assigned to a a telecom address type translated into a
     *     text representing it.
     */
    public final static String getTypeName(int type) {
        return typeNames[type];
    }
}
