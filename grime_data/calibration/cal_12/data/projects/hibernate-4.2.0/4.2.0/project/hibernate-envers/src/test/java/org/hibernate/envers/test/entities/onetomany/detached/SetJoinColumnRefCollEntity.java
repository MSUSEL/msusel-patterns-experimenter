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
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.test.entities.StrTestEntity;

/**
 * A detached relation to another entity, with a @OneToMany+@JoinColumn mapping.
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Table(name = "SetJoinColRefColl")
public class SetJoinColumnRefCollEntity {
    @Id
    private Integer id;

    @Audited
    private String data;

    @Audited
    @OneToMany
    @JoinColumn(name = "SJCR_ID")
    @AuditJoinTable(name = "SetJoinColRefColl_StrTest_AUD")
    private Set<StrTestEntity> collection;

    public SetJoinColumnRefCollEntity() {
    }

    public SetJoinColumnRefCollEntity(Integer id, String data) {
        this.id = id;
        this.data = data;
    }

    public SetJoinColumnRefCollEntity(String data) {
        this.data = data;
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

    public Set<StrTestEntity> getCollection() {
        return collection;
    }

    public void setCollection(Set<StrTestEntity> collection) {
        this.collection = collection;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SetJoinColumnRefCollEntity)) return false;

        SetJoinColumnRefCollEntity that = (SetJoinColumnRefCollEntity) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
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
        return "SetJoinColumnRefCollEntity(id = " + id + ", data = " + data + ")";
    }
}