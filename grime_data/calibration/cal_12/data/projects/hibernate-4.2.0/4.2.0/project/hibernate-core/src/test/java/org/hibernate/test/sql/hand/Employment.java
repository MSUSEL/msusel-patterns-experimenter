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
//$Id: Employment.java 11486 2007-05-08 21:57:24Z steve.ebersole@jboss.com $
package org.hibernate.test.sql.hand;
import java.util.Date;

/**
 * @author Gavin King
 */
public class Employment {
	private long employmentId;
	private Person employee;
	private Organization employer;
	private Date startDate;
	private Date endDate;
	private String regionCode;
	private MonetaryAmount salary;
	
	public Employment() {}

	public Employment(Person employee, Organization employer, String regionCode) {
		this.employee = employee;
		this.employer = employer;
		this.startDate = new Date();
		this.regionCode = regionCode;
		employer.getEmployments().add(this);
	}
	/**
	 * @return Returns the employee.
	 */
	public Person getEmployee() {
		return employee;
	}
	/**
	 * @param employee The employee to set.
	 */
	public void setEmployee(Person employee) {
		this.employee = employee;
	}
	/**
	 * @return Returns the employer.
	 */
	public Organization getEmployer() {
		return employer;
	}
	/**
	 * @param employer The employer to set.
	 */
	public void setEmployer(Organization employer) {
		this.employer = employer;
	}
	/**
	 * @return Returns the endDate.
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the id.
	 */
	public long getEmploymentId() {
		return employmentId;
	}
	/**
	 * @param id The id to set.
	 */
	public void setEmploymentId(long id) {
		this.employmentId = id;
	}
	/**
	 * @return Returns the startDate.
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return Returns the regionCode.
	 */
	public String getRegionCode() {
		return regionCode;
	}
	/**
	 * @param regionCode The regionCode to set.
	 */
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	
	public MonetaryAmount getSalary() {
		return salary;
	}
	
	public void setSalary(MonetaryAmount salary) {
		this.salary = salary;
	}
}
