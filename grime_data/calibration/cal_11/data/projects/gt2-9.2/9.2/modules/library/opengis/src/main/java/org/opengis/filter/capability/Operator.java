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
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    (C) 2001-2004 EXSE, Department of Geography, University of Bonn
 *                  lat/lon Fitzke/Fretter/Poth GbR
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *   
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.opengis.filter.capability;

// Annotations
import static org.opengis.annotation.Specification.UNSPECIFIED;

import org.opengis.annotation.UML;
import org.opengis.feature.type.Name;

/**
 * Indicates a supported Operator.
 * <p>
 * The operator that is supported is indicated by the getName() field, these
 * names are formally defined to match:
 * <ul>
 * <li>A subclass of Filter. Examples include "BBOX" and "EqualsTo"
 * <li>A subclass of Expression or Function. Examples include "ADD" and "Length"
 * </ul>
 * Each filter subclass has an associated name (such as BBOX or EqualsTo), you
 * can use this name to determine if a matching Operator is defined as part of
 * FilterCapabilities.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe</A>
 * @author Jody Garnett (Refractions Research)
 * @todo Which relationship with Filter and expressions?
 *
 *
 * @source $URL$
 */
public interface Operator {	
    /**
     * Name of supported Operator.
     * <p>
     * Each filter subclass has an associated name (such as BBOX or EqualsTo), you
     * can use this name to determine if a matching Operator is defined as part of
     * FilterCapabilities. 
     */
    @UML(identifier="name", specification=UNSPECIFIED)
    String getName();

    /**
     * The supported interface enabled by this Operator.
     * <p>
     * The mapping from getName() to supported interface is formally defined; and
     * is must agree with the interfaces defined in org.opengis.filter. Because this
     * binding is formal we should replace Operator here with a CodeList and capture
     * it as part of the GeoAPI project.
     * </p>
     * @return Interface marked as supported by this Operator
     */
    //Class getSupportedType();
    
    /**
     * Equals should be implemented simply in terms of getName()
     */
    ///@Override
    boolean equals(Object obj);
    /**
     * HashCode should be implemented simply in terms of getName().
     */
    ///@Override
    int hashCode();
}
