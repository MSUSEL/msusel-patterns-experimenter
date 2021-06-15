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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lon Lat Envelope Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines spatial extent by extending LonLatEnvelope with an optional time position pair.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.LonLatEnvelopeType#getTimePosition <em>Time Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getLonLatEnvelopeType()
 * @model extendedMetaData="name='LonLatEnvelopeType' kind='elementOnly'"
 * @generated
 */
public interface LonLatEnvelopeType extends LonLatEnvelopeBaseType {
    /**
	 * Returns the value of the '<em><b>Time Position</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.TimePositionType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Direct representation of a temporal position.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Time Position</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getLonLatEnvelopeType_TimePosition()
	 * @model type="net.opengis.gml.TimePositionType" containment="true" upper="2"
	 *        extendedMetaData="kind='element' name='timePosition' namespace='http://www.opengis.net/gml'"
	 * @generated
	 */
    EList getTimePosition();

} // LonLatEnvelopeType
