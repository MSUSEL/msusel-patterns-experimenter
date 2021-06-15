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

import net.opengis.ows11.DescriptionType;
import net.opengis.ows11.KeywordsType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.DescriptionTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DescriptionTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.DescriptionTypeImpl#getKeywords <em>Keywords</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescriptionTypeImpl extends EObjectImpl implements DescriptionType {
    /**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
    protected EList title;

    /**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
    protected EList abstract_;

    /**
     * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeywords()
     * @generated
     * @ordered
     */
    protected EList keywords;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DescriptionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.DESCRIPTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getTitle() {
        if (title == null) {
            title = new EObjectContainmentEList(LanguageStringType.class, this, Ows11Package.DESCRIPTION_TYPE__TITLE);
        }
        return title;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getAbstract() {
        if (abstract_ == null) {
            abstract_ = new EObjectContainmentEList(LanguageStringType.class, this, Ows11Package.DESCRIPTION_TYPE__ABSTRACT);
        }
        return abstract_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getKeywords() {
        if (keywords == null) {
            keywords = new EObjectContainmentEList(KeywordsType.class, this, Ows11Package.DESCRIPTION_TYPE__KEYWORDS);
        }
        return keywords;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.DESCRIPTION_TYPE__TITLE:
                return ((InternalEList)getTitle()).basicRemove(otherEnd, msgs);
            case Ows11Package.DESCRIPTION_TYPE__ABSTRACT:
                return ((InternalEList)getAbstract()).basicRemove(otherEnd, msgs);
            case Ows11Package.DESCRIPTION_TYPE__KEYWORDS:
                return ((InternalEList)getKeywords()).basicRemove(otherEnd, msgs);
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
            case Ows11Package.DESCRIPTION_TYPE__TITLE:
                return getTitle();
            case Ows11Package.DESCRIPTION_TYPE__ABSTRACT:
                return getAbstract();
            case Ows11Package.DESCRIPTION_TYPE__KEYWORDS:
                return getKeywords();
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
            case Ows11Package.DESCRIPTION_TYPE__TITLE:
                getTitle().clear();
                getTitle().addAll((Collection)newValue);
                return;
            case Ows11Package.DESCRIPTION_TYPE__ABSTRACT:
                getAbstract().clear();
                getAbstract().addAll((Collection)newValue);
                return;
            case Ows11Package.DESCRIPTION_TYPE__KEYWORDS:
                getKeywords().clear();
                getKeywords().addAll((Collection)newValue);
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
            case Ows11Package.DESCRIPTION_TYPE__TITLE:
                getTitle().clear();
                return;
            case Ows11Package.DESCRIPTION_TYPE__ABSTRACT:
                getAbstract().clear();
                return;
            case Ows11Package.DESCRIPTION_TYPE__KEYWORDS:
                getKeywords().clear();
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
            case Ows11Package.DESCRIPTION_TYPE__TITLE:
                return title != null && !title.isEmpty();
            case Ows11Package.DESCRIPTION_TYPE__ABSTRACT:
                return abstract_ != null && !abstract_.isEmpty();
            case Ows11Package.DESCRIPTION_TYPE__KEYWORDS:
                return keywords != null && !keywords.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //DescriptionTypeImpl
