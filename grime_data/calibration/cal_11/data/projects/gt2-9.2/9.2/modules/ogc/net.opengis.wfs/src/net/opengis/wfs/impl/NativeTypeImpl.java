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
package net.opengis.wfs.impl;

import net.opengis.wfs.NativeType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Native Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.NativeTypeImpl#isSafeToIgnore <em>Safe To Ignore</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.NativeTypeImpl#getVendorId <em>Vendor Id</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.NativeTypeImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NativeTypeImpl extends EObjectImpl implements NativeType {
	/**
     * The default value of the '{@link #isSafeToIgnore() <em>Safe To Ignore</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSafeToIgnore()
     * @generated
     * @ordered
     */
	protected static final boolean SAFE_TO_IGNORE_EDEFAULT = false;

	/**
     * The cached value of the '{@link #isSafeToIgnore() <em>Safe To Ignore</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSafeToIgnore()
     * @generated
     * @ordered
     */
	protected boolean safeToIgnore = SAFE_TO_IGNORE_EDEFAULT;

	/**
     * This is true if the Safe To Ignore attribute has been set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	protected boolean safeToIgnoreESet;

	/**
     * The default value of the '{@link #getVendorId() <em>Vendor Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getVendorId()
     * @generated
     * @ordered
     */
	protected static final String VENDOR_ID_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getVendorId() <em>Vendor Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getVendorId()
     * @generated
     * @ordered
     */
	protected String vendorId = VENDOR_ID_EDEFAULT;

	/**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected NativeTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.NATIVE_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean isSafeToIgnore() {
        return safeToIgnore;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setSafeToIgnore(boolean newSafeToIgnore) {
        boolean oldSafeToIgnore = safeToIgnore;
        safeToIgnore = newSafeToIgnore;
        boolean oldSafeToIgnoreESet = safeToIgnoreESet;
        safeToIgnoreESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE, oldSafeToIgnore, safeToIgnore, !oldSafeToIgnoreESet));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void unsetSafeToIgnore() {
        boolean oldSafeToIgnore = safeToIgnore;
        boolean oldSafeToIgnoreESet = safeToIgnoreESet;
        safeToIgnore = SAFE_TO_IGNORE_EDEFAULT;
        safeToIgnoreESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE, oldSafeToIgnore, SAFE_TO_IGNORE_EDEFAULT, oldSafeToIgnoreESet));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean isSetSafeToIgnore() {
        return safeToIgnoreESet;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getVendorId() {
        return vendorId;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setVendorId(String newVendorId) {
        String oldVendorId = vendorId;
        vendorId = newVendorId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.NATIVE_TYPE__VENDOR_ID, oldVendorId, vendorId));
    }

	/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.NATIVE_TYPE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE:
                return isSafeToIgnore() ? Boolean.TRUE : Boolean.FALSE;
            case WfsPackage.NATIVE_TYPE__VENDOR_ID:
                return getVendorId();
            case WfsPackage.NATIVE_TYPE__VALUE:
                return getValue();
        }
        return super.eGet(featureID, resolve, coreType);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE:
                setSafeToIgnore(((Boolean)newValue).booleanValue());
                return;
            case WfsPackage.NATIVE_TYPE__VENDOR_ID:
                setVendorId((String)newValue);
                return;
            case WfsPackage.NATIVE_TYPE__VALUE:
                setValue((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eUnset(int featureID) {
        switch (featureID) {
            case WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE:
                unsetSafeToIgnore();
                return;
            case WfsPackage.NATIVE_TYPE__VENDOR_ID:
                setVendorId(VENDOR_ID_EDEFAULT);
                return;
            case WfsPackage.NATIVE_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean eIsSet(int featureID) {
        switch (featureID) {
            case WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE:
                return isSetSafeToIgnore();
            case WfsPackage.NATIVE_TYPE__VENDOR_ID:
                return VENDOR_ID_EDEFAULT == null ? vendorId != null : !VENDOR_ID_EDEFAULT.equals(vendorId);
            case WfsPackage.NATIVE_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
        }
        return super.eIsSet(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (safeToIgnore: ");
        if (safeToIgnoreESet) result.append(safeToIgnore); else result.append("<unset>");
        result.append(", vendorId: ");
        result.append(vendorId);
        result.append(", value: ");
        result.append(value);
        result.append(')');
        return result.toString();
    }

} //NativeTypeImpl