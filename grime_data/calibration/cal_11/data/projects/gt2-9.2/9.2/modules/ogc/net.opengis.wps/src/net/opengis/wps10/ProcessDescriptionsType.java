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
package net.opengis.wps10;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Descriptions Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ProcessDescriptionsType#getProcessDescription <em>Process Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getProcessDescriptionsType()
 * @model extendedMetaData="name='ProcessDescriptions_._type' kind='elementOnly'"
 * @generated
 */
public interface ProcessDescriptionsType extends ResponseBaseType {
    /**
     * Returns the value of the '<em><b>Process Description</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wps10.ProcessDescriptionType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered list of one or more full Process descriptions, listed in the order in which they were requested in the DescribeProcess operation request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Description</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getProcessDescriptionsType_ProcessDescription()
     * @model type="net.opengis.wps10.ProcessDescriptionType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='ProcessDescription'"
     * @generated
     */
    EList getProcessDescription();

} // ProcessDescriptionsType
