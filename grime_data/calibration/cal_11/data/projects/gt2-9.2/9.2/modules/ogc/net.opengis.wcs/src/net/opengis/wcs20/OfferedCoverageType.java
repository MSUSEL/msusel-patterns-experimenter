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
/**
 */
package net.opengis.wcs20;

import java.lang.Object;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Offered Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.OfferedCoverageType#getAbstractCoverage <em>Abstract Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs20.OfferedCoverageType#getServiceParameters <em>Service Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getOfferedCoverageType()
 * @model extendedMetaData="name='OfferedCoverageType' kind='elementOnly'"
 * @generated
 */
public interface OfferedCoverageType extends EObject {
    /**
     * Returns the value of the '<em><b>Abstract Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Like its role model gml:AbstractCoverageType in GML 3.2.1, this element serves as the head of a substitution group which may contain any coverage whose type is derived, however, from gmlcov:AbstractCoverageType (rather than gml:AbstractCoverageType).  It may act as a variable in the definition of content models where it is required to permit any coverage to be valid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract Coverage</em>' containment reference.
     * @see net.opengis.wcs20.Wcs20Package#getOfferedCoverageType_AbstractCoverage()
     * @model type="java.lang.Object" containment="true" required="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractCoverage' namespace='http://www.opengis.net/gmlcov/1.0' group='http://www.opengis.net/gmlcov/1.0#AbstractCoverage:group'"
     */
    Object getAbstractCoverage();

    /**
     * Returns the value of the '<em><b>Service Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * ServiceParameters further define how the corresponding coverage is accessible. CoverageSubtype helps identifying the type of coverage on hand, in particular with respect to the potential size of its domainSet and rangeSet components. Extension elements allow WCS extensions to plug in their particular coverage-specific service information.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Parameters</em>' containment reference.
     * @see #setServiceParameters(ServiceParametersType)
     * @see net.opengis.wcs20.Wcs20Package#getOfferedCoverageType_ServiceParameters()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ServiceParameters' namespace='##targetNamespace'"
     * @generated
     */
    ServiceParametersType getServiceParameters();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.OfferedCoverageType#getServiceParameters <em>Service Parameters</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Parameters</em>' containment reference.
     * @see #getServiceParameters()
     * @generated
     */
    void setServiceParameters(ServiceParametersType value);

} // OfferedCoverageType
