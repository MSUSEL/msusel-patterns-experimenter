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
package org.hibernate.envers.test.integration.data;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Audited
public class EnumTestEntity {
    @Id
    @GeneratedValue
    private Integer id;

    public enum E1 { X, Y }
    public enum E2 { A, B }

    @Enumerated(EnumType.STRING)
    private E1 enum1;

    @Enumerated(EnumType.ORDINAL)
    private E2 enum2;

    public EnumTestEntity() {
    }

    public EnumTestEntity(E1 enum1, E2 enum2) {
        this.enum1 = enum1;
        this.enum2 = enum2;
    }

    public EnumTestEntity(Integer id, E1 enum1, E2 enum2) {
        this.id = id;
        this.enum1 = enum1;
        this.enum2 = enum2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public E1 getEnum1() {
        return enum1;
    }

    public void setEnum1(E1 enum1) {
        this.enum1 = enum1;
    }

    public E2 getEnum2() {
        return enum2;
    }

    public void setEnum2(E2 enum2) {
        this.enum2 = enum2;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnumTestEntity)) return false;

        EnumTestEntity that = (EnumTestEntity) o;

        if (enum1 != that.enum1) return false;
        if (enum2 != that.enum2) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (enum1 != null ? enum1.hashCode() : 0);
        result = 31 * result + (enum2 != null ? enum2.hashCode() : 0);
        return result;
    }
}
