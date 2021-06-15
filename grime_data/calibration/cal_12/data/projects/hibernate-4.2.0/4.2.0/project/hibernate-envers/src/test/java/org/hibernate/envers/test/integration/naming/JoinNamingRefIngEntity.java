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
package org.hibernate.envers.test.integration.naming;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

/**
 * ReferencIng entity
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
public class JoinNamingRefIngEntity {
    @Id
    @GeneratedValue
    @Column(name = "jnrie_id")
    private Integer id;

    @Audited
    private String data;

    @Audited
    @ManyToOne
    @JoinColumn(name = "jnree_column_reference")
    private JoinNamingRefEdEntity reference;

    public JoinNamingRefIngEntity() { }

    public JoinNamingRefIngEntity(Integer id, String data, JoinNamingRefEdEntity reference) {
        this.id = id;
        this.data = data;
        this.reference = reference;
    }

    public JoinNamingRefIngEntity(String data, JoinNamingRefEdEntity reference) {
        this.data = data;
        this.reference = reference;
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

    public JoinNamingRefEdEntity getReference() {
        return reference;
    }

    public void setReference(JoinNamingRefEdEntity reference) {
        this.reference = reference;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JoinNamingRefIngEntity)) return false;

        JoinNamingRefIngEntity that = (JoinNamingRefIngEntity) o;

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
        return "JoinNamingRefIngEntity(id = " + id + ", data = " + data + ")";
    }
}