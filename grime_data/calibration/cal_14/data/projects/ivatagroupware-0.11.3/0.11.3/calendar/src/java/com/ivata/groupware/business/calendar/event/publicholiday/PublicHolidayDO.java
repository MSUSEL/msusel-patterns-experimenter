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
package com.ivata.groupware.business.calendar.event.publicholiday;

import com.ivata.groupware.business.addressbook.address.country.CountryDO;
import com.ivata.groupware.business.calendar.event.EventDO;


/**
 * <p>Public holiday is event with country and region code. TODO:</p>
 *
 * @since 2002-08-02
 * @author Jan Boros <janboros@sourceforge.net>
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 *
 * @hibernate.joined-subclass
 *      table="public_holiday"
 * @hibernate.joined-subclass-key
 *      column="calendar_event"
 */
public class PublicHolidayDO  extends EventDO {
    /**
     * <p>The country to which this public holiday applies.</p>
     */
    private CountryDO country;

    /**
     * <p>Get the region code to which this public holiday applies.</p>
     */
    private String regionCode;
    /**
     * <p>Get the country to which this public holiday applies.</p>
     *
     * @return the country to which this public holiday applies.
     *
     * @hibernate.many-to-one
     */
    public final CountryDO getCountry() {
        return country;
    }
    /**
     * <p>Get the region code to which this public holiday applies.</p>
     *
     * @return region code to which this public holiday applies.
     *
     * @hibernate.property
     *      column="region_code"
     */
    public final String getRegionCode() {
        return regionCode;
    }

    /**
     * <p>Set the country to which this public holiday applies.</p>
     *
     * @param country to which this public holiday applies.
     */
    public final void setCountry(final CountryDO country) {
        this.country = country;
    }

    /**
     * <p>Set the region code to which this public holiday applies.</p>
     *
     * @param region code to which this public holiday applies.
     */
    public final void setRegionCode(final String regionCode) {
        this.regionCode = regionCode;
    }
}
