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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
@Entity
@Table(name = "CREDITCARD_TABLE")
public class CreditCard implements java.io.Serializable {
	private String id;
	private String number;
	private String type;
	private String expires;
	private boolean approved;
	private double balance;
	private Order order;
	private Customer customer;

	public CreditCard() {
	}

	public CreditCard(
			String v1, String v2, String v3, String v4,
			boolean v5, double v6, Order v7, Customer v8) {
		id = v1;
		number = v2;
		type = v3;
		expires = v4;
		approved = v5;
		balance = v6;
		order = v7;
		customer = v8;
	}

	public CreditCard(
			String v1, String v2, String v3, String v4,
			boolean v5, double v6) {
		id = v1;
		number = v2;
		type = v3;
		expires = v4;
		approved = v5;
		balance = v6;
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String v) {
		id = v;
	}

	@Column(name = "CREDITCARD_NUMBER")
	public String getNumber() {
		return number;
	}

	public void setNumber(String v) {
		number = v;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String v) {
		type = v;
	}

	@Column(name = "EXPIRES")
	public String getExpires() {
		return expires;
	}

	public void setExpires(String v) {
		expires = v;
	}

	@Column(name = "APPROVED")
	public boolean getApproved() {
		return approved;
	}

	public void setApproved(boolean v) {
		approved = v;
	}

	@Column(name = "BALANCE")
	public double getBalance() {
		return balance;
	}

	public void setBalance(double v) {
		balance = v;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_FOR_ORDER_TABLE")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order v) {
		order = v;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK3_FOR_CUSTOMER_TABLE")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer v) {
		customer = v;
	}
}
