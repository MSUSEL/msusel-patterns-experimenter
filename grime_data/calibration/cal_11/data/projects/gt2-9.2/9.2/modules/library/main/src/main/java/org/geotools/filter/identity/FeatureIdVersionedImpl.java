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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.identity;

import org.opengis.filter.identity.FeatureId;

/**
 * Implementation of {@link org.opengis.filter.identity.FeatureId}
 * <p>
 * This class is mutable under one condition only; during a commit
 * a datastore can update the internal fid to reflect the real identify
 * assigned by the database or wfs.
 * <p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 * @since 2.5
 * @version 8.0
 */
public class FeatureIdVersionedImpl extends FeatureIdImpl {
    
    protected String featureVersion;
    protected String previousRid;
    
    public FeatureIdVersionedImpl(String fid, String version ) {
        this( fid, version, null);
    }
    public FeatureIdVersionedImpl(String fid, String version, String previousRid) {
        super( fid );
        this.featureVersion = version;
        this.previousRid = previousRid;
    }

    
    public String toString() {
        return fid;
    }

    public boolean equals(Object obj) {
        if (obj instanceof FeatureId) {
            return fid.equals(((FeatureId) obj).getID());
        }
        return false;
    }

    public int hashCode() {
        return fid.hashCode();
    }

    @Override
    public boolean equalsExact(FeatureId id) {
        if (id instanceof FeatureId) {
            return fid.equals( id.getID() ) &&
                    fid.equals( id.getRid() ) &&
                    id.getPreviousRid() == null &&
                    id.getFeatureVersion() == null;
        }
        return false;
    }

    @Override
    public boolean equalsFID(FeatureId id) {
        if( id == null ) return false;
        
        return getID().equals(id.getID());
    }

    @Override
    public String getRid() {
        return featureVersion == null ? getID() : new StringBuilder(getID())
                .append(VERSION_SEPARATOR).append(featureVersion).toString();
    }

    @Override
    public String getPreviousRid() {
        return previousRid;
    }
    
    @Override
    public String getFeatureVersion() {
        return featureVersion;
    }


}