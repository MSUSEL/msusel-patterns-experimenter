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
//$Id: Customer.java 7858 2005-08-11 21:46:58Z epbernard $

package org.hibernate.test.idclass;






public class Customer {



	public Customer() {

		super();

	}



	public Customer(String orgName, String custName, String add) {

		this.orgName = orgName;

		this.customerName = custName;

		this.address = add;

	}



	private String orgName;

	private String customerName;

	private String address;



	public String getAddress() {

		return address;

	}



	public void setAddress(String address) {

		this.address = address;

	}



	public String getCustomerName() {

		return customerName;

	}



	public void setCustomerName(String customerName) {

		this.customerName = customerName;

	}



	public String getOrgName() {

		return orgName;

	}



	public void setOrgName(String orgName) {

		this.orgName = orgName;

	}



}

