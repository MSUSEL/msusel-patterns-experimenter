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
package org.hibernate.envers.test.entities.onetomany.detached.inheritance;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Entity for {@link org.hibernate.envers.test.integration.onetomany.detached.InheritanceIndexedJoinColumnBidirectionalList} test.
 * Child, owning side of the relation.
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Table(name = "ChildIdxJoinColBiRefIng")
@Audited
public class ChildIndexedListJoinColumnBidirectionalRefIngEntity extends ParentIndexedListJoinColumnBidirectionalRefIngEntity {
    private String data2;

    public ChildIndexedListJoinColumnBidirectionalRefIngEntity() {
    }

    public ChildIndexedListJoinColumnBidirectionalRefIngEntity(Integer id, String data, String data2, ParentOwnedIndexedListJoinColumnBidirectionalRefEdEntity... references) {
        super(id, data, references);
        this.data2 = data2;
    }

    public ChildIndexedListJoinColumnBidirectionalRefIngEntity(String data, String data2, ParentOwnedIndexedListJoinColumnBidirectionalRefEdEntity... references) {
        super(data, references);
        this.data2 = data2;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChildIndexedListJoinColumnBidirectionalRefIngEntity)) return false;
        if (!super.equals(o)) return false;

        ChildIndexedListJoinColumnBidirectionalRefIngEntity that = (ChildIndexedListJoinColumnBidirectionalRefIngEntity) o;

        if (data2 != null ? !data2.equals(that.data2) : that.data2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (data2 != null ? data2.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ChildIndexedListJoinColumnBidirectionalRefIngEntity(id = " + getId() + ", data = " + getData() + ", data2 = " + data2 + ")";
    }
}
