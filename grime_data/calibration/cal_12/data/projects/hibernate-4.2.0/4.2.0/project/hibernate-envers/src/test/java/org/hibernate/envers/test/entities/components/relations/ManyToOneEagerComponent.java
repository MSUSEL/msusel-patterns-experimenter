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
package org.hibernate.envers.test.entities.components.relations;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.test.entities.StrTestNoProxyEntity;

/**
 * Do not mark as {@link Audited}. Should be implicitly treated as audited when part of audited entity.
 *
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Embeddable
@Table(name = "ManyToOneEagerComp")
public class ManyToOneEagerComponent {
	@ManyToOne(fetch = FetchType.EAGER)
	private StrTestNoProxyEntity entity;

	private String data;

	public ManyToOneEagerComponent(StrTestNoProxyEntity entity, String data) {
		this.entity = entity;
		this.data = data;
	}

	public ManyToOneEagerComponent() {
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public StrTestNoProxyEntity getEntity() {
		return entity;
	}

	public void setEntity(StrTestNoProxyEntity entity) {
		this.entity = entity;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( !( o instanceof ManyToOneEagerComponent ) ) return false;

		ManyToOneEagerComponent that = (ManyToOneEagerComponent) o;

		if ( data != null ? !data.equals( that.data ) : that.data != null ) return false;
		if ( entity != null ? !entity.equals( that.entity ) : that.entity != null ) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = entity != null ? entity.hashCode() : 0;
		result = 31 * result + ( data != null ? data.hashCode() : 0 );
		return result;
	}

	public String toString() {
		return "ManyToOneEagerComponent(data = " + data + ")";
	}
}
