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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.metadata.sql;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;


/**
 * A metadata entity which implements (indirectly) metadata
 * interfaces like {@link org.opengis.metadata.MetaData},
 * {@link org.opengis.metadata.citation.Citation}, etc.
 *
 * Any call to a method in a metadata interface is redirected toward the
 * {@link #invoke} method. This method use reflection in order to find
 * the caller's method and class name. The class name is translated into
 * a table name, and the method name is translated into a column name.
 * Then the information is fetch in the underlying metadata database.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Touraïvane
 * @author Martin Desruisseaux (IRD)
 */
final class MetadataEntity implements InvocationHandler {
    /**
     * The identifier used in order to locate the record for
     * this metadata entity in the database. This is usually
     * the primary key in the table which contains this entity.
     */
    private final String identifier;

    /**
     * The connection to the database. All metadata entities
     * created from a single database should share the same source.
     */
    private final MetadataSource source;

    /**
     * Creates a new metadata entity.
     *
     * @param identifier The identifier used in order to locate the record for
     *                   this metadata entity in the database. This is usually
     *                   the primary key in the table which contains this entity.
     * @param source     The connection to the table which contains this entity.
     */
    public MetadataEntity(final String identifier, final MetadataSource source) {
        this.identifier = identifier;
        this.source     = source;
    }

    /**
     * Invoked when any method from a metadata interface is invoked.
     *
     * @param proxy  The object on which the method is invoked.
     * @param method The method invoked.
     * @param args   The argument given to the method.
     */
    public Object invoke(final Object proxy,
                         final Method method,
                         final Object[] args)
    {
        final Class<?> type = method.getDeclaringClass();
        if (type.getName().startsWith(source.metadataPackage)) {
            if (args!=null && args.length!=0) {
                throw new MetadataException("Unexpected argument."); // TODO: localize
            }
            /*
             * The method invoked is a method from the metadata interface.
             * Consequently, the information should exists in the underlying
             * database.
             */
            try {
                return source.getValue(type, method, identifier);
            } catch (SQLException e) {
                throw new MetadataException("Failed to query the database.", e); // TODO: localize
            }
        } else {
            /*
             * The method invoked is a method inherit from a parent class,
             * like Object.toString() or Object.hashCode(). This information
             * is not expected to exists in the database. Forward the call
             * to this object object, since they is only one instance by proxy.
             * Note: do not forward to the proxy in order to avoid never-ending
             * loop.
             */
            try {
                return method.invoke(this, args);
            } catch (IllegalAccessException e) {
                throw new MetadataException("Illegal method call.", e); // TODO: localize
            } catch (InvocationTargetException e) {
                throw new MetadataException("Illegal method call.", e); // TODO: localize
            }
        }
    }
}
