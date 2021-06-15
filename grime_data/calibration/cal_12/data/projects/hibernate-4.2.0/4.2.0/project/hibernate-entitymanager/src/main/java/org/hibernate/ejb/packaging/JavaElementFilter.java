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
package org.hibernate.ejb.packaging;


/**
 * Filter a Java element (class or package per fully qualified name and annotation existence)
 * At least 1 annotation has to annotate the element and the accept method must match
 * If none annotations are passed, only the accept method must pass.
 *
 * @author Emmanuel Bernard
 */
public abstract class JavaElementFilter extends Filter {
	private Class[] annotations;

	/**
	 * @param retrieveStream Give back an open stream to the matching element or not
	 * @param annotations	Array of annotations that must be present to match (1 of them should annotate the element
	 */
	protected JavaElementFilter(boolean retrieveStream, Class[] annotations) {
		super( retrieveStream );
		this.annotations = annotations == null ? new Class[]{} : annotations;
	}

	public Class[] getAnnotations() {
		return annotations;
	}

	/**
	 * Return true if the fully qualified name match
	 */
	public abstract boolean accept(String javaElementName);
}