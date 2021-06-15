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

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;
import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Specifies a query to execute against instances of one or
 *          more object types. A set of ElementName elements may be included
 *          to specify an adhoc view of the csw:Record instances in the result
 *          set. Otherwise, use ElementSetName to specify a predefined view.
 *          The Constraint element contains a query filter expressed in a
 *          supported query language. A sorting criterion that specifies a
 *          property to sort by may be included.
 * 
 *          typeNames - a list of object types to query.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getElementSetName <em>Element Set Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getElementName <em>Element Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getSortBy <em>Sort By</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getTypeNames <em>Type Names</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getQueryType()
 * @model extendedMetaData="name='QueryType' kind='elementOnly'"
 * @generated
 */
public interface QueryType extends AbstractQueryType {
    /**
     * Returns the value of the '<em><b>Element Set Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element Set Name</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Set Name</em>' containment reference.
     * @see #setElementSetName(ElementSetNameType)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_ElementSetName()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ElementSetName' namespace='##targetNamespace'"
     * @generated
     */
    ElementSetNameType getElementSetName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryType#getElementSetName <em>Element Set Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Set Name</em>' containment reference.
     * @see #getElementSetName()
     * @generated
     */
    void setElementSetName(ElementSetNameType value);

    /**
     * Returns the value of the '<em><b>Element Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Name</em>' attribute.
     * @see #setElementName(QName)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_ElementName()
     * @model 
     */
    EList<QName> getElementName();

    /**
     * Returns the value of the '<em><b>Constraint</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Constraint</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Constraint</em>' containment reference.
     * @see #setConstraint(QueryConstraintType)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_Constraint()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Constraint' namespace='##targetNamespace'"
     * @generated
     */
    QueryConstraintType getConstraint();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryType#getConstraint <em>Constraint</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Constraint</em>' containment reference.
     * @see #getConstraint()
     * @generated
     */
    void setConstraint(QueryConstraintType value);

    /**
     * Returns the value of the '<em><b>Sort By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sort By</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sort By</em>' containment reference.
     * @see #setSortBy(SortBy)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_SortBy()
     * @model 
     */
    SortBy[] getSortBy();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryType#getSortBy <em>Sort By</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sort By</em>' attribute.
     * @see #getSortBy()
     * @generated
     */
    void setSortBy(SortBy[] value);

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
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_TypeNames()
     * @model dataType="net.opengis.cat.csw20.TypeNameListType_1" required="true"
     *        extendedMetaData="kind='attribute' name='typeNames'"
     * @generated
     */
    List<QName> getTypeNames();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryType#getTypeNames <em>Type Names</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Names</em>' attribute.
     * @see #getTypeNames()
     * @generated
     */
    void setTypeNames(List<QName> value);

} // QueryType
