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
package org.geotools.data.efeature.tests;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Non Geo EObject</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.geotools.data.efeature.tests.NonGeoEObject#getNonGeoAttribute <em>Non Geo Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getNonGeoEObject()
 * @model
 * @generated
 *
 * @source $URL$
 */
public interface NonGeoEObject extends EObject {
    /**
     * Returns the value of the '<em><b>Non Geo Attribute</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Non Geo Attribute</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Non Geo Attribute</em>' attribute.
     * @see #setNonGeoAttribute(int)
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getNonGeoEObject_NonGeoAttribute()
     * @model
     * @generated
     */
    int getNonGeoAttribute();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.tests.NonGeoEObject#getNonGeoAttribute <em>Non Geo Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Non Geo Attribute</em>' attribute.
     * @see #getNonGeoAttribute()
     * @generated
     */
    void setNonGeoAttribute(int value);

} // NonGeoEObject
