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
package net.opengis.wcs10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Request Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.RequestType#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs10.RequestType#getDescribeCoverage <em>Describe Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs10.RequestType#getGetCoverage <em>Get Coverage</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getRequestType()
 * @model extendedMetaData="name='Request_._type' kind='elementOnly'"
 * @generated
 */
public interface RequestType extends EObject {
    /**
	 * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Get Capabilities</em>' containment reference.
	 * @see #setGetCapabilities(GetCapabilitiesType1)
	 * @see net.opengis.wcs10.Wcs10Package#getRequestType_GetCapabilities()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
	 * @generated
	 */
    GetCapabilitiesType1 getGetCapabilities();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.RequestType#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Capabilities</em>' containment reference.
	 * @see #getGetCapabilities()
	 * @generated
	 */
    void setGetCapabilities(GetCapabilitiesType1 value);

    /**
	 * Returns the value of the '<em><b>Describe Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Describe Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Describe Coverage</em>' containment reference.
	 * @see #setDescribeCoverage(DescribeCoverageType1)
	 * @see net.opengis.wcs10.Wcs10Package#getRequestType_DescribeCoverage()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='DescribeCoverage' namespace='##targetNamespace'"
	 * @generated
	 */
    DescribeCoverageType1 getDescribeCoverage();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.RequestType#getDescribeCoverage <em>Describe Coverage</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Describe Coverage</em>' containment reference.
	 * @see #getDescribeCoverage()
	 * @generated
	 */
    void setDescribeCoverage(DescribeCoverageType1 value);

    /**
	 * Returns the value of the '<em><b>Get Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Get Coverage</em>' containment reference.
	 * @see #setGetCoverage(GetCoverageType1)
	 * @see net.opengis.wcs10.Wcs10Package#getRequestType_GetCoverage()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='GetCoverage' namespace='##targetNamespace'"
	 * @generated
	 */
    GetCoverageType1 getGetCoverage();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.RequestType#getGetCoverage <em>Get Coverage</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Coverage</em>' containment reference.
	 * @see #getGetCoverage()
	 * @generated
	 */
    void setGetCoverage(GetCoverageType1 value);

} // RequestType
