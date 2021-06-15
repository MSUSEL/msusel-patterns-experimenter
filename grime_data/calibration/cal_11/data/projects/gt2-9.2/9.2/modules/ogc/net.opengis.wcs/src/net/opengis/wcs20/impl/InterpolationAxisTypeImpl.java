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

import net.opengis.wcs20.InterpolationAxisType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interpolation Axis Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.InterpolationAxisTypeImpl#getAxis <em>Axis</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.InterpolationAxisTypeImpl#getInterpolationMethod <em>Interpolation Method</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterpolationAxisTypeImpl extends EObjectImpl implements InterpolationAxisType {
    /**
     * The default value of the '{@link #getAxis() <em>Axis</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAxis()
     * @generated
     * @ordered
     */
    protected static final String AXIS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAxis() <em>Axis</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAxis()
     * @generated
     * @ordered
     */
    protected String axis = AXIS_EDEFAULT;

    /**
     * The default value of the '{@link #getInterpolationMethod() <em>Interpolation Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationMethod()
     * @generated
     * @ordered
     */
    protected static final String INTERPOLATION_METHOD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getInterpolationMethod() <em>Interpolation Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationMethod()
     * @generated
     * @ordered
     */
    protected String interpolationMethod = INTERPOLATION_METHOD_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InterpolationAxisTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.INTERPOLATION_AXIS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAxis() {
        return axis;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAxis(String newAxis) {
        String oldAxis = axis;
        axis = newAxis;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.INTERPOLATION_AXIS_TYPE__AXIS, oldAxis, axis));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInterpolationMethod() {
        return interpolationMethod;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInterpolationMethod(String newInterpolationMethod) {
        String oldInterpolationMethod = interpolationMethod;
        interpolationMethod = newInterpolationMethod;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.INTERPOLATION_AXIS_TYPE__INTERPOLATION_METHOD, oldInterpolationMethod, interpolationMethod));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.INTERPOLATION_AXIS_TYPE__AXIS:
                return getAxis();
            case Wcs20Package.INTERPOLATION_AXIS_TYPE__INTERPOLATION_METHOD:
                return getInterpolationMethod();
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
            case Wcs20Package.INTERPOLATION_AXIS_TYPE__AXIS:
                setAxis((String)newValue);
                return;
            case Wcs20Package.INTERPOLATION_AXIS_TYPE__INTERPOLATION_METHOD:
                setInterpolationMethod((String)newValue);
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
            case Wcs20Package.INTERPOLATION_AXIS_TYPE__AXIS:
                setAxis(AXIS_EDEFAULT);
                return;
            case Wcs20Package.INTERPOLATION_AXIS_TYPE__INTERPOLATION_METHOD:
                setInterpolationMethod(INTERPOLATION_METHOD_EDEFAULT);
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
            case Wcs20Package.INTERPOLATION_AXIS_TYPE__AXIS:
                return AXIS_EDEFAULT == null ? axis != null : !AXIS_EDEFAULT.equals(axis);
            case Wcs20Package.INTERPOLATION_AXIS_TYPE__INTERPOLATION_METHOD:
                return INTERPOLATION_METHOD_EDEFAULT == null ? interpolationMethod != null : !INTERPOLATION_METHOD_EDEFAULT.equals(interpolationMethod);
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
        result.append(" (axis: ");
        result.append(axis);
        result.append(", interpolationMethod: ");
        result.append(interpolationMethod);
        result.append(')');
        return result.toString();
    }

} //InterpolationAxisTypeImpl
