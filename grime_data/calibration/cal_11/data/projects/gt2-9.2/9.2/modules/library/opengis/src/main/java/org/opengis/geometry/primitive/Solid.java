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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.primitive;

import org.opengis.annotation.Association;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Basis for 3-dimensional geometry. The extent of a solid is
 * defined by the boundary surfaces.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see PrimitiveFactory#createSolid
 *
 * @todo Some associations are commented out for now.
 */
@UML(identifier="GM_Solid", specification=ISO_19107)
public interface Solid extends Primitive {
    /**
     * Returns a sequence of sets of {@linkplain Surface surfaces} that limit the extent of this
     * {@code Solid}. These surfaces shall be organized into one set of surfaces for each
     * boundary component of this {@code Solid}. Each of these shells shall be a cycle
     * (closed composite surface without boundary).
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> The exterior shell of a solid is defined only because the embedding
     * coordinate space is always a 3D Euclidean one. In general, a solid in a bounded 3-dimensional
     * manifold has no distinguished exterior boundary. In cases where "exterior" boundary is not
     * well defined, all the shells of the {@linkplain SolidBoundary solid boundary} shall be
     * listed as "interior".
     * </font></blockquote>
     *
     * The {@linkplain OrientableSurface orientable surfaces} that bound a solid shall be oriented
     * outward - that is, the "top" of each {@linkplain Surface surface} as defined by its orientation
     * shall face away from the interior of the solid. Each {@linkplain Shell shell}, when viewed as
     * a composite surface, shall be a cycle.
     *
     * @return The sets of positions on the boundary.
     */
    @UML(identifier="boundary", obligation=MANDATORY, specification=ISO_19107)
    SolidBoundary getBoundary();

    /**
     * Returns the sum of the surface areas of all of the boundary components of a solid.
     *
     * @return The area of this solid.
     *
     * @todo In UML diagram, this operation has an {@code Area} return type.
     */
    @UML(identifier="area", obligation=MANDATORY, specification=ISO_19107)
    double getArea();

    /**
     * Returns the volume of this solid. This is the volume interior to the exterior
     * boundary shell minus the sum of the volumes interior to any interior boundary
     * shell.
     *
     * @return The volume of this solid.
     *
     * @todo In UML diagram, this operation has a {@code Volume} return type.
     */
    @UML(identifier="volume", obligation=MANDATORY, specification=ISO_19107)
    double getVolume();

    /**
     * Returns always {@code null}, since solids have no proxy.
     *
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Oriented")
    @UML(identifier="proxy", obligation=FORBIDDEN, specification=ISO_19107)
    OrientablePrimitive[] getProxy();

//    public org.opengis.geometry.complex.GM_CompositeSolid[] getComposite() { return null; }
}
