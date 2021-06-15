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
package org.hibernate.transform;


/**
 * A ResultTransformer that operates on "well-defined" and consistent
 * subset of a tuple's elements.
 *
 * "Well-defined" means that:
 * <ol>
 *     <li>
 *         the indexes of tuple elements accessed by a
 *         TupleSubsetResultTransformer depends only on the aliases
 *         and the number of elements in the tuple; i.e, it does
 *         not depend on the value of the tuple being transformed;
 *     </li>
 *     <li>
 *         any tuple elements included in the transformed value are
 *         unmodified by the transformation;
 *     </li>
 *     <li>
 *         transforming equivalent tuples with the same aliases multiple
 *         times results in transformed values that are equivalent;
 *     </li>
 *     <li>
 *         the result of transforming the tuple subset (only those
 *         elements accessed by the transformer) using only the
 *         corresponding aliases is equivalent to transforming the
 *         full tuple with the full array of aliases;
 *     </li>
 *     <li>
 *         the result of transforming a tuple with non-accessed tuple
 *         elements and corresponding aliases set to null
 *         is equivalent to transforming the full tuple with the
 *         full array of aliases;
 *     </li>
 * </ol>
 *
 * @author Gail Badner
 */
public interface TupleSubsetResultTransformer extends ResultTransformer {
	/**
	 * When a tuple is transformed, is the result a single element of the tuple?
	 *
	 * @param aliases - the aliases that correspond to the tuple
	 * @param tupleLength - the number of elements in the tuple
	 * @return true, if the transformed value is a single element of the tuple;
	 *         false, otherwise.
	 */
	boolean isTransformedValueATupleElement(String[] aliases, int tupleLength);

	/**
	 * Returns an array with the i-th element indicating whether the i-th
	 * element of the tuple is included in the transformed value.
	 *
	 * @param aliases - the aliases that correspond to the tuple
	 * @param tupleLength - the number of elements in the tuple
	 * @return array with the i-th element indicating whether the i-th
	 *         element of the tuple is included in the transformed value.
	 */
	boolean[] includeInTransform(String[] aliases, int tupleLength);
}