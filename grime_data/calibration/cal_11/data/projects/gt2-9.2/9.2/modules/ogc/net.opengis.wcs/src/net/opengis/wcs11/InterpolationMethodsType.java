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
package net.opengis.wcs11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interpolation Methods Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.InterpolationMethodsType#getInterpolationMethod <em>Interpolation Method</em>}</li>
 *   <li>{@link net.opengis.wcs11.InterpolationMethodsType#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getInterpolationMethodsType()
 * @model extendedMetaData="name='InterpolationMethods_._type' kind='elementOnly'"
 * @generated
 */
public interface InterpolationMethodsType extends EObject {
    /**
     * Returns the value of the '<em><b>Interpolation Method</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs11.InterpolationMethodType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of identifiers of spatial interpolation methods. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interpolation Method</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getInterpolationMethodsType_InterpolationMethod()
     * @model type="net.opengis.wcs11.InterpolationMethodType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='InterpolationMethod' namespace='##targetNamespace'"
     * @generated
     */
    EList getInterpolationMethod();

    /**
     * Returns the value of the '<em><b>Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of interpolation method that will be used if the client does not specify one. Should be included when a default exists and is known. 
     * (Arliss) Can any string be used to identify an interpolation method in a KVP encoded Get Coverage operation request? 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default</em>' attribute.
     * @see #setDefault(String)
     * @see net.opengis.wcs11.Wcs111Package#getInterpolationMethodsType_Default()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='Default' namespace='##targetNamespace'"
     * @generated
     */
    String getDefault();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.InterpolationMethodsType#getDefault <em>Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default</em>' attribute.
     * @see #getDefault()
     * @generated
     */
    void setDefault(String value);

} // InterpolationMethodsType
