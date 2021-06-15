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
package org.hibernate.test.annotations.cascade;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

@Embeddable
class PersonPair implements Serializable {

	private static final long serialVersionUID = 4543565503074112720L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "LEFT_PERSON_ID", nullable = false, updatable = false)
	@ForeignKey(name = "FK_LEFT_PERSON")
	private Person left;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "RIGHT_PERSON_ID", nullable = false, updatable = false)
	@ForeignKey(name = "FK_RIGHT_PERSON")
	private Person right;

	PersonPair() {
		super();
	}

	PersonPair(final Person pLeft, final Person pRight) {
		super();
		this.left = pLeft;
		this.right = pRight;
	}

	Person getLeft() {
		return this.left;
	}

	Person getRight() {
		return this.right;
	}

	@Override
	public int hashCode() {
		final int prime = 107;
		int result = 1;
		result = prime * result + ((getLeft() == null) ? 0 : getLeft().hashCode());
		result = prime * result + ((getRight() == null) ? 0 : getRight().hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object pObject) {
		if (this == pObject) {
			return true;
		}
		if (pObject == null) {
			return false;
		}
		if (!(pObject instanceof PersonPair)) {
			return false;
		}
		final PersonPair other = (PersonPair) pObject;
		if (getRight() == null) {
			if (other.getRight() != null) {
				return false;
			}
		} else if (!getRight().equals(other.getRight())) {
			return false;
		}
		if (getLeft() == null) {
			if (other.getLeft() != null) {
				return false;
			}
		} else if (!getLeft().equals(other.getLeft())) {
			return false;
		}
		return true;
	}

}
