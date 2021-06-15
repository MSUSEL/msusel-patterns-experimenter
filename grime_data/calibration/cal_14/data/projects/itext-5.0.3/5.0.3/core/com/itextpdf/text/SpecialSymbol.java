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
package com.itextpdf.text;

import com.itextpdf.text.Font.FontFamily;

/**
 * This class contains the symbols that correspond with special symbols.
 * <P>
 * When you construct a <CODE>Phrase</CODE> with Phrase.getInstance using a <CODE>String</CODE>,
 * this <CODE>String</CODE> can contain special Symbols. These are characters with an int value
 * between 913 and 937 (except 930) and between 945 and 969. With this class the value of the
 * corresponding character of the Font Symbol, can be retrieved.
 *
 * @see		Phrase
 *
 * @author  Bruno Lowagie
 * @author  Evelyne De Cordier
 */

public class SpecialSymbol {
    
	/**
	 * Returns the first occurrence of a special symbol in a <CODE>String</CODE>.
	 *
	 * @param	string		a <CODE>String</CODE>
	 * @return	an index of -1 if no special symbol was found
	 */
    public static int index(String string) {
        int length = string.length();
        for (int i = 0; i < length; i++) {
            if (getCorrespondingSymbol(string.charAt(i)) != ' ') {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Gets a chunk with a symbol character.
     * @param c a character that has to be changed into a symbol
     * @param font Font if there is no SYMBOL character corresponding with c
     * @return a SYMBOL version of a character
     */
    public static Chunk get(char c, Font font) {
        char greek = SpecialSymbol.getCorrespondingSymbol(c);
        if (greek == ' ') {
            return new Chunk(String.valueOf(c), font);
        }
        Font symbol = new Font(FontFamily.SYMBOL, font.getSize(), font.getStyle(), font.getColor());
        String s = String.valueOf(greek);
        return new Chunk(s, symbol);
    }
    
    /**
     * Looks for the corresponding symbol in the font Symbol.
     *
     * @param	c	the original ASCII-char
     * @return	the corresponding symbol in font Symbol
     */
    public static char getCorrespondingSymbol(char c) {
        switch(c) {
            case 913:
                return 'A'; // ALFA
            case 914:
                return 'B'; // BETA
            case 915:
                return 'G'; // GAMMA
            case 916:
                return 'D'; // DELTA
            case 917:
                return 'E'; // EPSILON
            case 918:
                return 'Z'; // ZETA
            case 919:
                return 'H'; // ETA
            case 920:
                return 'Q'; // THETA
            case 921:
                return 'I'; // IOTA
            case 922:
                return 'K'; // KAPPA
            case 923:
                return 'L'; // LAMBDA
            case 924:
                return 'M'; // MU
            case 925:
                return 'N'; // NU
            case 926:
                return 'X'; // XI
            case 927:
                return 'O'; // OMICRON
            case 928:
                return 'P'; // PI
            case 929:
                return 'R'; // RHO
            case 931:
                return 'S'; // SIGMA
            case 932:
                return 'T'; // TAU
            case 933:
                return 'U'; // UPSILON
            case 934:
                return 'F'; // PHI
            case 935:
                return 'C'; // CHI
            case 936:
                return 'Y'; // PSI
            case 937:
                return 'W'; // OMEGA
            case 945:
                return 'a'; // alfa
            case 946:
                return 'b'; // beta
            case 947:
                return 'g'; // gamma
            case 948:
                return 'd'; // delta
            case 949:
                return 'e'; // epsilon
            case 950:
                return 'z'; // zeta
            case 951:
                return 'h'; // eta
            case 952:
                return 'q'; // theta
            case 953:
                return 'i'; // iota
            case 954:
                return 'k'; // kappa
            case 955:
                return 'l'; // lambda
            case 956:
                return 'm'; // mu
            case 957:
                return 'n'; // nu
            case 958:
                return 'x'; // xi
            case 959:
                return 'o'; // omicron
            case 960:
                return 'p'; // pi
            case 961:
                return 'r'; // rho
            case 962:
                return 'V'; // sigma
            case 963:
                return 's'; // sigma
            case 964:
                return 't'; // tau
            case 965:
                return 'u'; // upsilon
            case 966:
                return 'f'; // phi
            case 967:
                return 'c'; // chi
            case 968:
                return 'y'; // psi
            case 969:
                return 'w'; // omega
                default:
                    return ' ';
        }
    }
}