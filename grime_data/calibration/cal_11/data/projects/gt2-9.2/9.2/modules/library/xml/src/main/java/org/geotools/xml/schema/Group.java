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
package org.geotools.xml.schema;

import java.net.URI;


/**
 * <p>
 * This interface is intended to represent the Group construct within XML
 * Schemas.
 * </p>
 * 
 * <p>
 * In many situations it is recommended that groups be flatened out to their
 * child declaration, removing the additional layer of indirection. Although
 * this optimization is nice, it is imposible to complete this all the time,
 * as xml schemas may include publicly viewable Group definitions.
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 *
 * @source $URL$
 */
public interface Group extends ElementGrouping {
    /**
     * <p>
     * Returns the Child Schema element (Choice or Sequence) declaring valid
     * element sequences for this group.
     * </p>
     *
     */
    public ElementGrouping getChild();

    /**
     * <p>
     * The Group's declaration object id.
     * </p>
     *
     */
    public String getId();

    /**
     * <p>
     * The maximum number of times this group may appear in the instance
     * document.
     * </p>
     *
     */
    public int getMaxOccurs();

    /**
     * <p>
     * The minimum number of times this group may appear in the instance
     * document.
     * </p>
     *
     */
    public int getMinOccurs();

    /**
     * <p>
     * The group's name in the Schema document
     * </p>
     *
     */
    public String getName();

    /**
     * DOCUMENT ME!
     *
     */
    public URI getNamespace();
}
