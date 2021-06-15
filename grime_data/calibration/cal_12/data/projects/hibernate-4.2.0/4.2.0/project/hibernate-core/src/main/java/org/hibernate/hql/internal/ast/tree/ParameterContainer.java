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
import org.hibernate.param.ParameterSpecification;

/**
 * Currently this is needed in order to deal with {@link FromElement FromElements} which
 * contain "hidden" JDBC parameters from applying filters.
 * <p/>
 * Would love for this to go away, but that would require that Hibernate's
 * internal {@link org.hibernate.engine.internal.JoinSequence join handling} be able to either:<ul>
 * <li>render the same AST structures</li>
 * <li>render structures capable of being converted to these AST structures</li>
 * </ul>
 * <p/>
 * In the interim, this allows us to at least treat these "hidden" parameters properly which is
 * the most pressing need.
 *
 * @deprecated
 * @author Steve Ebersole
 */
public interface ParameterContainer {
	/**
	 * Set the renderable text of this node.
	 *
	 * @param text The renderable text
	 */
	public void setText(String text);

	/**
	 * Adds a parameter specification for a parameter encountered within this node.  We use the term 'embedded' here
	 * because of the fact that the parameter was simply encountered as part of the node's text; it does not exist
	 * as part of a subtree as it might in a true AST.
	 *
	 * @param specification The generated specification.
	 */
	public void addEmbeddedParameter(ParameterSpecification specification);

	/**
	 * Determine whether this node contains embedded parameters.  The implication is that
	 * {@link #getEmbeddedParameters()} is allowed to return null if this method returns false.
	 *
	 * @return True if this node contains embedded parameters; false otherwise.
	 */
	public boolean hasEmbeddedParameters();

	/**
	 * Retrieve all embedded parameter specifications.
	 *
	 * @return All embedded parameter specifications; may return null.
	 * @see #hasEmbeddedParameters()
	 */
	public ParameterSpecification[] getEmbeddedParameters();
}
