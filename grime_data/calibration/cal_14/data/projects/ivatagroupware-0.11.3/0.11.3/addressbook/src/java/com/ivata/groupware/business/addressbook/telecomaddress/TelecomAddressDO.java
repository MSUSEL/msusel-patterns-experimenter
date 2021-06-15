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


import com.ivata.groupware.container.persistence.NamedDO;


/**
 * <p>Represents an email, web site URL, telephone number or fax number. The
 * actual type of the number depends on the setting of the type fields,
 * which should be set to one of the constants in {@link TelecomAddressConstants
 * TelecomAddressConstants}.</p>
 *
 * @since 2002-05-14
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="telecom_address"
 * @hibernate.cache
 *      usage="read-write"
 */
public class TelecomAddressDO  extends NamedDO {
    /**
     * <p>The actual address value. Depending on the value of the type
     * field, this will be a telephone/fax number, URL or email address.</p>
     */
    private String address;

    /**
     * <p>Get the order in which this address should appear on the screen. Lower
     * numbers mean that the address will appear higher in any listing.</p>
     */
    private int number;

    /**
     * <p>The type of the address to a numeric value representing on of the
     * possible telecommunication types.</p>
     */
    private int type;

    /**
    /**
     * <p>Get the actual address value. Depending on the value of the type
     * field, this will be a telephone/fax number, URL or email address.</p>
     *
     * @return the telephone number, email address or URL string this
     *     telecommunications address represents.
     *
     * @hibernate.property
     */
    public final String getAddress() {
        return address;
    }
    /**
     * <p>
     * Get the 'name' of this telecom address - just returns the address value.
     * </p>
     *
     * @return see {@link #getAdddress}.
     * @see com.ivata.groupware.container.persistence.NamedDO#getName()
     */
    public final String getName() {
        return getAddress();
    }

    /**
     * <p>Get the order in which this address should appear on the screen. Lower
     * numbers mean that the address will appear higher in any listing.</p>
     *
     * @return number used for sorting the address to display. Lower numbers
     *     mean higher significance.
     *
     * @hibernate.property
     */
    public final int getNumber() {
        return number;
    }
    /**
     * <p>Get the type of the address to a numeric value representing on of the
     * possible telecommunication types.</p>
     *
     * @return the type of the address, defined in {@link
     *     TelecomAddressConstants TelecomAddressConstants}.
     *
     * @hibernate.property
     *      column="address_type"
     */
    public final int getType() {
        return type;
    }

    /**
     * <p>Set the actual address value. Depending on the value of the type
     * field, this will be a telephone/fax number, URL or email address.</p>
     *
     * @param address the telephone number, email address or URL string this
     *     telecommunications address represents.
     */
    public final void setAddress(final String address) {
        this.address = address;
    }

    /**
     * <p>Set the order in which this address should appear on the screen. Lower
     * numbers mean that the address will appear higher in any listing.</p>
     *
     * @param number number used for sorting the address to display. Lower
     *     numbers mean higher significance.
     */
    public final void setNumber(final int number) {
        this.number = number;
    }

    /**
     * <p>Set the type of the address to a numeric value representing on of the
     * possible telecommunication types.</p>
     *
     * @param type the type of the address, defined in {@link
     *     TelecomAddressConstants TelecomAddressConstants}.
     * @see TelecomAddressConstants
     */
    public final void setType(final int type) {
        this.type = type;
    }
}
