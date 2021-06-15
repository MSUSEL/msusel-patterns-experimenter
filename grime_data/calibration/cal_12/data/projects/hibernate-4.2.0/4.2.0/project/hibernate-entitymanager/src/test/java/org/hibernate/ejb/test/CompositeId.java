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
package org.hibernate.ejb.test;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 */
@Embeddable
public class CompositeId implements Serializable {

    private int id1;
    private int id2;

    public int getId1() {
        return id1;
    }

    public void setId1( int id1 ) {
        this.id1 = id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId2( int id2 ) {
        this.id2 = id2;
    }

    @Override
    public boolean equals( Object obj ) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final CompositeId other = (CompositeId)obj;
        if (this.id1 != other.id1) return false;
        if (this.id2 != other.id2) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.id1;
        hash = 73 * hash + this.id2;
        return hash;
    }
}
