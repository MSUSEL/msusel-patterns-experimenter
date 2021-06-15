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
package org.hibernate.envers.test.integration.secondary.ids;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;

import org.hibernate.envers.Audited;
import org.hibernate.envers.SecondaryAuditTable;
import org.hibernate.envers.test.entities.ids.EmbId;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@SecondaryTable(name = "secondary")
@SecondaryAuditTable(secondaryTableName = "secondary", secondaryAuditTableName = "sec_embid_versions")
@Audited
public class SecondaryEmbIdTestEntity {
    @Id
    private EmbId id;

    private String s1;

    @Column(table = "secondary")
    private String s2;

    public SecondaryEmbIdTestEntity(EmbId id, String s1, String s2) {
        this.id = id;
        this.s1 = s1;
        this.s2 = s2;
    }

    public SecondaryEmbIdTestEntity(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public SecondaryEmbIdTestEntity() {
    }

    public EmbId getId() {
        return id;
    }

    public void setId(EmbId id) {
        this.id = id;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecondaryEmbIdTestEntity)) return false;

        SecondaryEmbIdTestEntity that = (SecondaryEmbIdTestEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (s1 != null ? !s1.equals(that.s1) : that.s1 != null) return false;
        if (s2 != null ? !s2.equals(that.s2) : that.s2 != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (s1 != null ? s1.hashCode() : 0);
        result = 31 * result + (s2 != null ? s2.hashCode() : 0);
        return result;
    }
}