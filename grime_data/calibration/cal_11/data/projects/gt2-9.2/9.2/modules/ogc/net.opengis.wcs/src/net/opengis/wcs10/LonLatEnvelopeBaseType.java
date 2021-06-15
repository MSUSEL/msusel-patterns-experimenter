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
package net.opengis.wcs10;

import net.opengis.gml.EnvelopeType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lon Lat Envelope Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * For WCS use, LonLatEnvelopeBaseType restricts gml:Envelope to the WGS84 geographic CRS with Longitude preceding Latitude and both using decimal degrees only. If included, height values are third and use metre units.
 * Envelope defines an extent using a pair of positions defining opposite corners in arbitrary dimensions.
 * <!-- end-model-doc -->
 *
 *
 * @see net.opengis.wcs10.Wcs10Package#getLonLatEnvelopeBaseType()
 * @model extendedMetaData="name='LonLatEnvelopeBaseType' kind='elementOnly'"
 * @generated
 */
public interface LonLatEnvelopeBaseType extends EnvelopeType {
} // LonLatEnvelopeBaseType
