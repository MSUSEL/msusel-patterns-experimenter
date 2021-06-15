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
package net.opengis.ows20.impl;

import net.opengis.ows20.AdditionalParameterType;
import net.opengis.ows20.AdditionalParametersBaseType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Additional Parameters Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.AdditionalParametersBaseTypeImpl#getAdditionalParameter <em>Additional Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AdditionalParametersBaseTypeImpl extends MetadataTypeImpl implements AdditionalParametersBaseType {
    /**
     * The cached value of the '{@link #getAdditionalParameter() <em>Additional Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAdditionalParameter()
     * @generated
     * @ordered
     */
    protected AdditionalParameterType additionalParameter;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AdditionalParametersBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.ADDITIONAL_PARAMETERS_BASE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditionalParameterType getAdditionalParameter() {
        return additionalParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAdditionalParameter(AdditionalParameterType newAdditionalParameter, NotificationChain msgs) {
        AdditionalParameterType oldAdditionalParameter = additionalParameter;
        additionalParameter = newAdditionalParameter;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE__ADDITIONAL_PARAMETER, oldAdditionalParameter, newAdditionalParameter);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAdditionalParameter(AdditionalParameterType newAdditionalParameter) {
        if (newAdditionalParameter != additionalParameter) {
            NotificationChain msgs = null;
            if (additionalParameter != null)
                msgs = ((InternalEObject)additionalParameter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE__ADDITIONAL_PARAMETER, null, msgs);
            if (newAdditionalParameter != null)
                msgs = ((InternalEObject)newAdditionalParameter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE__ADDITIONAL_PARAMETER, null, msgs);
            msgs = basicSetAdditionalParameter(newAdditionalParameter, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE__ADDITIONAL_PARAMETER, newAdditionalParameter, newAdditionalParameter));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE__ADDITIONAL_PARAMETER:
                return basicSetAdditionalParameter(null, msgs);
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
            case Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE__ADDITIONAL_PARAMETER:
                return getAdditionalParameter();
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
            case Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE__ADDITIONAL_PARAMETER:
                setAdditionalParameter((AdditionalParameterType)newValue);
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
            case Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE__ADDITIONAL_PARAMETER:
                setAdditionalParameter((AdditionalParameterType)null);
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
            case Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE__ADDITIONAL_PARAMETER:
                return additionalParameter != null;
        }
        return super.eIsSet(featureID);
    }

} //AdditionalParametersBaseTypeImpl
