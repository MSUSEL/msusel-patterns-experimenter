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
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */package org.opengis.filter.expression;

import java.util.List;

import org.opengis.annotation.XmlElement;
import org.opengis.filter.capability.FunctionName;


/**
 * Instances of this class represent a function call into some implementation-specific
 * function.
 * <p>
 * Each execution environment should provide a list of supported functions
 * (and the number of arguments they expect) as part of a FilterCapabilities
 * data structure.
 * <p>
 * This is included for completeness with respect to the
 * OGC Filter specification.  However, no functions are required to be supported
 * by that specification.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("Function")
public interface Function extends Expression {
    /**
     * Returns the name of the function to be called.  For example, this might
     * be "{@code cos}" or "{@code atan2}".
     * <p>
     * You can use this name to look up the number of required parameters
     * in a FilterCapabilities data structure. For the specific meaning of
     * the required parameters you will need to consult the documentation.
     */
    String getName();
    
    /**
     * Access to the FunctionName description as used in a FilterCapabilities document.
     * 
     * @return FunctionName description, if available.
     */
    FunctionName getFunctionName();

   /**
     * Returns the list subexpressions that will be evaluated to provide the
     * parameters to the function.
     */
    List<Expression> getParameters();

    /**
     * The value of the fallbackValue attribute is used as a default value, if the SE
     * implementation does not support the function. If the implementation supports the
     * function, then the result value is determined by executing the function.
     *
     * @return Optional literal to use if an implementation for this function is not available.
     *
     * @since GeoAPI 2.2
     */
    @XmlElement("fallbackValue")
    Literal getFallbackValue();
}
