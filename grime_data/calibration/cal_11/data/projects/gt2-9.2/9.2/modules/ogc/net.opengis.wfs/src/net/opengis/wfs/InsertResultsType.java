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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Insert Results Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             Reports the list of identifiers of all features created
 *             by a transaction request.  New features are created using
 *             the Insert action and the list of idetifiers must be
 *             presented in the same order as the Insert actions were
 *             encountered in the transaction request.  Features may
 *             optionally be correlated with identifiers using the
 *             handle attribute (if it was specified on the Insert
 *             element).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.InsertResultsType#getFeature <em>Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getInsertResultsType()
 * @model extendedMetaData="name='InsertResultsType' kind='elementOnly'"
 * @generated
 */
public interface InsertResultsType extends EObject {
	/**
     * Returns the value of the '<em><b>Feature</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.InsertedFeatureType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Feature</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getInsertResultsType_Feature()
     * @model type="net.opengis.wfs.InsertedFeatureType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Feature' namespace='##targetNamespace'"
     * @generated
     */
	EList getFeature();

} // InsertResultsType
