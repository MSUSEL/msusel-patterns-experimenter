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
package org.hibernate.envers.test.integration.superclass.auditparents;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;
import org.hibernate.envers.test.entities.StrIntTestEntity;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
@Audited(auditParents = {MappedParentEntity.class, MappedGrandparentEntity.class})
public class ChildMultipleParentsEntity extends MappedParentEntity {
    private String child;

    public ChildMultipleParentsEntity() {
        super(null, null, null, null, null);
    }

    public ChildMultipleParentsEntity(Long id, String grandparent, String notAudited, String parent, String child, StrIntTestEntity relation) {
        super(id, grandparent, notAudited, parent, relation);
        this.child = child;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChildMultipleParentsEntity)) return false;
        if (!super.equals(o)) return false;

        ChildMultipleParentsEntity that = (ChildMultipleParentsEntity) o;

        if (child != null ? !child.equals(that.child) : that.child != null) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ChildMultipleParentsEntity(" + super.toString() + ", child = " + child + ")";
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }
}

