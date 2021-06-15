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
import java.util.List;

/**
 * Much like {@link RootEntityResultTransformer}, but we also distinct
 * the entity in the final result.
 * <p/>
 * Since this transformer is stateless, all instances would be considered equal.
 * So for optimization purposes we limit it to a single, singleton {@link #INSTANCE instance}.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class DistinctRootEntityResultTransformer implements TupleSubsetResultTransformer {

	public static final DistinctRootEntityResultTransformer INSTANCE = new DistinctRootEntityResultTransformer();

	/**
	 * Disallow instantiation of DistinctRootEntityResultTransformer.
	 */
	private DistinctRootEntityResultTransformer() {
	}

	/**
	 * Simply delegates to {@link RootEntityResultTransformer#transformTuple}.
	 *
	 * @param tuple The tuple to transform
	 * @param aliases The tuple aliases
	 * @return The transformed tuple row.
	 */
	public Object transformTuple(Object[] tuple, String[] aliases) {
		return RootEntityResultTransformer.INSTANCE.transformTuple( tuple, aliases );
	}

	/**
	 * Simply delegates to {@link DistinctResultTransformer#transformList}.
	 *
	 * @param list The list to transform.
	 * @return The transformed List.
	 */
	public List transformList(List list) {
		return DistinctResultTransformer.INSTANCE.transformList( list );
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean[] includeInTransform(String[] aliases, int tupleLength) {
		return RootEntityResultTransformer.INSTANCE.includeInTransform( aliases, tupleLength );
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return RootEntityResultTransformer.INSTANCE.isTransformedValueATupleElement( null, tupleLength );
	}

	/**
	 * Serialization hook for ensuring singleton uniqueing.
	 *
	 * @return The singleton instance : {@link #INSTANCE}
	 */
	private Object readResolve() {
		return INSTANCE;
	}

}
