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
package org.opengis.referencing.crs;

import org.opengis.referencing.datum.EngineeringDatum;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A contextually local coordinate reference system. It can be divided into two broad categories:
 * <p>
 * <ul>
 *   <li>earth-fixed systems applied to engineering activities on or near the surface of the
 *       earth;</li>
 *   <li>CRSs on moving platforms such as road vehicles, vessels, aircraft, or spacecraft.</li>
 * </ul>
 * <p>
 * Earth-fixed Engineering CRSs are commonly based on a simple flat-earth approximation of the
 * earth's surface, and the effect of earth curvature on feature geometry is ignored: calculations
 * on coordinates use simple plane arithmetic without any corrections for earth curvature. The
 * application of such Engineering CRSs to relatively small areas and "contextually local" is in
 * this case equivalent to "spatially local".
 * <p>
 * Engineering CRSs used on moving platforms are usually intermediate coordinate reference
 * systems that are computationally required to calculate coordinates referenced to
 * {@linkplain GeocentricCRS geocentric}, {@linkplain GeographicCRS geographic} or
 * {@linkplain ProjectedCRS projected} CRSs. These engineering coordinate reference
 * systems are subject to all the motions of the platform with which they are associated.
 * In this case "contextually local" means that the associated coordinates are meaningful
 * only relative to the moving platform. Earth curvature is usually irrelevant and is therefore
 * ignored. In the spatial sense their applicability may extend from the immediate vicinity of
 * the platform (e.g. a moving seismic ship) to the entire earth (e.g. in space applications).
 * The determining factor is the mathematical model deployed in the positioning calculations.
 * Transformation of coordinates from these moving Engineering CRSs to earth-referenced coordinate
 * reference systems involves time-dependent coordinate operation parameters.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.opengis.referencing.cs.AffineCS           Affine},
 *   {@link org.opengis.referencing.cs.CartesianCS        Cartesian},
 *   {@link org.opengis.referencing.cs.EllipsoidalCS      Ellipsoidal},
 *   {@link org.opengis.referencing.cs.SphericalCS        Spherical},
 *   {@link org.opengis.referencing.cs.CylindricalCS      Cylindrical},
 *   {@link org.opengis.referencing.cs.PolarCS            Polar},
 *   {@link org.opengis.referencing.cs.VerticalCS         Vertical},
 *   {@link org.opengis.referencing.cs.LinearCS           Linear}
 * </TD></TR></TABLE>
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="SC_EngineeringCRS", specification=ISO_19111)
public interface EngineeringCRS extends SingleCRS {
    /**
     * Returns the datum, which must be an engineering one.
     */
    @UML(identifier="usesDatum", obligation=MANDATORY, specification=ISO_19111)
    EngineeringDatum getDatum();
}
