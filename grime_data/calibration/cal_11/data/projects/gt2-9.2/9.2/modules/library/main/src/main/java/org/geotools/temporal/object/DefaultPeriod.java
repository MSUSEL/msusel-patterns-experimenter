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

import java.util.Date;
import org.geotools.util.Utilities;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.RelativePosition;

/**
 * A one-dimensional geometric primitive that represent extent in time.
 * 
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 *
 * @source $URL$
 */
public class DefaultPeriod extends DefaultTemporalGeometricPrimitive implements Period {

    /**
     * This is the TM_Instant at which this Period starts.
     */
    private Instant begining;
    /**
     * This is the TM_Instant at which this Period ends.
     */
    private Instant ending;

    public DefaultPeriod(Instant begining, Instant ending) {
        if (begining.relativePosition(ending).equals(RelativePosition.BEFORE)) {
            this.begining = begining;
            this.ending = ending;
        }
        /*if (((DefaultInstant) begining).getPosition().getDate().before(((DefaultInstant) ending).getPosition().getDate())) {
            this.begining = begining;
            this.ending = ending;
        } */else {
            throw new IllegalArgumentException("The temporal position of the beginning of the period must be less than (i.e. earlier than) the temporal position of the end of the period");
        }
    }

    /**
     * Links this period to the instant at which it starts.
     */
    public Instant getBeginning() {
        return begining;
    }

    public void setBegining(Instant begining) {
        this.begining = begining;
    }

    public void setBegining(Date date) {
        this.begining = new DefaultInstant(new DefaultPosition(date));
    }

    /**
     * Links this period to the instant at which it ends.
     */
    public Instant getEnding() {
        return ending;
    }

    public void setEnding(Instant ending) {
        this.ending = ending;
    }

    public void setEnding(Date date) {
        this.ending = new DefaultInstant(new DefaultPosition(date));
    }

    /**
     * Verify if this entry is identical to the specified object.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultPeriod) {
            final DefaultPeriod that = (DefaultPeriod) object;

            return Utilities.equals(this.begining, that.begining) &&
                    Utilities.equals(this.ending, that.ending);
        }
        return false;
    }

 //   /**
 //     * Verify if this entry is identical to the specified object.
 //     */
 //    public int compareTo(Object object) {
 //        if (object == this) {
 //            return 0;
 //        }
 //        if (object instanceof DefaultPeriod) {
 //            final DefaultPeriod that = (DefaultPeriod) object;
 //
 //            if (Utilities.equals(this.begining, that.begining) &&
 //                    Utilities.equals(this.ending, that.ending))
 //                return 0;
 //            else {
 //                // TODO
 //            }
 //        }
 //        
 //       throw new ClassCastException("Object of type " + object.getClass() + " cannot be compared to " + this.getClass());
 //    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.begining != null ? this.begining.hashCode() : 0);
        hash = 37 * hash + (this.ending != null ? this.ending.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Period:").append('\n');
        if (begining != null) {
            s.append("begin:").append(begining).append('\n');
        }
        if (ending != null) {
            s.append("end:").append(ending).append('\n');
        }

        return s.toString();
    }
}
