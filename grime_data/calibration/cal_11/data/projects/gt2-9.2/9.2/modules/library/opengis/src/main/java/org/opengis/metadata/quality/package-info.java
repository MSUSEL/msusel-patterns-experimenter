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
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

/**
 * {@linkplain org.opengis.metadata.quality.DataQuality Data quality} and
 * {@linkplain org.opengis.metadata.quality.PositionalAccuracy positional accuracy}.
 * The following is adapted from
 * <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">OpenGIS&reg;
 * Spatial Referencing by Coordinates (Topic 2)</A> specification.
 *
 * <P ALIGN="justify">The parameters that define a coordinate reference system
 * are chosen rather than measured to satisfy the degrees-of-freedom problem in
 * the changeover from observation to coordinate quantities. Coordinate reference
 * systems are therefore by definition error-free (i.e., non-stochastic).
 * A coordinate reference system is realised through a network of control points.
 * The coordinates of those control points, derived from surface and/or from
 * satellite observations, are stochastic. Their accuracy can be expressed in a
 * covariance matrix, which, due to the degrees-of-freedom problem, will have a
 * rank deficiency, described in geodetic literature.</P>
 *
 * <P ALIGN="justify">Coordinate transformations between coordinate reference
 * systems usually have parameter values derived from two sets of point coordinates,
 * one set in system 1, the other set in system 2. As these coordinates are stochastic
 * (i.e., have random-error characteristics) the derived transformation parameter
 * values will also be stochastic. Their covariance matrix can be calculated.</P>
 *
 * <P ALIGN="justify">Coordinates that have not been "naturally" determined in
 * coordinate reference system 2, but have been determined in coordinate system 1
 * and then transformed to system 2, have the random error effects of the
 * transformation superimposed on their original error characteristics. It may be
 * possible in well-controlled cases to calculate the covariance matrices of the
 * point coordinates before and after the transformation, and thus isolate the
 * effect of the transformation, but in practice a user will only be interested
 * in the accuracy of the final transformed coordinates.</P>
 *
 * <P ALIGN="justify">Nevertheless the option is offered to specify the
 * covariance matrix of point coordinates resulting exclusively from the
 * transformation. It is outside the scope of this specification to describe
 * how that covariance matrix should be used. Because a covariance matrix is
 * symmetrical, only the upper or lower diagonal part (including the main diagonal)
 * needs to be specified.</P>
 *
 * <P ALIGN="justify">For some transformations, this accuracy information is
 * compacted in some assessment of an average impact on horizontal position and
 * vertical position, allowing specification of average absolute accuracy and,
 * when relevant and available, average relative accuracy. Hence separate quality
 * measures may be specified for horizontal and for vertical position in those
 * objects.</P>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @since   GeoAPI 2.0
 */
package org.opengis.metadata.quality;
