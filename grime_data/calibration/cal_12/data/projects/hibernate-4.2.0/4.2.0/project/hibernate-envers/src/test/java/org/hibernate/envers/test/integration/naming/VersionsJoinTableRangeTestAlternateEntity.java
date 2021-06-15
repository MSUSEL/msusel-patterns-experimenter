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
 * Alternate implementation of mapped superclass for Audit join table test.
 * 
 * @author Erik-Berndt Scheper
 * @see VersionsJoinTableRangeTestEntity
 * @see VersionsJoinTableRangeTestEntitySuperClass
 */
@Entity
@Table(name = "RANGE_TEST_ALTERNATE_ENT")
@org.hibernate.envers.Audited
public class VersionsJoinTableRangeTestAlternateEntity extends
		VersionsJoinTableRangeTestEntitySuperClass {

	private String alternateValue;

	/**
	 * Default constructor
	 */
	public VersionsJoinTableRangeTestAlternateEntity() {
		super();
	}

	/**
	 * @return the alternateValue
	 */
	public String getAlternateValue() {
		return alternateValue;
	}

	/**
	 * @param alternateValue
	 *            the alternateValue to set
	 */
	public void setAlternateValue(String alternateValue) {
		this.alternateValue = alternateValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((alternateValue == null) ? 0 : alternateValue.hashCode());
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
		VersionsJoinTableRangeTestAlternateEntity other = (VersionsJoinTableRangeTestAlternateEntity) obj;
		if (alternateValue == null) {
			if (other.alternateValue != null)
				return false;
		} else if (!alternateValue.equals(other.alternateValue))
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
		output.append(" alternateValue = \"").append(this.alternateValue)
				.append("\"}");
		return output.toString();
	}

}
