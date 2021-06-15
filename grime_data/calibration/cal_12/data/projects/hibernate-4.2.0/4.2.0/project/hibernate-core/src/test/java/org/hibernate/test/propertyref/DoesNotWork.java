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
package org.hibernate.test.propertyref;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.IndexColumn;

/**
 * @author Steve Ebersole
 */
@Entity
@Table(name = "vgras007_v031")
public class DoesNotWork implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DoesNotWorkPk doesNotWorkPk;

	@Column(name = "production_credits_tid", insertable = false, updatable = false)
	private Long globAdditInfoTid;

	@ElementCollection
	@CollectionTable(
			name = "vgras029_v031",
			joinColumns = @JoinColumn(name = "text_id", referencedColumnName = "production_credits_tid")
	)
	@Column(name = "text_part", insertable = false, updatable = false)
	@IndexColumn(name = "seq_no", base = 1)
	private List<String> globalNotes = new ArrayList<String>();

	public DoesNotWork() {
	}

	public DoesNotWork(DoesNotWorkPk doesNotWorkPk) {
		this.doesNotWorkPk = doesNotWorkPk;
	}

	public DoesNotWorkPk getDoesNotWorkPk() {
		return doesNotWorkPk;
	}

	public void setDoesNotWorkPk(DoesNotWorkPk doesNotWorkPk) {
		this.doesNotWorkPk = doesNotWorkPk;
	}

	public List<String> getGlobalNotes() {
		return globalNotes;
	}

	public void setGlobalNotes(List<String> globalNotes) {
		this.globalNotes = globalNotes;
	}

	public Long getGlobAdditInfoTid() {
		return globAdditInfoTid;
	}

	public void setGlobAdditInfoTid(Long globAdditInfoTid) {
		this.globAdditInfoTid = globAdditInfoTid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((doesNotWorkPk == null) ? 0 : doesNotWorkPk.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( !(obj instanceof DoesNotWork) ) {
			return false;
		}
		DoesNotWork other = (DoesNotWork) obj;
		if ( doesNotWorkPk == null ) {
			if ( other.doesNotWorkPk != null ) {
				return false;
			}
		}
		else if ( !doesNotWorkPk.equals( other.doesNotWorkPk ) ) {
			return false;
		}
		return true;
	}

}
