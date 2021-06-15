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
package com.jasml.compiler;

public interface Scannable {
	public final static int EOF = -1;

	public final static int Defualt = 0;

	public final static int Number_Integer = 1; // digit

	public final static int Number_Long = 2; // digit + 'L'

	public final static int Number_Float = 3; // digit + 'F'

	public final static int Number_Double = 4; // digit +'D'

	public final static int String = 5; // "^*"

	public final static int Char = 6; // '^'

	public final static int Attribute = 7;

	public final static int Bracket_Left = 8; // '{'

	public final static int Bracket_Right = 9; // '}'

	public final static int SBracket_Left = 18;// '('

	public final static int SBracket_Right = 19;// '('

	public final static int Colon = 10; // ':'

	public final static int Comma = 11; // ','

	public final static int Equal = 16; // '='

	public final static int Pointer = 17; // '-'

	public final static int WhiteSpace = 12;

	public final static int Instruction = 13;

	public final static int AccessFlag = 22;

	public final static int JavaName = 23;

	public final static int Number_Float_Negativ_Infinity = 41; // -Infinity, this is valid as Float value 

	public final static int Number_Float_Positive_Infinity = 42; // Infinity, this is valid as Float value

	public final static int Number_Float_NaN = 43; // NaNF, this is valid as Float value

	public final static int Number_Double_Negativ_Infinity = 41; // -Infinity, this is valid as Double value 

	public final static int Number_Double_Positive_Infinity = 42; // Infinity, this is valid as Double value

	public final static int Number_Double_NaN = 43; // NaNF, this is valid as Double value
}
