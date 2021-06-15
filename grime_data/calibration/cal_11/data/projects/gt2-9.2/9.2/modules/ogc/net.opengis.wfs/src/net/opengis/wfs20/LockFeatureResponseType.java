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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lock Feature Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.LockFeatureResponseType#getFeaturesLocked <em>Features Locked</em>}</li>
 *   <li>{@link net.opengis.wfs20.LockFeatureResponseType#getFeaturesNotLocked <em>Features Not Locked</em>}</li>
 *   <li>{@link net.opengis.wfs20.LockFeatureResponseType#getLockId <em>Lock Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getLockFeatureResponseType()
 * @model extendedMetaData="name='LockFeatureResponseType' kind='elementOnly'"
 * @generated
 */
public interface LockFeatureResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Features Locked</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Features Locked</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Features Locked</em>' containment reference.
     * @see #setFeaturesLocked(FeaturesLockedType)
     * @see net.opengis.wfs20.Wfs20Package#getLockFeatureResponseType_FeaturesLocked()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='FeaturesLocked' namespace='##targetNamespace'"
     * @generated
     */
    FeaturesLockedType getFeaturesLocked();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.LockFeatureResponseType#getFeaturesLocked <em>Features Locked</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Features Locked</em>' containment reference.
     * @see #getFeaturesLocked()
     * @generated
     */
    void setFeaturesLocked(FeaturesLockedType value);

    /**
     * Returns the value of the '<em><b>Features Not Locked</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Features Not Locked</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Features Not Locked</em>' containment reference.
     * @see #setFeaturesNotLocked(FeaturesNotLockedType)
     * @see net.opengis.wfs20.Wfs20Package#getLockFeatureResponseType_FeaturesNotLocked()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='FeaturesNotLocked' namespace='##targetNamespace'"
     * @generated
     */
    FeaturesNotLockedType getFeaturesNotLocked();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.LockFeatureResponseType#getFeaturesNotLocked <em>Features Not Locked</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Features Not Locked</em>' containment reference.
     * @see #getFeaturesNotLocked()
     * @generated
     */
    void setFeaturesNotLocked(FeaturesNotLockedType value);

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
     * @see net.opengis.wfs20.Wfs20Package#getLockFeatureResponseType_LockId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='lockId'"
     * @generated
     */
    String getLockId();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.LockFeatureResponseType#getLockId <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Id</em>' attribute.
     * @see #getLockId()
     * @generated
     */
    void setLockId(String value);

} // LockFeatureResponseType
