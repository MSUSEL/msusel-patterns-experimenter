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
package org.hibernate.envers.test.entities.manytomany.unidirectional;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.envers.test.entities.UnversionedStrTestEntity;

/**
 * @author Vladimir Klyushnikov
 */
@Entity
@Table(name = "M2M_IDX_LIST")
public class M2MIndexedListTargetNotAuditedEntity {

    @Id
    private Integer id;

    @Audited
    private String data;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OrderColumn(name = "sortOrder")
    @ManyToMany(fetch = FetchType.LAZY)
    private List<UnversionedStrTestEntity> references = new ArrayList<UnversionedStrTestEntity>();


    public M2MIndexedListTargetNotAuditedEntity() {
    }


    public M2MIndexedListTargetNotAuditedEntity(Integer id, String data) {
        this.id = id;
        this.data = data;
    }

    public M2MIndexedListTargetNotAuditedEntity(Integer id, String data, List<UnversionedStrTestEntity> references) {
        this.id = id;
        this.data = data;
        this.references = references;
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

    public List<UnversionedStrTestEntity> getReferences() {
        return references;
    }

    public void setReferences(List<UnversionedStrTestEntity> references) {
        this.references = references;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        M2MIndexedListTargetNotAuditedEntity that = (M2MIndexedListTargetNotAuditedEntity) o;

        //noinspection RedundantIfStatement
        if (data != null ? !data.equals(that.data) : that.data != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }


    @Override
    public String toString() {
		return "M2MIndexedListTargetNotAuditedEntity(id = " + id + ", data = " + data + ")";
    }
}
