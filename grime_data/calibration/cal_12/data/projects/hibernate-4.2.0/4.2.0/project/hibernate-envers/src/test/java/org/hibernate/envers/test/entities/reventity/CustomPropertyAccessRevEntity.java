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
package org.hibernate.envers.test.entities.reventity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@GenericGenerator(name = "EnversTestingRevisionGenerator",
                  strategy = "org.hibernate.id.enhanced.TableGenerator",
                  parameters = {@Parameter(name = "table_name", value = "REVISION_GENERATOR"),
                                @Parameter(name = "initial_value", value = "1"),
                                @Parameter(name = "increment_size", value = "1"),
                                @Parameter(name = "prefer_entity_table_as_segment_value", value = "true")
                  }
)
@RevisionEntity
public class CustomPropertyAccessRevEntity {
    private int customId;

    private long customTimestamp;

    @Id
    @GeneratedValue(generator = "EnversTestingRevisionGenerator")
    @RevisionNumber
    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    @RevisionTimestamp
    public long getCustomTimestamp() {
        return customTimestamp;
    }

    public void setCustomTimestamp(long customTimestamp) {
        this.customTimestamp = customTimestamp;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomPropertyAccessRevEntity)) return false;

        CustomPropertyAccessRevEntity that = (CustomPropertyAccessRevEntity) o;

        if (customId != that.customId) return false;
        if (customTimestamp != that.customTimestamp) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = customId;
        result = 31 * result + (int) (customTimestamp ^ (customTimestamp >>> 32));
        return result;
    }
}