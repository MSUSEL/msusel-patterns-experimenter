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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exception Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An Exception element describes one detected error that a server chooses to convey to the client. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ExceptionType#getExceptionText <em>Exception Text</em>}</li>
 *   <li>{@link net.opengis.ows11.ExceptionType#getExceptionCode <em>Exception Code</em>}</li>
 *   <li>{@link net.opengis.ows11.ExceptionType#getLocator <em>Locator</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getExceptionType()
 * @model extendedMetaData="name='ExceptionType' kind='elementOnly'"
 * @generated
 */
public interface ExceptionType extends EObject {
    /**
     * Returns the value of the '<em><b>Exception Text</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered sequence of text strings that describe this specific exception or error. The contents of these strings are left open to definition by each server implementation. A server is strongly encouraged to include at least one ExceptionText value, to provide more information about the detected error than provided by the exceptionCode. When included, multiple ExceptionText values shall provide hierarchical information about one detected error, with the most significant information listed first. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exception Text</em>' attribute list.
     * @see net.opengis.ows11.Ows11Package#getExceptionType_ExceptionText()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='ExceptionText' namespace='##targetNamespace'"
     * @generated
     */
    EList getExceptionText();

    /**
     * Returns the value of the '<em><b>Exception Code</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A code representing the type of this exception, which shall be selected from a set of exceptionCode values specified for the specific service operation and server. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exception Code</em>' attribute.
     * @see #setExceptionCode(String)
     * @see net.opengis.ows11.Ows11Package#getExceptionType_ExceptionCode()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='exceptionCode'"
     * @generated
     */
    String getExceptionCode();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ExceptionType#getExceptionCode <em>Exception Code</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exception Code</em>' attribute.
     * @see #getExceptionCode()
     * @generated
     */
    void setExceptionCode(String value);

    /**
     * Returns the value of the '<em><b>Locator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * When included, this locator shall indicate to the client where an exception was encountered in servicing the client's operation request. This locator should be included whenever meaningful information can be provided by the server. The contents of this locator will depend on the specific exceptionCode and OWS service, and shall be specified in the OWS Implementation Specification. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Locator</em>' attribute.
     * @see #setLocator(String)
     * @see net.opengis.ows11.Ows11Package#getExceptionType_Locator()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='locator'"
     * @generated
     */
    String getLocator();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ExceptionType#getLocator <em>Locator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Locator</em>' attribute.
     * @see #getLocator()
     * @generated
     */
    void setLocator(String value);

} // ExceptionType
