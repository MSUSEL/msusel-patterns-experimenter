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
package org.hibernate.envers.test.entities.customtype;

import java.io.Serializable;

import org.hibernate.envers.Audited;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Audited
public class UnspecifiedEnumTypeEntity implements Serializable {
	public static enum E1 { X, Y }
	public static enum E2 { A, B }

	private Long id;

	private E1 enum1;

	private E2 enum2;

	public UnspecifiedEnumTypeEntity() {
	}

	public UnspecifiedEnumTypeEntity(E1 enum1, E2 enum2) {
		this.enum1 = enum1;
		this.enum2 = enum2;
	}

	public UnspecifiedEnumTypeEntity(E1 enum1, E2 enum2, Long id) {
		this.enum1 = enum1;
		this.enum2 = enum2;
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( ! ( o instanceof UnspecifiedEnumTypeEntity ) ) return false;

		UnspecifiedEnumTypeEntity that = (UnspecifiedEnumTypeEntity) o;

		if ( enum1 != that.enum1 ) return false;
		if ( enum2 != that.enum2 ) return false;
		if ( id != null ? !id.equals( that.id ) : that.id != null ) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + ( enum1 != null ? enum1.hashCode() : 0 );
		result = 31 * result + ( enum2 != null ? enum2.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		return "UnspecifiedEnumTypeEntity(id = " + id + ", enum1 = " + enum1 + ", enum2 = " + enum2 + ")";
	}

	public E1 getEnum1() {
		return enum1;
	}

	public void setEnum1(E1 enum1) {
		this.enum1 = enum1;
	}

	public E2 getEnum2() {
		return enum2;
	}

	public void setEnum2(E2 enum2) {
		this.enum2 = enum2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
