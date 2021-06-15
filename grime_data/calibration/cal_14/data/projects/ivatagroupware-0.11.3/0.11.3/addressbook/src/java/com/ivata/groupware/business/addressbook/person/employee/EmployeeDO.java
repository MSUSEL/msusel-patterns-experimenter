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
package com.ivata.groupware.business.addressbook.person.employee;


import com.ivata.groupware.business.addressbook.address.country.CountryDO;
import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Tracks which people are employees and the number of vacation days they
 * have remaining for the current calendar year.</p>
 *
 * <p>This is a dependent value class, used to pass data back from the.</p>
 * {@link EmployeeBean EmployeeBean} to a client application.</p>
 *
 * <p><strong>Note:</strong> This class provides data from {@link EmployeeBean EmployeeBean}.
 * This is no local copy of the bean class, however, and changes here
 * will not be automatically reflected in {@link EmployeeBean EmployeeBean}.</p>
 *
 * @since   2002-08-30
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 *
 * @hibernate.class
 *      table="person_employee"
 * @hibernate.cache
 *      usage="read-write"
 */
public class EmployeeDO extends BaseDO {
    /**
     * Static definitiaion of the display value prefix.
     */
    public static final String DISPLAY_VALUE_PREFIX = "[employee] ";
    /**
     * <p>Store the country for this employee.</p>
     */
    private CountryDO country;

    /**
     * <p>Store the internal company employee number. This number is used
     * by the client company for reference/internal use and is not required by
     * the ivata groupware system.</p>
     */
    private String number;

    /**
     * <p>Store the region for this employee. The region code together with the
     * country code is used to filter the employee's public holidays in the
     * calendar.</p>
     */
    private String regionCode;

    /**
     * <p>Store the total number of days vacation this employee has for the
     * current calendar year. <strong>Note:</strong> this is the total number of days
     * for the employee, not the number of days remaining. The system calculates
     * the days remaining by adding all previous vacation days within the
     * current calendar year.</p>
     */
    private Integer vacationDays;

    /**
     * <p>Get the country for this employee.</p>
     *
     * @return the country for the employee.
     * @hibernate.many-to-one
     *      column="address_country"
     */
    public final CountryDO getCountry() {
        return country;
    }
    /**
     * Just returns the text "[employee] " followed by the employee number.
     * Refer to {@link #getName}.
     * @return Refer to {@link #getName}.
     */
    public String getDisplayValue() {
        return DISPLAY_VALUE_PREFIX + getNumber();
    }
    /**
     * <p>Get the internal company employee number. This number is used
     * by the client company for reference/internal use and is not required by
     * the ivata groupware system.</p>
     *
     * @return employee number, for company records. Need not be numeric only.
     * @hibernate.property
     *      column="number"
     */
    public final String getNumber() {
        return number;
    }

    /**
     * <p>Get the region for this employee. The region code together with the
     * country code is used to filter the employee's public holidays in the
     * calendar.</p>
     *
     * @return text value for the region code.
     * @hibernate.property
     *      column="region_code"
     */
    public final String getRegionCode() {
        return regionCode;
    }

    /**
     * <p>Get the total number of days vacation this employee has for the
     * current calendar year. <strong>Note:</strong> this is the total number of days
     * for the employee, not the number of days remaining. The system calculates
     * the days remaining by adding all previous vacation days within the
     * current calendar year.</p>
     *
     * @return total number of days vacation for this employee for the current
     * calendar year.
     * @hibernate.property
     *      column="vacation_days"
     */
    public final Integer getVacationDays() {
        return vacationDays;
    }
    /**
     * <p>Set the country for this employee.</p>
     *
     * @param country new value of the country for the employee.
     */
    public final void setCountry(final CountryDO country) {
        this.country = country;
    }

    /**
     * <p>Set the internal company employee number. This number is used
     * by the client company for reference/internal use and is not required by
     * the ivata groupware system.</p>
     *
     * @param number employee number, for company records. Need not be numeric
     * only.
     */
    public final void setNumber(final String number) {
        this.number = number;
    }

    /**
     * <p>Set the region for this employee. The region code together with the
     * country code is used to filter the employee's public holidays in the
     * calendar.</p>
     *
     * @param regionCode new text value for the region code. Can be a maximum of
     * ten characters.
     */
    public final void setRegionCode(final String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * <p>Set the total number of days vacation this employee has for the
     * current calendar year. <strong>Note:</strong> this is the total number of days
     * for the employee, not the number of days remaining. The system calculates
     * the days remaining by adding all previous vacation days within the
     * current calendar year.</p>
     *
     * @param vacationDays total number of days vacation for this employee for
     * the current calendar year.
     */
    public final void setVacationDays(final Integer vacationDays) {
        this.vacationDays = vacationDays;
    }
}
