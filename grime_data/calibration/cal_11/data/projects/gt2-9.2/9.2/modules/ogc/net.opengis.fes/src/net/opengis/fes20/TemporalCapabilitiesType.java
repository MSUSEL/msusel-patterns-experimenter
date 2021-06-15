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
 * A representation of the model object '<em><b>Temporal Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperands <em>Temporal Operands</em>}</li>
 *   <li>{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperators <em>Temporal Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getTemporalCapabilitiesType()
 * @model extendedMetaData="name='Temporal_CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface TemporalCapabilitiesType extends EObject {
    /**
     * Returns the value of the '<em><b>Temporal Operands</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Operands</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Operands</em>' containment reference.
     * @see #setTemporalOperands(TemporalOperandsType)
     * @see net.opengis.fes20.Fes20Package#getTemporalCapabilitiesType_TemporalOperands()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TemporalOperands' namespace='##targetNamespace'"
     * @generated
     */
    TemporalOperandsType getTemporalOperands();

    /**
     * Sets the value of the '{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperands <em>Temporal Operands</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Operands</em>' containment reference.
     * @see #getTemporalOperands()
     * @generated
     */
    void setTemporalOperands(TemporalOperandsType value);

    /**
     * Returns the value of the '<em><b>Temporal Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Operators</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Operators</em>' containment reference.
     * @see #setTemporalOperators(TemporalOperatorsType)
     * @see net.opengis.fes20.Fes20Package#getTemporalCapabilitiesType_TemporalOperators()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TemporalOperators' namespace='##targetNamespace'"
     * @generated
     */
    TemporalOperatorsType getTemporalOperators();

    /**
     * Sets the value of the '{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperators <em>Temporal Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Operators</em>' containment reference.
     * @see #getTemporalOperators()
     * @generated
     */
    void setTemporalOperators(TemporalOperatorsType value);

} // TemporalCapabilitiesType
