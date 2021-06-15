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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Entity for {@link org.hibernate.envers.test.integration.onetomany.detached.DoubleJoinColumnBidirectionalList} test.
 * Owned side of the first relation.
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Table(name = "DoubleJoinColBiRefEd1")
@Audited
public class DoubleListJoinColumnBidirectionalRefEdEntity1 {
    @Id
    @GeneratedValue
    private Integer id;

    private String data;

    @ManyToOne
    @JoinColumn(name = "some_join_column_1", insertable = false, updatable = false)
    private DoubleListJoinColumnBidirectionalRefIngEntity owner;

    public DoubleListJoinColumnBidirectionalRefEdEntity1() { }

    public DoubleListJoinColumnBidirectionalRefEdEntity1(Integer id, String data, DoubleListJoinColumnBidirectionalRefIngEntity owner) {
        this.id = id;
        this.data = data;
        this.owner = owner;
    }

    public DoubleListJoinColumnBidirectionalRefEdEntity1(String data, DoubleListJoinColumnBidirectionalRefIngEntity owner) {
        this.data = data;
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DoubleListJoinColumnBidirectionalRefIngEntity getOwner() {
        return owner;
    }

    public void setOwner(DoubleListJoinColumnBidirectionalRefIngEntity owner) {
        this.owner = owner;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleListJoinColumnBidirectionalRefEdEntity1)) return false;

        DoubleListJoinColumnBidirectionalRefEdEntity1 that = (DoubleListJoinColumnBidirectionalRefEdEntity1) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        //noinspection RedundantIfStatement
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "DoubleListJoinColumnBidirectionalRefIngEntity1(id = " + id + ", data = " + data + ")";
    }
}