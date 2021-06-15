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
package net.opengis.cat.csw20;

import java.lang.String;

import org.eclipse.emf.ecore.EObject;

import org.opengis.filter.Filter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query Constraint Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A search constraint that adheres to one of the following syntaxes:
 *          Filter   - OGC filter expression
 *          CqlText  - OGC CQL predicate
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.QueryConstraintType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryConstraintType#getCqlText <em>Cql Text</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryConstraintType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getQueryConstraintType()
 * @model extendedMetaData="name='QueryConstraintType' kind='elementOnly'"
 * @generated
 */
public interface QueryConstraintType extends EObject {
    /**
     * Returns the value of the '<em><b>Filter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Filter</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Filter</em>' containment reference.
     * @see #setFilter(Filter)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryConstraintType_Filter()
     * @model type="net.opengis.cat.csw20.Filter" containment="true"
     *        extendedMetaData="kind='element' name='Filter' namespace='http://www.opengis.net/ogc'"
     * @generated
     */
    Filter getFilter();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryConstraintType#getFilter <em>Filter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter</em>' containment reference.
     * @see #getFilter()
     * @generated
     */
    void setFilter(Filter value);

    /**
     * Returns the value of the '<em><b>Cql Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cql Text</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cql Text</em>' attribute.
     * @see #setCqlText(String)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryConstraintType_CqlText()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='CqlText' namespace='##targetNamespace'"
     * @generated
     */
    String getCqlText();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryConstraintType#getCqlText <em>Cql Text</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cql Text</em>' attribute.
     * @see #getCqlText()
     * @generated
     */
    void setCqlText(String value);

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Query language version
     * <!-- end-model-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryConstraintType_Version()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryConstraintType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

} // QueryConstraintType
