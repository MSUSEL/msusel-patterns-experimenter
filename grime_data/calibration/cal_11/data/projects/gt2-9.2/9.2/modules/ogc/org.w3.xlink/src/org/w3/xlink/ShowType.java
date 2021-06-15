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
package org.w3.xlink;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Show Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3.xlink.XlinkPackage#getShowType()
 * @model extendedMetaData="name='showType'"
 * @generated
 */
public final class ShowType extends AbstractEnumerator {
    /**
     * The '<em><b>New</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>New</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #NEW_LITERAL
     * @model name="new"
     * @generated
     * @ordered
     */
    public static final int NEW = 0;

    /**
     * The '<em><b>Replace</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Replace</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #REPLACE_LITERAL
     * @model name="replace"
     * @generated
     * @ordered
     */
    public static final int REPLACE = 1;

    /**
     * The '<em><b>Embed</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Embed</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #EMBED_LITERAL
     * @model name="embed"
     * @generated
     * @ordered
     */
    public static final int EMBED = 2;

    /**
     * The '<em><b>Other</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Other</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #OTHER_LITERAL
     * @model name="other"
     * @generated
     * @ordered
     */
    public static final int OTHER = 3;

    /**
     * The '<em><b>None</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>None</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #NONE_LITERAL
     * @model name="none"
     * @generated
     * @ordered
     */
    public static final int NONE = 4;

    /**
     * The '<em><b>New</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NEW
     * @generated
     * @ordered
     */
    public static final ShowType NEW_LITERAL = new ShowType(NEW, "new", "new");

    /**
     * The '<em><b>Replace</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #REPLACE
     * @generated
     * @ordered
     */
    public static final ShowType REPLACE_LITERAL = new ShowType(REPLACE, "replace", "replace");

    /**
     * The '<em><b>Embed</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EMBED
     * @generated
     * @ordered
     */
    public static final ShowType EMBED_LITERAL = new ShowType(EMBED, "embed", "embed");

    /**
     * The '<em><b>Other</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #OTHER
     * @generated
     * @ordered
     */
    public static final ShowType OTHER_LITERAL = new ShowType(OTHER, "other", "other");

    /**
     * The '<em><b>None</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NONE
     * @generated
     * @ordered
     */
    public static final ShowType NONE_LITERAL = new ShowType(NONE, "none", "none");

    /**
     * An array of all the '<em><b>Show Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final ShowType[] VALUES_ARRAY =
        new ShowType[] {
            NEW_LITERAL,
            REPLACE_LITERAL,
            EMBED_LITERAL,
            OTHER_LITERAL,
            NONE_LITERAL,
        };

    /**
     * A public read-only list of all the '<em><b>Show Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Show Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ShowType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ShowType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Show Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ShowType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ShowType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Show Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ShowType get(int value) {
        switch (value) {
            case NEW: return NEW_LITERAL;
            case REPLACE: return REPLACE_LITERAL;
            case EMBED: return EMBED_LITERAL;
            case OTHER: return OTHER_LITERAL;
            case NONE: return NONE_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private ShowType(int value, String name, String literal) {
        super(value, name, literal);
    }

} //ShowType
