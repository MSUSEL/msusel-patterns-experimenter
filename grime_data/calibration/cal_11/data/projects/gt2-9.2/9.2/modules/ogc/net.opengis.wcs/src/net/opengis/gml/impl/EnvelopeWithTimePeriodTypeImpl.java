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

import java.util.Collection;

import net.opengis.gml.EnvelopeWithTimePeriodType;
import net.opengis.gml.GmlPackage;

import net.opengis.gml.TimePositionType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Envelope With Time Period Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.gml.impl.EnvelopeWithTimePeriodTypeImpl#getTimePosition <em>Time Position</em>}</li>
 *   <li>{@link net.opengis.gml.impl.EnvelopeWithTimePeriodTypeImpl#getFrame <em>Frame</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EnvelopeWithTimePeriodTypeImpl extends EnvelopeTypeImpl implements EnvelopeWithTimePeriodType {
    /**
	 * The cached value of the '{@link #getTimePosition() <em>Time Position</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getTimePosition()
	 * @generated
	 * @ordered
	 */
    protected EList timePosition;

    /**
	 * The default value of the '{@link #getFrame() <em>Frame</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFrame()
	 * @generated
	 * @ordered
	 */
    protected static final String FRAME_EDEFAULT = "#ISO-8601";

    /**
	 * The cached value of the '{@link #getFrame() <em>Frame</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFrame()
	 * @generated
	 * @ordered
	 */
    protected String frame = FRAME_EDEFAULT;

    /**
	 * This is true if the Frame attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean frameESet;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EnvelopeWithTimePeriodTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return GmlPackage.Literals.ENVELOPE_WITH_TIME_PERIOD_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getTimePosition() {
		if (timePosition == null) {
			timePosition = new EObjectContainmentEList(TimePositionType.class, this, GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION);
		}
		return timePosition;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getFrame() {
		return frame;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFrame(String newFrame) {
		String oldFrame = frame;
		frame = newFrame;
		boolean oldFrameESet = frameESet;
		frameESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME, oldFrame, frame, !oldFrameESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetFrame() {
		String oldFrame = frame;
		boolean oldFrameESet = frameESet;
		frame = FRAME_EDEFAULT;
		frameESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME, oldFrame, FRAME_EDEFAULT, oldFrameESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetFrame() {
		return frameESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
				return ((InternalEList)getTimePosition()).basicRemove(otherEnd, msgs);
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
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
				return getTimePosition();
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME:
				return getFrame();
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
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
				getTimePosition().clear();
				getTimePosition().addAll((Collection)newValue);
				return;
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME:
				setFrame((String)newValue);
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
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
				getTimePosition().clear();
				return;
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME:
				unsetFrame();
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
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
				return timePosition != null && !timePosition.isEmpty();
			case GmlPackage.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME:
				return isSetFrame();
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
		result.append(" (frame: ");
		if (frameESet) result.append(frame); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //EnvelopeWithTimePeriodTypeImpl
