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
package net.opengis.gml.impl;

import net.opengis.gml.GmlPackage;

import net.opengis.gml.GridEnvelopeType;
import net.opengis.gml.GridLimitsType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grid Limits Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.gml.impl.GridLimitsTypeImpl#getGridEnvelope <em>Grid Envelope</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GridLimitsTypeImpl extends EObjectImpl implements GridLimitsType {
    /**
	 * The cached value of the '{@link #getGridEnvelope() <em>Grid Envelope</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getGridEnvelope()
	 * @generated
	 * @ordered
	 */
    protected GridEnvelopeType gridEnvelope;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected GridLimitsTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return GmlPackage.Literals.GRID_LIMITS_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public GridEnvelopeType getGridEnvelope() {
		return gridEnvelope;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetGridEnvelope(GridEnvelopeType newGridEnvelope, NotificationChain msgs) {
		GridEnvelopeType oldGridEnvelope = gridEnvelope;
		gridEnvelope = newGridEnvelope;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GmlPackage.GRID_LIMITS_TYPE__GRID_ENVELOPE, oldGridEnvelope, newGridEnvelope);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setGridEnvelope(GridEnvelopeType newGridEnvelope) {
		if (newGridEnvelope != gridEnvelope) {
			NotificationChain msgs = null;
			if (gridEnvelope != null)
				msgs = ((InternalEObject)gridEnvelope).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GmlPackage.GRID_LIMITS_TYPE__GRID_ENVELOPE, null, msgs);
			if (newGridEnvelope != null)
				msgs = ((InternalEObject)newGridEnvelope).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GmlPackage.GRID_LIMITS_TYPE__GRID_ENVELOPE, null, msgs);
			msgs = basicSetGridEnvelope(newGridEnvelope, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.GRID_LIMITS_TYPE__GRID_ENVELOPE, newGridEnvelope, newGridEnvelope));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GmlPackage.GRID_LIMITS_TYPE__GRID_ENVELOPE:
				return basicSetGridEnvelope(null, msgs);
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
			case GmlPackage.GRID_LIMITS_TYPE__GRID_ENVELOPE:
				return getGridEnvelope();
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
			case GmlPackage.GRID_LIMITS_TYPE__GRID_ENVELOPE:
				setGridEnvelope((GridEnvelopeType)newValue);
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
			case GmlPackage.GRID_LIMITS_TYPE__GRID_ENVELOPE:
				setGridEnvelope((GridEnvelopeType)null);
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
			case GmlPackage.GRID_LIMITS_TYPE__GRID_ENVELOPE:
				return gridEnvelope != null;
		}
		return super.eIsSet(featureID);
	}

} //GridLimitsTypeImpl
