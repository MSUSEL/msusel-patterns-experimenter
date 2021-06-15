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
package net.opengis.cat.csw20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Of Values Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.RangeOfValuesType#getMinValue <em>Min Value</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.RangeOfValuesType#getMaxValue <em>Max Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getRangeOfValuesType()
 * @model extendedMetaData="name='RangeOfValuesType' kind='elementOnly'"
 * @generated
 */
public interface RangeOfValuesType extends EObject {
    /**
     * Returns the value of the '<em><b>Min Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Min Value</em>' containment reference.
     * @see #setMinValue(EObject)
     * @see net.opengis.cat.csw20.Csw20Package#getRangeOfValuesType_MinValue()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='MinValue' namespace='##targetNamespace'"
     * @generated
     */
    EObject getMinValue();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.RangeOfValuesType#getMinValue <em>Min Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Min Value</em>' containment reference.
     * @see #getMinValue()
     * @generated
     */
    void setMinValue(EObject value);

    /**
     * Returns the value of the '<em><b>Max Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Max Value</em>' containment reference.
     * @see #setMaxValue(EObject)
     * @see net.opengis.cat.csw20.Csw20Package#getRangeOfValuesType_MaxValue()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='MaxValue' namespace='##targetNamespace'"
     * @generated
     */
    EObject getMaxValue();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.RangeOfValuesType#getMaxValue <em>Max Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Max Value</em>' containment reference.
     * @see #getMaxValue()
     * @generated
     */
    void setMaxValue(EObject value);

} // RangeOfValuesType
