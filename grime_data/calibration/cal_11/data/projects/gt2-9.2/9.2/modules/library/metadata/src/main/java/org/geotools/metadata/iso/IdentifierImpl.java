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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;


/**
 * Value uniquely identifying an object within a namespace.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class IdentifierImpl extends MetadataEntity implements Identifier {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 7459062382170865919L;

    /**
     * Alphanumeric value identifying an instance in the namespace.
     */
    private String code;

    /**
     * Identifier of the version of the associated code space or code, as specified
     * by the code space or code authority. This version is included only when the
     * {@linkplain #getCode code} uses versions.
     * When appropriate, the edition is identified by the effective date, coded using
     * ISO 8601 date format.
     */
    private String version;

    /**
     * Organization or party responsible for definition and maintenance of the
     * {@linkplain #getCode code}.
     */
    private Citation authority;

    /**
     * Construct an initially empty identifier.
     */
    public IdentifierImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public IdentifierImpl(final Identifier source) {
        super(source);
    }

    /**
     * Creates an identifier initialized to the given code.
     */
    public IdentifierImpl(final String code) {
        setCode(code);
    }

    /**
     * Creates an identifier initialized to the given authority and code.
     *
     * @since 2.2
     */
    public IdentifierImpl(final Citation authority, final String code) {
        setAuthority(authority);
        setCode(code);
    }

    /**
     * Alphanumeric value identifying an instance in the namespace.
     *
     * @return The code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the alphanumeric value identifying an instance in the namespace.
     */
    public synchronized void setCode(final String newValue) {
        checkWritePermission();
        code = newValue;
    }

    /**
     * Identifier of the version of the associated code, as specified
     * by the code space or code authority. This version is included only when the
     * {@linkplain #getCode code} uses versions.
     * When appropriate, the edition is identified by the effective date, coded using
     * ISO 8601 date format.
     *
     * @return The version, or {@code null} if not available.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set an identifier of the version of the associated code.
     */
    public synchronized void setVersion(final String newValue) {
        checkWritePermission();
        version = newValue;
    }

    /**
     * Organization or party responsible for definition and maintenance of the
     * {@linkplain #getCode code}.
     *
     * @return The authority, or {@code null} if not available.
     */
    public Citation getAuthority() {
        return authority;
    }

    /**
     * Set the organization or party responsible for definition and maintenance of the
     * {@linkplain #getCode code}.
     */
    public synchronized void setAuthority(final Citation newValue) {
        checkWritePermission();
        authority = newValue;
    }
}
