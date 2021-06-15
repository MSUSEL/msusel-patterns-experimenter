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
package org.geotools.data.efeature.tests.impl;

import com.vividsolutions.jts.geom.Geometry;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.geotools.data.efeature.tests.EFeatureCompatibleData;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EFeature Compatible Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.geotools.data.efeature.tests.impl.EFeatureCompatibleDataImpl#getID <em>ID</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.impl.EFeatureCompatibleDataImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.impl.EFeatureCompatibleDataImpl#getGeometry <em>Geometry</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.impl.EFeatureCompatibleDataImpl#getSRID <em>SRID</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.impl.EFeatureCompatibleDataImpl#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class EFeatureCompatibleDataImpl<A, G extends Geometry> extends EObjectImpl implements EFeatureCompatibleData<A, G> {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EFeatureCompatibleDataImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected int eStaticFeatureCount() {
        return 0;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public A getAttribute() {
        return (A)eGet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__ATTRIBUTE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAttribute(A newAttribute) {
        eSet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__ATTRIBUTE, newAttribute);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public G getGeometry() {
        return (G)eGet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__GEOMETRY, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setGeometry(G newGeometry) {
        eSet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__GEOMETRY, newGeometry);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getSRID() {
        return (String)eGet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__SRID, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSRID(String newSRID) {
        eSet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__SRID, newSRID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getDefault() {
        return (String)eGet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__DEFAULT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDefault(String newDefault) {
        eSet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__DEFAULT, newDefault);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getID() {
        return (String)eGet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__ID, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setID(String newID) {
        eSet(EFeatureTestsPackage.Literals.EFEATURE_COMPATIBLE_DATA__ID, newID);
    }

} //EFeatureCompatibleDataImpl
