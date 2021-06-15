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
package net.opengis.cat.csw20;

import java.lang.String;

import net.opengis.ows10.BoundingBoxType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Brief Record Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This type defines a brief representation of the common record
 *             format.  It extends AbstractRecordType to include only the
 *              dc:identifier and dc:type properties.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getBoundingBox <em>Bounding Box</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType()
 * @model extendedMetaData="name='BriefRecordType' kind='elementOnly'"
 * @generated
 */
public interface BriefRecordType extends AbstractRecordType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An unambiguous reference to the resource within a given context.
     *       Recommended best practice is to identify the resource by means of a
     *       string or number conforming to a formal identification system. Formal
     *       identification systems include but are not limited to the Uniform
     *       Resource Identifier (URI) (including the Uniform Resource Locator
     *       (URL)), the Digital Object Identifier (DOI), and the International
     *       Standard Book Number (ISBN).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_Identifier()
     * @model 
     */
    EList<SimpleLiteral> getIdentifier();

    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A name given to the resource. Typically, Title will be a name by
     *       which the resource is formally known.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_Title()
     * @model 
     */
    EList<SimpleLiteral> getTitle();

    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The nature or genre of the content of the resource. Type includes
     *       terms describing general categories, functions, genres, or aggregation
     *       levels for content. Recommended best practice is to select a value
     *       from a controlled vocabulary (for example, the DCMI Type Vocabulary).
     *       To describe the physical or digital manifestation of the resource,
     *       use the Format element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(String)
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_Type()
     * @model 
     */
    SimpleLiteral getType();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.BriefRecordType#getType <em>Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' reference.
     * @see #getType()
     * @generated
     */
    void setType(SimpleLiteral value);

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows10.BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounding Box</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_BoundingBox()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='http://www.opengis.net/ows' group='http://www.opengis.net/ows#BoundingBox:group'"
     * @generated
     */
    EList<BoundingBoxType> getBoundingBox();

} // BriefRecordType
