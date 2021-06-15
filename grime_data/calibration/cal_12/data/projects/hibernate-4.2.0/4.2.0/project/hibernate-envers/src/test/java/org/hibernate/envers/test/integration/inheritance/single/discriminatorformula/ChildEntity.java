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
package org.hibernate.envers.test.integration.inheritance.single.discriminatorformula;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
@DiscriminatorValue(ClassTypeEntity.CHILD_TYPE)
@Audited
public class ChildEntity extends ParentEntity {
    private String specificData;

    public ChildEntity() {
    }

    public ChildEntity(Long typeId, String data, String specificData) {
        super(typeId, data);
        this.specificData = specificData;
    }

    public ChildEntity(Long id, Long typeId, String data, String specificData) {
        super(id, typeId, data);
        this.specificData = specificData;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChildEntity)) return false;
        if (!super.equals(o)) return false;

        ChildEntity that = (ChildEntity) o;

        if (specificData != null ? !specificData.equals(that.specificData) : that.specificData != null) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (specificData != null ? specificData.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ChildEntity(id = " + id + ", typeId = " + typeId + ", data = " + data + ", specificData = " + specificData + ")";
    }

    public String getSpecificData() {
        return specificData;
    }

    public void setSpecificData(String specificData) {
        this.specificData = specificData;
    }
}
