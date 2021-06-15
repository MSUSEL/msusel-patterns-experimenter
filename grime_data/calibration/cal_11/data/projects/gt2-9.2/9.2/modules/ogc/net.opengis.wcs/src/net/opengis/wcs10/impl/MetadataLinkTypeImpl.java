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
package net.opengis.wcs10.impl;

import net.opengis.wcs10.MetadataLinkType;
import net.opengis.wcs10.MetadataTypeType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metadata Link Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.MetadataLinkTypeImpl#getMetadataType <em>Metadata Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MetadataLinkTypeImpl extends MetadataAssociationTypeImpl implements MetadataLinkType {
    /**
	 * The default value of the '{@link #getMetadataType() <em>Metadata Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMetadataType()
	 * @generated
	 * @ordered
	 */
    protected static final MetadataTypeType METADATA_TYPE_EDEFAULT = MetadataTypeType.TC211_LITERAL;

    /**
	 * The cached value of the '{@link #getMetadataType() <em>Metadata Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMetadataType()
	 * @generated
	 * @ordered
	 */
    protected MetadataTypeType metadataType = METADATA_TYPE_EDEFAULT;

    /**
	 * This is true if the Metadata Type attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean metadataTypeESet;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected MetadataLinkTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.METADATA_LINK_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public MetadataTypeType getMetadataType() {
		return metadataType;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMetadataType(MetadataTypeType newMetadataType) {
		MetadataTypeType oldMetadataType = metadataType;
		metadataType = newMetadataType == null ? METADATA_TYPE_EDEFAULT : newMetadataType;
		boolean oldMetadataTypeESet = metadataTypeESet;
		metadataTypeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.METADATA_LINK_TYPE__METADATA_TYPE, oldMetadataType, metadataType, !oldMetadataTypeESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetMetadataType() {
		MetadataTypeType oldMetadataType = metadataType;
		boolean oldMetadataTypeESet = metadataTypeESet;
		metadataType = METADATA_TYPE_EDEFAULT;
		metadataTypeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.METADATA_LINK_TYPE__METADATA_TYPE, oldMetadataType, METADATA_TYPE_EDEFAULT, oldMetadataTypeESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetMetadataType() {
		return metadataTypeESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wcs10Package.METADATA_LINK_TYPE__METADATA_TYPE:
				return getMetadataType();
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
			case Wcs10Package.METADATA_LINK_TYPE__METADATA_TYPE:
				setMetadataType((MetadataTypeType)newValue);
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
			case Wcs10Package.METADATA_LINK_TYPE__METADATA_TYPE:
				unsetMetadataType();
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
			case Wcs10Package.METADATA_LINK_TYPE__METADATA_TYPE:
				return isSetMetadataType();
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
		result.append(" (metadataType: ");
		if (metadataTypeESet) result.append(metadataType); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //MetadataLinkTypeImpl
