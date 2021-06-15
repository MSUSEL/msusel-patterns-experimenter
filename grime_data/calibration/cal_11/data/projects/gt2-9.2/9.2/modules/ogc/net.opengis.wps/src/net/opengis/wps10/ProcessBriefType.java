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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Brief Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ProcessBriefType#getProfile <em>Profile</em>}</li>
 *   <li>{@link net.opengis.wps10.ProcessBriefType#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wps10.ProcessBriefType#getProcessVersion <em>Process Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getProcessBriefType()
 * @model extendedMetaData="name='ProcessBriefType' kind='elementOnly'"
 * @generated
 */
public interface ProcessBriefType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Profile</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of application profiles to which this process complies.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Profile</em>' attribute.
     * @see #setProfile(String)
     * @see net.opengis.wps10.Wps10Package#getProcessBriefType_Profile()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='Profile' namespace='##targetNamespace'"
     * @generated
     */
    String getProfile();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessBriefType#getProfile <em>Profile</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Profile</em>' attribute.
     * @see #getProfile()
     * @generated
     */
    void setProfile(String value);

    /**
     * Returns the value of the '<em><b>WSDL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Location of a WSDL document which describes this process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>WSDL</em>' containment reference.
     * @see #setWSDL(WSDLType)
     * @see net.opengis.wps10.Wps10Package#getProcessBriefType_WSDL()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='WSDL' namespace='##targetNamespace'"
     * @generated
     */
    WSDLType getWSDL();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessBriefType#getWSDL <em>WSDL</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>WSDL</em>' containment reference.
     * @see #getWSDL()
     * @generated
     */
    void setWSDL(WSDLType value);

    /**
     * Returns the value of the '<em><b>Process Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Release version of this Process, included when a process version needs to be included for clarification about the process to be used. It is possible that a WPS supports a process with different versions due to reasons such as modifications of process algorithms.  Notice that this is the version identifier for the process, not the version of the WPS interface. The processVersion is informative only.  Version negotiation for processVersion is not available.  Requests to Execute a process do not include a processVersion identifier.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Version</em>' attribute.
     * @see #setProcessVersion(String)
     * @see net.opengis.wps10.Wps10Package#getProcessBriefType_ProcessVersion()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='processVersion' namespace='##targetNamespace'"
     * @generated
     */
    String getProcessVersion();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessBriefType#getProcessVersion <em>Process Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Version</em>' attribute.
     * @see #getProcessVersion()
     * @generated
     */
    void setProcessVersion(String value);

} // ProcessBriefType
