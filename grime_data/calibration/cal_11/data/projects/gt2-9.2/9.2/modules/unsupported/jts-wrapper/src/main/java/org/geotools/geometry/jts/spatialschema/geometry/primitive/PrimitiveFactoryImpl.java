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
import org.geotools.geometry.jts.spatialschema.geometry.DirectPositionImpl;
import org.geotools.geometry.jts.spatialschema.geometry.geometry.GeometryFactoryImpl;
import org.geotools.geometry.jts.spatialschema.geometry.geometry.PolygonImpl;

import java.util.Collection;
import java.util.List;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Solid;
import org.opengis.geometry.primitive.SolidBoundary;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.primitive.SurfacePatch;

/**
 * Factory that knows how to create instances of the 19107 primitives as
 * implemented in LiteGO1.
 *
 *
 *
 *
 * @source $URL$
 */
public class PrimitiveFactoryImpl implements PrimitiveFactory {

    //*************************************************************************
    //  Fields
    //*************************************************************************
    
    /**
     * a default CRS to use when creating primitives
     */
    private CoordinateReferenceSystem crs;

    private GeometryFactory geomFact;

    //*************************************************************************
    //  Constructors
    //*************************************************************************
    
    /**
     * DOCUMENT ME
     */
    public PrimitiveFactoryImpl() {
        this(null);
    }

    /**
     * DOCUMENT ME
     * 
     * @param crs
     */
    public PrimitiveFactoryImpl(final CoordinateReferenceSystem crs) {
        this.crs = crs;
        geomFact = new GeometryFactoryImpl(crs);
    }

    //*************************************************************************
    //  implement the PrimitiveFactory interface
    //*************************************************************************
    
    /**
     * Returns the coordinate reference system in use for all
     * {@linkPlain Primitive primitive}geometric objects to be created through
     * this interface.
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * Not implemented. Returns null.
     */
    public Primitive createPrimitive(final Envelope envelope) {
        return null;
    }

    /**
     * Create a direct position at the specified location specified by
     * coordinates. If the parameter is null, the position is left
     * uninitialized.
     * @param coordinates
     * @return
     */
    public DirectPosition createDirectPosition(final double[] coordinates) {
        if (coordinates != null) {
            return new DirectPositionImpl(crs, coordinates);
        } else {
            return new DirectPositionImpl(crs);
        }
    }

    /**
     * Creates a point at the specified location specified by coordinates.
     */
    public Point createPoint(final double[] coordinates) {
        return new PointImpl(createDirectPosition(coordinates), crs);
    }

    /**
     * Creates a point at the specified position.
     */
    public Point createPoint(final Position position) {
        return new PointImpl(position.getDirectPosition(), crs);
    }

    /**
     * Takes a list of {@linkPlain CurveSegment curve segments}with the
     * appropriate end-to-start relationships and creates a
     * {@linkPlain Curve curve}. This may throw an IllegalArgumentException if
     * the List contains objects that are not instances of the CurveSegment
     * interface.
     */
    public Curve createCurve(final List<CurveSegment> segments) {
        CurveImpl result = new CurveImpl(crs);
        if (segments != null)
            result.getSegments().addAll(segments);
        return result;
    }

    /**
     * Creates a new Surface. This method can't possibly be used in the current
     * implementation since there are no implementations of the SurfacePatch
     * interface. Returns null.
     */
    @SuppressWarnings("unchecked")
    public Surface createSurface(final List<SurfacePatch> patches) {
        SurfaceImpl result = new SurfaceImpl(crs);
        List<?> cast = (List<?>) patches;   
        result.getPatches().addAll( (List<SurfacePatchImpl>) cast );
        return result;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.PrimitiveFactory#createSurface(org.opengis.geometry.primitive.SurfaceBoundary)
     */
    public Surface createSurface(final SurfaceBoundary boundary) {
        // For now, our implementation has to assume that the boundary is a
        // polygon.
        Surface result = new SurfaceImpl(crs);
        Polygon poly = geomFact.createPolygon(boundary);
        // PENDING(jdc): the following line is 1.5 specific.
        // the result.getPatches() list is a generic list with a type of "? extends SurfacePatch"
        // we can compile without the generic if we cast down to List, but why do we need the cast?
        // Polygon extends SurfacePatch, so in theory this should work...
        //((List<SurfacePatch>) result.getPatches()).add(poly);
        ((List)result.getPatches()).add(poly);
        return result;
    }
    
    
    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.PrimitiveFactory#createSurfaceBoundary(org.opengis.geometry.primitive.Ring, java.util.List)
     */
    public SurfaceBoundary createSurfaceBoundary(Ring exterior, List interiors)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return new SurfaceBoundaryImpl(crs, exterior, (Ring []) interiors.toArray(new Ring[interiors.size()]));
    }

    /**
     * Constructs a {@linkPlain Solid solid}by indicating its boundary as a
     * collection of {@linkPlain Shell shells}organized into a
     * {@linkPlain SolidBoundary solid boundary}. Since this specification is
     * limited to 3-dimensional coordinate reference systems, any solid is
     * definable by its boundary.
     * @param boundary
     * @return a {@code Solid} based on the given {@code boundary}
     */
    public Solid createSolid(final SolidBoundary boundary) {
        return null;
    }

    public Ring createRing(final List curves) {
        Ring result = new RingImpl(crs);
        if (curves != null)
            result.getGenerators().addAll(curves);
        return result;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Factory#createPolyhedralSurface(java.util.List)
     */
    public PolyhedralSurface createPolyhedralSurface(List<Polygon> patches)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        PolyhedralSurfaceImpl result = new PolyhedralSurfaceImpl(crs);
        List<?> cast = (List<?>) patches;
        result.getPatches().addAll((List<PolygonImpl>) cast );
        return result;
    }
}
