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

import javax.persistence.Entity;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
@Audited
@AuditOverrides({@AuditOverride(forClass = BaseEntity.class, name = "str1", isAudited = true),
                 @AuditOverride(forClass = ExtendedBaseEntity.class, name = "number2", isAudited = true)})
public class TransitiveOverrideEntity extends ExtendedBaseEntity {
    private String str3;

    public TransitiveOverrideEntity() {
    }

    public TransitiveOverrideEntity(String str1, Integer number1, Integer id, String str2, Integer number2, String str3) {
        super(str1, number1, id, str2, number2);
        this.str3 = str3;
    }

    public TransitiveOverrideEntity(String str1, Integer number1, String str2, Integer number2, String str3) {
        super(str1, number1, str2, number2);
        this.str3 = str3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransitiveOverrideEntity)) return false;
        if (!super.equals(o)) return false;

        TransitiveOverrideEntity that = (TransitiveOverrideEntity) o;

        if (str3 != null ? !str3.equals(that.str3) : that.str3 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (str3 != null ? str3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TransitiveOverrideEntity(" + super.toString() + ", str3 = " + str3 + ")";
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }
}
