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
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dataset Description Summary Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Typical dataset metadata in typical Contents section of
 *       an OWS service metadata (Capabilities) document. This type shall be
 *       extended and/or restricted if needed for specific OWS use, to include
 *       the specific Dataset description metadata needed.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.DatasetDescriptionSummaryBaseType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows20.DatasetDescriptionSummaryBaseType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows20.DatasetDescriptionSummaryBaseType#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.ows20.DatasetDescriptionSummaryBaseType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows20.DatasetDescriptionSummaryBaseType#getMetadataGroup <em>Metadata Group</em>}</li>
 *   <li>{@link net.opengis.ows20.DatasetDescriptionSummaryBaseType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows20.DatasetDescriptionSummaryBaseType#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getDatasetDescriptionSummaryBaseType()
 * @model extendedMetaData="name='DatasetDescriptionSummaryBaseType' kind='elementOnly'"
 * @generated
 */
public interface DatasetDescriptionSummaryBaseType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.WGS84BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more minimum bounding
     *               rectangles surrounding coverage data, using the WGS 84 CRS with
     *               decimal degrees and longitude before latitude. If no WGS 84
     *               bounding box is recorded for a coverage, any such bounding boxes
     *               recorded for a higher level in a hierarchy of datasets shall
     *               apply to this coverage. If WGS 84 bounding box(es) are recorded
     *               for a coverage, any such bounding boxes recorded for a higher
     *               level in a hierarchy of datasets shall be ignored. For each
     *               lowest-level coverage in a hierarchy, at least one applicable
     *               WGS84BoundingBox shall be either recorded or inherited, to
     *               simplify searching for datasets that might overlap a specified
     *               region. If multiple WGS 84 bounding boxes are included, this
     *               shall be interpreted as the union of the areas of these bounding
     *               boxes.
     * <!-- end-model-doc -->
     * @return the value of the '<em>WGS84 Bounding Box</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getDatasetDescriptionSummaryBaseType_WGS84BoundingBox()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='WGS84BoundingBox' namespace='##targetNamespace'"
     * @generated
     */
    EList<WGS84BoundingBoxType> getWGS84BoundingBox();

    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unambiguous identifier or name of this coverage,
     *               unique for this server.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.ows20.Ows20Package#getDatasetDescriptionSummaryBaseType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.ows20.DatasetDescriptionSummaryBaseType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Bounding Box Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more minimum bounding
     *               rectangles surrounding coverage data, in AvailableCRSs. Zero or
     *               more BoundingBoxes are allowed in addition to one or more
     *               WGS84BoundingBoxes to allow more precise specification of the
     *               Dataset area in AvailableCRSs. These Bounding Boxes shall not
     *               use any CRS not listed as an AvailableCRS. However, an
     *               AvailableCRS can be listed without a corresponding Bounding Box.
     *               If no such bounding box is recorded for a coverage, any such
     *               bounding boxes recorded for a higher level in a hierarchy of
     *               datasets shall apply to this coverage. If such bounding box(es)
     *               are recorded for a coverage, any such bounding boxes recorded
     *               for a higher level in a hierarchy of datasets shall be ignored.
     *               If multiple bounding boxes are included with the same CRS, this
     *               shall be interpreted as the union of the areas of these bounding
     *               boxes.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box Group</em>' attribute list.
     * @see net.opengis.ows20.Ows20Package#getDatasetDescriptionSummaryBaseType_BoundingBoxGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='BoundingBox:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getBoundingBoxGroup();

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more minimum bounding
     *               rectangles surrounding coverage data, in AvailableCRSs. Zero or
     *               more BoundingBoxes are allowed in addition to one or more
     *               WGS84BoundingBoxes to allow more precise specification of the
     *               Dataset area in AvailableCRSs. These Bounding Boxes shall not
     *               use any CRS not listed as an AvailableCRS. However, an
     *               AvailableCRS can be listed without a corresponding Bounding Box.
     *               If no such bounding box is recorded for a coverage, any such
     *               bounding boxes recorded for a higher level in a hierarchy of
     *               datasets shall apply to this coverage. If such bounding box(es)
     *               are recorded for a coverage, any such bounding boxes recorded
     *               for a higher level in a hierarchy of datasets shall be ignored.
     *               If multiple bounding boxes are included with the same CRS, this
     *               shall be interpreted as the union of the areas of these bounding
     *               boxes.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getDatasetDescriptionSummaryBaseType_BoundingBox()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='##targetNamespace' group='BoundingBox:group'"
     * @generated
     */
    EList<BoundingBoxType> getBoundingBox();

    /**
     * Returns the value of the '<em><b>Metadata Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata
     *               about this dataset. A list of optional metadata elements for
     *               this dataset description could be specified in the
     *               Implementation Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata Group</em>' attribute list.
     * @see net.opengis.ows20.Ows20Package#getDatasetDescriptionSummaryBaseType_MetadataGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='Metadata:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getMetadataGroup();

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata
     *               about this dataset. A list of optional metadata elements for
     *               this dataset description could be specified in the
     *               Implementation Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getDatasetDescriptionSummaryBaseType_Metadata()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='##targetNamespace' group='Metadata:group'"
     * @generated
     */
    EList<MetadataType> getMetadata();

    /**
     * Returns the value of the '<em><b>Dataset Description Summary</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.DatasetDescriptionSummaryBaseType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata describing zero or more unordered
     *               subsidiary datasets available from this server.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dataset Description Summary</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getDatasetDescriptionSummaryBaseType_DatasetDescriptionSummary()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DatasetDescriptionSummary' namespace='##targetNamespace'"
     * @generated
     */
    EList<DatasetDescriptionSummaryBaseType> getDatasetDescriptionSummary();

} // DatasetDescriptionSummaryBaseType
