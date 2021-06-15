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
//$Id: Location.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.io.Serializable;
import java.util.Locale;

public class Location implements Serializable {
	private int streetNumber;
	private String city;
	private String streetName;
	private String countryCode;
	private Locale locale;
	private String description;
	
	/**
	 * Returns the countryCode.
	 * @return String
	 */
	public String getCountryCode() {
		return countryCode;
	}
	
	/**
	 * Returns the description.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the locale.
	 * @return Locale
	 */
	public Locale getLocale() {
		return locale;
	}
	
	/**
	 * Returns the streetName.
	 * @return String
	 */
	public String getStreetName() {
		return streetName;
	}
	
	/**
	 * Returns the streetNumber.
	 * @return int
	 */
	public int getStreetNumber() {
		return streetNumber;
	}
	
	/**
	 * Sets the countryCode.
	 * @param countryCode The countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	/**
	 * Sets the description.
	 * @param description The description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Sets the locale.
	 * @param locale The locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * Sets the streetName.
	 * @param streetName The streetName to set
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	
	/**
	 * Sets the streetNumber.
	 * @param streetNumber The streetNumber to set
	 */
	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}
	
	/**
	 * Returns the city.
	 * @return String
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Sets the city.
	 * @param city The city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	public boolean equals(Object other) {
		Location l = (Location) other;
		return l.getCity().equals(city) && l.getStreetName().equals(streetName) && l.getCountryCode().equals(countryCode) && l.getStreetNumber()==streetNumber;
	}
	public int hashCode() {
		return streetName.hashCode();
	}
	
}






