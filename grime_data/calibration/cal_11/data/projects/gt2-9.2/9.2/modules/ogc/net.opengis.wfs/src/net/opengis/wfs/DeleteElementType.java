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

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

import org.opengis.filter.Filter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Delete Element Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.DeleteElementType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.DeleteElementType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.DeleteElementType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getDeleteElementType()
 * @model extendedMetaData="name='DeleteElementType' kind='elementOnly'"
 * @generated
 */
public interface DeleteElementType extends EObject {
	/**
	 * Returns the value of the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The Filter element is used to constrain the scope
	 *                   of the delete operation to those features identified
	 *                   by the filter.  Feature instances can be specified
	 *                   explicitly and individually using the identifier of
	 *                   each feature instance OR a set of features to be
	 *                   operated on can be identified by specifying spatial
	 *                   and non-spatial constraints in the filter.
	 *                   If no filter is specified then an exception should
	 *                   be raised since it is unlikely that a client application
	 *                   intends to delete all feature instances.
	 *                
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Filter</em>' attribute.
	 * @see #setFilter(Object)
	 * @see net.opengis.wfs.WFSPackage#getDeleteElementType_Filter()
	 * @model 
	 */
	Filter getFilter();
	

	/**
     * Sets the value of the '{@link net.opengis.wfs.DeleteElementType#getFilter <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter</em>' attribute.
     * @see #getFilter()
     * @generated
     */
	void setFilter(Filter value);

	/**
     * Returns the value of the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                The handle attribute allows a client application
     *                to assign a client-generated request identifier
     *                to an Insert action.  The handle is included to
     *                facilitate error reporting.  If a Delete action
     *                in a Transaction request fails, then a WFS may
     *                include the handle in an exception report to localize
     *                the error.  If no handle is included of the offending
     *                Insert element then a WFS may employee other means of
     *                localizing the error (e.g. line number).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.wfs.WfsPackage#getDeleteElementType_Handle()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
	String getHandle();

	/**
     * Sets the value of the '{@link net.opengis.wfs.DeleteElementType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
	void setHandle(String value);

	/**
	 * Returns the value of the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *               The value of the typeName attribute is the name 
	 *               of the feature type to be updated. The name
	 *               specified must be a valid type that belongs to
	 *               the feature content as defined by the GML
	 *               Application Schema.
	 *            
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type Name</em>' attribute.
	 * @see #setTypeName(Object)
	 * @see net.opengis.wfs.WFSPackage#getDeleteElementType_TypeName()
	 * @model 
	 */
	QName getTypeName();

	/**
     * Sets the value of the '{@link net.opengis.wfs.DeleteElementType#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
	void setTypeName(QName value);

} // DeleteElementType
