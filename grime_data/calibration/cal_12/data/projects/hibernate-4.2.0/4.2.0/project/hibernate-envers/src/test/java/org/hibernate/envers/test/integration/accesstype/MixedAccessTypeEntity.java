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
package org.hibernate.envers.test.integration.accesstype;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.AccessType;
import org.hibernate.envers.Audited;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
public class MixedAccessTypeEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @AccessType("property")
    private String data;

    @Transient
    private boolean dataSet;

    public MixedAccessTypeEntity() {
    }

    public MixedAccessTypeEntity(String data) {
        this.data = data;
    }

    public MixedAccessTypeEntity(Integer id, String data) {
        this.id = id;
        this.data = data;
    }

    public Integer getId() {
        throw new RuntimeException();
    }

    public void setId(Integer id) {
        throw new RuntimeException();
    }

    // TODO: this should be on the property. But how to discover in AnnotationsMetadataReader that the
    // we should read annotations from fields, even though the access type is "property"?
    @Audited
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataSet = true;
    }

    public boolean isDataSet() {
        return dataSet;
    }

    public Integer readId() {
        return id;
    }

    public void writeData(String data) {
        this.data = data;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MixedAccessTypeEntity)) return false;

        MixedAccessTypeEntity that = (MixedAccessTypeEntity) o;

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
}