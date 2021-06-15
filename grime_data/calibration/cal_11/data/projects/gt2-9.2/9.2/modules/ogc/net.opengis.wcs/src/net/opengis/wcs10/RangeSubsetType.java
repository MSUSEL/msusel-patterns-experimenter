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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Subset Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a subset of the named coverage range(s). Currently, only a value enumeration definition of a range subset.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.RangeSubsetType#getAxisSubset <em>Axis Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getRangeSubsetType()
 * @model extendedMetaData="name='RangeSubsetType' kind='elementOnly'"
 * @generated
 */
public interface RangeSubsetType extends EObject {
    /**
	 * Returns the value of the '<em><b>Axis Subset</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.AxisSubsetType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Ordered sequence of points and/or intervals along one axis of a compound range set.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Axis Subset</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getRangeSubsetType_AxisSubset()
	 * @model type="net.opengis.wcs10.AxisSubsetType" containment="true" required="true"
	 *        extendedMetaData="kind='element' name='axisSubset' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getAxisSubset();

} // RangeSubsetType
