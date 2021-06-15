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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Result Type Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs.WfsPackage#getResultTypeType()
 * @model
 * @generated
 */
public final class ResultTypeType extends AbstractEnumerator {
	/**
     * The '<em><b>Results</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   Indicates that a complete response should be generated
     *                   by the WFS.  That is, all response feature instances
     *                   should be returned to the client.
     * <!-- end-model-doc -->
     * @see #RESULTS_LITERAL
     * @model name="results"
     * @generated
     * @ordered
     */
	public static final int RESULTS = 0;

	/**
     * The '<em><b>Hits</b></em>' literal value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   Indicates that an empty response should be generated with
     *                   the "numberOfFeatures" attribute set (i.e. no feature
     *                   instances should be returned).  In this manner a client may
     *                   determine the number of feature instances that a GetFeature
     *                   request will return without having to actually get the
     *                   entire result set back.
     * <!-- end-model-doc -->
     * @see #HITS_LITERAL
     * @model name="hits"
     * @generated
     * @ordered
     */
	public static final int HITS = 1;

	/**
     * The '<em><b>Results</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #RESULTS
     * @generated
     * @ordered
     */
	public static final ResultTypeType RESULTS_LITERAL = new ResultTypeType(RESULTS, "results", "results");

	/**
     * The '<em><b>Hits</b></em>' literal object.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #HITS
     * @generated
     * @ordered
     */
	public static final ResultTypeType HITS_LITERAL = new ResultTypeType(HITS, "hits", "hits");

	/**
     * An array of all the '<em><b>Result Type Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private static final ResultTypeType[] VALUES_ARRAY =
		new ResultTypeType[] {
            RESULTS_LITERAL,
            HITS_LITERAL,
        };

	/**
     * A public read-only list of all the '<em><b>Result Type Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
     * Returns the '<em><b>Result Type Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static ResultTypeType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ResultTypeType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

	/**
     * Returns the '<em><b>Result Type Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static ResultTypeType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ResultTypeType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

	/**
     * Returns the '<em><b>Result Type Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static ResultTypeType get(int value) {
        switch (value) {
            case RESULTS: return RESULTS_LITERAL;
            case HITS: return HITS_LITERAL;
        }
        return null;
    }

	/**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	private ResultTypeType(int value, String name, String literal) {
        super(value, name, literal);
    }

} //ResultTypeType
