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
/**
 */
package net.opengis.cat.csw20.impl;

import net.opengis.cat.csw20.AbstractRecordType;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.GetRecordByIdResponseType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Record By Id Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordByIdResponseTypeImpl#getAbstractRecordGroup <em>Abstract Record Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordByIdResponseTypeImpl#getAbstractRecord <em>Abstract Record</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordByIdResponseTypeImpl#getAny <em>Any</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetRecordByIdResponseTypeImpl extends EObjectImpl implements GetRecordByIdResponseType {
    /**
     * The cached value of the '{@link #getAbstractRecordGroup() <em>Abstract Record Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractRecordGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap abstractRecordGroup;

    /**
     * The cached value of the '{@link #getAny() <em>Any</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAny()
     * @generated
     * @ordered
     */
    protected FeatureMap any;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetRecordByIdResponseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.GET_RECORD_BY_ID_RESPONSE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAbstractRecordGroup() {
        if (abstractRecordGroup == null) {
            abstractRecordGroup = new BasicFeatureMap(this, Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD_GROUP);
        }
        return abstractRecordGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractRecordType> getAbstractRecord() {
        return getAbstractRecordGroup().list(Csw20Package.Literals.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAny() {
        if (any == null) {
            any = new BasicFeatureMap(this, Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ANY);
        }
        return any;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD_GROUP:
                return ((InternalEList<?>)getAbstractRecordGroup()).basicRemove(otherEnd, msgs);
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD:
                return ((InternalEList<?>)getAbstractRecord()).basicRemove(otherEnd, msgs);
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ANY:
                return ((InternalEList<?>)getAny()).basicRemove(otherEnd, msgs);
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
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD_GROUP:
                if (coreType) return getAbstractRecordGroup();
                return ((FeatureMap.Internal)getAbstractRecordGroup()).getWrapper();
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD:
                return getAbstractRecord();
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ANY:
                if (coreType) return getAny();
                return ((FeatureMap.Internal)getAny()).getWrapper();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD_GROUP:
                ((FeatureMap.Internal)getAbstractRecordGroup()).set(newValue);
                return;
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ANY:
                ((FeatureMap.Internal)getAny()).set(newValue);
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
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD_GROUP:
                getAbstractRecordGroup().clear();
                return;
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ANY:
                getAny().clear();
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
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD_GROUP:
                return abstractRecordGroup != null && !abstractRecordGroup.isEmpty();
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ABSTRACT_RECORD:
                return !getAbstractRecord().isEmpty();
            case Csw20Package.GET_RECORD_BY_ID_RESPONSE_TYPE__ANY:
                return any != null && !any.isEmpty();
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
        result.append(" (abstractRecordGroup: ");
        result.append(abstractRecordGroup);
        result.append(", any: ");
        result.append(any);
        result.append(')');
        return result.toString();
    }

} //GetRecordByIdResponseTypeImpl
