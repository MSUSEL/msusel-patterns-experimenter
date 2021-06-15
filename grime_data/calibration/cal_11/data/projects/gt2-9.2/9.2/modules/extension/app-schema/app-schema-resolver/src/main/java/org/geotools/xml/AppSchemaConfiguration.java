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

/**
 * XML encoder {@link Configuration} that uses {@link AppSchemaResolver} to obtain schemas.
 * 
 * <p>
 * 
 * Because we do not know the dependent GML {@link Configuration} when an instance is constructed,
 * it must be added later using {@link #addDependency(Configuration)}. Failure to do this will
 * result in bindings not being found at encode time.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 *
 *
 *
 * @source $URL$
 */
public class AppSchemaConfiguration extends Configuration {

    /**
     * Original (unresolved) schema location.
     */
    private final String originalSchemaLocation;

    /**
     * Because we do not know the dependent GML {@link Configuration} until runtime, it must be
     * specified as a constructor argument.
     * 
     * @param namespace
     *            the namespace URI
     * @param schemaLocation
     *            URL giving canonical schema location
     * @param resolver
     */
    public AppSchemaConfiguration(String namespace, String schemaLocation,
            AppSchemaResolver resolver) {
        super(new AppSchemaXSD(namespace, schemaLocation, resolver));
        originalSchemaLocation = schemaLocation;
        ((AppSchemaXSD) getXSD()).setConfiguration(this);
    }

    /**
     * Get the original (unresolved) schema location.
     * 
     * @return the schema location
     */
    public String getSchemaLocation() {
        return originalSchemaLocation;
    }

    /**
     * Allow late addition of a dependency such as GML.
     * 
     * @see org.geotools.xml.Configuration#addDependency(org.geotools.xml.Configuration)
     */
    @Override
    public void addDependency(Configuration dependency) {
        super.addDependency(dependency);
    }

}
