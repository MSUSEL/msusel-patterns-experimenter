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
package org.hibernate.test.annotations.onetomany;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table( name = "Order_tbl" )
@IdClass( OrderID.class )
public class Order {
	private String schoolId;
	private Integer schoolIdSort;
	private Integer academicYear;

	private List<OrderItem> itemList = new ArrayList<OrderItem>();

	public boolean equals(Object obj) {
		return super.equals( obj );
	}

	public int hashCode() {
		return 10;
	}

	@Id
	public Integer getAcademicYear() {
		return this.academicYear;
	}

	protected void setAcademicYear(Integer academicYear) {
		this.academicYear = academicYear;
	}

	@Id
	public String getSchoolId() {
		return this.schoolId;
	}

	protected void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	@OneToMany( mappedBy = "order" )
	@OrderBy( "dayNo desc" )
	public List<OrderItem> getItemList() {
		return this.itemList;
	}

	public void setItemList(List<OrderItem> itemList) {
		this.itemList = itemList;
	}

	public Integer getSchoolIdSort() {
		return this.schoolIdSort;
	}

	public void setSchoolIdSort(Integer schoolIdSort) {
		this.schoolIdSort = schoolIdSort;
	}


}