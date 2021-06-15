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
package net.opengis.wcs20.impl;

import java.lang.Object;

import net.opengis.wcs20.OfferedCoverageType;
import net.opengis.wcs20.ServiceParametersType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Offered Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.OfferedCoverageTypeImpl#getAbstractCoverage <em>Abstract Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.OfferedCoverageTypeImpl#getServiceParameters <em>Service Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OfferedCoverageTypeImpl extends EObjectImpl implements OfferedCoverageType {
    /**
     * The cached value of the '{@link #getServiceParameters() <em>Service Parameters</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceParameters()
     * @generated
     * @ordered
     */
    protected ServiceParametersType serviceParameters;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OfferedCoverageTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.OFFERED_COVERAGE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getAbstractCoverage() {
        // TODO: implement this method to return the 'Abstract Coverage' containment reference
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstractCoverage(Object newAbstractCoverage, NotificationChain msgs) {
        // TODO: implement this method to set the contained 'Abstract Coverage' containment reference
        // -> this method is automatically invoked to keep the containment relationship in synch
        // -> do not modify other features
        // -> return msgs, after adding any generated Notification to it (if it is null, a NotificationChain object must be created first)
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceParametersType getServiceParameters() {
        return serviceParameters;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceParameters(ServiceParametersType newServiceParameters, NotificationChain msgs) {
        ServiceParametersType oldServiceParameters = serviceParameters;
        serviceParameters = newServiceParameters;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS, oldServiceParameters, newServiceParameters);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceParameters(ServiceParametersType newServiceParameters) {
        if (newServiceParameters != serviceParameters) {
            NotificationChain msgs = null;
            if (serviceParameters != null)
                msgs = ((InternalEObject)serviceParameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS, null, msgs);
            if (newServiceParameters != null)
                msgs = ((InternalEObject)newServiceParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS, null, msgs);
            msgs = basicSetServiceParameters(newServiceParameters, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS, newServiceParameters, newServiceParameters));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.OFFERED_COVERAGE_TYPE__ABSTRACT_COVERAGE:
                return basicSetAbstractCoverage(null, msgs);
            case Wcs20Package.OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS:
                return basicSetServiceParameters(null, msgs);
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
            case Wcs20Package.OFFERED_COVERAGE_TYPE__ABSTRACT_COVERAGE:
                return getAbstractCoverage();
            case Wcs20Package.OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS:
                return getServiceParameters();
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
            case Wcs20Package.OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS:
                setServiceParameters((ServiceParametersType)newValue);
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
            case Wcs20Package.OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS:
                setServiceParameters((ServiceParametersType)null);
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
            case Wcs20Package.OFFERED_COVERAGE_TYPE__ABSTRACT_COVERAGE:
                return getAbstractCoverage() != null;
            case Wcs20Package.OFFERED_COVERAGE_TYPE__SERVICE_PARAMETERS:
                return serviceParameters != null;
        }
        return super.eIsSet(featureID);
    }

} //OfferedCoverageTypeImpl
