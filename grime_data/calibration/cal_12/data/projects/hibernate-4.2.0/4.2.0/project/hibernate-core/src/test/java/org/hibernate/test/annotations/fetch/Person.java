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
package org.hibernate.test.annotations.fetch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


/**
 * @author Emmanuel Bernard
 */
@Entity
@Table(name = "Person")
public class Person implements Serializable {

	// member declaration
	private int id;
	private String firstName;
	private String lastName;
	private String companyName;
	private Collection<Stay> stays;
	private Collection<Stay> oldStays;
	private Collection<Stay> veryOldStays;
	private List<Stay> orderedStay = new ArrayList<Stay>();

	// constructors
	public Person() {
	}

	public Person(String firstName, String lastName, String companyName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.companyName = companyName;
	}

	// properties
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// relationships

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Collection<Stay> getStays() {
		return this.stays;
	}

	public void setStays(List<Stay> stays) {
		this.stays = stays;
	}

	@OneToMany(cascade=CascadeType.ALL, mappedBy = "oldPerson")
	@LazyCollection(LazyCollectionOption.EXTRA)
	@Fetch(FetchMode.SUBSELECT)
	public Collection<Stay> getOldStays() {
		return oldStays;
	}

	public void setOldStays(Collection<Stay> oldStays) {
		this.oldStays = oldStays;
	}

	@OneToMany(cascade=CascadeType.ALL, mappedBy = "veryOldPerson")
	@Fetch(FetchMode.SELECT)
	public Collection<Stay> getVeryOldStays() {
		return veryOldStays;
	}

	public void setVeryOldStays(Collection<Stay> veryOldStays) {
		this.veryOldStays = veryOldStays;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	@Fetch(FetchMode.SUBSELECT)
	@OrderColumn(name="orderedStayIndex")
	public List<Stay> getOrderedStay() {
		return orderedStay;
	}

	public void setOrderedStay(List<Stay> orderedStay) {
		this.orderedStay = orderedStay;
	}


	// business logic
	public void addStay(Date startDate, Date endDate, String vessel, String authoriser, String comments) {
		Stay stay = new Stay( this, startDate, endDate, vessel, authoriser, comments );
		addStay( stay );
	}

	public void addStay(Stay stay) {
		Collection<Stay> stays = getStays();
		if ( stays == null ) {
			stays = new ArrayList<Stay>();
		}
		stays.add( stay );

		this.stays = stays;
	}

	public void addOldStay(Date startDate, Date endDate, String vessel, String authoriser, String comments) {
		Stay stay = new Stay( this, startDate, endDate, vessel, authoriser, comments );
		addOldStay( stay );
	}

	public void addOldStay(Stay stay) {
		Collection<Stay> stays = getOldStays();
		if ( stays == null ) {
			stays = new ArrayList<Stay>();
		}
		stays.add( stay );

		this.oldStays = stays;
	}

	public void addVeryOldStay(Date startDate, Date endDate, String vessel, String authoriser, String comments) {
		Stay stay = new Stay( this, startDate, endDate, vessel, authoriser, comments );
		addVeryOldStay( stay );
	}

	public void addVeryOldStay(Stay stay) {
		Collection<Stay> stays = getVeryOldStays();
		if ( stays == null ) {
			stays = new ArrayList<Stay>();
		}
		stays.add( stay );

		this.veryOldStays = stays;
	}
}
