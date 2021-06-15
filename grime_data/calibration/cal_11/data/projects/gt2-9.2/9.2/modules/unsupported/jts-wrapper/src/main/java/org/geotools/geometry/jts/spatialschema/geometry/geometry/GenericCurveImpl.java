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
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

// OpenGIS direct dependencies
import org.opengis.geometry.coordinate.GenericCurve;

import org.geotools.geometry.jts.JTSGeometry;

/**
 * The {@code GenericCurveImpl} class/interface...
 * 
 * @author SYS Technologies
 * @author crossley
 *
 *
 *
 *
 * @source $URL$
 * @version $Revision $
 */
public abstract class GenericCurveImpl implements GenericCurve, JTSGeometry {

    //*************************************************************************
    //  fields
    //*************************************************************************

    private com.vividsolutions.jts.geom.Geometry jtsPeer;

    protected JTSGeometry parent;

    public final void setParent(JTSGeometry parent) {
        this.parent = parent;
    }

    /**
     * Subclasses must override this method to compute the JTS equivalent of
     * this geometry.
     */
    protected abstract com.vividsolutions.jts.geom.Geometry computeJTSPeer();

    /**
     * This method must be called by subclasses whenever the user makes a change
     * to the geometry so that the cached JTS object can be recreated.
     */
    public final void invalidateCachedJTSPeer() {
        jtsPeer = null;
        if (parent != null) parent.invalidateCachedJTSPeer();
    }

    /**
     * This method is meant to be invoked by the JTSUtils utility class when it
     * creates a Geometry from a JTS geometry.  This prevents the Geometry from
     * having to recompute the JTS peer the first time.
     */
    protected final void setJTSPeer(com.vividsolutions.jts.geom.Geometry g) {
        jtsPeer = g;
    }

    /**
     * Returns the JTS version of this geometry.  If the geometry has not
     * changed since the last time this method was called, it will return the
     * exact same object.
     */
    public final com.vividsolutions.jts.geom.Geometry getJTSGeometry() {
        if (jtsPeer == null) {
            jtsPeer = computeJTSPeer();
        }
        return jtsPeer;
    }
}
