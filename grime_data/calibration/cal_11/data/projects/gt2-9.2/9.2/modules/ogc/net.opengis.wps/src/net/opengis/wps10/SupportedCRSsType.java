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
package net.opengis.wps10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Supported CR Ss Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Listing of the Coordinate Reference System (CRS) support for this process input or output.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.SupportedCRSsType#getDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wps10.SupportedCRSsType#getSupported <em>Supported</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getSupportedCRSsType()
 * @model extendedMetaData="name='SupportedCRSsType' kind='elementOnly'"
 * @generated
 */
public interface SupportedCRSsType extends EObject {
    /**
     * Returns the value of the '<em><b>Default</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies the default CRS that will be used unless the Execute operation request specifies another supported CRS.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default</em>' containment reference.
     * @see #setDefault(DefaultType)
     * @see net.opengis.wps10.Wps10Package#getSupportedCRSsType_Default()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Default'"
     * @generated
     */
    DefaultType getDefault();

    /**
     * Sets the value of the '{@link net.opengis.wps10.SupportedCRSsType#getDefault <em>Default</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default</em>' containment reference.
     * @see #getDefault()
     * @generated
     */
    void setDefault(DefaultType value);

    /**
     * Returns the value of the '<em><b>Supported</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of references to all of the CRSs supported for this Input/Output. The default CRS shall be included in this list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported</em>' containment reference.
     * @see #setSupported(CRSsType)
     * @see net.opengis.wps10.Wps10Package#getSupportedCRSsType_Supported()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Supported'"
     * @generated
     */
    CRSsType getSupported();

    /**
     * Sets the value of the '{@link net.opengis.wps10.SupportedCRSsType#getSupported <em>Supported</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Supported</em>' containment reference.
     * @see #getSupported()
     * @generated
     */
    void setSupported(CRSsType value);

} // SupportedCRSsType
