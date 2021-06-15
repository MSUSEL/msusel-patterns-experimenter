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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Human-readable descriptive information for the object it is included within.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.AbstractDescriptionType#getMetadataLink <em>Metadata Link</em>}</li>
 *   <li>{@link net.opengis.wcs10.AbstractDescriptionType#getDescription1 <em>Description1</em>}</li>
 *   <li>{@link net.opengis.wcs10.AbstractDescriptionType#getName1 <em>Name1</em>}</li>
 *   <li>{@link net.opengis.wcs10.AbstractDescriptionType#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getAbstractDescriptionType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface AbstractDescriptionType extends AbstractDescriptionBaseType {
    /**
	 * Returns the value of the '<em><b>Metadata Link</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.MetadataLinkType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata Link</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Metadata Link</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getAbstractDescriptionType_MetadataLink()
	 * @model type="net.opengis.wcs10.MetadataLinkType" containment="true"
	 *        extendedMetaData="kind='element' name='metadataLink' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getMetadataLink();

    /**
	 * Returns the value of the '<em><b>Description1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Contains a simple text description of the object.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Description1</em>' attribute.
	 * @see #setDescription1(String)
	 * @see net.opengis.wcs10.Wcs10Package#getAbstractDescriptionType_Description1()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
    String getDescription1();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.AbstractDescriptionType#getDescription1 <em>Description1</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description1</em>' attribute.
	 * @see #getDescription1()
	 * @generated
	 */
    void setDescription1(String value);

    /**
	 * Returns the value of the '<em><b>Name1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Identifier for the object, normally a descriptive name.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name1</em>' attribute.
	 * @see #setName1(String)
	 * @see net.opengis.wcs10.Wcs10Package#getAbstractDescriptionType_Name1()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='name' namespace='##targetNamespace'"
	 * @generated
	 */
    String getName1();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.AbstractDescriptionType#getName1 <em>Name1</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name1</em>' attribute.
	 * @see #getName1()
	 * @generated
	 */
    void setName1(String value);

    /**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Short human-readable label for this object, for human interface display.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see net.opengis.wcs10.Wcs10Package#getAbstractDescriptionType_Label()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='label' namespace='##targetNamespace'"
	 * @generated
	 */
    String getLabel();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.AbstractDescriptionType#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
    void setLabel(String value);

} // AbstractDescriptionType
