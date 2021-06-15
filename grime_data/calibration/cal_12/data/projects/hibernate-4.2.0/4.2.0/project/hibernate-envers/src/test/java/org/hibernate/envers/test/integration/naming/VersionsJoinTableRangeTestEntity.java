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
package org.hibernate.envers.test.integration.naming;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Concrete implementation of mapped superclass for Audit join table test.
 * 
 * @author Erik-Berndt Scheper
 * @see VersionsJoinTableRangeTestAlternateEntity
 * @see VersionsJoinTableRangeTestEntitySuperClass
 */
@Entity
@Table(name = "RANGE_TEST_ENTITY")
@org.hibernate.envers.Audited
public class VersionsJoinTableRangeTestEntity extends
		VersionsJoinTableRangeTestEntitySuperClass {

	private String value;

	/**
	 * Default constructor
	 */
	public VersionsJoinTableRangeTestEntity() {
		super();
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersionsJoinTableRangeTestEntity other = (VersionsJoinTableRangeTestEntity) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();

		output.append("VersionsJoinTableRangeComponentTestEntity {");
		output.append(" id = \"").append(getId()).append("\", ");
		output.append(" genericValue = \"").append(getGenericValue()).append(
				"\", ");
		output.append(" value = \"").append(this.value).append("\"}");
		return output.toString();
	}

}
