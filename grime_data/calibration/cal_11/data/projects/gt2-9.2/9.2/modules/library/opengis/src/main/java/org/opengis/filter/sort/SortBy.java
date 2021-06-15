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
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.sort;

import org.opengis.filter.expression.PropertyName;


/**
 * Defines the sort order, based on a property and ascending/descending.
 * <p>
 * Having SortBy at the Filter level is an interesting undertaking of Filter 1.1
 * support. Why you ask? It is at the Same level as Filter, it is not *used* by
 * Filter itself. The services that make use of Filter, such as WFS are starting
 * to make use of SortBy in the same breath.
 * </p>
 * <p>
 * Where is SortBy used:
 * <ul>
 * <li>WFS 1.1 Query
 * <li>CSW 2.0.1 AbstractQuery
 * </ul>
 * There may be more ...
 * </p>
 * <p>
 * What this means is that the GeoTools Query will make use of this
 * construct. As for sorting the result of an expression (where an
 * expression matches more then one element), we will splice it in to
 * AttributeExpression as an optional parameter. Note function is defined
 * to return a single value (so we don't need to worry there).
 * </p>
 * @see <a href="http://schemas.opengis.net/filter/1.1.0/sort.xsd">
 * @see <a href="http://schemas.opengis.net/wfs/1.1.0/wfs.xsd">
 *
 * @since GeoAPI 2.1
 * @author Jody Garnett (Refractions Research)
 *
 *
 * @source $URL$
 */
public interface SortBy {
    /**
     * Used to indicate lack of a sorting order.
     * <p>
     * This is the default value for used when setting up a Query.
     * </p>
     */
    SortBy[] UNSORTED = new SortBy[0];

    /**
     * Used to indicate "natural" sorting order, usually according
     * FID (hopefully based on Key attribtues).
     * <p>
     * This is the order that is most likely to be available in optimzied
     * form, if an Attribute is marked as "key" an optimized ordering should
     * be considered avaialble.
     * </p>
     * <p>
     * Non optimized orderings are will at the very least require as pass
     * through the data to bring it into memory, you can assume somekind
     * of TreeSet would be used. Where the nodes in the tree would indicate
     * a list of FeatureIds assoicated with the node, in the order encountered.
     * </p>
     * <p>
     * This is a "NullObject".
     * </p>
     */
    SortBy NATURAL_ORDER = new NullSortBy(SortOrder.ASCENDING);

    /**
     * Indicate the reverse order, usually assoicated with "Fid".
     * <p>
     * This is a "NullObject".
     * </p>
     */
    SortBy REVERSE_ORDER = new NullSortBy(SortOrder.DESCENDING);

    /**
     * Indicate property to sort by, specification is limited to PropertyName.
     * <p>
     * Not sure if this is allowed to be a xPath expression?
     * <ul>
     *   <li>It would be consist with our use in GeoTools</li>
     *   <li>It would not seem to agree with the short hand notation
     *       used by WFS1.1 (ie. "year A, month A, day A" )</li>
     * </ul>
     * </p>
     * @todo Use QName
     * @return Name of property to sort by.
     */
    PropertyName getPropertyName();

    /**
     * The the sort order - one of {@link SortOrder#ASCENDING ASCENDING}
     * or {@link SortOrder#DESCENDING DESCENDING}.
     */
    SortOrder getSortOrder();
}
