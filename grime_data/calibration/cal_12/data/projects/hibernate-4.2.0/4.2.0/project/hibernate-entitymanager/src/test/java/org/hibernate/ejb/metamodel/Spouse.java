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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
@Entity
@Table(name = "SPOUSE_TABLE")
public class Spouse implements java.io.Serializable {
	private String id;
	private String first;
	private String maiden;
	private String last;
	private String sNumber;
	private Info info;
	private Customer customer;

	public Spouse() {
	}

	public Spouse(
			String v1, String v2, String v3, String v4,
			String v5, Info v6) {
		id = v1;
		first = v2;
		maiden = v3;
		last = v4;
		sNumber = v5;
		info = v6;
	}


	public Spouse(
			String v1, String v2, String v3, String v4,
			String v5, Info v6, Customer v7) {
		id = v1;
		first = v2;
		maiden = v3;
		last = v4;
		sNumber = v5;
		info = v6;
		customer = v7;
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String v) {
		id = v;
	}

	@Column(name = "FIRSTNAME")
	public String getFirstName() {
		return first;
	}

	public void setFirstName(String v) {
		first = v;
	}

	@Column(name = "MAIDENNAME")
	public String getMaidenName() {
		return maiden;
	}

	public void setMaidenName(String v) {
		maiden = v;
	}

	@Column(name = "LASTNAME")
	public String getLastName() {
		return last;
	}

	public void setLastName(String v) {
		last = v;
	}

	@Column(name = "SOCSECNUM")
	public String getSocialSecurityNumber() {
		return sNumber;
	}

	public void setSocialSecurityNumber(String v) {
		sNumber = v;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_FOR_INFO_TABLE")
	public Info getInfo() {
		return info;
	}

	public void setInfo(Info v) {
		info = v;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK7_FOR_CUSTOMER_TABLE")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer v) {
		customer = v;
	}

}
