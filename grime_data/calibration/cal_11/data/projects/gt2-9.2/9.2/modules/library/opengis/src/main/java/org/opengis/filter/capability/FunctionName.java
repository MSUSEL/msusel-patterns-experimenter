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
import java.util.List;

import org.opengis.annotation.UML;
import org.opengis.feature.type.Name;
import org.opengis.parameter.Parameter;

import static org.opengis.annotation.Specification.*;

/**
 * Function description provided in a filter capabilities.
 * <p>
 * <pre>
 * &lt;xsd:complexType name="FunctionNameType">
 *     &lt;xsd:simpleContent>
 *        &lt;xsd:extension base="xsd:string">
 *           &lt;xsd:attribute name="nArgs" type="xsd:string" use="required"/>
 *        &lt;/xsd:extension>
 *     &lt;/xsd:simpleContent>
 *  &lt;/xsd:complexType>
 * </pre>
 * </p>
 * <p>
 * We have extended this idea to include a list of argument names to better serve interactive
 * clients.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </A>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public interface FunctionName extends Operator {

    /**
     * The qualified name of the function.
     * <p>
     * Client code should this method over {@link Operator#getName()} to handle qualified names. 
     * </p>
     */
    Name getFunctionName();

    /**
     * Number of arguments the function accepts.
     * <p>
     * <ul>
     * <li>Use a postivie number to indicate the number of arguments.
     *     Example: add( number1, number2 ) = 2</li>
     * <li>Use a negative number to indicate a minimum number:
     *    Example: concat( str1, str2,... ) has -2</li>
     * </ul> 
     *
     * <pre>
     * &lt;xsd:attribute name="nArgs" type="xsd:string" use="required"/>
     * </pre>
     * </p>
     * <p>
     * This value is derived from {@link #getArguments()}
     * </p>
     */
    @UML(identifier="argumentCount", specification=UNSPECIFIED)
    int getArgumentCount();
    
    /**
     * Argument names for documentation purposes if known.
     * <p>
     * This value is derived from {@link #getArguments()}
     * </p>
     * @return Argument names (for documentation purposes) if known
     */
    List<String> getArgumentNames();
    
    /**
     * Arguments for the function accepts.
     * 
     * @version 8.0
     */
    List<Parameter<?>> getArguments();

    /**
     * Return type of the function.
     * 
     * @version 8.0
     */
    Parameter<?> getReturn();
}
