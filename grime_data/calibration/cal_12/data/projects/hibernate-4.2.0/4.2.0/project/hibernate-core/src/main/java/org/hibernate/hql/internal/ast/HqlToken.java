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
package org.hibernate.hql.internal.ast;


/**
 * A custom token class for the HQL grammar.
 * <p><i>NOTE:<i> This class must be public becuase it is instantiated by the ANTLR library.  Ignore any suggestions
 * by various code 'analyzers' about this class being package local.</p>
 */
public class HqlToken extends antlr.CommonToken {
	/**
	 * True if this token could be an identifier. *
	 */
	private boolean possibleID = false;
	/**
	 * The previous token type. *
	 */
	private int tokenType;

	/**
	 * Returns true if the token could be an identifier.
	 *
	 * @return True if the token could be interpreted as in identifier,
	 *         false if not.
	 */
	public boolean isPossibleID() {
		return possibleID;
	}

	/**
	 * Sets the type of the token, remembering the previous type.
	 *
	 * @param t The new token type.
	 */
	public void setType(int t) {
		this.tokenType = getType();
		super.setType( t );
	}

	/**
	 * Returns the previous token type.
	 *
	 * @return int - The old token type.
	 */
	private int getPreviousType() {
		return tokenType;
	}

	/**
	 * Set to true if this token can be interpreted as an identifier,
	 * false if not.
	 *
	 * @param possibleID True if this is a keyword/identifier, false if not.
	 */
	public void setPossibleID(boolean possibleID) {
		this.possibleID = possibleID;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return String - The debug string.
	 */
	public String toString() {
		return "[\""
				+ getText()
				+ "\",<" + getType() + "> previously: <" + getPreviousType() + ">,line="
				+ line + ",col="
				+ col + ",possibleID=" + possibleID + "]";
	}

}
