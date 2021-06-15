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
package org.hibernate.test.hql;

import java.io.Serializable;

public class CompositeIdEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long key1;
	private String key2;
	private String someProperty;

	public Long getKey1() {
		return key1;
	}

	public void setKey1( Long key1 ) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2( String key2 ) {
		this.key2 = key2;
	}

	public String getSomeProperty() {
		return someProperty;
	}

	public void setSomeProperty( String someProperty ) {
		this.someProperty = someProperty;
	}

	@Override
	public int hashCode() {
		// not really needed, thus the dumb implementation.
		return 42;
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj) {
			return true;
		}
		if ( !( obj instanceof CompositeIdEntity ) ) {
			return false; 
		}
		CompositeIdEntity other = ( CompositeIdEntity ) obj;
		if ( key1 == null ? other.key1 != null : !key1.equals( other.key1 ) ) {
			return false;
		}
		if ( key2 == null ? other.key2 != null : !key2.equals( other.key2 ) ) {
			return false;
		}
		return true;
	}
}
