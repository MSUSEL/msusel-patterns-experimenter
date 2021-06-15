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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.DomCharacterData;

/**
 * A JavaScript object for CharacterData.
 *
 * @version $Revision: 5301 $
 * @author David K. Taylor
 * @author Chris Erskine
 */
public class CharacterDataImpl extends Node {

    private static final long serialVersionUID = 5413850371617638797L;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public CharacterDataImpl() {
    }

    /**
     * Gets the JavaScript property "data" for this character data.
     * @return the String of data
     */
    public Object jsxGet_data() {
        final DomCharacterData domCharacterData = (DomCharacterData) getDomNodeOrDie();
        return domCharacterData.getData();
    }

    /**
     * Sets the JavaScript property "data" for this character data.
     * @param newValue the new String of data
     */
    public void jsxSet_data(final String newValue) {
        final DomCharacterData domCharacterData = (DomCharacterData) getDomNodeOrDie();
        domCharacterData.setData(newValue);
    }

    /**
     * Gets the number of character in the character data.
     * @return the number of characters
     */
    public int jsxGet_length() {
        final DomCharacterData domCharacterData = (DomCharacterData) getDomNodeOrDie();
        return domCharacterData.getLength();
    }

    /**
     * Append a string to character data.
     * @param arg the string to be appended to the character data
     */
    public void jsxFunction_appendData(final String arg) {
        final DomCharacterData domCharacterData = (DomCharacterData) getDomNodeOrDie();
        domCharacterData.appendData(arg);
    }

    /**
     * Delete characters from character data.
     * @param offset the position of the first character to be deleted
     * @param count the number of characters to be deleted
     */
    public void jsxFunction_deleteData(final int offset, final int count) {
        final DomCharacterData domCharacterData = (DomCharacterData) getDomNodeOrDie();
        domCharacterData.deleteData(offset, count);
    }

    /**
     * Insert a string into character data.
     * @param offset the position within the first character at which
     * the string is to be inserted.
     * @param arg the string to insert
     */
    public void jsxFunction_insertData(final int offset, final String arg) {
        final DomCharacterData domCharacterData = (DomCharacterData) getDomNodeOrDie();
        domCharacterData.insertData(offset, arg);
    }

    /**
     * Replace characters of character data with a string.
     * @param offset the position within the first character at which
     * the string is to be replaced.
     * @param count the number of characters to be replaced
     * @param arg the string that replaces the count characters beginning at
     * the character at offset.
     */
    public void jsxFunction_replaceData(final int offset, final int count, final String arg) {
        final DomCharacterData domCharacterData = (DomCharacterData) getDomNodeOrDie();
        domCharacterData.replaceData(offset, count, arg);
    }

    /**
     * Extract a substring from character data.
     * @param offset the position of the first character to be extracted
     * @param count the number of characters to be extracted
     * @return a string that consists of the count characters of the character
     *         data starting from the character at position offset
     */
    public String jsxFunction_substringData(final int offset, final int count) {
        final DomCharacterData domCharacterData = (DomCharacterData) getDomNodeOrDie();
        return domCharacterData.substringData(offset, count);
    }
}
