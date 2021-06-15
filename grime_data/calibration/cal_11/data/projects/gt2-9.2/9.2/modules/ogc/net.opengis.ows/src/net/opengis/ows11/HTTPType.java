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
package net.opengis.ows11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>HTTP Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.HTTPType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.ows11.HTTPType#getGet <em>Get</em>}</li>
 *   <li>{@link net.opengis.ows11.HTTPType#getPost <em>Post</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getHTTPType()
 * @model extendedMetaData="name='HTTP_._type' kind='elementOnly'"
 * @generated
 */
public interface HTTPType extends EObject {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see net.opengis.ows11.Ows11Package#getHTTPType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Get</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.RequestMethodType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Connect point URL prefix and any constraints for the HTTP "Get" request method for this operation request. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getHTTPType_Get()
     * @model type="net.opengis.ows11.RequestMethodType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Get' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList getGet();

    /**
     * Returns the value of the '<em><b>Post</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.RequestMethodType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Connect point URL and any constraints for the HTTP "Post" request method for this operation request. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Post</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getHTTPType_Post()
     * @model type="net.opengis.ows11.RequestMethodType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Post' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList getPost();

} // HTTPType
