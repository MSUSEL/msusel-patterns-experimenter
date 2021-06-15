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
package org.hibernate.envers.test.integration.ids.protectedmodifier;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class ProtectedConstructorEntity implements Serializable {
	@EmbeddedId
	private WrappedStringId wrappedStringId;

	private String str1;

	@SuppressWarnings("unused")
	protected ProtectedConstructorEntity() {
		// For JPA. Protected access modifier is essential in terms of unit test.
	}

	public ProtectedConstructorEntity(WrappedStringId wrappedStringId, String str1) {
		this.wrappedStringId = wrappedStringId;
		this.str1 = str1;
	}

	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( !( o instanceof ProtectedConstructorEntity ) ) return false;

		ProtectedConstructorEntity that = (ProtectedConstructorEntity) o;

		if ( wrappedStringId != null ? !wrappedStringId.equals( that.wrappedStringId ) : that.wrappedStringId != null ) return false;
		if ( str1 != null ? !str1.equals( that.str1 ) : that.str1 != null ) return false;

		return true;
	}

	public int hashCode() {
		int result = ( wrappedStringId != null ? wrappedStringId.hashCode() : 0 );
		result = 31 * result + ( str1 != null ? str1.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		return "ProtectedConstructorEntity(wrappedStringId = " + wrappedStringId + ", str1 = " + str1 + ")";
	}

	public WrappedStringId getWrappedStringId() {
		return wrappedStringId;
	}

	public void setWrappedStringId(WrappedStringId wrappedStringId) {
		this.wrappedStringId = wrappedStringId;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}
}
