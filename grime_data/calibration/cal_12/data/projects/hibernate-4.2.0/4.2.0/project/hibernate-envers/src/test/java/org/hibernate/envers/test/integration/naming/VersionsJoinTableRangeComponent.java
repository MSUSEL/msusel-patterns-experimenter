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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.FetchMode;

/**
 * An embeddable component containing a list of
 * {@link VersionsJoinTableRangeTestEntitySuperClass}-instances
 * 
 * @author Erik-Berndt Scheper
 * @param <T>
 */
@Embeddable
public final class VersionsJoinTableRangeComponent<T extends VersionsJoinTableRangeTestEntitySuperClass> {

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@org.hibernate.annotations.Fetch(value = FetchMode.SUBSELECT) 
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "VJTRCTE_ID", insertable = true, updatable = false, nullable = false)
//	Note:	If this is processed without override annotation, then we should get a 
//			org.hibernate.DuplicateMappingException: 
//			Duplicate class/entity mapping JOIN_TABLE_COMPONENT_1_AUD
	@org.hibernate.envers.AuditJoinTable(name = "JOIN_TABLE_COMPONENT_1_AUD", inverseJoinColumns = @JoinColumn(name = "VJTRTE_ID"))
	private List<T> range = new ArrayList<T>();

	// ********************** Accessor Methods ********************** //

	protected List<T> getRange() {
		return this.range;
	}

	// ********************** Common Methods ********************** //

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersionsJoinTableRangeComponent<?> other = (VersionsJoinTableRangeComponent<?>) obj;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();

		output.append("VersionsJoinTableRangeComponent {");
		output.append(" range = \"");
		for (T instance : range) {
			output.append(instance).append("\n");
		}
		output.append("\"}");

		return output.toString();
	}

}
