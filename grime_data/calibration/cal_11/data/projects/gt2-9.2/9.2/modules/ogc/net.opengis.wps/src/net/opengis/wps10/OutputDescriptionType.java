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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of a process Output.
 * In this use, the DescriptionType shall describe this process output.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.OutputDescriptionType#getComplexOutput <em>Complex Output</em>}</li>
 *   <li>{@link net.opengis.wps10.OutputDescriptionType#getLiteralOutput <em>Literal Output</em>}</li>
 *   <li>{@link net.opengis.wps10.OutputDescriptionType#getBoundingBoxOutput <em>Bounding Box Output</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getOutputDescriptionType()
 * @model extendedMetaData="name='OutputDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface OutputDescriptionType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Complex Output</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this Output shall be a complex data structure (such as a GML fragment) that is returned by the execute operation response. The value of this complex data structure can be output either embedded in the execute operation response or remotely accessible to the client. When this output form is indicated, the process produces only a single output, and "store" is "false, the output shall be returned directly, without being embedded in the XML document that is otherwise provided by execute operation response.
     * 					This element also provides a list of format, encoding, and schema combinations supported for this output. The client can select from among the identified combinations of formats, encodings, and schemas to specify the form of the output. This allows for complete specification of particular versions of GML, or image formats.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Complex Output</em>' containment reference.
     * @see #setComplexOutput(SupportedComplexDataType)
     * @see net.opengis.wps10.Wps10Package#getOutputDescriptionType_ComplexOutput()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ComplexOutput'"
     * @generated
     */
    SupportedComplexDataType getComplexOutput();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDescriptionType#getComplexOutput <em>Complex Output</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Complex Output</em>' containment reference.
     * @see #getComplexOutput()
     * @generated
     */
    void setComplexOutput(SupportedComplexDataType value);

    /**
     * Returns the value of the '<em><b>Literal Output</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this output shall be a simple literal value (such as an integer) that is embedded in the execute response, and describes that output.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Literal Output</em>' containment reference.
     * @see #setLiteralOutput(LiteralOutputType)
     * @see net.opengis.wps10.Wps10Package#getOutputDescriptionType_LiteralOutput()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='LiteralOutput'"
     * @generated
     */
    LiteralOutputType getLiteralOutput();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDescriptionType#getLiteralOutput <em>Literal Output</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Literal Output</em>' containment reference.
     * @see #getLiteralOutput()
     * @generated
     */
    void setLiteralOutput(LiteralOutputType value);

    /**
     * Returns the value of the '<em><b>Bounding Box Output</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this output shall be a BoundingBox data structure, and provides a list of the CRSs supported in these Bounding Boxes. This element shall be included when this process output is an ows:BoundingBox element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box Output</em>' containment reference.
     * @see #setBoundingBoxOutput(SupportedCRSsType)
     * @see net.opengis.wps10.Wps10Package#getOutputDescriptionType_BoundingBoxOutput()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='BoundingBoxOutput'"
     * @generated
     */
    SupportedCRSsType getBoundingBoxOutput();

    /**
     * Sets the value of the '{@link net.opengis.wps10.OutputDescriptionType#getBoundingBoxOutput <em>Bounding Box Output</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounding Box Output</em>' containment reference.
     * @see #getBoundingBoxOutput()
     * @generated
     */
    void setBoundingBoxOutput(SupportedCRSsType value);

} // OutputDescriptionType
