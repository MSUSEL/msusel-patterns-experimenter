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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transaction Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.TransactionType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.TransactionType#getAbstractTransactionActionGroup <em>Abstract Transaction Action Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.TransactionType#getAbstractTransactionAction <em>Abstract Transaction Action</em>}</li>
 *   <li>{@link net.opengis.wfs20.TransactionType#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs20.TransactionType#getReleaseAction <em>Release Action</em>}</li>
 *   <li>{@link net.opengis.wfs20.TransactionType#getSrsName <em>Srs Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getTransactionType()
 * @model extendedMetaData="name='TransactionType' kind='elementOnly'"
 * @generated
 */
public interface TransactionType extends BaseRequestType {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getTransactionType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:3'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Abstract Transaction Action Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Transaction Action Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Transaction Action Group</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getTransactionType_AbstractTransactionActionGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='AbstractTransactionAction:group' namespace='##targetNamespace' group='#group:3'"
     * @generated
     */
    FeatureMap getAbstractTransactionActionGroup();

    /**
     * Returns the value of the '<em><b>Abstract Transaction Action</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.AbstractTransactionActionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Transaction Action</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Transaction Action</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getTransactionType_AbstractTransactionAction()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractTransactionAction' namespace='##targetNamespace' group='AbstractTransactionAction:group'"
     * @generated
     */
    EList<AbstractTransactionActionType> getAbstractTransactionAction();

    /**
     * Returns the value of the '<em><b>Lock Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lock Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lock Id</em>' attribute.
     * @see #setLockId(String)
     * @see net.opengis.wfs20.Wfs20Package#getTransactionType_LockId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='lockId'"
     * @generated
     */
    String getLockId();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TransactionType#getLockId <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Id</em>' attribute.
     * @see #getLockId()
     * @generated
     */
    void setLockId(String value);

    /**
     * Returns the value of the '<em><b>Release Action</b></em>' attribute.
     * The default value is <code>"ALL"</code>.
     * The literals are from the enumeration {@link net.opengis.wfs20.AllSomeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Release Action</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Release Action</em>' attribute.
     * @see net.opengis.wfs20.AllSomeType
     * @see #isSetReleaseAction()
     * @see #unsetReleaseAction()
     * @see #setReleaseAction(AllSomeType)
     * @see net.opengis.wfs20.Wfs20Package#getTransactionType_ReleaseAction()
     * @model default="ALL" unsettable="true"
     *        extendedMetaData="kind='attribute' name='releaseAction'"
     * @generated
     */
    AllSomeType getReleaseAction();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TransactionType#getReleaseAction <em>Release Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Release Action</em>' attribute.
     * @see net.opengis.wfs20.AllSomeType
     * @see #isSetReleaseAction()
     * @see #unsetReleaseAction()
     * @see #getReleaseAction()
     * @generated
     */
    void setReleaseAction(AllSomeType value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.TransactionType#getReleaseAction <em>Release Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetReleaseAction()
     * @see #getReleaseAction()
     * @see #setReleaseAction(AllSomeType)
     * @generated
     */
    void unsetReleaseAction();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.TransactionType#getReleaseAction <em>Release Action</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Release Action</em>' attribute is set.
     * @see #unsetReleaseAction()
     * @see #getReleaseAction()
     * @see #setReleaseAction(AllSomeType)
     * @generated
     */
    boolean isSetReleaseAction();

    /**
     * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Srs Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Srs Name</em>' attribute.
     * @see #setSrsName(String)
     * @see net.opengis.wfs20.Wfs20Package#getTransactionType_SrsName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='srsName'"
     * @generated
     */
    String getSrsName();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TransactionType#getSrsName <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' attribute.
     * @see #getSrsName()
     * @generated
     */
    void setSrsName(String value);

} // TransactionType
