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

import javax.persistence.MappedSuperclass;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@MappedSuperclass
@AuditOverrides({@AuditOverride(forClass = BaseEntity.class, name = "str1", isAudited = false),
                 @AuditOverride(forClass = BaseEntity.class, name = "number1", isAudited = true)})
public class ExtendedBaseEntity extends BaseEntity {
    @Audited
    private String str2;

    @NotAudited
    private Integer number2;

    public ExtendedBaseEntity() {
    }

    public ExtendedBaseEntity(String str1, Integer number1, Integer id, String str2, Integer number2) {
        super(str1, number1, id);
        this.str2 = str2;
        this.number2 = number2;
    }

    public ExtendedBaseEntity(String str1, Integer number1, String str2, Integer number2) {
        super(str1, number1);
        this.str2 = str2;
        this.number2 = number2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtendedBaseEntity)) return false;
        if (!super.equals(o)) return false;

        ExtendedBaseEntity that = (ExtendedBaseEntity) o;

        if (number2 != null ? !number2.equals(that.number2) : that.number2 != null) return false;
        if (str2 != null ? !str2.equals(that.str2) : that.str2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (str2 != null ? str2.hashCode() : 0);
        result = 31 * result + (number2 != null ? number2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExtendedBaseEntity(" + super.toString() + ", str2 = " + str2 + ", number2 = " + number2 + ")";
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public Integer getNumber2() {
        return number2;
    }

    public void setNumber2(Integer number2) {
        this.number2 = number2;
    }
}
