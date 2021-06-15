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
package com.itextpdf.text.html;

import com.itextpdf.text.Element;
import com.itextpdf.text.BaseColor;

/**
 * This class converts a <CODE>String</CODE> to the HTML-format of a String.
 * <P>
 * To convert the <CODE>String</CODE>, each character is examined:
 * <UL>
 * <LI>ASCII-characters from 000 till 031 are represented as &amp;#xxx;<BR>
 *     (with xxx = the value of the character)
 * <LI>ASCII-characters from 032 t/m 127 are represented by the character itself, except for:
 *     <UL>
 *     <LI>'\n'	becomes &lt;BR&gt;\n
 *     <LI>&quot; becomes &amp;quot;
 *     <LI>&amp; becomes &amp;amp;
 *     <LI>&lt; becomes &amp;lt;
 *     <LI>&gt; becomes &amp;gt;
 *     </UL>
 * <LI>ASCII-characters from 128 till 255 are represented as &amp;#xxx;<BR>
 *     (with xxx = the value of the character)
 * </UL>
 * <P>
 * Example:
 * <P><BLOCKQUOTE><PRE>
 *    String htmlPresentation = HtmlEncoder.encode("Marie-Th&#233;r&#232;se S&#248;rensen");
 * </PRE></BLOCKQUOTE><P>
 * for more info: see O'Reilly; "HTML: The Definitive Guide" (page 164)
 *
 * @author  mario.maccarini@ugent.be
 */

public final class HtmlEncoder {
    
    // membervariables
    
/** List with the HTML translation of all the characters. */
    private static final String[] htmlCode = new String[256];
    
    static {
        for (int i = 0; i < 10; i++) {
            htmlCode[i] = "&#00" + i + ";";
        }
        
        for (int i = 10; i < 32; i++) {
            htmlCode[i] = "&#0" + i + ";";
        }
        
        for (int i = 32; i < 128; i++) {
            htmlCode[i] = String.valueOf((char)i);
        }
        
        // Special characters
        htmlCode['\t'] = "\t";
        htmlCode['\n'] = "<" + HtmlTags.NEWLINE + " />\n";
        htmlCode['\"'] = "&quot;"; // double quote
        htmlCode['&'] = "&amp;"; // ampersand
        htmlCode['<'] = "&lt;"; // lower than
        htmlCode['>'] = "&gt;"; // greater than
        
        for (int i = 128; i < 256; i++) {
            htmlCode[i] = "&#" + i + ";";
        }
    }
    
    
    // constructors
    
/**
 * This class will never be constructed.
 * <P>
 * HtmlEncoder only contains static methods.
 */
    
    private HtmlEncoder () { }
    
    // methods
    
/**
 * Converts a <CODE>String</CODE> to the HTML-format of this <CODE>String</CODE>.
 *
 * @param	string	The <CODE>String</CODE> to convert
 * @return	a <CODE>String</CODE>
 */
    
    public static String encode(String string) {
        int n = string.length();
        char character;
        StringBuffer buffer = new StringBuffer();
        // loop over all the characters of the String.
        for (int i = 0; i < n; i++) {
            character = string.charAt(i);
            // the Htmlcode of these characters are added to a StringBuffer one by one
            if (character < 256) {
                buffer.append(htmlCode[character]);
            }
            else {
                // Improvement posted by Joachim Eyrich
                buffer.append("&#").append((int)character).append(';');
            }
        }
        return buffer.toString();
    }
    
/**
 * Converts a <CODE>BaseColor</CODE> into a HTML representation of this <CODE>BaseColor</CODE>.
 *
 * @param	color	the <CODE>BaseColor</CODE> that has to be converted.
 * @return	the HTML representation of this <COLOR>BaseColor</COLOR>
 */
    
    public static String encode(BaseColor color) {
        StringBuffer buffer = new StringBuffer("#");
        if (color.getRed() < 16) {
            buffer.append('0');
        }
        buffer.append(Integer.toString(color.getRed(), 16));
        if (color.getGreen() < 16) {
            buffer.append('0');
        }
        buffer.append(Integer.toString(color.getGreen(), 16));
        if (color.getBlue() < 16) {
            buffer.append('0');
        }
        buffer.append(Integer.toString(color.getBlue(), 16));
        return buffer.toString();
    }
    
/**
 * Translates the alignment value.
 *
 * @param   alignment   the alignment value
 * @return  the translated value
 */
    
    public static String getAlignment(int alignment) {
        switch(alignment) {
            case Element.ALIGN_LEFT:
                return HtmlTags.ALIGN_LEFT;
            case Element.ALIGN_CENTER:
                return HtmlTags.ALIGN_CENTER;
            case Element.ALIGN_RIGHT:
                return HtmlTags.ALIGN_RIGHT;
            case Element.ALIGN_JUSTIFIED:
            case Element.ALIGN_JUSTIFIED_ALL:
                return HtmlTags.ALIGN_JUSTIFIED;
            case Element.ALIGN_TOP:
                return HtmlTags.ALIGN_TOP;
            case Element.ALIGN_MIDDLE:
                return HtmlTags.ALIGN_MIDDLE;
            case Element.ALIGN_BOTTOM:
                return HtmlTags.ALIGN_BOTTOM;
            case Element.ALIGN_BASELINE:
                return HtmlTags.ALIGN_BASELINE;
                default:
                    return "";
        }
    }
}