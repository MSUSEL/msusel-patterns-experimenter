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
package net.opengis.fes20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scalar Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.ScalarCapabilitiesType#getLogicalOperators <em>Logical Operators</em>}</li>
 *   <li>{@link net.opengis.fes20.ScalarCapabilitiesType#getComparisonOperators <em>Comparison Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getScalarCapabilitiesType()
 * @model extendedMetaData="name='Scalar_CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface ScalarCapabilitiesType extends EObject {
    /**
     * Returns the value of the '<em><b>Logical Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Logical Operators</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Logical Operators</em>' containment reference.
     * @see #setLogicalOperators(LogicalOperatorsType)
     * @see net.opengis.fes20.Fes20Package#getScalarCapabilitiesType_LogicalOperators()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='LogicalOperators' namespace='##targetNamespace'"
     * @generated
     */
    LogicalOperatorsType getLogicalOperators();

    /**
     * Sets the value of the '{@link net.opengis.fes20.ScalarCapabilitiesType#getLogicalOperators <em>Logical Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Logical Operators</em>' containment reference.
     * @see #getLogicalOperators()
     * @generated
     */
    void setLogicalOperators(LogicalOperatorsType value);

    /**
     * Returns the value of the '<em><b>Comparison Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Comparison Operators</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Comparison Operators</em>' containment reference.
     * @see #setComparisonOperators(ComparisonOperatorsType)
     * @see net.opengis.fes20.Fes20Package#getScalarCapabilitiesType_ComparisonOperators()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ComparisonOperators' namespace='##targetNamespace'"
     * @generated
     */
    ComparisonOperatorsType getComparisonOperators();

    /**
     * Sets the value of the '{@link net.opengis.fes20.ScalarCapabilitiesType#getComparisonOperators <em>Comparison Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Comparison Operators</em>' containment reference.
     * @see #getComparisonOperators()
     * @generated
     */
    void setComparisonOperators(ComparisonOperatorsType value);

} // ScalarCapabilitiesType
