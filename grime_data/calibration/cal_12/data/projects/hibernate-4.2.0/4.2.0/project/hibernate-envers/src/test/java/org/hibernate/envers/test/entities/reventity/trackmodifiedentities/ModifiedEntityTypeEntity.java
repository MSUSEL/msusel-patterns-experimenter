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
package org.hibernate.envers.test.entities.reventity.trackmodifiedentities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Custom detail of revision entity.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
public class ModifiedEntityTypeEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private CustomTrackingRevisionEntity revision;
    
    private String entityClassName;

    public ModifiedEntityTypeEntity() {
    }

    public ModifiedEntityTypeEntity(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public ModifiedEntityTypeEntity(CustomTrackingRevisionEntity revision, String entityClassName) {
        this.revision = revision;
        this.entityClassName = entityClassName;
    }

    public CustomTrackingRevisionEntity getRevision() {
        return revision;
    }

    public void setRevision(CustomTrackingRevisionEntity revision) {
        this.revision = revision;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModifiedEntityTypeEntity)) return false;

        ModifiedEntityTypeEntity that = (ModifiedEntityTypeEntity) o;

        if (entityClassName != null ? !entityClassName.equals(that.entityClassName) : that.entityClassName != null) return false;

        return true;
    }

    public int hashCode() {
        return entityClassName != null ? entityClassName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CustomTrackingRevisionEntity(entityClassName = " + entityClassName + ")";
    }
}
