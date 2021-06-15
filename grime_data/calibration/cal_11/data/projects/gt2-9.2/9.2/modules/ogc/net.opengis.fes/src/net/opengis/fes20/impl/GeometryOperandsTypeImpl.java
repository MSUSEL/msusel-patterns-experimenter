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

import java.util.Collection;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.GeometryOperandType;
import net.opengis.fes20.GeometryOperandsType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geometry Operands Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.GeometryOperandsTypeImpl#getGeometryOperand <em>Geometry Operand</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GeometryOperandsTypeImpl extends EObjectImpl implements GeometryOperandsType {
    /**
     * The cached value of the '{@link #getGeometryOperand() <em>Geometry Operand</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometryOperand()
     * @generated
     * @ordered
     */
    protected EList<GeometryOperandType> geometryOperand;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GeometryOperandsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.GEOMETRY_OPERANDS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<GeometryOperandType> getGeometryOperand() {
        if (geometryOperand == null) {
            geometryOperand = new EObjectContainmentEList<GeometryOperandType>(GeometryOperandType.class, this, Fes20Package.GEOMETRY_OPERANDS_TYPE__GEOMETRY_OPERAND);
        }
        return geometryOperand;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.GEOMETRY_OPERANDS_TYPE__GEOMETRY_OPERAND:
                return ((InternalEList<?>)getGeometryOperand()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Fes20Package.GEOMETRY_OPERANDS_TYPE__GEOMETRY_OPERAND:
                return getGeometryOperand();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Fes20Package.GEOMETRY_OPERANDS_TYPE__GEOMETRY_OPERAND:
                getGeometryOperand().clear();
                getGeometryOperand().addAll((Collection<? extends GeometryOperandType>)newValue);
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
            case Fes20Package.GEOMETRY_OPERANDS_TYPE__GEOMETRY_OPERAND:
                getGeometryOperand().clear();
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
            case Fes20Package.GEOMETRY_OPERANDS_TYPE__GEOMETRY_OPERAND:
                return geometryOperand != null && !geometryOperand.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //GeometryOperandsTypeImpl
