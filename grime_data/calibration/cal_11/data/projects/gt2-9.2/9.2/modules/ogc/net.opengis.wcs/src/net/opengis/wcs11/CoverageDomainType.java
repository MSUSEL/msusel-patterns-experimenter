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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of the spatial-temporal domain of a coverage. The Domain shall include a SpatialDomain (describing the spatial locations for which coverages can be requested), and should included a TemporalDomain (describing the time instants or intervals for which coverages can be requested). 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.CoverageDomainType#getSpatialDomain <em>Spatial Domain</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageDomainType#getTemporalDomain <em>Temporal Domain</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getCoverageDomainType()
 * @model extendedMetaData="name='CoverageDomainType' kind='elementOnly'"
 * @generated
 */
public interface CoverageDomainType extends EObject {
    /**
     * Returns the value of the '<em><b>Spatial Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spatial Domain</em>' containment reference.
     * @see #setSpatialDomain(SpatialDomainType)
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDomainType_SpatialDomain()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='SpatialDomain' namespace='##targetNamespace'"
     * @generated
     */
    SpatialDomainType getSpatialDomain();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CoverageDomainType#getSpatialDomain <em>Spatial Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Spatial Domain</em>' containment reference.
     * @see #getSpatialDomain()
     * @generated
     */
    void setSpatialDomain(SpatialDomainType value);

    /**
     * Returns the value of the '<em><b>Temporal Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Although optional, the TemporalDomain should be included whenever a value is known or a useful estimate is available. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Temporal Domain</em>' containment reference.
     * @see #setTemporalDomain(TimeSequenceType)
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDomainType_TemporalDomain()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='TemporalDomain' namespace='##targetNamespace'"
     * @generated
     */
    TimeSequenceType getTemporalDomain();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CoverageDomainType#getTemporalDomain <em>Temporal Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Domain</em>' containment reference.
     * @see #getTemporalDomain()
     * @generated
     */
    void setTemporalDomain(TimeSequenceType value);

} // CoverageDomainType
