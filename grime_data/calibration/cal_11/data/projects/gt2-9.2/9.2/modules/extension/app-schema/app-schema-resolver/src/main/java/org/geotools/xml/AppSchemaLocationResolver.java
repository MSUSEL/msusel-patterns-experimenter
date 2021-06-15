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
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml;

import org.eclipse.xsd.XSDSchema;

/**
 * A {@link SchemaLocationResolver} that uses {@link AppSchemaResolver} to locate schema resources
 * in a catalog, on the classpath, or in a cache..
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 *
 *
 *
 * @source $URL$
 */
public class AppSchemaLocationResolver extends SchemaLocationResolver {

    /**
     * The resolver used to locate schemas
     */
    private final AppSchemaResolver resolver;

    /**
     * Constructor.
     * 
     * @param resolver
     *            the resolver used to locate schemas
     */
    public AppSchemaLocationResolver(AppSchemaResolver resolver) {
        super(null);
        this.resolver = resolver;
    }

    /**
     * Resolve imports and includes to local resources.
     * 
     * @param schema
     *            the parent schema from which the import/include originates
     * @param uri
     *            the namespace of an import (ignored in this implementation)
     * @param location
     *            the URL of the import or include (may be relative)
     * 
     * @see org.geotools.xml.SchemaLocationResolver#resolveSchemaLocation(org.eclipse.xsd.XSDSchema,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public String resolveSchemaLocation(final XSDSchema schema, final String uri,
            final String location) {
        return resolver.resolve(location, schema.getSchemaLocation());
    }

    /**
     * We override this because the parent {@link #toString()} is horribly misleading.
     * 
     * @see org.geotools.xml.SchemaLocationResolver#toString()
     */
    @Override
    public String toString() {
        return getClass().getCanonicalName();
    }

}
