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

import net.opengis.wcs20.InterpolationAxesType;
import net.opengis.wcs20.InterpolationMethodType;
import net.opengis.wcs20.InterpolationType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interpolation Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.InterpolationTypeImpl#getInterpolationMethod <em>Interpolation Method</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.InterpolationTypeImpl#getInterpolationAxes <em>Interpolation Axes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterpolationTypeImpl extends EObjectImpl implements InterpolationType {
    /**
     * The cached value of the '{@link #getInterpolationMethod() <em>Interpolation Method</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationMethod()
     * @generated
     * @ordered
     */
    protected InterpolationMethodType interpolationMethod;

    /**
     * The cached value of the '{@link #getInterpolationAxes() <em>Interpolation Axes</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationAxes()
     * @generated
     * @ordered
     */
    protected InterpolationAxesType interpolationAxes;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InterpolationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.INTERPOLATION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodType getInterpolationMethod() {
        if (interpolationMethod != null && interpolationMethod.eIsProxy()) {
            InternalEObject oldInterpolationMethod = (InternalEObject)interpolationMethod;
            interpolationMethod = (InterpolationMethodType)eResolveProxy(oldInterpolationMethod);
            if (interpolationMethod != oldInterpolationMethod) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_METHOD, oldInterpolationMethod, interpolationMethod));
            }
        }
        return interpolationMethod;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodType basicGetInterpolationMethod() {
        return interpolationMethod;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInterpolationMethod(InterpolationMethodType newInterpolationMethod) {
        InterpolationMethodType oldInterpolationMethod = interpolationMethod;
        interpolationMethod = newInterpolationMethod;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_METHOD, oldInterpolationMethod, interpolationMethod));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationAxesType getInterpolationAxes() {
        if (interpolationAxes != null && interpolationAxes.eIsProxy()) {
            InternalEObject oldInterpolationAxes = (InternalEObject)interpolationAxes;
            interpolationAxes = (InterpolationAxesType)eResolveProxy(oldInterpolationAxes);
            if (interpolationAxes != oldInterpolationAxes) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_AXES, oldInterpolationAxes, interpolationAxes));
            }
        }
        return interpolationAxes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationAxesType basicGetInterpolationAxes() {
        return interpolationAxes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInterpolationAxes(InterpolationAxesType newInterpolationAxes) {
        InterpolationAxesType oldInterpolationAxes = interpolationAxes;
        interpolationAxes = newInterpolationAxes;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_AXES, oldInterpolationAxes, interpolationAxes));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_METHOD:
                if (resolve) return getInterpolationMethod();
                return basicGetInterpolationMethod();
            case Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_AXES:
                if (resolve) return getInterpolationAxes();
                return basicGetInterpolationAxes();
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
            case Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_METHOD:
                setInterpolationMethod((InterpolationMethodType)newValue);
                return;
            case Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_AXES:
                setInterpolationAxes((InterpolationAxesType)newValue);
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
            case Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_METHOD:
                setInterpolationMethod((InterpolationMethodType)null);
                return;
            case Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_AXES:
                setInterpolationAxes((InterpolationAxesType)null);
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
            case Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_METHOD:
                return interpolationMethod != null;
            case Wcs20Package.INTERPOLATION_TYPE__INTERPOLATION_AXES:
                return interpolationAxes != null;
        }
        return super.eIsSet(featureID);
    }

} //InterpolationTypeImpl
