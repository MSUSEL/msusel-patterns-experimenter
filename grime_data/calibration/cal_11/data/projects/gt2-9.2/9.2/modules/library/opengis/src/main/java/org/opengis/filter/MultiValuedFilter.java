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
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter;

import org.opengis.annotation.XmlElement;

/**
 * Defines a filter that supports filtering on multi-valued attributes.
 * 
 * @author Niels Charlier, Curtin University of Technology
 *
 *
 *
 * @source $URL$
 */
public interface MultiValuedFilter extends Filter {
        
    /**
     * Enumerated type for MatchAction property (used by comparison and geometry operations):
     * When one or more of the operands evaluates to multiple values rather than a single value, which
     * action should be taken?
     * 
     * For example, in case of a binary comparison, if there are n values for the left operand 
     * and m values for the right operand, there are n * m possible combinations that can be compared,
     * 
     * ANY - if any of the possible combinations match, the result is true (aggregated OR)
     * ALL - only if all of the possible combinations match, the result is true (aggregated AND)
     * ONE - only if exactly one of the possible combinations match, the result is true (aggregated XOR)
     * 
     * @author Niels Charlier, Curtin University of Technology
     *
     */
    public enum MatchAction {ANY, ALL, ONE};
    

    /***
     * Flag Controlling MatchAction property
     * When one or more of the operands evaluates to multiple values rather than a single value, which
     * action should be taken? 
     * If there are n values for the left operand and m values for the right operand, there are 
     * n * m possible combinations that can be compared,
     * 
     * ANY - if any of the possible combinations match, the result is true (aggregated OR)
     * ALL - only if all of the possible combinations match, the result is true (aggregated AND)
     * ONE - only if exactly one of the possible combinations match, the result is true (aggregated XOR)
     * 
     * @return MatchAction flag
     * 
     */
    @XmlElement("matchAction")
    MatchAction getMatchAction();

}
