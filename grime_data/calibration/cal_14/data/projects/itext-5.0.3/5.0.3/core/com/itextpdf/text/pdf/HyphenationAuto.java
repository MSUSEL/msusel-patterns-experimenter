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
package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.hyphenation.Hyphenation;
import com.itextpdf.text.pdf.hyphenation.Hyphenator;

/** Hyphenates words automatically accordingly to the language and country.
 * The hyphenator engine was taken from FOP and uses the TEX patterns. If a language
 * is not provided and a TEX pattern for it exists, it can be easily adapted.
 *
 * @author Paulo Soares
 */
public class HyphenationAuto implements HyphenationEvent {

    /** The hyphenator engine.
     */    
    protected Hyphenator hyphenator;
    /** The second part of the hyphenated word.
     */    
    protected String post;
    
    /** Creates a new hyphenation instance usable in <CODE>Chunk</CODE>.
     * @param lang the language ("en" for English, for example)
     * @param country the country ("GB" for Great-Britain or "none" for no country, for example)
     * @param leftMin the minimum number of letters before the hyphen
     * @param rightMin the minimum number of letters after the hyphen
     */    
    public HyphenationAuto(String lang, String country, int leftMin, int rightMin) {
        hyphenator = new Hyphenator(lang, country, leftMin, rightMin);
    }

    /** Gets the hyphen symbol.
     * @return the hyphen symbol
     */    
    public String getHyphenSymbol() {
        return "-";
    }
    
    /** Hyphenates a word and returns the first part of it. To get
     * the second part of the hyphenated word call <CODE>getHyphenatedWordPost()</CODE>.
     * @param word the word to hyphenate
     * @param font the font used by this word
     * @param fontSize the font size used by this word
     * @param remainingWidth the width available to fit this word in
     * @return the first part of the hyphenated word including
     * the hyphen symbol, if any
     */    
    public String getHyphenatedWordPre(String word, BaseFont font, float fontSize, float remainingWidth) {
        post = word;
        String hyphen = getHyphenSymbol();
        float hyphenWidth = font.getWidthPoint(hyphen, fontSize);
        if (hyphenWidth > remainingWidth)
            return "";
        Hyphenation hyphenation = hyphenator.hyphenate(word);
        if (hyphenation == null) {
            return "";
        }
        int len = hyphenation.length();
        int k;
        for (k = 0; k < len; ++k) {
            if (font.getWidthPoint(hyphenation.getPreHyphenText(k), fontSize) + hyphenWidth > remainingWidth)
                break;
        }
        --k;
        if (k < 0)
            return "";
        post = hyphenation.getPostHyphenText(k);
        return hyphenation.getPreHyphenText(k) + hyphen;
    }
    
    /** Gets the second part of the hyphenated word. Must be called
     * after <CODE>getHyphenatedWordPre()</CODE>.
     * @return the second part of the hyphenated word
     */    
    public String getHyphenatedWordPost() {
        return post;
    }
    
}
