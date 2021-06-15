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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v1_0;

import java.util.Set;

import org.geotools.filter.v1_0.OGC;

/**
 * XSD for wfs 1.0; except for WFS 1.0 capabilities document, for which {@link WFSCapabilities}
 * shall be used, as its based on a different schema.
 * 
 * @author Justin Deoliveira, OpenGEO
 * 
 * 
 * 
 * @source $URL$
 */
public final class WFS extends org.geotools.wfs.WFS {

    /** singleton instance */
    private static final WFS instance = new WFS();

    /**
     * Returns the singleton instance.
     */
    public static final WFS getInstance() {
        return instance;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void addDependencies(Set dependencies) {
        dependencies.add( OGC.getInstance() );
    }
    
    /**
     * Returns the location of 'WFS-transaction.xsd.'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("WFS-transaction.xsd").toString();
    }
    
    /**
     * Returns '1.0.0'.
     */
    @Override
    public String getVersion() {
        return "1.0.0";
    }

}
