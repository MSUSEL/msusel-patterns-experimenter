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
package net.opengis.wps10;

import net.opengis.ows11.CodeType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Execute Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ExecuteType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps10.ExecuteType#getDataInputs <em>Data Inputs</em>}</li>
 *   <li>{@link net.opengis.wps10.ExecuteType#getResponseForm <em>Response Form</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getExecuteType()
 * @model extendedMetaData="name='Execute_._type' kind='elementOnly'"
 * @generated
 */
public interface ExecuteType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of the Process to be executed. This Process identifier shall be as listed in the ProcessOfferings section of the WPS Capabilities document.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.wps10.Wps10Package#getExecuteType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Data Inputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of input (or parameter) values provided to the process, including each of the Inputs needed to execute the process. It is possible to have no inputs provided only when all the inputs are predetermined fixed resources. In all other cases, at least one input is required.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Inputs</em>' containment reference.
     * @see #setDataInputs(DataInputsType1)
     * @see net.opengis.wps10.Wps10Package#getExecuteType_DataInputs()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DataInputs' namespace='##targetNamespace'"
     * @generated
     */
    DataInputsType1 getDataInputs();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteType#getDataInputs <em>Data Inputs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Inputs</em>' containment reference.
     * @see #getDataInputs()
     * @generated
     */
    void setDataInputs(DataInputsType1 value);

    /**
     * Returns the value of the '<em><b>Response Form</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Defines the response type of the WPS, either raw data or XML document.  If absent, the response shall be a response document which includes all outputs encoded in the response.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Response Form</em>' containment reference.
     * @see #setResponseForm(ResponseFormType)
     * @see net.opengis.wps10.Wps10Package#getExecuteType_ResponseForm()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ResponseForm' namespace='##targetNamespace'"
     * @generated
     */
    ResponseFormType getResponseForm();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteType#getResponseForm <em>Response Form</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Response Form</em>' containment reference.
     * @see #getResponseForm()
     * @generated
     */
    void setResponseForm(ResponseFormType value);

} // ExecuteType
