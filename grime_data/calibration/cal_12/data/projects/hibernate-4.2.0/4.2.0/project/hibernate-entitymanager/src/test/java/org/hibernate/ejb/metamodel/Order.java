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
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
@Entity
@Table(name = "ORDER_TABLE")
public class Order implements java.io.Serializable {
	private String id;
	private double totalPrice;
	private Customer customer;
	private CreditCard creditCard;
	private LineItem sampleLineItem;
	private Collection<LineItem> lineItems = new java.util.ArrayList<LineItem>();

	private char[] domen;
	private byte[] number;

	public Order() {
	}

	public Order(String id, double totalPrice) {
		this.id = id;
		this.totalPrice = totalPrice;
	}

	public Order(String id, Customer customer) {
		this.id = id;
		this.customer = customer;
	}

	public Order(String id, char[] domen) {
		this.id = id;
		this.domen = domen;
	}


	public Order(String id) {
		this.id = id;
	}

	//====================================================================
	// getters and setters for State fields

	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TOTALPRICE")
	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double price) {
		this.totalPrice = price;
	}

	//====================================================================
	// getters and setters for Association fields

	// MANYx1

	@ManyToOne
	@JoinColumn(
			name = "FK4_FOR_CUSTOMER_TABLE")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	//1x1

	@OneToOne(mappedBy = "order")
	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard cc) {
		this.creditCard = cc;
	}

	// 1x1

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(
			name = "FK0_FOR_LINEITEM_TABLE")
	public LineItem getSampleLineItem() {
		return sampleLineItem;
	}

	public void setSampleLineItem(LineItem l) {
		this.sampleLineItem = l;
	}

	//1xMANY

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	public Collection<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(Collection<LineItem> c) {
		this.lineItems = c;
	}

	public char[] getDomen() {
		return domen;
	}

	public void setDomen(char[] d) {
		domen = d;
	}

	@Column(name="fld_number")
	public byte[] getNumber() {
		return number;
	}

	public void setNumber(byte[] n) {
		number = n;
	}


}
