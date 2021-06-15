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
 */
package org.opengis.filter.identity;

import org.opengis.annotation.XmlElement;


/**
 * Feature identifier.
 * <p>
 * Features are identified as strings.
 *
 * @source $URL$
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @author Justin Deoliveira (The Open Planning Project)
 * @since GeoAPI 2.0
 */
@XmlElement("FeatureId")
public interface FeatureId extends Identifier {
    
    public static final char VERSION_SEPARATOR = '@';

    /**
     * The identifier value, which is a string.
     */
    @XmlElement("fid")
    String getID();

    //
    // Query and Test methods used to test a feature or record
    // 
    /**
     * Evaluates the identifer value against the given feature.
     *
     * @param feature The feature to be tested.
     * @return {@code true} if a match, otherwise {@code false}.
     */
    boolean matches(Object feature);
    
    /** Check if the provided FeatureId is an exact match (including any optional version
     * information).
     * 
     * @param id
     * @return true if this is an exact match (including any optional version information)
     */
    boolean equalsExact(FeatureId id);
    
    /**
     * Checks if the provided FeatureId reflects the same feature.
     * <p>
     * This comparison does not compare any optional version information.
     * 
     * @param id
     * @return true if both identifiers describe the same feature (does not compare version information).
     */
    boolean equalsFID(FeatureId id);
    
    //
    // Filter 2.0 Versioning Support
    //
    // The following methods are optional and are used as part of the FeatureId data
    // structure to report any available version information associated with a resoruce.
    //
    /**
     * id of the resource that shall be selected by the predicate.
     * <p>
     * Equals to {@link #getID()} if no feature version is provided, or
     * {@code getID() + "@" + getFeatureVersion()} if {@code getFeatureVersion() != null}
     * 
     * <p>
     * If an implementation that references this International Standard supports versioning, the rid
     * shall be a system generated hash containing a logical resource identifier and a version
     * number. The specific details of the hash are implementation dependant and shall be opaque to
     * a client
     * </p>
     * <p>
     * If versioning is not supported, the same value than {@link FeatureId#getID()} shall be
     * returned.
     * </p>
     * @return Resource identifier made up of FID (combined with FeatureVersion if available)
     */
    @XmlElement("rid")
    String getRid();

    /**
     * previousRid attribute may be used, in implementations that support versioning, to report the
     * previous identifier of a resource.
     * 
     * @return Previous rid if available; or {@code null}
     */
    @XmlElement("previousRid")
    String getPreviousRid();

    /**
     * Version identifier for the feature instance, may be {@code null}
     * 
     * @see #getID()
     * @see #getRid()
     * @return Optional version information; {@code null} if not available
     */
    String getFeatureVersion();

}
