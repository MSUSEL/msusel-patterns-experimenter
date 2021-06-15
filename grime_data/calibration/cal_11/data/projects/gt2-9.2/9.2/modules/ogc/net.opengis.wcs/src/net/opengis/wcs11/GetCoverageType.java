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
package net.opengis.wcs11;

import net.opengis.ows11.CodeType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.GetCoverageType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs11.GetCoverageType#getDomainSubset <em>Domain Subset</em>}</li>
 *   <li>{@link net.opengis.wcs11.GetCoverageType#getRangeSubset <em>Range Subset</em>}</li>
 *   <li>{@link net.opengis.wcs11.GetCoverageType#getOutput <em>Output</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs11Package#getGetCoverageType()
 * @model extendedMetaData="name='GetCoverage_._type' kind='elementOnly'"
 * @generated
 */
public interface GetCoverageType extends RequestBaseType {
    /**
	 * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Identifier of the coverage that this GetCoverage operation request shall draw from.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Identifier</em>' containment reference.
	 * @see #setIdentifier(CodeType)
	 * @see net.opengis.wcs11.Wcs11Package#getGetCoverageType_Identifier()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
	 * @generated
	 */
    CodeType getIdentifier();

    /**
	 * Sets the value of the '{@link net.opengis.wcs11.GetCoverageType#getIdentifier <em>Identifier</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Identifier</em>' containment reference.
	 * @see #getIdentifier()
	 * @generated
	 */
    void setIdentifier(CodeType value);

    /**
	 * Returns the value of the '<em><b>Domain Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Domain Subset</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Domain Subset</em>' containment reference.
	 * @see #setDomainSubset(DomainSubsetType)
	 * @see net.opengis.wcs11.Wcs11Package#getGetCoverageType_DomainSubset()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='DomainSubset' namespace='##targetNamespace'"
	 * @generated
	 */
    DomainSubsetType getDomainSubset();

    /**
	 * Sets the value of the '{@link net.opengis.wcs11.GetCoverageType#getDomainSubset <em>Domain Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Domain Subset</em>' containment reference.
	 * @see #getDomainSubset()
	 * @generated
	 */
    void setDomainSubset(DomainSubsetType value);

    /**
	 * Returns the value of the '<em><b>Range Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional selection of a subset of the coverage's range.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Range Subset</em>' containment reference.
	 * @see #setRangeSubset(RangeSubsetType)
	 * @see net.opengis.wcs11.Wcs11Package#getGetCoverageType_RangeSubset()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='RangeSubset' namespace='##targetNamespace'"
	 * @generated
	 */
    RangeSubsetType getRangeSubset();

    /**
	 * Sets the value of the '{@link net.opengis.wcs11.GetCoverageType#getRangeSubset <em>Range Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Range Subset</em>' containment reference.
	 * @see #getRangeSubset()
	 * @generated
	 */
    void setRangeSubset(RangeSubsetType value);

    /**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Output</em>' containment reference.
	 * @see #setOutput(OutputType)
	 * @see net.opengis.wcs11.Wcs11Package#getGetCoverageType_Output()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
	 * @generated
	 */
    OutputType getOutput();

    /**
	 * Sets the value of the '{@link net.opengis.wcs11.GetCoverageType#getOutput <em>Output</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output</em>' containment reference.
	 * @see #getOutput()
	 * @generated
	 */
    void setOutput(OutputType value);

} // GetCoverageType
