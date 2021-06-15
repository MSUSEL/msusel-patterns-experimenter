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
package net.opengis.fes20.impl;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.SortOrderType;
import net.opengis.fes20.SortPropertyType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sort Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.SortPropertyTypeImpl#getValueReference <em>Value Reference</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.SortPropertyTypeImpl#getSortOrder <em>Sort Order</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SortPropertyTypeImpl extends EObjectImpl implements SortPropertyType {
    /**
     * The default value of the '{@link #getValueReference() <em>Value Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueReference()
     * @generated
     * @ordered
     */
    protected static final String VALUE_REFERENCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValueReference() <em>Value Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueReference()
     * @generated
     * @ordered
     */
    protected String valueReference = VALUE_REFERENCE_EDEFAULT;

    /**
     * The default value of the '{@link #getSortOrder() <em>Sort Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSortOrder()
     * @generated
     * @ordered
     */
    protected static final SortOrderType SORT_ORDER_EDEFAULT = SortOrderType.DESC;

    /**
     * The cached value of the '{@link #getSortOrder() <em>Sort Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSortOrder()
     * @generated
     * @ordered
     */
    protected SortOrderType sortOrder = SORT_ORDER_EDEFAULT;

    /**
     * This is true if the Sort Order attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean sortOrderESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SortPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.SORT_PROPERTY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValueReference() {
        return valueReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValueReference(String newValueReference) {
        String oldValueReference = valueReference;
        valueReference = newValueReference;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.SORT_PROPERTY_TYPE__VALUE_REFERENCE, oldValueReference, valueReference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SortOrderType getSortOrder() {
        return sortOrder;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSortOrder(SortOrderType newSortOrder) {
        SortOrderType oldSortOrder = sortOrder;
        sortOrder = newSortOrder == null ? SORT_ORDER_EDEFAULT : newSortOrder;
        boolean oldSortOrderESet = sortOrderESet;
        sortOrderESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.SORT_PROPERTY_TYPE__SORT_ORDER, oldSortOrder, sortOrder, !oldSortOrderESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetSortOrder() {
        SortOrderType oldSortOrder = sortOrder;
        boolean oldSortOrderESet = sortOrderESet;
        sortOrder = SORT_ORDER_EDEFAULT;
        sortOrderESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Fes20Package.SORT_PROPERTY_TYPE__SORT_ORDER, oldSortOrder, SORT_ORDER_EDEFAULT, oldSortOrderESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetSortOrder() {
        return sortOrderESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Fes20Package.SORT_PROPERTY_TYPE__VALUE_REFERENCE:
                return getValueReference();
            case Fes20Package.SORT_PROPERTY_TYPE__SORT_ORDER:
                return getSortOrder();
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
            case Fes20Package.SORT_PROPERTY_TYPE__VALUE_REFERENCE:
                setValueReference((String)newValue);
                return;
            case Fes20Package.SORT_PROPERTY_TYPE__SORT_ORDER:
                setSortOrder((SortOrderType)newValue);
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
            case Fes20Package.SORT_PROPERTY_TYPE__VALUE_REFERENCE:
                setValueReference(VALUE_REFERENCE_EDEFAULT);
                return;
            case Fes20Package.SORT_PROPERTY_TYPE__SORT_ORDER:
                unsetSortOrder();
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
            case Fes20Package.SORT_PROPERTY_TYPE__VALUE_REFERENCE:
                return VALUE_REFERENCE_EDEFAULT == null ? valueReference != null : !VALUE_REFERENCE_EDEFAULT.equals(valueReference);
            case Fes20Package.SORT_PROPERTY_TYPE__SORT_ORDER:
                return isSetSortOrder();
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
        result.append(" (valueReference: ");
        result.append(valueReference);
        result.append(", sortOrder: ");
        if (sortOrderESet) result.append(sortOrder); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //SortPropertyTypeImpl
