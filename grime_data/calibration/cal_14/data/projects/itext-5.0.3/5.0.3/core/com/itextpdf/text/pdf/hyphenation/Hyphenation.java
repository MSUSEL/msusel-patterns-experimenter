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

/**
 * This class represents a hyphenated word.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 */
public class Hyphenation {
    
    private int[] hyphenPoints;
    private String word;

    /**
     * number of hyphenation points in word
     */
    private int len;

    /**
     * rawWord as made of alternating strings and {@link Hyphen Hyphen}
     * instances
     */
    Hyphenation(String word, int[] points) {
        this.word = word;
        hyphenPoints = points;
        len = points.length;
    }

    /**
     * @return the number of hyphenation points in the word
     */
    public int length() {
        return len;
    }

    /**
     * @return the pre-break text, not including the hyphen character
     */
    public String getPreHyphenText(int index) {
        return word.substring(0, hyphenPoints[index]);
    }

    /**
     * @return the post-break text
     */
    public String getPostHyphenText(int index) {
        return word.substring(hyphenPoints[index]);
    }

    /**
     * @return the hyphenation points
     */
    public int[] getHyphenationPoints() {
        return hyphenPoints;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        int start = 0;
        for (int i = 0; i < len; i++) {
            str.append(word.substring(start, hyphenPoints[i])).append('-');
            start = hyphenPoints[i];
        }
        str.append(word.substring(start));
        return str.toString();
    }

}
