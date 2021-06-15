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
 * A representation of the model object '<em><b>Supported Interpolations Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Unordered list of interpolation methods supported.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.SupportedInterpolationsType#getInterpolationMethod <em>Interpolation Method</em>}</li>
 *   <li>{@link net.opengis.wcs10.SupportedInterpolationsType#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getSupportedInterpolationsType()
 * @model extendedMetaData="name='SupportedInterpolationsType' kind='elementOnly'"
 * @generated
 */
public interface SupportedInterpolationsType extends EObject {
    /**
	 * Returns the value of the '<em><b>Interpolation Method</b></em>' attribute.
	 * The literals are from the enumeration {@link net.opengis.wcs10.InterpolationMethodType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Interpolation Method</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Interpolation Method</em>' attribute.
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @see #setInterpolationMethod(InterpolationMethodType)
	 * @see net.opengis.wcs10.Wcs10Package#getSupportedInterpolationsType_InterpolationMethod()
	 * @model unique="false" required="true"
	 *        extendedMetaData="kind='element' name='interpolationMethod' namespace='##targetNamespace'"
	 * @generated
	 */
    InterpolationMethodType getInterpolationMethod();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.SupportedInterpolationsType#getInterpolationMethod <em>Interpolation Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interpolation Method</em>' attribute.
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @see #getInterpolationMethod()
	 * @generated
	 */
	void setInterpolationMethod(InterpolationMethodType value);

				/**
	 * Returns the value of the '<em><b>Default</b></em>' attribute.
	 * The default value is <code>"nearest neighbor"</code>.
	 * The literals are from the enumeration {@link net.opengis.wcs10.InterpolationMethodType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Default</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Default</em>' attribute.
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @see #isSetDefault()
	 * @see #unsetDefault()
	 * @see #setDefault(InterpolationMethodType)
	 * @see net.opengis.wcs10.Wcs10Package#getSupportedInterpolationsType_Default()
	 * @model default="nearest neighbor" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='default'"
	 * @generated
	 */
    InterpolationMethodType getDefault();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.SupportedInterpolationsType#getDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default</em>' attribute.
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @see #isSetDefault()
	 * @see #unsetDefault()
	 * @see #getDefault()
	 * @generated
	 */
    void setDefault(InterpolationMethodType value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.SupportedInterpolationsType#getDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetDefault()
	 * @see #getDefault()
	 * @see #setDefault(InterpolationMethodType)
	 * @generated
	 */
    void unsetDefault();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.SupportedInterpolationsType#getDefault <em>Default</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Default</em>' attribute is set.
	 * @see #unsetDefault()
	 * @see #getDefault()
	 * @see #setDefault(InterpolationMethodType)
	 * @generated
	 */
    boolean isSetDefault();

} // SupportedInterpolationsType
