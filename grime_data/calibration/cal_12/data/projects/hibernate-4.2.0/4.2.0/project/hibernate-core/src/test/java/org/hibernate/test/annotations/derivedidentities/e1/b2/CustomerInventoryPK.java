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
package org.hibernate.test.annotations.derivedidentities.e1.b2;
import java.io.Serializable;


public class CustomerInventoryPK implements Serializable {

	private Integer id;

	private Customer customer;

	public CustomerInventoryPK() {
	}

	public CustomerInventoryPK(Integer id, Customer customer) {
		this.id = id;
		this.customer = customer;
	}

	public boolean equals(Object other) {
		if ( other == this ) {
			return true;
		}
		if ( other == null || getClass() != other.getClass() ) {
			return false;
		}
		CustomerInventoryPK cip = ( CustomerInventoryPK ) other;
		return ( getCustomer().getId() == cip.getCustomer().getId() && ( id == cip.id ||
				( id != null && id.equals( cip.id ) ) ) );
	}

	public int hashCode() {
		return ( id == null ? 0 : id.hashCode() ) ^ getCustomer().getId();
	}

	public Integer getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}


}
