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

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Native Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.NativeType#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wfs20.NativeType#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.wfs20.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}</li>
 *   <li>{@link net.opengis.wfs20.NativeType#getVendorId <em>Vendor Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getNativeType()
 * @model extendedMetaData="name='NativeType' kind='mixed'"
 * @generated
 */
public interface NativeType extends AbstractTransactionActionType {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getNativeType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getNativeType_Any()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':2' processing='lax'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Safe To Ignore</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Safe To Ignore</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Safe To Ignore</em>' attribute.
     * @see #isSetSafeToIgnore()
     * @see #unsetSafeToIgnore()
     * @see #setSafeToIgnore(boolean)
     * @see net.opengis.wfs20.Wfs20Package#getNativeType_SafeToIgnore()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
     *        extendedMetaData="kind='attribute' name='safeToIgnore'"
     * @generated
     */
    boolean isSafeToIgnore();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Safe To Ignore</em>' attribute.
     * @see #isSetSafeToIgnore()
     * @see #unsetSafeToIgnore()
     * @see #isSafeToIgnore()
     * @generated
     */
    void setSafeToIgnore(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetSafeToIgnore()
     * @see #isSafeToIgnore()
     * @see #setSafeToIgnore(boolean)
     * @generated
     */
    void unsetSafeToIgnore();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Safe To Ignore</em>' attribute is set.
     * @see #unsetSafeToIgnore()
     * @see #isSafeToIgnore()
     * @see #setSafeToIgnore(boolean)
     * @generated
     */
    boolean isSetSafeToIgnore();

    /**
     * Returns the value of the '<em><b>Vendor Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vendor Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vendor Id</em>' attribute.
     * @see #setVendorId(String)
     * @see net.opengis.wfs20.Wfs20Package#getNativeType_VendorId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='vendorId'"
     * @generated
     */
    String getVendorId();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.NativeType#getVendorId <em>Vendor Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vendor Id</em>' attribute.
     * @see #getVendorId()
     * @generated
     */
    void setVendorId(String value);

} // NativeType
