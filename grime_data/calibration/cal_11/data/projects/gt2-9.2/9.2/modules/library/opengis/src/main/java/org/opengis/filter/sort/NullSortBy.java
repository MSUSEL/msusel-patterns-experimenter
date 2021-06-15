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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.sort;

import java.io.ObjectStreamException;
import java.io.Serializable;
import org.opengis.filter.expression.PropertyName;


/**
 * Default implementation of {@link SortBy} as a "null object". Used for {@link SortBy} constants.
 *
 * @author Jody Garnett (Refractions Research)
 * @author Martin Desruisseaux (Geomatys)
 *
 * @source $URL$
 */
final class NullSortBy implements SortBy, Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -4846119001746135007L;

    /**
     * The sort order.
     */
    private final SortOrder order;

    /**
     * Creates a new Null object for the specified sort order.
     */
    NullSortBy(final SortOrder order) {
        this.order = order;
    }

    /**
     * Natural order usually associated with FID, or Key Attribtues.
     */
    public PropertyName getPropertyName() {
        return null;
    }

    /**
     * Returns the sort order.
     */
    public SortOrder getSortOrder() {
        return order;
    }

    /**
     * Returns a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return (int) serialVersionUID ^ order.hashCode();
    }

    /**
     * Compares this object with the specified one for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof NullSortBy) {
            return order.equals(((NullSortBy) object).order);
        }
        return false;
    }

    /**
     * Returns a single instance after deserialization.
     */
    private Object readResolve() throws ObjectStreamException {
        if (order.equals(SortOrder.ASCENDING))  return SortBy.NATURAL_ORDER;
        if (order.equals(SortOrder.DESCENDING)) return SortBy.REVERSE_ORDER;
        return this;
    }
}
