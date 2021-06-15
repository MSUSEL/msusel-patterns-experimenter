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
package com.ivata.groupware.business.addressbook.address;

import com.ivata.groupware.business.addressbook.address.country.CountryDO;
import com.ivata.groupware.business.addressbook.person.PersonDO;
import com.ivata.groupware.container.persistence.BaseDO;

/**
 * <p>Represents a street address within the system. The current model will allow
 * each person to have multiple street addresses, though the JSP front-end is
 * currently restricted to just one.</p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   2002-05-15
 * @version $Revision: 1.4 $
 *
 * @hibernate.class
 *      table="address"
 * @hibernate.cache
 *      usage="read-write"
 */
public class AddressDO extends BaseDO {
    /**
     * <p>
     * Used to separate all the values in the display value.
     * </p>
     */
    private final static String DISPLAY_VALUE_SEPERATOR = ", ";
    /**
     * Country this address is in.
     */
    private CountryDO country;
    /**
     * <p>
     * In this version of ivata groupware, each address is associated with a single
     * person.
     * </p>
     */
    private PersonDO person;
    /**
     * Zipcode or postcode. Can be aphanumeric in some countries (UK/CA).
     */
    private String postCode;
    /**
     * <p>
     * State or county/region within country.
     * </p>
     */
    private String region;
    /**
     * <p>
     * House name/house number, street and district.
     * </p>
     */
    private String streetAddress;
    /**
     * <p>
     * Town or city.
     * </p>
     */
    private String town;

    /**
     * <p>
     * Get the country of an address.
     * </p>
     *
     * @return country for this address bean.
     * @hibernate.many-to-one
     *      column="address_country"
     */
    public final  CountryDO getCountry() {
        return country;
    }

    /**
     * <p>
     * For this class, shows the whole address.
     * </p>
     *
     * @see com.ivata.mask.valueobject.ValueObject#getDisplayValue()
     */
    public final String getDisplayValue() {
        StringBuffer displayValue = new StringBuffer();
        if (streetAddress != null) {
            displayValue.append(streetAddress);
        }
        if (town != null) {
            if (displayValue.length() > 0) {
                displayValue.append(DISPLAY_VALUE_SEPERATOR);
            }
            displayValue.append(town);
        }
        if (postCode != null) {
            if (displayValue.length() > 0) {
                displayValue.append(DISPLAY_VALUE_SEPERATOR);
            }
            displayValue.append(postCode);
        }
        if (region != null) {
            if (displayValue.length() > 0) {
                displayValue.append(DISPLAY_VALUE_SEPERATOR);
            }
            displayValue.append(region);
        }
        if (country != null) {
            if (displayValue.length() > 0) {
                displayValue.append(DISPLAY_VALUE_SEPERATOR);
            }
            displayValue.append(country.getDisplayValue());
        }
        return displayValue.toString();
    }
    /**
     * @return Returns the person.
     * @hibernate.one-to-one
     */
    public PersonDO getPerson() {
        return person;
    }

    /**
     * <p>
     * Get the zipcode or postcode. Can be aphanumeric in some countries (UK/CA).
     * </p>
     *
     * @return zipcode or postcode. Can be aphanumeric in some countries
     *     (UK/CA).
     * @hibernate.property
     *      column="post_code"
     */
    public final  String getPostCode() {
        return postCode;
    }

    /**
     * <p>
     * Get the state or county/region within country.
     * </p>
     *
     * @return state or county/region within country.
     * @hibernate.property
     */
    public final  String getRegion() {
        return region;
    }
    /**
     * <p>Get the house name/house number, street and district.</p>
     *
     * @return the house name/house number, street and district.
     * @hibernate.property
     *      column="street_address"
     */
    public final  String getStreetAddress() {
        return streetAddress;
    }
    /**
     * <p>Get the town or city.</p>
     *
     * @return the town or city.
     * @hibernate.property
     */
    public final  String getTown() {
        return town;
    }

    /**
     * <p>Set the country of an address.</p>
     *
     * @param country the new country for this address bean.
     */
    public final  void setCountry(final CountryDO country) {
        this.country = country;
    }
    /**
     * @param person The person to set.
     */
    public final void setPerson(final PersonDO person) {
        this.person = person;
    }

    /**
     * <p>Set the zipcode or postcode. Can be aphanumeric in some countries
     * (UK/CA).</p>
     *
     * @param postCode zipcode or postcode. Can be aphanumeric in some countries
     *      (UK/CA).
     */
    public final  void setPostCode(final String postCode) {
        this.postCode = postCode;
    }

    /**
     * <p>Set the state or county/region within country.</p>
     *
     * @param region state or county/region within country.
     */
    public final  void setRegion(final String region) {
        this.region = region;
    }

    /**
     * <p>Set the house name/house number, street and district.</p>
     *
     * @param streetAddress house name/house number, street and district.
     */
    public final  void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }
    /**
     * <p>Set the town or city.</p>
     *
     * @param town or city.
     */
    public final  void setTown(final String town) {
        this.town = town;
    }
    /**
     * Overridden to set the id type.
     * @see com.ivata.groupware.container.persistence.BaseDO#getId()
     * @return current identifier.
     * @hibernate.id
     *      generator-class="foreign"
     * @hibernate.generator-param
     *      name="property"
     *      value="person"
     */
    public Integer getId() {
        return super.getId();
    }
}
