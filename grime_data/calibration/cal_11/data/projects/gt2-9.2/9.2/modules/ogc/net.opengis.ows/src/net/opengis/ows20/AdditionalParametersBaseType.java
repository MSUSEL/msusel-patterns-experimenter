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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Additional Parameters Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.AdditionalParametersBaseType#getAdditionalParameter <em>Additional Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getAdditionalParametersBaseType()
 * @model extendedMetaData="name='AdditionalParametersBaseType' kind='elementOnly'"
 * @generated
 */
public interface AdditionalParametersBaseType extends MetadataType {
    /**
     * Returns the value of the '<em><b>Additional Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * One additional metadata parameter.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Additional Parameter</em>' containment reference.
     * @see #setAdditionalParameter(AdditionalParameterType)
     * @see net.opengis.ows20.Ows20Package#getAdditionalParametersBaseType_AdditionalParameter()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='AdditionalParameter' namespace='##targetNamespace'"
     * @generated
     */
    AdditionalParameterType getAdditionalParameter();

    /**
     * Sets the value of the '{@link net.opengis.ows20.AdditionalParametersBaseType#getAdditionalParameter <em>Additional Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Additional Parameter</em>' containment reference.
     * @see #getAdditionalParameter()
     * @generated
     */
    void setAdditionalParameter(AdditionalParameterType value);

} // AdditionalParametersBaseType
