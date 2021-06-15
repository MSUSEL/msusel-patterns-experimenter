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
package com.ivata.groupware.business.addressbook.address.country;


import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Stores all valid countries in the system. These countries are used in
 * public holidays, employee records as well as addresses.</p>
 *
 * @since 2002-09-01
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="address_country"
 * @hibernate.cache
 *      usage="read-write"
 */
public class CountryDO extends BaseDO {

    /**
     * <p>Store the two-letter country code here.</p>
     */
    private String code;

    /**
     * <p>Store the name of the country.</p>
     */
    private String name;

    /**
     * <p>Priority of this country in the system. Lower = better.</p>
     */
    private int priority;

    /**
     * <p>Get the country code for this country.</p>
     *
     * @return two letter country abbreviation following the standards of
     * internet TLDs.
     *
     * @hibernate.property
     */
    public final String getCode() {
        return code;
    }

    /**
     * Just returns the country name.
     * Refer to {@link #getName}.
     * @return Refer to {@link #getName}.
     */
    public String getDisplayValue() {
        return getName();
    }

    /**
     * <p>Get the name of the country.</p>
     *
     * @return US English clear-text country name.
     *
     * @hibernate.property
     */
    public final String getName() {
        return name;
    }

    /**
     * <p>Priority of this country in the system for lists. Lower = better.</p>
     *
     * @return priority of this country in the system. Lower = better.
     * @hibernate.property
     */
    public final int getPriority() {
        return priority;
    }
    /**
     * <p>Set the country code for this country.</p>
     *
     * @param code two letter country abbreviation following the standards of
     * internet TLDs.
     */
    public final void setCode(final String code) {
        this.code = code;
    }

    /**
     * <p>Set the name of the country.</p>
     *
     * @param name US English clear-text country name.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * <p>Priority of this country in the system for lists. Lower = better.</p>
     * @param i priority of this country in the system. Lower = better.
     */
    public final void setPriority(final int i) {
        priority = i;
    }
}
