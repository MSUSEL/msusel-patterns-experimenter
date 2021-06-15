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
package org.hibernate.envers.test.entities.onetomany.ids;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.test.entities.ids.MulId;

/**
 * ReferencIng entity
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@IdClass(MulId.class)
public class SetRefIngMulIdEntity {
    @Id
    private Integer id1;

    @Id
    private Integer id2;

    @Audited
    private String data;

    @Audited
    @ManyToOne
    private SetRefEdMulIdEntity reference;

    public SetRefIngMulIdEntity() { }

    public SetRefIngMulIdEntity(MulId id, String data, SetRefEdMulIdEntity reference) {
        this.id1 = id.getId1();
        this.id2 = id.getId2();
        this.data = data;
        this.reference = reference;
    }

    public SetRefIngMulIdEntity(Integer id1, Integer id2, String data, SetRefEdMulIdEntity reference) {
        this.id1 = id1;
        this.id2 = id2;
        this.data = data;
        this.reference = reference;
    }

    public SetRefIngMulIdEntity(String data, SetRefEdMulIdEntity reference) {
        this.data = data;
        this.reference = reference;
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public Integer getId2() {
        return id2;
    }

    public void setId2(Integer id2) {
        this.id2 = id2;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public SetRefEdMulIdEntity getReference() {
        return reference;
    }

    public void setReference(SetRefEdMulIdEntity reference) {
        this.reference = reference;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SetRefIngMulIdEntity)) return false;

        SetRefIngMulIdEntity that = (SetRefIngMulIdEntity) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (id1 != null ? !id1.equals(that.id1) : that.id1 != null) return false;
        if (id2 != null ? !id2.equals(that.id2) : that.id2 != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id1 != null ? id1.hashCode() : 0);
        result = 31 * result + (id2 != null ? id2.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "SetRefIngMulIdEntity(id1 = " + id1 + ", id2 = " + id2 + ", data = " + data + ")";
    }
}