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
package org.hibernate.envers.test.entities.onetomany.detached;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Entity for {@link org.hibernate.envers.test.integration.onetomany.detached.JoinColumnBidirectionalListWithInheritance} test.
 * Owned child side of the relation.
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Table(name = "ListJoinColBiInhRefEdChild")
@DiscriminatorValue("2")
@Audited
public class ListJoinColumnBidirectionalInheritanceRefEdChildEntity extends ListJoinColumnBidirectionalInheritanceRefEdParentEntity {
    private String childData;

    public ListJoinColumnBidirectionalInheritanceRefEdChildEntity() { }

    public ListJoinColumnBidirectionalInheritanceRefEdChildEntity(Integer id, String parentData, ListJoinColumnBidirectionalInheritanceRefIngEntity owner, String childData) {
        super(id, parentData, owner);
        this.childData = childData;
    }

    public ListJoinColumnBidirectionalInheritanceRefEdChildEntity(String parentData, ListJoinColumnBidirectionalInheritanceRefIngEntity owner, String childData) {
        super(parentData, owner);
        this.childData = childData;
    }

    public String getChildData() {
        return childData;
    }

    public void setChildData(String childData) {
        this.childData = childData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ListJoinColumnBidirectionalInheritanceRefEdChildEntity that = (ListJoinColumnBidirectionalInheritanceRefEdChildEntity) o;

        //noinspection RedundantIfStatement
        if (childData != null ? !childData.equals(that.childData) : that.childData != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (childData != null ? childData.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ListJoinColumnBidirectionalInheritanceRefEdChildEntity(id = " + getId() + 
                ", parentData = " + getParentData() + ", childData = " + childData + ")";
    }
}