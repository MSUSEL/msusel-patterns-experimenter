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
package org.hibernate.envers.test.entities.onetoone;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * Test class for issue HHH-3854. Restricting creation of proxy objects is essential.
 */
@Entity
@Audited
@Proxy(lazy=false)
// Class name is too long of an identifier for Oracle.
@Table(name = "EdOneToOne")
public final class BidirectionalEagerAnnotationRefEdOneToOne {
	/**
	 * ID column.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * Field containing the referring entity.
	 */
	@OneToOne(mappedBy = "refedOne", fetch = FetchType.EAGER)
	@NotAudited
	private BidirectionalEagerAnnotationRefIngOneToOne refIng;

	/**
	 * Field containing some data.
	 */
	private String data;

	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( ! ( o instanceof BidirectionalEagerAnnotationRefEdOneToOne ) ) return false;

		BidirectionalEagerAnnotationRefEdOneToOne that = (BidirectionalEagerAnnotationRefEdOneToOne) o;

		if ( data != null ? !data.equals( that.data ) : that.data != null ) return false;
		if ( id != null ? !id.equals( that.id ) : that.id != null ) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + ( data != null ? data.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		return "BidirectionalEagerAnnotationRefEdOneToOne(id = " + id + ", data = " + data + ")";
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the refIng
	 */
	public BidirectionalEagerAnnotationRefIngOneToOne getRefIng() {
		return refIng;
	}

	/**
	 * @param refIng the refIng to set
	 */
	public void setRefIng(BidirectionalEagerAnnotationRefIngOneToOne refIng) {
		this.refIng = refIng;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
}