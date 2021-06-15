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

import net.opengis.ows11.Ows11Package;
import net.opengis.ows11.TelephoneType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Telephone Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.TelephoneTypeImpl#getVoice <em>Voice</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.TelephoneTypeImpl#getFacsimile <em>Facsimile</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TelephoneTypeImpl extends EObjectImpl implements TelephoneType {
    /**
     * The cached value of the '{@link #getVoice() <em>Voice</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVoice()
     * @generated
     * @ordered
     */
    protected EList voice;

    /**
     * The cached value of the '{@link #getFacsimile() <em>Facsimile</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFacsimile()
     * @generated
     * @ordered
     */
    protected EList facsimile;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TelephoneTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.TELEPHONE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getVoice() {
        if (voice == null) {
            voice = new EDataTypeEList(String.class, this, Ows11Package.TELEPHONE_TYPE__VOICE);
        }
        return voice;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getFacsimile() {
        if (facsimile == null) {
            facsimile = new EDataTypeEList(String.class, this, Ows11Package.TELEPHONE_TYPE__FACSIMILE);
        }
        return facsimile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows11Package.TELEPHONE_TYPE__VOICE:
                return getVoice();
            case Ows11Package.TELEPHONE_TYPE__FACSIMILE:
                return getFacsimile();
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
            case Ows11Package.TELEPHONE_TYPE__VOICE:
                getVoice().clear();
                getVoice().addAll((Collection)newValue);
                return;
            case Ows11Package.TELEPHONE_TYPE__FACSIMILE:
                getFacsimile().clear();
                getFacsimile().addAll((Collection)newValue);
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
            case Ows11Package.TELEPHONE_TYPE__VOICE:
                getVoice().clear();
                return;
            case Ows11Package.TELEPHONE_TYPE__FACSIMILE:
                getFacsimile().clear();
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
            case Ows11Package.TELEPHONE_TYPE__VOICE:
                return voice != null && !voice.isEmpty();
            case Ows11Package.TELEPHONE_TYPE__FACSIMILE:
                return facsimile != null && !facsimile.isEmpty();
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
        result.append(" (voice: ");
        result.append(voice);
        result.append(", facsimile: ");
        result.append(facsimile);
        result.append(')');
        return result.toString();
    }

} //TelephoneTypeImpl
