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
package org.hibernate.hql.internal.ast.tree;
import org.hibernate.type.Type;

/**
 * A base AST node for the intermediate tree.
 * User: josh
 * Date: Dec 6, 2003
 * Time: 10:29:14 AM
 */
public class SqlNode extends Node {
	/**
	 * The original text for the node, mostly for debugging.
	 */
	private String originalText;
	/**
	 * The data type of this node.  Null for 'no type'.
	 */
	private Type dataType;

	public void setText(String s) {
		super.setText( s );
		if ( s != null && s.length() > 0 && originalText == null ) {
			originalText = s;
		}
	}

	public String getOriginalText() {
		return originalText;
	}

	public Type getDataType() {
		return dataType;
	}

	public void setDataType(Type dataType) {
		this.dataType = dataType;
	}

}
