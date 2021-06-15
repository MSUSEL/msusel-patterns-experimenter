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
package org.hibernate.envers.test.integration.naming.ids;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * ReferencEd entity
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Table(name = "JoinMulIdRefEd")
@IdClass(MulIdNaming.class)
public class JoinMulIdNamingRefEdEntity {
    @Id
    private Integer id1;

    @Id
    private Integer id2;

    @Audited
    private String data;

    @Audited
    @OneToMany(mappedBy="reference")
    private List<JoinMulIdNamingRefIngEntity> reffering;

    public JoinMulIdNamingRefEdEntity() {
    }

    public JoinMulIdNamingRefEdEntity(MulIdNaming id, String data) {
        this.id1 = id.getId1();
        this.id2 = id.getId2();
        this.data = data;
    }

    public JoinMulIdNamingRefEdEntity(String data) {
        this.data = data;
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

    public List<JoinMulIdNamingRefIngEntity> getReffering() {
        return reffering;
    }

    public void setReffering(List<JoinMulIdNamingRefIngEntity> reffering) {
        this.reffering = reffering;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JoinMulIdNamingRefEdEntity)) return false;

        JoinMulIdNamingRefEdEntity that = (JoinMulIdNamingRefEdEntity) o;

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
        return "JoinMulIdNamingRefEdEntity(id1 = " + id1 + ", id2 = " + id2 + ", data = " + data + ")";
    }
}