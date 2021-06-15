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
import javax.persistence.Id;

import org.hibernate.envers.Audited;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
public class IntNoAutoIdTestEntity {
    @Id
    private Integer id;

    @Audited
    private Integer numVal;

    public IntNoAutoIdTestEntity() {
    }

    public IntNoAutoIdTestEntity(Integer numVal, Integer id) {
        this.id = id;
        this.numVal = numVal;
    }

    public IntNoAutoIdTestEntity(Integer numVal) {
        this.numVal = numVal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumVal() {
        return numVal;
    }

    public void setNumVal(Integer numVal) {
        this.numVal = numVal;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntNoAutoIdTestEntity)) return false;

        IntNoAutoIdTestEntity that = (IntNoAutoIdTestEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        //noinspection RedundantIfStatement
        if (numVal != null ? !numVal.equals(that.numVal) : that.numVal != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (numVal != null ? numVal.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "INATE(id = " + id + ", numVal = " + numVal + ")";
    }
}