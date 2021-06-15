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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata Link Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Refers to a metadata package that contains metadata properties for an object. The metadataType attribute indicates the type of metadata referred to.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.MetadataLinkType#getMetadataType <em>Metadata Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getMetadataLinkType()
 * @model extendedMetaData="name='MetadataLinkType' kind='empty'"
 * @generated
 */
public interface MetadataLinkType extends MetadataAssociationType {
    /**
	 * Returns the value of the '<em><b>Metadata Type</b></em>' attribute.
	 * The default value is <code>"TC211"</code>.
	 * The literals are from the enumeration {@link net.opengis.wcs10.MetadataTypeType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Metadata Type</em>' attribute.
	 * @see net.opengis.wcs10.MetadataTypeType
	 * @see #isSetMetadataType()
	 * @see #unsetMetadataType()
	 * @see #setMetadataType(MetadataTypeType)
	 * @see net.opengis.wcs10.Wcs10Package#getMetadataLinkType_MetadataType()
	 * @model default="TC211" unsettable="true" required="true"
	 *        extendedMetaData="kind='attribute' name='metadataType'"
	 * @generated
	 */
    MetadataTypeType getMetadataType();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.MetadataLinkType#getMetadataType <em>Metadata Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metadata Type</em>' attribute.
	 * @see net.opengis.wcs10.MetadataTypeType
	 * @see #isSetMetadataType()
	 * @see #unsetMetadataType()
	 * @see #getMetadataType()
	 * @generated
	 */
    void setMetadataType(MetadataTypeType value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.MetadataLinkType#getMetadataType <em>Metadata Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetMetadataType()
	 * @see #getMetadataType()
	 * @see #setMetadataType(MetadataTypeType)
	 * @generated
	 */
    void unsetMetadataType();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.MetadataLinkType#getMetadataType <em>Metadata Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Metadata Type</em>' attribute is set.
	 * @see #unsetMetadataType()
	 * @see #getMetadataType()
	 * @see #setMetadataType(MetadataTypeType)
	 * @generated
	 */
    boolean isSetMetadataType();

} // MetadataLinkType
