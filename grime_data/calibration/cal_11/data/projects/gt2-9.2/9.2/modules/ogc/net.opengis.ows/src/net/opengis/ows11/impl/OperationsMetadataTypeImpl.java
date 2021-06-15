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
package net.opengis.ows11.impl;

import java.util.Collection;

import net.opengis.ows11.DomainType;
import net.opengis.ows11.OperationType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operations Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.OperationsMetadataTypeImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.OperationsMetadataTypeImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.OperationsMetadataTypeImpl#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.OperationsMetadataTypeImpl#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationsMetadataTypeImpl extends EObjectImpl implements OperationsMetadataType {
    /**
     * The cached value of the '{@link #getOperation() <em>Operation</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperation()
     * @generated
     * @ordered
     */
    protected EList operation;

    /**
     * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameter()
     * @generated
     * @ordered
     */
    protected EList parameter;

    /**
     * The cached value of the '{@link #getConstraint() <em>Constraint</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConstraint()
     * @generated
     * @ordered
     */
    protected EList constraint;

    /**
     * The default value of the '{@link #getExtendedCapabilities() <em>Extended Capabilities</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExtendedCapabilities()
     * @generated
     * @ordered
     */
    protected static final Object EXTENDED_CAPABILITIES_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getExtendedCapabilities() <em>Extended Capabilities</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExtendedCapabilities()
     * @generated
     * @ordered
     */
    protected Object extendedCapabilities = EXTENDED_CAPABILITIES_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OperationsMetadataTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.OPERATIONS_METADATA_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getOperation() {
        if (operation == null) {
            operation = new EObjectContainmentEList(OperationType.class, this, Ows11Package.OPERATIONS_METADATA_TYPE__OPERATION);
        }
        return operation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getParameter() {
        if (parameter == null) {
            parameter = new EObjectContainmentEList(DomainType.class, this, Ows11Package.OPERATIONS_METADATA_TYPE__PARAMETER);
        }
        return parameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getConstraint() {
        if (constraint == null) {
            constraint = new EObjectContainmentEList(DomainType.class, this, Ows11Package.OPERATIONS_METADATA_TYPE__CONSTRAINT);
        }
        return constraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getExtendedCapabilities() {
        return extendedCapabilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtendedCapabilities(Object newExtendedCapabilities) {
        Object oldExtendedCapabilities = extendedCapabilities;
        extendedCapabilities = newExtendedCapabilities;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES, oldExtendedCapabilities, extendedCapabilities));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.OPERATIONS_METADATA_TYPE__OPERATION:
                return ((InternalEList)getOperation()).basicRemove(otherEnd, msgs);
            case Ows11Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                return ((InternalEList)getParameter()).basicRemove(otherEnd, msgs);
            case Ows11Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                return ((InternalEList)getConstraint()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows11Package.OPERATIONS_METADATA_TYPE__OPERATION:
                return getOperation();
            case Ows11Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                return getParameter();
            case Ows11Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                return getConstraint();
            case Ows11Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
                return getExtendedCapabilities();
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
            case Ows11Package.OPERATIONS_METADATA_TYPE__OPERATION:
                getOperation().clear();
                getOperation().addAll((Collection)newValue);
                return;
            case Ows11Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                getParameter().clear();
                getParameter().addAll((Collection)newValue);
                return;
            case Ows11Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                getConstraint().clear();
                getConstraint().addAll((Collection)newValue);
                return;
            case Ows11Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
                setExtendedCapabilities(newValue);
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
            case Ows11Package.OPERATIONS_METADATA_TYPE__OPERATION:
                getOperation().clear();
                return;
            case Ows11Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                getParameter().clear();
                return;
            case Ows11Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                getConstraint().clear();
                return;
            case Ows11Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
                setExtendedCapabilities(EXTENDED_CAPABILITIES_EDEFAULT);
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
            case Ows11Package.OPERATIONS_METADATA_TYPE__OPERATION:
                return operation != null && !operation.isEmpty();
            case Ows11Package.OPERATIONS_METADATA_TYPE__PARAMETER:
                return parameter != null && !parameter.isEmpty();
            case Ows11Package.OPERATIONS_METADATA_TYPE__CONSTRAINT:
                return constraint != null && !constraint.isEmpty();
            case Ows11Package.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
                return EXTENDED_CAPABILITIES_EDEFAULT == null ? extendedCapabilities != null : !EXTENDED_CAPABILITIES_EDEFAULT.equals(extendedCapabilities);
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
        result.append(" (extendedCapabilities: ");
        result.append(extendedCapabilities);
        result.append(')');
        return result.toString();
    }

} //OperationsMetadataTypeImpl
