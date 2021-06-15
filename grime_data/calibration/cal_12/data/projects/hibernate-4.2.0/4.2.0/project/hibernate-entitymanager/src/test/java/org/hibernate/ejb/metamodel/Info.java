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
package org.hibernate.ejb.metamodel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
@Entity
@Table(name = "INFO_TABLE")
public class Info implements java.io.Serializable {
	private String id;
	private String street;
	private String city;
	private String state;
	private String zip;
	private Spouse spouse;

	public Info() {
	}

	public Info(String v1, String v2, String v3, String v4, String v5) {
		id = v1;
		street = v2;
		city = v3;
		state = v4;
		zip = v5;
	}

	public Info(
			String v1, String v2, String v3, String v4,
			String v5, Spouse v6) {
		id = v1;
		street = v2;
		city = v3;
		state = v4;
		zip = v5;
		spouse = v6;
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String v) {
		id = v;
	}

	@Column(name = "INFOSTREET")
	public String getStreet() {
		return street;
	}

	public void setStreet(String v) {
		street = v;
	}

	@Column(name = "INFOSTATE")
	public String getState() {
		return state;
	}

	public void setState(String v) {
		state = v;
	}

	@Column(name = "INFOCITY")
	public String getCity() {
		return city;
	}

	public void setCity(String v) {
		city = v;
	}

	@Column(name = "INFOZIP")
	public String getZip() {
		return zip;
	}

	public void setZip(String v) {
		zip = v;
	}

	@OneToOne(mappedBy = "info") @JoinTable( name = "INFO_SPOUSE_TABLE" )
	public Spouse getSpouse() {
		return spouse;
	}

	public void setSpouse(Spouse v) {
		this.spouse = v;
	}

}
