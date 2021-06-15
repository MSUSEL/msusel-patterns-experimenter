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
package org.hibernate.exception.spi;

/**
 * Knows how to extract a violated constraint name from an error message based on the
 * fact that the constraint name is templated within the message.
 *
 * @author Steve Ebersole
 */
public abstract class TemplatedViolatedConstraintNameExtracter implements ViolatedConstraintNameExtracter {

	/**
	 * Extracts the constraint name based on a template (i.e., <i>templateStart</i><b>constraintName</b><i>templateEnd</i>).
	 *
	 * @param templateStart The pattern denoting the start of the constraint name within the message.
	 * @param templateEnd   The pattern denoting the end of the constraint name within the message.
	 * @param message       The templated error message containing the constraint name.
	 * @return The found constraint name, or null.
	 */
	protected String extractUsingTemplate(String templateStart, String templateEnd, String message) {
		int templateStartPosition = message.indexOf( templateStart );
		if ( templateStartPosition < 0 ) {
			return null;
		}

		int start = templateStartPosition + templateStart.length();
		int end = message.indexOf( templateEnd, start );
		if ( end < 0 ) {
			end = message.length();
		}

		return message.substring( start, end );
	}

}
