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
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature.type;

import java.util.Set;
import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;


/**
 * A set of 0 or more names, with no duplicates.
 * <p>
 * A namespace contains {@link Name} objects. Each name usually corresponds to
 * the name of a type. The namespace uri of each name ({@link Namespace#getURI()}
 * is the same as the uri of the Namespace object containing it ({@link #getURI()}.
 * </p>
 * <pre>
 *  //create namespace for gml
 *  Namespace namespace = new NamespaceImpl( "http://www.opengis.net/gml" );
 *
 *  //add some names
 *  namespace.add( new NameImpl( "http://www.opengis.net/gml", "PointType" ) );
 *  namespace.add( new NameImpl( "http://www.opengis.net/gml", "LineStringType" ) );
 *  namespace.add( new NameImpl( "http://www.opengis.net/gml", "PolygonType" ) );
 *  namespace.add( new NameImpl( "http://www.opengis.net/gml", "AbstractFeatureType" );
 * </pre>
 * </p>
 * <p>
 * <h3>ISO 19103</h3>
 * The ISO 19103 specification asks that we have:
 * <ul>
 * <li>isGlobal()
 * <li>name() - inidicating the name of this namespace
 * <li>getNames() - set of names
 * </ul>
 * We have combined these concerns by making this a Set of Names,
 * and we remember the URI of this namespace.
 * <p>
 * One allowance ISO_19103 allows for is having a Namespace located inside another
 * namespace. You may certaintly do this by constructing a facility similar to Schema
 * in which namespaces may be looked up via a Name with the same URI as the one
 * used here.
 * <p>
 * We are simply not dictating the lookup mechanism, or a backpointer to
 * a containing namespace (note the two solutions are in conflict and we would like
 * to offer application the freedom to back this interface onto a facility such as
 * JNDI used in their own application).
 * </p>
 * @author Jody Garnett, Refractions Research, Inc.
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @since GeoAPI 2.1
 *
 *
 * @source $URL$
 */
@UML(identifier="NameSpace", specification=ISO_19103)
public interface Namespace extends Set<Name> {

    /**
     * The namespace uri of this namespace.
     * <p>
     * This value can never be <code>null</code>.
     * </p>
     */
    String getURI();

    /**
     * Looks up a name in the namespace.
     * <p>
     * Since all Name objects in the namespace share the same uri as the
     * namespace itself, only the local part of the name is specified.
     * </p>
     * <p>
     * This method returns <code>null</code> if no such name exists.
     * </p>
     * @param name The local part of the name to look up.
     *
     * @return The name, or <code>null</code>.
     */
    Name lookup( String name );
}
