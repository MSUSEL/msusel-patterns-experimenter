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
package net.opengis.wfs;

import java.math.BigInteger;
import java.util.Calendar;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This type defines a container for the response to a
 *             GetFeature or GetFeatureWithLock request.  If the
 *             request is GetFeatureWithLock, the lockId attribute
 *             must be populated.  The lockId attribute can otherwise
 *             be safely ignored.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.FeatureCollectionType#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.FeatureCollectionType#getTimeStamp <em>Time Stamp</em>}</li>
 *   <li>{@link net.opengis.wfs.FeatureCollectionType#getNumberOfFeatures <em>Number Of Features</em>}</li>
 *   <li>{@link net.opengis.wfs.FeatureCollectionType#getFeature <em>Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getFeatureCollectionType()
 * @model extendedMetaData="name='FeatureCollectionType' kind='empty'"
 * @generated
 */
public interface FeatureCollectionType extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * <pre>
	 * &lt;xsd:attribute name="lockId" type="xsd:string" use="optional"&gt;
     *       &lt;xsd:annotation&gt;
     *          &lt;xsd:documentation&gt;
     *             The value of the lockId attribute is an identifier
     *             that a Web Feature Service generates when responding
     *             to a GetFeatureWithLock request.  A client application
     *             can use this value in subsequent operations (such as a
     *             Transaction request) to reference the set of locked
     *             features.
     *          &lt;/xsd:documentation&gt;
     *       &lt;/xsd:annotation&gt;
     *    &lt;/xsd:attribute&gt;
	 * </pre>
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @model
	 */
	String getLockId();
	
	/**
     * Sets the value of the '{@link net.opengis.wfs.FeatureCollectionType#getLockId <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Id</em>' attribute.
     * @see #getLockId()
     * @generated
     */
	void setLockId(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * <pre>
	 *   &lt;xsd:attribute name="timeStamp" type="xsd:dateTime" use="optional"&gt;
     *       &lt;xsd:annotation&gt;
     *          &lt;xsd:documentation&gt;
     *             The timeStamp attribute should contain the date and time
     *             that the response was generated.
     *          &lt;/xsd:documentation&gt;
     *       &lt;/xsd:annotation&gt;
     *    &lt;/xsd:attribute&gt;
     * </pre>
     * </p>
     * <!-- end-user-doc -->
	 * @model
	 */
	Calendar getTimeStamp();
	
	

	/**
     * Sets the value of the '{@link net.opengis.wfs.FeatureCollectionType#getTimeStamp <em>Time Stamp</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Stamp</em>' attribute.
     * @see #getTimeStamp()
     * @generated
     */
	void setTimeStamp(Calendar value);

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * <pre>
	 *   &lt;xsd:attribute name="numberOfFeatures"
     *                   type="xsd:nonNegativeInteger"
     *                   use="optional"&gt;
     *       &lt;xsd:annotation&gt;
     *          &lt;xsd:documentation&gt;
     *             The numberOfFeatures attribute should contain a
     *             count of the number of features in the response.
     *             That is a count of all features elements dervied
     *             from gml:AbstractFeatureType.
     *          &lt;/xsd:documentation&gt;
     *       &lt;/xsd:annotation&gt;
     *    &lt;/xsd:attribute&gt;
     * </pre>
     * </p>
     * <!-- end-user-doc -->
	 * @model
	 */
	BigInteger getNumberOfFeatures();
	
	/**
     * Sets the value of the '{@link net.opengis.wfs.FeatureCollectionType#getNumberOfFeatures <em>Number Of Features</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Number Of Features</em>' attribute.
     * @see #getNumberOfFeatures()
     * @generated
     */
	void setNumberOfFeatures(BigInteger value);

	/**
	 * Reference to a set of geotools feature collections.
	 * @model type="org.geotools.feature.FeatureCollection"
	 */
	EList getFeature();
} // FeatureCollectionType
