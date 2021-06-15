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
package net.opengis.wfs20;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.ValueReferenceType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wfs20.ValueReferenceType#getAction <em>Action</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getValueReferenceType()
 * @model extendedMetaData="name='ValueReference_._type' kind='simple'"
 * @generated
 */
public interface ValueReferenceType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see net.opengis.wfs20.Wfs20Package#getValueReferenceType_Value()
     * @model 
     */
    QName getValue();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.ValueReferenceType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(QName value);

    /**
     * Returns the value of the '<em><b>Action</b></em>' attribute.
     * The default value is <code>"replace"</code>.
     * The literals are from the enumeration {@link net.opengis.wfs20.UpdateActionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Action</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Action</em>' attribute.
     * @see net.opengis.wfs20.UpdateActionType
     * @see #isSetAction()
     * @see #unsetAction()
     * @see #setAction(UpdateActionType)
     * @see net.opengis.wfs20.Wfs20Package#getValueReferenceType_Action()
     * @model default="replace" unsettable="true"
     *        extendedMetaData="kind='attribute' name='action'"
     * @generated
     */
    UpdateActionType getAction();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.ValueReferenceType#getAction <em>Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Action</em>' attribute.
     * @see net.opengis.wfs20.UpdateActionType
     * @see #isSetAction()
     * @see #unsetAction()
     * @see #getAction()
     * @generated
     */
    void setAction(UpdateActionType value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.ValueReferenceType#getAction <em>Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetAction()
     * @see #getAction()
     * @see #setAction(UpdateActionType)
     * @generated
     */
    void unsetAction();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.ValueReferenceType#getAction <em>Action</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Action</em>' attribute is set.
     * @see #unsetAction()
     * @see #getAction()
     * @see #setAction(UpdateActionType)
     * @generated
     */
    boolean isSetAction();

} // ValueReferenceType
