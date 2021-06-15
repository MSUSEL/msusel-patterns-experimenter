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

import java.util.Collection;

import org.geotools.util.Utilities;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.Position;

/**
 * A zero-dimensional geometric primitive that represents position in time, equivalent to a point
 * in space.
 * 
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 *
 * @source $URL$
 */
public class DefaultInstant extends DefaultTemporalGeometricPrimitive implements Instant {

    /**
     * This is the Collection of temporal {@link Period}s,
     * for which this Instant is the beginning. The collection may be empty.
     */
    private Collection<Period> begunBy;
    /**
     * This is the Collection of temporal {@link Period}s,
     * for which this Instant is the end. The collection may be empty.
     */
    private Collection<Period> endBy;
    /**
     * This is the position of this TM_Instant, it shall be associated with a single temporal reference system.
     */
    private Position position;

    public DefaultInstant(Position position) {
        this.position = position;
    }

    /**
     * Get the position of this instant.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the Collection of temporal {@link Period}s,
     * for which this Instant is the beginning. The collection may be empty.
     * @see Period#begin
     */
    public Collection<Period> getBegunBy() {
        return begunBy;
    }

    /**
     * Get the Collection of temporal {@link Period}s,
     * for which this Instant is the end. The collection may be empty.
     * @see Period#end
     */
    public Collection<Period> getEndedBy() {
        return endBy;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setBegunBy(Collection<Period> begunBy) {
        this.begunBy = begunBy;
    }

    public void setEndBy(Collection<Period> endBy) {
        this.endBy = endBy;
    }

    /**
     * Verify if this entry is identical to the specified object.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultInstant) {
            final DefaultInstant that = (DefaultInstant) object;

            return Utilities.equals(this.position, that.position) &&
                    Utilities.equals(this.begunBy, that.begunBy) &&
                    Utilities.equals(this.endBy, that.endBy);
        }
        return false;
    }

 //   /**
 //     * Verify if this entry is identical to the specified object.
 //    */
 //   public int compareTo(Object object) {
 //      if (object == this) {
 //           return 0;
 //       }
 //       if (object instanceof DefaultInstant) {
 //           final DefaultInstant that = (DefaultInstant) object;
 //
 //           if (Utilities.equals(this.position, that.position) &&
 //                   Utilities.equals(this.begunBy, that.begunBy) &&
 //                   Utilities.equals(this.endBy, that.endBy))
 //               return 0;
 //           else 
 //               return this.position.getDate().compareTo(that.getPosition().getDate());
 //       }
 //       
 //       throw new ClassCastException("Object of type " + object.getClass() + " cannot be compared to " + this.getClass());
 //   }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.position != null ? this.position.hashCode() : 0);
        hash = 37 * hash + (this.begunBy != null ? this.begunBy.hashCode() : 0);
        hash = 37 * hash + (this.endBy != null ? this.endBy.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Instant:").append('\n');
        if (position != null) {
            s.append("position:").append(position).append('\n');
        }
        if (begunBy != null) {
            s.append("begunBy:").append(begunBy).append('\n');
        }
        if (endBy != null) {
            s.append("endBy:").append(endBy).append('\n');
        }
        return s.toString();
    }
}
