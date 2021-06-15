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
package org.hibernate.envers.test.integration.superclass.auditoverride;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@MappedSuperclass
public class NotAuditedBaseEntity implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    private String str1;

    private Integer number1;

    public NotAuditedBaseEntity() {
    }

    public NotAuditedBaseEntity(String str1, Integer number1, Integer id) {
        this.id = id;
        this.str1 = str1;
        this.number1 = number1;
    }

    public NotAuditedBaseEntity(String str1, Integer number1) {
        this.str1 = str1;
        this.number1 = number1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public Integer getNumber1() {
        return number1;
    }

    public void setNumber1(Integer number1) {
        this.number1 = number1;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotAuditedBaseEntity)) return false;

        NotAuditedBaseEntity that = (NotAuditedBaseEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (number1 != null ? !number1.equals(that.number1) : that.number1 != null) return false;
        if (str1 != null ? !str1.equals(that.str1) : that.str1 != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (str1 != null ? str1.hashCode() : 0);
        result = 31 * result + (number1 != null ? number1.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "NotAuditedBaseEntity(id = " + id + ", str1 = " + str1 + ", number1 = " + number1 + ")";
    }
}