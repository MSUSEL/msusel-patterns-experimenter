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
package org.hibernate.envers.configuration.metadata;
import org.hibernate.MappingException;
import org.hibernate.mapping.JoinedSubclass;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.SingleTableSubclass;
import org.hibernate.mapping.Subclass;
import org.hibernate.mapping.UnionSubclass;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public enum InheritanceType {
    NONE,
    JOINED,
    SINGLE,
    TABLE_PER_CLASS;

    /**
     * @param pc The class for which to get the inheritance type.
     * @return The inheritance type of this class. NONE, if this class does not inherit from
     * another persisten class.
     */
    public static InheritanceType get(PersistentClass pc) {
        PersistentClass superclass = pc.getSuperclass();
        if (superclass == null) {
            return InheritanceType.NONE;
        }

        // We assume that every subclass is of the same type.
        Subclass subclass = (Subclass) superclass.getSubclassIterator().next();

        if (subclass instanceof SingleTableSubclass) {
            return InheritanceType.SINGLE;
        } else if (subclass instanceof JoinedSubclass) {
            return InheritanceType.JOINED;
        } else if (subclass instanceof UnionSubclass) {
            return InheritanceType.TABLE_PER_CLASS;
        }

        throw new MappingException("Unknown subclass class: " + subclass.getClass());
    }
}
