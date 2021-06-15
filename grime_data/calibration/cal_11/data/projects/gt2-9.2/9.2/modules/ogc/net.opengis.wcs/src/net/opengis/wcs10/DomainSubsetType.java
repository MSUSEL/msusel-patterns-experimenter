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
 * A representation of the model object '<em><b>Domain Subset Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines the desired subset of the domain set of the coverage. Is a GML property containing either or both spatialSubset and temporalSubset GML objects.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.DomainSubsetType#getSpatialSubset <em>Spatial Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.DomainSubsetType#getTemporalSubset <em>Temporal Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.DomainSubsetType#getTemporalSubset1 <em>Temporal Subset1</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getDomainSubsetType()
 * @model extendedMetaData="name='DomainSubsetType' kind='elementOnly'"
 * @generated
 */
public interface DomainSubsetType extends EObject {
    /**
	 * Returns the value of the '<em><b>Spatial Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Subset</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Spatial Subset</em>' containment reference.
	 * @see #setSpatialSubset(SpatialSubsetType)
	 * @see net.opengis.wcs10.Wcs10Package#getDomainSubsetType_SpatialSubset()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='spatialSubset' namespace='##targetNamespace'"
	 * @generated
	 */
    SpatialSubsetType getSpatialSubset();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DomainSubsetType#getSpatialSubset <em>Spatial Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spatial Subset</em>' containment reference.
	 * @see #getSpatialSubset()
	 * @generated
	 */
    void setSpatialSubset(SpatialSubsetType value);

    /**
	 * Returns the value of the '<em><b>Temporal Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Subset</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Temporal Subset</em>' containment reference.
	 * @see #setTemporalSubset(TimeSequenceType)
	 * @see net.opengis.wcs10.Wcs10Package#getDomainSubsetType_TemporalSubset()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='temporalSubset' namespace='##targetNamespace'"
	 * @generated
	 */
    TimeSequenceType getTemporalSubset();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DomainSubsetType#getTemporalSubset <em>Temporal Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporal Subset</em>' containment reference.
	 * @see #getTemporalSubset()
	 * @generated
	 */
    void setTemporalSubset(TimeSequenceType value);

    /**
	 * Returns the value of the '<em><b>Temporal Subset1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Subset1</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Temporal Subset1</em>' containment reference.
	 * @see #setTemporalSubset1(TimeSequenceType)
	 * @see net.opengis.wcs10.Wcs10Package#getDomainSubsetType_TemporalSubset1()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='temporalSubset' namespace='##targetNamespace'"
	 * @generated
	 */
    TimeSequenceType getTemporalSubset1();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DomainSubsetType#getTemporalSubset1 <em>Temporal Subset1</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporal Subset1</em>' containment reference.
	 * @see #getTemporalSubset1()
	 * @generated
	 */
    void setTemporalSubset1(TimeSequenceType value);

} // DomainSubsetType
