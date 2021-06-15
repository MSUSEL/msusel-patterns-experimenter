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
package org.hibernate.envers.test.entities;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
public class PrimitiveTestEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Audited
    private int numVal1;

	private int numVal2;

    public PrimitiveTestEntity() {
    }

	public PrimitiveTestEntity(int numVal1, int numVal2) {
		this.numVal1 = numVal1;
		this.numVal2 = numVal2;
	}

	public PrimitiveTestEntity(Integer id, int numVal1, int number2) {
		this.id = id;
		this.numVal1 = numVal1;
		this.numVal2 = numVal2;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumVal1() {
        return numVal1;
    }

    public void setNumVal1(Integer numVal1) {
        this.numVal1 = numVal1;
    }

	public int getNumVal2() {
		return numVal2;
	}

	public void setNumVal2(int numVal2) {
		this.numVal2 = numVal2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PrimitiveTestEntity)) return false;

		PrimitiveTestEntity that = (PrimitiveTestEntity) o;

		if (numVal1 != that.numVal1) return false;
		if (numVal2 != that.numVal2) return false;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + numVal1;
		result = 31 * result + numVal2;
		return result;
	}

	public String toString() {
        return "PTE(id = " + id + ", numVal1 = " + numVal1 + ", numVal2 = " + numVal2 + ")";
    }
}