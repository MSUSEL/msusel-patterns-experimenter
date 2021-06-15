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
package org.hibernate.mapping;


/**
 * @author max
 *
 */
public interface ValueVisitor {

	/**
	 * @param bag
	 */
	Object accept(Bag bag);

	/**
	 * @param bag
	 */
	Object accept(IdentifierBag bag);

	/**
	 * @param list
	 */
	Object accept(List list);
	
	Object accept(PrimitiveArray primitiveArray);
	Object accept(Array list);

	/**
	 * @param map
	 */
	Object accept(Map map);

	/**
	 * @param many
	 */
	Object accept(OneToMany many);

	/**
	 * @param set
	 */
	Object accept(Set set);

	/**
	 * @param any
	 */
	Object accept(Any any);

	/**
	 * @param value
	 */
	Object accept(SimpleValue value);
	Object accept(DependantValue value);
	
	Object accept(Component component);
	
	Object accept(ManyToOne mto);
	Object accept(OneToOne oto);
	

}
