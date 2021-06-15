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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.metadata;

import it.geosolutions.imageio.utilities.Utilities;

import java.io.Serializable;

/**
 * @author Martin Desruisseaux
 * @author Daniele Romagnoli, GeoSolutions
 *
 *
 *
 * @source $URL$
 */
public class Identification implements CharSequence, Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 7439545624472885445L;

    /**
     * The object name, or {@code null} if none.
     */
    private final String name;

    /**
     * The object remarks, or {@code null} if none.
     */
    private final String remarks;

    /**
     * The object alias, or {@code null} if none.
     */
    private final String alias;

    /**
     * The object identifier, or {@code null} if none.
     */
    private final String identifier;

    /**
     * Creates an identification from the specified object name.
     */
    public Identification(final String name, final String remarks,
            final String alias, final String identifier) {
        this.name = name;
        this.remarks = remarks;
        this.alias = alias;
        this.identifier = identifier;
    }

    /**
     * Creates an identification from the specified object name.
     */
    public Identification(final String name) {
        this.name = name;
        this.remarks = null;
        this.alias = null;
        this.identifier = null;
    }

    /**
     * Creates an identification from the specified accessor.
     */
    public Identification(final MetadataAccessor accessor) {
        name = accessor.getString(SpatioTemporalMetadataFormat.MD_COMM_NAME);
        remarks = accessor.getString(SpatioTemporalMetadataFormat.MD_COMM_REMARKS);
        alias = accessor.getString(SpatioTemporalMetadataFormat.MD_COMM_ALIAS);
        identifier = accessor.getString(SpatioTemporalMetadataFormat.MD_COMM_IDENTIFIER);
    }

    /**
     * Returns the {@linkplain #name} length.
     */
    public int length() {
        return (name != null) ? name.length() : 0;
    }

    /**
     * Returns the {@linkplain #name} character at the specified index.
     */
    public char charAt(final int index) {
        return name.charAt(index);
    }

    /**
     * Returns a subsequence of this identification. The new identification will
     * contains a substring of the {@linkplain #name}, but the
     * {@linkplain #type} will be unchanged.
     */
    public CharSequence subSequence(final int start, final int end) {
        if (start == 0 && end == length()) {
            return this;
        }
        return new Identification(name.substring(start, end), remarks, alias, identifier);
    }

    /**
     * Returns a hash value for this identification.
     */
    public int hashCode() {
        int code = (int) serialVersionUID;
        if (name != null)
            code ^= name.hashCode();
        return code;
    }

    /**
     * Compares the specified object with this identification for equality.
     */
    public boolean equals(final Object object) {
        if (object != null && object.getClass().equals(getClass())) {
            final Identification that = (Identification) object;
            return Utilities.equals(this.name, that.name)
                    && Utilities.equals(this.remarks, that.remarks)
                    && Utilities.equals(this.alias, that.alias)
                    && Utilities.equals(this.identifier, that.identifier);
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getAlias() {
        return alias;
    }

    public String getIdentifier() {
        return identifier;
    }
    
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name);
        if (alias != null)
            sb.append("\n Alias: ").append(alias);
        if (identifier != null)
            sb.append("\n Identifier: ").append(identifier);
        if (remarks != null)
            sb.append("\n Remarks: ").append(remarks);
        return sb.toString();
    }
}
