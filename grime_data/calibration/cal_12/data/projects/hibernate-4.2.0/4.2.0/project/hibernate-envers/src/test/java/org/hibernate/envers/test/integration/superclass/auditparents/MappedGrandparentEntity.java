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

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.NotAudited;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@MappedSuperclass
public class MappedGrandparentEntity {
    @Id
    private Long id;

    private String grandparent;

    @NotAudited
    private String notAudited;

    public MappedGrandparentEntity(Long id, String grandparent, String notAudited) {
        this.id = id;
        this.grandparent = grandparent;
        this.notAudited = notAudited;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MappedGrandparentEntity)) return false;

        MappedGrandparentEntity that = (MappedGrandparentEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (grandparent != null ? !grandparent.equals(that.grandparent) : that.grandparent != null) return false;
        if (notAudited != null ? !notAudited.equals(that.notAudited) : that.notAudited != null) return false;

        return true;
    }

    public int hashCode() {
        int result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (grandparent != null ? grandparent.hashCode() : 0);
        result = 31 * result + (notAudited != null ? notAudited.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "MappedGrandparentEntity(id = " + id + ", grandparent = " + grandparent + ", notAudited = " + notAudited + ")";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrandparent() {
        return grandparent;
    }

    public void setGrandparent(String grandparent) {
        this.grandparent = grandparent;
    }

    public String getNotAudited() {
        return notAudited;
    }

    public void setNotAudited(String notAudited) {
        this.notAudited = notAudited;
    }
}
