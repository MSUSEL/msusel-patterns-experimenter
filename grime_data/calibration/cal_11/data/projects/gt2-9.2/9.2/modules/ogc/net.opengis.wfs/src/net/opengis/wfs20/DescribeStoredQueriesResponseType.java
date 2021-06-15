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
package net.opengis.wfs20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Describe Stored Queries Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.DescribeStoredQueriesResponseType#getStoredQueryDescription <em>Stored Query Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getDescribeStoredQueriesResponseType()
 * @model extendedMetaData="name='DescribeStoredQueriesResponseType' kind='elementOnly'"
 * @generated
 */
public interface DescribeStoredQueriesResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Stored Query Description</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.StoredQueryDescriptionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Stored Query Description</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Stored Query Description</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getDescribeStoredQueriesResponseType_StoredQueryDescription()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='StoredQueryDescription' namespace='##targetNamespace'"
     * @generated
     */
    EList<StoredQueryDescriptionType> getStoredQueryDescription();

} // DescribeStoredQueriesResponseType
