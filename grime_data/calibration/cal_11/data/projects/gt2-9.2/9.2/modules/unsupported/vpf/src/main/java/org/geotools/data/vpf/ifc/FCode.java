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
package org.geotools.data.vpf.ifc;

import java.util.Arrays;
import java.util.List;

/**
 * Defines constants related to FACC Code processing.  
 *
 * @author John Meagher
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public interface FCode {

    /**
     * Array of attributes the FACC Code information is allowed to be stored under.
     * The default allowed attribute names are "f_code" and "facc".  This can be
     * be overridden using the system property "gt.vpf.allowedFCodeAttributes"
     * with a comma separated list of the desired values.  
     **/
    public static final String[] ALLOWED_FCODE_ATTRIBUTES =
        System.getProperty( "gt.vpf.allowedFCodeAttributes", "f_code,facc" ).split( "," );

    /**
     * The ALLOWED_FCODE_ATTRIBUTES array in list form.
     * @see #ALLOWED_FCODE_ATTRIBUTES
     **/
    public static final List ALLOWED_FCODE_ATTRIBUTES_LIST = Arrays.asList( ALLOWED_FCODE_ATTRIBUTES );
}
