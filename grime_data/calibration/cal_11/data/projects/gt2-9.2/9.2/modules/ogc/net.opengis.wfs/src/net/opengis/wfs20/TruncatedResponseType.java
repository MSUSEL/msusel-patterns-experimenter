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

import net.opengis.ows11.ExceptionReportType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Truncated Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.TruncatedResponseType#getExceptionReport <em>Exception Report</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getTruncatedResponseType()
 * @model extendedMetaData="name='truncatedResponse_._type' kind='elementOnly'"
 * @generated
 */
public interface TruncatedResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Exception Report</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Report message returned to the client that requested any OWS operation when the server detects an error while processing that operation request. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exception Report</em>' containment reference.
     * @see #setExceptionReport(ExceptionReportType)
     * @see net.opengis.wfs20.Wfs20Package#getTruncatedResponseType_ExceptionReport()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ExceptionReport' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    ExceptionReportType getExceptionReport();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TruncatedResponseType#getExceptionReport <em>Exception Report</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exception Report</em>' containment reference.
     * @see #getExceptionReport()
     * @generated
     */
    void setExceptionReport(ExceptionReportType value);

} // TruncatedResponseType
