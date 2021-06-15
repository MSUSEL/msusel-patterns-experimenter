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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.util;

import org.opengis.util.MemberName;
import org.opengis.util.TypeName;


/**
 * The name to identify a member of a {@linkplain org.opengis.util.Record record}. This is
 * typically used as of a <code>Map<{@linkplain MemberName},{@linkplain TypeName}></code>.
 * <p>
 * It may be more simple to think of {@code MemberName} <em>as</em> a
 * {@link java.util.Map.Entry} - since it is both the "key" and the "value".
 * <ul>
 *   <li>key: {@code this}</li>
 *   <li>value: associated {@link TypeName}</li>
 * </ul>
 * This presents a bit of a conflict in that we are never quite sure
 * what comes first the record or the member during creation time.
 *
 * @since 2.4
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 */
public class MemberNameImpl extends LocalName implements MemberName {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 6188284973982058318L;

    /**
     * The type of the data associated with the record member.
     */
    private final TypeName typeName;

    /**
     * Constructs a member name from the specified string with no scope.
     *
     * @param name The local name (never {@code null}).
     * @param type The type associated with this name.
     */
    public MemberNameImpl(final CharSequence name, final TypeName typeName) {
        super(name);
        this.typeName = typeName;
    }

    /**
     * Returns the type of the data associated with the record member.
     *
     * @todo Check in the specification if this association is really navigable that way.
     *       This association seems redundant with {@link RecordType#locate}.
     */
    public TypeName getAttributeType() {
        return typeName;
    }

    /**
     * Compares this member name with the associated object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (super.equals(object)) {
            final MemberNameImpl that = (MemberNameImpl) object;
            return Utilities.equals(this.typeName, that.typeName);
        }
        return false;
    }
}
