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
// $Id: Address.java 7996 2005-08-22 14:49:57Z steveebersole $
package org.hibernate.test.hql;


/**
 * Implementation of Address.
 *
 * @author Steve Ebersole
 */
public class Address {
	private String street;
	private String city;
	private String postalCode;
	private String country;
	private StateProvince stateProvince;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public StateProvince getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(StateProvince stateProvince) {
		this.stateProvince = stateProvince;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Address address = ( Address ) o;

		if ( city != null ? !city.equals( address.city ) : address.city != null ) {
			return false;
		}
		if ( country != null ? !country.equals( address.country ) : address.country != null ) {
			return false;
		}
		if ( postalCode != null ? !postalCode.equals( address.postalCode ) : address.postalCode != null ) {
			return false;
		}
		if ( stateProvince != null ? !stateProvince.equals( address.stateProvince ) : address.stateProvince != null ) {
			return false;
		}
		if ( street != null ? !street.equals( address.street ) : address.street != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = street != null ? street.hashCode() : 0;
		result = 31 * result + ( city != null ? city.hashCode() : 0 );
		result = 31 * result + ( postalCode != null ? postalCode.hashCode() : 0 );
		result = 31 * result + ( country != null ? country.hashCode() : 0 );
		result = 31 * result + ( stateProvince != null ? stateProvince.hashCode() : 0 );
		return result;
	}
}
