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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binary Comparison Op Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.BinaryComparisonOpType#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryComparisonOpType#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryComparisonOpType#getMatchAction <em>Match Action</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryComparisonOpType#isMatchCase <em>Match Case</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getBinaryComparisonOpType()
 * @model extendedMetaData="name='BinaryComparisonOpType' kind='elementOnly'"
 * @generated
 */
public interface BinaryComparisonOpType extends ComparisonOpsType {
    /**
     * Returns the value of the '<em><b>Expression Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getBinaryComparisonOpType_ExpressionGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" lower="2" upper="2"
     *        extendedMetaData="kind='group' name='expression:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getExpressionGroup();

    /**
     * Returns the value of the '<em><b>Expression</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getBinaryComparisonOpType_Expression()
     * @model containment="true" lower="2" upper="2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='expression' namespace='##targetNamespace' group='expression:group'"
     * @generated
     */
    EList<EObject> getExpression();

    /**
     * Returns the value of the '<em><b>Match Action</b></em>' attribute.
     * The default value is <code>"Any"</code>.
     * The literals are from the enumeration {@link net.opengis.fes20.MatchActionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Match Action</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Match Action</em>' attribute.
     * @see net.opengis.fes20.MatchActionType
     * @see #isSetMatchAction()
     * @see #unsetMatchAction()
     * @see #setMatchAction(MatchActionType)
     * @see net.opengis.fes20.Fes20Package#getBinaryComparisonOpType_MatchAction()
     * @model default="Any" unsettable="true"
     *        extendedMetaData="kind='attribute' name='matchAction'"
     * @generated
     */
    MatchActionType getMatchAction();

    /**
     * Sets the value of the '{@link net.opengis.fes20.BinaryComparisonOpType#getMatchAction <em>Match Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Match Action</em>' attribute.
     * @see net.opengis.fes20.MatchActionType
     * @see #isSetMatchAction()
     * @see #unsetMatchAction()
     * @see #getMatchAction()
     * @generated
     */
    void setMatchAction(MatchActionType value);

    /**
     * Unsets the value of the '{@link net.opengis.fes20.BinaryComparisonOpType#getMatchAction <em>Match Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetMatchAction()
     * @see #getMatchAction()
     * @see #setMatchAction(MatchActionType)
     * @generated
     */
    void unsetMatchAction();

    /**
     * Returns whether the value of the '{@link net.opengis.fes20.BinaryComparisonOpType#getMatchAction <em>Match Action</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Match Action</em>' attribute is set.
     * @see #unsetMatchAction()
     * @see #getMatchAction()
     * @see #setMatchAction(MatchActionType)
     * @generated
     */
    boolean isSetMatchAction();

    /**
     * Returns the value of the '<em><b>Match Case</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Match Case</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Match Case</em>' attribute.
     * @see #isSetMatchCase()
     * @see #unsetMatchCase()
     * @see #setMatchCase(boolean)
     * @see net.opengis.fes20.Fes20Package#getBinaryComparisonOpType_MatchCase()
     * @model default="true" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='matchCase'"
     * @generated
     */
    boolean isMatchCase();

    /**
     * Sets the value of the '{@link net.opengis.fes20.BinaryComparisonOpType#isMatchCase <em>Match Case</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Match Case</em>' attribute.
     * @see #isSetMatchCase()
     * @see #unsetMatchCase()
     * @see #isMatchCase()
     * @generated
     */
    void setMatchCase(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.fes20.BinaryComparisonOpType#isMatchCase <em>Match Case</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetMatchCase()
     * @see #isMatchCase()
     * @see #setMatchCase(boolean)
     * @generated
     */
    void unsetMatchCase();

    /**
     * Returns whether the value of the '{@link net.opengis.fes20.BinaryComparisonOpType#isMatchCase <em>Match Case</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Match Case</em>' attribute is set.
     * @see #unsetMatchCase()
     * @see #isMatchCase()
     * @see #setMatchCase(boolean)
     * @generated
     */
    boolean isSetMatchCase();

} // BinaryComparisonOpType
