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
package net.opengis.wfs20;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Additional Values Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.AdditionalValuesType#getValueCollection <em>Value Collection</em>}</li>
 *   <li>{@link net.opengis.wfs20.AdditionalValuesType#getSimpleFeatureCollectionGroup <em>Simple Feature Collection Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.AdditionalValuesType#getSimpleFeatureCollection <em>Simple Feature Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getAdditionalValuesType()
 * @model extendedMetaData="name='additionalValues_._type' kind='elementOnly'"
 * @generated
 */
public interface AdditionalValuesType extends EObject {
    /**
     * Returns the value of the '<em><b>Value Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value Collection</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value Collection</em>' containment reference.
     * @see #setValueCollection(ValueCollectionType)
     * @see net.opengis.wfs20.Wfs20Package#getAdditionalValuesType_ValueCollection()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ValueCollection' namespace='##targetNamespace'"
     * @generated
     */
    ValueCollectionType getValueCollection();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.AdditionalValuesType#getValueCollection <em>Value Collection</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Collection</em>' containment reference.
     * @see #getValueCollection()
     * @generated
     */
    void setValueCollection(ValueCollectionType value);

    /**
     * Returns the value of the '<em><b>Simple Feature Collection Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Simple Feature Collection Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Simple Feature Collection Group</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getAdditionalValuesType_SimpleFeatureCollectionGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='SimpleFeatureCollection:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getSimpleFeatureCollectionGroup();

    /**
     * Returns the value of the '<em><b>Simple Feature Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Simple Feature Collection</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Simple Feature Collection</em>' containment reference.
     * @see #setSimpleFeatureCollection(SimpleFeatureCollectionType)
     * @see net.opengis.wfs20.Wfs20Package#getAdditionalValuesType_SimpleFeatureCollection()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='SimpleFeatureCollection' namespace='##targetNamespace' group='SimpleFeatureCollection:group'"
     * @generated
     */
    SimpleFeatureCollectionType getSimpleFeatureCollection();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.AdditionalValuesType#getSimpleFeatureCollection <em>Simple Feature Collection</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Simple Feature Collection</em>' containment reference.
     * @see #getSimpleFeatureCollection()
     * @generated
     */
    void setSimpleFeatureCollection(SimpleFeatureCollectionType value);

} // AdditionalValuesType
