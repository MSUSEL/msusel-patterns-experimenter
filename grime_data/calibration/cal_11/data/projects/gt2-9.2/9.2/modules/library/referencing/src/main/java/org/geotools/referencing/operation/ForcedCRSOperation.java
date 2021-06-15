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
package org.geotools.referencing.operation;

import java.util.Collection;
import java.util.Set;

import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.quality.PositionalAccuracy;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

/**
 * Used by {@link AuthorityBackedFactory} when concanating operations, in case we're concatenating
 * identities, but so that the source or target CRS are not the same as the non identity part. This
 * happens, for example, when trying to work against a projected CRS with a wrapped geographic CRS
 * axis in lon/lat order, and with the database providing an operation that uses the same projected
 * CRS around a geographic CRS with axis in lat/lon order
 * 
 * @author Andrea Aime - GeoSolutions
 */
class ForcedCRSOperation implements CoordinateOperation {

    CoordinateOperation delegate;

    CoordinateReferenceSystem sourceCRS;

    CoordinateReferenceSystem targetCRS;

    public ForcedCRSOperation(CoordinateOperation delegate, CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem targetCRS) {
        this.delegate = delegate;
        this.sourceCRS = sourceCRS;
        this.targetCRS = targetCRS;
    }

    public CoordinateReferenceSystem getSourceCRS() {
        return sourceCRS;
    }

    public CoordinateReferenceSystem getTargetCRS() {
        return targetCRS;
    }

    public ReferenceIdentifier getName() {
        return delegate.getName();
    }

    public Collection<GenericName> getAlias() {
        return delegate.getAlias();
    }

    public Set<ReferenceIdentifier> getIdentifiers() {
        return delegate.getIdentifiers();
    }

    public InternationalString getRemarks() {
        return delegate.getRemarks();
    }

    public String toWKT() throws UnsupportedOperationException {
        return delegate.toWKT();
    }

    public String getOperationVersion() {
        return delegate.getOperationVersion();
    }

    public Collection<PositionalAccuracy> getCoordinateOperationAccuracy() {
        return delegate.getCoordinateOperationAccuracy();
    }

    public Extent getDomainOfValidity() {
        return delegate.getDomainOfValidity();
    }

    public InternationalString getScope() {
        return delegate.getScope();
    }

    public MathTransform getMathTransform() {
        return delegate.getMathTransform();
    }

}
