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
//$Id$
package org.hibernate.test.annotations;
import java.io.Serializable;
import java.util.Collection;
import java.util.SortedSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import static org.hibernate.annotations.CascadeType.ALL;


/**
 * Company customer
 *
 * @author Emmanuel Bernard
 */
@Entity
public class Customer implements Serializable {
	Long id;
	String name;
	SortedSet<Ticket> tickets;
	Collection discountTickets;
	Passport passport;

	public Customer() {
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Long long1) {
		id = long1;
	}

	public void setName(String string) {
		name = string;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "CUST_ID")
	@Sort(type = SortType.COMPARATOR, comparator = TicketComparator.class)
	public SortedSet<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(SortedSet<Ticket> tickets) {
		this.tickets = tickets;
	}

	@OneToMany(targetEntity = org.hibernate.test.annotations.Discount.class,
			cascade = CascadeType.ALL, mappedBy = "owner")
	@Cascade({ALL})
	public Collection getDiscountTickets() {
		return discountTickets;
	}

	public void setDiscountTickets(Collection collection) {
		discountTickets = collection;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public Passport getPassport() {
		return passport;
	}

	public void setPassport(Passport passport) {
		this.passport = passport;
	}

}

