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
package com.itextpdf.text.pdf.hyphenation;

import java.util.ArrayList;

/**
 * This interface is used to connect the XML pattern file parser to
 * the hyphenation tree.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 */
public interface PatternConsumer {

    /**
     * Add a character class.
     * A character class defines characters that are considered
     * equivalent for the purpose of hyphenation (e.g. "aA"). It
     * usually means to ignore case.
     * @param chargroup character group
     */
    void addClass(String chargroup);

    /**
     * Add a hyphenation exception. An exception replaces the
     * result obtained by the algorithm for cases for which this
     * fails or the user wants to provide his own hyphenation.
     * A hyphenatedword is a vector of alternating String's and
     * {@link Hyphen Hyphen} instances
     */
    void addException(String word, ArrayList<Object> hyphenatedword);

    /**
     * Add hyphenation patterns.
     * @param pattern the pattern
     * @param values interletter values expressed as a string of
     * digit characters.
     */
    void addPattern(String pattern, String values);

}
