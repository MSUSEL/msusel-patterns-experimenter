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
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

// J2SE direct dependencies
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfacePatch;

import org.geotools.geometry.jts.spatialschema.geometry.GeometryImpl;
import org.geotools.geometry.jts.JTSGeometry;
import org.geotools.geometry.jts.JTSUtils;
import org.opengis.geometry.primitive.SurfaceBoundary;


/**
 * Surface with a positive orientation.
 * {@code Surface} is a subclass of {@link Primitive} and is the basis for 2-dimensional
 * geometry. Unorientable surfaces such as the M&ouml;bius band are not allowed. The orientation of
 * a surface chooses an "up" direction through the choice of the upward normal, which, if the
 * surface is not a cycle, is the side of the surface from which the exterior boundary appears
 * counterclockwise. Reversal of the surface orientation reverses the curve orientation of each
 * boundary component, and interchanges the conceptual "up" and "down" direction of the surface.
 * If the surface is the boundary of a solid, the "up" direction is usually outward. For closed
 * surfaces, which have no boundary, the up direction is that of the surface patches, which must
 * be consistent with one another. Its included {@linkplain SurfacePatch surface patches} describe
 * the interior structure of a {@code Surface}.
 *
 * <blockquote><font size=2>
 * <strong>NOTE:</strong> Other than the restriction on orientability, no other "validity" condition is required for GM_Surface.
 * </font></blockquote>
 *
 * @UML type GM_Surface
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 *
 *
 *
 * @source $URL$
 * @version 2.0
 *
 * @see PrimitiveFactory#createSurface(List)
 * @see PrimitiveFactory#createSurface(SurfaceBoundary)
 */
public class SurfaceImpl extends GeometryImpl implements Surface {
    protected List<SurfacePatchImpl> patches;

    public SurfaceImpl() {
        this(null);
    }

    public SurfaceImpl(CoordinateReferenceSystem crs) {
        super(crs);
        patches = new ArrayList();
    }

    @SuppressWarnings("unchecked")
    public List<SurfacePatchImpl> getPatches() {
        return patches;
    }

    public SurfaceBoundary getBoundary() {
        return (SurfaceBoundary) super.getBoundary();
    }

    public double [] getUpNormal(DirectPosition point) {
        return new double [] { 0, 0, 1 };
    }

    public double getPerimeter() {
        return getJTSGeometry().getBoundary().getLength();
    }

    public double getArea() {
        return getJTSGeometry().getArea();
    }

    public CompositeSurface getComposite() {
        return null;
    }

    public int getOrientation() {
        return 0;
    }

    public Surface getPrimitive() {
        return this;
    }

    public Set getComplexes() {
        return null;
    }

    public Set getContainingPrimitives() {
        return null;
    }

    public OrientableSurface[] getProxy() {
        return null;
    }

    public Set getContainedPrimitives() {
        return null;
    }

    /**
     * @return
     * @see com.polexis.lite.spatialschema.geometry.GeometryImpl#computeJTSPeer()
     */
    protected com.vividsolutions.jts.geom.Geometry computeJTSPeer() {
        if (patches.size() > 1) {
            //throw new UnsupportedOperationException("This implementation does not support surfaces with multiple patches.");
            final com.vividsolutions.jts.geom.Polygon[] polygons = 
                new com.vividsolutions.jts.geom.Polygon[patches.size()];
            for (int i = 0; i < patches.size(); i++) {
                final JTSGeometry jtsGeometry = (JTSGeometry) patches.get(i);
                polygons[i] = (com.vividsolutions.jts.geom.Polygon) jtsGeometry.getJTSGeometry();
            }
            return JTSUtils.GEOMETRY_FACTORY.createMultiPolygon(polygons);
        }
        return ((JTSGeometry) patches.get(0)).getJTSGeometry();
    }
}
