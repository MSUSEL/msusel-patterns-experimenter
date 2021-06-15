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

import java.util.List;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Adhoc Query Expression Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractProjectionClause <em>Abstract Projection Clause</em>}</li>
 *   <li>{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractSelectionClause <em>Abstract Selection Clause</em>}</li>
 *   <li>{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractSortingClause <em>Abstract Sorting Clause</em>}</li>
 *   <li>{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAliases <em>Aliases</em>}</li>
 *   <li>{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getTypeNames <em>Type Names</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getAbstractAdhocQueryExpressionType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractAdhocQueryExpressionType' kind='elementOnly'"
 * @generated
 */
public interface AbstractAdhocQueryExpressionType extends AbstractQueryExpressionType {
    /**
     * Returns the value of the '<em><b>Abstract Projection Clause</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Projection Clause</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Projection Clause</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getAbstractAdhocQueryExpressionType_AbstractProjectionClause()
     * @model 
     * @generated NOT
     */
    EList<Object> getAbstractProjectionClause();

    /**
     * Returns the value of the '<em><b>Abstract Selection Clause</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Selection Clause</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Selection Clause</em>' containment reference.
     * @see net.opengis.fes20.Fes20Package#getAbstractAdhocQueryExpressionType_AbstractSelectionClause()
     * @model 
     */
    Object getAbstractSelectionClause();
    
    /**
     * Sets the value of the '{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractSelectionClause <em>Abstract Selection Clause</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract Selection Clause</em>' attribute.
     * @see #getAbstractSelectionClause()
     * @generated
     */
    void setAbstractSelectionClause(Object value);

    /**
     * Returns the value of the '<em><b>Abstract Sorting Clause</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Sorting Clause</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Sorting Clause</em>' containment reference.
     * @see net.opengis.fes20.Fes20Package#getAbstractAdhocQueryExpressionType_AbstractSortingClause()
     * @model 
     */
    Object getAbstractSortingClause();

    /**
     * Sets the value of the '{@link net.opengis.fes20.AbstractAdhocQueryExpressionType#getAbstractSortingClause <em>Abstract Sorting Clause</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract Sorting Clause</em>' attribute.
     * @see #getAbstractSortingClause()
     * @generated
     */
    void setAbstractSortingClause(Object value);

    /**
     * Returns the value of the '<em><b>Aliases</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Aliases</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Aliases</em>' attribute.
     * @see #setAliases(List)
     * @see net.opengis.fes20.Fes20Package#getAbstractAdhocQueryExpressionType_Aliases()
     * @model
     * @generated NOT
     */
    EList<String> getAliases();

    /**
     * Returns the value of the '<em><b>Type Names</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type Names</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type Names</em>' attribute.
     * @see #setTypeNames(List)
     * @see net.opengis.fes20.Fes20Package#getAbstractAdhocQueryExpressionType_TypeNames()
     * @model
     * @generated NOT
     */
    EList<Object> getTypeNames();

} // AbstractAdhocQueryExpressionType
