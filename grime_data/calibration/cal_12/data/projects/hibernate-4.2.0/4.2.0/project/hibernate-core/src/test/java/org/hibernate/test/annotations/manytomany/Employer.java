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
package org.hibernate.test.annotations.manytomany;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * Employer in a employer-Employee relationship
 *
 * @author Emmanuel Bernard
 */
@Entity()
@Table(name="`Employer`")
@SuppressWarnings({"serial", "unchecked"})
public class Employer implements Serializable {
	private Integer id;
	private Collection employees;
	private List contractors;

	@ManyToMany(
			targetEntity = org.hibernate.test.annotations.manytomany.Contractor.class,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE}
	)
	@JoinTable(
			name = "EMPLOYER_CONTRACTOR",
			joinColumns = {@JoinColumn(name = "EMPLOYER_ID")},
			inverseJoinColumns = {@JoinColumn(name = "CONTRACTOR_ID")}
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@OrderBy("name desc")	
	public List getContractors() {
		return contractors;
	}

	public void setContractors(List contractors) {
		this.contractors = contractors;
	}

	@ManyToMany(
			targetEntity = org.hibernate.test.annotations.manytomany.Employee.class,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE}
	)
	@JoinTable(
			name = "EMPLOYER_EMPLOYEE",
			joinColumns = {@JoinColumn(name = "EMPER_ID")},
			inverseJoinColumns = {@JoinColumn(name = "EMPEE_ID")}
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@OrderBy("name asc")
	public Collection getEmployees() {
		return employees;
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setEmployees(Collection set) {
		employees = set;
	}

	public void setId(Integer integer) {
		id = integer;
	}
}
