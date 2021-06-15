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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.temporal.object;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.util.Utilities;
import org.geotools.temporal.reference.DefaultTemporalReferenceSystem;
import org.geotools.util.SimpleInternationalString;
import org.opengis.temporal.IndeterminateValue;
import org.opengis.temporal.TemporalPosition;
import org.opengis.temporal.TemporalReferenceSystem;

/**
 * Used for describing temporal positions referenced to other temporal reference systems.
 * 
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 *
 * @source $URL$
 */
public class DefaultTemporalPosition implements TemporalPosition {

    /**
     * This is the TM_ReferenceSystem associated with this TM_TemporalPosition, 
     * if not specified, it is assumed to be an association to the Gregorian calendar and UTC.
     */
    private TemporalReferenceSystem frame;
    /**
     * This attribute provides the only value for TM_TemporalPosition unless a subtype of TM_TemporalPosition is used as the data type.
     */
    private IndeterminateValue indeterminatePosition;

    /**
     * Creates a new instance from a TemporalReferenceSystem and an IndeterminateValue
     * @param frame cannot be @CODE null
     * @param indeterminatePosition can be @CODE NULL if none.
     */
    public DefaultTemporalPosition(TemporalReferenceSystem frame, IndeterminateValue indeterminatePosition) {
        this.frame = frame;
        this.indeterminatePosition = indeterminatePosition;
    }

    /**
     * When this attribute is used with a subtype of {@code TemporalPosition}, it provides a qualifier to the specific value
     * for temporal position provided by the subtype.
     *
     */
    public IndeterminateValue getIndeterminatePosition() {
        return this.indeterminatePosition;
    }

    /**
     * Returns the TM_ReferenceSystem associated with this TM_TemporalPosition, 
     * if not specified, it is assumed to be an association to the Gregorian calendar and UTC. 
     * @return
     */
    public TemporalReferenceSystem getFrame() {
        if (frame == null) {
            frame = new DefaultTemporalReferenceSystem(new NamedIdentifier(
                    Citations.CRS, new SimpleInternationalString("Gregorian calendar")), null);
        }
        return frame;
    }

    public void setFrame(TemporalReferenceSystem frame) {
        this.frame = frame;
    }

    public void setIndeterminatePosition(IndeterminateValue indeterminatePosition) {
        this.indeterminatePosition = indeterminatePosition;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultTemporalPosition) {
            final DefaultTemporalPosition that = (DefaultTemporalPosition) object;

            return Utilities.equals(this.frame, that.frame) &&
                    Utilities.equals(this.indeterminatePosition, that.indeterminatePosition);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.frame != null ? this.frame.hashCode() : 0);
        hash = 37 * hash + (this.indeterminatePosition != null ? this.indeterminatePosition.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("TemporalPosition:").append('\n');
        if (frame != null) {
            s.append("frame:").append(frame).append('\n');
        }
        if (indeterminatePosition != null) {
            s.append("indeterminatePosition:").append(indeterminatePosition).append('\n');
        }
        return s.toString();
    }
}
