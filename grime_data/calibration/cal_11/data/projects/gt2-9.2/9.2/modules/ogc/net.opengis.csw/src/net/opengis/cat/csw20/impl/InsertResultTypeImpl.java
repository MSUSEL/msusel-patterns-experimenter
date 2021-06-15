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
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.util.Collection;

import net.opengis.cat.csw20.BriefRecordType;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.InsertResultType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Insert Result Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.InsertResultTypeImpl#getBriefRecord <em>Brief Record</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.InsertResultTypeImpl#getHandleRef <em>Handle Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InsertResultTypeImpl extends EObjectImpl implements InsertResultType {
    /**
     * The cached value of the '{@link #getBriefRecord() <em>Brief Record</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBriefRecord()
     * @generated
     * @ordered
     */
    protected EList<BriefRecordType> briefRecord;

    /**
     * The default value of the '{@link #getHandleRef() <em>Handle Ref</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHandleRef()
     * @generated
     * @ordered
     */
    protected static final String HANDLE_REF_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHandleRef() <em>Handle Ref</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHandleRef()
     * @generated
     * @ordered
     */
    protected String handleRef = HANDLE_REF_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InsertResultTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.INSERT_RESULT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<BriefRecordType> getBriefRecord() {
        if (briefRecord == null) {
            briefRecord = new EObjectContainmentEList<BriefRecordType>(BriefRecordType.class, this, Csw20Package.INSERT_RESULT_TYPE__BRIEF_RECORD);
        }
        return briefRecord;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHandleRef() {
        return handleRef;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHandleRef(String newHandleRef) {
        String oldHandleRef = handleRef;
        handleRef = newHandleRef;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.INSERT_RESULT_TYPE__HANDLE_REF, oldHandleRef, handleRef));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.INSERT_RESULT_TYPE__BRIEF_RECORD:
                return ((InternalEList<?>)getBriefRecord()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.INSERT_RESULT_TYPE__BRIEF_RECORD:
                return getBriefRecord();
            case Csw20Package.INSERT_RESULT_TYPE__HANDLE_REF:
                return getHandleRef();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Csw20Package.INSERT_RESULT_TYPE__BRIEF_RECORD:
                getBriefRecord().clear();
                getBriefRecord().addAll((Collection<? extends BriefRecordType>)newValue);
                return;
            case Csw20Package.INSERT_RESULT_TYPE__HANDLE_REF:
                setHandleRef((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Csw20Package.INSERT_RESULT_TYPE__BRIEF_RECORD:
                getBriefRecord().clear();
                return;
            case Csw20Package.INSERT_RESULT_TYPE__HANDLE_REF:
                setHandleRef(HANDLE_REF_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Csw20Package.INSERT_RESULT_TYPE__BRIEF_RECORD:
                return briefRecord != null && !briefRecord.isEmpty();
            case Csw20Package.INSERT_RESULT_TYPE__HANDLE_REF:
                return HANDLE_REF_EDEFAULT == null ? handleRef != null : !HANDLE_REF_EDEFAULT.equals(handleRef);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (handleRef: ");
        result.append(handleRef);
        result.append(')');
        return result.toString();
    }

} //InsertResultTypeImpl
