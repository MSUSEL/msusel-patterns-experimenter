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
package com.itextpdf.text.factories;

import com.itextpdf.text.SpecialSymbol;

/**
 * This class can produce String combinations representing a number built with
 * Greek letters (from alpha to omega, then alpha alpha, alpha beta, alpha gamma).
 * We are aware of the fact that the original Greek numbering is different;
 * See http://www.cogsci.indiana.edu/farg/harry/lan/grknum.htm#ancient
 * but this isn't implemented yet; the main reason being the fact that we
 * need a font that has the obsolete Greek characters qoppa and sampi.
 *
 * @since 2.0.7 (was called GreekNumberFactory in earlier versions)
 */
public class GreekAlphabetFactory {
	/** 
	 * Changes an int into a lower case Greek letter combination.
	 * @param index the original number
	 * @return the letter combination
	 */
	public static final String getString(int index) {
		return getString(index, true);
	}
	
	/** 
	 * Changes an int into a lower case Greek letter combination.
	 * @param index the original number
	 * @return the letter combination
	 */
	public static final String getLowerCaseString(int index) {
		return getString(index);		
	}
	
	/** 
	 * Changes an int into a upper case Greek letter combination.
	 * @param index the original number
	 * @return the letter combination
	 */
	public static final String getUpperCaseString(int index) {
		return getString(index).toUpperCase();		
	}

	/** 
	 * Changes an int into a Greek letter combination.
	 * @param index the original number
	 * @return the letter combination
	 */
	public static final String getString(int index, boolean lowercase) {
		if (index < 1) return "";
	    index--;
	    	
	    int bytes = 1;
	    int start = 0;
	    int symbols = 24;  
	   	while(index >= symbols + start) {
	   		bytes++;
	   	    start += symbols;
	   		symbols *= 24;
	   	}
	   	      
	   	int c = index - start;
	   	char[] value = new char[bytes];
	   	while(bytes > 0) {
	   		bytes--;
	   		value[bytes] = (char)(c % 24);
	   		if (value[bytes] > 16) value[bytes]++;
	   		value[bytes] += (lowercase ? 945 : 913);
	   		value[bytes] = SpecialSymbol.getCorrespondingSymbol(value[bytes]);
	   		c /= 24;
	   	}
	   	
	   	return String.valueOf(value);
	}
	
	/**
	 * Test this class using this main method.
	 */
	public static void main(String[] args) {
		for (int i = 1; i < 1000; i++) {
			System.out.println(getString(i));
		}
	}
}
