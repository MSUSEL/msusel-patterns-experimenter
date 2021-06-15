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
package org.hibernate.envers.test.integration.inheritance.joined.primarykeyjoin;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.envers.Audited;
import org.hibernate.envers.test.integration.inheritance.joined.ParentEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Audited
@PrimaryKeyJoinColumn(name = "other_id")
public class ChildPrimaryKeyJoinEntity extends ParentEntity {
    @Basic
    private Long namVal;

    public ChildPrimaryKeyJoinEntity() {
    }

    public ChildPrimaryKeyJoinEntity(Integer id, String data, Long namVal) {
        super(id, data);
        this.namVal = namVal;
    }

    public Long getNumVal() {
        return namVal;
    }

    public void setNumVal(Long namVal) {
        this.namVal = namVal;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChildPrimaryKeyJoinEntity)) return false;
        if (!super.equals(o)) return false;

        ChildPrimaryKeyJoinEntity childPrimaryKeyJoinEntity = (ChildPrimaryKeyJoinEntity) o;

        //noinspection RedundantIfStatement
        if (namVal != null ? !namVal.equals(childPrimaryKeyJoinEntity.namVal) : childPrimaryKeyJoinEntity.namVal != null) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (namVal != null ? namVal.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "CPKJE(id = " + getId() + ", data = " + getData() + ", namVal = " + namVal + ")";
    }
}